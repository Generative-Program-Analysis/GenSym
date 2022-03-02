from flask import Flask, request
from redis import Redis
import requests
import logging
import rq

from lib import *

app = Flask(__name__)
queue = rq.Queue(connection=Redis())


@app.route("/event_handler", methods=["POST"])
@verify_signature
def main_handler():
    payload = request.get_json()
    payload["event"] = request.headers["X-GitHub-Event"]
    client = Client.from_payload(payload)
    H = Handler()

    @H.register(check_suite__app__slug="sai-bench", action="rerequested")
    @H.register(check_run__app__slug="sai-bench", action="rerequested")
    @H.register(check_suite__app__slug="github-actions",
                check_suite__head_branch="master",
                check_suite__conclusion="success")
    def create_new_run():
        data = payload.get("check_run") or payload.get("check_suite")
        client.create_check_run(name="Performance Benchmark", head_sha=data["head_sha"])
        return { "result": "created" }

    @H.register(check_run__app__slug="sai-bench", action="created")
    def initiate_run():
        client.update_check_run(payload["check_run"]["id"], status="in_progress")
        env = { k: v for k, v in os.environ.items()
                if k.startswith("GITHUB_") or k == "PATH" }
        queue.enqueue(do_check_run, payload, env, job_timeout="10h")
        return { "result": "updated" }

    return H.dispatch(payload) or { "result": "unprocessed" }


if __name__ == "__main__":
    logging.basicConfig(level=logging.WARNING)
    app.run(host='0.0.0.0', port=12321, debug=False)
