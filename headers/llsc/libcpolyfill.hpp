std::monostate app_main(SS&, immer::flex_vector<PtrVal>, std::function<std::monostate(SS&, PtrVal)>);
inline std::monostate llsc_dummy(SS&, immer::flex_vector<PtrVal>, std::function<std::monostate(SS&, PtrVal)>) {
    return std::monostate{};
}