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
std::monostate x114(std::monostate);
std::monostate x116(std::monostate);
std::monostate x127(std::monostate);
std::monostate x113(std::monostate);
std::monostate x152(std::monostate);
std::monostate x154(std::monostate);
std::monostate x156(std::monostate);
std::monostate x66(std::monostate);
std::monostate x184(std::monostate);
std::monostate x186(std::monostate);
std::monostate x207(std::monostate);
std::monostate x297(std::monostate);
std::monostate x314(std::monostate);
std::monostate x296(std::monostate);
std::monostate x342(std::monostate);
std::monostate x344(std::monostate);
std::monostate x350(std::monostate);
std::monostate x352(std::monostate);
std::monostate x354(std::monostate);
std::monostate x313(std::monostate);
std::monostate x379(std::monostate);
std::monostate x381(std::monostate);
std::monostate x400(std::monostate);
std::monostate x402(std::monostate);
std::monostate x413(std::monostate);
std::monostate x435(std::monostate);
std::monostate x452(std::monostate);
std::monostate x574(std::monostate);
std::monostate x576(std::monostate);
std::monostate x627(std::monostate);
std::monostate x629(std::monostate);
std::monostate x631(std::monostate);
std::monostate x642(std::monostate);
std::monostate x644(std::monostate);
std::monostate x664(std::monostate);
std::monostate x683(std::monostate);
std::monostate x451(std::monostate);
std::monostate x711(std::monostate);
std::monostate x713(std::monostate);
std::monostate x767(std::monostate);
std::monostate x769(std::monostate);
std::monostate x663(std::monostate);
std::monostate x816(std::monostate);
std::monostate x818(std::monostate);
std::monostate x820(std::monostate);
std::monostate x826(std::monostate);
std::monostate x830(std::monostate);
std::monostate x836(std::monostate);
std::monostate x840(std::monostate);
std::monostate x844(std::monostate);
std::monostate x846(std::monostate);
std::monostate x859(std::monostate);
std::monostate x876(std::monostate);
std::monostate x878(std::monostate);

