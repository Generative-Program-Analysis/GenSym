#include "wasm.hpp"
#include <functional>
#include <variant>
#include <cassert>
#include <stdint.h>
#include <stdbool.h>

/*****************************************
Emitting Generated Code
*******************************************/
    

/************* Function Declarations **************/
std::monostate x1(std::monostate);
std::monostate x3(std::monostate);
std::monostate x5(std::monostate);
std::monostate x7(std::monostate);
std::monostate x9(std::monostate);
std::monostate x11(std::monostate);
std::monostate x13(std::monostate);
std::monostate x67(std::monostate);
std::monostate x69(std::monostate);
std::monostate x80(std::monostate);
std::monostate x66(std::monostate);
std::monostate x105(std::monostate);
std::monostate x107(std::monostate);
std::monostate x124(std::monostate);
std::monostate x234(std::monostate);
std::monostate x236(std::monostate);
std::monostate x238(std::monostate);
std::monostate x233(std::monostate);
std::monostate x276(std::monostate);
std::monostate x278(std::monostate);
std::monostate x308(std::monostate);
std::monostate x412(std::monostate);
std::monostate x429(std::monostate);
std::monostate x411(std::monostate);
std::monostate x457(std::monostate);
std::monostate x459(std::monostate);
std::monostate x465(std::monostate);
std::monostate x467(std::monostate);
std::monostate x469(std::monostate);
std::monostate x428(std::monostate);
std::monostate x494(std::monostate);
std::monostate x496(std::monostate);
std::monostate x515(std::monostate);
std::monostate x517(std::monostate);
std::monostate x528(std::monostate);
std::monostate x550(std::monostate);
std::monostate x597(std::monostate);
std::monostate x599(std::monostate);
std::monostate x601(std::monostate);
std::monostate x596(std::monostate);
std::monostate x629(std::monostate);
std::monostate x631(std::monostate);
std::monostate x657(std::monostate);
std::monostate x674(std::monostate);
std::monostate x796(std::monostate);
std::monostate x798(std::monostate);
std::monostate x849(std::monostate);
std::monostate x851(std::monostate);
std::monostate x853(std::monostate);
std::monostate x864(std::monostate);
std::monostate x866(std::monostate);
std::monostate x886(std::monostate);
std::monostate x905(std::monostate);
std::monostate x673(std::monostate);
std::monostate x933(std::monostate);
std::monostate x935(std::monostate);
std::monostate x989(std::monostate);
std::monostate x991(std::monostate);
std::monostate x885(std::monostate);
std::monostate x1038(std::monostate);
std::monostate x1040(std::monostate);
std::monostate x1042(std::monostate);
std::monostate x1048(std::monostate);
std::monostate x1054(std::monostate);
std::monostate x1060(std::monostate);
std::monostate x1066(std::monostate);
std::monostate x1074(std::monostate);
std::monostate x1080(std::monostate);
std::monostate x1084(std::monostate);
std::monostate x1090(std::monostate);
std::monostate x1092(std::monostate);
std::monostate x1094(std::monostate);
std::monostate x1107(std::monostate);
std::monostate x1124(std::monostate);
std::monostate x1126(std::monostate);

