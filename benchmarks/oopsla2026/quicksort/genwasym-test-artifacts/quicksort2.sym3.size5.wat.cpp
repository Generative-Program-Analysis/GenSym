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
std::monostate x91(std::monostate);
std::monostate x93(std::monostate);
std::monostate x104(std::monostate);
std::monostate x90(std::monostate);
std::monostate x142(std::monostate);
std::monostate x144(std::monostate);
std::monostate x176(std::monostate);
std::monostate x280(std::monostate);
std::monostate x297(std::monostate);
std::monostate x279(std::monostate);
std::monostate x325(std::monostate);
std::monostate x327(std::monostate);
std::monostate x333(std::monostate);
std::monostate x335(std::monostate);
std::monostate x337(std::monostate);
std::monostate x296(std::monostate);
std::monostate x362(std::monostate);
std::monostate x364(std::monostate);
std::monostate x383(std::monostate);
std::monostate x385(std::monostate);
std::monostate x396(std::monostate);
std::monostate x418(std::monostate);
std::monostate x465(std::monostate);
std::monostate x467(std::monostate);
std::monostate x469(std::monostate);
std::monostate x464(std::monostate);
std::monostate x497(std::monostate);
std::monostate x499(std::monostate);
std::monostate x525(std::monostate);
std::monostate x542(std::monostate);
std::monostate x664(std::monostate);
std::monostate x666(std::monostate);
std::monostate x717(std::monostate);
std::monostate x719(std::monostate);
std::monostate x721(std::monostate);
std::monostate x732(std::monostate);
std::monostate x734(std::monostate);
std::monostate x754(std::monostate);
std::monostate x773(std::monostate);
std::monostate x541(std::monostate);
std::monostate x801(std::monostate);
std::monostate x803(std::monostate);
std::monostate x857(std::monostate);
std::monostate x859(std::monostate);
std::monostate x753(std::monostate);
std::monostate x906(std::monostate);
std::monostate x908(std::monostate);
std::monostate x910(std::monostate);
std::monostate x916(std::monostate);
std::monostate x922(std::monostate);
std::monostate x928(std::monostate);
std::monostate x934(std::monostate);
std::monostate x942(std::monostate);
std::monostate x948(std::monostate);
std::monostate x952(std::monostate);
std::monostate x958(std::monostate);
std::monostate x960(std::monostate);
std::monostate x973(std::monostate);
std::monostate x990(std::monostate);
std::monostate x992(std::monostate);

