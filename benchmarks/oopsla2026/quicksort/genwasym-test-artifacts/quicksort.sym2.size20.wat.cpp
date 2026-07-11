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
std::monostate x123(std::monostate);
std::monostate x125(std::monostate);
std::monostate x136(std::monostate);
std::monostate x122(std::monostate);
std::monostate x161(std::monostate);
std::monostate x163(std::monostate);
std::monostate x165(std::monostate);
std::monostate x66(std::monostate);
std::monostate x193(std::monostate);
std::monostate x195(std::monostate);
std::monostate x216(std::monostate);
std::monostate x218(std::monostate);
std::monostate x233(std::monostate);
std::monostate x250(std::monostate);
std::monostate x372(std::monostate);
std::monostate x374(std::monostate);
std::monostate x425(std::monostate);
std::monostate x427(std::monostate);
std::monostate x429(std::monostate);
std::monostate x440(std::monostate);
std::monostate x442(std::monostate);
std::monostate x462(std::monostate);
std::monostate x481(std::monostate);
std::monostate x249(std::monostate);
std::monostate x509(std::monostate);
std::monostate x511(std::monostate);
std::monostate x565(std::monostate);
std::monostate x567(std::monostate);
std::monostate x461(std::monostate);
std::monostate x614(std::monostate);
std::monostate x616(std::monostate);
std::monostate x618(std::monostate);
std::monostate x624(std::monostate);
std::monostate x628(std::monostate);
std::monostate x634(std::monostate);
std::monostate x638(std::monostate);
std::monostate x642(std::monostate);
std::monostate x644(std::monostate);
std::monostate x657(std::monostate);
std::monostate x674(std::monostate);
std::monostate x676(std::monostate);