/************* Functions **************/
std::monostate x1126(std::monostate x1127) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(0);
SymFrames.popFrameCaller(0);
return x1124(std::monostate{});
}
std::monostate x1124(std::monostate x1125) {
return enterCC(std::monostate());
}
std::monostate x1107(std::monostate x1108) {
infoWhen("CALL", "Entered the function at 8, stackSize =", Stack.size());
Frames.pushFrameCallee(2);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Stack.push(Globals.get(0));
SymStack.push(SymGlobals.get(0));
Stack.push(I32V(16));
SymStack.push(Concrete(I32V(16), 32));
{
Num x1109 = Stack.pop();
SymVal x1110 = SymStack.pop();
Num x1111 = Stack.pop();
SymVal x1112 = SymStack.pop();
Num x1113 = x1111.i32_sub(x1109);
Stack.push(x1113);
bool x1114 = allConcrete(x1112, x1110);
SymVal x1115 = x1114 ? Concrete(x1113, 32) : x1112.minus(x1110);
SymStack.push(x1115);
}
{
Num x1116 = Stack.pop();
SymVal x1117 = SymStack.pop();
Frames.set(0, x1116);
SymFrames.set(0, x1117);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x1118 = Stack.pop();
SymVal x1119 = SymStack.pop();
Globals.set(0, x1118);
SymGlobals.set(0, x1119);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1120 = Stack.pop();
SymVal x1121 = SymStack.pop();
Num x1122 = Stack.pop();
SymStack.pop();
int x1123 = x1122.toInt();
Memory.storeInt(x1123, 12, x1120.toInt());
SymMemory.storeSym(x1123, 12, x1121);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 2);
Frames.pushFrameCaller(0);
SymFrames.pushFramePtr();
updateCurrentMCont(prependCont(x1094, CURRENT_MCONT));
}
__attribute__((musttail)) return x1090(std::monostate{});
return std::monostate{};
}
std::monostate x1094(std::monostate x1095) {
infoWhen("CALL", "Returning from the function at 2, stackSize =", Stack.size());
Frames.popFrameCaller(0);
SymFrames.popFrameCaller(0);
{
Num x1096 = Stack.pop();
SymVal x1097 = SymStack.pop();
Frames.set(1, x1096);
SymFrames.set(1, x1097);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(16));
SymStack.push(Concrete(I32V(16), 32));
{
Num x1098 = Stack.pop();
SymVal x1099 = SymStack.pop();
Num x1100 = Stack.pop();
SymVal x1101 = SymStack.pop();
Num x1102 = x1100.i32_add(x1098);
Stack.push(x1102);
bool x1103 = allConcrete(x1101, x1099);
SymVal x1104 = x1103 ? Concrete(x1102, 32) : x1101.add(x1099);
SymStack.push(x1104);
}
{
Num x1105 = Stack.pop();
SymVal x1106 = SymStack.pop();
Globals.set(0, x1105);
SymGlobals.set(0, x1106);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
return x1092(std::monostate{});
}
std::monostate x1092(std::monostate x1093) {
infoWhen("CALL", "Exiting the function at 8, stackSize =", Stack.size());
Frames.popFrameCallee(2);
SymFrames.popFrameCallee(2);
return enterCC(std::monostate());
}
std::monostate x1090(std::monostate x1091) {
infoWhen("CALL", "Entered the function at 2, stackSize =", Stack.size());
Frames.pushFrameCallee(1);
SymFrames.pushFrameSlot(32);
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 3);
Frames.pushFrameCaller(0);
SymFrames.pushFramePtr();
updateCurrentMCont(prependCont(x1084, CURRENT_MCONT));
}
__attribute__((musttail)) return x107(std::monostate{});
return std::monostate{};
}
std::monostate x1084(std::monostate x1085) {
infoWhen("CALL", "Returning from the function at 3, stackSize =", Stack.size());
Frames.popFrameCaller(0);
SymFrames.popFrameCaller(0);
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
Stack.push(I32V(3));
SymStack.push(Concrete(I32V(3), 32));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 4);
Num x1086 = Stack.pop();
Num x1087 = Stack.pop();
SymVal x1088 = SymStack.pop();
SymVal x1089 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x1087);
Frames.set(1, x1086);
SymFrames.set(0, x1089);
SymFrames.set(1, x1088);
updateCurrentMCont(prependCont(x1080, CURRENT_MCONT));
}
__attribute__((musttail)) return x278(std::monostate{});
return std::monostate{};
}
std::monostate x1080(std::monostate x1081) {
infoWhen("CALL", "Returning from the function at 4, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(3));
SymStack.push(Concrete(I32V(3), 32));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 5);
Num x1082 = Stack.pop();
SymVal x1083 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x1082);
SymFrames.set(0, x1083);
updateCurrentMCont(prependCont(x1074, CURRENT_MCONT));
}
__attribute__((musttail)) return x528(std::monostate{});
return std::monostate{};
}
std::monostate x1074(std::monostate x1075) {
infoWhen("CALL", "Returning from the function at 5, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
Stack.push(I32V(3));
SymStack.push(Concrete(I32V(3), 32));
Stack.push(I32V(10));
SymStack.push(Concrete(I32V(10), 32));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 6);
Num x1076 = Stack.pop();
Num x1077 = Stack.pop();
SymVal x1078 = SymStack.pop();
SymVal x1079 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x1077);
Frames.set(1, x1076);
SymFrames.set(0, x1079);
SymFrames.set(1, x1078);
updateCurrentMCont(prependCont(x1066, CURRENT_MCONT));
}
__attribute__((musttail)) return x631(std::monostate{});
return std::monostate{};
}
std::monostate x1066(std::monostate x1067) {
infoWhen("CALL", "Returning from the function at 6, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(3));
SymStack.push(Concrete(I32V(3), 32));
{
Num x1068 = Stack.pop();
SymVal x1069 = SymStack.pop();
Frames.set(0, x1068);
SymFrames.set(0, x1069);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 4);
Num x1070 = Stack.pop();
Num x1071 = Stack.pop();
SymVal x1072 = SymStack.pop();
SymVal x1073 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x1071);
Frames.set(1, x1070);
SymFrames.set(0, x1073);
SymFrames.set(1, x1072);
updateCurrentMCont(prependCont(x1060, CURRENT_MCONT));
}
__attribute__((musttail)) return x278(std::monostate{});
return std::monostate{};
}
std::monostate x1060(std::monostate x1061) {
infoWhen("CALL", "Returning from the function at 4, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(3));
SymStack.push(Concrete(I32V(3), 32));
Stack.push(I32V(12));
SymStack.push(Concrete(I32V(12), 32));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 7);
Num x1062 = Stack.pop();
Num x1063 = Stack.pop();
SymVal x1064 = SymStack.pop();
SymVal x1065 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x1063);
Frames.set(1, x1062);
SymFrames.set(0, x1065);
SymFrames.set(1, x1064);
updateCurrentMCont(prependCont(x1054, CURRENT_MCONT));
}
__attribute__((musttail)) return x885(std::monostate{});
return std::monostate{};
}
std::monostate x1054(std::monostate x1055) {
infoWhen("CALL", "Returning from the function at 7, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
Stack.push(I32V(10));
SymStack.push(Concrete(I32V(10), 32));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 6);
Num x1056 = Stack.pop();
Num x1057 = Stack.pop();
SymVal x1058 = SymStack.pop();
SymVal x1059 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x1057);
Frames.set(1, x1056);
SymFrames.set(0, x1059);
SymFrames.set(1, x1058);
updateCurrentMCont(prependCont(x1048, CURRENT_MCONT));
}
__attribute__((musttail)) return x631(std::monostate{});
return std::monostate{};
}
std::monostate x1048(std::monostate x1049) {
infoWhen("CALL", "Returning from the function at 6, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
Stack.push(I32V(3));
SymStack.push(Concrete(I32V(3), 32));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 4);
Num x1050 = Stack.pop();
Num x1051 = Stack.pop();
SymVal x1052 = SymStack.pop();
SymVal x1053 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x1051);
Frames.set(1, x1050);
SymFrames.set(0, x1053);
SymFrames.set(1, x1052);
updateCurrentMCont(prependCont(x1042, CURRENT_MCONT));
}
__attribute__((musttail)) return x278(std::monostate{});
return std::monostate{};
}
std::monostate x1042(std::monostate x1043) {
infoWhen("CALL", "Returning from the function at 4, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
Stack.push(I32V(9));
SymStack.push(Concrete(I32V(9), 32));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 7);
Num x1044 = Stack.pop();
Num x1045 = Stack.pop();
SymVal x1046 = SymStack.pop();
SymVal x1047 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x1045);
Frames.set(1, x1044);
SymFrames.set(0, x1047);
SymFrames.set(1, x1046);
updateCurrentMCont(prependCont(x1040, CURRENT_MCONT));
}
__attribute__((musttail)) return x885(std::monostate{});
return std::monostate{};
}
std::monostate x1040(std::monostate x1041) {
infoWhen("CALL", "Returning from the function at 7, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
return x1038(std::monostate{});
}
std::monostate x1038(std::monostate x1039) {
infoWhen("CALL", "Exiting the function at 2, stackSize =", Stack.size());
Frames.popFrameCallee(1);
SymFrames.popFrameCallee(1);
return enterCC(std::monostate());
}
std::monostate x885(std::monostate x1018) {
infoWhen("CALL", "Entered the function at 7, stackSize =", Stack.size());
Frames.pushFrameCallee(6);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Stack.push(Globals.get(0));
SymStack.push(SymGlobals.get(0));
Stack.push(I32V(32));
SymStack.push(Concrete(I32V(32), 32));
{
Num x1019 = Stack.pop();
SymVal x1020 = SymStack.pop();
Num x1021 = Stack.pop();
SymVal x1022 = SymStack.pop();
Num x1023 = x1021.i32_sub(x1019);
Stack.push(x1023);
bool x1024 = allConcrete(x1022, x1020);
SymVal x1025 = x1024 ? Concrete(x1023, 32) : x1022.minus(x1020);
SymStack.push(x1025);
}
{
Num x1026 = Stack.pop();
SymVal x1027 = SymStack.pop();
Frames.set(2, x1026);
SymFrames.set(2, x1027);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x1028 = Stack.pop();
SymVal x1029 = SymStack.pop();
Globals.set(0, x1028);
SymGlobals.set(0, x1029);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x1030 = Stack.pop();
SymVal x1031 = SymStack.pop();
Num x1032 = Stack.pop();
SymStack.pop();
int x1033 = x1032.toInt();
Memory.storeInt(x1033, 28, x1030.toInt());
SymMemory.storeSym(x1033, 28, x1031);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x1034 = Stack.pop();
SymVal x1035 = SymStack.pop();
Num x1036 = Stack.pop();
SymStack.pop();
int x1037 = x1036.toInt();
Memory.storeInt(x1037, 24, x1034.toInt());
SymMemory.storeSym(x1037, 24, x1035);
}
__attribute__((musttail)) return x991(std::monostate{});
return std::monostate{};
}
std::monostate x991(std::monostate x992) {
info("Entering the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x993 = Stack.pop();
SymStack.pop();
Num x994 = I32V(Memory.loadInt(x993.toInt(), 28));
SymVal x995 = SymMemory.loadSym(x993.toInt(), 28);
Stack.push(x994);
SymStack.push(x995);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x996 = Stack.pop();
SymStack.pop();
Num x997 = I32V(Memory.loadInt(x996.toInt(), 24));
SymVal x998 = SymMemory.loadSym(x996.toInt(), 24);
Stack.push(x997);
SymStack.push(x998);
}
{
Num x999 = Stack.pop();
SymVal x1000 = SymStack.pop();
Num x1001 = Stack.pop();
SymVal x1002 = SymStack.pop();
Num x1003 = x1001.i32_lt_s(x999);
Stack.push(x1003);
bool x1004 = allConcrete(x1002, x1000);
SymVal x1005 = x1004 ? Concrete(x1003, 32) : x1002.lt(x1000).bool2bv();
SymStack.push(x1005);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1006 = Stack.pop();
SymVal x1007 = SymStack.pop();
Num x1008 = Stack.pop();
SymVal x1009 = SymStack.pop();
Num x1010 = x1008.i32_and(x1006);
Stack.push(x1010);
bool x1011 = allConcrete(x1009, x1007);
SymVal x1012 = x1011 ? Concrete(x1010, 32) : x1009.bitwise_and(x1007);
SymStack.push(x1012);
}
{
Num x1013 = Stack.pop();
SymVal x1014 = SymStack.pop();
Stack.push(I32V((0 == x1013.toInt())));
SymStack.push(x1014.is_zero().bool2bv());
}
Num x1015 = Stack.pop();
info("The br_if(0)'s condition is ", x1015.toInt());
{
SymVal x1016 = SymStack.pop();
ExploreTree.fillIfElseNode(x1016, 1);
}
int x1017 = x1015.toInt();
if (x1017 != 0) {
info("Jump to 0");
{
ExploreTree.moveCursor(true, makeControl(x935, CURRENT_MCONT));
}
__attribute__((musttail)) return x989(std::monostate{});
} else {
info("Continue rest of the block");
{
ExploreTree.moveCursor(false, makeControl(x989, CURRENT_MCONT));
}
__attribute__((musttail)) return x935(std::monostate{});
}
return std::monostate{};
}
std::monostate x989(std::monostate x990) {
__attribute__((musttail)) return x853(std::monostate{});
return std::monostate{};
}
std::monostate x935(std::monostate x936) {
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x937 = Stack.pop();
SymStack.pop();
Num x938 = I32V(Memory.loadInt(x937.toInt(), 24));
SymVal x939 = SymMemory.loadSym(x937.toInt(), 24);
Stack.push(x938);
SymStack.push(x939);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x940 = Stack.pop();
SymVal x941 = SymStack.pop();
Num x942 = Stack.pop();
SymVal x943 = SymStack.pop();
Num x944 = x942.i32_shl(x940);
Stack.push(x944);
bool x945 = allConcrete(x943, x941);
SymVal x946 = x945 ? Concrete(x944, 32) : x943.shl(x941);
SymStack.push(x946);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
Stack.push(Globals.get(1));
SymStack.push(SymGlobals.get(1));
{
Num x947 = Stack.pop();
SymVal x948 = SymStack.pop();
Num x949 = Stack.pop();
SymVal x950 = SymStack.pop();
Num x951 = x949.i32_add(x947);
Stack.push(x951);
bool x952 = allConcrete(x950, x948);
SymVal x953 = x952 ? Concrete(x951, 32) : x950.add(x948);
SymStack.push(x953);
}
{
Num x954 = Stack.pop();
SymVal x955 = SymStack.pop();
Num x956 = Stack.pop();
SymVal x957 = SymStack.pop();
Num x958 = x956.i32_add(x954);
Stack.push(x958);
bool x959 = allConcrete(x957, x955);
SymVal x960 = x959 ? Concrete(x958, 32) : x957.add(x955);
SymStack.push(x960);
}
{
Num x961 = Stack.pop();
SymStack.pop();
Num x962 = I32V(Memory.loadInt(x961.toInt(), 0));
SymVal x963 = SymMemory.loadSym(x961.toInt(), 0);
Stack.push(x962);
SymStack.push(x963);
}
{
Num x964 = Stack.pop();
SymVal x965 = SymStack.pop();
Num x966 = Stack.pop();
SymStack.pop();
int x967 = x966.toInt();
Memory.storeInt(x967, 20, x964.toInt());
SymMemory.storeSym(x967, 20, x965);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x968 = Stack.pop();
SymStack.pop();
Num x969 = I32V(Memory.loadInt(x968.toInt(), 28));
SymVal x970 = SymMemory.loadSym(x968.toInt(), 28);
Stack.push(x969);
SymStack.push(x970);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x971 = Stack.pop();
SymVal x972 = SymStack.pop();
Num x973 = Stack.pop();
SymVal x974 = SymStack.pop();
Num x975 = x973.i32_sub(x971);
Stack.push(x975);
bool x976 = allConcrete(x974, x972);
SymVal x977 = x976 ? Concrete(x975, 32) : x974.minus(x972);
SymStack.push(x977);
}
{
Num x978 = Stack.pop();
SymVal x979 = SymStack.pop();
Num x980 = Stack.pop();
SymStack.pop();
int x981 = x980.toInt();
Memory.storeInt(x981, 16, x978.toInt());
SymMemory.storeSym(x981, 16, x979);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x982 = Stack.pop();
SymStack.pop();
Num x983 = I32V(Memory.loadInt(x982.toInt(), 28));
SymVal x984 = SymMemory.loadSym(x982.toInt(), 28);
Stack.push(x983);
SymStack.push(x984);
}
{
Num x985 = Stack.pop();
SymVal x986 = SymStack.pop();
Num x987 = Stack.pop();
SymStack.pop();
int x988 = x987.toInt();
Memory.storeInt(x988, 12, x985.toInt());
SymMemory.storeSym(x988, 12, x986);
}
__attribute__((musttail)) return x933(std::monostate{});
return std::monostate{};
}
std::monostate x933(std::monostate x934) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x673(std::monostate{});
return std::monostate{};
}
std::monostate x673(std::monostate x907) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x908 = Stack.pop();
SymStack.pop();
Num x909 = I32V(Memory.loadInt(x908.toInt(), 12));
SymVal x910 = SymMemory.loadSym(x908.toInt(), 12);
Stack.push(x909);
SymStack.push(x910);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x911 = Stack.pop();
SymStack.pop();
Num x912 = I32V(Memory.loadInt(x911.toInt(), 24));
SymVal x913 = SymMemory.loadSym(x911.toInt(), 24);
Stack.push(x912);
SymStack.push(x913);
}
{
Num x914 = Stack.pop();
SymVal x915 = SymStack.pop();
Num x916 = Stack.pop();
SymVal x917 = SymStack.pop();
Num x918 = x916.i32_le_s(x914);
Stack.push(x918);
bool x919 = allConcrete(x917, x915);
SymVal x920 = x919 ? Concrete(x918, 32) : x917.le(x915).bool2bv();
SymStack.push(x920);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x921 = Stack.pop();
SymVal x922 = SymStack.pop();
Num x923 = Stack.pop();
SymVal x924 = SymStack.pop();
Num x925 = x923.i32_and(x921);
Stack.push(x925);
bool x926 = allConcrete(x924, x922);
SymVal x927 = x926 ? Concrete(x925, 32) : x924.bitwise_and(x922);
SymStack.push(x927);
}
{
Num x928 = Stack.pop();
SymVal x929 = SymStack.pop();
Stack.push(I32V((0 == x928.toInt())));
SymStack.push(x929.is_zero().bool2bv());
}
Num x930 = Stack.pop();
info("The br_if(1)'s condition is ", x930.toInt());
{
SymVal x931 = SymStack.pop();
ExploreTree.fillIfElseNode(x931, 0);
}
int x932 = x930.toInt();
if (x932 != 0) {
info("Jump to 1");
{
ExploreTree.moveCursor(true, makeControl(x849, CURRENT_MCONT));
}
__attribute__((musttail)) return x905(std::monostate{});
} else {
info("Continue rest of the block");
{
ExploreTree.moveCursor(false, makeControl(x905, CURRENT_MCONT));
}
__attribute__((musttail)) return x849(std::monostate{});
}
return std::monostate{};
}
std::monostate x905(std::monostate x906) {
__attribute__((musttail)) return x886(std::monostate{});
return std::monostate{};
}
std::monostate x886(std::monostate x887) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x888 = Stack.pop();
SymStack.pop();
Num x889 = I32V(Memory.loadInt(x888.toInt(), 28));
SymVal x890 = SymMemory.loadSym(x888.toInt(), 28);
Stack.push(x889);
SymStack.push(x890);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x891 = Stack.pop();
SymStack.pop();
Num x892 = I32V(Memory.loadInt(x891.toInt(), 16));
SymVal x893 = SymMemory.loadSym(x891.toInt(), 16);
Stack.push(x892);
SymStack.push(x893);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x894 = Stack.pop();
SymVal x895 = SymStack.pop();
Num x896 = Stack.pop();
SymVal x897 = SymStack.pop();
Num x898 = x896.i32_sub(x894);
Stack.push(x898);
bool x899 = allConcrete(x897, x895);
SymVal x900 = x899 ? Concrete(x898, 32) : x897.minus(x895);
SymStack.push(x900);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 7);
Num x901 = Stack.pop();
Num x902 = Stack.pop();
SymVal x903 = SymStack.pop();
SymVal x904 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x902);
Frames.set(1, x901);
SymFrames.set(0, x904);
SymFrames.set(1, x903);
updateCurrentMCont(prependCont(x866, CURRENT_MCONT));
}
__attribute__((musttail)) return x885(std::monostate{});
return std::monostate{};
}
std::monostate x866(std::monostate x867) {
infoWhen("CALL", "Returning from the function at 7, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x868 = Stack.pop();
SymStack.pop();
Num x869 = I32V(Memory.loadInt(x868.toInt(), 16));
SymVal x870 = SymMemory.loadSym(x868.toInt(), 16);
Stack.push(x869);
SymStack.push(x870);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x871 = Stack.pop();
SymVal x872 = SymStack.pop();
Num x873 = Stack.pop();
SymVal x874 = SymStack.pop();
Num x875 = x873.i32_add(x871);
Stack.push(x875);
bool x876 = allConcrete(x874, x872);
SymVal x877 = x876 ? Concrete(x875, 32) : x874.add(x872);
SymStack.push(x877);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x878 = Stack.pop();
SymStack.pop();
Num x879 = I32V(Memory.loadInt(x878.toInt(), 24));
SymVal x880 = SymMemory.loadSym(x878.toInt(), 24);
Stack.push(x879);
SymStack.push(x880);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 7);
Num x881 = Stack.pop();
Num x882 = Stack.pop();
SymVal x883 = SymStack.pop();
SymVal x884 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x882);
Frames.set(1, x881);
SymFrames.set(0, x884);
SymFrames.set(1, x883);
updateCurrentMCont(prependCont(x864, CURRENT_MCONT));
}
__attribute__((musttail)) return x885(std::monostate{});
return std::monostate{};
}
std::monostate x864(std::monostate x865) {
infoWhen("CALL", "Returning from the function at 7, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
__attribute__((musttail)) return x853(std::monostate{});
return std::monostate{};
}
std::monostate x853(std::monostate x854) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(32));
SymStack.push(Concrete(I32V(32), 32));
{
Num x855 = Stack.pop();
SymVal x856 = SymStack.pop();
Num x857 = Stack.pop();
SymVal x858 = SymStack.pop();
Num x859 = x857.i32_add(x855);
Stack.push(x859);
bool x860 = allConcrete(x858, x856);
SymVal x861 = x860 ? Concrete(x859, 32) : x858.add(x856);
SymStack.push(x861);
}
{
Num x862 = Stack.pop();
SymVal x863 = SymStack.pop();
Globals.set(0, x862);
SymGlobals.set(0, x863);
}
return x851(std::monostate{});
}
std::monostate x851(std::monostate x852) {
infoWhen("CALL", "Exiting the function at 7, stackSize =", Stack.size());
Frames.popFrameCallee(6);
SymFrames.popFrameCallee(6);
return enterCC(std::monostate());
}
std::monostate x849(std::monostate x850) {
__attribute__((musttail)) return x798(std::monostate{});
return std::monostate{};
}
std::monostate x798(std::monostate x799) {
info("Entering the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x800 = Stack.pop();
SymStack.pop();
Num x801 = I32V(Memory.loadInt(x800.toInt(), 12));
SymVal x802 = SymMemory.loadSym(x800.toInt(), 12);
Stack.push(x801);
SymStack.push(x802);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x803 = Stack.pop();
SymVal x804 = SymStack.pop();
Num x805 = Stack.pop();
SymVal x806 = SymStack.pop();
Num x807 = x805.i32_shl(x803);
Stack.push(x807);
bool x808 = allConcrete(x806, x804);
SymVal x809 = x808 ? Concrete(x807, 32) : x806.shl(x804);
SymStack.push(x809);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
Stack.push(Globals.get(1));
SymStack.push(SymGlobals.get(1));
{
Num x810 = Stack.pop();
SymVal x811 = SymStack.pop();
Num x812 = Stack.pop();
SymVal x813 = SymStack.pop();
Num x814 = x812.i32_add(x810);
Stack.push(x814);
bool x815 = allConcrete(x813, x811);
SymVal x816 = x815 ? Concrete(x814, 32) : x813.add(x811);
SymStack.push(x816);
}
{
Num x817 = Stack.pop();
SymVal x818 = SymStack.pop();
Num x819 = Stack.pop();
SymVal x820 = SymStack.pop();
Num x821 = x819.i32_add(x817);
Stack.push(x821);
bool x822 = allConcrete(x820, x818);
SymVal x823 = x822 ? Concrete(x821, 32) : x820.add(x818);
SymStack.push(x823);
}
{
Num x824 = Stack.pop();
SymStack.pop();
Num x825 = I32V(Memory.loadInt(x824.toInt(), 0));
SymVal x826 = SymMemory.loadSym(x824.toInt(), 0);
Stack.push(x825);
SymStack.push(x826);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x827 = Stack.pop();
SymStack.pop();
Num x828 = I32V(Memory.loadInt(x827.toInt(), 20));
SymVal x829 = SymMemory.loadSym(x827.toInt(), 20);
Stack.push(x828);
SymStack.push(x829);
}
{
Num x830 = Stack.pop();
SymVal x831 = SymStack.pop();
Num x832 = Stack.pop();
SymVal x833 = SymStack.pop();
Num x834 = x832.i32_le_s(x830);
Stack.push(x834);
bool x835 = allConcrete(x833, x831);
SymVal x836 = x835 ? Concrete(x834, 32) : x833.le(x831).bool2bv();
SymStack.push(x836);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x837 = Stack.pop();
SymVal x838 = SymStack.pop();
Num x839 = Stack.pop();
SymVal x840 = SymStack.pop();
Num x841 = x839.i32_and(x837);
Stack.push(x841);
bool x842 = allConcrete(x840, x838);
SymVal x843 = x842 ? Concrete(x841, 32) : x840.bitwise_and(x838);
SymStack.push(x843);
}
{
Num x844 = Stack.pop();
SymVal x845 = SymStack.pop();
Stack.push(I32V((0 == x844.toInt())));
SymStack.push(x845.is_zero().bool2bv());
}
Num x846 = Stack.pop();
info("The br_if(0)'s condition is ", x846.toInt());
{
SymVal x847 = SymStack.pop();
ExploreTree.fillIfElseNode(x847, 1);
}
int x848 = x846.toInt();
if (x848 != 0) {
info("Jump to 0");
{
ExploreTree.moveCursor(true, makeControl(x674, CURRENT_MCONT));
}
__attribute__((musttail)) return x796(std::monostate{});
} else {
info("Continue rest of the block");
{
ExploreTree.moveCursor(false, makeControl(x796, CURRENT_MCONT));
}
__attribute__((musttail)) return x674(std::monostate{});
}
return std::monostate{};
}
std::monostate x796(std::monostate x797) {
__attribute__((musttail)) return x657(std::monostate{});
return std::monostate{};
}
std::monostate x674(std::monostate x675) {
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x676 = Stack.pop();
SymStack.pop();
Num x677 = I32V(Memory.loadInt(x676.toInt(), 16));
SymVal x678 = SymMemory.loadSym(x676.toInt(), 16);
Stack.push(x677);
SymStack.push(x678);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x679 = Stack.pop();
SymVal x680 = SymStack.pop();
Num x681 = Stack.pop();
SymVal x682 = SymStack.pop();
Num x683 = x681.i32_add(x679);
Stack.push(x683);
bool x684 = allConcrete(x682, x680);
SymVal x685 = x684 ? Concrete(x683, 32) : x682.add(x680);
SymStack.push(x685);
}
{
Num x686 = Stack.pop();
SymVal x687 = SymStack.pop();
Num x688 = Stack.pop();
SymStack.pop();
int x689 = x688.toInt();
Memory.storeInt(x689, 16, x686.toInt());
SymMemory.storeSym(x689, 16, x687);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x690 = Stack.pop();
SymStack.pop();
Num x691 = I32V(Memory.loadInt(x690.toInt(), 12));
SymVal x692 = SymMemory.loadSym(x690.toInt(), 12);
Stack.push(x691);
SymStack.push(x692);
}
{
Num x693 = Stack.pop();
SymVal x694 = SymStack.pop();
Frames.set(3, x693);
SymFrames.set(3, x694);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
Stack.push(Globals.get(1));
SymStack.push(SymGlobals.get(1));
{
Num x695 = Stack.pop();
SymVal x696 = SymStack.pop();
Num x697 = Stack.pop();
SymVal x698 = SymStack.pop();
Num x699 = x697.i32_add(x695);
Stack.push(x699);
bool x700 = allConcrete(x698, x696);
SymVal x701 = x700 ? Concrete(x699, 32) : x698.add(x696);
SymStack.push(x701);
}
{
Num x702 = Stack.pop();
SymVal x703 = SymStack.pop();
Frames.set(4, x702);
SymFrames.set(4, x703);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x704 = Stack.pop();
SymVal x705 = SymStack.pop();
Frames.set(5, x704);
SymFrames.set(5, x705);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(4));
SymStack.push(SymFrames.get(4));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x706 = Stack.pop();
SymVal x707 = SymStack.pop();
Num x708 = Stack.pop();
SymVal x709 = SymStack.pop();
Num x710 = x708.i32_shl(x706);
Stack.push(x710);
bool x711 = allConcrete(x709, x707);
SymVal x712 = x711 ? Concrete(x710, 32) : x709.shl(x707);
SymStack.push(x712);
}
{
Num x713 = Stack.pop();
SymVal x714 = SymStack.pop();
Num x715 = Stack.pop();
SymVal x716 = SymStack.pop();
Num x717 = x715.i32_add(x713);
Stack.push(x717);
bool x718 = allConcrete(x716, x714);
SymVal x719 = x718 ? Concrete(x717, 32) : x716.add(x714);
SymStack.push(x719);
}
{
Num x720 = Stack.pop();
SymStack.pop();
Num x721 = I32V(Memory.loadInt(x720.toInt(), 0));
SymVal x722 = SymMemory.loadSym(x720.toInt(), 0);
Stack.push(x721);
SymStack.push(x722);
}
{
Num x723 = Stack.pop();
SymVal x724 = SymStack.pop();
Num x725 = Stack.pop();
SymStack.pop();
int x726 = x725.toInt();
Memory.storeInt(x726, 8, x723.toInt());
SymMemory.storeSym(x726, 8, x724);
}
Stack.push(Frames.get(4));
SymStack.push(SymFrames.get(4));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x727 = Stack.pop();
SymStack.pop();
Num x728 = I32V(Memory.loadInt(x727.toInt(), 16));
SymVal x729 = SymMemory.loadSym(x727.toInt(), 16);
Stack.push(x728);
SymStack.push(x729);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x730 = Stack.pop();
SymVal x731 = SymStack.pop();
Num x732 = Stack.pop();
SymVal x733 = SymStack.pop();
Num x734 = x732.i32_shl(x730);
Stack.push(x734);
bool x735 = allConcrete(x733, x731);
SymVal x736 = x735 ? Concrete(x734, 32) : x733.shl(x731);
SymStack.push(x736);
}
{
Num x737 = Stack.pop();
SymVal x738 = SymStack.pop();
Num x739 = Stack.pop();
SymVal x740 = SymStack.pop();
Num x741 = x739.i32_add(x737);
Stack.push(x741);
bool x742 = allConcrete(x740, x738);
SymVal x743 = x742 ? Concrete(x741, 32) : x740.add(x738);
SymStack.push(x743);
}
{
Num x744 = Stack.pop();
SymStack.pop();
Num x745 = I32V(Memory.loadInt(x744.toInt(), 0));
SymVal x746 = SymMemory.loadSym(x744.toInt(), 0);
Stack.push(x745);
SymStack.push(x746);
}
{
Num x747 = Stack.pop();
SymVal x748 = SymStack.pop();
Frames.set(6, x747);
SymFrames.set(6, x748);
}
Stack.push(Frames.get(4));
SymStack.push(SymFrames.get(4));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x749 = Stack.pop();
SymStack.pop();
Num x750 = I32V(Memory.loadInt(x749.toInt(), 12));
SymVal x751 = SymMemory.loadSym(x749.toInt(), 12);
Stack.push(x750);
SymStack.push(x751);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x752 = Stack.pop();
SymVal x753 = SymStack.pop();
Num x754 = Stack.pop();
SymVal x755 = SymStack.pop();
Num x756 = x754.i32_shl(x752);
Stack.push(x756);
bool x757 = allConcrete(x755, x753);
SymVal x758 = x757 ? Concrete(x756, 32) : x755.shl(x753);
SymStack.push(x758);
}
{
Num x759 = Stack.pop();
SymVal x760 = SymStack.pop();
Num x761 = Stack.pop();
SymVal x762 = SymStack.pop();
Num x763 = x761.i32_add(x759);
Stack.push(x763);
bool x764 = allConcrete(x762, x760);
SymVal x765 = x764 ? Concrete(x763, 32) : x762.add(x760);
SymStack.push(x765);
}
Stack.push(Frames.get(6));
SymStack.push(SymFrames.get(6));
{
Num x766 = Stack.pop();
SymVal x767 = SymStack.pop();
Num x768 = Stack.pop();
SymStack.pop();
int x769 = x768.toInt();
Memory.storeInt(x769, 0, x766.toInt());
SymMemory.storeSym(x769, 0, x767);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x770 = Stack.pop();
SymStack.pop();
Num x771 = I32V(Memory.loadInt(x770.toInt(), 8));
SymVal x772 = SymMemory.loadSym(x770.toInt(), 8);
Stack.push(x771);
SymStack.push(x772);
}
{
Num x773 = Stack.pop();
SymVal x774 = SymStack.pop();
Frames.set(7, x773);
SymFrames.set(7, x774);
}
Stack.push(Frames.get(4));
SymStack.push(SymFrames.get(4));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x775 = Stack.pop();
SymStack.pop();
Num x776 = I32V(Memory.loadInt(x775.toInt(), 16));
SymVal x777 = SymMemory.loadSym(x775.toInt(), 16);
Stack.push(x776);
SymStack.push(x777);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x778 = Stack.pop();
SymVal x779 = SymStack.pop();
Num x780 = Stack.pop();
SymVal x781 = SymStack.pop();
Num x782 = x780.i32_shl(x778);
Stack.push(x782);
bool x783 = allConcrete(x781, x779);
SymVal x784 = x783 ? Concrete(x782, 32) : x781.shl(x779);
SymStack.push(x784);
}
{
Num x785 = Stack.pop();
SymVal x786 = SymStack.pop();
Num x787 = Stack.pop();
SymVal x788 = SymStack.pop();
Num x789 = x787.i32_add(x785);
Stack.push(x789);
bool x790 = allConcrete(x788, x786);
SymVal x791 = x790 ? Concrete(x789, 32) : x788.add(x786);
SymStack.push(x791);
}
Stack.push(Frames.get(7));
SymStack.push(SymFrames.get(7));
{
Num x792 = Stack.pop();
SymVal x793 = SymStack.pop();
Num x794 = Stack.pop();
SymStack.pop();
int x795 = x794.toInt();
Memory.storeInt(x795, 0, x792.toInt());
SymMemory.storeSym(x795, 0, x793);
}
__attribute__((musttail)) return x657(std::monostate{});
return std::monostate{};
}
std::monostate x657(std::monostate x658) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x659 = Stack.pop();
SymStack.pop();
Num x660 = I32V(Memory.loadInt(x659.toInt(), 12));
SymVal x661 = SymMemory.loadSym(x659.toInt(), 12);
Stack.push(x660);
SymStack.push(x661);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x662 = Stack.pop();
SymVal x663 = SymStack.pop();
Num x664 = Stack.pop();
SymVal x665 = SymStack.pop();
Num x666 = x664.i32_add(x662);
Stack.push(x666);
bool x667 = allConcrete(x665, x663);
SymVal x668 = x667 ? Concrete(x666, 32) : x665.add(x663);
SymStack.push(x668);
}
{
Num x669 = Stack.pop();
SymVal x670 = SymStack.pop();
Num x671 = Stack.pop();
SymStack.pop();
int x672 = x671.toInt();
Memory.storeInt(x672, 12, x669.toInt());
SymMemory.storeSym(x672, 12, x670);
}
info("Jump to 0");
__attribute__((musttail)) return x673(std::monostate{});
return std::monostate{};
}
std::monostate x631(std::monostate x632) {
infoWhen("CALL", "Entered the function at 6, stackSize =", Stack.size());
Frames.pushFrameCallee(2);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Stack.push(Globals.get(0));
SymStack.push(SymGlobals.get(0));
Stack.push(I32V(16));
SymStack.push(Concrete(I32V(16), 32));
{
Num x633 = Stack.pop();
SymVal x634 = SymStack.pop();
Num x635 = Stack.pop();
SymVal x636 = SymStack.pop();
Num x637 = x635.i32_sub(x633);
Stack.push(x637);
bool x638 = allConcrete(x636, x634);
SymVal x639 = x638 ? Concrete(x637, 32) : x636.minus(x634);
SymStack.push(x639);
}
{
Num x640 = Stack.pop();
SymVal x641 = SymStack.pop();
Frames.set(2, x640);
SymFrames.set(2, x641);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x642 = Stack.pop();
SymVal x643 = SymStack.pop();
Num x644 = Stack.pop();
SymStack.pop();
int x645 = x644.toInt();
Memory.storeInt(x645, 12, x642.toInt());
SymMemory.storeSym(x645, 12, x643);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x646 = Stack.pop();
SymVal x647 = SymStack.pop();
Num x648 = Stack.pop();
SymStack.pop();
int x649 = x648.toInt();
Memory.storeInt(x649, 8, x646.toInt());
SymMemory.storeSym(x649, 8, x647);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x650 = Stack.pop();
SymStack.pop();
Num x651 = I32V(Memory.loadInt(x650.toInt(), 12));
SymVal x652 = SymMemory.loadSym(x650.toInt(), 12);
Stack.push(x651);
SymStack.push(x652);
}
{
Num x653 = Stack.pop();
SymVal x654 = SymStack.pop();
Num x655 = Stack.pop();
SymStack.pop();
int x656 = x655.toInt();
Memory.storeInt(x656, 4, x653.toInt());
SymMemory.storeSym(x656, 4, x654);
}
__attribute__((musttail)) return x629(std::monostate{});
return std::monostate{};
}
std::monostate x629(std::monostate x630) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x596(std::monostate{});
return std::monostate{};
}
std::monostate x596(std::monostate x603) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x604 = Stack.pop();
SymStack.pop();
Num x605 = I32V(Memory.loadInt(x604.toInt(), 4));
SymVal x606 = SymMemory.loadSym(x604.toInt(), 4);
Stack.push(x605);
SymStack.push(x606);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x607 = Stack.pop();
SymStack.pop();
Num x608 = I32V(Memory.loadInt(x607.toInt(), 8));
SymVal x609 = SymMemory.loadSym(x607.toInt(), 8);
Stack.push(x608);
SymStack.push(x609);
}
{
Num x610 = Stack.pop();
SymVal x611 = SymStack.pop();
Num x612 = Stack.pop();
SymVal x613 = SymStack.pop();
Num x614 = x612.i32_lt_s(x610);
Stack.push(x614);
bool x615 = allConcrete(x613, x611);
SymVal x616 = x615 ? Concrete(x614, 32) : x613.lt(x611).bool2bv();
SymStack.push(x616);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x617 = Stack.pop();
SymVal x618 = SymStack.pop();
Num x619 = Stack.pop();
SymVal x620 = SymStack.pop();
Num x621 = x619.i32_and(x617);
Stack.push(x621);
bool x622 = allConcrete(x620, x618);
SymVal x623 = x622 ? Concrete(x621, 32) : x620.bitwise_and(x618);
SymStack.push(x623);
}
{
Num x624 = Stack.pop();
SymVal x625 = SymStack.pop();
Stack.push(I32V((0 == x624.toInt())));
SymStack.push(x625.is_zero().bool2bv());
}
Num x626 = Stack.pop();
info("The br_if(1)'s condition is ", x626.toInt());
{
SymVal x627 = SymStack.pop();
ExploreTree.fillIfElseNode(x627, 0);
}
int x628 = x626.toInt();
if (x628 != 0) {
info("Jump to 1");
{
ExploreTree.moveCursor(true, makeControl(x550, CURRENT_MCONT));
}
__attribute__((musttail)) return x601(std::monostate{});
} else {
info("Continue rest of the block");
{
ExploreTree.moveCursor(false, makeControl(x601, CURRENT_MCONT));
}
__attribute__((musttail)) return x550(std::monostate{});
}
return std::monostate{};
}
std::monostate x601(std::monostate x602) {
__attribute__((musttail)) return x599(std::monostate{});
return std::monostate{};
}
std::monostate x599(std::monostate x600) {
info("Exiting the block, stackSize =", Stack.size());
return x597(std::monostate{});
}
std::monostate x597(std::monostate x598) {
infoWhen("CALL", "Exiting the function at 6, stackSize =", Stack.size());
Frames.popFrameCallee(2);
SymFrames.popFrameCallee(2);
return enterCC(std::monostate());
}
std::monostate x550(std::monostate x551) {
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x552 = Stack.pop();
SymStack.pop();
Num x553 = I32V(Memory.loadInt(x552.toInt(), 4));
SymVal x554 = SymMemory.loadSym(x552.toInt(), 4);
Stack.push(x553);
SymStack.push(x554);
}
{
Num x555 = Stack.pop();
SymVal x556 = SymStack.pop();
Frames.set(3, x555);
SymFrames.set(3, x556);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x557 = Stack.pop();
SymVal x558 = SymStack.pop();
Num x559 = Stack.pop();
SymVal x560 = SymStack.pop();
Num x561 = x559.i32_shl(x557);
Stack.push(x561);
bool x562 = allConcrete(x560, x558);
SymVal x563 = x562 ? Concrete(x561, 32) : x560.shl(x558);
SymStack.push(x563);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
Stack.push(Globals.get(1));
SymStack.push(SymGlobals.get(1));
{
Num x564 = Stack.pop();
SymVal x565 = SymStack.pop();
Num x566 = Stack.pop();
SymVal x567 = SymStack.pop();
Num x568 = x566.i32_add(x564);
Stack.push(x568);
bool x569 = allConcrete(x567, x565);
SymVal x570 = x569 ? Concrete(x568, 32) : x567.add(x565);
SymStack.push(x570);
}
{
Num x571 = Stack.pop();
SymVal x572 = SymStack.pop();
Num x573 = Stack.pop();
SymVal x574 = SymStack.pop();
Num x575 = x573.i32_add(x571);
Stack.push(x575);
bool x576 = allConcrete(x574, x572);
SymVal x577 = x576 ? Concrete(x575, 32) : x574.add(x572);
SymStack.push(x577);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x578 = Stack.pop();
SymVal x579 = SymStack.pop();
Num x580 = Stack.pop();
SymStack.pop();
int x581 = x580.toInt();
Memory.storeInt(x581, 0, x578.toInt());
SymMemory.storeSym(x581, 0, x579);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x582 = Stack.pop();
SymStack.pop();
Num x583 = I32V(Memory.loadInt(x582.toInt(), 4));
SymVal x584 = SymMemory.loadSym(x582.toInt(), 4);
Stack.push(x583);
SymStack.push(x584);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x585 = Stack.pop();
SymVal x586 = SymStack.pop();
Num x587 = Stack.pop();
SymVal x588 = SymStack.pop();
Num x589 = x587.i32_add(x585);
Stack.push(x589);
bool x590 = allConcrete(x588, x586);
SymVal x591 = x590 ? Concrete(x589, 32) : x588.add(x586);
SymStack.push(x591);
}
{
Num x592 = Stack.pop();
SymVal x593 = SymStack.pop();
Num x594 = Stack.pop();
SymStack.pop();
int x595 = x594.toInt();
Memory.storeInt(x595, 4, x592.toInt());
SymMemory.storeSym(x595, 4, x593);
}
info("Jump to 0");
__attribute__((musttail)) return x596(std::monostate{});
return std::monostate{};
}
std::monostate x528(std::monostate x529) {
infoWhen("CALL", "Entered the function at 5, stackSize =", Stack.size());
Frames.pushFrameCallee(1);
SymFrames.pushFrameSlot(32);
Stack.push(Globals.get(0));
SymStack.push(SymGlobals.get(0));
Stack.push(I32V(16));
SymStack.push(Concrete(I32V(16), 32));
{
Num x530 = Stack.pop();
SymVal x531 = SymStack.pop();
Num x532 = Stack.pop();
SymVal x533 = SymStack.pop();
Num x534 = x532.i32_sub(x530);
Stack.push(x534);
bool x535 = allConcrete(x533, x531);
SymVal x536 = x535 ? Concrete(x534, 32) : x533.minus(x531);
SymStack.push(x536);
}
{
Num x537 = Stack.pop();
SymVal x538 = SymStack.pop();
Frames.set(1, x537);
SymFrames.set(1, x538);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x539 = Stack.pop();
SymVal x540 = SymStack.pop();
Globals.set(0, x539);
SymGlobals.set(0, x540);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x541 = Stack.pop();
SymVal x542 = SymStack.pop();
Num x543 = Stack.pop();
SymStack.pop();
int x544 = x543.toInt();
Memory.storeInt(x544, 12, x541.toInt());
SymMemory.storeSym(x544, 12, x542);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x545 = Stack.pop();
SymStack.pop();
Num x546 = I32V(Memory.loadInt(x545.toInt(), 12));
SymVal x547 = SymMemory.loadSym(x545.toInt(), 12);
Stack.push(x546);
SymStack.push(x547);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 9);
Num x548 = Stack.pop();
SymVal x549 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x548);
SymFrames.set(0, x549);
updateCurrentMCont(prependCont(x517, CURRENT_MCONT));
}
__attribute__((musttail)) return x496(std::monostate{});
return std::monostate{};
}
std::monostate x517(std::monostate x518) {
infoWhen("CALL", "Returning from the function at 9, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(16));
SymStack.push(Concrete(I32V(16), 32));
{
Num x519 = Stack.pop();
SymVal x520 = SymStack.pop();
Num x521 = Stack.pop();
SymVal x522 = SymStack.pop();
Num x523 = x521.i32_add(x519);
Stack.push(x523);
bool x524 = allConcrete(x522, x520);
SymVal x525 = x524 ? Concrete(x523, 32) : x522.add(x520);
SymStack.push(x525);
}
{
Num x526 = Stack.pop();
SymVal x527 = SymStack.pop();
Globals.set(0, x526);
SymGlobals.set(0, x527);
}
return x515(std::monostate{});
}
std::monostate x515(std::monostate x516) {
infoWhen("CALL", "Exiting the function at 5, stackSize =", Stack.size());
Frames.popFrameCallee(1);
SymFrames.popFrameCallee(1);
return enterCC(std::monostate());
}
std::monostate x496(std::monostate x497) {
infoWhen("CALL", "Entered the function at 9, stackSize =", Stack.size());
Frames.pushFrameCallee(3);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Stack.push(Globals.get(0));
SymStack.push(SymGlobals.get(0));
Stack.push(I32V(16));
SymStack.push(Concrete(I32V(16), 32));
{
Num x498 = Stack.pop();
SymVal x499 = SymStack.pop();
Num x500 = Stack.pop();
SymVal x501 = SymStack.pop();
Num x502 = x500.i32_sub(x498);
Stack.push(x502);
bool x503 = allConcrete(x501, x499);
SymVal x504 = x503 ? Concrete(x502, 32) : x501.minus(x499);
SymStack.push(x504);
}
{
Num x505 = Stack.pop();
SymVal x506 = SymStack.pop();
Frames.set(1, x505);
SymFrames.set(1, x506);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x507 = Stack.pop();
SymVal x508 = SymStack.pop();
Num x509 = Stack.pop();
SymStack.pop();
int x510 = x509.toInt();
Memory.storeInt(x510, 12, x507.toInt());
SymMemory.storeSym(x510, 12, x508);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x511 = Stack.pop();
SymVal x512 = SymStack.pop();
Num x513 = Stack.pop();
SymStack.pop();
int x514 = x513.toInt();
Memory.storeInt(x514, 8, x511.toInt());
SymMemory.storeSym(x514, 8, x512);
}
__attribute__((musttail)) return x494(std::monostate{});
return std::monostate{};
}
std::monostate x494(std::monostate x495) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x428(std::monostate{});
return std::monostate{};
}
std::monostate x428(std::monostate x471) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x472 = Stack.pop();
SymStack.pop();
Num x473 = I32V(Memory.loadInt(x472.toInt(), 8));
SymVal x474 = SymMemory.loadSym(x472.toInt(), 8);
Stack.push(x473);
SymStack.push(x474);
}
Stack.push(I32V(400));
SymStack.push(Concrete(I32V(400), 32));
{
Num x475 = Stack.pop();
SymVal x476 = SymStack.pop();
Num x477 = Stack.pop();
SymVal x478 = SymStack.pop();
Num x479 = x477.i32_lt_s(x475);
Stack.push(x479);
bool x480 = allConcrete(x478, x476);
SymVal x481 = x480 ? Concrete(x479, 32) : x478.lt(x476).bool2bv();
SymStack.push(x481);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x482 = Stack.pop();
SymVal x483 = SymStack.pop();
Num x484 = Stack.pop();
SymVal x485 = SymStack.pop();
Num x486 = x484.i32_and(x482);
Stack.push(x486);
bool x487 = allConcrete(x485, x483);
SymVal x488 = x487 ? Concrete(x486, 32) : x485.bitwise_and(x483);
SymStack.push(x488);
}
{
Num x489 = Stack.pop();
SymVal x490 = SymStack.pop();
Stack.push(I32V((0 == x489.toInt())));
SymStack.push(x490.is_zero().bool2bv());
}
Num x491 = Stack.pop();
info("The br_if(1)'s condition is ", x491.toInt());
{
SymVal x492 = SymStack.pop();
ExploreTree.fillIfElseNode(x492, 0);
}
int x493 = x491.toInt();
if (x493 != 0) {
info("Jump to 1");
{
ExploreTree.moveCursor(true, makeControl(x459, CURRENT_MCONT));
}
__attribute__((musttail)) return x469(std::monostate{});
} else {
info("Continue rest of the block");
{
ExploreTree.moveCursor(false, makeControl(x469, CURRENT_MCONT));
}
__attribute__((musttail)) return x459(std::monostate{});
}
return std::monostate{};
}
std::monostate x469(std::monostate x470) {
__attribute__((musttail)) return x467(std::monostate{});
return std::monostate{};
}
std::monostate x467(std::monostate x468) {
info("Exiting the block, stackSize =", Stack.size());
return x465(std::monostate{});
}
std::monostate x465(std::monostate x466) {
infoWhen("CALL", "Exiting the function at 9, stackSize =", Stack.size());
Frames.popFrameCallee(3);
SymFrames.popFrameCallee(3);
return enterCC(std::monostate());
}
std::monostate x459(std::monostate x460) {
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x461 = Stack.pop();
SymVal x462 = SymStack.pop();
Num x463 = Stack.pop();
SymStack.pop();
int x464 = x463.toInt();
Memory.storeInt(x464, 4, x461.toInt());
SymMemory.storeSym(x464, 4, x462);
}
__attribute__((musttail)) return x457(std::monostate{});
return std::monostate{};
}
std::monostate x457(std::monostate x458) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x411(std::monostate{});
return std::monostate{};
}
std::monostate x411(std::monostate x431) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x432 = Stack.pop();
SymStack.pop();
Num x433 = I32V(Memory.loadInt(x432.toInt(), 4));
SymVal x434 = SymMemory.loadSym(x432.toInt(), 4);
Stack.push(x433);
SymStack.push(x434);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x435 = Stack.pop();
SymStack.pop();
Num x436 = I32V(Memory.loadInt(x435.toInt(), 12));
SymVal x437 = SymMemory.loadSym(x435.toInt(), 12);
Stack.push(x436);
SymStack.push(x437);
}
{
Num x438 = Stack.pop();
SymVal x439 = SymStack.pop();
Num x440 = Stack.pop();
SymVal x441 = SymStack.pop();
Num x442 = x440.i32_lt_s(x438);
Stack.push(x442);
bool x443 = allConcrete(x441, x439);
SymVal x444 = x443 ? Concrete(x442, 32) : x441.lt(x439).bool2bv();
SymStack.push(x444);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x445 = Stack.pop();
SymVal x446 = SymStack.pop();
Num x447 = Stack.pop();
SymVal x448 = SymStack.pop();
Num x449 = x447.i32_and(x445);
Stack.push(x449);
bool x450 = allConcrete(x448, x446);
SymVal x451 = x450 ? Concrete(x449, 32) : x448.bitwise_and(x446);
SymStack.push(x451);
}
{
Num x452 = Stack.pop();
SymVal x453 = SymStack.pop();
Stack.push(I32V((0 == x452.toInt())));
SymStack.push(x453.is_zero().bool2bv());
}
Num x454 = Stack.pop();
info("The br_if(1)'s condition is ", x454.toInt());
{
SymVal x455 = SymStack.pop();
ExploreTree.fillIfElseNode(x455, 0);
}
int x456 = x454.toInt();
if (x456 != 0) {
info("Jump to 1");
{
ExploreTree.moveCursor(true, makeControl(x308, CURRENT_MCONT));
}
__attribute__((musttail)) return x429(std::monostate{});
} else {
info("Continue rest of the block");
{
ExploreTree.moveCursor(false, makeControl(x429, CURRENT_MCONT));
}
__attribute__((musttail)) return x308(std::monostate{});
}
return std::monostate{};
}
std::monostate x429(std::monostate x430) {
__attribute__((musttail)) return x412(std::monostate{});
return std::monostate{};
}
std::monostate x412(std::monostate x413) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x414 = Stack.pop();
SymStack.pop();
Num x415 = I32V(Memory.loadInt(x414.toInt(), 8));
SymVal x416 = SymMemory.loadSym(x414.toInt(), 8);
Stack.push(x415);
SymStack.push(x416);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x417 = Stack.pop();
SymVal x418 = SymStack.pop();
Num x419 = Stack.pop();
SymVal x420 = SymStack.pop();
Num x421 = x419.i32_add(x417);
Stack.push(x421);
bool x422 = allConcrete(x420, x418);
SymVal x423 = x422 ? Concrete(x421, 32) : x420.add(x418);
SymStack.push(x423);
}
{
Num x424 = Stack.pop();
SymVal x425 = SymStack.pop();
Num x426 = Stack.pop();
SymStack.pop();
int x427 = x426.toInt();
Memory.storeInt(x427, 8, x424.toInt());
SymMemory.storeSym(x427, 8, x425);
}
info("Jump to 0");
__attribute__((musttail)) return x428(std::monostate{});
return std::monostate{};
}
std::monostate x308(std::monostate x309) {
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x310 = Stack.pop();
SymStack.pop();
Num x311 = I32V(Memory.loadInt(x310.toInt(), 4));
SymVal x312 = SymMemory.loadSym(x310.toInt(), 4);
Stack.push(x311);
SymStack.push(x312);
}
{
Num x313 = Stack.pop();
SymVal x314 = SymStack.pop();
Frames.set(2, x313);
SymFrames.set(2, x314);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x315 = Stack.pop();
SymVal x316 = SymStack.pop();
Num x317 = Stack.pop();
SymVal x318 = SymStack.pop();
Num x319 = x317.i32_shl(x315);
Stack.push(x319);
bool x320 = allConcrete(x318, x316);
SymVal x321 = x320 ? Concrete(x319, 32) : x318.shl(x316);
SymStack.push(x321);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
Stack.push(Globals.get(1));
SymStack.push(SymGlobals.get(1));
{
Num x322 = Stack.pop();
SymVal x323 = SymStack.pop();
Num x324 = Stack.pop();
SymVal x325 = SymStack.pop();
Num x326 = x324.i32_add(x322);
Stack.push(x326);
bool x327 = allConcrete(x325, x323);
SymVal x328 = x327 ? Concrete(x326, 32) : x325.add(x323);
SymStack.push(x328);
}
{
Num x329 = Stack.pop();
SymVal x330 = SymStack.pop();
Num x331 = Stack.pop();
SymVal x332 = SymStack.pop();
Num x333 = x331.i32_add(x329);
Stack.push(x333);
bool x334 = allConcrete(x332, x330);
SymVal x335 = x334 ? Concrete(x333, 32) : x332.add(x330);
SymStack.push(x335);
}
{
Num x336 = Stack.pop();
SymVal x337 = SymStack.pop();
Frames.set(3, x336);
SymFrames.set(3, x337);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x338 = Stack.pop();
SymStack.pop();
Num x339 = I32V(Memory.loadInt(x338.toInt(), 0));
SymVal x340 = SymMemory.loadSym(x338.toInt(), 0);
Stack.push(x339);
SymStack.push(x340);
}
{
Num x341 = Stack.pop();
SymVal x342 = SymStack.pop();
Num x343 = Stack.pop();
SymVal x344 = SymStack.pop();
Num x345 = x343.i32_mul(x341);
Stack.push(x345);
bool x346 = allConcrete(x344, x342);
SymVal x347 = x346 ? Concrete(x345, 32) : x344.mul(x342);
SymStack.push(x347);
}
Stack.push(I32V(7));
SymStack.push(Concrete(I32V(7), 32));
{
Num x348 = Stack.pop();
SymVal x349 = SymStack.pop();
Num x350 = Stack.pop();
SymVal x351 = SymStack.pop();
Num x352 = x350.i32_mul(x348);
Stack.push(x352);
bool x353 = allConcrete(x351, x349);
SymVal x354 = x353 ? Concrete(x352, 32) : x351.mul(x349);
SymStack.push(x354);
}
{
Num x355 = Stack.pop();
SymVal x356 = SymStack.pop();
Num x357 = Stack.pop();
SymVal x358 = SymStack.pop();
Num x359 = x357.i32_add(x355);
Stack.push(x359);
bool x360 = allConcrete(x358, x356);
SymVal x361 = x360 ? Concrete(x359, 32) : x358.add(x356);
SymStack.push(x361);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(-4));
SymStack.push(Concrete(I32V(-4), 32));
{
Num x362 = Stack.pop();
SymVal x363 = SymStack.pop();
Num x364 = Stack.pop();
SymVal x365 = SymStack.pop();
Num x366 = x364.i32_add(x362);
Stack.push(x366);
bool x367 = allConcrete(x365, x363);
SymVal x368 = x367 ? Concrete(x366, 32) : x365.add(x363);
SymStack.push(x368);
}
{
Num x369 = Stack.pop();
SymStack.pop();
Num x370 = I32V(Memory.loadInt(x369.toInt(), 0));
SymVal x371 = SymMemory.loadSym(x369.toInt(), 0);
Stack.push(x370);
SymStack.push(x371);
}
{
Num x372 = Stack.pop();
SymVal x373 = SymStack.pop();
Num x374 = Stack.pop();
SymVal x375 = SymStack.pop();
Num x376 = x374.i32_mul(x372);
Stack.push(x376);
bool x377 = allConcrete(x375, x373);
SymVal x378 = x377 ? Concrete(x376, 32) : x375.mul(x373);
SymStack.push(x378);
}
{
Num x379 = Stack.pop();
SymVal x380 = SymStack.pop();
Num x381 = Stack.pop();
SymVal x382 = SymStack.pop();
Num x383 = x381.i32_add(x379);
Stack.push(x383);
bool x384 = allConcrete(x382, x380);
SymVal x385 = x384 ? Concrete(x383, 32) : x382.add(x380);
SymStack.push(x385);
}
Stack.push(I32V(3));
SymStack.push(Concrete(I32V(3), 32));
{
Num x386 = Stack.pop();
SymVal x387 = SymStack.pop();
Num x388 = Stack.pop();
SymVal x389 = SymStack.pop();
Num x390 = x388.i32_add(x386);
Stack.push(x390);
bool x391 = allConcrete(x389, x387);
SymVal x392 = x391 ? Concrete(x390, 32) : x389.add(x387);
SymStack.push(x392);
}
{
Num x393 = Stack.pop();
SymVal x394 = SymStack.pop();
Num x395 = Stack.pop();
SymStack.pop();
int x396 = x395.toInt();
Memory.storeInt(x396, 0, x393.toInt());
SymMemory.storeSym(x396, 0, x394);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x397 = Stack.pop();
SymStack.pop();
Num x398 = I32V(Memory.loadInt(x397.toInt(), 4));
SymVal x399 = SymMemory.loadSym(x397.toInt(), 4);
Stack.push(x398);
SymStack.push(x399);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x400 = Stack.pop();
SymVal x401 = SymStack.pop();
Num x402 = Stack.pop();
SymVal x403 = SymStack.pop();
Num x404 = x402.i32_add(x400);
Stack.push(x404);
bool x405 = allConcrete(x403, x401);
SymVal x406 = x405 ? Concrete(x404, 32) : x403.add(x401);
SymStack.push(x406);
}
{
Num x407 = Stack.pop();
SymVal x408 = SymStack.pop();
Num x409 = Stack.pop();
SymStack.pop();
int x410 = x409.toInt();
Memory.storeInt(x410, 4, x407.toInt());
SymMemory.storeSym(x410, 4, x408);
}
info("Jump to 0");
__attribute__((musttail)) return x411(std::monostate{});
return std::monostate{};
}
std::monostate x278(std::monostate x279) {
infoWhen("CALL", "Entered the function at 4, stackSize =", Stack.size());
Frames.pushFrameCallee(8);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Stack.push(Globals.get(0));
SymStack.push(SymGlobals.get(0));
Stack.push(I32V(16));
SymStack.push(Concrete(I32V(16), 32));
{
Num x280 = Stack.pop();
SymVal x281 = SymStack.pop();
Num x282 = Stack.pop();
SymVal x283 = SymStack.pop();
Num x284 = x282.i32_sub(x280);
Stack.push(x284);
bool x285 = allConcrete(x283, x281);
SymVal x286 = x285 ? Concrete(x284, 32) : x283.minus(x281);
SymStack.push(x286);
}
{
Num x287 = Stack.pop();
SymVal x288 = SymStack.pop();
Frames.set(2, x287);
SymFrames.set(2, x288);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x289 = Stack.pop();
SymVal x290 = SymStack.pop();
Num x291 = Stack.pop();
SymStack.pop();
int x292 = x291.toInt();
Memory.storeInt(x292, 12, x289.toInt());
SymMemory.storeSym(x292, 12, x290);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x293 = Stack.pop();
SymVal x294 = SymStack.pop();
Num x295 = Stack.pop();
SymStack.pop();
int x296 = x295.toInt();
Memory.storeInt(x296, 8, x293.toInt());
SymMemory.storeSym(x296, 8, x294);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x297 = Stack.pop();
SymStack.pop();
Num x298 = I32V(Memory.loadInt(x297.toInt(), 12));
SymVal x299 = SymMemory.loadSym(x297.toInt(), 12);
Stack.push(x298);
SymStack.push(x299);
}
{
Num x300 = Stack.pop();
SymVal x301 = SymStack.pop();
Num x302 = Stack.pop();
SymStack.pop();
int x303 = x302.toInt();
Memory.storeInt(x303, 4, x300.toInt());
SymMemory.storeSym(x303, 4, x301);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x304 = Stack.pop();
SymVal x305 = SymStack.pop();
Num x306 = Stack.pop();
SymStack.pop();
int x307 = x306.toInt();
Memory.storeInt(x307, 0, x304.toInt());
SymMemory.storeSym(x307, 0, x305);
}
__attribute__((musttail)) return x276(std::monostate{});
return std::monostate{};
}
std::monostate x276(std::monostate x277) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x233(std::monostate{});
return std::monostate{};
}
std::monostate x233(std::monostate x240) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x241 = Stack.pop();
SymStack.pop();
Num x242 = I32V(Memory.loadInt(x241.toInt(), 4));
SymVal x243 = SymMemory.loadSym(x241.toInt(), 4);
Stack.push(x242);
SymStack.push(x243);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x244 = Stack.pop();
SymStack.pop();
Num x245 = I32V(Memory.loadInt(x244.toInt(), 12));
SymVal x246 = SymMemory.loadSym(x244.toInt(), 12);
Stack.push(x245);
SymStack.push(x246);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x247 = Stack.pop();
SymStack.pop();
Num x248 = I32V(Memory.loadInt(x247.toInt(), 8));
SymVal x249 = SymMemory.loadSym(x247.toInt(), 8);
Stack.push(x248);
SymStack.push(x249);
}
{
Num x250 = Stack.pop();
SymVal x251 = SymStack.pop();
Num x252 = Stack.pop();
SymVal x253 = SymStack.pop();
Num x254 = x252.i32_add(x250);
Stack.push(x254);
bool x255 = allConcrete(x253, x251);
SymVal x256 = x255 ? Concrete(x254, 32) : x253.add(x251);
SymStack.push(x256);
}
{
Num x257 = Stack.pop();
SymVal x258 = SymStack.pop();
Num x259 = Stack.pop();
SymVal x260 = SymStack.pop();
Num x261 = x259.i32_lt_s(x257);
Stack.push(x261);
bool x262 = allConcrete(x260, x258);
SymVal x263 = x262 ? Concrete(x261, 32) : x260.lt(x258).bool2bv();
SymStack.push(x263);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x264 = Stack.pop();
SymVal x265 = SymStack.pop();
Num x266 = Stack.pop();
SymVal x267 = SymStack.pop();
Num x268 = x266.i32_and(x264);
Stack.push(x268);
bool x269 = allConcrete(x267, x265);
SymVal x270 = x269 ? Concrete(x268, 32) : x267.bitwise_and(x265);
SymStack.push(x270);
}
{
Num x271 = Stack.pop();
SymVal x272 = SymStack.pop();
Stack.push(I32V((0 == x271.toInt())));
SymStack.push(x272.is_zero().bool2bv());
}
Num x273 = Stack.pop();
info("The br_if(1)'s condition is ", x273.toInt());
{
SymVal x274 = SymStack.pop();
ExploreTree.fillIfElseNode(x274, 0);
}
int x275 = x273.toInt();
if (x275 != 0) {
info("Jump to 1");
{
ExploreTree.moveCursor(true, makeControl(x124, CURRENT_MCONT));
}
__attribute__((musttail)) return x238(std::monostate{});
} else {
info("Continue rest of the block");
{
ExploreTree.moveCursor(false, makeControl(x238, CURRENT_MCONT));
}
__attribute__((musttail)) return x124(std::monostate{});
}
return std::monostate{};
}
std::monostate x238(std::monostate x239) {
__attribute__((musttail)) return x236(std::monostate{});
return std::monostate{};
}
std::monostate x236(std::monostate x237) {
info("Exiting the block, stackSize =", Stack.size());
return x234(std::monostate{});
}
std::monostate x234(std::monostate x235) {
infoWhen("CALL", "Exiting the function at 4, stackSize =", Stack.size());
Frames.popFrameCallee(8);
SymFrames.popFrameCallee(8);
return enterCC(std::monostate());
}
std::monostate x124(std::monostate x125) {
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x126 = Stack.pop();
SymStack.pop();
Num x127 = I32V(Memory.loadInt(x126.toInt(), 0));
SymVal x128 = SymMemory.loadSym(x126.toInt(), 0);
Stack.push(x127);
SymStack.push(x128);
}
{
Num x129 = Stack.pop();
SymVal x130 = SymStack.pop();
Frames.set(3, x129);
SymFrames.set(3, x130);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x131 = Stack.pop();
SymVal x132 = SymStack.pop();
Frames.set(4, x131);
SymFrames.set(4, x132);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(Frames.get(4));
SymStack.push(SymFrames.get(4));
{
Num x133 = Stack.pop();
SymVal x134 = SymStack.pop();
Num x135 = Stack.pop();
SymVal x136 = SymStack.pop();
Num x137 = x135.i32_shl(x133);
Stack.push(x137);
bool x138 = allConcrete(x136, x134);
SymVal x139 = x138 ? Concrete(x137, 32) : x136.shl(x134);
SymStack.push(x139);
}
{
Num x140 = Stack.pop();
SymVal x141 = SymStack.pop();
Frames.set(5, x140);
SymFrames.set(5, x141);
}
Stack.push(I32V(1424));
SymStack.push(Concrete(I32V(1424), 32));
{
Num x142 = Stack.pop();
SymVal x143 = SymStack.pop();
Frames.set(6, x142);
SymFrames.set(6, x143);
}
Stack.push(Globals.get(1));
SymStack.push(SymGlobals.get(1));
{
Num x144 = Stack.pop();
SymVal x145 = SymStack.pop();
Frames.set(7, x144);
SymFrames.set(7, x145);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(Frames.get(7));
SymStack.push(SymFrames.get(7));
Stack.push(Frames.get(6));
SymStack.push(SymFrames.get(6));
{
Num x146 = Stack.pop();
SymVal x147 = SymStack.pop();
Num x148 = Stack.pop();
SymVal x149 = SymStack.pop();
Num x150 = x148.i32_add(x146);
Stack.push(x150);
bool x151 = allConcrete(x149, x147);
SymVal x152 = x151 ? Concrete(x150, 32) : x149.add(x147);
SymStack.push(x152);
}
{
Num x153 = Stack.pop();
SymVal x154 = SymStack.pop();
Num x155 = Stack.pop();
SymVal x156 = SymStack.pop();
Num x157 = x155.i32_add(x153);
Stack.push(x157);
bool x158 = allConcrete(x156, x154);
SymVal x159 = x158 ? Concrete(x157, 32) : x156.add(x154);
SymStack.push(x159);
}
{
Num x160 = Stack.pop();
SymStack.pop();
Num x161 = I32V(Memory.loadInt(x160.toInt(), 0));
SymVal x162 = SymMemory.loadSym(x160.toInt(), 0);
Stack.push(x161);
SymStack.push(x162);
}
{
Num x163 = Stack.pop();
SymVal x164 = SymStack.pop();
Frames.set(8, x163);
SymFrames.set(8, x164);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x165 = Stack.pop();
SymStack.pop();
Num x166 = I32V(Memory.loadInt(x165.toInt(), 12));
SymVal x167 = SymMemory.loadSym(x165.toInt(), 12);
Stack.push(x166);
SymStack.push(x167);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x168 = Stack.pop();
SymStack.pop();
Num x169 = I32V(Memory.loadInt(x168.toInt(), 4));
SymVal x170 = SymMemory.loadSym(x168.toInt(), 4);
Stack.push(x169);
SymStack.push(x170);
}
{
Num x171 = Stack.pop();
SymVal x172 = SymStack.pop();
Num x173 = Stack.pop();
SymVal x174 = SymStack.pop();
Num x175 = x173.i32_add(x171);
Stack.push(x175);
bool x176 = allConcrete(x174, x172);
SymVal x177 = x176 ? Concrete(x175, 32) : x174.add(x172);
SymStack.push(x177);
}
{
Num x178 = Stack.pop();
SymVal x179 = SymStack.pop();
Frames.set(9, x178);
SymFrames.set(9, x179);
}
Stack.push(Frames.get(7));
SymStack.push(SymFrames.get(7));
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
{
Num x180 = Stack.pop();
SymVal x181 = SymStack.pop();
Num x182 = Stack.pop();
SymVal x183 = SymStack.pop();
Num x184 = x182.i32_add(x180);
Stack.push(x184);
bool x185 = allConcrete(x183, x181);
SymVal x186 = x185 ? Concrete(x184, 32) : x183.add(x181);
SymStack.push(x186);
}
Stack.push(Frames.get(9));
SymStack.push(SymFrames.get(9));
Stack.push(Frames.get(4));
SymStack.push(SymFrames.get(4));
{
Num x187 = Stack.pop();
SymVal x188 = SymStack.pop();
Num x189 = Stack.pop();
SymVal x190 = SymStack.pop();
Num x191 = x189.i32_shl(x187);
Stack.push(x191);
bool x192 = allConcrete(x190, x188);
SymVal x193 = x192 ? Concrete(x191, 32) : x190.shl(x188);
SymStack.push(x193);
}
{
Num x194 = Stack.pop();
SymVal x195 = SymStack.pop();
Num x196 = Stack.pop();
SymVal x197 = SymStack.pop();
Num x198 = x196.i32_add(x194);
Stack.push(x198);
bool x199 = allConcrete(x197, x195);
SymVal x200 = x199 ? Concrete(x198, 32) : x197.add(x195);
SymStack.push(x200);
}
Stack.push(Frames.get(8));
SymStack.push(SymFrames.get(8));
{
Num x201 = Stack.pop();
SymVal x202 = SymStack.pop();
Num x203 = Stack.pop();
SymStack.pop();
int x204 = x203.toInt();
Memory.storeInt(x204, 0, x201.toInt());
SymMemory.storeSym(x204, 0, x202);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x205 = Stack.pop();
SymStack.pop();
Num x206 = I32V(Memory.loadInt(x205.toInt(), 4));
SymVal x207 = SymMemory.loadSym(x205.toInt(), 4);
Stack.push(x206);
SymStack.push(x207);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x208 = Stack.pop();
SymVal x209 = SymStack.pop();
Num x210 = Stack.pop();
SymVal x211 = SymStack.pop();
Num x212 = x210.i32_add(x208);
Stack.push(x212);
bool x213 = allConcrete(x211, x209);
SymVal x214 = x213 ? Concrete(x212, 32) : x211.add(x209);
SymStack.push(x214);
}
{
Num x215 = Stack.pop();
SymVal x216 = SymStack.pop();
Num x217 = Stack.pop();
SymStack.pop();
int x218 = x217.toInt();
Memory.storeInt(x218, 4, x215.toInt());
SymMemory.storeSym(x218, 4, x216);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x219 = Stack.pop();
SymStack.pop();
Num x220 = I32V(Memory.loadInt(x219.toInt(), 0));
SymVal x221 = SymMemory.loadSym(x219.toInt(), 0);
Stack.push(x220);
SymStack.push(x221);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x222 = Stack.pop();
SymVal x223 = SymStack.pop();
Num x224 = Stack.pop();
SymVal x225 = SymStack.pop();
Num x226 = x224.i32_add(x222);
Stack.push(x226);
bool x227 = allConcrete(x225, x223);
SymVal x228 = x227 ? Concrete(x226, 32) : x225.add(x223);
SymStack.push(x228);
}
{
Num x229 = Stack.pop();
SymVal x230 = SymStack.pop();
Num x231 = Stack.pop();
SymStack.pop();
int x232 = x231.toInt();
Memory.storeInt(x232, 0, x229.toInt());
SymMemory.storeSym(x232, 0, x230);
}
info("Jump to 0");
__attribute__((musttail)) return x233(std::monostate{});
return std::monostate{};
}
std::monostate x107(std::monostate x108) {
infoWhen("CALL", "Entered the function at 3, stackSize =", Stack.size());
Frames.pushFrameCallee(3);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Stack.push(Globals.get(0));
SymStack.push(SymGlobals.get(0));
Stack.push(I32V(16));
SymStack.push(Concrete(I32V(16), 32));
{
Num x109 = Stack.pop();
SymVal x110 = SymStack.pop();
Num x111 = Stack.pop();
SymVal x112 = SymStack.pop();
Num x113 = x111.i32_sub(x109);
Stack.push(x113);
bool x114 = allConcrete(x112, x110);
SymVal x115 = x114 ? Concrete(x113, 32) : x112.minus(x110);
SymStack.push(x115);
}
{
Num x116 = Stack.pop();
SymVal x117 = SymStack.pop();
Frames.set(0, x116);
SymFrames.set(0, x117);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x118 = Stack.pop();
SymVal x119 = SymStack.pop();
Globals.set(0, x118);
SymGlobals.set(0, x119);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x120 = Stack.pop();
SymVal x121 = SymStack.pop();
Num x122 = Stack.pop();
SymStack.pop();
int x123 = x122.toInt();
Memory.storeInt(x123, 12, x120.toInt());
SymMemory.storeSym(x123, 12, x121);
}
__attribute__((musttail)) return x105(std::monostate{});
return std::monostate{};
}
std::monostate x105(std::monostate x106) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x66(std::monostate{});
return std::monostate{};
}
std::monostate x66(std::monostate x82) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x83 = Stack.pop();
SymStack.pop();
Num x84 = I32V(Memory.loadInt(x83.toInt(), 12));
SymVal x85 = SymMemory.loadSym(x83.toInt(), 12);
Stack.push(x84);
SymStack.push(x85);
}
Stack.push(I32V(3));
SymStack.push(Concrete(I32V(3), 32));
{
Num x86 = Stack.pop();
SymVal x87 = SymStack.pop();
Num x88 = Stack.pop();
SymVal x89 = SymStack.pop();
Num x90 = x88.i32_lt_s(x86);
Stack.push(x90);
bool x91 = allConcrete(x89, x87);
SymVal x92 = x91 ? Concrete(x90, 32) : x89.lt(x87).bool2bv();
SymStack.push(x92);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x93 = Stack.pop();
SymVal x94 = SymStack.pop();
Num x95 = Stack.pop();
SymVal x96 = SymStack.pop();
Num x97 = x95.i32_and(x93);
Stack.push(x97);
bool x98 = allConcrete(x96, x94);
SymVal x99 = x98 ? Concrete(x97, 32) : x96.bitwise_and(x94);
SymStack.push(x99);
}
{
Num x100 = Stack.pop();
SymVal x101 = SymStack.pop();
Stack.push(I32V((0 == x100.toInt())));
SymStack.push(x101.is_zero().bool2bv());
}
Num x102 = Stack.pop();
info("The br_if(1)'s condition is ", x102.toInt());
{
SymVal x103 = SymStack.pop();
ExploreTree.fillIfElseNode(x103, 0);
}
int x104 = x102.toInt();
if (x104 != 0) {
info("Jump to 1");
{
ExploreTree.moveCursor(true, makeControl(x13, CURRENT_MCONT));
}
__attribute__((musttail)) return x80(std::monostate{});
} else {
info("Continue rest of the block");
{
ExploreTree.moveCursor(false, makeControl(x80, CURRENT_MCONT));
}
__attribute__((musttail)) return x13(std::monostate{});
}
return std::monostate{};
}
std::monostate x80(std::monostate x81) {
__attribute__((musttail)) return x69(std::monostate{});
return std::monostate{};
}
std::monostate x69(std::monostate x70) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(16));
SymStack.push(Concrete(I32V(16), 32));
{
Num x71 = Stack.pop();
SymVal x72 = SymStack.pop();
Num x73 = Stack.pop();
SymVal x74 = SymStack.pop();
Num x75 = x73.i32_add(x71);
Stack.push(x75);
bool x76 = allConcrete(x74, x72);
SymVal x77 = x76 ? Concrete(x75, 32) : x74.add(x72);
SymStack.push(x77);
}
{
Num x78 = Stack.pop();
SymVal x79 = SymStack.pop();
Globals.set(0, x78);
SymGlobals.set(0, x79);
}
return x67(std::monostate{});
}
std::monostate x67(std::monostate x68) {
infoWhen("CALL", "Exiting the function at 3, stackSize =", Stack.size());
Frames.popFrameCallee(3);
SymFrames.popFrameCallee(3);
return enterCC(std::monostate());
}
std::monostate x13(std::monostate x14) {
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x15 = Stack.pop();
SymStack.pop();
Num x16 = I32V(Memory.loadInt(x15.toInt(), 12));
SymVal x17 = SymMemory.loadSym(x15.toInt(), 12);
Stack.push(x16);
SymStack.push(x17);
}
{
Stack.pop();
SymVal x18 = SymStack.pop();
SymVal x19 = x18.makeI32Symbol();
Stack.push(SymEnv.read(x19));
SymStack.push(x19);
}
{
Num x20 = Stack.pop();
SymVal x21 = SymStack.pop();
Frames.set(1, x20);
SymFrames.set(1, x21);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x22 = Stack.pop();
SymStack.pop();
Num x23 = I32V(Memory.loadInt(x22.toInt(), 12));
SymVal x24 = SymMemory.loadSym(x22.toInt(), 12);
Stack.push(x23);
SymStack.push(x24);
}
{
Num x25 = Stack.pop();
SymVal x26 = SymStack.pop();
Frames.set(2, x25);
SymFrames.set(2, x26);
}
Stack.push(I32V(1424));
SymStack.push(Concrete(I32V(1424), 32));
Stack.push(Globals.get(1));
SymStack.push(SymGlobals.get(1));
{
Num x27 = Stack.pop();
SymVal x28 = SymStack.pop();
Num x29 = Stack.pop();
SymVal x30 = SymStack.pop();
Num x31 = x29.i32_add(x27);
Stack.push(x31);
bool x32 = allConcrete(x30, x28);
SymVal x33 = x32 ? Concrete(x31, 32) : x30.add(x28);
SymStack.push(x33);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x34 = Stack.pop();
SymVal x35 = SymStack.pop();
Num x36 = Stack.pop();
SymVal x37 = SymStack.pop();
Num x38 = x36.i32_shl(x34);
Stack.push(x38);
bool x39 = allConcrete(x37, x35);
SymVal x40 = x39 ? Concrete(x38, 32) : x37.shl(x35);
SymStack.push(x40);
}
{
Num x41 = Stack.pop();
SymVal x42 = SymStack.pop();
Num x43 = Stack.pop();
SymVal x44 = SymStack.pop();
Num x45 = x43.i32_add(x41);
Stack.push(x45);
bool x46 = allConcrete(x44, x42);
SymVal x47 = x46 ? Concrete(x45, 32) : x44.add(x42);
SymStack.push(x47);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x48 = Stack.pop();
SymVal x49 = SymStack.pop();
Num x50 = Stack.pop();
SymStack.pop();
int x51 = x50.toInt();
Memory.storeInt(x51, 0, x48.toInt());
SymMemory.storeSym(x51, 0, x49);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x52 = Stack.pop();
SymStack.pop();
Num x53 = I32V(Memory.loadInt(x52.toInt(), 12));
SymVal x54 = SymMemory.loadSym(x52.toInt(), 12);
Stack.push(x53);
SymStack.push(x54);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x55 = Stack.pop();
SymVal x56 = SymStack.pop();
Num x57 = Stack.pop();
SymVal x58 = SymStack.pop();
Num x59 = x57.i32_add(x55);
Stack.push(x59);
bool x60 = allConcrete(x58, x56);
SymVal x61 = x60 ? Concrete(x59, 32) : x58.add(x56);
SymStack.push(x61);
}
{
Num x62 = Stack.pop();
SymVal x63 = SymStack.pop();
Num x64 = Stack.pop();
SymStack.pop();
int x65 = x64.toInt();
Memory.storeInt(x65, 12, x62.toInt());
SymMemory.storeSym(x65, 12, x63);
}
info("Jump to 0");
__attribute__((musttail)) return x66(std::monostate{});
return std::monostate{};
}
std::monostate x11(std::monostate x12) {
info("Exiting the entry function");
Frames.popFrameCaller(0);
SymFrames.popFrameCaller(0);
return enterCC(std::monostate());
}
std::monostate x9(std::monostate x10) {
info("Initializing memory...");
return std::monostate{};
}
std::monostate x7(std::monostate x8) {
info("Initializing function table...");
updateCurrentMCont(MCont_t(x5));
return std::monostate{};
}
std::monostate x5(std::monostate x6) {
return std::monostate{};
}
std::monostate x3(std::monostate x4) {
info("Initializing globals...");
Globals.pushFrameCaller(2);
SymGlobals.pushFramePtr();
SymGlobals.pushFrameSlot(32);
SymGlobals.pushFrameSlot(32);
Globals.set(0, I32V(66976));
SymGlobals.set(0, Concrete(I32V(66976), 32));
Globals.set(1, I32V(0));
SymGlobals.set(1, Concrete(I32V(0), 32));
return std::monostate{};
}
std::monostate x1(std::monostate x2) {
info("Exiting the program...");
ExploreTree.fillFinishedNode();
return std::monostate();
}
std::monostate Snippet(std::monostate x0) {
x3(std::monostate{});
x7(std::monostate{});
x9(std::monostate{});
Frames.pushFrameCaller(0);
SymFrames.pushFramePtr();
{
updateCurrentMCont(prependCont(x11, MCont_t(x1)));
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Frames.pushFrameCaller(0);
SymFrames.pushFramePtr();
updateCurrentMCont(prependCont(x1126, CURRENT_MCONT));
}
__attribute__((musttail)) return x1107(std::monostate{});
return std::monostate{};
}

/*****************************************
End of Generated Code
*******************************************/
int main(int argc, char *argv[]) {
  start_concolic_execution_with(Snippet, 2);
  return 0;
}
