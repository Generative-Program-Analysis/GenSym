#include <iostream>
#define IMPURE_STATE
#include "../gensym.hpp"

PtrVal intV_0 = make_IntV(0);
PtrVal intV_1 = make_IntV(1);
PtrVal intV_2 = make_IntV(2);
PtrVal intV_3 = make_IntV(3);
PtrVal intV_4 = make_IntV(4);
PtrVal intV_5 = make_IntV(5);
PtrVal intV_6 = make_IntV(6);
PtrVal intV_7 = make_IntV(7);
PtrVal intV_8 = make_IntV(8);
PtrVal intV_9 = make_IntV(9);
        
std::monostate test(int x0) {
/* test readAt */;
Ptr<File> x1 = std::make_shared<File>("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2});
immer::flex_vector<PtrVal> x2 = x1->content.drop(0);
immer::flex_vector<PtrVal> x3 = x2.take(2);
/* assertEq */;
ASSERT((x3 == immer::flex_vector<PtrVal>{intV_0, intV_1}), "readAt");
immer::flex_vector<PtrVal> x4 = x1->content.drop(1);
immer::flex_vector<PtrVal> x5 = x4.take(4);
/* assertEq */;
ASSERT((x5 == immer::flex_vector<PtrVal>{intV_1, intV_2}), "readAt with more bytes");
immer::flex_vector<PtrVal> x6 = x1->content.drop(0);
immer::flex_vector<PtrVal> x7 = x6.take(0);
/* assertEq */;
ASSERT((x7 == immer::flex_vector<PtrVal>{}), "readAt with no bytes");
/* test size */;
int x8 = std::make_shared<File>("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2})->content.size();
/* assertEq */;
ASSERT((x8 == 3), "size of non-empty file");
int x9 = std::make_shared<File>("B", 0)->content.size();
/* assertEq */;
ASSERT((x9 == 0), "size of an empty file");
/* test clear */;
Ptr<File> x10 = std::make_shared<File>("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2});
x10->content = immer::flex_vector<PtrVal>{};
int x11 = x10->content.size();
/* assertEq */;
ASSERT((x11 == 0), "clear should result in empty file");
/* test writeAt */;
Ptr<File> x12 = std::make_shared<File>("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2});
Ptr<File> x13 = std::make_shared<File>("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2});
Ptr<File> x14 = std::make_shared<File>("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2});
immer::flex_vector<PtrVal> x15 = immer::flex_vector<PtrVal>{intV_4};
// File.writeAt;
int x16 = x12->content.size();
int x17 = 5 - x16;
if (x17 > 0) {
immer::flex_vector<PtrVal> x18 = immer::flex_vector<PtrVal>(x17, intV_0);
immer::flex_vector<PtrVal> x19 = x12->content + x18;
x12->content = x19;
}
// File.writeAtNoFill;
immer::flex_vector<PtrVal> x20 = x12->content.take(5);
immer::flex_vector<PtrVal> x21 = x20 + x15;
int x22 = x15.size();
immer::flex_vector<PtrVal> x23 = x12->content.drop(5 + x22);
immer::flex_vector<PtrVal> x24 = x21 + x23;
x12->content = x24;
/* assertEq */;
ASSERT((x12->content == immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2, intV_0, intV_0, intV_4}), "write after the end of the file, a hole should be created");
immer::flex_vector<PtrVal> x25 = immer::flex_vector<PtrVal>{intV_4};
// File.writeAt;
int x26 = x13->content.size();
int x27 = 3 - x26;
if (x27 > 0) {
immer::flex_vector<PtrVal> x28 = immer::flex_vector<PtrVal>(x27, intV_0);
immer::flex_vector<PtrVal> x29 = x13->content + x28;
x13->content = x29;
}
// File.writeAtNoFill;
immer::flex_vector<PtrVal> x30 = x13->content.take(3);
immer::flex_vector<PtrVal> x31 = x30 + x25;
int x32 = x25.size();
immer::flex_vector<PtrVal> x33 = x13->content.drop(3 + x32);
immer::flex_vector<PtrVal> x34 = x31 + x33;
x13->content = x34;
immer::flex_vector<PtrVal> x35 = immer::flex_vector<PtrVal>{intV_4};
// File.writeAtNoFill;
immer::flex_vector<PtrVal> x36 = x14->content.take(3);
immer::flex_vector<PtrVal> x37 = x36 + x35;
int x38 = x35.size();
immer::flex_vector<PtrVal> x39 = x14->content.drop(3 + x38);
immer::flex_vector<PtrVal> x40 = x37 + x39;
x14->content = x40;
/* assertEq */;
ASSERT((x13->content == x14->content), "writeAt and writeAtNoFill should behave the same when not writing after the end");
/* test writeAtNoFill */;
Ptr<File> x41 = std::make_shared<File>("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2});
Ptr<File> x42 = std::make_shared<File>("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2});
Ptr<File> x43 = std::make_shared<File>("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2});
immer::flex_vector<PtrVal> x44 = immer::flex_vector<PtrVal>{intV_3, intV_4, intV_5};
// File.writeAtNoFill;
immer::flex_vector<PtrVal> x45 = x41->content.take(3);
immer::flex_vector<PtrVal> x46 = x45 + x44;
int x47 = x44.size();
immer::flex_vector<PtrVal> x48 = x41->content.drop(3 + x47);
immer::flex_vector<PtrVal> x49 = x46 + x48;
x41->content = x49;
/* assertEq */;
ASSERT((x41->content == immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2, intV_3, intV_4, intV_5}), "write at the end of a file");
immer::flex_vector<PtrVal> x50 = immer::flex_vector<PtrVal>{intV_3, intV_4, intV_5};
// File.writeAtNoFill;
immer::flex_vector<PtrVal> x51 = x42->content.take(2);
immer::flex_vector<PtrVal> x52 = x51 + x50;
int x53 = x50.size();
immer::flex_vector<PtrVal> x54 = x42->content.drop(2 + x53);
immer::flex_vector<PtrVal> x55 = x52 + x54;
x42->content = x55;
/* assertEq */;
ASSERT((x42->content == immer::flex_vector<PtrVal>{intV_0, intV_1, intV_3, intV_4, intV_5}), "write at the middle of a file, exceeding the end");
immer::flex_vector<PtrVal> x56 = immer::flex_vector<PtrVal>{intV_4};
// File.writeAtNoFill;
immer::flex_vector<PtrVal> x57 = x43->content.take(1);
immer::flex_vector<PtrVal> x58 = x57 + x56;
int x59 = x56.size();
immer::flex_vector<PtrVal> x60 = x43->content.drop(1 + x59);
immer::flex_vector<PtrVal> x61 = x58 + x60;
x43->content = x61;
/* assertEq */;
ASSERT((x43->content == immer::flex_vector<PtrVal>{intV_0, intV_4, intV_2}), "write at the middle of a file, not exceeding the end");
int64_t x62 = std::make_shared<Stream>(std::make_shared<File>("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2}))->cursor;
/* assertEq */;
ASSERT((x62 == 0), "cursor should default to 0");
/* testReadStatField */;
Ptr<File> x63 = std::make_shared<File>("A", 0);
x63->stat = immer::flex_vector<PtrVal>{make_IntV(0L, 8), make_IntV(1L, 8), make_IntV(2L, 8), make_IntV(3L, 8), make_IntV(4L, 8), make_IntV(5L, 8), make_IntV(6L, 8), make_IntV(7L, 8), make_IntV(8L, 8), make_IntV(9L, 8), make_IntV(10L, 8), make_IntV(11L, 8), make_IntV(12L, 8), make_IntV(13L, 8), make_IntV(14L, 8), make_IntV(15L, 8), make_IntV(16L, 8), make_IntV(17L, 8), make_IntV(18L, 8), make_IntV(19L, 8), make_IntV(20L, 8), make_IntV(21L, 8), make_IntV(22L, 8), make_IntV(23L, 8), make_IntV(24L, 8), make_IntV(25L, 8), make_IntV(26L, 8), make_IntV(27L, 8), make_IntV(28L, 8), make_IntV(29L, 8), make_IntV(30L, 8), make_IntV(31L, 8), make_IntV(32L, 8), make_IntV(33L, 8), make_IntV(34L, 8), make_IntV(35L, 8), make_IntV(36L, 8), make_IntV(37L, 8), make_IntV(38L, 8), make_IntV(39L, 8), make_IntV(40L, 8), make_IntV(41L, 8), make_IntV(42L, 8), make_IntV(43L, 8), make_IntV(44L, 8), make_IntV(45L, 8), make_IntV(46L, 8), make_IntV(47L, 8), make_IntV(48L, 8), make_IntV(49L, 8), make_IntV(50L, 8), make_IntV(51L, 8), make_IntV(52L, 8), make_IntV(53L, 8), make_IntV(54L, 8), make_IntV(55L, 8), make_IntV(56L, 8), make_IntV(57L, 8), make_IntV(58L, 8), make_IntV(59L, 8), make_IntV(60L, 8), make_IntV(61L, 8), make_IntV(62L, 8), make_IntV(63L, 8), make_IntV(64L, 8), make_IntV(65L, 8), make_IntV(66L, 8), make_IntV(67L, 8), make_IntV(68L, 8), make_IntV(69L, 8), make_IntV(70L, 8), make_IntV(71L, 8), make_IntV(72L, 8), make_IntV(73L, 8), make_IntV(74L, 8), make_IntV(75L, 8), make_IntV(76L, 8), make_IntV(77L, 8), make_IntV(78L, 8), make_IntV(79L, 8), make_IntV(80L, 8), make_IntV(81L, 8), make_IntV(82L, 8), make_IntV(83L, 8), make_IntV(84L, 8), make_IntV(85L, 8), make_IntV(86L, 8), make_IntV(87L, 8), make_IntV(88L, 8), make_IntV(89L, 8), make_IntV(90L, 8), make_IntV(91L, 8), make_IntV(92L, 8), make_IntV(93L, 8), make_IntV(94L, 8), make_IntV(95L, 8), make_IntV(96L, 8), make_IntV(97L, 8), make_IntV(98L, 8), make_IntV(99L, 8), make_IntV(100L, 8), make_IntV(101L, 8), make_IntV(102L, 8), make_IntV(103L, 8), make_IntV(104L, 8), make_IntV(105L, 8), make_IntV(106L, 8), make_IntV(107L, 8), make_IntV(108L, 8), make_IntV(109L, 8), make_IntV(110L, 8), make_IntV(111L, 8), make_IntV(112L, 8), make_IntV(113L, 8), make_IntV(114L, 8), make_IntV(115L, 8), make_IntV(116L, 8), make_IntV(117L, 8), make_IntV(118L, 8), make_IntV(119L, 8)};
immer::flex_vector<PtrVal> x64 = x63->stat.drop(24);
immer::flex_vector<PtrVal> x65 = x64.take(4);
/* assertEq */;
ASSERT((*Value::from_bytes(x65) == *Value::from_bytes(immer::flex_vector<PtrVal>{make_IntV(24L, 8), make_IntV(25L, 8), make_IntV(26L, 8), make_IntV(27L, 8)})), "testReadStatField");
/* testWriteStatField */;
x63->stat = immer::flex_vector<PtrVal>{make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8), make_IntV(-1L, 8)};
PtrVal x66 = make_IntV(-559038737L, 32);
immer::flex_vector<PtrVal> x67 = x66->to_bytes();
immer::flex_vector<PtrVal> x68 = x63->stat.take(24);
immer::flex_vector<PtrVal> x69 = x68 + x67;
int x70 = x67.size();
immer::flex_vector<PtrVal> x71 = x63->stat.drop(24 + x70);
immer::flex_vector<PtrVal> x72 = x69 + x71;
x63->stat = x72;
immer::flex_vector<PtrVal> x73 = x63->stat.drop(24);
immer::flex_vector<PtrVal> x74 = x73.take(4);
/* assertEq */;
ASSERT((*Value::from_bytes(x74) == *x66), "testWriteStatField");
/* testing ptrderef. deref shouldn't generate explicit 'any' typed variable */;
PtrVal x75 = make_IntV(3L, 32);
*x75;
*x75;
/* test stringops */;
int x76 = Str::split("hello world", " ").size();
/* assertEq */;
ASSERT((x76 == 2), "segment should have two elements");
int x77 = Str::split("another phrase that is longer", " ").size();
/* assertEq */;
ASSERT((x77 == 5), "segment should have five elements");
/* test stream copy constructor */;
Ptr<Stream> x78 = std::make_shared<Stream>(x63, O_RDONLY, 1L);
Ptr<Stream> x79 = Stream::shallow_copy(x78);
x78;
x78->cursor = 5L;
x78;
int64_t x80 = x78->cursor;
/* assertEq */;
ASSERT((x80 == 5L), "strm1 should be updated");
int64_t x81 = x79->cursor;
/* assertEq */;
ASSERT((x81 == 1L), "strm2 should not be updated");
/* test directory structure */;
FS x82 = FS();
Ptr<File> x83 = std::make_shared<File>("a", 0);
/* setFile */;
immer::flex_vector<String> x84 = Str::split("/a", "/");
immer::flex_vector<String> x85 = Vec::filter(x84, [&](auto x86) {
return x86.length() > 0;
});
int x87 = x85.size();
immer::flex_vector<String> x88 = x85.take(x87 - 1);
Ptr<File> x89 = Vec::foldLeft(x88, x82.root_file, [&](auto x90, auto x91) {
bool x92 = x90 == nullptr || ({
bool x93 = Map::contains(x90->children, x91);
!x93;
});
Ptr<File> x94 = x92 ? nullptr : ({
Ptr<File> x95 = x90->children.at(x91);
x95;
});
return x94;
});
String x96 = x85.back();
String x97 = x83->name;
/* assertEq */;
ASSERT((x96 == x97), "setFile name should equal to last segment");
if (x89 != nullptr) {
x83->parent = x89;
immer::map<String, Ptr<File>> x98 = x89->children.insert(std::make_pair(x97, x83));
x89->children = x98;
}
immer::flex_vector<PtrVal> x99 = x82.preferred_cex;
immer::flex_vector<PtrVal> x100 = x83->stat.drop(24);
immer::flex_vector<PtrVal> x101 = x100.take(4);
PtrVal x102 = Value::from_bytes(x101);
immer::flex_vector<PtrVal> x103 = x83->stat.drop(16);
immer::flex_vector<PtrVal> x104 = x103.take(8);
immer::flex_vector<PtrVal> x105 = x83->stat.drop(56);
immer::flex_vector<PtrVal> x106 = x105.take(8);
immer::flex_vector<PtrVal> x107 = x83->stat.drop(0);
immer::flex_vector<PtrVal> x108 = x107.take(8);
immer::flex_vector<PtrVal> x109 = x83->stat.drop(40);
immer::flex_vector<PtrVal> x110 = x109.take(8);
immer::flex_vector<PtrVal> x111 = x83->stat.drop(28);
immer::flex_vector<PtrVal> x112 = x111.take(4);
immer::flex_vector<PtrVal> x113 = x83->stat.drop(32);
immer::flex_vector<PtrVal> x114 = x113.take(4);
immer::flex_vector<PtrVal> x115 = Vec::filter(immer::flex_vector<PtrVal>{int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x102, make_IntV(~(S_IFMT | 0777), 32)), make_IntV(0L, 32)), int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x102, make_IntV(0777, 32)), make_IntV(0644, 32)), int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x102, make_IntV((int64_t)S_IFMT, 32)), make_IntV((int64_t)S_IFREG, 32)), int_op_2(iOP::op_eq, Value::from_bytes(x104), make_IntV(1L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x106), make_IntV(4096L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x108), make_IntV(2053L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x110), make_IntV(0L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x112), make_IntV(1000L, 32)), int_op_2(iOP::op_eq, Value::from_bytes(x114), make_IntV(1000L, 32))}, [&](auto x116) {
return !x116->is_conc();
});
immer::flex_vector<PtrVal> x117 = x99 + x115;
x82.preferred_cex = x117;
Ptr<File> x118 = std::make_shared<File>("b", 0);
/* setFile */;
immer::flex_vector<String> x119 = Str::split("/a/b", "/");
immer::flex_vector<String> x120 = Vec::filter(x119, [&](auto x121) {
return x121.length() > 0;
});
int x122 = x120.size();
immer::flex_vector<String> x123 = x120.take(x122 - 1);
Ptr<File> x124 = Vec::foldLeft(x123, x82.root_file, [&](auto x125, auto x126) {
bool x127 = x125 == nullptr || ({
bool x128 = Map::contains(x125->children, x126);
!x128;
});
Ptr<File> x129 = x127 ? nullptr : ({
Ptr<File> x130 = x125->children.at(x126);
x130;
});
return x129;
});
String x131 = x120.back();
String x132 = x118->name;
/* assertEq */;
ASSERT((x131 == x132), "setFile name should equal to last segment");
if (x124 != nullptr) {
x118->parent = x124;
immer::map<String, Ptr<File>> x133 = x124->children.insert(std::make_pair(x132, x118));
x124->children = x133;
}
immer::flex_vector<PtrVal> x134 = x82.preferred_cex;
immer::flex_vector<PtrVal> x135 = x118->stat.drop(24);
immer::flex_vector<PtrVal> x136 = x135.take(4);
PtrVal x137 = Value::from_bytes(x136);
immer::flex_vector<PtrVal> x138 = x118->stat.drop(16);
immer::flex_vector<PtrVal> x139 = x138.take(8);
immer::flex_vector<PtrVal> x140 = x118->stat.drop(56);
immer::flex_vector<PtrVal> x141 = x140.take(8);
immer::flex_vector<PtrVal> x142 = x118->stat.drop(0);
immer::flex_vector<PtrVal> x143 = x142.take(8);
immer::flex_vector<PtrVal> x144 = x118->stat.drop(40);
immer::flex_vector<PtrVal> x145 = x144.take(8);
immer::flex_vector<PtrVal> x146 = x118->stat.drop(28);
immer::flex_vector<PtrVal> x147 = x146.take(4);
immer::flex_vector<PtrVal> x148 = x118->stat.drop(32);
immer::flex_vector<PtrVal> x149 = x148.take(4);
immer::flex_vector<PtrVal> x150 = Vec::filter(immer::flex_vector<PtrVal>{int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x137, make_IntV(~(S_IFMT | 0777), 32)), make_IntV(0L, 32)), int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x137, make_IntV(0777, 32)), make_IntV(0644, 32)), int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x137, make_IntV((int64_t)S_IFMT, 32)), make_IntV((int64_t)S_IFREG, 32)), int_op_2(iOP::op_eq, Value::from_bytes(x139), make_IntV(1L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x141), make_IntV(4096L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x143), make_IntV(2053L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x145), make_IntV(0L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x147), make_IntV(1000L, 32)), int_op_2(iOP::op_eq, Value::from_bytes(x149), make_IntV(1000L, 32))}, [&](auto x151) {
return !x151->is_conc();
});
immer::flex_vector<PtrVal> x152 = x134 + x150;
x82.preferred_cex = x152;
Ptr<File> x153 = std::make_shared<File>("c", 0);
/* setFile */;
immer::flex_vector<String> x154 = Str::split("/a/b/c", "/");
immer::flex_vector<String> x155 = Vec::filter(x154, [&](auto x156) {
return x156.length() > 0;
});
int x157 = x155.size();
immer::flex_vector<String> x158 = x155.take(x157 - 1);
Ptr<File> x159 = Vec::foldLeft(x158, x82.root_file, [&](auto x160, auto x161) {
bool x162 = x160 == nullptr || ({
bool x163 = Map::contains(x160->children, x161);
!x163;
});
Ptr<File> x164 = x162 ? nullptr : ({
Ptr<File> x165 = x160->children.at(x161);
x165;
});
return x164;
});
String x166 = x155.back();
String x167 = x153->name;
/* assertEq */;
ASSERT((x166 == x167), "setFile name should equal to last segment");
if (x159 != nullptr) {
x153->parent = x159;
immer::map<String, Ptr<File>> x168 = x159->children.insert(std::make_pair(x167, x153));
x159->children = x168;
}
immer::flex_vector<PtrVal> x169 = x82.preferred_cex;
immer::flex_vector<PtrVal> x170 = x153->stat.drop(24);
immer::flex_vector<PtrVal> x171 = x170.take(4);
PtrVal x172 = Value::from_bytes(x171);
immer::flex_vector<PtrVal> x173 = x153->stat.drop(16);
immer::flex_vector<PtrVal> x174 = x173.take(8);
immer::flex_vector<PtrVal> x175 = x153->stat.drop(56);
immer::flex_vector<PtrVal> x176 = x175.take(8);
immer::flex_vector<PtrVal> x177 = x153->stat.drop(0);
immer::flex_vector<PtrVal> x178 = x177.take(8);
immer::flex_vector<PtrVal> x179 = x153->stat.drop(40);
immer::flex_vector<PtrVal> x180 = x179.take(8);
immer::flex_vector<PtrVal> x181 = x153->stat.drop(28);
immer::flex_vector<PtrVal> x182 = x181.take(4);
immer::flex_vector<PtrVal> x183 = x153->stat.drop(32);
immer::flex_vector<PtrVal> x184 = x183.take(4);
immer::flex_vector<PtrVal> x185 = Vec::filter(immer::flex_vector<PtrVal>{int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x172, make_IntV(~(S_IFMT | 0777), 32)), make_IntV(0L, 32)), int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x172, make_IntV(0777, 32)), make_IntV(0644, 32)), int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x172, make_IntV((int64_t)S_IFMT, 32)), make_IntV((int64_t)S_IFREG, 32)), int_op_2(iOP::op_eq, Value::from_bytes(x174), make_IntV(1L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x176), make_IntV(4096L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x178), make_IntV(2053L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x180), make_IntV(0L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x182), make_IntV(1000L, 32)), int_op_2(iOP::op_eq, Value::from_bytes(x184), make_IntV(1000L, 32))}, [&](auto x186) {
return !x186->is_conc();
});
immer::flex_vector<PtrVal> x187 = x169 + x185;
x82.preferred_cex = x187;
immer::flex_vector<String> x188 = Vec::filter(x154, [&](auto x189) {
return x189.length() > 0;
});
Ptr<File> x190 = Vec::foldLeft(x188, x82.root_file, [&](auto x191, auto x192) {
bool x193 = x191 == nullptr || ({
bool x194 = Map::contains(x191->children, x192);
!x194;
});
Ptr<File> x195 = x193 ? nullptr : ({
Ptr<File> x196 = x191->children.at(x192);
x196;
});
return x195;
});
/* assertNeq */;
ASSERT((!(x190 == nullptr)), "file should exist");
/* test full path */;
FS x197 = FS();
/* setFile */;
immer::flex_vector<String> x198 = Vec::filter(x84, [&](auto x199) {
return x199.length() > 0;
});
int x200 = x198.size();
immer::flex_vector<String> x201 = x198.take(x200 - 1);
Ptr<File> x202 = Vec::foldLeft(x201, x197.root_file, [&](auto x203, auto x204) {
bool x205 = x203 == nullptr || ({
bool x206 = Map::contains(x203->children, x204);
!x206;
});
Ptr<File> x207 = x205 ? nullptr : ({
Ptr<File> x208 = x203->children.at(x204);
x208;
});
return x207;
});
String x209 = x198.back();
/* assertEq */;
ASSERT((x209 == x97), "setFile name should equal to last segment");
if (x202 != nullptr) {
x83->parent = x202;
immer::map<String, Ptr<File>> x210 = x202->children.insert(std::make_pair(x97, x83));
x202->children = x210;
}
immer::flex_vector<PtrVal> x211 = x197.preferred_cex;
immer::flex_vector<PtrVal> x212 = x83->stat.drop(24);
immer::flex_vector<PtrVal> x213 = x212.take(4);
PtrVal x214 = Value::from_bytes(x213);
immer::flex_vector<PtrVal> x215 = x83->stat.drop(16);
immer::flex_vector<PtrVal> x216 = x215.take(8);
immer::flex_vector<PtrVal> x217 = x83->stat.drop(56);
immer::flex_vector<PtrVal> x218 = x217.take(8);
immer::flex_vector<PtrVal> x219 = x83->stat.drop(0);
immer::flex_vector<PtrVal> x220 = x219.take(8);
immer::flex_vector<PtrVal> x221 = x83->stat.drop(40);
immer::flex_vector<PtrVal> x222 = x221.take(8);
immer::flex_vector<PtrVal> x223 = x83->stat.drop(28);
immer::flex_vector<PtrVal> x224 = x223.take(4);
immer::flex_vector<PtrVal> x225 = x83->stat.drop(32);
immer::flex_vector<PtrVal> x226 = x225.take(4);
immer::flex_vector<PtrVal> x227 = Vec::filter(immer::flex_vector<PtrVal>{int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x214, make_IntV(~(S_IFMT | 0777), 32)), make_IntV(0L, 32)), int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x214, make_IntV(0777, 32)), make_IntV(0644, 32)), int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x214, make_IntV((int64_t)S_IFMT, 32)), make_IntV((int64_t)S_IFREG, 32)), int_op_2(iOP::op_eq, Value::from_bytes(x216), make_IntV(1L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x218), make_IntV(4096L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x220), make_IntV(2053L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x222), make_IntV(0L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x224), make_IntV(1000L, 32)), int_op_2(iOP::op_eq, Value::from_bytes(x226), make_IntV(1000L, 32))}, [&](auto x228) {
return !x228->is_conc();
});
immer::flex_vector<PtrVal> x229 = x211 + x227;
x197.preferred_cex = x229;
/* setFile */;
immer::flex_vector<String> x230 = Vec::filter(x119, [&](auto x231) {
return x231.length() > 0;
});
int x232 = x230.size();
immer::flex_vector<String> x233 = x230.take(x232 - 1);
Ptr<File> x234 = Vec::foldLeft(x233, x197.root_file, [&](auto x235, auto x236) {
bool x237 = x235 == nullptr || ({
bool x238 = Map::contains(x235->children, x236);
!x238;
});
Ptr<File> x239 = x237 ? nullptr : ({
Ptr<File> x240 = x235->children.at(x236);
x240;
});
return x239;
});
String x241 = x230.back();
/* assertEq */;
ASSERT((x241 == x132), "setFile name should equal to last segment");
if (x234 != nullptr) {
x118->parent = x234;
immer::map<String, Ptr<File>> x242 = x234->children.insert(std::make_pair(x132, x118));
x234->children = x242;
}
immer::flex_vector<PtrVal> x243 = x197.preferred_cex;
immer::flex_vector<PtrVal> x244 = x118->stat.drop(24);
immer::flex_vector<PtrVal> x245 = x244.take(4);
PtrVal x246 = Value::from_bytes(x245);
immer::flex_vector<PtrVal> x247 = x118->stat.drop(16);
immer::flex_vector<PtrVal> x248 = x247.take(8);
immer::flex_vector<PtrVal> x249 = x118->stat.drop(56);
immer::flex_vector<PtrVal> x250 = x249.take(8);
immer::flex_vector<PtrVal> x251 = x118->stat.drop(0);
immer::flex_vector<PtrVal> x252 = x251.take(8);
immer::flex_vector<PtrVal> x253 = x118->stat.drop(40);
immer::flex_vector<PtrVal> x254 = x253.take(8);
immer::flex_vector<PtrVal> x255 = x118->stat.drop(28);
immer::flex_vector<PtrVal> x256 = x255.take(4);
immer::flex_vector<PtrVal> x257 = x118->stat.drop(32);
immer::flex_vector<PtrVal> x258 = x257.take(4);
immer::flex_vector<PtrVal> x259 = Vec::filter(immer::flex_vector<PtrVal>{int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x246, make_IntV(~(S_IFMT | 0777), 32)), make_IntV(0L, 32)), int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x246, make_IntV(0777, 32)), make_IntV(0644, 32)), int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x246, make_IntV((int64_t)S_IFMT, 32)), make_IntV((int64_t)S_IFREG, 32)), int_op_2(iOP::op_eq, Value::from_bytes(x248), make_IntV(1L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x250), make_IntV(4096L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x252), make_IntV(2053L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x254), make_IntV(0L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x256), make_IntV(1000L, 32)), int_op_2(iOP::op_eq, Value::from_bytes(x258), make_IntV(1000L, 32))}, [&](auto x260) {
return !x260->is_conc();
});
immer::flex_vector<PtrVal> x261 = x243 + x259;
x197.preferred_cex = x261;
/* setFile */;
immer::flex_vector<String> x262 = Vec::filter(x154, [&](auto x263) {
return x263.length() > 0;
});
int x264 = x262.size();
immer::flex_vector<String> x265 = x262.take(x264 - 1);
Ptr<File> x266 = Vec::foldLeft(x265, x197.root_file, [&](auto x267, auto x268) {
bool x269 = x267 == nullptr || ({
bool x270 = Map::contains(x267->children, x268);
!x270;
});
Ptr<File> x271 = x269 ? nullptr : ({
Ptr<File> x272 = x267->children.at(x268);
x272;
});
return x271;
});
String x273 = x262.back();
/* assertEq */;
ASSERT((x273 == x167), "setFile name should equal to last segment");
if (x266 != nullptr) {
x153->parent = x266;
immer::map<String, Ptr<File>> x274 = x266->children.insert(std::make_pair(x167, x153));
x266->children = x274;
}
immer::flex_vector<PtrVal> x275 = x197.preferred_cex;
immer::flex_vector<PtrVal> x276 = x153->stat.drop(24);
immer::flex_vector<PtrVal> x277 = x276.take(4);
PtrVal x278 = Value::from_bytes(x277);
immer::flex_vector<PtrVal> x279 = x153->stat.drop(16);
immer::flex_vector<PtrVal> x280 = x279.take(8);
immer::flex_vector<PtrVal> x281 = x153->stat.drop(56);
immer::flex_vector<PtrVal> x282 = x281.take(8);
immer::flex_vector<PtrVal> x283 = x153->stat.drop(0);
immer::flex_vector<PtrVal> x284 = x283.take(8);
immer::flex_vector<PtrVal> x285 = x153->stat.drop(40);
immer::flex_vector<PtrVal> x286 = x285.take(8);
immer::flex_vector<PtrVal> x287 = x153->stat.drop(28);
immer::flex_vector<PtrVal> x288 = x287.take(4);
immer::flex_vector<PtrVal> x289 = x153->stat.drop(32);
immer::flex_vector<PtrVal> x290 = x289.take(4);
immer::flex_vector<PtrVal> x291 = Vec::filter(immer::flex_vector<PtrVal>{int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x278, make_IntV(~(S_IFMT | 0777), 32)), make_IntV(0L, 32)), int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x278, make_IntV(0777, 32)), make_IntV(0644, 32)), int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x278, make_IntV((int64_t)S_IFMT, 32)), make_IntV((int64_t)S_IFREG, 32)), int_op_2(iOP::op_eq, Value::from_bytes(x280), make_IntV(1L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x282), make_IntV(4096L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x284), make_IntV(2053L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x286), make_IntV(0L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x288), make_IntV(1000L, 32)), int_op_2(iOP::op_eq, Value::from_bytes(x290), make_IntV(1000L, 32))}, [&](auto x292) {
return !x292->is_conc();
});
immer::flex_vector<PtrVal> x293 = x275 + x291;
x197.preferred_cex = x293;
immer::flex_vector<String> x294 = Vec::filter(x154, [&](auto x295) {
return x295.length() > 0;
});
Ptr<File> x296 = Vec::foldLeft(x294, x197.root_file, [&](auto x297, auto x298) {
bool x299 = x297 == nullptr || ({
bool x300 = Map::contains(x297->children, x298);
!x300;
});
Ptr<File> x301 = x299 ? nullptr : ({
Ptr<File> x302 = x297->children.at(x298);
x302;
});
return x301;
});
Ptr<File> x303 = x296;
String x304 = x296->name;
while (x303->parent != nullptr) {
Ptr<File> x305 = x303->parent;
x303 = x305;
bool x306 = x305->parent == nullptr && x305->name == "/";
if (x306) x304 = "/" + x304;
 else x304 = x305->name + "/" + x304;
}
/* assertEq */;
ASSERT((x303->name == "/"), "Outermost ancestor should be named /");
/* assertEq */;
ASSERT((x304 == "/a/b/c"), "full path should be correct");
/* test isLeft */;
false;
ASSERT((true), "Left value should not be set");
/* test isRight */;
true;
ASSERT((true), "Right value should not be set");
/* test get value */;
/* assertEq */;
ASSERT((true), "Right value should be set");
/* assertEq */;
ASSERT((true), "assigning to a string should work");
/* test fs copy */;
FS x307 = FS();
/* setFile */;
immer::flex_vector<String> x308 = Vec::filter(x84, [&](auto x309) {
return x309.length() > 0;
});
int x310 = x308.size();
immer::flex_vector<String> x311 = x308.take(x310 - 1);
Ptr<File> x312 = Vec::foldLeft(x311, x307.root_file, [&](auto x313, auto x314) {
bool x315 = x313 == nullptr || ({
bool x316 = Map::contains(x313->children, x314);
!x316;
});
Ptr<File> x317 = x315 ? nullptr : ({
Ptr<File> x318 = x313->children.at(x314);
x318;
});
return x317;
});
String x319 = x308.back();
/* assertEq */;
ASSERT((x319 == x97), "setFile name should equal to last segment");
if (x312 != nullptr) {
x83->parent = x312;
immer::map<String, Ptr<File>> x320 = x312->children.insert(std::make_pair(x97, x83));
x312->children = x320;
}
immer::flex_vector<PtrVal> x321 = x307.preferred_cex;
immer::flex_vector<PtrVal> x322 = x83->stat.drop(24);
immer::flex_vector<PtrVal> x323 = x322.take(4);
PtrVal x324 = Value::from_bytes(x323);
immer::flex_vector<PtrVal> x325 = x83->stat.drop(16);
immer::flex_vector<PtrVal> x326 = x325.take(8);
immer::flex_vector<PtrVal> x327 = x83->stat.drop(56);
immer::flex_vector<PtrVal> x328 = x327.take(8);
immer::flex_vector<PtrVal> x329 = x83->stat.drop(0);
immer::flex_vector<PtrVal> x330 = x329.take(8);
immer::flex_vector<PtrVal> x331 = x83->stat.drop(40);
immer::flex_vector<PtrVal> x332 = x331.take(8);
immer::flex_vector<PtrVal> x333 = x83->stat.drop(28);
immer::flex_vector<PtrVal> x334 = x333.take(4);
immer::flex_vector<PtrVal> x335 = x83->stat.drop(32);
immer::flex_vector<PtrVal> x336 = x335.take(4);
immer::flex_vector<PtrVal> x337 = Vec::filter(immer::flex_vector<PtrVal>{int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x324, make_IntV(~(S_IFMT | 0777), 32)), make_IntV(0L, 32)), int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x324, make_IntV(0777, 32)), make_IntV(0644, 32)), int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x324, make_IntV((int64_t)S_IFMT, 32)), make_IntV((int64_t)S_IFREG, 32)), int_op_2(iOP::op_eq, Value::from_bytes(x326), make_IntV(1L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x328), make_IntV(4096L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x330), make_IntV(2053L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x332), make_IntV(0L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x334), make_IntV(1000L, 32)), int_op_2(iOP::op_eq, Value::from_bytes(x336), make_IntV(1000L, 32))}, [&](auto x338) {
return !x338->is_conc();
});
immer::flex_vector<PtrVal> x339 = x321 + x337;
x307.preferred_cex = x339;
/* setFile */;
immer::flex_vector<String> x340 = Vec::filter(x119, [&](auto x341) {
return x341.length() > 0;
});
int x342 = x340.size();
immer::flex_vector<String> x343 = x340.take(x342 - 1);
Ptr<File> x344 = Vec::foldLeft(x343, x307.root_file, [&](auto x345, auto x346) {
bool x347 = x345 == nullptr || ({
bool x348 = Map::contains(x345->children, x346);
!x348;
});
Ptr<File> x349 = x347 ? nullptr : ({
Ptr<File> x350 = x345->children.at(x346);
x350;
});
return x349;
});
String x351 = x340.back();
/* assertEq */;
ASSERT((x351 == x132), "setFile name should equal to last segment");
if (x344 != nullptr) {
x118->parent = x344;
immer::map<String, Ptr<File>> x352 = x344->children.insert(std::make_pair(x132, x118));
x344->children = x352;
}
immer::flex_vector<PtrVal> x353 = x307.preferred_cex;
immer::flex_vector<PtrVal> x354 = x118->stat.drop(24);
immer::flex_vector<PtrVal> x355 = x354.take(4);
PtrVal x356 = Value::from_bytes(x355);
immer::flex_vector<PtrVal> x357 = x118->stat.drop(16);
immer::flex_vector<PtrVal> x358 = x357.take(8);
immer::flex_vector<PtrVal> x359 = x118->stat.drop(56);
immer::flex_vector<PtrVal> x360 = x359.take(8);
immer::flex_vector<PtrVal> x361 = x118->stat.drop(0);
immer::flex_vector<PtrVal> x362 = x361.take(8);
immer::flex_vector<PtrVal> x363 = x118->stat.drop(40);
immer::flex_vector<PtrVal> x364 = x363.take(8);
immer::flex_vector<PtrVal> x365 = x118->stat.drop(28);
immer::flex_vector<PtrVal> x366 = x365.take(4);
immer::flex_vector<PtrVal> x367 = x118->stat.drop(32);
immer::flex_vector<PtrVal> x368 = x367.take(4);
immer::flex_vector<PtrVal> x369 = Vec::filter(immer::flex_vector<PtrVal>{int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x356, make_IntV(~(S_IFMT | 0777), 32)), make_IntV(0L, 32)), int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x356, make_IntV(0777, 32)), make_IntV(0644, 32)), int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x356, make_IntV((int64_t)S_IFMT, 32)), make_IntV((int64_t)S_IFREG, 32)), int_op_2(iOP::op_eq, Value::from_bytes(x358), make_IntV(1L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x360), make_IntV(4096L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x362), make_IntV(2053L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x364), make_IntV(0L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x366), make_IntV(1000L, 32)), int_op_2(iOP::op_eq, Value::from_bytes(x368), make_IntV(1000L, 32))}, [&](auto x370) {
return !x370->is_conc();
});
immer::flex_vector<PtrVal> x371 = x353 + x369;
x307.preferred_cex = x371;
Ptr<File> x372 = std::make_shared<File>("c", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2});
/* setFile */;
immer::flex_vector<String> x373 = Vec::filter(x154, [&](auto x374) {
return x374.length() > 0;
});
int x375 = x373.size();
immer::flex_vector<String> x376 = x373.take(x375 - 1);
Ptr<File> x377 = Vec::foldLeft(x376, x307.root_file, [&](auto x378, auto x379) {
bool x380 = x378 == nullptr || ({
bool x381 = Map::contains(x378->children, x379);
!x381;
});
Ptr<File> x382 = x380 ? nullptr : ({
Ptr<File> x383 = x378->children.at(x379);
x383;
});
return x382;
});
String x384 = x373.back();
String x385 = x372->name;
/* assertEq */;
ASSERT((x384 == x385), "setFile name should equal to last segment");
if (x377 != nullptr) {
x372->parent = x377;
immer::map<String, Ptr<File>> x386 = x377->children.insert(std::make_pair(x385, x372));
x377->children = x386;
}
immer::flex_vector<PtrVal> x387 = x307.preferred_cex;
immer::flex_vector<PtrVal> x388 = x372->stat.drop(24);
immer::flex_vector<PtrVal> x389 = x388.take(4);
PtrVal x390 = Value::from_bytes(x389);
immer::flex_vector<PtrVal> x391 = x372->stat.drop(16);
immer::flex_vector<PtrVal> x392 = x391.take(8);
immer::flex_vector<PtrVal> x393 = x372->stat.drop(56);
immer::flex_vector<PtrVal> x394 = x393.take(8);
immer::flex_vector<PtrVal> x395 = x372->stat.drop(0);
immer::flex_vector<PtrVal> x396 = x395.take(8);
immer::flex_vector<PtrVal> x397 = x372->stat.drop(40);
immer::flex_vector<PtrVal> x398 = x397.take(8);
immer::flex_vector<PtrVal> x399 = x372->stat.drop(28);
immer::flex_vector<PtrVal> x400 = x399.take(4);
immer::flex_vector<PtrVal> x401 = x372->stat.drop(32);
immer::flex_vector<PtrVal> x402 = x401.take(4);
immer::flex_vector<PtrVal> x403 = Vec::filter(immer::flex_vector<PtrVal>{int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x390, make_IntV(~(S_IFMT | 0777), 32)), make_IntV(0L, 32)), int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x390, make_IntV(0777, 32)), make_IntV(0644, 32)), int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x390, make_IntV((int64_t)S_IFMT, 32)), make_IntV((int64_t)S_IFREG, 32)), int_op_2(iOP::op_eq, Value::from_bytes(x392), make_IntV(1L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x394), make_IntV(4096L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x396), make_IntV(2053L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x398), make_IntV(0L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x400), make_IntV(1000L, 32)), int_op_2(iOP::op_eq, Value::from_bytes(x402), make_IntV(1000L, 32))}, [&](auto x404) {
return !x404->is_conc();
});
immer::flex_vector<PtrVal> x405 = x387 + x403;
x307.preferred_cex = x405;
/* make a copy */;
Ptr<File> x406 = File::deep_copy(x307.root_file);
immer::map<int, Ptr<Stream>> x407 = immer::map<int, Ptr<Stream>>({});
FS x408 = FS(x407, x406, x307);
Map::foreach(x307.opened_files, [&](auto x409, auto x410) {
Ptr<File> x411 = x410->file;
Ptr<File> x412 = x411;
String x413 = x411->name;
while (x412->parent != nullptr) {
Ptr<File> x414 = x412->parent;
x412 = x414;
bool x415 = x414->parent == nullptr && x414->name == "/";
if (x415) x413 = "/" + x413;
 else x413 = x414->name + "/" + x413;
}
/* assertEq */;
ASSERT((x412->name == "/"), "Outermost ancestor should be named /");
immer::flex_vector<String> x416 = Vec::filter(Str::split(x413, "/"), [&](auto x417) {
return x417.length() > 0;
});
Ptr<File> x418 = Vec::foldLeft(x416, x408.root_file, [&](auto x419, auto x420) {
bool x421 = x419 == nullptr || ({
bool x422 = Map::contains(x419->children, x420);
!x422;
});
Ptr<File> x423 = x421 ? nullptr : ({
Ptr<File> x424 = x419->children.at(x420);
x424;
});
return x423;
});
Ptr<Stream> x425 = std::make_shared<Stream>(x418, x410->mode, x410->cursor);
immer::map<int, Ptr<Stream>> x426 = x408.opened_files.insert(std::make_pair(x409, x425));
x408.opened_files = x426;
return std::monostate{};
});immer::flex_vector<String> x427 = Vec::filter(x154, [&](auto x428) {
return x428.length() > 0;
});
Ptr<File> x429 = Vec::foldLeft(x427, x408.root_file, [&](auto x430, auto x431) {
bool x432 = x430 == nullptr || ({
bool x433 = Map::contains(x430->children, x431);
!x433;
});
Ptr<File> x434 = x432 ? nullptr : ({
Ptr<File> x435 = x430->children.at(x431);
x435;
});
return x434;
});
/* assertNeq */;
ASSERT((!(x429 == nullptr)), "file should exist in the copied file system");
/* modify a file in the copied file system */;
immer::flex_vector<PtrVal> x436 = immer::flex_vector<PtrVal>{intV_3, intV_4, intV_5};
// File.writeAtNoFill;
immer::flex_vector<PtrVal> x437 = x429->content.take(3);
immer::flex_vector<PtrVal> x438 = x437 + x436;
int x439 = x436.size();
immer::flex_vector<PtrVal> x440 = x429->content.drop(3 + x439);
immer::flex_vector<PtrVal> x441 = x438 + x440;
x429->content = x441;
/* check that the original file is not modified */;
immer::flex_vector<String> x442 = Vec::filter(x154, [&](auto x443) {
return x443.length() > 0;
});
Ptr<File> x444 = Vec::foldLeft(x442, x307.root_file, [&](auto x445, auto x446) {
bool x447 = x445 == nullptr || ({
bool x448 = Map::contains(x445->children, x446);
!x448;
});
Ptr<File> x449 = x447 ? nullptr : ({
Ptr<File> x450 = x445->children.at(x446);
x450;
});
return x449;
});
/* assertEq */;
ASSERT((x444->content == immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2}), "file should not be modified");
/* add a file in the original system */;
Ptr<File> x451 = std::make_shared<File>("d", 0);
/* setFile */;
immer::flex_vector<String> x452 = Str::split("/a/b/d", "/");
immer::flex_vector<String> x453 = Vec::filter(x452, [&](auto x454) {
return x454.length() > 0;
});
int x455 = x453.size();
immer::flex_vector<String> x456 = x453.take(x455 - 1);
Ptr<File> x457 = Vec::foldLeft(x456, x307.root_file, [&](auto x458, auto x459) {
bool x460 = x458 == nullptr || ({
bool x461 = Map::contains(x458->children, x459);
!x461;
});
Ptr<File> x462 = x460 ? nullptr : ({
Ptr<File> x463 = x458->children.at(x459);
x463;
});
return x462;
});
String x464 = x453.back();
String x465 = x451->name;
/* assertEq */;
ASSERT((x464 == x465), "setFile name should equal to last segment");
if (x457 != nullptr) {
x451->parent = x457;
immer::map<String, Ptr<File>> x466 = x457->children.insert(std::make_pair(x465, x451));
x457->children = x466;
}
immer::flex_vector<PtrVal> x467 = x307.preferred_cex;
immer::flex_vector<PtrVal> x468 = x451->stat.drop(24);
immer::flex_vector<PtrVal> x469 = x468.take(4);
PtrVal x470 = Value::from_bytes(x469);
immer::flex_vector<PtrVal> x471 = x451->stat.drop(16);
immer::flex_vector<PtrVal> x472 = x471.take(8);
immer::flex_vector<PtrVal> x473 = x451->stat.drop(56);
immer::flex_vector<PtrVal> x474 = x473.take(8);
immer::flex_vector<PtrVal> x475 = x451->stat.drop(0);
immer::flex_vector<PtrVal> x476 = x475.take(8);
immer::flex_vector<PtrVal> x477 = x451->stat.drop(40);
immer::flex_vector<PtrVal> x478 = x477.take(8);
immer::flex_vector<PtrVal> x479 = x451->stat.drop(28);
immer::flex_vector<PtrVal> x480 = x479.take(4);
immer::flex_vector<PtrVal> x481 = x451->stat.drop(32);
immer::flex_vector<PtrVal> x482 = x481.take(4);
immer::flex_vector<PtrVal> x483 = Vec::filter(immer::flex_vector<PtrVal>{int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x470, make_IntV(~(S_IFMT | 0777), 32)), make_IntV(0L, 32)), int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x470, make_IntV(0777, 32)), make_IntV(0644, 32)), int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x470, make_IntV((int64_t)S_IFMT, 32)), make_IntV((int64_t)S_IFREG, 32)), int_op_2(iOP::op_eq, Value::from_bytes(x472), make_IntV(1L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x474), make_IntV(4096L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x476), make_IntV(2053L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x478), make_IntV(0L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x480), make_IntV(1000L, 32)), int_op_2(iOP::op_eq, Value::from_bytes(x482), make_IntV(1000L, 32))}, [&](auto x484) {
return !x484->is_conc();
});
immer::flex_vector<PtrVal> x485 = x467 + x483;
x307.preferred_cex = x485;
/* check that the copied file system is not modified */;
immer::flex_vector<String> x486 = Vec::filter(x452, [&](auto x487) {
return x487.length() > 0;
});
Ptr<File> x488 = Vec::foldLeft(x486, x408.root_file, [&](auto x489, auto x490) {
bool x491 = x489 == nullptr || ({
bool x492 = Map::contains(x489->children, x490);
!x492;
});
Ptr<File> x493 = x491 ? nullptr : ({
Ptr<File> x494 = x489->children.at(x490);
x494;
});
return x493;
});
/* assertEq */;
ASSERT((x488 == nullptr), "file should not be added to the copied file system");
/* test fs copy with stream */;
FS x495 = FS();
/* setFile */;
immer::flex_vector<String> x496 = Vec::filter(x84, [&](auto x497) {
return x497.length() > 0;
});
int x498 = x496.size();
immer::flex_vector<String> x499 = x496.take(x498 - 1);
Ptr<File> x500 = Vec::foldLeft(x499, x495.root_file, [&](auto x501, auto x502) {
bool x503 = x501 == nullptr || ({
bool x504 = Map::contains(x501->children, x502);
!x504;
});
Ptr<File> x505 = x503 ? nullptr : ({
Ptr<File> x506 = x501->children.at(x502);
x506;
});
return x505;
});
String x507 = x496.back();
/* assertEq */;
ASSERT((x507 == x97), "setFile name should equal to last segment");
if (x500 != nullptr) {
x83->parent = x500;
immer::map<String, Ptr<File>> x508 = x500->children.insert(std::make_pair(x97, x83));
x500->children = x508;
}
immer::flex_vector<PtrVal> x509 = x495.preferred_cex;
immer::flex_vector<PtrVal> x510 = x83->stat.drop(24);
immer::flex_vector<PtrVal> x511 = x510.take(4);
PtrVal x512 = Value::from_bytes(x511);
immer::flex_vector<PtrVal> x513 = x83->stat.drop(16);
immer::flex_vector<PtrVal> x514 = x513.take(8);
immer::flex_vector<PtrVal> x515 = x83->stat.drop(56);
immer::flex_vector<PtrVal> x516 = x515.take(8);
immer::flex_vector<PtrVal> x517 = x83->stat.drop(0);
immer::flex_vector<PtrVal> x518 = x517.take(8);
immer::flex_vector<PtrVal> x519 = x83->stat.drop(40);
immer::flex_vector<PtrVal> x520 = x519.take(8);
immer::flex_vector<PtrVal> x521 = x83->stat.drop(28);
immer::flex_vector<PtrVal> x522 = x521.take(4);
immer::flex_vector<PtrVal> x523 = x83->stat.drop(32);
immer::flex_vector<PtrVal> x524 = x523.take(4);
immer::flex_vector<PtrVal> x525 = Vec::filter(immer::flex_vector<PtrVal>{int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x512, make_IntV(~(S_IFMT | 0777), 32)), make_IntV(0L, 32)), int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x512, make_IntV(0777, 32)), make_IntV(0644, 32)), int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x512, make_IntV((int64_t)S_IFMT, 32)), make_IntV((int64_t)S_IFREG, 32)), int_op_2(iOP::op_eq, Value::from_bytes(x514), make_IntV(1L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x516), make_IntV(4096L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x518), make_IntV(2053L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x520), make_IntV(0L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x522), make_IntV(1000L, 32)), int_op_2(iOP::op_eq, Value::from_bytes(x524), make_IntV(1000L, 32))}, [&](auto x526) {
return !x526->is_conc();
});
immer::flex_vector<PtrVal> x527 = x509 + x525;
x495.preferred_cex = x527;
/* setFile */;
immer::flex_vector<String> x528 = Vec::filter(x119, [&](auto x529) {
return x529.length() > 0;
});
int x530 = x528.size();
immer::flex_vector<String> x531 = x528.take(x530 - 1);
Ptr<File> x532 = Vec::foldLeft(x531, x495.root_file, [&](auto x533, auto x534) {
bool x535 = x533 == nullptr || ({
bool x536 = Map::contains(x533->children, x534);
!x536;
});
Ptr<File> x537 = x535 ? nullptr : ({
Ptr<File> x538 = x533->children.at(x534);
x538;
});
return x537;
});
String x539 = x528.back();
/* assertEq */;
ASSERT((x539 == x132), "setFile name should equal to last segment");
if (x532 != nullptr) {
x118->parent = x532;
immer::map<String, Ptr<File>> x540 = x532->children.insert(std::make_pair(x132, x118));
x532->children = x540;
}
immer::flex_vector<PtrVal> x541 = x495.preferred_cex;
immer::flex_vector<PtrVal> x542 = x118->stat.drop(24);
immer::flex_vector<PtrVal> x543 = x542.take(4);
PtrVal x544 = Value::from_bytes(x543);
immer::flex_vector<PtrVal> x545 = x118->stat.drop(16);
immer::flex_vector<PtrVal> x546 = x545.take(8);
immer::flex_vector<PtrVal> x547 = x118->stat.drop(56);
immer::flex_vector<PtrVal> x548 = x547.take(8);
immer::flex_vector<PtrVal> x549 = x118->stat.drop(0);
immer::flex_vector<PtrVal> x550 = x549.take(8);
immer::flex_vector<PtrVal> x551 = x118->stat.drop(40);
immer::flex_vector<PtrVal> x552 = x551.take(8);
immer::flex_vector<PtrVal> x553 = x118->stat.drop(28);
immer::flex_vector<PtrVal> x554 = x553.take(4);
immer::flex_vector<PtrVal> x555 = x118->stat.drop(32);
immer::flex_vector<PtrVal> x556 = x555.take(4);
immer::flex_vector<PtrVal> x557 = Vec::filter(immer::flex_vector<PtrVal>{int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x544, make_IntV(~(S_IFMT | 0777), 32)), make_IntV(0L, 32)), int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x544, make_IntV(0777, 32)), make_IntV(0644, 32)), int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x544, make_IntV((int64_t)S_IFMT, 32)), make_IntV((int64_t)S_IFREG, 32)), int_op_2(iOP::op_eq, Value::from_bytes(x546), make_IntV(1L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x548), make_IntV(4096L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x550), make_IntV(2053L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x552), make_IntV(0L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x554), make_IntV(1000L, 32)), int_op_2(iOP::op_eq, Value::from_bytes(x556), make_IntV(1000L, 32))}, [&](auto x558) {
return !x558->is_conc();
});
immer::flex_vector<PtrVal> x559 = x541 + x557;
x495.preferred_cex = x559;
Ptr<File> x560 = std::make_shared<File>("c", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2});
/* setFile */;
immer::flex_vector<String> x561 = Vec::filter(x154, [&](auto x562) {
return x562.length() > 0;
});
int x563 = x561.size();
immer::flex_vector<String> x564 = x561.take(x563 - 1);
Ptr<File> x565 = Vec::foldLeft(x564, x495.root_file, [&](auto x566, auto x567) {
bool x568 = x566 == nullptr || ({
bool x569 = Map::contains(x566->children, x567);
!x569;
});
Ptr<File> x570 = x568 ? nullptr : ({
Ptr<File> x571 = x566->children.at(x567);
x571;
});
return x570;
});
String x572 = x561.back();
String x573 = x560->name;
/* assertEq */;
ASSERT((x572 == x573), "setFile name should equal to last segment");
if (x565 != nullptr) {
x560->parent = x565;
immer::map<String, Ptr<File>> x574 = x565->children.insert(std::make_pair(x573, x560));
x565->children = x574;
}
immer::flex_vector<PtrVal> x575 = x495.preferred_cex;
immer::flex_vector<PtrVal> x576 = x560->stat.drop(24);
immer::flex_vector<PtrVal> x577 = x576.take(4);
PtrVal x578 = Value::from_bytes(x577);
immer::flex_vector<PtrVal> x579 = x560->stat.drop(16);
immer::flex_vector<PtrVal> x580 = x579.take(8);
immer::flex_vector<PtrVal> x581 = x560->stat.drop(56);
immer::flex_vector<PtrVal> x582 = x581.take(8);
immer::flex_vector<PtrVal> x583 = x560->stat.drop(0);
immer::flex_vector<PtrVal> x584 = x583.take(8);
immer::flex_vector<PtrVal> x585 = x560->stat.drop(40);
immer::flex_vector<PtrVal> x586 = x585.take(8);
immer::flex_vector<PtrVal> x587 = x560->stat.drop(28);
immer::flex_vector<PtrVal> x588 = x587.take(4);
immer::flex_vector<PtrVal> x589 = x560->stat.drop(32);
immer::flex_vector<PtrVal> x590 = x589.take(4);
immer::flex_vector<PtrVal> x591 = Vec::filter(immer::flex_vector<PtrVal>{int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x578, make_IntV(~(S_IFMT | 0777), 32)), make_IntV(0L, 32)), int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x578, make_IntV(0777, 32)), make_IntV(0644, 32)), int_op_2(iOP::op_eq, int_op_2(iOP::op_and, x578, make_IntV((int64_t)S_IFMT, 32)), make_IntV((int64_t)S_IFREG, 32)), int_op_2(iOP::op_eq, Value::from_bytes(x580), make_IntV(1L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x582), make_IntV(4096L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x584), make_IntV(2053L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x586), make_IntV(0L, 64)), int_op_2(iOP::op_eq, Value::from_bytes(x588), make_IntV(1000L, 32)), int_op_2(iOP::op_eq, Value::from_bytes(x590), make_IntV(1000L, 32))}, [&](auto x592) {
return !x592->is_conc();
});
immer::flex_vector<PtrVal> x593 = x575 + x591;
x495.preferred_cex = x593;
immer::flex_vector<String> x594 = Vec::filter(x154, [&](auto x595) {
return x595.length() > 0;
});
Ptr<File> x596 = Vec::foldLeft(x594, x495.root_file, [&](auto x597, auto x598) {
bool x599 = x597 == nullptr || ({
bool x600 = Map::contains(x597->children, x598);
!x600;
});
Ptr<File> x601 = x599 ? nullptr : ({
Ptr<File> x602 = x597->children.at(x598);
x602;
});
return x601;
});
Ptr<Stream> x603 = std::make_shared<Stream>(x596, O_RDONLY, 1L);
immer::map<int, Ptr<Stream>> x604 = x495.opened_files.insert(std::make_pair(3, x603));
x495.opened_files = x604;
/* make a copy */;
Ptr<File> x605 = File::deep_copy(x495.root_file);
immer::map<int, Ptr<Stream>> x606 = immer::map<int, Ptr<Stream>>({});
FS x607 = FS(x606, x605, x495);
Map::foreach(x495.opened_files, [&](auto x608, auto x609) {
Ptr<File> x610 = x609->file;
Ptr<File> x611 = x610;
String x612 = x610->name;
while (x611->parent != nullptr) {
Ptr<File> x613 = x611->parent;
x611 = x613;
bool x614 = x613->parent == nullptr && x613->name == "/";
if (x614) x612 = "/" + x612;
 else x612 = x613->name + "/" + x612;
}
/* assertEq */;
ASSERT((x611->name == "/"), "Outermost ancestor should be named /");
immer::flex_vector<String> x615 = Vec::filter(Str::split(x612, "/"), [&](auto x616) {
return x616.length() > 0;
});
Ptr<File> x617 = Vec::foldLeft(x615, x607.root_file, [&](auto x618, auto x619) {
bool x620 = x618 == nullptr || ({
bool x621 = Map::contains(x618->children, x619);
!x621;
});
Ptr<File> x622 = x620 ? nullptr : ({
Ptr<File> x623 = x618->children.at(x619);
x623;
});
return x622;
});
Ptr<Stream> x624 = std::make_shared<Stream>(x617, x609->mode, x609->cursor);
immer::map<int, Ptr<Stream>> x625 = x607.opened_files.insert(std::make_pair(x608, x624));
x607.opened_files = x625;
return std::monostate{};
});bool x626 = Map::contains(x607.opened_files, 3);
/* assertEq */;
ASSERT((x626 == true), "stream should exist in the copied file system");
Ptr<Stream> x627 = x607.opened_files.at(3);
Ptr<File> x628 = x627->file;
immer::flex_vector<String> x629 = Vec::filter(x154, [&](auto x630) {
return x630.length() > 0;
});
Ptr<File> x631 = Vec::foldLeft(x629, x607.root_file, [&](auto x632, auto x633) {
bool x634 = x632 == nullptr || ({
bool x635 = Map::contains(x632->children, x633);
!x635;
});
Ptr<File> x636 = x634 ? nullptr : ({
Ptr<File> x637 = x632->children.at(x633);
x637;
});
return x636;
});
/* assertEq */;
ASSERT((x628 == x631), "stream should point to the correct file");
int64_t x638 = x627->cursor;
/* assertEq */;
ASSERT((x638 == 1L), "stream should point to the correct offset");
int x639 = x627->mode;
/* assertEq */;
ASSERT((x639 == O_RDONLY), "stream should be read-only");
/* modify a stream in the original file system */;
Ptr<Stream> x640 = x495.opened_files.at(3);
int64_t x641 = x640->cursor;
int64_t x642 = x641 + 3L;
if (x642 < 0L) {} else x640->cursor = x642;
/* check that the copied file system is not modified */;
Ptr<Stream> x643 = x607.opened_files.at(3);
int64_t x644 = x643->cursor;
/* assertEq */;
ASSERT((x644 == 1L), "stream should still point to the old offset");
/* add a stream in the original system */;
immer::flex_vector<String> x645 = Vec::filter(x84, [&](auto x646) {
return x646.length() > 0;
});
Ptr<File> x647 = Vec::foldLeft(x645, x495.root_file, [&](auto x648, auto x649) {
bool x650 = x648 == nullptr || ({
bool x651 = Map::contains(x648->children, x649);
!x651;
});
Ptr<File> x652 = x650 ? nullptr : ({
Ptr<File> x653 = x648->children.at(x649);
x653;
});
return x652;
});
Ptr<Stream> x654 = std::make_shared<Stream>(x647, O_RDONLY, 0L);
immer::map<int, Ptr<Stream>> x655 = x495.opened_files.insert(std::make_pair(4, x654));
x495.opened_files = x655;
/* check that the copied file system is not modified */;
bool x656 = Map::contains(x607.opened_files, 4);
/* assertEq */;
ASSERT((x656 == false), "stream should not exist in the copied file system");
return std::monostate{};
}

int main(int argc, char *argv[]) {
  test(0);
  return 0;
} 