/************* Functions **************/
std::monostate x878(std::monostate x879) {
infoWhen("CALL", "Returning from the function at 6, stackSize =", Stack.size());
Frames.popFrameCaller(0);
SymFrames.popFrameCaller(0);
return x876(std::monostate{});
}
std::monostate x876(std::monostate x877) {
return enterCC(std::monostate());
}
std::monostate x859(std::monostate x860) {
infoWhen("CALL", "Entered the function at 6, stackSize =", Stack.size());
Frames.pushFrameCallee(2);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Stack.push(Globals.get(0));
SymStack.push(SymGlobals.get(0));
Stack.push(I32V(16));
SymStack.push(Concrete(I32V(16), 32));
{
Num x861 = Stack.pop();
SymVal x862 = SymStack.pop();
Num x863 = Stack.pop();
SymVal x864 = SymStack.pop();
Num x865 = x863.i32_sub(x861);
Stack.push(x865);
bool x866 = allConcrete(x864, x862);
SymVal x867 = x866 ? Concrete(x865, 32) : x864.minus(x862);
SymStack.push(x867);
}
{
Num x868 = Stack.pop();
SymVal x869 = SymStack.pop();
Frames.set(0, x868);
SymFrames.set(0, x869);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x870 = Stack.pop();
SymVal x871 = SymStack.pop();
Globals.set(0, x870);
SymGlobals.set(0, x871);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x872 = Stack.pop();
SymVal x873 = SymStack.pop();
Num x874 = Stack.pop();
SymStack.pop();
int x875 = x874.toInt();
Memory.storeInt(x875, 12, x872.toInt());
SymMemory.storeSym(x875, 12, x873);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 2);
Frames.pushFrameCaller(0);
SymFrames.pushFramePtr();
updateCurrentMCont(prependCont(x846, CURRENT_MCONT));
}
__attribute__((musttail)) return x840(std::monostate{});
return std::monostate{};
}
std::monostate x846(std::monostate x847) {
infoWhen("CALL", "Returning from the function at 2, stackSize =", Stack.size());
Frames.popFrameCaller(0);
SymFrames.popFrameCaller(0);
{
Num x848 = Stack.pop();
SymVal x849 = SymStack.pop();
Frames.set(1, x848);
SymFrames.set(1, x849);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(16));
SymStack.push(Concrete(I32V(16), 32));
{
Num x850 = Stack.pop();
SymVal x851 = SymStack.pop();
Num x852 = Stack.pop();
SymVal x853 = SymStack.pop();
Num x854 = x852.i32_add(x850);
Stack.push(x854);
bool x855 = allConcrete(x853, x851);
SymVal x856 = x855 ? Concrete(x854, 32) : x853.add(x851);
SymStack.push(x856);
}
{
Num x857 = Stack.pop();
SymVal x858 = SymStack.pop();
Globals.set(0, x857);
SymGlobals.set(0, x858);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
return x844(std::monostate{});
}
std::monostate x844(std::monostate x845) {
infoWhen("CALL", "Exiting the function at 6, stackSize =", Stack.size());
Frames.popFrameCallee(2);
SymFrames.popFrameCallee(2);
return enterCC(std::monostate());
}
std::monostate x840(std::monostate x841) {
infoWhen("CALL", "Entered the function at 2, stackSize =", Stack.size());
Stack.push(I32V(3));
SymStack.push(Concrete(I32V(3), 32));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 3);
Num x842 = Stack.pop();
SymVal x843 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x842);
SymFrames.set(0, x843);
updateCurrentMCont(prependCont(x836, CURRENT_MCONT));
}
__attribute__((musttail)) return x186(std::monostate{});
return std::monostate{};
}
std::monostate x836(std::monostate x837) {
infoWhen("CALL", "Returning from the function at 3, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
Stack.push(I32V(3));
SymStack.push(Concrete(I32V(3), 32));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 4);
Num x838 = Stack.pop();
SymVal x839 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x838);
SymFrames.set(0, x839);
updateCurrentMCont(prependCont(x830, CURRENT_MCONT));
}
__attribute__((musttail)) return x413(std::monostate{});
return std::monostate{};
}
std::monostate x830(std::monostate x831) {
infoWhen("CALL", "Returning from the function at 4, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
Stack.push(I32V(3));
SymStack.push(Concrete(I32V(3), 32));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 5);
Num x832 = Stack.pop();
Num x833 = Stack.pop();
SymVal x834 = SymStack.pop();
SymVal x835 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x833);
Frames.set(1, x832);
SymFrames.set(0, x835);
SymFrames.set(1, x834);
updateCurrentMCont(prependCont(x826, CURRENT_MCONT));
}
__attribute__((musttail)) return x663(std::monostate{});
return std::monostate{};
}
std::monostate x826(std::monostate x827) {
infoWhen("CALL", "Returning from the function at 5, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(3));
SymStack.push(Concrete(I32V(3), 32));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 3);
Num x828 = Stack.pop();
SymVal x829 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x828);
SymFrames.set(0, x829);
updateCurrentMCont(prependCont(x820, CURRENT_MCONT));
}
__attribute__((musttail)) return x186(std::monostate{});
return std::monostate{};
}
std::monostate x820(std::monostate x821) {
infoWhen("CALL", "Returning from the function at 3, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
Stack.push(I32V(9));
SymStack.push(Concrete(I32V(9), 32));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 5);
Num x822 = Stack.pop();
Num x823 = Stack.pop();
SymVal x824 = SymStack.pop();
SymVal x825 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x823);
Frames.set(1, x822);
SymFrames.set(0, x825);
SymFrames.set(1, x824);
updateCurrentMCont(prependCont(x818, CURRENT_MCONT));
}
__attribute__((musttail)) return x663(std::monostate{});
return std::monostate{};
}
std::monostate x818(std::monostate x819) {
infoWhen("CALL", "Returning from the function at 5, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
return x816(std::monostate{});
}
std::monostate x816(std::monostate x817) {
infoWhen("CALL", "Exiting the function at 2, stackSize =", Stack.size());
Frames.popFrameCallee(0);
SymFrames.popFrameCallee(0);
return enterCC(std::monostate());
}
std::monostate x663(std::monostate x796) {
infoWhen("CALL", "Entered the function at 5, stackSize =", Stack.size());
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
Num x797 = Stack.pop();
SymVal x798 = SymStack.pop();
Num x799 = Stack.pop();
SymVal x800 = SymStack.pop();
Num x801 = x799.i32_sub(x797);
Stack.push(x801);
bool x802 = allConcrete(x800, x798);
SymVal x803 = x802 ? Concrete(x801, 32) : x800.minus(x798);
SymStack.push(x803);
}
{
Num x804 = Stack.pop();
SymVal x805 = SymStack.pop();
Frames.set(2, x804);
SymFrames.set(2, x805);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x806 = Stack.pop();
SymVal x807 = SymStack.pop();
Globals.set(0, x806);
SymGlobals.set(0, x807);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x808 = Stack.pop();
SymVal x809 = SymStack.pop();
Num x810 = Stack.pop();
SymStack.pop();
int x811 = x810.toInt();
Memory.storeInt(x811, 28, x808.toInt());
SymMemory.storeSym(x811, 28, x809);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x812 = Stack.pop();
SymVal x813 = SymStack.pop();
Num x814 = Stack.pop();
SymStack.pop();
int x815 = x814.toInt();
Memory.storeInt(x815, 24, x812.toInt());
SymMemory.storeSym(x815, 24, x813);
}
__attribute__((musttail)) return x769(std::monostate{});
return std::monostate{};
}
std::monostate x769(std::monostate x770) {
info("Entering the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x771 = Stack.pop();
SymStack.pop();
Num x772 = I32V(Memory.loadInt(x771.toInt(), 28));
SymVal x773 = SymMemory.loadSym(x771.toInt(), 28);
Stack.push(x772);
SymStack.push(x773);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x774 = Stack.pop();
SymStack.pop();
Num x775 = I32V(Memory.loadInt(x774.toInt(), 24));
SymVal x776 = SymMemory.loadSym(x774.toInt(), 24);
Stack.push(x775);
SymStack.push(x776);
}
{
Num x777 = Stack.pop();
SymVal x778 = SymStack.pop();
Num x779 = Stack.pop();
SymVal x780 = SymStack.pop();
Num x781 = x779.i32_lt_s(x777);
Stack.push(x781);
bool x782 = allConcrete(x780, x778);
SymVal x783 = x782 ? Concrete(x781, 32) : x780.lt(x778).bool2bv();
SymStack.push(x783);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x784 = Stack.pop();
SymVal x785 = SymStack.pop();
Num x786 = Stack.pop();
SymVal x787 = SymStack.pop();
Num x788 = x786.i32_and(x784);
Stack.push(x788);
bool x789 = allConcrete(x787, x785);
SymVal x790 = x789 ? Concrete(x788, 32) : x787.bitwise_and(x785);
SymStack.push(x790);
}
{
Num x791 = Stack.pop();
SymVal x792 = SymStack.pop();
Stack.push(I32V((0 == x791.toInt())));
SymStack.push(x792.is_zero().bool2bv());
}
Num x793 = Stack.pop();
info("The br_if(0)'s condition is ", x793.toInt());
{
SymVal x794 = SymStack.pop();
ExploreTree.fillIfElseNode(x794, 1);
}
int x795 = x793.toInt();
if (x795 != 0) {
info("Jump to 0");
{
ExploreTree.moveCursor(true, makeControl(x713, CURRENT_MCONT));
}
__attribute__((musttail)) return x767(std::monostate{});
} else {
info("Continue rest of the block");
{
ExploreTree.moveCursor(false, makeControl(x767, CURRENT_MCONT));
}
__attribute__((musttail)) return x713(std::monostate{});
}
return std::monostate{};
}
std::monostate x767(std::monostate x768) {
__attribute__((musttail)) return x631(std::monostate{});
return std::monostate{};
}
std::monostate x713(std::monostate x714) {
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x715 = Stack.pop();
SymStack.pop();
Num x716 = I32V(Memory.loadInt(x715.toInt(), 24));
SymVal x717 = SymMemory.loadSym(x715.toInt(), 24);
Stack.push(x716);
SymStack.push(x717);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x718 = Stack.pop();
SymVal x719 = SymStack.pop();
Num x720 = Stack.pop();
SymVal x721 = SymStack.pop();
Num x722 = x720.i32_shl(x718);
Stack.push(x722);
bool x723 = allConcrete(x721, x719);
SymVal x724 = x723 ? Concrete(x722, 32) : x721.shl(x719);
SymStack.push(x724);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
Stack.push(Globals.get(1));
SymStack.push(SymGlobals.get(1));
{
Num x725 = Stack.pop();
SymVal x726 = SymStack.pop();
Num x727 = Stack.pop();
SymVal x728 = SymStack.pop();
Num x729 = x727.i32_add(x725);
Stack.push(x729);
bool x730 = allConcrete(x728, x726);
SymVal x731 = x730 ? Concrete(x729, 32) : x728.add(x726);
SymStack.push(x731);
}
{
Num x732 = Stack.pop();
SymVal x733 = SymStack.pop();
Num x734 = Stack.pop();
SymVal x735 = SymStack.pop();
Num x736 = x734.i32_add(x732);
Stack.push(x736);
bool x737 = allConcrete(x735, x733);
SymVal x738 = x737 ? Concrete(x736, 32) : x735.add(x733);
SymStack.push(x738);
}
{
Num x739 = Stack.pop();
SymStack.pop();
Num x740 = I32V(Memory.loadInt(x739.toInt(), 0));
SymVal x741 = SymMemory.loadSym(x739.toInt(), 0);
Stack.push(x740);
SymStack.push(x741);
}
{
Num x742 = Stack.pop();
SymVal x743 = SymStack.pop();
Num x744 = Stack.pop();
SymStack.pop();
int x745 = x744.toInt();
Memory.storeInt(x745, 20, x742.toInt());
SymMemory.storeSym(x745, 20, x743);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x746 = Stack.pop();
SymStack.pop();
Num x747 = I32V(Memory.loadInt(x746.toInt(), 28));
SymVal x748 = SymMemory.loadSym(x746.toInt(), 28);
Stack.push(x747);
SymStack.push(x748);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x749 = Stack.pop();
SymVal x750 = SymStack.pop();
Num x751 = Stack.pop();
SymVal x752 = SymStack.pop();
Num x753 = x751.i32_sub(x749);
Stack.push(x753);
bool x754 = allConcrete(x752, x750);
SymVal x755 = x754 ? Concrete(x753, 32) : x752.minus(x750);
SymStack.push(x755);
}
{
Num x756 = Stack.pop();
SymVal x757 = SymStack.pop();
Num x758 = Stack.pop();
SymStack.pop();
int x759 = x758.toInt();
Memory.storeInt(x759, 16, x756.toInt());
SymMemory.storeSym(x759, 16, x757);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x760 = Stack.pop();
SymStack.pop();
Num x761 = I32V(Memory.loadInt(x760.toInt(), 28));
SymVal x762 = SymMemory.loadSym(x760.toInt(), 28);
Stack.push(x761);
SymStack.push(x762);
}
{
Num x763 = Stack.pop();
SymVal x764 = SymStack.pop();
Num x765 = Stack.pop();
SymStack.pop();
int x766 = x765.toInt();
Memory.storeInt(x766, 12, x763.toInt());
SymMemory.storeSym(x766, 12, x764);
}
__attribute__((musttail)) return x711(std::monostate{});
return std::monostate{};
}
std::monostate x711(std::monostate x712) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x451(std::monostate{});
return std::monostate{};
}
std::monostate x451(std::monostate x685) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x686 = Stack.pop();
SymStack.pop();
Num x687 = I32V(Memory.loadInt(x686.toInt(), 12));
SymVal x688 = SymMemory.loadSym(x686.toInt(), 12);
Stack.push(x687);
SymStack.push(x688);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x689 = Stack.pop();
SymStack.pop();
Num x690 = I32V(Memory.loadInt(x689.toInt(), 24));
SymVal x691 = SymMemory.loadSym(x689.toInt(), 24);
Stack.push(x690);
SymStack.push(x691);
}
{
Num x692 = Stack.pop();
SymVal x693 = SymStack.pop();
Num x694 = Stack.pop();
SymVal x695 = SymStack.pop();
Num x696 = x694.i32_le_s(x692);
Stack.push(x696);
bool x697 = allConcrete(x695, x693);
SymVal x698 = x697 ? Concrete(x696, 32) : x695.le(x693).bool2bv();
SymStack.push(x698);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x699 = Stack.pop();
SymVal x700 = SymStack.pop();
Num x701 = Stack.pop();
SymVal x702 = SymStack.pop();
Num x703 = x701.i32_and(x699);
Stack.push(x703);
bool x704 = allConcrete(x702, x700);
SymVal x705 = x704 ? Concrete(x703, 32) : x702.bitwise_and(x700);
SymStack.push(x705);
}
{
Num x706 = Stack.pop();
SymVal x707 = SymStack.pop();
Stack.push(I32V((0 == x706.toInt())));
SymStack.push(x707.is_zero().bool2bv());
}
Num x708 = Stack.pop();
info("The br_if(1)'s condition is ", x708.toInt());
{
SymVal x709 = SymStack.pop();
ExploreTree.fillIfElseNode(x709, 0);
}
int x710 = x708.toInt();
if (x710 != 0) {
info("Jump to 1");
{
ExploreTree.moveCursor(true, makeControl(x627, CURRENT_MCONT));
}
__attribute__((musttail)) return x683(std::monostate{});
} else {
info("Continue rest of the block");
{
ExploreTree.moveCursor(false, makeControl(x683, CURRENT_MCONT));
}
__attribute__((musttail)) return x627(std::monostate{});
}
return std::monostate{};
}
std::monostate x683(std::monostate x684) {
__attribute__((musttail)) return x664(std::monostate{});
return std::monostate{};
}
std::monostate x664(std::monostate x665) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x666 = Stack.pop();
SymStack.pop();
Num x667 = I32V(Memory.loadInt(x666.toInt(), 28));
SymVal x668 = SymMemory.loadSym(x666.toInt(), 28);
Stack.push(x667);
SymStack.push(x668);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x669 = Stack.pop();
SymStack.pop();
Num x670 = I32V(Memory.loadInt(x669.toInt(), 16));
SymVal x671 = SymMemory.loadSym(x669.toInt(), 16);
Stack.push(x670);
SymStack.push(x671);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x672 = Stack.pop();
SymVal x673 = SymStack.pop();
Num x674 = Stack.pop();
SymVal x675 = SymStack.pop();
Num x676 = x674.i32_sub(x672);
Stack.push(x676);
bool x677 = allConcrete(x675, x673);
SymVal x678 = x677 ? Concrete(x676, 32) : x675.minus(x673);
SymStack.push(x678);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 5);
Num x679 = Stack.pop();
Num x680 = Stack.pop();
SymVal x681 = SymStack.pop();
SymVal x682 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x680);
Frames.set(1, x679);
SymFrames.set(0, x682);
SymFrames.set(1, x681);
updateCurrentMCont(prependCont(x644, CURRENT_MCONT));
}
__attribute__((musttail)) return x663(std::monostate{});
return std::monostate{};
}
std::monostate x644(std::monostate x645) {
infoWhen("CALL", "Returning from the function at 5, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x646 = Stack.pop();
SymStack.pop();
Num x647 = I32V(Memory.loadInt(x646.toInt(), 16));
SymVal x648 = SymMemory.loadSym(x646.toInt(), 16);
Stack.push(x647);
SymStack.push(x648);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x649 = Stack.pop();
SymVal x650 = SymStack.pop();
Num x651 = Stack.pop();
SymVal x652 = SymStack.pop();
Num x653 = x651.i32_add(x649);
Stack.push(x653);
bool x654 = allConcrete(x652, x650);
SymVal x655 = x654 ? Concrete(x653, 32) : x652.add(x650);
SymStack.push(x655);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x656 = Stack.pop();
SymStack.pop();
Num x657 = I32V(Memory.loadInt(x656.toInt(), 24));
SymVal x658 = SymMemory.loadSym(x656.toInt(), 24);
Stack.push(x657);
SymStack.push(x658);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 5);
Num x659 = Stack.pop();
Num x660 = Stack.pop();
SymVal x661 = SymStack.pop();
SymVal x662 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x660);
Frames.set(1, x659);
SymFrames.set(0, x662);
SymFrames.set(1, x661);
updateCurrentMCont(prependCont(x642, CURRENT_MCONT));
}
__attribute__((musttail)) return x663(std::monostate{});
return std::monostate{};
}
std::monostate x642(std::monostate x643) {
infoWhen("CALL", "Returning from the function at 5, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
__attribute__((musttail)) return x631(std::monostate{});
return std::monostate{};
}
std::monostate x631(std::monostate x632) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(32));
SymStack.push(Concrete(I32V(32), 32));
{
Num x633 = Stack.pop();
SymVal x634 = SymStack.pop();
Num x635 = Stack.pop();
SymVal x636 = SymStack.pop();
Num x637 = x635.i32_add(x633);
Stack.push(x637);
bool x638 = allConcrete(x636, x634);
SymVal x639 = x638 ? Concrete(x637, 32) : x636.add(x634);
SymStack.push(x639);
}
{
Num x640 = Stack.pop();
SymVal x641 = SymStack.pop();
Globals.set(0, x640);
SymGlobals.set(0, x641);
}
return x629(std::monostate{});
}
std::monostate x629(std::monostate x630) {
infoWhen("CALL", "Exiting the function at 5, stackSize =", Stack.size());
Frames.popFrameCallee(6);
SymFrames.popFrameCallee(6);
return enterCC(std::monostate());
}
std::monostate x627(std::monostate x628) {
__attribute__((musttail)) return x576(std::monostate{});
return std::monostate{};
}
std::monostate x576(std::monostate x577) {
info("Entering the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x578 = Stack.pop();
SymStack.pop();
Num x579 = I32V(Memory.loadInt(x578.toInt(), 12));
SymVal x580 = SymMemory.loadSym(x578.toInt(), 12);
Stack.push(x579);
SymStack.push(x580);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x581 = Stack.pop();
SymVal x582 = SymStack.pop();
Num x583 = Stack.pop();
SymVal x584 = SymStack.pop();
Num x585 = x583.i32_shl(x581);
Stack.push(x585);
bool x586 = allConcrete(x584, x582);
SymVal x587 = x586 ? Concrete(x585, 32) : x584.shl(x582);
SymStack.push(x587);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
Stack.push(Globals.get(1));
SymStack.push(SymGlobals.get(1));
{
Num x588 = Stack.pop();
SymVal x589 = SymStack.pop();
Num x590 = Stack.pop();
SymVal x591 = SymStack.pop();
Num x592 = x590.i32_add(x588);
Stack.push(x592);
bool x593 = allConcrete(x591, x589);
SymVal x594 = x593 ? Concrete(x592, 32) : x591.add(x589);
SymStack.push(x594);
}
{
Num x595 = Stack.pop();
SymVal x596 = SymStack.pop();
Num x597 = Stack.pop();
SymVal x598 = SymStack.pop();
Num x599 = x597.i32_add(x595);
Stack.push(x599);
bool x600 = allConcrete(x598, x596);
SymVal x601 = x600 ? Concrete(x599, 32) : x598.add(x596);
SymStack.push(x601);
}
{
Num x602 = Stack.pop();
SymStack.pop();
Num x603 = I32V(Memory.loadInt(x602.toInt(), 0));
SymVal x604 = SymMemory.loadSym(x602.toInt(), 0);
Stack.push(x603);
SymStack.push(x604);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x605 = Stack.pop();
SymStack.pop();
Num x606 = I32V(Memory.loadInt(x605.toInt(), 20));
SymVal x607 = SymMemory.loadSym(x605.toInt(), 20);
Stack.push(x606);
SymStack.push(x607);
}
{
Num x608 = Stack.pop();
SymVal x609 = SymStack.pop();
Num x610 = Stack.pop();
SymVal x611 = SymStack.pop();
Num x612 = x610.i32_le_s(x608);
Stack.push(x612);
bool x613 = allConcrete(x611, x609);
SymVal x614 = x613 ? Concrete(x612, 32) : x611.le(x609).bool2bv();
SymStack.push(x614);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x615 = Stack.pop();
SymVal x616 = SymStack.pop();
Num x617 = Stack.pop();
SymVal x618 = SymStack.pop();
Num x619 = x617.i32_and(x615);
Stack.push(x619);
bool x620 = allConcrete(x618, x616);
SymVal x621 = x620 ? Concrete(x619, 32) : x618.bitwise_and(x616);
SymStack.push(x621);
}
{
Num x622 = Stack.pop();
SymVal x623 = SymStack.pop();
Stack.push(I32V((0 == x622.toInt())));
SymStack.push(x623.is_zero().bool2bv());
}
Num x624 = Stack.pop();
info("The br_if(0)'s condition is ", x624.toInt());
{
SymVal x625 = SymStack.pop();
ExploreTree.fillIfElseNode(x625, 1);
}
int x626 = x624.toInt();
if (x626 != 0) {
info("Jump to 0");
{
ExploreTree.moveCursor(true, makeControl(x452, CURRENT_MCONT));
}
__attribute__((musttail)) return x574(std::monostate{});
} else {
info("Continue rest of the block");
{
ExploreTree.moveCursor(false, makeControl(x574, CURRENT_MCONT));
}
__attribute__((musttail)) return x452(std::monostate{});
}
return std::monostate{};
}
std::monostate x574(std::monostate x575) {
__attribute__((musttail)) return x435(std::monostate{});
return std::monostate{};
}
std::monostate x452(std::monostate x453) {
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x454 = Stack.pop();
SymStack.pop();
Num x455 = I32V(Memory.loadInt(x454.toInt(), 16));
SymVal x456 = SymMemory.loadSym(x454.toInt(), 16);
Stack.push(x455);
SymStack.push(x456);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x457 = Stack.pop();
SymVal x458 = SymStack.pop();
Num x459 = Stack.pop();
SymVal x460 = SymStack.pop();
Num x461 = x459.i32_add(x457);
Stack.push(x461);
bool x462 = allConcrete(x460, x458);
SymVal x463 = x462 ? Concrete(x461, 32) : x460.add(x458);
SymStack.push(x463);
}
{
Num x464 = Stack.pop();
SymVal x465 = SymStack.pop();
Num x466 = Stack.pop();
SymStack.pop();
int x467 = x466.toInt();
Memory.storeInt(x467, 16, x464.toInt());
SymMemory.storeSym(x467, 16, x465);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x468 = Stack.pop();
SymStack.pop();
Num x469 = I32V(Memory.loadInt(x468.toInt(), 12));
SymVal x470 = SymMemory.loadSym(x468.toInt(), 12);
Stack.push(x469);
SymStack.push(x470);
}
{
Num x471 = Stack.pop();
SymVal x472 = SymStack.pop();
Frames.set(3, x471);
SymFrames.set(3, x472);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
Stack.push(Globals.get(1));
SymStack.push(SymGlobals.get(1));
{
Num x473 = Stack.pop();
SymVal x474 = SymStack.pop();
Num x475 = Stack.pop();
SymVal x476 = SymStack.pop();
Num x477 = x475.i32_add(x473);
Stack.push(x477);
bool x478 = allConcrete(x476, x474);
SymVal x479 = x478 ? Concrete(x477, 32) : x476.add(x474);
SymStack.push(x479);
}
{
Num x480 = Stack.pop();
SymVal x481 = SymStack.pop();
Frames.set(4, x480);
SymFrames.set(4, x481);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x482 = Stack.pop();
SymVal x483 = SymStack.pop();
Frames.set(5, x482);
SymFrames.set(5, x483);
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
Num x484 = Stack.pop();
SymVal x485 = SymStack.pop();
Num x486 = Stack.pop();
SymVal x487 = SymStack.pop();
Num x488 = x486.i32_shl(x484);
Stack.push(x488);
bool x489 = allConcrete(x487, x485);
SymVal x490 = x489 ? Concrete(x488, 32) : x487.shl(x485);
SymStack.push(x490);
}
{
Num x491 = Stack.pop();
SymVal x492 = SymStack.pop();
Num x493 = Stack.pop();
SymVal x494 = SymStack.pop();
Num x495 = x493.i32_add(x491);
Stack.push(x495);
bool x496 = allConcrete(x494, x492);
SymVal x497 = x496 ? Concrete(x495, 32) : x494.add(x492);
SymStack.push(x497);
}
{
Num x498 = Stack.pop();
SymStack.pop();
Num x499 = I32V(Memory.loadInt(x498.toInt(), 0));
SymVal x500 = SymMemory.loadSym(x498.toInt(), 0);
Stack.push(x499);
SymStack.push(x500);
}
{
Num x501 = Stack.pop();
SymVal x502 = SymStack.pop();
Num x503 = Stack.pop();
SymStack.pop();
int x504 = x503.toInt();
Memory.storeInt(x504, 8, x501.toInt());
SymMemory.storeSym(x504, 8, x502);
}
Stack.push(Frames.get(4));
SymStack.push(SymFrames.get(4));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x505 = Stack.pop();
SymStack.pop();
Num x506 = I32V(Memory.loadInt(x505.toInt(), 16));
SymVal x507 = SymMemory.loadSym(x505.toInt(), 16);
Stack.push(x506);
SymStack.push(x507);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x508 = Stack.pop();
SymVal x509 = SymStack.pop();
Num x510 = Stack.pop();
SymVal x511 = SymStack.pop();
Num x512 = x510.i32_shl(x508);
Stack.push(x512);
bool x513 = allConcrete(x511, x509);
SymVal x514 = x513 ? Concrete(x512, 32) : x511.shl(x509);
SymStack.push(x514);
}
{
Num x515 = Stack.pop();
SymVal x516 = SymStack.pop();
Num x517 = Stack.pop();
SymVal x518 = SymStack.pop();
Num x519 = x517.i32_add(x515);
Stack.push(x519);
bool x520 = allConcrete(x518, x516);
SymVal x521 = x520 ? Concrete(x519, 32) : x518.add(x516);
SymStack.push(x521);
}
{
Num x522 = Stack.pop();
SymStack.pop();
Num x523 = I32V(Memory.loadInt(x522.toInt(), 0));
SymVal x524 = SymMemory.loadSym(x522.toInt(), 0);
Stack.push(x523);
SymStack.push(x524);
}
{
Num x525 = Stack.pop();
SymVal x526 = SymStack.pop();
Frames.set(6, x525);
SymFrames.set(6, x526);
}
Stack.push(Frames.get(4));
SymStack.push(SymFrames.get(4));
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
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x530 = Stack.pop();
SymVal x531 = SymStack.pop();
Num x532 = Stack.pop();
SymVal x533 = SymStack.pop();
Num x534 = x532.i32_shl(x530);
Stack.push(x534);
bool x535 = allConcrete(x533, x531);
SymVal x536 = x535 ? Concrete(x534, 32) : x533.shl(x531);
SymStack.push(x536);
}
{
Num x537 = Stack.pop();
SymVal x538 = SymStack.pop();
Num x539 = Stack.pop();
SymVal x540 = SymStack.pop();
Num x541 = x539.i32_add(x537);
Stack.push(x541);
bool x542 = allConcrete(x540, x538);
SymVal x543 = x542 ? Concrete(x541, 32) : x540.add(x538);
SymStack.push(x543);
}
Stack.push(Frames.get(6));
SymStack.push(SymFrames.get(6));
{
Num x544 = Stack.pop();
SymVal x545 = SymStack.pop();
Num x546 = Stack.pop();
SymStack.pop();
int x547 = x546.toInt();
Memory.storeInt(x547, 0, x544.toInt());
SymMemory.storeSym(x547, 0, x545);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x548 = Stack.pop();
SymStack.pop();
Num x549 = I32V(Memory.loadInt(x548.toInt(), 8));
SymVal x550 = SymMemory.loadSym(x548.toInt(), 8);
Stack.push(x549);
SymStack.push(x550);
}
{
Num x551 = Stack.pop();
SymVal x552 = SymStack.pop();
Frames.set(7, x551);
SymFrames.set(7, x552);
}
Stack.push(Frames.get(4));
SymStack.push(SymFrames.get(4));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x553 = Stack.pop();
SymStack.pop();
Num x554 = I32V(Memory.loadInt(x553.toInt(), 16));
SymVal x555 = SymMemory.loadSym(x553.toInt(), 16);
Stack.push(x554);
SymStack.push(x555);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x556 = Stack.pop();
SymVal x557 = SymStack.pop();
Num x558 = Stack.pop();
SymVal x559 = SymStack.pop();
Num x560 = x558.i32_shl(x556);
Stack.push(x560);
bool x561 = allConcrete(x559, x557);
SymVal x562 = x561 ? Concrete(x560, 32) : x559.shl(x557);
SymStack.push(x562);
}
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
Stack.push(Frames.get(7));
SymStack.push(SymFrames.get(7));
{
Num x570 = Stack.pop();
SymVal x571 = SymStack.pop();
Num x572 = Stack.pop();
SymStack.pop();
int x573 = x572.toInt();
Memory.storeInt(x573, 0, x570.toInt());
SymMemory.storeSym(x573, 0, x571);
}
__attribute__((musttail)) return x435(std::monostate{});
return std::monostate{};
}
std::monostate x435(std::monostate x436) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x437 = Stack.pop();
SymStack.pop();
Num x438 = I32V(Memory.loadInt(x437.toInt(), 12));
SymVal x439 = SymMemory.loadSym(x437.toInt(), 12);
Stack.push(x438);
SymStack.push(x439);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x440 = Stack.pop();
SymVal x441 = SymStack.pop();
Num x442 = Stack.pop();
SymVal x443 = SymStack.pop();
Num x444 = x442.i32_add(x440);
Stack.push(x444);
bool x445 = allConcrete(x443, x441);
SymVal x446 = x445 ? Concrete(x444, 32) : x443.add(x441);
SymStack.push(x446);
}
{
Num x447 = Stack.pop();
SymVal x448 = SymStack.pop();
Num x449 = Stack.pop();
SymStack.pop();
int x450 = x449.toInt();
Memory.storeInt(x450, 12, x447.toInt());
SymMemory.storeSym(x450, 12, x448);
}
info("Jump to 0");
__attribute__((musttail)) return x451(std::monostate{});
return std::monostate{};
}
std::monostate x413(std::monostate x414) {
infoWhen("CALL", "Entered the function at 4, stackSize =", Stack.size());
Frames.pushFrameCallee(1);
SymFrames.pushFrameSlot(32);
Stack.push(Globals.get(0));
SymStack.push(SymGlobals.get(0));
Stack.push(I32V(16));
SymStack.push(Concrete(I32V(16), 32));
{
Num x415 = Stack.pop();
SymVal x416 = SymStack.pop();
Num x417 = Stack.pop();
SymVal x418 = SymStack.pop();
Num x419 = x417.i32_sub(x415);
Stack.push(x419);
bool x420 = allConcrete(x418, x416);
SymVal x421 = x420 ? Concrete(x419, 32) : x418.minus(x416);
SymStack.push(x421);
}
{
Num x422 = Stack.pop();
SymVal x423 = SymStack.pop();
Frames.set(1, x422);
SymFrames.set(1, x423);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x424 = Stack.pop();
SymVal x425 = SymStack.pop();
Globals.set(0, x424);
SymGlobals.set(0, x425);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x426 = Stack.pop();
SymVal x427 = SymStack.pop();
Num x428 = Stack.pop();
SymStack.pop();
int x429 = x428.toInt();
Memory.storeInt(x429, 12, x426.toInt());
SymMemory.storeSym(x429, 12, x427);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x430 = Stack.pop();
SymStack.pop();
Num x431 = I32V(Memory.loadInt(x430.toInt(), 12));
SymVal x432 = SymMemory.loadSym(x430.toInt(), 12);
Stack.push(x431);
SymStack.push(x432);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 7);
Num x433 = Stack.pop();
SymVal x434 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x433);
SymFrames.set(0, x434);
updateCurrentMCont(prependCont(x402, CURRENT_MCONT));
}
__attribute__((musttail)) return x381(std::monostate{});
return std::monostate{};
}
std::monostate x402(std::monostate x403) {
infoWhen("CALL", "Returning from the function at 7, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(16));
SymStack.push(Concrete(I32V(16), 32));
{
Num x404 = Stack.pop();
SymVal x405 = SymStack.pop();
Num x406 = Stack.pop();
SymVal x407 = SymStack.pop();
Num x408 = x406.i32_add(x404);
Stack.push(x408);
bool x409 = allConcrete(x407, x405);
SymVal x410 = x409 ? Concrete(x408, 32) : x407.add(x405);
SymStack.push(x410);
}
{
Num x411 = Stack.pop();
SymVal x412 = SymStack.pop();
Globals.set(0, x411);
SymGlobals.set(0, x412);
}
return x400(std::monostate{});
}
std::monostate x400(std::monostate x401) {
infoWhen("CALL", "Exiting the function at 4, stackSize =", Stack.size());
Frames.popFrameCallee(1);
SymFrames.popFrameCallee(1);
return enterCC(std::monostate());
}
std::monostate x381(std::monostate x382) {
infoWhen("CALL", "Entered the function at 7, stackSize =", Stack.size());
Frames.pushFrameCallee(3);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Stack.push(Globals.get(0));
SymStack.push(SymGlobals.get(0));
Stack.push(I32V(16));
SymStack.push(Concrete(I32V(16), 32));
{
Num x383 = Stack.pop();
SymVal x384 = SymStack.pop();
Num x385 = Stack.pop();
SymVal x386 = SymStack.pop();
Num x387 = x385.i32_sub(x383);
Stack.push(x387);
bool x388 = allConcrete(x386, x384);
SymVal x389 = x388 ? Concrete(x387, 32) : x386.minus(x384);
SymStack.push(x389);
}
{
Num x390 = Stack.pop();
SymVal x391 = SymStack.pop();
Frames.set(1, x390);
SymFrames.set(1, x391);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x392 = Stack.pop();
SymVal x393 = SymStack.pop();
Num x394 = Stack.pop();
SymStack.pop();
int x395 = x394.toInt();
Memory.storeInt(x395, 12, x392.toInt());
SymMemory.storeSym(x395, 12, x393);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x396 = Stack.pop();
SymVal x397 = SymStack.pop();
Num x398 = Stack.pop();
SymStack.pop();
int x399 = x398.toInt();
Memory.storeInt(x399, 8, x396.toInt());
SymMemory.storeSym(x399, 8, x397);
}
__attribute__((musttail)) return x379(std::monostate{});
return std::monostate{};
}
std::monostate x379(std::monostate x380) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x313(std::monostate{});
return std::monostate{};
}
std::monostate x313(std::monostate x356) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x357 = Stack.pop();
SymStack.pop();
Num x358 = I32V(Memory.loadInt(x357.toInt(), 8));
SymVal x359 = SymMemory.loadSym(x357.toInt(), 8);
Stack.push(x358);
SymStack.push(x359);
}
Stack.push(I32V(30));
SymStack.push(Concrete(I32V(30), 32));
{
Num x360 = Stack.pop();
SymVal x361 = SymStack.pop();
Num x362 = Stack.pop();
SymVal x363 = SymStack.pop();
Num x364 = x362.i32_lt_s(x360);
Stack.push(x364);
bool x365 = allConcrete(x363, x361);
SymVal x366 = x365 ? Concrete(x364, 32) : x363.lt(x361).bool2bv();
SymStack.push(x366);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x367 = Stack.pop();
SymVal x368 = SymStack.pop();
Num x369 = Stack.pop();
SymVal x370 = SymStack.pop();
Num x371 = x369.i32_and(x367);
Stack.push(x371);
bool x372 = allConcrete(x370, x368);
SymVal x373 = x372 ? Concrete(x371, 32) : x370.bitwise_and(x368);
SymStack.push(x373);
}
{
Num x374 = Stack.pop();
SymVal x375 = SymStack.pop();
Stack.push(I32V((0 == x374.toInt())));
SymStack.push(x375.is_zero().bool2bv());
}
Num x376 = Stack.pop();
info("The br_if(1)'s condition is ", x376.toInt());
{
SymVal x377 = SymStack.pop();
ExploreTree.fillIfElseNode(x377, 0);
}
int x378 = x376.toInt();
if (x378 != 0) {
info("Jump to 1");
{
ExploreTree.moveCursor(true, makeControl(x344, CURRENT_MCONT));
}
__attribute__((musttail)) return x354(std::monostate{});
} else {
info("Continue rest of the block");
{
ExploreTree.moveCursor(false, makeControl(x354, CURRENT_MCONT));
}
__attribute__((musttail)) return x344(std::monostate{});
}
return std::monostate{};
}
std::monostate x354(std::monostate x355) {
__attribute__((musttail)) return x352(std::monostate{});
return std::monostate{};
}
std::monostate x352(std::monostate x353) {
info("Exiting the block, stackSize =", Stack.size());
return x350(std::monostate{});
}
std::monostate x350(std::monostate x351) {
infoWhen("CALL", "Exiting the function at 7, stackSize =", Stack.size());
Frames.popFrameCallee(3);
SymFrames.popFrameCallee(3);
return enterCC(std::monostate());
}
std::monostate x344(std::monostate x345) {
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x346 = Stack.pop();
SymVal x347 = SymStack.pop();
Num x348 = Stack.pop();
SymStack.pop();
int x349 = x348.toInt();
Memory.storeInt(x349, 4, x346.toInt());
SymMemory.storeSym(x349, 4, x347);
}
__attribute__((musttail)) return x342(std::monostate{});
return std::monostate{};
}
std::monostate x342(std::monostate x343) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x296(std::monostate{});
return std::monostate{};
}
std::monostate x296(std::monostate x316) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x317 = Stack.pop();
SymStack.pop();
Num x318 = I32V(Memory.loadInt(x317.toInt(), 4));
SymVal x319 = SymMemory.loadSym(x317.toInt(), 4);
Stack.push(x318);
SymStack.push(x319);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x320 = Stack.pop();
SymStack.pop();
Num x321 = I32V(Memory.loadInt(x320.toInt(), 12));
SymVal x322 = SymMemory.loadSym(x320.toInt(), 12);
Stack.push(x321);
SymStack.push(x322);
}
{
Num x323 = Stack.pop();
SymVal x324 = SymStack.pop();
Num x325 = Stack.pop();
SymVal x326 = SymStack.pop();
Num x327 = x325.i32_lt_s(x323);
Stack.push(x327);
bool x328 = allConcrete(x326, x324);
SymVal x329 = x328 ? Concrete(x327, 32) : x326.lt(x324).bool2bv();
SymStack.push(x329);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x330 = Stack.pop();
SymVal x331 = SymStack.pop();
Num x332 = Stack.pop();
SymVal x333 = SymStack.pop();
Num x334 = x332.i32_and(x330);
Stack.push(x334);
bool x335 = allConcrete(x333, x331);
SymVal x336 = x335 ? Concrete(x334, 32) : x333.bitwise_and(x331);
SymStack.push(x336);
}
{
Num x337 = Stack.pop();
SymVal x338 = SymStack.pop();
Stack.push(I32V((0 == x337.toInt())));
SymStack.push(x338.is_zero().bool2bv());
}
Num x339 = Stack.pop();
info("The br_if(1)'s condition is ", x339.toInt());
{
SymVal x340 = SymStack.pop();
ExploreTree.fillIfElseNode(x340, 0);
}
int x341 = x339.toInt();
if (x341 != 0) {
info("Jump to 1");
{
ExploreTree.moveCursor(true, makeControl(x207, CURRENT_MCONT));
}
__attribute__((musttail)) return x314(std::monostate{});
} else {
info("Continue rest of the block");
{
ExploreTree.moveCursor(false, makeControl(x314, CURRENT_MCONT));
}
__attribute__((musttail)) return x207(std::monostate{});
}
return std::monostate{};
}
std::monostate x314(std::monostate x315) {
__attribute__((musttail)) return x297(std::monostate{});
return std::monostate{};
}
std::monostate x297(std::monostate x298) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x299 = Stack.pop();
SymStack.pop();
Num x300 = I32V(Memory.loadInt(x299.toInt(), 8));
SymVal x301 = SymMemory.loadSym(x299.toInt(), 8);
Stack.push(x300);
SymStack.push(x301);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x302 = Stack.pop();
SymVal x303 = SymStack.pop();
Num x304 = Stack.pop();
SymVal x305 = SymStack.pop();
Num x306 = x304.i32_add(x302);
Stack.push(x306);
bool x307 = allConcrete(x305, x303);
SymVal x308 = x307 ? Concrete(x306, 32) : x305.add(x303);
SymStack.push(x308);
}
{
Num x309 = Stack.pop();
SymVal x310 = SymStack.pop();
Num x311 = Stack.pop();
SymStack.pop();
int x312 = x311.toInt();
Memory.storeInt(x312, 8, x309.toInt());
SymMemory.storeSym(x312, 8, x310);
}
info("Jump to 0");
__attribute__((musttail)) return x313(std::monostate{});
return std::monostate{};
}
std::monostate x207(std::monostate x208) {
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x209 = Stack.pop();
SymStack.pop();
Num x210 = I32V(Memory.loadInt(x209.toInt(), 4));
SymVal x211 = SymMemory.loadSym(x209.toInt(), 4);
Stack.push(x210);
SymStack.push(x211);
}
{
Num x212 = Stack.pop();
SymVal x213 = SymStack.pop();
Frames.set(2, x212);
SymFrames.set(2, x213);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x214 = Stack.pop();
SymVal x215 = SymStack.pop();
Num x216 = Stack.pop();
SymVal x217 = SymStack.pop();
Num x218 = x216.i32_shl(x214);
Stack.push(x218);
bool x219 = allConcrete(x217, x215);
SymVal x220 = x219 ? Concrete(x218, 32) : x217.shl(x215);
SymStack.push(x220);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
Stack.push(Globals.get(1));
SymStack.push(SymGlobals.get(1));
{
Num x221 = Stack.pop();
SymVal x222 = SymStack.pop();
Num x223 = Stack.pop();
SymVal x224 = SymStack.pop();
Num x225 = x223.i32_add(x221);
Stack.push(x225);
bool x226 = allConcrete(x224, x222);
SymVal x227 = x226 ? Concrete(x225, 32) : x224.add(x222);
SymStack.push(x227);
}
{
Num x228 = Stack.pop();
SymVal x229 = SymStack.pop();
Num x230 = Stack.pop();
SymVal x231 = SymStack.pop();
Num x232 = x230.i32_add(x228);
Stack.push(x232);
bool x233 = allConcrete(x231, x229);
SymVal x234 = x233 ? Concrete(x232, 32) : x231.add(x229);
SymStack.push(x234);
}
{
Num x235 = Stack.pop();
SymVal x236 = SymStack.pop();
Frames.set(3, x235);
SymFrames.set(3, x236);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x237 = Stack.pop();
SymStack.pop();
Num x238 = I32V(Memory.loadInt(x237.toInt(), 0));
SymVal x239 = SymMemory.loadSym(x237.toInt(), 0);
Stack.push(x238);
SymStack.push(x239);
}
Stack.push(I32V(7));
SymStack.push(Concrete(I32V(7), 32));
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
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(-4));
SymStack.push(Concrete(I32V(-4), 32));
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
SymStack.pop();
Num x262 = I32V(Memory.loadInt(x261.toInt(), 0));
SymVal x263 = SymMemory.loadSym(x261.toInt(), 0);
Stack.push(x262);
SymStack.push(x263);
}
{
Num x264 = Stack.pop();
SymVal x265 = SymStack.pop();
Num x266 = Stack.pop();
SymVal x267 = SymStack.pop();
Num x268 = x266.i32_sub(x264);
Stack.push(x268);
bool x269 = allConcrete(x267, x265);
SymVal x270 = x269 ? Concrete(x268, 32) : x267.minus(x265);
SymStack.push(x270);
}
Stack.push(I32V(3));
SymStack.push(Concrete(I32V(3), 32));
{
Num x271 = Stack.pop();
SymVal x272 = SymStack.pop();
Num x273 = Stack.pop();
SymVal x274 = SymStack.pop();
Num x275 = x273.i32_add(x271);
Stack.push(x275);
bool x276 = allConcrete(x274, x272);
SymVal x277 = x276 ? Concrete(x275, 32) : x274.add(x272);
SymStack.push(x277);
}
{
Num x278 = Stack.pop();
SymVal x279 = SymStack.pop();
Num x280 = Stack.pop();
SymStack.pop();
int x281 = x280.toInt();
Memory.storeInt(x281, 0, x278.toInt());
SymMemory.storeSym(x281, 0, x279);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x282 = Stack.pop();
SymStack.pop();
Num x283 = I32V(Memory.loadInt(x282.toInt(), 4));
SymVal x284 = SymMemory.loadSym(x282.toInt(), 4);
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
Memory.storeInt(x295, 4, x292.toInt());
SymMemory.storeSym(x295, 4, x293);
}
info("Jump to 0");
__attribute__((musttail)) return x296(std::monostate{});
return std::monostate{};
}
std::monostate x186(std::monostate x187) {
infoWhen("CALL", "Entered the function at 3, stackSize =", Stack.size());
Frames.pushFrameCallee(4);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Stack.push(Globals.get(0));
SymStack.push(SymGlobals.get(0));
Stack.push(I32V(16));
SymStack.push(Concrete(I32V(16), 32));
{
Num x188 = Stack.pop();
SymVal x189 = SymStack.pop();
Num x190 = Stack.pop();
SymVal x191 = SymStack.pop();
Num x192 = x190.i32_sub(x188);
Stack.push(x192);
bool x193 = allConcrete(x191, x189);
SymVal x194 = x193 ? Concrete(x192, 32) : x191.minus(x189);
SymStack.push(x194);
}
{
Num x195 = Stack.pop();
SymVal x196 = SymStack.pop();
Frames.set(1, x195);
SymFrames.set(1, x196);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x197 = Stack.pop();
SymVal x198 = SymStack.pop();
Globals.set(0, x197);
SymGlobals.set(0, x198);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x199 = Stack.pop();
SymVal x200 = SymStack.pop();
Num x201 = Stack.pop();
SymStack.pop();
int x202 = x201.toInt();
Memory.storeInt(x202, 12, x199.toInt());
SymMemory.storeSym(x202, 12, x200);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x203 = Stack.pop();
SymVal x204 = SymStack.pop();
Num x205 = Stack.pop();
SymStack.pop();
int x206 = x205.toInt();
Memory.storeInt(x206, 8, x203.toInt());
SymMemory.storeSym(x206, 8, x204);
}
__attribute__((musttail)) return x184(std::monostate{});
return std::monostate{};
}
std::monostate x184(std::monostate x185) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x66(std::monostate{});
return std::monostate{};
}
std::monostate x66(std::monostate x158) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x159 = Stack.pop();
SymStack.pop();
Num x160 = I32V(Memory.loadInt(x159.toInt(), 8));
SymVal x161 = SymMemory.loadSym(x159.toInt(), 8);
Stack.push(x160);
SymStack.push(x161);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x162 = Stack.pop();
SymStack.pop();
Num x163 = I32V(Memory.loadInt(x162.toInt(), 12));
SymVal x164 = SymMemory.loadSym(x162.toInt(), 12);
Stack.push(x163);
SymStack.push(x164);
}
{
Num x165 = Stack.pop();
SymVal x166 = SymStack.pop();
Num x167 = Stack.pop();
SymVal x168 = SymStack.pop();
Num x169 = x167.i32_lt_s(x165);
Stack.push(x169);
bool x170 = allConcrete(x168, x166);
SymVal x171 = x170 ? Concrete(x169, 32) : x168.lt(x166).bool2bv();
SymStack.push(x171);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x172 = Stack.pop();
SymVal x173 = SymStack.pop();
Num x174 = Stack.pop();
SymVal x175 = SymStack.pop();
Num x176 = x174.i32_and(x172);
Stack.push(x176);
bool x177 = allConcrete(x175, x173);
SymVal x178 = x177 ? Concrete(x176, 32) : x175.bitwise_and(x173);
SymStack.push(x178);
}
{
Num x179 = Stack.pop();
SymVal x180 = SymStack.pop();
Stack.push(I32V((0 == x179.toInt())));
SymStack.push(x180.is_zero().bool2bv());
}
Num x181 = Stack.pop();
info("The br_if(1)'s condition is ", x181.toInt());
{
SymVal x182 = SymStack.pop();
ExploreTree.fillIfElseNode(x182, 0);
}
int x183 = x181.toInt();
if (x183 != 0) {
info("Jump to 1");
{
ExploreTree.moveCursor(true, makeControl(x13, CURRENT_MCONT));
}
__attribute__((musttail)) return x156(std::monostate{});
} else {
info("Continue rest of the block");
{
ExploreTree.moveCursor(false, makeControl(x156, CURRENT_MCONT));
}
__attribute__((musttail)) return x13(std::monostate{});
}
return std::monostate{};
}
std::monostate x156(std::monostate x157) {
__attribute__((musttail)) return x154(std::monostate{});
return std::monostate{};
}
std::monostate x154(std::monostate x155) {
info("Exiting the block, stackSize =", Stack.size());
__attribute__((musttail)) return x152(std::monostate{});
return std::monostate{};
}
std::monostate x152(std::monostate x153) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x113(std::monostate{});
return std::monostate{};
}
std::monostate x113(std::monostate x129) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x130 = Stack.pop();
SymStack.pop();
Num x131 = I32V(Memory.loadInt(x130.toInt(), 8));
SymVal x132 = SymMemory.loadSym(x130.toInt(), 8);
Stack.push(x131);
SymStack.push(x132);
}
Stack.push(I32V(10));
SymStack.push(Concrete(I32V(10), 32));
{
Num x133 = Stack.pop();
SymVal x134 = SymStack.pop();
Num x135 = Stack.pop();
SymVal x136 = SymStack.pop();
Num x137 = x135.i32_lt_s(x133);
Stack.push(x137);
bool x138 = allConcrete(x136, x134);
SymVal x139 = x138 ? Concrete(x137, 32) : x136.lt(x134).bool2bv();
SymStack.push(x139);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x140 = Stack.pop();
SymVal x141 = SymStack.pop();
Num x142 = Stack.pop();
SymVal x143 = SymStack.pop();
Num x144 = x142.i32_and(x140);
Stack.push(x144);
bool x145 = allConcrete(x143, x141);
SymVal x146 = x145 ? Concrete(x144, 32) : x143.bitwise_and(x141);
SymStack.push(x146);
}
{
Num x147 = Stack.pop();
SymVal x148 = SymStack.pop();
Stack.push(I32V((0 == x147.toInt())));
SymStack.push(x148.is_zero().bool2bv());
}
Num x149 = Stack.pop();
info("The br_if(1)'s condition is ", x149.toInt());
{
SymVal x150 = SymStack.pop();
ExploreTree.fillIfElseNode(x150, 0);
}
int x151 = x149.toInt();
if (x151 != 0) {
info("Jump to 1");
{
ExploreTree.moveCursor(true, makeControl(x67, CURRENT_MCONT));
}
__attribute__((musttail)) return x127(std::monostate{});
} else {
info("Continue rest of the block");
{
ExploreTree.moveCursor(false, makeControl(x127, CURRENT_MCONT));
}
__attribute__((musttail)) return x67(std::monostate{});
}
return std::monostate{};
}
std::monostate x127(std::monostate x128) {
__attribute__((musttail)) return x116(std::monostate{});
return std::monostate{};
}
std::monostate x116(std::monostate x117) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(16));
SymStack.push(Concrete(I32V(16), 32));
{
Num x118 = Stack.pop();
SymVal x119 = SymStack.pop();
Num x120 = Stack.pop();
SymVal x121 = SymStack.pop();
Num x122 = x120.i32_add(x118);
Stack.push(x122);
bool x123 = allConcrete(x121, x119);
SymVal x124 = x123 ? Concrete(x122, 32) : x121.add(x119);
SymStack.push(x124);
}
{
Num x125 = Stack.pop();
SymVal x126 = SymStack.pop();
Globals.set(0, x125);
SymGlobals.set(0, x126);
}
return x114(std::monostate{});
}
std::monostate x114(std::monostate x115) {
infoWhen("CALL", "Exiting the function at 3, stackSize =", Stack.size());
Frames.popFrameCallee(4);
SymFrames.popFrameCallee(4);
return enterCC(std::monostate());
}
std::monostate x67(std::monostate x68) {
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x69 = Stack.pop();
SymStack.pop();
Num x70 = I32V(Memory.loadInt(x69.toInt(), 8));
SymVal x71 = SymMemory.loadSym(x69.toInt(), 8);
Stack.push(x70);
SymStack.push(x71);
}
{
Num x72 = Stack.pop();
SymVal x73 = SymStack.pop();
Frames.set(4, x72);
SymFrames.set(4, x73);
}
Stack.push(Frames.get(4));
SymStack.push(SymFrames.get(4));
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x74 = Stack.pop();
SymVal x75 = SymStack.pop();
Num x76 = Stack.pop();
SymVal x77 = SymStack.pop();
Num x78 = x76.i32_shl(x74);
Stack.push(x78);
bool x79 = allConcrete(x77, x75);
SymVal x80 = x79 ? Concrete(x78, 32) : x77.shl(x75);
SymStack.push(x80);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
Stack.push(Globals.get(1));
SymStack.push(SymGlobals.get(1));
{
Num x81 = Stack.pop();
SymVal x82 = SymStack.pop();
Num x83 = Stack.pop();
SymVal x84 = SymStack.pop();
Num x85 = x83.i32_add(x81);
Stack.push(x85);
bool x86 = allConcrete(x84, x82);
SymVal x87 = x86 ? Concrete(x85, 32) : x84.add(x82);
SymStack.push(x87);
}
{
Num x88 = Stack.pop();
SymVal x89 = SymStack.pop();
Num x90 = Stack.pop();
SymVal x91 = SymStack.pop();
Num x92 = x90.i32_add(x88);
Stack.push(x92);
bool x93 = allConcrete(x91, x89);
SymVal x94 = x93 ? Concrete(x92, 32) : x91.add(x89);
SymStack.push(x94);
}
Stack.push(Frames.get(4));
SymStack.push(SymFrames.get(4));
{
Num x95 = Stack.pop();
SymVal x96 = SymStack.pop();
Num x97 = Stack.pop();
SymStack.pop();
int x98 = x97.toInt();
Memory.storeInt(x98, 0, x95.toInt());
SymMemory.storeSym(x98, 0, x96);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x99 = Stack.pop();
SymStack.pop();
Num x100 = I32V(Memory.loadInt(x99.toInt(), 8));
SymVal x101 = SymMemory.loadSym(x99.toInt(), 8);
Stack.push(x100);
SymStack.push(x101);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x102 = Stack.pop();
SymVal x103 = SymStack.pop();
Num x104 = Stack.pop();
SymVal x105 = SymStack.pop();
Num x106 = x104.i32_add(x102);
Stack.push(x106);
bool x107 = allConcrete(x105, x103);
SymVal x108 = x107 ? Concrete(x106, 32) : x105.add(x103);
SymStack.push(x108);
}
{
Num x109 = Stack.pop();
SymVal x110 = SymStack.pop();
Num x111 = Stack.pop();
SymStack.pop();
int x112 = x111.toInt();
Memory.storeInt(x112, 8, x109.toInt());
SymMemory.storeSym(x112, 8, x110);
}
info("Jump to 0");
__attribute__((musttail)) return x113(std::monostate{});
return std::monostate{};
}
std::monostate x13(std::monostate x14) {
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x15 = Stack.pop();
SymStack.pop();
Num x16 = I32V(Memory.loadInt(x15.toInt(), 8));
SymVal x17 = SymMemory.loadSym(x15.toInt(), 8);
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
Frames.set(2, x20);
SymFrames.set(2, x21);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x22 = Stack.pop();
SymStack.pop();
Num x23 = I32V(Memory.loadInt(x22.toInt(), 8));
SymVal x24 = SymMemory.loadSym(x22.toInt(), 8);
Stack.push(x23);
SymStack.push(x24);
}
{
Num x25 = Stack.pop();
SymVal x26 = SymStack.pop();
Frames.set(3, x25);
SymFrames.set(3, x26);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
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
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
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
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x48 = Stack.pop();
SymVal x49 = SymStack.pop();
Num x50 = Stack.pop();
SymStack.pop();
int x51 = x50.toInt();
Memory.storeInt(x51, 0, x48.toInt());
SymMemory.storeSym(x51, 0, x49);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x52 = Stack.pop();
SymStack.pop();
Num x53 = I32V(Memory.loadInt(x52.toInt(), 8));
SymVal x54 = SymMemory.loadSym(x52.toInt(), 8);
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
Memory.storeInt(x65, 8, x62.toInt());
SymMemory.storeSym(x65, 8, x63);
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
Globals.set(0, I32V(66608));
SymGlobals.set(0, Concrete(I32V(66608), 32));
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
infoWhen("CALL", "Taking arguments from stack to call function at ", 6);
Frames.pushFrameCaller(0);
SymFrames.pushFramePtr();
updateCurrentMCont(prependCont(x878, CURRENT_MCONT));
}
__attribute__((musttail)) return x859(std::monostate{});
return std::monostate{};
}

/*****************************************
End of Generated Code
*******************************************/
int main(int argc, char *argv[]) {
  start_concolic_execution_with(Snippet, 2);
  return 0;
}