/************* Functions **************/
std::monostate x992(std::monostate x993) {
infoWhen("CALL", "Returning from the function at 7, stackSize =", Stack.size());
Frames.popFrameCaller(0);
SymFrames.popFrameCaller(0);
return x990(std::monostate{});
}
std::monostate x990(std::monostate x991) {
return enterCC(std::monostate());
}
std::monostate x973(std::monostate x974) {
infoWhen("CALL", "Entered the function at 7, stackSize =", Stack.size());
Frames.pushFrameCallee(2);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Stack.push(Globals.get(0));
SymStack.push(SymGlobals.get(0));
Stack.push(I32V(16));
SymStack.push(Concrete(I32V(16), 32));
{
Num x975 = Stack.pop();
SymVal x976 = SymStack.pop();
Num x977 = Stack.pop();
SymVal x978 = SymStack.pop();
Num x979 = x977.i32_sub(x975);
Stack.push(x979);
bool x980 = allConcrete(x978, x976);
SymVal x981 = x980 ? Concrete(x979, 32) : x978.minus(x976);
SymStack.push(x981);
}
{
Num x982 = Stack.pop();
SymVal x983 = SymStack.pop();
Frames.set(0, x982);
SymFrames.set(0, x983);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x984 = Stack.pop();
SymVal x985 = SymStack.pop();
Globals.set(0, x984);
SymGlobals.set(0, x985);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x986 = Stack.pop();
SymVal x987 = SymStack.pop();
Num x988 = Stack.pop();
SymStack.pop();
int x989 = x988.toInt();
Memory.storeInt(x989, 12, x986.toInt());
SymMemory.storeSym(x989, 12, x987);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 2);
Frames.pushFrameCaller(0);
SymFrames.pushFramePtr();
updateCurrentMCont(prependCont(x960, CURRENT_MCONT));
}
__attribute__((musttail)) return x952(std::monostate{});
return std::monostate{};
}
std::monostate x960(std::monostate x961) {
infoWhen("CALL", "Returning from the function at 2, stackSize =", Stack.size());
Frames.popFrameCaller(0);
SymFrames.popFrameCaller(0);
{
Num x962 = Stack.pop();
SymVal x963 = SymStack.pop();
Frames.set(1, x962);
SymFrames.set(1, x963);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(16));
SymStack.push(Concrete(I32V(16), 32));
{
Num x964 = Stack.pop();
SymVal x965 = SymStack.pop();
Num x966 = Stack.pop();
SymVal x967 = SymStack.pop();
Num x968 = x966.i32_add(x964);
Stack.push(x968);
bool x969 = allConcrete(x967, x965);
SymVal x970 = x969 ? Concrete(x968, 32) : x967.add(x965);
SymStack.push(x970);
}
{
Num x971 = Stack.pop();
SymVal x972 = SymStack.pop();
Globals.set(0, x971);
SymGlobals.set(0, x972);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
return x958(std::monostate{});
}
std::monostate x958(std::monostate x959) {
infoWhen("CALL", "Exiting the function at 7, stackSize =", Stack.size());
Frames.popFrameCallee(2);
SymFrames.popFrameCallee(2);
return enterCC(std::monostate());
}
std::monostate x952(std::monostate x953) {
infoWhen("CALL", "Entered the function at 2, stackSize =", Stack.size());
Frames.pushFrameCallee(1);
SymFrames.pushFrameSlot(32);
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
Stack.push(I32V(3));
SymStack.push(Concrete(I32V(3), 32));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 3);
Num x954 = Stack.pop();
Num x955 = Stack.pop();
SymVal x956 = SymStack.pop();
SymVal x957 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x955);
Frames.set(1, x954);
SymFrames.set(0, x957);
SymFrames.set(1, x956);
updateCurrentMCont(prependCont(x948, CURRENT_MCONT));
}
__attribute__((musttail)) return x144(std::monostate{});
return std::monostate{};
}
std::monostate x948(std::monostate x949) {
infoWhen("CALL", "Returning from the function at 3, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(3));
SymStack.push(Concrete(I32V(3), 32));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 4);
Num x950 = Stack.pop();
SymVal x951 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x950);
SymFrames.set(0, x951);
updateCurrentMCont(prependCont(x942, CURRENT_MCONT));
}
__attribute__((musttail)) return x396(std::monostate{});
return std::monostate{};
}
std::monostate x942(std::monostate x943) {
infoWhen("CALL", "Returning from the function at 4, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
Stack.push(I32V(3));
SymStack.push(Concrete(I32V(3), 32));
Stack.push(I32V(5));
SymStack.push(Concrete(I32V(5), 32));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 5);
Num x944 = Stack.pop();
Num x945 = Stack.pop();
SymVal x946 = SymStack.pop();
SymVal x947 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x945);
Frames.set(1, x944);
SymFrames.set(0, x947);
SymFrames.set(1, x946);
updateCurrentMCont(prependCont(x934, CURRENT_MCONT));
}
__attribute__((musttail)) return x499(std::monostate{});
return std::monostate{};
}
std::monostate x934(std::monostate x935) {
infoWhen("CALL", "Returning from the function at 5, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(3));
SymStack.push(Concrete(I32V(3), 32));
{
Num x936 = Stack.pop();
SymVal x937 = SymStack.pop();
Frames.set(0, x936);
SymFrames.set(0, x937);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 3);
Num x938 = Stack.pop();
Num x939 = Stack.pop();
SymVal x940 = SymStack.pop();
SymVal x941 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x939);
Frames.set(1, x938);
SymFrames.set(0, x941);
SymFrames.set(1, x940);
updateCurrentMCont(prependCont(x928, CURRENT_MCONT));
}
__attribute__((musttail)) return x144(std::monostate{});
return std::monostate{};
}
std::monostate x928(std::monostate x929) {
infoWhen("CALL", "Returning from the function at 3, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(3));
SymStack.push(Concrete(I32V(3), 32));
Stack.push(I32V(7));
SymStack.push(Concrete(I32V(7), 32));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 6);
Num x930 = Stack.pop();
Num x931 = Stack.pop();
SymVal x932 = SymStack.pop();
SymVal x933 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x931);
Frames.set(1, x930);
SymFrames.set(0, x933);
SymFrames.set(1, x932);
updateCurrentMCont(prependCont(x922, CURRENT_MCONT));
}
__attribute__((musttail)) return x753(std::monostate{});
return std::monostate{};
}
std::monostate x922(std::monostate x923) {
infoWhen("CALL", "Returning from the function at 6, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
Stack.push(I32V(5));
SymStack.push(Concrete(I32V(5), 32));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 5);
Num x924 = Stack.pop();
Num x925 = Stack.pop();
SymVal x926 = SymStack.pop();
SymVal x927 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x925);
Frames.set(1, x924);
SymFrames.set(0, x927);
SymFrames.set(1, x926);
updateCurrentMCont(prependCont(x916, CURRENT_MCONT));
}
__attribute__((musttail)) return x499(std::monostate{});
return std::monostate{};
}
std::monostate x916(std::monostate x917) {
infoWhen("CALL", "Returning from the function at 5, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
Stack.push(I32V(3));
SymStack.push(Concrete(I32V(3), 32));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 3);
Num x918 = Stack.pop();
Num x919 = Stack.pop();
SymVal x920 = SymStack.pop();
SymVal x921 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x919);
Frames.set(1, x918);
SymFrames.set(0, x921);
SymFrames.set(1, x920);
updateCurrentMCont(prependCont(x910, CURRENT_MCONT));
}
__attribute__((musttail)) return x144(std::monostate{});
return std::monostate{};
}
std::monostate x910(std::monostate x911) {
infoWhen("CALL", "Returning from the function at 3, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 6);
Num x912 = Stack.pop();
Num x913 = Stack.pop();
SymVal x914 = SymStack.pop();
SymVal x915 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x913);
Frames.set(1, x912);
SymFrames.set(0, x915);
SymFrames.set(1, x914);
updateCurrentMCont(prependCont(x908, CURRENT_MCONT));
}
__attribute__((musttail)) return x753(std::monostate{});
return std::monostate{};
}
std::monostate x908(std::monostate x909) {
infoWhen("CALL", "Returning from the function at 6, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
return x906(std::monostate{});
}
std::monostate x906(std::monostate x907) {
infoWhen("CALL", "Exiting the function at 2, stackSize =", Stack.size());
Frames.popFrameCallee(1);
SymFrames.popFrameCallee(1);
return enterCC(std::monostate());
}
std::monostate x753(std::monostate x886) {
infoWhen("CALL", "Entered the function at 6, stackSize =", Stack.size());
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
Num x887 = Stack.pop();
SymVal x888 = SymStack.pop();
Num x889 = Stack.pop();
SymVal x890 = SymStack.pop();
Num x891 = x889.i32_sub(x887);
Stack.push(x891);
bool x892 = allConcrete(x890, x888);
SymVal x893 = x892 ? Concrete(x891, 32) : x890.minus(x888);
SymStack.push(x893);
}
{
Num x894 = Stack.pop();
SymVal x895 = SymStack.pop();
Frames.set(2, x894);
SymFrames.set(2, x895);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x896 = Stack.pop();
SymVal x897 = SymStack.pop();
Globals.set(0, x896);
SymGlobals.set(0, x897);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x898 = Stack.pop();
SymVal x899 = SymStack.pop();
Num x900 = Stack.pop();
SymStack.pop();
int x901 = x900.toInt();
Memory.storeInt(x901, 28, x898.toInt());
SymMemory.storeSym(x901, 28, x899);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x902 = Stack.pop();
SymVal x903 = SymStack.pop();
Num x904 = Stack.pop();
SymStack.pop();
int x905 = x904.toInt();
Memory.storeInt(x905, 24, x902.toInt());
SymMemory.storeSym(x905, 24, x903);
}
__attribute__((musttail)) return x859(std::monostate{});
return std::monostate{};
}
std::monostate x859(std::monostate x860) {
info("Entering the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x861 = Stack.pop();
SymStack.pop();
Num x862 = I32V(Memory.loadInt(x861.toInt(), 28));
SymVal x863 = SymMemory.loadSym(x861.toInt(), 28);
Stack.push(x862);
SymStack.push(x863);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x864 = Stack.pop();
SymStack.pop();
Num x865 = I32V(Memory.loadInt(x864.toInt(), 24));
SymVal x866 = SymMemory.loadSym(x864.toInt(), 24);
Stack.push(x865);
SymStack.push(x866);
}
{
Num x867 = Stack.pop();
SymVal x868 = SymStack.pop();
Num x869 = Stack.pop();
SymVal x870 = SymStack.pop();
Num x871 = x869.i32_lt_s(x867);
Stack.push(x871);
bool x872 = allConcrete(x870, x868);
SymVal x873 = x872 ? Concrete(x871, 32) : x870.lt(x868).bool2bv();
SymStack.push(x873);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x874 = Stack.pop();
SymVal x875 = SymStack.pop();
Num x876 = Stack.pop();
SymVal x877 = SymStack.pop();
Num x878 = x876.i32_and(x874);
Stack.push(x878);
bool x879 = allConcrete(x877, x875);
SymVal x880 = x879 ? Concrete(x878, 32) : x877.bitwise_and(x875);
SymStack.push(x880);
}
{
Num x881 = Stack.pop();
SymVal x882 = SymStack.pop();
Stack.push(I32V((0 == x881.toInt())));
SymStack.push(x882.is_zero().bool2bv());
}
Num x883 = Stack.pop();
info("The br_if(0)'s condition is ", x883.toInt());
{
SymVal x884 = SymStack.pop();
ExploreTree.fillIfElseNode(x884, 1);
}
int x885 = x883.toInt();
if (x885 != 0) {
info("Jump to 0");
{
ExploreTree.moveCursor(true, makeControl(x803, CURRENT_MCONT));
}
__attribute__((musttail)) return x857(std::monostate{});
} else {
info("Continue rest of the block");
{
ExploreTree.moveCursor(false, makeControl(x857, CURRENT_MCONT));
}
__attribute__((musttail)) return x803(std::monostate{});
}
return std::monostate{};
}
std::monostate x857(std::monostate x858) {
__attribute__((musttail)) return x721(std::monostate{});
return std::monostate{};
}
std::monostate x803(std::monostate x804) {
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x805 = Stack.pop();
SymStack.pop();
Num x806 = I32V(Memory.loadInt(x805.toInt(), 24));
SymVal x807 = SymMemory.loadSym(x805.toInt(), 24);
Stack.push(x806);
SymStack.push(x807);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x808 = Stack.pop();
SymVal x809 = SymStack.pop();
Num x810 = Stack.pop();
SymVal x811 = SymStack.pop();
Num x812 = x810.i32_shl(x808);
Stack.push(x812);
bool x813 = allConcrete(x811, x809);
SymVal x814 = x813 ? Concrete(x812, 32) : x811.shl(x809);
SymStack.push(x814);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
Stack.push(Globals.get(1));
SymStack.push(SymGlobals.get(1));
{
Num x815 = Stack.pop();
SymVal x816 = SymStack.pop();
Num x817 = Stack.pop();
SymVal x818 = SymStack.pop();
Num x819 = x817.i32_add(x815);
Stack.push(x819);
bool x820 = allConcrete(x818, x816);
SymVal x821 = x820 ? Concrete(x819, 32) : x818.add(x816);
SymStack.push(x821);
}
{
Num x822 = Stack.pop();
SymVal x823 = SymStack.pop();
Num x824 = Stack.pop();
SymVal x825 = SymStack.pop();
Num x826 = x824.i32_add(x822);
Stack.push(x826);
bool x827 = allConcrete(x825, x823);
SymVal x828 = x827 ? Concrete(x826, 32) : x825.add(x823);
SymStack.push(x828);
}
{
Num x829 = Stack.pop();
SymStack.pop();
Num x830 = I32V(Memory.loadInt(x829.toInt(), 0));
SymVal x831 = SymMemory.loadSym(x829.toInt(), 0);
Stack.push(x830);
SymStack.push(x831);
}
{
Num x832 = Stack.pop();
SymVal x833 = SymStack.pop();
Num x834 = Stack.pop();
SymStack.pop();
int x835 = x834.toInt();
Memory.storeInt(x835, 20, x832.toInt());
SymMemory.storeSym(x835, 20, x833);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x836 = Stack.pop();
SymStack.pop();
Num x837 = I32V(Memory.loadInt(x836.toInt(), 28));
SymVal x838 = SymMemory.loadSym(x836.toInt(), 28);
Stack.push(x837);
SymStack.push(x838);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x839 = Stack.pop();
SymVal x840 = SymStack.pop();
Num x841 = Stack.pop();
SymVal x842 = SymStack.pop();
Num x843 = x841.i32_sub(x839);
Stack.push(x843);
bool x844 = allConcrete(x842, x840);
SymVal x845 = x844 ? Concrete(x843, 32) : x842.minus(x840);
SymStack.push(x845);
}
{
Num x846 = Stack.pop();
SymVal x847 = SymStack.pop();
Num x848 = Stack.pop();
SymStack.pop();
int x849 = x848.toInt();
Memory.storeInt(x849, 16, x846.toInt());
SymMemory.storeSym(x849, 16, x847);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x850 = Stack.pop();
SymStack.pop();
Num x851 = I32V(Memory.loadInt(x850.toInt(), 28));
SymVal x852 = SymMemory.loadSym(x850.toInt(), 28);
Stack.push(x851);
SymStack.push(x852);
}
{
Num x853 = Stack.pop();
SymVal x854 = SymStack.pop();
Num x855 = Stack.pop();
SymStack.pop();
int x856 = x855.toInt();
Memory.storeInt(x856, 12, x853.toInt());
SymMemory.storeSym(x856, 12, x854);
}
__attribute__((musttail)) return x801(std::monostate{});
return std::monostate{};
}
std::monostate x801(std::monostate x802) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x541(std::monostate{});
return std::monostate{};
}
std::monostate x541(std::monostate x775) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x776 = Stack.pop();
SymStack.pop();
Num x777 = I32V(Memory.loadInt(x776.toInt(), 12));
SymVal x778 = SymMemory.loadSym(x776.toInt(), 12);
Stack.push(x777);
SymStack.push(x778);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x779 = Stack.pop();
SymStack.pop();
Num x780 = I32V(Memory.loadInt(x779.toInt(), 24));
SymVal x781 = SymMemory.loadSym(x779.toInt(), 24);
Stack.push(x780);
SymStack.push(x781);
}
{
Num x782 = Stack.pop();
SymVal x783 = SymStack.pop();
Num x784 = Stack.pop();
SymVal x785 = SymStack.pop();
Num x786 = x784.i32_le_s(x782);
Stack.push(x786);
bool x787 = allConcrete(x785, x783);
SymVal x788 = x787 ? Concrete(x786, 32) : x785.le(x783).bool2bv();
SymStack.push(x788);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x789 = Stack.pop();
SymVal x790 = SymStack.pop();
Num x791 = Stack.pop();
SymVal x792 = SymStack.pop();
Num x793 = x791.i32_and(x789);
Stack.push(x793);
bool x794 = allConcrete(x792, x790);
SymVal x795 = x794 ? Concrete(x793, 32) : x792.bitwise_and(x790);
SymStack.push(x795);
}
{
Num x796 = Stack.pop();
SymVal x797 = SymStack.pop();
Stack.push(I32V((0 == x796.toInt())));
SymStack.push(x797.is_zero().bool2bv());
}
Num x798 = Stack.pop();
info("The br_if(1)'s condition is ", x798.toInt());
{
SymVal x799 = SymStack.pop();
ExploreTree.fillIfElseNode(x799, 0);
}
int x800 = x798.toInt();
if (x800 != 0) {
info("Jump to 1");
{
ExploreTree.moveCursor(true, makeControl(x717, CURRENT_MCONT));
}
__attribute__((musttail)) return x773(std::monostate{});
} else {
info("Continue rest of the block");
{
ExploreTree.moveCursor(false, makeControl(x773, CURRENT_MCONT));
}
__attribute__((musttail)) return x717(std::monostate{});
}
return std::monostate{};
}
std::monostate x773(std::monostate x774) {
__attribute__((musttail)) return x754(std::monostate{});
return std::monostate{};
}
std::monostate x754(std::monostate x755) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x756 = Stack.pop();
SymStack.pop();
Num x757 = I32V(Memory.loadInt(x756.toInt(), 28));
SymVal x758 = SymMemory.loadSym(x756.toInt(), 28);
Stack.push(x757);
SymStack.push(x758);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x759 = Stack.pop();
SymStack.pop();
Num x760 = I32V(Memory.loadInt(x759.toInt(), 16));
SymVal x761 = SymMemory.loadSym(x759.toInt(), 16);
Stack.push(x760);
SymStack.push(x761);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x762 = Stack.pop();
SymVal x763 = SymStack.pop();
Num x764 = Stack.pop();
SymVal x765 = SymStack.pop();
Num x766 = x764.i32_sub(x762);
Stack.push(x766);
bool x767 = allConcrete(x765, x763);
SymVal x768 = x767 ? Concrete(x766, 32) : x765.minus(x763);
SymStack.push(x768);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 6);
Num x769 = Stack.pop();
Num x770 = Stack.pop();
SymVal x771 = SymStack.pop();
SymVal x772 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x770);
Frames.set(1, x769);
SymFrames.set(0, x772);
SymFrames.set(1, x771);
updateCurrentMCont(prependCont(x734, CURRENT_MCONT));
}
__attribute__((musttail)) return x753(std::monostate{});
return std::monostate{};
}
std::monostate x734(std::monostate x735) {
infoWhen("CALL", "Returning from the function at 6, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x736 = Stack.pop();
SymStack.pop();
Num x737 = I32V(Memory.loadInt(x736.toInt(), 16));
SymVal x738 = SymMemory.loadSym(x736.toInt(), 16);
Stack.push(x737);
SymStack.push(x738);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x739 = Stack.pop();
SymVal x740 = SymStack.pop();
Num x741 = Stack.pop();
SymVal x742 = SymStack.pop();
Num x743 = x741.i32_add(x739);
Stack.push(x743);
bool x744 = allConcrete(x742, x740);
SymVal x745 = x744 ? Concrete(x743, 32) : x742.add(x740);
SymStack.push(x745);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x746 = Stack.pop();
SymStack.pop();
Num x747 = I32V(Memory.loadInt(x746.toInt(), 24));
SymVal x748 = SymMemory.loadSym(x746.toInt(), 24);
Stack.push(x747);
SymStack.push(x748);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 6);
Num x749 = Stack.pop();
Num x750 = Stack.pop();
SymVal x751 = SymStack.pop();
SymVal x752 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x750);
Frames.set(1, x749);
SymFrames.set(0, x752);
SymFrames.set(1, x751);
updateCurrentMCont(prependCont(x732, CURRENT_MCONT));
}
__attribute__((musttail)) return x753(std::monostate{});
return std::monostate{};
}
std::monostate x732(std::monostate x733) {
infoWhen("CALL", "Returning from the function at 6, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
__attribute__((musttail)) return x721(std::monostate{});
return std::monostate{};
}
std::monostate x721(std::monostate x722) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(32));
SymStack.push(Concrete(I32V(32), 32));
{
Num x723 = Stack.pop();
SymVal x724 = SymStack.pop();
Num x725 = Stack.pop();
SymVal x726 = SymStack.pop();
Num x727 = x725.i32_add(x723);
Stack.push(x727);
bool x728 = allConcrete(x726, x724);
SymVal x729 = x728 ? Concrete(x727, 32) : x726.add(x724);
SymStack.push(x729);
}
{
Num x730 = Stack.pop();
SymVal x731 = SymStack.pop();
Globals.set(0, x730);
SymGlobals.set(0, x731);
}
return x719(std::monostate{});
}
std::monostate x719(std::monostate x720) {
infoWhen("CALL", "Exiting the function at 6, stackSize =", Stack.size());
Frames.popFrameCallee(6);
SymFrames.popFrameCallee(6);
return enterCC(std::monostate());
}
std::monostate x717(std::monostate x718) {
__attribute__((musttail)) return x666(std::monostate{});
return std::monostate{};
}
std::monostate x666(std::monostate x667) {
info("Entering the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x668 = Stack.pop();
SymStack.pop();
Num x669 = I32V(Memory.loadInt(x668.toInt(), 12));
SymVal x670 = SymMemory.loadSym(x668.toInt(), 12);
Stack.push(x669);
SymStack.push(x670);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x671 = Stack.pop();
SymVal x672 = SymStack.pop();
Num x673 = Stack.pop();
SymVal x674 = SymStack.pop();
Num x675 = x673.i32_shl(x671);
Stack.push(x675);
bool x676 = allConcrete(x674, x672);
SymVal x677 = x676 ? Concrete(x675, 32) : x674.shl(x672);
SymStack.push(x677);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
Stack.push(Globals.get(1));
SymStack.push(SymGlobals.get(1));
{
Num x678 = Stack.pop();
SymVal x679 = SymStack.pop();
Num x680 = Stack.pop();
SymVal x681 = SymStack.pop();
Num x682 = x680.i32_add(x678);
Stack.push(x682);
bool x683 = allConcrete(x681, x679);
SymVal x684 = x683 ? Concrete(x682, 32) : x681.add(x679);
SymStack.push(x684);
}
{
Num x685 = Stack.pop();
SymVal x686 = SymStack.pop();
Num x687 = Stack.pop();
SymVal x688 = SymStack.pop();
Num x689 = x687.i32_add(x685);
Stack.push(x689);
bool x690 = allConcrete(x688, x686);
SymVal x691 = x690 ? Concrete(x689, 32) : x688.add(x686);
SymStack.push(x691);
}
{
Num x692 = Stack.pop();
SymStack.pop();
Num x693 = I32V(Memory.loadInt(x692.toInt(), 0));
SymVal x694 = SymMemory.loadSym(x692.toInt(), 0);
Stack.push(x693);
SymStack.push(x694);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x695 = Stack.pop();
SymStack.pop();
Num x696 = I32V(Memory.loadInt(x695.toInt(), 20));
SymVal x697 = SymMemory.loadSym(x695.toInt(), 20);
Stack.push(x696);
SymStack.push(x697);
}
{
Num x698 = Stack.pop();
SymVal x699 = SymStack.pop();
Num x700 = Stack.pop();
SymVal x701 = SymStack.pop();
Num x702 = x700.i32_le_s(x698);
Stack.push(x702);
bool x703 = allConcrete(x701, x699);
SymVal x704 = x703 ? Concrete(x702, 32) : x701.le(x699).bool2bv();
SymStack.push(x704);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x705 = Stack.pop();
SymVal x706 = SymStack.pop();
Num x707 = Stack.pop();
SymVal x708 = SymStack.pop();
Num x709 = x707.i32_and(x705);
Stack.push(x709);
bool x710 = allConcrete(x708, x706);
SymVal x711 = x710 ? Concrete(x709, 32) : x708.bitwise_and(x706);
SymStack.push(x711);
}
{
Num x712 = Stack.pop();
SymVal x713 = SymStack.pop();
Stack.push(I32V((0 == x712.toInt())));
SymStack.push(x713.is_zero().bool2bv());
}
Num x714 = Stack.pop();
info("The br_if(0)'s condition is ", x714.toInt());
{
SymVal x715 = SymStack.pop();
ExploreTree.fillIfElseNode(x715, 1);
}
int x716 = x714.toInt();
if (x716 != 0) {
info("Jump to 0");
{
ExploreTree.moveCursor(true, makeControl(x542, CURRENT_MCONT));
}
__attribute__((musttail)) return x664(std::monostate{});
} else {
info("Continue rest of the block");
{
ExploreTree.moveCursor(false, makeControl(x664, CURRENT_MCONT));
}
__attribute__((musttail)) return x542(std::monostate{});
}
return std::monostate{};
}
std::monostate x664(std::monostate x665) {
__attribute__((musttail)) return x525(std::monostate{});
return std::monostate{};
}
std::monostate x542(std::monostate x543) {
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x544 = Stack.pop();
SymStack.pop();
Num x545 = I32V(Memory.loadInt(x544.toInt(), 16));
SymVal x546 = SymMemory.loadSym(x544.toInt(), 16);
Stack.push(x545);
SymStack.push(x546);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x547 = Stack.pop();
SymVal x548 = SymStack.pop();
Num x549 = Stack.pop();
SymVal x550 = SymStack.pop();
Num x551 = x549.i32_add(x547);
Stack.push(x551);
bool x552 = allConcrete(x550, x548);
SymVal x553 = x552 ? Concrete(x551, 32) : x550.add(x548);
SymStack.push(x553);
}
{
Num x554 = Stack.pop();
SymVal x555 = SymStack.pop();
Num x556 = Stack.pop();
SymStack.pop();
int x557 = x556.toInt();
Memory.storeInt(x557, 16, x554.toInt());
SymMemory.storeSym(x557, 16, x555);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x558 = Stack.pop();
SymStack.pop();
Num x559 = I32V(Memory.loadInt(x558.toInt(), 12));
SymVal x560 = SymMemory.loadSym(x558.toInt(), 12);
Stack.push(x559);
SymStack.push(x560);
}
{
Num x561 = Stack.pop();
SymVal x562 = SymStack.pop();
Frames.set(3, x561);
SymFrames.set(3, x562);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
Stack.push(Globals.get(1));
SymStack.push(SymGlobals.get(1));
{
Num x563 = Stack.pop();
SymVal x564 = SymStack.pop();
Num x565 = Stack.pop();
SymVal x566 = SymStack.pop();
Num x567 = x565.i32_add(x563);
Stack.push(x567);
bool x568 = allConcrete(x566, x564);
SymVal x569 = x568 ? Concrete(x567, 32) : x566.add(x564);
SymStack.push(x569);
}
{
Num x570 = Stack.pop();
SymVal x571 = SymStack.pop();
Frames.set(4, x570);
SymFrames.set(4, x571);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x572 = Stack.pop();
SymVal x573 = SymStack.pop();
Frames.set(5, x572);
SymFrames.set(5, x573);
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
Num x574 = Stack.pop();
SymVal x575 = SymStack.pop();
Num x576 = Stack.pop();
SymVal x577 = SymStack.pop();
Num x578 = x576.i32_shl(x574);
Stack.push(x578);
bool x579 = allConcrete(x577, x575);
SymVal x580 = x579 ? Concrete(x578, 32) : x577.shl(x575);
SymStack.push(x580);
}
{
Num x581 = Stack.pop();
SymVal x582 = SymStack.pop();
Num x583 = Stack.pop();
SymVal x584 = SymStack.pop();
Num x585 = x583.i32_add(x581);
Stack.push(x585);
bool x586 = allConcrete(x584, x582);
SymVal x587 = x586 ? Concrete(x585, 32) : x584.add(x582);
SymStack.push(x587);
}
{
Num x588 = Stack.pop();
SymStack.pop();
Num x589 = I32V(Memory.loadInt(x588.toInt(), 0));
SymVal x590 = SymMemory.loadSym(x588.toInt(), 0);
Stack.push(x589);
SymStack.push(x590);
}
{
Num x591 = Stack.pop();
SymVal x592 = SymStack.pop();
Num x593 = Stack.pop();
SymStack.pop();
int x594 = x593.toInt();
Memory.storeInt(x594, 8, x591.toInt());
SymMemory.storeSym(x594, 8, x592);
}
Stack.push(Frames.get(4));
SymStack.push(SymFrames.get(4));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x595 = Stack.pop();
SymStack.pop();
Num x596 = I32V(Memory.loadInt(x595.toInt(), 16));
SymVal x597 = SymMemory.loadSym(x595.toInt(), 16);
Stack.push(x596);
SymStack.push(x597);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x598 = Stack.pop();
SymVal x599 = SymStack.pop();
Num x600 = Stack.pop();
SymVal x601 = SymStack.pop();
Num x602 = x600.i32_shl(x598);
Stack.push(x602);
bool x603 = allConcrete(x601, x599);
SymVal x604 = x603 ? Concrete(x602, 32) : x601.shl(x599);
SymStack.push(x604);
}
{
Num x605 = Stack.pop();
SymVal x606 = SymStack.pop();
Num x607 = Stack.pop();
SymVal x608 = SymStack.pop();
Num x609 = x607.i32_add(x605);
Stack.push(x609);
bool x610 = allConcrete(x608, x606);
SymVal x611 = x610 ? Concrete(x609, 32) : x608.add(x606);
SymStack.push(x611);
}
{
Num x612 = Stack.pop();
SymStack.pop();
Num x613 = I32V(Memory.loadInt(x612.toInt(), 0));
SymVal x614 = SymMemory.loadSym(x612.toInt(), 0);
Stack.push(x613);
SymStack.push(x614);
}
{
Num x615 = Stack.pop();
SymVal x616 = SymStack.pop();
Frames.set(6, x615);
SymFrames.set(6, x616);
}
Stack.push(Frames.get(4));
SymStack.push(SymFrames.get(4));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x617 = Stack.pop();
SymStack.pop();
Num x618 = I32V(Memory.loadInt(x617.toInt(), 12));
SymVal x619 = SymMemory.loadSym(x617.toInt(), 12);
Stack.push(x618);
SymStack.push(x619);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x620 = Stack.pop();
SymVal x621 = SymStack.pop();
Num x622 = Stack.pop();
SymVal x623 = SymStack.pop();
Num x624 = x622.i32_shl(x620);
Stack.push(x624);
bool x625 = allConcrete(x623, x621);
SymVal x626 = x625 ? Concrete(x624, 32) : x623.shl(x621);
SymStack.push(x626);
}
{
Num x627 = Stack.pop();
SymVal x628 = SymStack.pop();
Num x629 = Stack.pop();
SymVal x630 = SymStack.pop();
Num x631 = x629.i32_add(x627);
Stack.push(x631);
bool x632 = allConcrete(x630, x628);
SymVal x633 = x632 ? Concrete(x631, 32) : x630.add(x628);
SymStack.push(x633);
}
Stack.push(Frames.get(6));
SymStack.push(SymFrames.get(6));
{
Num x634 = Stack.pop();
SymVal x635 = SymStack.pop();
Num x636 = Stack.pop();
SymStack.pop();
int x637 = x636.toInt();
Memory.storeInt(x637, 0, x634.toInt());
SymMemory.storeSym(x637, 0, x635);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x638 = Stack.pop();
SymStack.pop();
Num x639 = I32V(Memory.loadInt(x638.toInt(), 8));
SymVal x640 = SymMemory.loadSym(x638.toInt(), 8);
Stack.push(x639);
SymStack.push(x640);
}
{
Num x641 = Stack.pop();
SymVal x642 = SymStack.pop();
Frames.set(7, x641);
SymFrames.set(7, x642);
}
Stack.push(Frames.get(4));
SymStack.push(SymFrames.get(4));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x643 = Stack.pop();
SymStack.pop();
Num x644 = I32V(Memory.loadInt(x643.toInt(), 16));
SymVal x645 = SymMemory.loadSym(x643.toInt(), 16);
Stack.push(x644);
SymStack.push(x645);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x646 = Stack.pop();
SymVal x647 = SymStack.pop();
Num x648 = Stack.pop();
SymVal x649 = SymStack.pop();
Num x650 = x648.i32_shl(x646);
Stack.push(x650);
bool x651 = allConcrete(x649, x647);
SymVal x652 = x651 ? Concrete(x650, 32) : x649.shl(x647);
SymStack.push(x652);
}
{
Num x653 = Stack.pop();
SymVal x654 = SymStack.pop();
Num x655 = Stack.pop();
SymVal x656 = SymStack.pop();
Num x657 = x655.i32_add(x653);
Stack.push(x657);
bool x658 = allConcrete(x656, x654);
SymVal x659 = x658 ? Concrete(x657, 32) : x656.add(x654);
SymStack.push(x659);
}
Stack.push(Frames.get(7));
SymStack.push(SymFrames.get(7));
{
Num x660 = Stack.pop();
SymVal x661 = SymStack.pop();
Num x662 = Stack.pop();
SymStack.pop();
int x663 = x662.toInt();
Memory.storeInt(x663, 0, x660.toInt());
SymMemory.storeSym(x663, 0, x661);
}
__attribute__((musttail)) return x525(std::monostate{});
return std::monostate{};
}
std::monostate x525(std::monostate x526) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x527 = Stack.pop();
SymStack.pop();
Num x528 = I32V(Memory.loadInt(x527.toInt(), 12));
SymVal x529 = SymMemory.loadSym(x527.toInt(), 12);
Stack.push(x528);
SymStack.push(x529);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x530 = Stack.pop();
SymVal x531 = SymStack.pop();
Num x532 = Stack.pop();
SymVal x533 = SymStack.pop();
Num x534 = x532.i32_add(x530);
Stack.push(x534);
bool x535 = allConcrete(x533, x531);
SymVal x536 = x535 ? Concrete(x534, 32) : x533.add(x531);
SymStack.push(x536);
}
{
Num x537 = Stack.pop();
SymVal x538 = SymStack.pop();
Num x539 = Stack.pop();
SymStack.pop();
int x540 = x539.toInt();
Memory.storeInt(x540, 12, x537.toInt());
SymMemory.storeSym(x540, 12, x538);
}
info("Jump to 0");
__attribute__((musttail)) return x541(std::monostate{});
return std::monostate{};
}
std::monostate x499(std::monostate x500) {
infoWhen("CALL", "Entered the function at 5, stackSize =", Stack.size());
Frames.pushFrameCallee(2);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Stack.push(Globals.get(0));
SymStack.push(SymGlobals.get(0));
Stack.push(I32V(16));
SymStack.push(Concrete(I32V(16), 32));
{
Num x501 = Stack.pop();
SymVal x502 = SymStack.pop();
Num x503 = Stack.pop();
SymVal x504 = SymStack.pop();
Num x505 = x503.i32_sub(x501);
Stack.push(x505);
bool x506 = allConcrete(x504, x502);
SymVal x507 = x506 ? Concrete(x505, 32) : x504.minus(x502);
SymStack.push(x507);
}
{
Num x508 = Stack.pop();
SymVal x509 = SymStack.pop();
Frames.set(2, x508);
SymFrames.set(2, x509);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x510 = Stack.pop();
SymVal x511 = SymStack.pop();
Num x512 = Stack.pop();
SymStack.pop();
int x513 = x512.toInt();
Memory.storeInt(x513, 12, x510.toInt());
SymMemory.storeSym(x513, 12, x511);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x514 = Stack.pop();
SymVal x515 = SymStack.pop();
Num x516 = Stack.pop();
SymStack.pop();
int x517 = x516.toInt();
Memory.storeInt(x517, 8, x514.toInt());
SymMemory.storeSym(x517, 8, x515);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x518 = Stack.pop();
SymStack.pop();
Num x519 = I32V(Memory.loadInt(x518.toInt(), 12));
SymVal x520 = SymMemory.loadSym(x518.toInt(), 12);
Stack.push(x519);
SymStack.push(x520);
}
{
Num x521 = Stack.pop();
SymVal x522 = SymStack.pop();
Num x523 = Stack.pop();
SymStack.pop();
int x524 = x523.toInt();
Memory.storeInt(x524, 4, x521.toInt());
SymMemory.storeSym(x524, 4, x522);
}
__attribute__((musttail)) return x497(std::monostate{});
return std::monostate{};
}
std::monostate x497(std::monostate x498) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x464(std::monostate{});
return std::monostate{};
}
std::monostate x464(std::monostate x471) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x472 = Stack.pop();
SymStack.pop();
Num x473 = I32V(Memory.loadInt(x472.toInt(), 4));
SymVal x474 = SymMemory.loadSym(x472.toInt(), 4);
Stack.push(x473);
SymStack.push(x474);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x475 = Stack.pop();
SymStack.pop();
Num x476 = I32V(Memory.loadInt(x475.toInt(), 8));
SymVal x477 = SymMemory.loadSym(x475.toInt(), 8);
Stack.push(x476);
SymStack.push(x477);
}
{
Num x478 = Stack.pop();
SymVal x479 = SymStack.pop();
Num x480 = Stack.pop();
SymVal x481 = SymStack.pop();
Num x482 = x480.i32_lt_s(x478);
Stack.push(x482);
bool x483 = allConcrete(x481, x479);
SymVal x484 = x483 ? Concrete(x482, 32) : x481.lt(x479).bool2bv();
SymStack.push(x484);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x485 = Stack.pop();
SymVal x486 = SymStack.pop();
Num x487 = Stack.pop();
SymVal x488 = SymStack.pop();
Num x489 = x487.i32_and(x485);
Stack.push(x489);
bool x490 = allConcrete(x488, x486);
SymVal x491 = x490 ? Concrete(x489, 32) : x488.bitwise_and(x486);
SymStack.push(x491);
}
{
Num x492 = Stack.pop();
SymVal x493 = SymStack.pop();
Stack.push(I32V((0 == x492.toInt())));
SymStack.push(x493.is_zero().bool2bv());
}
Num x494 = Stack.pop();
info("The br_if(1)'s condition is ", x494.toInt());
{
SymVal x495 = SymStack.pop();
ExploreTree.fillIfElseNode(x495, 0);
}
int x496 = x494.toInt();
if (x496 != 0) {
info("Jump to 1");
{
ExploreTree.moveCursor(true, makeControl(x418, CURRENT_MCONT));
}
__attribute__((musttail)) return x469(std::monostate{});
} else {
info("Continue rest of the block");
{
ExploreTree.moveCursor(false, makeControl(x469, CURRENT_MCONT));
}
__attribute__((musttail)) return x418(std::monostate{});
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
infoWhen("CALL", "Exiting the function at 5, stackSize =", Stack.size());
Frames.popFrameCallee(2);
SymFrames.popFrameCallee(2);
return enterCC(std::monostate());
}
std::monostate x418(std::monostate x419) {
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x420 = Stack.pop();
SymStack.pop();
Num x421 = I32V(Memory.loadInt(x420.toInt(), 4));
SymVal x422 = SymMemory.loadSym(x420.toInt(), 4);
Stack.push(x421);
SymStack.push(x422);
}
{
Num x423 = Stack.pop();
SymVal x424 = SymStack.pop();
Frames.set(3, x423);
SymFrames.set(3, x424);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x425 = Stack.pop();
SymVal x426 = SymStack.pop();
Num x427 = Stack.pop();
SymVal x428 = SymStack.pop();
Num x429 = x427.i32_shl(x425);
Stack.push(x429);
bool x430 = allConcrete(x428, x426);
SymVal x431 = x430 ? Concrete(x429, 32) : x428.shl(x426);
SymStack.push(x431);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
Stack.push(Globals.get(1));
SymStack.push(SymGlobals.get(1));
{
Num x432 = Stack.pop();
SymVal x433 = SymStack.pop();
Num x434 = Stack.pop();
SymVal x435 = SymStack.pop();
Num x436 = x434.i32_add(x432);
Stack.push(x436);
bool x437 = allConcrete(x435, x433);
SymVal x438 = x437 ? Concrete(x436, 32) : x435.add(x433);
SymStack.push(x438);
}
{
Num x439 = Stack.pop();
SymVal x440 = SymStack.pop();
Num x441 = Stack.pop();
SymVal x442 = SymStack.pop();
Num x443 = x441.i32_add(x439);
Stack.push(x443);
bool x444 = allConcrete(x442, x440);
SymVal x445 = x444 ? Concrete(x443, 32) : x442.add(x440);
SymStack.push(x445);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x446 = Stack.pop();
SymVal x447 = SymStack.pop();
Num x448 = Stack.pop();
SymStack.pop();
int x449 = x448.toInt();
Memory.storeInt(x449, 0, x446.toInt());
SymMemory.storeSym(x449, 0, x447);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x450 = Stack.pop();
SymStack.pop();
Num x451 = I32V(Memory.loadInt(x450.toInt(), 4));
SymVal x452 = SymMemory.loadSym(x450.toInt(), 4);
Stack.push(x451);
SymStack.push(x452);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x453 = Stack.pop();
SymVal x454 = SymStack.pop();
Num x455 = Stack.pop();
SymVal x456 = SymStack.pop();
Num x457 = x455.i32_add(x453);
Stack.push(x457);
bool x458 = allConcrete(x456, x454);
SymVal x459 = x458 ? Concrete(x457, 32) : x456.add(x454);
SymStack.push(x459);
}
{
Num x460 = Stack.pop();
SymVal x461 = SymStack.pop();
Num x462 = Stack.pop();
SymStack.pop();
int x463 = x462.toInt();
Memory.storeInt(x463, 4, x460.toInt());
SymMemory.storeSym(x463, 4, x461);
}
info("Jump to 0");
__attribute__((musttail)) return x464(std::monostate{});
return std::monostate{};
}
std::monostate x396(std::monostate x397) {
infoWhen("CALL", "Entered the function at 4, stackSize =", Stack.size());
Frames.pushFrameCallee(1);
SymFrames.pushFrameSlot(32);
Stack.push(Globals.get(0));
SymStack.push(SymGlobals.get(0));
Stack.push(I32V(16));
SymStack.push(Concrete(I32V(16), 32));
{
Num x398 = Stack.pop();
SymVal x399 = SymStack.pop();
Num x400 = Stack.pop();
SymVal x401 = SymStack.pop();
Num x402 = x400.i32_sub(x398);
Stack.push(x402);
bool x403 = allConcrete(x401, x399);
SymVal x404 = x403 ? Concrete(x402, 32) : x401.minus(x399);
SymStack.push(x404);
}
{
Num x405 = Stack.pop();
SymVal x406 = SymStack.pop();
Frames.set(1, x405);
SymFrames.set(1, x406);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x407 = Stack.pop();
SymVal x408 = SymStack.pop();
Globals.set(0, x407);
SymGlobals.set(0, x408);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x409 = Stack.pop();
SymVal x410 = SymStack.pop();
Num x411 = Stack.pop();
SymStack.pop();
int x412 = x411.toInt();
Memory.storeInt(x412, 12, x409.toInt());
SymMemory.storeSym(x412, 12, x410);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x413 = Stack.pop();
SymStack.pop();
Num x414 = I32V(Memory.loadInt(x413.toInt(), 12));
SymVal x415 = SymMemory.loadSym(x413.toInt(), 12);
Stack.push(x414);
SymStack.push(x415);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x416 = Stack.pop();
SymVal x417 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x416);
SymFrames.set(0, x417);
updateCurrentMCont(prependCont(x385, CURRENT_MCONT));
}
__attribute__((musttail)) return x364(std::monostate{});
return std::monostate{};
}
std::monostate x385(std::monostate x386) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(16));
SymStack.push(Concrete(I32V(16), 32));
{
Num x387 = Stack.pop();
SymVal x388 = SymStack.pop();
Num x389 = Stack.pop();
SymVal x390 = SymStack.pop();
Num x391 = x389.i32_add(x387);
Stack.push(x391);
bool x392 = allConcrete(x390, x388);
SymVal x393 = x392 ? Concrete(x391, 32) : x390.add(x388);
SymStack.push(x393);
}
{
Num x394 = Stack.pop();
SymVal x395 = SymStack.pop();
Globals.set(0, x394);
SymGlobals.set(0, x395);
}
return x383(std::monostate{});
}
std::monostate x383(std::monostate x384) {
infoWhen("CALL", "Exiting the function at 4, stackSize =", Stack.size());
Frames.popFrameCallee(1);
SymFrames.popFrameCallee(1);
return enterCC(std::monostate());
}
std::monostate x364(std::monostate x365) {
infoWhen("CALL", "Entered the function at 8, stackSize =", Stack.size());
Frames.pushFrameCallee(3);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Stack.push(Globals.get(0));
SymStack.push(SymGlobals.get(0));
Stack.push(I32V(16));
SymStack.push(Concrete(I32V(16), 32));
{
Num x366 = Stack.pop();
SymVal x367 = SymStack.pop();
Num x368 = Stack.pop();
SymVal x369 = SymStack.pop();
Num x370 = x368.i32_sub(x366);
Stack.push(x370);
bool x371 = allConcrete(x369, x367);
SymVal x372 = x371 ? Concrete(x370, 32) : x369.minus(x367);
SymStack.push(x372);
}
{
Num x373 = Stack.pop();
SymVal x374 = SymStack.pop();
Frames.set(1, x373);
SymFrames.set(1, x374);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x375 = Stack.pop();
SymVal x376 = SymStack.pop();
Num x377 = Stack.pop();
SymStack.pop();
int x378 = x377.toInt();
Memory.storeInt(x378, 12, x375.toInt());
SymMemory.storeSym(x378, 12, x376);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x379 = Stack.pop();
SymVal x380 = SymStack.pop();
Num x381 = Stack.pop();
SymStack.pop();
int x382 = x381.toInt();
Memory.storeInt(x382, 8, x379.toInt());
SymMemory.storeSym(x382, 8, x380);
}
__attribute__((musttail)) return x362(std::monostate{});
return std::monostate{};
}
std::monostate x362(std::monostate x363) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x296(std::monostate{});
return std::monostate{};
}
std::monostate x296(std::monostate x339) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x340 = Stack.pop();
SymStack.pop();
Num x341 = I32V(Memory.loadInt(x340.toInt(), 8));
SymVal x342 = SymMemory.loadSym(x340.toInt(), 8);
Stack.push(x341);
SymStack.push(x342);
}
Stack.push(I32V(400));
SymStack.push(Concrete(I32V(400), 32));
{
Num x343 = Stack.pop();
SymVal x344 = SymStack.pop();
Num x345 = Stack.pop();
SymVal x346 = SymStack.pop();
Num x347 = x345.i32_lt_s(x343);
Stack.push(x347);
bool x348 = allConcrete(x346, x344);
SymVal x349 = x348 ? Concrete(x347, 32) : x346.lt(x344).bool2bv();
SymStack.push(x349);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x350 = Stack.pop();
SymVal x351 = SymStack.pop();
Num x352 = Stack.pop();
SymVal x353 = SymStack.pop();
Num x354 = x352.i32_and(x350);
Stack.push(x354);
bool x355 = allConcrete(x353, x351);
SymVal x356 = x355 ? Concrete(x354, 32) : x353.bitwise_and(x351);
SymStack.push(x356);
}
{
Num x357 = Stack.pop();
SymVal x358 = SymStack.pop();
Stack.push(I32V((0 == x357.toInt())));
SymStack.push(x358.is_zero().bool2bv());
}
Num x359 = Stack.pop();
info("The br_if(1)'s condition is ", x359.toInt());
{
SymVal x360 = SymStack.pop();
ExploreTree.fillIfElseNode(x360, 0);
}
int x361 = x359.toInt();
if (x361 != 0) {
info("Jump to 1");
{
ExploreTree.moveCursor(true, makeControl(x327, CURRENT_MCONT));
}
__attribute__((musttail)) return x337(std::monostate{});
} else {
info("Continue rest of the block");
{
ExploreTree.moveCursor(false, makeControl(x337, CURRENT_MCONT));
}
__attribute__((musttail)) return x327(std::monostate{});
}
return std::monostate{};
}
std::monostate x337(std::monostate x338) {
__attribute__((musttail)) return x335(std::monostate{});
return std::monostate{};
}
std::monostate x335(std::monostate x336) {
info("Exiting the block, stackSize =", Stack.size());
return x333(std::monostate{});
}
std::monostate x333(std::monostate x334) {
infoWhen("CALL", "Exiting the function at 8, stackSize =", Stack.size());
Frames.popFrameCallee(3);
SymFrames.popFrameCallee(3);
return enterCC(std::monostate());
}
std::monostate x327(std::monostate x328) {
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x329 = Stack.pop();
SymVal x330 = SymStack.pop();
Num x331 = Stack.pop();
SymStack.pop();
int x332 = x331.toInt();
Memory.storeInt(x332, 4, x329.toInt());
SymMemory.storeSym(x332, 4, x330);
}
__attribute__((musttail)) return x325(std::monostate{});
return std::monostate{};
}
std::monostate x325(std::monostate x326) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x279(std::monostate{});
return std::monostate{};
}
std::monostate x279(std::monostate x299) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x300 = Stack.pop();
SymStack.pop();
Num x301 = I32V(Memory.loadInt(x300.toInt(), 4));
SymVal x302 = SymMemory.loadSym(x300.toInt(), 4);
Stack.push(x301);
SymStack.push(x302);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x303 = Stack.pop();
SymStack.pop();
Num x304 = I32V(Memory.loadInt(x303.toInt(), 12));
SymVal x305 = SymMemory.loadSym(x303.toInt(), 12);
Stack.push(x304);
SymStack.push(x305);
}
{
Num x306 = Stack.pop();
SymVal x307 = SymStack.pop();
Num x308 = Stack.pop();
SymVal x309 = SymStack.pop();
Num x310 = x308.i32_lt_s(x306);
Stack.push(x310);
bool x311 = allConcrete(x309, x307);
SymVal x312 = x311 ? Concrete(x310, 32) : x309.lt(x307).bool2bv();
SymStack.push(x312);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x313 = Stack.pop();
SymVal x314 = SymStack.pop();
Num x315 = Stack.pop();
SymVal x316 = SymStack.pop();
Num x317 = x315.i32_and(x313);
Stack.push(x317);
bool x318 = allConcrete(x316, x314);
SymVal x319 = x318 ? Concrete(x317, 32) : x316.bitwise_and(x314);
SymStack.push(x319);
}
{
Num x320 = Stack.pop();
SymVal x321 = SymStack.pop();
Stack.push(I32V((0 == x320.toInt())));
SymStack.push(x321.is_zero().bool2bv());
}
Num x322 = Stack.pop();
info("The br_if(1)'s condition is ", x322.toInt());
{
SymVal x323 = SymStack.pop();
ExploreTree.fillIfElseNode(x323, 0);
}
int x324 = x322.toInt();
if (x324 != 0) {
info("Jump to 1");
{
ExploreTree.moveCursor(true, makeControl(x176, CURRENT_MCONT));
}
__attribute__((musttail)) return x297(std::monostate{});
} else {
info("Continue rest of the block");
{
ExploreTree.moveCursor(false, makeControl(x297, CURRENT_MCONT));
}
__attribute__((musttail)) return x176(std::monostate{});
}
return std::monostate{};
}
std::monostate x297(std::monostate x298) {
__attribute__((musttail)) return x280(std::monostate{});
return std::monostate{};
}
std::monostate x280(std::monostate x281) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x282 = Stack.pop();
SymStack.pop();
Num x283 = I32V(Memory.loadInt(x282.toInt(), 8));
SymVal x284 = SymMemory.loadSym(x282.toInt(), 8);
Stack.push(x283);
SymStack.push(x284);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x285 = Stack.pop();
SymVal x286 = SymStack.pop();
Num x287 = Stack.pop();
SymVal x288 = SymStack.pop();
Num x289 = x287.i32_add(x285);
Stack.push(x289);
bool x290 = allConcrete(x288, x286);
SymVal x291 = x290 ? Concrete(x289, 32) : x288.add(x286);
SymStack.push(x291);
}
{
Num x292 = Stack.pop();
SymVal x293 = SymStack.pop();
Num x294 = Stack.pop();
SymStack.pop();
int x295 = x294.toInt();
Memory.storeInt(x295, 8, x292.toInt());
SymMemory.storeSym(x295, 8, x293);
}
info("Jump to 0");
__attribute__((musttail)) return x296(std::monostate{});
return std::monostate{};
}
std::monostate x176(std::monostate x177) {
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x178 = Stack.pop();
SymStack.pop();
Num x179 = I32V(Memory.loadInt(x178.toInt(), 4));
SymVal x180 = SymMemory.loadSym(x178.toInt(), 4);
Stack.push(x179);
SymStack.push(x180);
}
{
Num x181 = Stack.pop();
SymVal x182 = SymStack.pop();
Frames.set(2, x181);
SymFrames.set(2, x182);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x183 = Stack.pop();
SymVal x184 = SymStack.pop();
Num x185 = Stack.pop();
SymVal x186 = SymStack.pop();
Num x187 = x185.i32_shl(x183);
Stack.push(x187);
bool x188 = allConcrete(x186, x184);
SymVal x189 = x188 ? Concrete(x187, 32) : x186.shl(x184);
SymStack.push(x189);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
Stack.push(Globals.get(1));
SymStack.push(SymGlobals.get(1));
{
Num x190 = Stack.pop();
SymVal x191 = SymStack.pop();
Num x192 = Stack.pop();
SymVal x193 = SymStack.pop();
Num x194 = x192.i32_add(x190);
Stack.push(x194);
bool x195 = allConcrete(x193, x191);
SymVal x196 = x195 ? Concrete(x194, 32) : x193.add(x191);
SymStack.push(x196);
}
{
Num x197 = Stack.pop();
SymVal x198 = SymStack.pop();
Num x199 = Stack.pop();
SymVal x200 = SymStack.pop();
Num x201 = x199.i32_add(x197);
Stack.push(x201);
bool x202 = allConcrete(x200, x198);
SymVal x203 = x202 ? Concrete(x201, 32) : x200.add(x198);
SymStack.push(x203);
}
{
Num x204 = Stack.pop();
SymVal x205 = SymStack.pop();
Frames.set(3, x204);
SymFrames.set(3, x205);
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
Num x206 = Stack.pop();
SymStack.pop();
Num x207 = I32V(Memory.loadInt(x206.toInt(), 0));
SymVal x208 = SymMemory.loadSym(x206.toInt(), 0);
Stack.push(x207);
SymStack.push(x208);
}
{
Num x209 = Stack.pop();
SymVal x210 = SymStack.pop();
Num x211 = Stack.pop();
SymVal x212 = SymStack.pop();
Num x213 = x211.i32_mul(x209);
Stack.push(x213);
bool x214 = allConcrete(x212, x210);
SymVal x215 = x214 ? Concrete(x213, 32) : x212.mul(x210);
SymStack.push(x215);
}
Stack.push(I32V(7));
SymStack.push(Concrete(I32V(7), 32));
{
Num x216 = Stack.pop();
SymVal x217 = SymStack.pop();
Num x218 = Stack.pop();
SymVal x219 = SymStack.pop();
Num x220 = x218.i32_mul(x216);
Stack.push(x220);
bool x221 = allConcrete(x219, x217);
SymVal x222 = x221 ? Concrete(x220, 32) : x219.mul(x217);
SymStack.push(x222);
}
{
Num x223 = Stack.pop();
SymVal x224 = SymStack.pop();
Num x225 = Stack.pop();
SymVal x226 = SymStack.pop();
Num x227 = x225.i32_add(x223);
Stack.push(x227);
bool x228 = allConcrete(x226, x224);
SymVal x229 = x228 ? Concrete(x227, 32) : x226.add(x224);
SymStack.push(x229);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(-4));
SymStack.push(Concrete(I32V(-4), 32));
{
Num x230 = Stack.pop();
SymVal x231 = SymStack.pop();
Num x232 = Stack.pop();
SymVal x233 = SymStack.pop();
Num x234 = x232.i32_add(x230);
Stack.push(x234);
bool x235 = allConcrete(x233, x231);
SymVal x236 = x235 ? Concrete(x234, 32) : x233.add(x231);
SymStack.push(x236);
}
{
Num x237 = Stack.pop();
SymStack.pop();
Num x238 = I32V(Memory.loadInt(x237.toInt(), 0));
SymVal x239 = SymMemory.loadSym(x237.toInt(), 0);
Stack.push(x238);
SymStack.push(x239);
}
{
Num x240 = Stack.pop();
SymVal x241 = SymStack.pop();
Num x242 = Stack.pop();
SymVal x243 = SymStack.pop();
Num x244 = x242.i32_mul(x240);
Stack.push(x244);
bool x245 = allConcrete(x243, x241);
SymVal x246 = x245 ? Concrete(x244, 32) : x243.mul(x241);
SymStack.push(x246);
}
{
Num x247 = Stack.pop();
SymVal x248 = SymStack.pop();
Num x249 = Stack.pop();
SymVal x250 = SymStack.pop();
Num x251 = x249.i32_add(x247);
Stack.push(x251);
bool x252 = allConcrete(x250, x248);
SymVal x253 = x252 ? Concrete(x251, 32) : x250.add(x248);
SymStack.push(x253);
}
Stack.push(I32V(3));
SymStack.push(Concrete(I32V(3), 32));
{
Num x254 = Stack.pop();
SymVal x255 = SymStack.pop();
Num x256 = Stack.pop();
SymVal x257 = SymStack.pop();
Num x258 = x256.i32_add(x254);
Stack.push(x258);
bool x259 = allConcrete(x257, x255);
SymVal x260 = x259 ? Concrete(x258, 32) : x257.add(x255);
SymStack.push(x260);
}
{
Num x261 = Stack.pop();
SymVal x262 = SymStack.pop();
Num x263 = Stack.pop();
SymStack.pop();
int x264 = x263.toInt();
Memory.storeInt(x264, 0, x261.toInt());
SymMemory.storeSym(x264, 0, x262);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x265 = Stack.pop();
SymStack.pop();
Num x266 = I32V(Memory.loadInt(x265.toInt(), 4));
SymVal x267 = SymMemory.loadSym(x265.toInt(), 4);
Stack.push(x266);
SymStack.push(x267);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x268 = Stack.pop();
SymVal x269 = SymStack.pop();
Num x270 = Stack.pop();
SymVal x271 = SymStack.pop();
Num x272 = x270.i32_add(x268);
Stack.push(x272);
bool x273 = allConcrete(x271, x269);
SymVal x274 = x273 ? Concrete(x272, 32) : x271.add(x269);
SymStack.push(x274);
}
{
Num x275 = Stack.pop();
SymVal x276 = SymStack.pop();
Num x277 = Stack.pop();
SymStack.pop();
int x278 = x277.toInt();
Memory.storeInt(x278, 4, x275.toInt());
SymMemory.storeSym(x278, 4, x276);
}
info("Jump to 0");
__attribute__((musttail)) return x279(std::monostate{});
return std::monostate{};
}
std::monostate x144(std::monostate x145) {
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
Num x146 = Stack.pop();
SymVal x147 = SymStack.pop();
Num x148 = Stack.pop();
SymVal x149 = SymStack.pop();
Num x150 = x148.i32_sub(x146);
Stack.push(x150);
bool x151 = allConcrete(x149, x147);
SymVal x152 = x151 ? Concrete(x150, 32) : x149.minus(x147);
SymStack.push(x152);
}
{
Num x153 = Stack.pop();
SymVal x154 = SymStack.pop();
Frames.set(2, x153);
SymFrames.set(2, x154);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x155 = Stack.pop();
SymVal x156 = SymStack.pop();
Globals.set(0, x155);
SymGlobals.set(0, x156);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x157 = Stack.pop();
SymVal x158 = SymStack.pop();
Num x159 = Stack.pop();
SymStack.pop();
int x160 = x159.toInt();
Memory.storeInt(x160, 12, x157.toInt());
SymMemory.storeSym(x160, 12, x158);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x161 = Stack.pop();
SymVal x162 = SymStack.pop();
Num x163 = Stack.pop();
SymStack.pop();
int x164 = x163.toInt();
Memory.storeInt(x164, 8, x161.toInt());
SymMemory.storeSym(x164, 8, x162);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
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
{
Num x168 = Stack.pop();
SymVal x169 = SymStack.pop();
Num x170 = Stack.pop();
SymStack.pop();
int x171 = x170.toInt();
Memory.storeInt(x171, 4, x168.toInt());
SymMemory.storeSym(x171, 4, x169);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x172 = Stack.pop();
SymVal x173 = SymStack.pop();
Num x174 = Stack.pop();
SymStack.pop();
int x175 = x174.toInt();
Memory.storeInt(x175, 0, x172.toInt());
SymMemory.storeSym(x175, 0, x173);
}
__attribute__((musttail)) return x142(std::monostate{});
return std::monostate{};
}
std::monostate x142(std::monostate x143) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x90(std::monostate{});
return std::monostate{};
}
std::monostate x90(std::monostate x106) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x107 = Stack.pop();
SymStack.pop();
Num x108 = I32V(Memory.loadInt(x107.toInt(), 4));
SymVal x109 = SymMemory.loadSym(x107.toInt(), 4);
Stack.push(x108);
SymStack.push(x109);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x110 = Stack.pop();
SymStack.pop();
Num x111 = I32V(Memory.loadInt(x110.toInt(), 12));
SymVal x112 = SymMemory.loadSym(x110.toInt(), 12);
Stack.push(x111);
SymStack.push(x112);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x113 = Stack.pop();
SymStack.pop();
Num x114 = I32V(Memory.loadInt(x113.toInt(), 8));
SymVal x115 = SymMemory.loadSym(x113.toInt(), 8);
Stack.push(x114);
SymStack.push(x115);
}
{
Num x116 = Stack.pop();
SymVal x117 = SymStack.pop();
Num x118 = Stack.pop();
SymVal x119 = SymStack.pop();
Num x120 = x118.i32_add(x116);
Stack.push(x120);
bool x121 = allConcrete(x119, x117);
SymVal x122 = x121 ? Concrete(x120, 32) : x119.add(x117);
SymStack.push(x122);
}
{
Num x123 = Stack.pop();
SymVal x124 = SymStack.pop();
Num x125 = Stack.pop();
SymVal x126 = SymStack.pop();
Num x127 = x125.i32_lt_s(x123);
Stack.push(x127);
bool x128 = allConcrete(x126, x124);
SymVal x129 = x128 ? Concrete(x127, 32) : x126.lt(x124).bool2bv();
SymStack.push(x129);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x130 = Stack.pop();
SymVal x131 = SymStack.pop();
Num x132 = Stack.pop();
SymVal x133 = SymStack.pop();
Num x134 = x132.i32_and(x130);
Stack.push(x134);
bool x135 = allConcrete(x133, x131);
SymVal x136 = x135 ? Concrete(x134, 32) : x133.bitwise_and(x131);
SymStack.push(x136);
}
{
Num x137 = Stack.pop();
SymVal x138 = SymStack.pop();
Stack.push(I32V((0 == x137.toInt())));
SymStack.push(x138.is_zero().bool2bv());
}
Num x139 = Stack.pop();
info("The br_if(1)'s condition is ", x139.toInt());
{
SymVal x140 = SymStack.pop();
ExploreTree.fillIfElseNode(x140, 0);
}
int x141 = x139.toInt();
if (x141 != 0) {
info("Jump to 1");
{
ExploreTree.moveCursor(true, makeControl(x13, CURRENT_MCONT));
}
__attribute__((musttail)) return x104(std::monostate{});
} else {
info("Continue rest of the block");
{
ExploreTree.moveCursor(false, makeControl(x104, CURRENT_MCONT));
}
__attribute__((musttail)) return x13(std::monostate{});
}
return std::monostate{};
}
std::monostate x104(std::monostate x105) {
__attribute__((musttail)) return x93(std::monostate{});
return std::monostate{};
}
std::monostate x93(std::monostate x94) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(16));
SymStack.push(Concrete(I32V(16), 32));
{
Num x95 = Stack.pop();
SymVal x96 = SymStack.pop();
Num x97 = Stack.pop();
SymVal x98 = SymStack.pop();
Num x99 = x97.i32_add(x95);
Stack.push(x99);
bool x100 = allConcrete(x98, x96);
SymVal x101 = x100 ? Concrete(x99, 32) : x98.add(x96);
SymStack.push(x101);
}
{
Num x102 = Stack.pop();
SymVal x103 = SymStack.pop();
Globals.set(0, x102);
SymGlobals.set(0, x103);
}
return x91(std::monostate{});
}
std::monostate x91(std::monostate x92) {
infoWhen("CALL", "Exiting the function at 3, stackSize =", Stack.size());
Frames.popFrameCallee(3);
SymFrames.popFrameCallee(3);
return enterCC(std::monostate());
}
std::monostate x13(std::monostate x14) {
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x15 = Stack.pop();
SymStack.pop();
Num x16 = I32V(Memory.loadInt(x15.toInt(), 0));
SymVal x17 = SymMemory.loadSym(x15.toInt(), 0);
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
Frames.set(3, x20);
SymFrames.set(3, x21);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x22 = Stack.pop();
SymStack.pop();
Num x23 = I32V(Memory.loadInt(x22.toInt(), 12));
SymVal x24 = SymMemory.loadSym(x22.toInt(), 12);
Stack.push(x23);
SymStack.push(x24);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x25 = Stack.pop();
SymStack.pop();
Num x26 = I32V(Memory.loadInt(x25.toInt(), 4));
SymVal x27 = SymMemory.loadSym(x25.toInt(), 4);
Stack.push(x26);
SymStack.push(x27);
}
{
Num x28 = Stack.pop();
SymVal x29 = SymStack.pop();
Num x30 = Stack.pop();
SymVal x31 = SymStack.pop();
Num x32 = x30.i32_add(x28);
Stack.push(x32);
bool x33 = allConcrete(x31, x29);
SymVal x34 = x33 ? Concrete(x32, 32) : x31.add(x29);
SymStack.push(x34);
}
{
Num x35 = Stack.pop();
SymVal x36 = SymStack.pop();
Frames.set(4, x35);
SymFrames.set(4, x36);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
Stack.push(Globals.get(1));
SymStack.push(SymGlobals.get(1));
{
Num x37 = Stack.pop();
SymVal x38 = SymStack.pop();
Num x39 = Stack.pop();
SymVal x40 = SymStack.pop();
Num x41 = x39.i32_add(x37);
Stack.push(x41);
bool x42 = allConcrete(x40, x38);
SymVal x43 = x42 ? Concrete(x41, 32) : x40.add(x38);
SymStack.push(x43);
}
Stack.push(Frames.get(4));
SymStack.push(SymFrames.get(4));
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x44 = Stack.pop();
SymVal x45 = SymStack.pop();
Num x46 = Stack.pop();
SymVal x47 = SymStack.pop();
Num x48 = x46.i32_shl(x44);
Stack.push(x48);
bool x49 = allConcrete(x47, x45);
SymVal x50 = x49 ? Concrete(x48, 32) : x47.shl(x45);
SymStack.push(x50);
}
{
Num x51 = Stack.pop();
SymVal x52 = SymStack.pop();
Num x53 = Stack.pop();
SymVal x54 = SymStack.pop();
Num x55 = x53.i32_add(x51);
Stack.push(x55);
bool x56 = allConcrete(x54, x52);
SymVal x57 = x56 ? Concrete(x55, 32) : x54.add(x52);
SymStack.push(x57);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x58 = Stack.pop();
SymVal x59 = SymStack.pop();
Num x60 = Stack.pop();
SymStack.pop();
int x61 = x60.toInt();
Memory.storeInt(x61, 0, x58.toInt());
SymMemory.storeSym(x61, 0, x59);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x62 = Stack.pop();
SymStack.pop();
Num x63 = I32V(Memory.loadInt(x62.toInt(), 4));
SymVal x64 = SymMemory.loadSym(x62.toInt(), 4);
Stack.push(x63);
SymStack.push(x64);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x65 = Stack.pop();
SymVal x66 = SymStack.pop();
Num x67 = Stack.pop();
SymVal x68 = SymStack.pop();
Num x69 = x67.i32_add(x65);
Stack.push(x69);
bool x70 = allConcrete(x68, x66);
SymVal x71 = x70 ? Concrete(x69, 32) : x68.add(x66);
SymStack.push(x71);
}
{
Num x72 = Stack.pop();
SymVal x73 = SymStack.pop();
Num x74 = Stack.pop();
SymStack.pop();
int x75 = x74.toInt();
Memory.storeInt(x75, 4, x72.toInt());
SymMemory.storeSym(x75, 4, x73);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x76 = Stack.pop();
SymStack.pop();
Num x77 = I32V(Memory.loadInt(x76.toInt(), 0));
SymVal x78 = SymMemory.loadSym(x76.toInt(), 0);
Stack.push(x77);
SymStack.push(x78);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x79 = Stack.pop();
SymVal x80 = SymStack.pop();
Num x81 = Stack.pop();
SymVal x82 = SymStack.pop();
Num x83 = x81.i32_add(x79);
Stack.push(x83);
bool x84 = allConcrete(x82, x80);
SymVal x85 = x84 ? Concrete(x83, 32) : x82.add(x80);
SymStack.push(x85);
}
{
Num x86 = Stack.pop();
SymVal x87 = SymStack.pop();
Num x88 = Stack.pop();
SymStack.pop();
int x89 = x88.toInt();
Memory.storeInt(x89, 0, x86.toInt());
SymMemory.storeSym(x89, 0, x87);
}
info("Jump to 0");
__attribute__((musttail)) return x90(std::monostate{});
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
Globals.set(0, I32V(66960));
SymGlobals.set(0, Concrete(I32V(66960), 32));
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
infoWhen("CALL", "Taking arguments from stack to call function at ", 7);
Frames.pushFrameCaller(0);
SymFrames.pushFramePtr();
updateCurrentMCont(prependCont(x992, CURRENT_MCONT));
}
__attribute__((musttail)) return x973(std::monostate{});
return std::monostate{};
}

/*****************************************
End of Generated Code
*******************************************/
int main(int argc, char *argv[]) {
  start_concolic_execution_with(Snippet, 2);
  return 0;
}
