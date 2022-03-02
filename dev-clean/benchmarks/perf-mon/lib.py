from flask import request
from urllib.parse import urljoin
from requests import Session
from fnmatch import fnmatchcase
import subprocess as subp
import datetime as dt
import functools
import logging
import shutil
import hmac
import jwt
import os


class Client:
    @staticmethod
    def url(base, *args):
        return urljoin("https://api.github.com/", base % args)

    def __init__(self):
        timestamp = int(dt.datetime.now().timestamp())
        payload = {
            "iat": timestamp - 60,
            "exp": timestamp + 600,
            "iss": int(os.environ["GITHUB_APP_IDENTIFIER"])
        }
        private_key = os.environb[b"GITHUB_PRIVATE_KEY"]
        token = jwt.encode(payload, private_key, algorithm="RS256")
        self.sess = Session()
        self.sess.headers["Authorization"] = "Bearer %s" % token

    def install(self, installid):
        url = self.url("/app/installations/%s/access_tokens", installid)
        rsp = self.sess.post(url).json()
        self.sess.headers["Authorization"] = "token %s" % rsp['token']
        return self
    
    def set_repo(self, repo):
        self.repo = repo
        return self

    @classmethod
    def from_payload(cls, payload):
        self = cls()
        if "installation" in payload:
            self.install(payload["installation"]["id"])
        if "repository" in payload:
            self.set_repo(payload["repository"]["full_name"])
        return self

    def create_check_run(self, **kwargs):
        assert "name" in kwargs and "head_sha" in kwargs
        url = self.url("/repos/%s/check-runs", self.repo)
        rsp = self.sess.post(url, json=kwargs)
        assert rsp.status_code == 201

    def list_check_runs(self, head_sha, **kwargs):
        url = self.url("/repos/%s/commits/%s/check-runs", self.repo, head_sha)
        rsp = self.sess.get(url, params=kwargs)
        assert rsp.status_code == 200
        return rsp.json()

    def update_check_run(self, checkid, **kwargs):
        assert "status" in kwargs or "conclusion" in kwargs
        url = self.url("/repos/%s/check-runs/%s", self.repo, checkid)
        rsp = self.sess.patch(url, json=kwargs)
        assert rsp.status_code == 200


class Handler:
    def __init__(self):
        self.handlers = []

    def register(self, func=None, **kwargs):
        if func is None:
            return functools.partial(self.register, **kwargs)
        self.handlers.append((kwargs, func))
        return func

    def dispatch(self, payload):
        def long_get(data, keys):
            try:
                for k in keys:
                    data = data[k]
                return data
            except KeyError:
                return

        def match(attrs):
            for k, v in attrs.items():
                cur = long_get(payload, k.split("__"))
                if not cur or not fnmatchcase(cur, v):
                    return False
            return True

        for attrs, func in self.handlers:
            if match(attrs):
                ret = func()
                if ret:
                    return ret


def verify_signature(func):
    key = os.environb[b"GITHUB_WEBHOOK_SECRET"]
    @functools.wraps(func)
    def wrapped(*args, **kwargs):
        ref = request.headers["X-Hub-Signature-256"]
        std = "sha256=" + hmac.new(key, request.data, "sha256").hexdigest()
        if not hmac.compare_digest(std, ref):
            return { "bye": "mars" }
        return func(*args, **kwargs)
    return wrapped


def do_check_run(payload):
    os.chdir(os.path.dirname(__file__))
    subp.run("git pull --quiet --prune --recurse-submodules".split())
    subp.run(["git", "checkout", payload["check_run"]["head_sha"]])
    subp.run(["make"])

    os.chdir("../..")
    subp.run(["sbt", "Bench / test"])
    dstfile = os.path.expanduser("~/.www/benchllsc/bench.csv")
    with open("bench.csv", "r") as f1, open(dstfile, "a") as f2:
        shutil.copyfileobj(f1, f2)
    os.remove("bench.csv")

    os.chdir(os.path.dirname(dstfile))
    cmd = "jupyter nbconvert --to html --execute {0}.ipynb --out {0}.html"
    subp.run(cmd.format("dataprocess").split())

    client = Client.from_payload(payload)
    client.update_check_run(payload["check_run"]["id"], conclusion="success")