/************* Functions **************/
std::monostate x676(std::monostate x677) {
infoWhen("CALL", "Returning from the function at 6, stackSize =", Stack.size());
Frames.popFrameCaller(0);
SymFrames.popFrameCaller(0);
return x674(std::monostate{});
}
std::monostate x674(std::monostate x675) {
return enterCC(std::monostate());
}
std::monostate x657(std::monostate x658) {
infoWhen("CALL", "Entered the function at 6, stackSize =", Stack.size());
Frames.pushFrameCallee(2);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Stack.push(Globals.get(0));
SymStack.push(SymGlobals.get(0));
Stack.push(I32V(16));
SymStack.push(Concrete(I32V(16), 32));
{
Num x659 = Stack.pop();
SymVal x660 = SymStack.pop();
Num x661 = Stack.pop();
SymVal x662 = SymStack.pop();
Num x663 = x661.i32_sub(x659);
Stack.push(x663);
bool x664 = allConcrete(x662, x660);
SymVal x665 = x664 ? Concrete(x663, 32) : x662.minus(x660);
SymStack.push(x665);
}
{
Num x666 = Stack.pop();
SymVal x667 = SymStack.pop();
Frames.set(0, x666);
SymFrames.set(0, x667);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x668 = Stack.pop();
SymVal x669 = SymStack.pop();
Globals.set(0, x668);
SymGlobals.set(0, x669);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x670 = Stack.pop();
SymVal x671 = SymStack.pop();
Num x672 = Stack.pop();
SymStack.pop();
int x673 = x672.toInt();
Memory.storeInt(x673, 12, x670.toInt());
SymMemory.storeSym(x673, 12, x671);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 2);
Frames.pushFrameCaller(0);
SymFrames.pushFramePtr();
updateCurrentMCont(prependCont(x644, CURRENT_MCONT));
}
__attribute__((musttail)) return x638(std::monostate{});
return std::monostate{};
}
std::monostate x644(std::monostate x645) {
infoWhen("CALL", "Returning from the function at 2, stackSize =", Stack.size());
Frames.popFrameCaller(0);
SymFrames.popFrameCaller(0);
{
Num x646 = Stack.pop();
SymVal x647 = SymStack.pop();
Frames.set(1, x646);
SymFrames.set(1, x647);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(16));
SymStack.push(Concrete(I32V(16), 32));
{
Num x648 = Stack.pop();
SymVal x649 = SymStack.pop();
Num x650 = Stack.pop();
SymVal x651 = SymStack.pop();
Num x652 = x650.i32_add(x648);
Stack.push(x652);
bool x653 = allConcrete(x651, x649);
SymVal x654 = x653 ? Concrete(x652, 32) : x651.add(x649);
SymStack.push(x654);
}
{
Num x655 = Stack.pop();
SymVal x656 = SymStack.pop();
Globals.set(0, x655);
SymGlobals.set(0, x656);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
return x642(std::monostate{});
}
std::monostate x642(std::monostate x643) {
infoWhen("CALL", "Exiting the function at 6, stackSize =", Stack.size());
Frames.popFrameCallee(2);
SymFrames.popFrameCallee(2);
return enterCC(std::monostate());
}
std::monostate x638(std::monostate x639) {
infoWhen("CALL", "Entered the function at 2, stackSize =", Stack.size());
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 3);
Num x640 = Stack.pop();
SymVal x641 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x640);
SymFrames.set(0, x641);
updateCurrentMCont(prependCont(x634, CURRENT_MCONT));
}
__attribute__((musttail)) return x195(std::monostate{});
return std::monostate{};
}
std::monostate x634(std::monostate x635) {
infoWhen("CALL", "Returning from the function at 3, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 4);
Num x636 = Stack.pop();
SymVal x637 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x636);
SymFrames.set(0, x637);
updateCurrentMCont(prependCont(x628, CURRENT_MCONT));
}
__attribute__((musttail)) return x218(std::monostate{});
return std::monostate{};
}
std::monostate x628(std::monostate x629) {
infoWhen("CALL", "Returning from the function at 4, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
Stack.push(I32V(3));
SymStack.push(Concrete(I32V(3), 32));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 5);
Num x630 = Stack.pop();
Num x631 = Stack.pop();
SymVal x632 = SymStack.pop();
SymVal x633 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x631);
Frames.set(1, x630);
SymFrames.set(0, x633);
SymFrames.set(1, x632);
updateCurrentMCont(prependCont(x624, CURRENT_MCONT));
}
__attribute__((musttail)) return x461(std::monostate{});
return std::monostate{};
}
std::monostate x624(std::monostate x625) {
infoWhen("CALL", "Returning from the function at 5, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 3);
Num x626 = Stack.pop();
SymVal x627 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x626);
SymFrames.set(0, x627);
updateCurrentMCont(prependCont(x618, CURRENT_MCONT));
}
__attribute__((musttail)) return x195(std::monostate{});
return std::monostate{};
}
std::monostate x618(std::monostate x619) {
infoWhen("CALL", "Returning from the function at 3, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
Stack.push(I32V(19));
SymStack.push(Concrete(I32V(19), 32));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 5);
Num x620 = Stack.pop();
Num x621 = Stack.pop();
SymVal x622 = SymStack.pop();
SymVal x623 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x621);
Frames.set(1, x620);
SymFrames.set(0, x623);
SymFrames.set(1, x622);
updateCurrentMCont(prependCont(x616, CURRENT_MCONT));
}
__attribute__((musttail)) return x461(std::monostate{});
return std::monostate{};
}
std::monostate x616(std::monostate x617) {
infoWhen("CALL", "Returning from the function at 5, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
return x614(std::monostate{});
}
std::monostate x614(std::monostate x615) {
infoWhen("CALL", "Exiting the function at 2, stackSize =", Stack.size());
Frames.popFrameCallee(0);
SymFrames.popFrameCallee(0);
return enterCC(std::monostate());
}
std::monostate x461(std::monostate x594) {
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
Num x595 = Stack.pop();
SymVal x596 = SymStack.pop();
Num x597 = Stack.pop();
SymVal x598 = SymStack.pop();
Num x599 = x597.i32_sub(x595);
Stack.push(x599);
bool x600 = allConcrete(x598, x596);
SymVal x601 = x600 ? Concrete(x599, 32) : x598.minus(x596);
SymStack.push(x601);
}
{
Num x602 = Stack.pop();
SymVal x603 = SymStack.pop();
Frames.set(2, x602);
SymFrames.set(2, x603);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x604 = Stack.pop();
SymVal x605 = SymStack.pop();
Globals.set(0, x604);
SymGlobals.set(0, x605);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x606 = Stack.pop();
SymVal x607 = SymStack.pop();
Num x608 = Stack.pop();
SymStack.pop();
int x609 = x608.toInt();
Memory.storeInt(x609, 28, x606.toInt());
SymMemory.storeSym(x609, 28, x607);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x610 = Stack.pop();
SymVal x611 = SymStack.pop();
Num x612 = Stack.pop();
SymStack.pop();
int x613 = x612.toInt();
Memory.storeInt(x613, 24, x610.toInt());
SymMemory.storeSym(x613, 24, x611);
}
__attribute__((musttail)) return x567(std::monostate{});
return std::monostate{};
}
std::monostate x567(std::monostate x568) {
info("Entering the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x569 = Stack.pop();
SymStack.pop();
Num x570 = I32V(Memory.loadInt(x569.toInt(), 28));
SymVal x571 = SymMemory.loadSym(x569.toInt(), 28);
Stack.push(x570);
SymStack.push(x571);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x572 = Stack.pop();
SymStack.pop();
Num x573 = I32V(Memory.loadInt(x572.toInt(), 24));
SymVal x574 = SymMemory.loadSym(x572.toInt(), 24);
Stack.push(x573);
SymStack.push(x574);
}
{
Num x575 = Stack.pop();
SymVal x576 = SymStack.pop();
Num x577 = Stack.pop();
SymVal x578 = SymStack.pop();
Num x579 = x577.i32_lt_s(x575);
Stack.push(x579);
bool x580 = allConcrete(x578, x576);
SymVal x581 = x580 ? Concrete(x579, 32) : x578.lt(x576).bool2bv();
SymStack.push(x581);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x582 = Stack.pop();
SymVal x583 = SymStack.pop();
Num x584 = Stack.pop();
SymVal x585 = SymStack.pop();
Num x586 = x584.i32_and(x582);
Stack.push(x586);
bool x587 = allConcrete(x585, x583);
SymVal x588 = x587 ? Concrete(x586, 32) : x585.bitwise_and(x583);
SymStack.push(x588);
}
{
Num x589 = Stack.pop();
SymVal x590 = SymStack.pop();
Stack.push(I32V((0 == x589.toInt())));
SymStack.push(x590.is_zero().bool2bv());
}
Num x591 = Stack.pop();
info("The br_if(0)'s condition is ", x591.toInt());
{
SymVal x592 = SymStack.pop();
ExploreTree.fillIfElseNode(x592, 1);
}
int x593 = x591.toInt();
if (x593 != 0) {
info("Jump to 0");
{
ExploreTree.moveCursor(true, makeControl(x511, CURRENT_MCONT));
}
__attribute__((musttail)) return x565(std::monostate{});
} else {
info("Continue rest of the block");
{
ExploreTree.moveCursor(false, makeControl(x565, CURRENT_MCONT));
}
__attribute__((musttail)) return x511(std::monostate{});
}
return std::monostate{};
}
std::monostate x565(std::monostate x566) {
__attribute__((musttail)) return x429(std::monostate{});
return std::monostate{};
}
std::monostate x511(std::monostate x512) {
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x513 = Stack.pop();
SymStack.pop();
Num x514 = I32V(Memory.loadInt(x513.toInt(), 24));
SymVal x515 = SymMemory.loadSym(x513.toInt(), 24);
Stack.push(x514);
SymStack.push(x515);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x516 = Stack.pop();
SymVal x517 = SymStack.pop();
Num x518 = Stack.pop();
SymVal x519 = SymStack.pop();
Num x520 = x518.i32_shl(x516);
Stack.push(x520);
bool x521 = allConcrete(x519, x517);
SymVal x522 = x521 ? Concrete(x520, 32) : x519.shl(x517);
SymStack.push(x522);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
Stack.push(Globals.get(1));
SymStack.push(SymGlobals.get(1));
{
Num x523 = Stack.pop();
SymVal x524 = SymStack.pop();
Num x525 = Stack.pop();
SymVal x526 = SymStack.pop();
Num x527 = x525.i32_add(x523);
Stack.push(x527);
bool x528 = allConcrete(x526, x524);
SymVal x529 = x528 ? Concrete(x527, 32) : x526.add(x524);
SymStack.push(x529);
}
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
SymStack.pop();
Num x538 = I32V(Memory.loadInt(x537.toInt(), 0));
SymVal x539 = SymMemory.loadSym(x537.toInt(), 0);
Stack.push(x538);
SymStack.push(x539);
}
{
Num x540 = Stack.pop();
SymVal x541 = SymStack.pop();
Num x542 = Stack.pop();
SymStack.pop();
int x543 = x542.toInt();
Memory.storeInt(x543, 20, x540.toInt());
SymMemory.storeSym(x543, 20, x541);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x544 = Stack.pop();
SymStack.pop();
Num x545 = I32V(Memory.loadInt(x544.toInt(), 28));
SymVal x546 = SymMemory.loadSym(x544.toInt(), 28);
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
Num x551 = x549.i32_sub(x547);
Stack.push(x551);
bool x552 = allConcrete(x550, x548);
SymVal x553 = x552 ? Concrete(x551, 32) : x550.minus(x548);
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
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x558 = Stack.pop();
SymStack.pop();
Num x559 = I32V(Memory.loadInt(x558.toInt(), 28));
SymVal x560 = SymMemory.loadSym(x558.toInt(), 28);
Stack.push(x559);
SymStack.push(x560);
}
{
Num x561 = Stack.pop();
SymVal x562 = SymStack.pop();
Num x563 = Stack.pop();
SymStack.pop();
int x564 = x563.toInt();
Memory.storeInt(x564, 12, x561.toInt());
SymMemory.storeSym(x564, 12, x562);
}
__attribute__((musttail)) return x509(std::monostate{});
return std::monostate{};
}
std::monostate x509(std::monostate x510) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x249(std::monostate{});
return std::monostate{};
}
std::monostate x249(std::monostate x483) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x484 = Stack.pop();
SymStack.pop();
Num x485 = I32V(Memory.loadInt(x484.toInt(), 12));
SymVal x486 = SymMemory.loadSym(x484.toInt(), 12);
Stack.push(x485);
SymStack.push(x486);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x487 = Stack.pop();
SymStack.pop();
Num x488 = I32V(Memory.loadInt(x487.toInt(), 24));
SymVal x489 = SymMemory.loadSym(x487.toInt(), 24);
Stack.push(x488);
SymStack.push(x489);
}
{
Num x490 = Stack.pop();
SymVal x491 = SymStack.pop();
Num x492 = Stack.pop();
SymVal x493 = SymStack.pop();
Num x494 = x492.i32_le_s(x490);
Stack.push(x494);
bool x495 = allConcrete(x493, x491);
SymVal x496 = x495 ? Concrete(x494, 32) : x493.le(x491).bool2bv();
SymStack.push(x496);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x497 = Stack.pop();
SymVal x498 = SymStack.pop();
Num x499 = Stack.pop();
SymVal x500 = SymStack.pop();
Num x501 = x499.i32_and(x497);
Stack.push(x501);
bool x502 = allConcrete(x500, x498);
SymVal x503 = x502 ? Concrete(x501, 32) : x500.bitwise_and(x498);
SymStack.push(x503);
}
{
Num x504 = Stack.pop();
SymVal x505 = SymStack.pop();
Stack.push(I32V((0 == x504.toInt())));
SymStack.push(x505.is_zero().bool2bv());
}
Num x506 = Stack.pop();
info("The br_if(1)'s condition is ", x506.toInt());
{
SymVal x507 = SymStack.pop();
ExploreTree.fillIfElseNode(x507, 0);
}
int x508 = x506.toInt();
if (x508 != 0) {
info("Jump to 1");
{
ExploreTree.moveCursor(true, makeControl(x425, CURRENT_MCONT));
}
__attribute__((musttail)) return x481(std::monostate{});
} else {
info("Continue rest of the block");
{
ExploreTree.moveCursor(false, makeControl(x481, CURRENT_MCONT));
}
__attribute__((musttail)) return x425(std::monostate{});
}
return std::monostate{};
}
std::monostate x481(std::monostate x482) {
__attribute__((musttail)) return x462(std::monostate{});
return std::monostate{};
}
std::monostate x462(std::monostate x463) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x464 = Stack.pop();
SymStack.pop();
Num x465 = I32V(Memory.loadInt(x464.toInt(), 28));
SymVal x466 = SymMemory.loadSym(x464.toInt(), 28);
Stack.push(x465);
SymStack.push(x466);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x467 = Stack.pop();
SymStack.pop();
Num x468 = I32V(Memory.loadInt(x467.toInt(), 16));
SymVal x469 = SymMemory.loadSym(x467.toInt(), 16);
Stack.push(x468);
SymStack.push(x469);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x470 = Stack.pop();
SymVal x471 = SymStack.pop();
Num x472 = Stack.pop();
SymVal x473 = SymStack.pop();
Num x474 = x472.i32_sub(x470);
Stack.push(x474);
bool x475 = allConcrete(x473, x471);
SymVal x476 = x475 ? Concrete(x474, 32) : x473.minus(x471);
SymStack.push(x476);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 5);
Num x477 = Stack.pop();
Num x478 = Stack.pop();
SymVal x479 = SymStack.pop();
SymVal x480 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x478);
Frames.set(1, x477);
SymFrames.set(0, x480);
SymFrames.set(1, x479);
updateCurrentMCont(prependCont(x442, CURRENT_MCONT));
}
__attribute__((musttail)) return x461(std::monostate{});
return std::monostate{};
}
std::monostate x442(std::monostate x443) {
infoWhen("CALL", "Returning from the function at 5, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x444 = Stack.pop();
SymStack.pop();
Num x445 = I32V(Memory.loadInt(x444.toInt(), 16));
SymVal x446 = SymMemory.loadSym(x444.toInt(), 16);
Stack.push(x445);
SymStack.push(x446);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x447 = Stack.pop();
SymVal x448 = SymStack.pop();
Num x449 = Stack.pop();
SymVal x450 = SymStack.pop();
Num x451 = x449.i32_add(x447);
Stack.push(x451);
bool x452 = allConcrete(x450, x448);
SymVal x453 = x452 ? Concrete(x451, 32) : x450.add(x448);
SymStack.push(x453);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x454 = Stack.pop();
SymStack.pop();
Num x455 = I32V(Memory.loadInt(x454.toInt(), 24));
SymVal x456 = SymMemory.loadSym(x454.toInt(), 24);
Stack.push(x455);
SymStack.push(x456);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 5);
Num x457 = Stack.pop();
Num x458 = Stack.pop();
SymVal x459 = SymStack.pop();
SymVal x460 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x458);
Frames.set(1, x457);
SymFrames.set(0, x460);
SymFrames.set(1, x459);
updateCurrentMCont(prependCont(x440, CURRENT_MCONT));
}
__attribute__((musttail)) return x461(std::monostate{});
return std::monostate{};
}
std::monostate x440(std::monostate x441) {
infoWhen("CALL", "Returning from the function at 5, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
__attribute__((musttail)) return x429(std::monostate{});
return std::monostate{};
}
std::monostate x429(std::monostate x430) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(32));
SymStack.push(Concrete(I32V(32), 32));
{
Num x431 = Stack.pop();
SymVal x432 = SymStack.pop();
Num x433 = Stack.pop();
SymVal x434 = SymStack.pop();
Num x435 = x433.i32_add(x431);
Stack.push(x435);
bool x436 = allConcrete(x434, x432);
SymVal x437 = x436 ? Concrete(x435, 32) : x434.add(x432);
SymStack.push(x437);
}
{
Num x438 = Stack.pop();
SymVal x439 = SymStack.pop();
Globals.set(0, x438);
SymGlobals.set(0, x439);
}
return x427(std::monostate{});
}
std::monostate x427(std::monostate x428) {
infoWhen("CALL", "Exiting the function at 5, stackSize =", Stack.size());
Frames.popFrameCallee(6);
SymFrames.popFrameCallee(6);
return enterCC(std::monostate());
}
std::monostate x425(std::monostate x426) {
__attribute__((musttail)) return x374(std::monostate{});
return std::monostate{};
}
std::monostate x374(std::monostate x375) {
info("Entering the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x376 = Stack.pop();
SymStack.pop();
Num x377 = I32V(Memory.loadInt(x376.toInt(), 12));
SymVal x378 = SymMemory.loadSym(x376.toInt(), 12);
Stack.push(x377);
SymStack.push(x378);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x379 = Stack.pop();
SymVal x380 = SymStack.pop();
Num x381 = Stack.pop();
SymVal x382 = SymStack.pop();
Num x383 = x381.i32_shl(x379);
Stack.push(x383);
bool x384 = allConcrete(x382, x380);
SymVal x385 = x384 ? Concrete(x383, 32) : x382.shl(x380);
SymStack.push(x385);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
Stack.push(Globals.get(1));
SymStack.push(SymGlobals.get(1));
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
SymVal x396 = SymStack.pop();
Num x397 = x395.i32_add(x393);
Stack.push(x397);
bool x398 = allConcrete(x396, x394);
SymVal x399 = x398 ? Concrete(x397, 32) : x396.add(x394);
SymStack.push(x399);
}
{
Num x400 = Stack.pop();
SymStack.pop();
Num x401 = I32V(Memory.loadInt(x400.toInt(), 0));
SymVal x402 = SymMemory.loadSym(x400.toInt(), 0);
Stack.push(x401);
SymStack.push(x402);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x403 = Stack.pop();
SymStack.pop();
Num x404 = I32V(Memory.loadInt(x403.toInt(), 20));
SymVal x405 = SymMemory.loadSym(x403.toInt(), 20);
Stack.push(x404);
SymStack.push(x405);
}
{
Num x406 = Stack.pop();
SymVal x407 = SymStack.pop();
Num x408 = Stack.pop();
SymVal x409 = SymStack.pop();
Num x410 = x408.i32_le_s(x406);
Stack.push(x410);
bool x411 = allConcrete(x409, x407);
SymVal x412 = x411 ? Concrete(x410, 32) : x409.le(x407).bool2bv();
SymStack.push(x412);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x413 = Stack.pop();
SymVal x414 = SymStack.pop();
Num x415 = Stack.pop();
SymVal x416 = SymStack.pop();
Num x417 = x415.i32_and(x413);
Stack.push(x417);
bool x418 = allConcrete(x416, x414);
SymVal x419 = x418 ? Concrete(x417, 32) : x416.bitwise_and(x414);
SymStack.push(x419);
}
{
Num x420 = Stack.pop();
SymVal x421 = SymStack.pop();
Stack.push(I32V((0 == x420.toInt())));
SymStack.push(x421.is_zero().bool2bv());
}
Num x422 = Stack.pop();
info("The br_if(0)'s condition is ", x422.toInt());
{
SymVal x423 = SymStack.pop();
ExploreTree.fillIfElseNode(x423, 1);
}
int x424 = x422.toInt();
if (x424 != 0) {
info("Jump to 0");
{
ExploreTree.moveCursor(true, makeControl(x250, CURRENT_MCONT));
}
__attribute__((musttail)) return x372(std::monostate{});
} else {
info("Continue rest of the block");
{
ExploreTree.moveCursor(false, makeControl(x372, CURRENT_MCONT));
}
__attribute__((musttail)) return x250(std::monostate{});
}
return std::monostate{};
}
std::monostate x372(std::monostate x373) {
__attribute__((musttail)) return x233(std::monostate{});
return std::monostate{};
}
std::monostate x250(std::monostate x251) {
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x252 = Stack.pop();
SymStack.pop();
Num x253 = I32V(Memory.loadInt(x252.toInt(), 16));
SymVal x254 = SymMemory.loadSym(x252.toInt(), 16);
Stack.push(x253);
SymStack.push(x254);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x255 = Stack.pop();
SymVal x256 = SymStack.pop();
Num x257 = Stack.pop();
SymVal x258 = SymStack.pop();
Num x259 = x257.i32_add(x255);
Stack.push(x259);
bool x260 = allConcrete(x258, x256);
SymVal x261 = x260 ? Concrete(x259, 32) : x258.add(x256);
SymStack.push(x261);
}
{
Num x262 = Stack.pop();
SymVal x263 = SymStack.pop();
Num x264 = Stack.pop();
SymStack.pop();
int x265 = x264.toInt();
Memory.storeInt(x265, 16, x262.toInt());
SymMemory.storeSym(x265, 16, x263);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x266 = Stack.pop();
SymStack.pop();
Num x267 = I32V(Memory.loadInt(x266.toInt(), 12));
SymVal x268 = SymMemory.loadSym(x266.toInt(), 12);
Stack.push(x267);
SymStack.push(x268);
}
{
Num x269 = Stack.pop();
SymVal x270 = SymStack.pop();
Frames.set(3, x269);
SymFrames.set(3, x270);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
Stack.push(Globals.get(1));
SymStack.push(SymGlobals.get(1));
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
Frames.set(4, x278);
SymFrames.set(4, x279);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x280 = Stack.pop();
SymVal x281 = SymStack.pop();
Frames.set(5, x280);
SymFrames.set(5, x281);
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
Num x282 = Stack.pop();
SymVal x283 = SymStack.pop();
Num x284 = Stack.pop();
SymVal x285 = SymStack.pop();
Num x286 = x284.i32_shl(x282);
Stack.push(x286);
bool x287 = allConcrete(x285, x283);
SymVal x288 = x287 ? Concrete(x286, 32) : x285.shl(x283);
SymStack.push(x288);
}
{
Num x289 = Stack.pop();
SymVal x290 = SymStack.pop();
Num x291 = Stack.pop();
SymVal x292 = SymStack.pop();
Num x293 = x291.i32_add(x289);
Stack.push(x293);
bool x294 = allConcrete(x292, x290);
SymVal x295 = x294 ? Concrete(x293, 32) : x292.add(x290);
SymStack.push(x295);
}
{
Num x296 = Stack.pop();
SymStack.pop();
Num x297 = I32V(Memory.loadInt(x296.toInt(), 0));
SymVal x298 = SymMemory.loadSym(x296.toInt(), 0);
Stack.push(x297);
SymStack.push(x298);
}
{
Num x299 = Stack.pop();
SymVal x300 = SymStack.pop();
Num x301 = Stack.pop();
SymStack.pop();
int x302 = x301.toInt();
Memory.storeInt(x302, 8, x299.toInt());
SymMemory.storeSym(x302, 8, x300);
}
Stack.push(Frames.get(4));
SymStack.push(SymFrames.get(4));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x303 = Stack.pop();
SymStack.pop();
Num x304 = I32V(Memory.loadInt(x303.toInt(), 16));
SymVal x305 = SymMemory.loadSym(x303.toInt(), 16);
Stack.push(x304);
SymStack.push(x305);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x306 = Stack.pop();
SymVal x307 = SymStack.pop();
Num x308 = Stack.pop();
SymVal x309 = SymStack.pop();
Num x310 = x308.i32_shl(x306);
Stack.push(x310);
bool x311 = allConcrete(x309, x307);
SymVal x312 = x311 ? Concrete(x310, 32) : x309.shl(x307);
SymStack.push(x312);
}
{
Num x313 = Stack.pop();
SymVal x314 = SymStack.pop();
Num x315 = Stack.pop();
SymVal x316 = SymStack.pop();
Num x317 = x315.i32_add(x313);
Stack.push(x317);
bool x318 = allConcrete(x316, x314);
SymVal x319 = x318 ? Concrete(x317, 32) : x316.add(x314);
SymStack.push(x319);
}
{
Num x320 = Stack.pop();
SymStack.pop();
Num x321 = I32V(Memory.loadInt(x320.toInt(), 0));
SymVal x322 = SymMemory.loadSym(x320.toInt(), 0);
Stack.push(x321);
SymStack.push(x322);
}
{
Num x323 = Stack.pop();
SymVal x324 = SymStack.pop();
Frames.set(6, x323);
SymFrames.set(6, x324);
}
Stack.push(Frames.get(4));
SymStack.push(SymFrames.get(4));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x325 = Stack.pop();
SymStack.pop();
Num x326 = I32V(Memory.loadInt(x325.toInt(), 12));
SymVal x327 = SymMemory.loadSym(x325.toInt(), 12);
Stack.push(x326);
SymStack.push(x327);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x328 = Stack.pop();
SymVal x329 = SymStack.pop();
Num x330 = Stack.pop();
SymVal x331 = SymStack.pop();
Num x332 = x330.i32_shl(x328);
Stack.push(x332);
bool x333 = allConcrete(x331, x329);
SymVal x334 = x333 ? Concrete(x332, 32) : x331.shl(x329);
SymStack.push(x334);
}
{
Num x335 = Stack.pop();
SymVal x336 = SymStack.pop();
Num x337 = Stack.pop();
SymVal x338 = SymStack.pop();
Num x339 = x337.i32_add(x335);
Stack.push(x339);
bool x340 = allConcrete(x338, x336);
SymVal x341 = x340 ? Concrete(x339, 32) : x338.add(x336);
SymStack.push(x341);
}
Stack.push(Frames.get(6));
SymStack.push(SymFrames.get(6));
{
Num x342 = Stack.pop();
SymVal x343 = SymStack.pop();
Num x344 = Stack.pop();
SymStack.pop();
int x345 = x344.toInt();
Memory.storeInt(x345, 0, x342.toInt());
SymMemory.storeSym(x345, 0, x343);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x346 = Stack.pop();
SymStack.pop();
Num x347 = I32V(Memory.loadInt(x346.toInt(), 8));
SymVal x348 = SymMemory.loadSym(x346.toInt(), 8);
Stack.push(x347);
SymStack.push(x348);
}
{
Num x349 = Stack.pop();
SymVal x350 = SymStack.pop();
Frames.set(7, x349);
SymFrames.set(7, x350);
}
Stack.push(Frames.get(4));
SymStack.push(SymFrames.get(4));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x351 = Stack.pop();
SymStack.pop();
Num x352 = I32V(Memory.loadInt(x351.toInt(), 16));
SymVal x353 = SymMemory.loadSym(x351.toInt(), 16);
Stack.push(x352);
SymStack.push(x353);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x354 = Stack.pop();
SymVal x355 = SymStack.pop();
Num x356 = Stack.pop();
SymVal x357 = SymStack.pop();
Num x358 = x356.i32_shl(x354);
Stack.push(x358);
bool x359 = allConcrete(x357, x355);
SymVal x360 = x359 ? Concrete(x358, 32) : x357.shl(x355);
SymStack.push(x360);
}
{
Num x361 = Stack.pop();
SymVal x362 = SymStack.pop();
Num x363 = Stack.pop();
SymVal x364 = SymStack.pop();
Num x365 = x363.i32_add(x361);
Stack.push(x365);
bool x366 = allConcrete(x364, x362);
SymVal x367 = x366 ? Concrete(x365, 32) : x364.add(x362);
SymStack.push(x367);
}
Stack.push(Frames.get(7));
SymStack.push(SymFrames.get(7));
{
Num x368 = Stack.pop();
SymVal x369 = SymStack.pop();
Num x370 = Stack.pop();
SymStack.pop();
int x371 = x370.toInt();
Memory.storeInt(x371, 0, x368.toInt());
SymMemory.storeSym(x371, 0, x369);
}
__attribute__((musttail)) return x233(std::monostate{});
return std::monostate{};
}
std::monostate x233(std::monostate x234) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x235 = Stack.pop();
SymStack.pop();
Num x236 = I32V(Memory.loadInt(x235.toInt(), 12));
SymVal x237 = SymMemory.loadSym(x235.toInt(), 12);
Stack.push(x236);
SymStack.push(x237);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x238 = Stack.pop();
SymVal x239 = SymStack.pop();
Num x240 = Stack.pop();
SymVal x241 = SymStack.pop();
Num x242 = x240.i32_add(x238);
Stack.push(x242);
bool x243 = allConcrete(x241, x239);
SymVal x244 = x243 ? Concrete(x242, 32) : x241.add(x239);
SymStack.push(x244);
}
{
Num x245 = Stack.pop();
SymVal x246 = SymStack.pop();
Num x247 = Stack.pop();
SymStack.pop();
int x248 = x247.toInt();
Memory.storeInt(x248, 12, x245.toInt());
SymMemory.storeSym(x248, 12, x246);
}
info("Jump to 0");
__attribute__((musttail)) return x249(std::monostate{});
return std::monostate{};
}
std::monostate x218(std::monostate x219) {
infoWhen("CALL", "Entered the function at 4, stackSize =", Stack.size());
Frames.pushFrameCallee(1);
SymFrames.pushFrameSlot(32);
Stack.push(Globals.get(0));
SymStack.push(SymGlobals.get(0));
Stack.push(I32V(16));
SymStack.push(Concrete(I32V(16), 32));
{
Num x220 = Stack.pop();
SymVal x221 = SymStack.pop();
Num x222 = Stack.pop();
SymVal x223 = SymStack.pop();
Num x224 = x222.i32_sub(x220);
Stack.push(x224);
bool x225 = allConcrete(x223, x221);
SymVal x226 = x225 ? Concrete(x224, 32) : x223.minus(x221);
SymStack.push(x226);
}
{
Num x227 = Stack.pop();
SymVal x228 = SymStack.pop();
Frames.set(1, x227);
SymFrames.set(1, x228);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x229 = Stack.pop();
SymVal x230 = SymStack.pop();
Num x231 = Stack.pop();
SymStack.pop();
int x232 = x231.toInt();
Memory.storeInt(x232, 12, x229.toInt());
SymMemory.storeSym(x232, 12, x230);
}
return x216(std::monostate{});
}
std::monostate x216(std::monostate x217) {
infoWhen("CALL", "Exiting the function at 4, stackSize =", Stack.size());
Frames.popFrameCallee(1);
SymFrames.popFrameCallee(1);
return enterCC(std::monostate());
}
std::monostate x195(std::monostate x196) {
infoWhen("CALL", "Entered the function at 3, stackSize =", Stack.size());
Frames.pushFrameCallee(5);
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
Num x197 = Stack.pop();
SymVal x198 = SymStack.pop();
Num x199 = Stack.pop();
SymVal x200 = SymStack.pop();
Num x201 = x199.i32_sub(x197);
Stack.push(x201);
bool x202 = allConcrete(x200, x198);
SymVal x203 = x202 ? Concrete(x201, 32) : x200.minus(x198);
SymStack.push(x203);
}
{
Num x204 = Stack.pop();
SymVal x205 = SymStack.pop();
Frames.set(1, x204);
SymFrames.set(1, x205);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x206 = Stack.pop();
SymVal x207 = SymStack.pop();
Globals.set(0, x206);
SymGlobals.set(0, x207);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x208 = Stack.pop();
SymVal x209 = SymStack.pop();
Num x210 = Stack.pop();
SymStack.pop();
int x211 = x210.toInt();
Memory.storeInt(x211, 12, x208.toInt());
SymMemory.storeSym(x211, 12, x209);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x212 = Stack.pop();
SymVal x213 = SymStack.pop();
Num x214 = Stack.pop();
SymStack.pop();
int x215 = x214.toInt();
Memory.storeInt(x215, 8, x212.toInt());
SymMemory.storeSym(x215, 8, x213);
}
__attribute__((musttail)) return x193(std::monostate{});
return std::monostate{};
}
std::monostate x193(std::monostate x194) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x66(std::monostate{});
return std::monostate{};
}
std::monostate x66(std::monostate x167) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x168 = Stack.pop();
SymStack.pop();
Num x169 = I32V(Memory.loadInt(x168.toInt(), 8));
SymVal x170 = SymMemory.loadSym(x168.toInt(), 8);
Stack.push(x169);
SymStack.push(x170);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x171 = Stack.pop();
SymStack.pop();
Num x172 = I32V(Memory.loadInt(x171.toInt(), 12));
SymVal x173 = SymMemory.loadSym(x171.toInt(), 12);
Stack.push(x172);
SymStack.push(x173);
}
{
Num x174 = Stack.pop();
SymVal x175 = SymStack.pop();
Num x176 = Stack.pop();
SymVal x177 = SymStack.pop();
Num x178 = x176.i32_lt_s(x174);
Stack.push(x178);
bool x179 = allConcrete(x177, x175);
SymVal x180 = x179 ? Concrete(x178, 32) : x177.lt(x175).bool2bv();
SymStack.push(x180);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x181 = Stack.pop();
SymVal x182 = SymStack.pop();
Num x183 = Stack.pop();
SymVal x184 = SymStack.pop();
Num x185 = x183.i32_and(x181);
Stack.push(x185);
bool x186 = allConcrete(x184, x182);
SymVal x187 = x186 ? Concrete(x185, 32) : x184.bitwise_and(x182);
SymStack.push(x187);
}
{
Num x188 = Stack.pop();
SymVal x189 = SymStack.pop();
Stack.push(I32V((0 == x188.toInt())));
SymStack.push(x189.is_zero().bool2bv());
}
Num x190 = Stack.pop();
info("The br_if(1)'s condition is ", x190.toInt());
{
SymVal x191 = SymStack.pop();
ExploreTree.fillIfElseNode(x191, 0);
}
int x192 = x190.toInt();
if (x192 != 0) {
info("Jump to 1");
{
ExploreTree.moveCursor(true, makeControl(x13, CURRENT_MCONT));
}
__attribute__((musttail)) return x165(std::monostate{});
} else {
info("Continue rest of the block");
{
ExploreTree.moveCursor(false, makeControl(x165, CURRENT_MCONT));
}
__attribute__((musttail)) return x13(std::monostate{});
}
return std::monostate{};
}
std::monostate x165(std::monostate x166) {
__attribute__((musttail)) return x163(std::monostate{});
return std::monostate{};
}
std::monostate x163(std::monostate x164) {
info("Exiting the block, stackSize =", Stack.size());
__attribute__((musttail)) return x161(std::monostate{});
return std::monostate{};
}
std::monostate x161(std::monostate x162) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x122(std::monostate{});
return std::monostate{};
}
std::monostate x122(std::monostate x138) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x139 = Stack.pop();
SymStack.pop();
Num x140 = I32V(Memory.loadInt(x139.toInt(), 8));
SymVal x141 = SymMemory.loadSym(x139.toInt(), 8);
Stack.push(x140);
SymStack.push(x141);
}
Stack.push(I32V(20));
SymStack.push(Concrete(I32V(20), 32));
{
Num x142 = Stack.pop();
SymVal x143 = SymStack.pop();
Num x144 = Stack.pop();
SymVal x145 = SymStack.pop();
Num x146 = x144.i32_lt_s(x142);
Stack.push(x146);
bool x147 = allConcrete(x145, x143);
SymVal x148 = x147 ? Concrete(x146, 32) : x145.lt(x143).bool2bv();
SymStack.push(x148);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x149 = Stack.pop();
SymVal x150 = SymStack.pop();
Num x151 = Stack.pop();
SymVal x152 = SymStack.pop();
Num x153 = x151.i32_and(x149);
Stack.push(x153);
bool x154 = allConcrete(x152, x150);
SymVal x155 = x154 ? Concrete(x153, 32) : x152.bitwise_and(x150);
SymStack.push(x155);
}
{
Num x156 = Stack.pop();
SymVal x157 = SymStack.pop();
Stack.push(I32V((0 == x156.toInt())));
SymStack.push(x157.is_zero().bool2bv());
}
Num x158 = Stack.pop();
info("The br_if(1)'s condition is ", x158.toInt());
{
SymVal x159 = SymStack.pop();
ExploreTree.fillIfElseNode(x159, 0);
}
int x160 = x158.toInt();
if (x160 != 0) {
info("Jump to 1");
{
ExploreTree.moveCursor(true, makeControl(x67, CURRENT_MCONT));
}
__attribute__((musttail)) return x136(std::monostate{});
} else {
info("Continue rest of the block");
{
ExploreTree.moveCursor(false, makeControl(x136, CURRENT_MCONT));
}
__attribute__((musttail)) return x67(std::monostate{});
}
return std::monostate{};
}
std::monostate x136(std::monostate x137) {
__attribute__((musttail)) return x125(std::monostate{});
return std::monostate{};
}
std::monostate x125(std::monostate x126) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(16));
SymStack.push(Concrete(I32V(16), 32));
{
Num x127 = Stack.pop();
SymVal x128 = SymStack.pop();
Num x129 = Stack.pop();
SymVal x130 = SymStack.pop();
Num x131 = x129.i32_add(x127);
Stack.push(x131);
bool x132 = allConcrete(x130, x128);
SymVal x133 = x132 ? Concrete(x131, 32) : x130.add(x128);
SymStack.push(x133);
}
{
Num x134 = Stack.pop();
SymVal x135 = SymStack.pop();
Globals.set(0, x134);
SymGlobals.set(0, x135);
}
return x123(std::monostate{});
}
std::monostate x123(std::monostate x124) {
infoWhen("CALL", "Exiting the function at 3, stackSize =", Stack.size());
Frames.popFrameCallee(5);
SymFrames.popFrameCallee(5);
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
Stack.push(I32V(20));
SymStack.push(Concrete(I32V(20), 32));
Stack.push(Frames.get(4));
SymStack.push(SymFrames.get(4));
{
Num x74 = Stack.pop();
SymVal x75 = SymStack.pop();
Num x76 = Stack.pop();
SymVal x77 = SymStack.pop();
Num x78 = x76.i32_sub(x74);
Stack.push(x78);
bool x79 = allConcrete(x77, x75);
SymVal x80 = x79 ? Concrete(x78, 32) : x77.minus(x75);
SymStack.push(x80);
}
{
Num x81 = Stack.pop();
SymVal x82 = SymStack.pop();
Frames.set(5, x81);
SymFrames.set(5, x82);
}
Stack.push(Frames.get(4));
SymStack.push(SymFrames.get(4));
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x83 = Stack.pop();
SymVal x84 = SymStack.pop();
Num x85 = Stack.pop();
SymVal x86 = SymStack.pop();
Num x87 = x85.i32_shl(x83);
Stack.push(x87);
bool x88 = allConcrete(x86, x84);
SymVal x89 = x88 ? Concrete(x87, 32) : x86.shl(x84);
SymStack.push(x89);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
Stack.push(Globals.get(1));
SymStack.push(SymGlobals.get(1));
{
Num x90 = Stack.pop();
SymVal x91 = SymStack.pop();
Num x92 = Stack.pop();
SymVal x93 = SymStack.pop();
Num x94 = x92.i32_add(x90);
Stack.push(x94);
bool x95 = allConcrete(x93, x91);
SymVal x96 = x95 ? Concrete(x94, 32) : x93.add(x91);
SymStack.push(x96);
}
{
Num x97 = Stack.pop();
SymVal x98 = SymStack.pop();
Num x99 = Stack.pop();
SymVal x100 = SymStack.pop();
Num x101 = x99.i32_add(x97);
Stack.push(x101);
bool x102 = allConcrete(x100, x98);
SymVal x103 = x102 ? Concrete(x101, 32) : x100.add(x98);
SymStack.push(x103);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x104 = Stack.pop();
SymVal x105 = SymStack.pop();
Num x106 = Stack.pop();
SymStack.pop();
int x107 = x106.toInt();
Memory.storeInt(x107, 0, x104.toInt());
SymMemory.storeSym(x107, 0, x105);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x108 = Stack.pop();
SymStack.pop();
Num x109 = I32V(Memory.loadInt(x108.toInt(), 8));
SymVal x110 = SymMemory.loadSym(x108.toInt(), 8);
Stack.push(x109);
SymStack.push(x110);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x111 = Stack.pop();
SymVal x112 = SymStack.pop();
Num x113 = Stack.pop();
SymVal x114 = SymStack.pop();
Num x115 = x113.i32_add(x111);
Stack.push(x115);
bool x116 = allConcrete(x114, x112);
SymVal x117 = x116 ? Concrete(x115, 32) : x114.add(x112);
SymStack.push(x117);
}
{
Num x118 = Stack.pop();
SymVal x119 = SymStack.pop();
Num x120 = Stack.pop();
SymStack.pop();
int x121 = x120.toInt();
Memory.storeInt(x121, 8, x118.toInt());
SymMemory.storeSym(x121, 8, x119);
}
info("Jump to 0");
__attribute__((musttail)) return x122(std::monostate{});
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
Globals.set(0, I32V(66640));
SymGlobals.set(0, Concrete(I32V(66640), 32));
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
updateCurrentMCont(prependCont(x676, CURRENT_MCONT));
}
__attribute__((musttail)) return x657(std::monostate{});
return std::monostate{};
}

/*****************************************
End of Generated Code
*******************************************/
int main(int argc, char *argv[]) {
  start_concolic_execution_with(Snippet, 2);
  return 0;
}
