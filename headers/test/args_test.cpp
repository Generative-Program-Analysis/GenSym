#include <iostream>
#include <assert.h>
#include "../gensym.hpp"
#include <variant>

int main() {
  using std::variant, std::string;
  /* immer::flex_vector<unsigned> l1 {1,2,3}; */
  /* immer::flex_vector<unsigned> l2 {1,2,3}; */
  MatchResult l1 { {1u}, {"something"} };
  MatchResult l2 { {1u}, {"something"} };
  ASSERT((l1 == l2), "equal");

  MatchResult result;

  result = match_arg("abc#5def");
  ASSERT((result == MatchResult { {"abc"}, {5u}, {"def"} }),
        "no braces");

  result = match_arg("#{5}");
  ASSERT((result == MatchResult { {5u} }),
        "sym");

  result = match_arg("#{5}def");
  ASSERT((result == MatchResult { {5u}, {"def"} }),
        "sym + conc");

  result = match_arg("abc#{5}");
  ASSERT((result == MatchResult { {"abc"}, {5u} }),
        "conc + sym");

  result = match_arg("abc#{5}def");
  ASSERT((result == MatchResult { {"abc"}, {5u}, {"def"} }),
        "conc + sym + conc");

  result = match_arg("#{5}abc#{13}");
  ASSERT((result == MatchResult { {5u}, {"abc"}, {13u} }),
        "sym + conc + sym");

  result = match_arg("#{5}#{13}");
  ASSERT((result == MatchResult { {5u}, {13u} }),
        "sym + sym");

  result = match_arg("abc#!{5}def");
  ASSERT((result == MatchResult { {"abc#{5}def"} }),
        "escaping #");

  result = match_arg("abc#!!!{5}def");
  ASSERT((result == MatchResult { {"abc#!{5}def"} }),
        "escaping # and !");
}
