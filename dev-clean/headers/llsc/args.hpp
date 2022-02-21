#ifndef LLSC_ARGS_HEADERS
#define LLSC_ARGS_HEADERS

inline unsigned int args_var_name = 0;
inline PtrVal make_SymV_args(int bw = 8) {
  return make_SymV("args_x" + std::to_string(fs_var_name++), bw);
}
inline immer::flex_vector<immer::flex_vector<PtrVal>> cli_argv;
// pass to llsc_main
inline PtrVal g_argv;
inline PtrVal g_argc;

using MatchResult = List<std::variant<unsigned, std::string>>;

inline List<PtrVal> match_result_to_ptrvals(MatchResult mr) {
  immer::flex_vector_transient<PtrVal> vec;
  for (auto segment: mr) {
    if (std::holds_alternative<unsigned>(segment)) {
      auto length = std::get<unsigned>(segment);
      for (auto i = 0; i < length; ++i) {
        vec.push_back(make_SymV_args());
      }
    } else if (std::holds_alternative<std::string>(segment)) {
      auto content = std::get<std::string>(segment);
      for (auto c: content) {
        vec.push_back(make_IntV(c, 8));
      }
    }
  }
  return vec.persistent();
}

inline std::string escape_concrete(const std::string &raw) {
   auto content = raw;
   content = std::regex_replace(content, std::regex("#!"), "#");
   content = std::regex_replace(content, std::regex("!!"), "!");
   return content;
}

inline MatchResult match_arg(std::string argstring) {
  // the regex matching function
  std::smatch sm;
  std::string symarg (R"(#\{?([[:d:]]+)\}?)");
  std::regex re(symarg);

  std::regex_token_iterator<std::string::iterator> rend;
  std::regex_token_iterator<std::string::iterator> sym_lengths ( argstring.begin(), argstring.end(), re, 1 );

  std::regex_token_iterator<std::string::iterator> conc_contents ( argstring.begin(), argstring.end(), re, -1 );

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
  
  MatchResult result;
  while (conc_contents!=rend) {
    conc_content = escape_concrete(conc_contents->str());
    if (!conc_content.empty()) {
      result = result.push_back({conc_content});
    }
    conc_contents++;
    if (sym_lengths != rend) {
      /* TODO: set a bound on sym_length <2022-02-14, David Deng> */
      sym_length = stoi(sym_lengths->str());
      result = result.push_back({static_cast<unsigned>(sym_length)});
      sym_lengths++;
    }
  }
  return result;
}

inline immer::flex_vector<immer::flex_vector<PtrVal>> parse_args(std::string argstrings) {
  // split spaces
  std::stringstream ss(argstrings); 
  std::string s;
  immer::flex_vector<immer::flex_vector<PtrVal>> result;
  while (std::getline(ss, s, ' ')) { 
    if (s.empty()) {
      /* std::cout << "space matched. Skipping" << std::endl; */
      continue;
    }
    result = result.push_back(match_result_to_ptrvals(match_arg(s)).push_back(make_IntV(0, 8)));
  } 
  return result;
}

#endif
