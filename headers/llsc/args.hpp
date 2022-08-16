#ifndef LLSC_ARGS_HEADERS
#define LLSC_ARGS_HEADERS

/* Auxiliary functions to process command line argv passed to the application */

inline immer::flex_vector<immer::flex_vector<PtrVal>> cli_argv;
// the argv and argc value passed to llsc_main
inline PtrVal g_argv;
inline PtrVal g_argc;
inline int conc_g_argc = 0;
inline char** conc_g_argv = nullptr;

using ArgTy = std::variant<unsigned, std::string>;

inline List<PtrVal> to_chars(List<ArgTy> mr) {
  return Vec::flatMap<PtrVal>(mr, [](const ArgTy& e) {
    if (isInstanceOf<unsigned>(e))
      return make_SymV_seq(std::get<unsigned>(e), "args", 8);
    if (isInstanceOf<std::string>(e))
      return Value::from_string(std::get<std::string>(e));
    ABORT("Unknown cli argument type");
  });
}

inline std::string escape_concrete(const std::string &raw) {
   auto content = raw;
   content = std::regex_replace(content, std::regex("#!"), "#");
   content = std::regex_replace(content, std::regex("!!"), "!");
   return content;
}

inline List<ArgTy> match_arg(std::string argstring) {
  // the regex matching function
  std::smatch sm;
  std::string symarg (R"(#\{?([[:d:]]+)\}?)");
  std::regex re(symarg);

  std::regex_token_iterator<std::string::iterator> rend;
  std::regex_token_iterator<std::string::iterator> sym_lengths(argstring.begin(), argstring.end(), re, 1);
  std::regex_token_iterator<std::string::iterator> conc_contents(argstring.begin(), argstring.end(), re, -1);

  std::string conc_content; // store the concrete content
  int sym_length; // store the symbolic length

  /*
   * input            conc_contents       | sym_contents
   * abc    -->       "abc"               | _
   * #13    -->       ""                  | 13
   * #13#26 -->       "" ""               | 13 26
   * #13abc#26 -->    "" "abc"            | 13 26
   * abc#13 -->       "abc"               | 13
   * #13abc -->       "" "abc"            | 13
   * abc#13def -->    "abc" "def"         | 13
   */

  immer::flex_vector_transient<ArgTy> result;
  while (conc_contents != rend) {
    conc_content = escape_concrete(conc_contents->str());
    if (!conc_content.empty()) {
      result.push_back({conc_content});
    }
    conc_contents++;
    if (sym_lengths != rend) {
      /* TODO: set a bound on sym_length <2022-02-14, David Deng> */
      sym_length = stoi(sym_lengths->str());
      result.push_back({static_cast<unsigned>(sym_length)});
      sym_lengths++;
    }
  }
  return result.persistent();
}

inline List<List<PtrVal>> parse_args(std::string argstrings) {
  // split spaces
  std::stringstream ss(argstrings);
  std::string s;
  immer::flex_vector_transient<List<PtrVal>> result;
  while (std::getline(ss, s, ' ')) {
    if (!s.empty()) {
      result.push_back(to_chars(match_arg(s)).push_back(make_IntV(0, 8)));
    }
  }
  return result.persistent();
}

#endif
