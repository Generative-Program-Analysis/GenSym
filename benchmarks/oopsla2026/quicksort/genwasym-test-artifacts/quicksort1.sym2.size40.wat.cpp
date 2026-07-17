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
std::monostate x233(std::monostate);
std::monostate x355(std::monostate);
std::monostate x357(std::monostate);
std::monostate x408(std::monostate);
std::monostate x410(std::monostate);
std::monostate x412(std::monostate);
std::monostate x423(std::monostate);
std::monostate x425(std::monostate);
std::monostate x445(std::monostate);
std::monostate x464(std::monostate);
std::monostate x232(std::monostate);
std::monostate x492(std::monostate);
std::monostate x494(std::monostate);
std::monostate x548(std::monostate);
std::monostate x550(std::monostate);
std::monostate x444(std::monostate);
std::monostate x597(std::monostate);
std::monostate x599(std::monostate);
std::monostate x601(std::monostate);
std::monostate x607(std::monostate);
std::monostate x611(std::monostate);
std::monostate x613(std::monostate);
std::monostate x626(std::monostate);
std::monostate x643(std::monostate);
std::monostate x645(std::monostate);

/************* Functions **************/
std::monostate x645(std::monostate x646) {
infoWhen("CALL", "Returning from the function at 5, stackSize =", Stack.size());
Frames.popFrameCaller(0);
SymFrames.popFrameCaller(0);
return x643(std::monostate{});
}
std::monostate x643(std::monostate x644) {
return enterCC(std::monostate());
}
std::monostate x626(std::monostate x627) {
infoWhen("CALL", "Entered the function at 5, stackSize =", Stack.size());
Frames.pushFrameCallee(2);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Stack.push(Globals.get(0));
SymStack.push(SymGlobals.get(0));
Stack.push(I32V(16));
SymStack.push(Concrete(I32V(16), 32));
{
Num x628 = Stack.pop();
SymVal x629 = SymStack.pop();
Num x630 = Stack.pop();
SymVal x631 = SymStack.pop();
Num x632 = x630.i32_sub(x628);
Stack.push(x632);
bool x633 = allConcrete(x631, x629);
SymVal x634 = x633 ? Concrete(x632, 32) : x631.minus(x629);
SymStack.push(x634);
}
{
Num x635 = Stack.pop();
SymVal x636 = SymStack.pop();
Frames.set(0, x635);
SymFrames.set(0, x636);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x637 = Stack.pop();
SymVal x638 = SymStack.pop();
Globals.set(0, x637);
SymGlobals.set(0, x638);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x639 = Stack.pop();
SymVal x640 = SymStack.pop();
Num x641 = Stack.pop();
SymStack.pop();
int x642 = x641.toInt();
Memory.storeInt(x642, 12, x639.toInt());
SymMemory.storeSym(x642, 12, x640);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 2);
Frames.pushFrameCaller(0);
SymFrames.pushFramePtr();
updateCurrentMCont(prependCont(x613, CURRENT_MCONT));
}
__attribute__((musttail)) return x607(std::monostate{});
return std::monostate{};
}
std::monostate x613(std::monostate x614) {
infoWhen("CALL", "Returning from the function at 2, stackSize =", Stack.size());
Frames.popFrameCaller(0);
SymFrames.popFrameCaller(0);
{
Num x615 = Stack.pop();
SymVal x616 = SymStack.pop();
Frames.set(1, x615);
SymFrames.set(1, x616);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(16));
SymStack.push(Concrete(I32V(16), 32));
{
Num x617 = Stack.pop();
SymVal x618 = SymStack.pop();
Num x619 = Stack.pop();
SymVal x620 = SymStack.pop();
Num x621 = x619.i32_add(x617);
Stack.push(x621);
bool x622 = allConcrete(x620, x618);
SymVal x623 = x622 ? Concrete(x621, 32) : x620.add(x618);
SymStack.push(x623);
}
{
Num x624 = Stack.pop();
SymVal x625 = SymStack.pop();
Globals.set(0, x624);
SymGlobals.set(0, x625);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
return x611(std::monostate{});
}
std::monostate x611(std::monostate x612) {
infoWhen("CALL", "Exiting the function at 5, stackSize =", Stack.size());
Frames.popFrameCallee(2);
SymFrames.popFrameCallee(2);
return enterCC(std::monostate());
}
std::monostate x607(std::monostate x608) {
infoWhen("CALL", "Entered the function at 2, stackSize =", Stack.size());
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 3);
Num x609 = Stack.pop();
SymVal x610 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x609);
SymFrames.set(0, x610);
updateCurrentMCont(prependCont(x601, CURRENT_MCONT));
}
__attribute__((musttail)) return x195(std::monostate{});
return std::monostate{};
}
std::monostate x601(std::monostate x602) {
infoWhen("CALL", "Returning from the function at 3, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
Stack.push(I32V(39));
SymStack.push(Concrete(I32V(39), 32));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 4);
Num x603 = Stack.pop();
Num x604 = Stack.pop();
SymVal x605 = SymStack.pop();
SymVal x606 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x604);
Frames.set(1, x603);
SymFrames.set(0, x606);
SymFrames.set(1, x605);
updateCurrentMCont(prependCont(x599, CURRENT_MCONT));
}
__attribute__((musttail)) return x444(std::monostate{});
return std::monostate{};
}
std::monostate x599(std::monostate x600) {
infoWhen("CALL", "Returning from the function at 4, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
return x597(std::monostate{});
}
std::monostate x597(std::monostate x598) {
infoWhen("CALL", "Exiting the function at 2, stackSize =", Stack.size());
Frames.popFrameCallee(0);
SymFrames.popFrameCallee(0);
return enterCC(std::monostate());
}
std::monostate x444(std::monostate x577) {
infoWhen("CALL", "Entered the function at 4, stackSize =", Stack.size());
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
Num x578 = Stack.pop();
SymVal x579 = SymStack.pop();
Num x580 = Stack.pop();
SymVal x581 = SymStack.pop();
Num x582 = x580.i32_sub(x578);
Stack.push(x582);
bool x583 = allConcrete(x581, x579);
SymVal x584 = x583 ? Concrete(x582, 32) : x581.minus(x579);
SymStack.push(x584);
}
{
Num x585 = Stack.pop();
SymVal x586 = SymStack.pop();
Frames.set(2, x585);
SymFrames.set(2, x586);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x587 = Stack.pop();
SymVal x588 = SymStack.pop();
Globals.set(0, x587);
SymGlobals.set(0, x588);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x589 = Stack.pop();
SymVal x590 = SymStack.pop();
Num x591 = Stack.pop();
SymStack.pop();
int x592 = x591.toInt();
Memory.storeInt(x592, 28, x589.toInt());
SymMemory.storeSym(x592, 28, x590);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x593 = Stack.pop();
SymVal x594 = SymStack.pop();
Num x595 = Stack.pop();
SymStack.pop();
int x596 = x595.toInt();
Memory.storeInt(x596, 24, x593.toInt());
SymMemory.storeSym(x596, 24, x594);
}
__attribute__((musttail)) return x550(std::monostate{});
return std::monostate{};
}
std::monostate x550(std::monostate x551) {
info("Entering the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x552 = Stack.pop();
SymStack.pop();
Num x553 = I32V(Memory.loadInt(x552.toInt(), 28));
SymVal x554 = SymMemory.loadSym(x552.toInt(), 28);
Stack.push(x553);
SymStack.push(x554);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x555 = Stack.pop();
SymStack.pop();
Num x556 = I32V(Memory.loadInt(x555.toInt(), 24));
SymVal x557 = SymMemory.loadSym(x555.toInt(), 24);
Stack.push(x556);
SymStack.push(x557);
}
{
Num x558 = Stack.pop();
SymVal x559 = SymStack.pop();
Num x560 = Stack.pop();
SymVal x561 = SymStack.pop();
Num x562 = x560.i32_lt_s(x558);
Stack.push(x562);
bool x563 = allConcrete(x561, x559);
SymVal x564 = x563 ? Concrete(x562, 32) : x561.lt(x559).bool2bv();
SymStack.push(x564);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x565 = Stack.pop();
SymVal x566 = SymStack.pop();
Num x567 = Stack.pop();
SymVal x568 = SymStack.pop();
Num x569 = x567.i32_and(x565);
Stack.push(x569);
bool x570 = allConcrete(x568, x566);
SymVal x571 = x570 ? Concrete(x569, 32) : x568.bitwise_and(x566);
SymStack.push(x571);
}
{
Num x572 = Stack.pop();
SymVal x573 = SymStack.pop();
Stack.push(I32V((0 == x572.toInt())));
SymStack.push(x573.is_zero().bool2bv());
}
Num x574 = Stack.pop();
info("The br_if(0)'s condition is ", x574.toInt());
{
SymVal x575 = SymStack.pop();
ExploreTree.fillIfElseNode(x575, 1);
}
int x576 = x574.toInt();
if (x576 != 0) {
info("Jump to 0");
{
ExploreTree.moveCursor(true, makeControl(x494, CURRENT_MCONT));
}
__attribute__((musttail)) return x548(std::monostate{});
} else {
info("Continue rest of the block");
{
ExploreTree.moveCursor(false, makeControl(x548, CURRENT_MCONT));
}
__attribute__((musttail)) return x494(std::monostate{});
}
return std::monostate{};
}
std::monostate x548(std::monostate x549) {
__attribute__((musttail)) return x412(std::monostate{});
return std::monostate{};
}
std::monostate x494(std::monostate x495) {
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x496 = Stack.pop();
SymStack.pop();
Num x497 = I32V(Memory.loadInt(x496.toInt(), 24));
SymVal x498 = SymMemory.loadSym(x496.toInt(), 24);
Stack.push(x497);
SymStack.push(x498);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x499 = Stack.pop();
SymVal x500 = SymStack.pop();
Num x501 = Stack.pop();
SymVal x502 = SymStack.pop();
Num x503 = x501.i32_shl(x499);
Stack.push(x503);
bool x504 = allConcrete(x502, x500);
SymVal x505 = x504 ? Concrete(x503, 32) : x502.shl(x500);
SymStack.push(x505);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
Stack.push(Globals.get(1));
SymStack.push(SymGlobals.get(1));
{
Num x506 = Stack.pop();
SymVal x507 = SymStack.pop();
Num x508 = Stack.pop();
SymVal x509 = SymStack.pop();
Num x510 = x508.i32_add(x506);
Stack.push(x510);
bool x511 = allConcrete(x509, x507);
SymVal x512 = x511 ? Concrete(x510, 32) : x509.add(x507);
SymStack.push(x512);
}
{
Num x513 = Stack.pop();
SymVal x514 = SymStack.pop();
Num x515 = Stack.pop();
SymVal x516 = SymStack.pop();
Num x517 = x515.i32_add(x513);
Stack.push(x517);
bool x518 = allConcrete(x516, x514);
SymVal x519 = x518 ? Concrete(x517, 32) : x516.add(x514);
SymStack.push(x519);
}
{
Num x520 = Stack.pop();
SymStack.pop();
Num x521 = I32V(Memory.loadInt(x520.toInt(), 0));
SymVal x522 = SymMemory.loadSym(x520.toInt(), 0);
Stack.push(x521);
SymStack.push(x522);
}
{
Num x523 = Stack.pop();
SymVal x524 = SymStack.pop();
Num x525 = Stack.pop();
SymStack.pop();
int x526 = x525.toInt();
Memory.storeInt(x526, 20, x523.toInt());
SymMemory.storeSym(x526, 20, x524);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x527 = Stack.pop();
SymStack.pop();
Num x528 = I32V(Memory.loadInt(x527.toInt(), 28));
SymVal x529 = SymMemory.loadSym(x527.toInt(), 28);
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
Num x534 = x532.i32_sub(x530);
Stack.push(x534);
bool x535 = allConcrete(x533, x531);
SymVal x536 = x535 ? Concrete(x534, 32) : x533.minus(x531);
SymStack.push(x536);
}
{
Num x537 = Stack.pop();
SymVal x538 = SymStack.pop();
Num x539 = Stack.pop();
SymStack.pop();
int x540 = x539.toInt();
Memory.storeInt(x540, 16, x537.toInt());
SymMemory.storeSym(x540, 16, x538);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x541 = Stack.pop();
SymStack.pop();
Num x542 = I32V(Memory.loadInt(x541.toInt(), 28));
SymVal x543 = SymMemory.loadSym(x541.toInt(), 28);
Stack.push(x542);
SymStack.push(x543);
}
{
Num x544 = Stack.pop();
SymVal x545 = SymStack.pop();
Num x546 = Stack.pop();
SymStack.pop();
int x547 = x546.toInt();
Memory.storeInt(x547, 12, x544.toInt());
SymMemory.storeSym(x547, 12, x545);
}
__attribute__((musttail)) return x492(std::monostate{});
return std::monostate{};
}
std::monostate x492(std::monostate x493) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x232(std::monostate{});
return std::monostate{};
}
std::monostate x232(std::monostate x466) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x467 = Stack.pop();
SymStack.pop();
Num x468 = I32V(Memory.loadInt(x467.toInt(), 12));
SymVal x469 = SymMemory.loadSym(x467.toInt(), 12);
Stack.push(x468);
SymStack.push(x469);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x470 = Stack.pop();
SymStack.pop();
Num x471 = I32V(Memory.loadInt(x470.toInt(), 24));
SymVal x472 = SymMemory.loadSym(x470.toInt(), 24);
Stack.push(x471);
SymStack.push(x472);
}
{
Num x473 = Stack.pop();
SymVal x474 = SymStack.pop();
Num x475 = Stack.pop();
SymVal x476 = SymStack.pop();
Num x477 = x475.i32_le_s(x473);
Stack.push(x477);
bool x478 = allConcrete(x476, x474);
SymVal x479 = x478 ? Concrete(x477, 32) : x476.le(x474).bool2bv();
SymStack.push(x479);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x480 = Stack.pop();
SymVal x481 = SymStack.pop();
Num x482 = Stack.pop();
SymVal x483 = SymStack.pop();
Num x484 = x482.i32_and(x480);
Stack.push(x484);
bool x485 = allConcrete(x483, x481);
SymVal x486 = x485 ? Concrete(x484, 32) : x483.bitwise_and(x481);
SymStack.push(x486);
}
{
Num x487 = Stack.pop();
SymVal x488 = SymStack.pop();
Stack.push(I32V((0 == x487.toInt())));
SymStack.push(x488.is_zero().bool2bv());
}
Num x489 = Stack.pop();
info("The br_if(1)'s condition is ", x489.toInt());
{
SymVal x490 = SymStack.pop();
ExploreTree.fillIfElseNode(x490, 0);
}
int x491 = x489.toInt();
if (x491 != 0) {
info("Jump to 1");
{
ExploreTree.moveCursor(true, makeControl(x408, CURRENT_MCONT));
}
__attribute__((musttail)) return x464(std::monostate{});
} else {
info("Continue rest of the block");
{
ExploreTree.moveCursor(false, makeControl(x464, CURRENT_MCONT));
}
__attribute__((musttail)) return x408(std::monostate{});
}
return std::monostate{};
}
std::monostate x464(std::monostate x465) {
__attribute__((musttail)) return x445(std::monostate{});
return std::monostate{};
}
std::monostate x445(std::monostate x446) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x447 = Stack.pop();
SymStack.pop();
Num x448 = I32V(Memory.loadInt(x447.toInt(), 28));
SymVal x449 = SymMemory.loadSym(x447.toInt(), 28);
Stack.push(x448);
SymStack.push(x449);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x450 = Stack.pop();
SymStack.pop();
Num x451 = I32V(Memory.loadInt(x450.toInt(), 16));
SymVal x452 = SymMemory.loadSym(x450.toInt(), 16);
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
Num x457 = x455.i32_sub(x453);
Stack.push(x457);
bool x458 = allConcrete(x456, x454);
SymVal x459 = x458 ? Concrete(x457, 32) : x456.minus(x454);
SymStack.push(x459);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 4);
Num x460 = Stack.pop();
Num x461 = Stack.pop();
SymVal x462 = SymStack.pop();
SymVal x463 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x461);
Frames.set(1, x460);
SymFrames.set(0, x463);
SymFrames.set(1, x462);
updateCurrentMCont(prependCont(x425, CURRENT_MCONT));
}
__attribute__((musttail)) return x444(std::monostate{});
return std::monostate{};
}
std::monostate x425(std::monostate x426) {
infoWhen("CALL", "Returning from the function at 4, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x427 = Stack.pop();
SymStack.pop();
Num x428 = I32V(Memory.loadInt(x427.toInt(), 16));
SymVal x429 = SymMemory.loadSym(x427.toInt(), 16);
Stack.push(x428);
SymStack.push(x429);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x430 = Stack.pop();
SymVal x431 = SymStack.pop();
Num x432 = Stack.pop();
SymVal x433 = SymStack.pop();
Num x434 = x432.i32_add(x430);
Stack.push(x434);
bool x435 = allConcrete(x433, x431);
SymVal x436 = x435 ? Concrete(x434, 32) : x433.add(x431);
SymStack.push(x436);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x437 = Stack.pop();
SymStack.pop();
Num x438 = I32V(Memory.loadInt(x437.toInt(), 24));
SymVal x439 = SymMemory.loadSym(x437.toInt(), 24);
Stack.push(x438);
SymStack.push(x439);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 4);
Num x440 = Stack.pop();
Num x441 = Stack.pop();
SymVal x442 = SymStack.pop();
SymVal x443 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x441);
Frames.set(1, x440);
SymFrames.set(0, x443);
SymFrames.set(1, x442);
updateCurrentMCont(prependCont(x423, CURRENT_MCONT));
}
__attribute__((musttail)) return x444(std::monostate{});
return std::monostate{};
}
std::monostate x423(std::monostate x424) {
infoWhen("CALL", "Returning from the function at 4, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
__attribute__((musttail)) return x412(std::monostate{});
return std::monostate{};
}
std::monostate x412(std::monostate x413) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(32));
SymStack.push(Concrete(I32V(32), 32));
{
Num x414 = Stack.pop();
SymVal x415 = SymStack.pop();
Num x416 = Stack.pop();
SymVal x417 = SymStack.pop();
Num x418 = x416.i32_add(x414);
Stack.push(x418);
bool x419 = allConcrete(x417, x415);
SymVal x420 = x419 ? Concrete(x418, 32) : x417.add(x415);
SymStack.push(x420);
}
{
Num x421 = Stack.pop();
SymVal x422 = SymStack.pop();
Globals.set(0, x421);
SymGlobals.set(0, x422);
}
return x410(std::monostate{});
}
std::monostate x410(std::monostate x411) {
infoWhen("CALL", "Exiting the function at 4, stackSize =", Stack.size());
Frames.popFrameCallee(6);
SymFrames.popFrameCallee(6);
return enterCC(std::monostate());
}
std::monostate x408(std::monostate x409) {
__attribute__((musttail)) return x357(std::monostate{});
return std::monostate{};
}
std::monostate x357(std::monostate x358) {
info("Entering the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x359 = Stack.pop();
SymStack.pop();
Num x360 = I32V(Memory.loadInt(x359.toInt(), 12));
SymVal x361 = SymMemory.loadSym(x359.toInt(), 12);
Stack.push(x360);
SymStack.push(x361);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x362 = Stack.pop();
SymVal x363 = SymStack.pop();
Num x364 = Stack.pop();
SymVal x365 = SymStack.pop();
Num x366 = x364.i32_shl(x362);
Stack.push(x366);
bool x367 = allConcrete(x365, x363);
SymVal x368 = x367 ? Concrete(x366, 32) : x365.shl(x363);
SymStack.push(x368);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
Stack.push(Globals.get(1));
SymStack.push(SymGlobals.get(1));
{
Num x369 = Stack.pop();
SymVal x370 = SymStack.pop();
Num x371 = Stack.pop();
SymVal x372 = SymStack.pop();
Num x373 = x371.i32_add(x369);
Stack.push(x373);
bool x374 = allConcrete(x372, x370);
SymVal x375 = x374 ? Concrete(x373, 32) : x372.add(x370);
SymStack.push(x375);
}
{
Num x376 = Stack.pop();
SymVal x377 = SymStack.pop();
Num x378 = Stack.pop();
SymVal x379 = SymStack.pop();
Num x380 = x378.i32_add(x376);
Stack.push(x380);
bool x381 = allConcrete(x379, x377);
SymVal x382 = x381 ? Concrete(x380, 32) : x379.add(x377);
SymStack.push(x382);
}
{
Num x383 = Stack.pop();
SymStack.pop();
Num x384 = I32V(Memory.loadInt(x383.toInt(), 0));
SymVal x385 = SymMemory.loadSym(x383.toInt(), 0);
Stack.push(x384);
SymStack.push(x385);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x386 = Stack.pop();
SymStack.pop();
Num x387 = I32V(Memory.loadInt(x386.toInt(), 20));
SymVal x388 = SymMemory.loadSym(x386.toInt(), 20);
Stack.push(x387);
SymStack.push(x388);
}
{
Num x389 = Stack.pop();
SymVal x390 = SymStack.pop();
Num x391 = Stack.pop();
SymVal x392 = SymStack.pop();
Num x393 = x391.i32_le_s(x389);
Stack.push(x393);
bool x394 = allConcrete(x392, x390);
SymVal x395 = x394 ? Concrete(x393, 32) : x392.le(x390).bool2bv();
SymStack.push(x395);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x396 = Stack.pop();
SymVal x397 = SymStack.pop();
Num x398 = Stack.pop();
SymVal x399 = SymStack.pop();
Num x400 = x398.i32_and(x396);
Stack.push(x400);
bool x401 = allConcrete(x399, x397);
SymVal x402 = x401 ? Concrete(x400, 32) : x399.bitwise_and(x397);
SymStack.push(x402);
}
{
Num x403 = Stack.pop();
SymVal x404 = SymStack.pop();
Stack.push(I32V((0 == x403.toInt())));
SymStack.push(x404.is_zero().bool2bv());
}
Num x405 = Stack.pop();
info("The br_if(0)'s condition is ", x405.toInt());
{
SymVal x406 = SymStack.pop();
ExploreTree.fillIfElseNode(x406, 1);
}
int x407 = x405.toInt();
if (x407 != 0) {
info("Jump to 0");
{
ExploreTree.moveCursor(true, makeControl(x233, CURRENT_MCONT));
}
__attribute__((musttail)) return x355(std::monostate{});
} else {
info("Continue rest of the block");
{
ExploreTree.moveCursor(false, makeControl(x355, CURRENT_MCONT));
}
__attribute__((musttail)) return x233(std::monostate{});
}
return std::monostate{};
}
std::monostate x355(std::monostate x356) {
__attribute__((musttail)) return x216(std::monostate{});
return std::monostate{};
}
std::monostate x233(std::monostate x234) {
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x235 = Stack.pop();
SymStack.pop();
Num x236 = I32V(Memory.loadInt(x235.toInt(), 16));
SymVal x237 = SymMemory.loadSym(x235.toInt(), 16);
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
Memory.storeInt(x248, 16, x245.toInt());
SymMemory.storeSym(x248, 16, x246);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x249 = Stack.pop();
SymStack.pop();
Num x250 = I32V(Memory.loadInt(x249.toInt(), 12));
SymVal x251 = SymMemory.loadSym(x249.toInt(), 12);
Stack.push(x250);
SymStack.push(x251);
}
{
Num x252 = Stack.pop();
SymVal x253 = SymStack.pop();
Frames.set(3, x252);
SymFrames.set(3, x253);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
Stack.push(Globals.get(1));
SymStack.push(SymGlobals.get(1));
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
Frames.set(4, x261);
SymFrames.set(4, x262);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x263 = Stack.pop();
SymVal x264 = SymStack.pop();
Frames.set(5, x263);
SymFrames.set(5, x264);
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
Num x265 = Stack.pop();
SymVal x266 = SymStack.pop();
Num x267 = Stack.pop();
SymVal x268 = SymStack.pop();
Num x269 = x267.i32_shl(x265);
Stack.push(x269);
bool x270 = allConcrete(x268, x266);
SymVal x271 = x270 ? Concrete(x269, 32) : x268.shl(x266);
SymStack.push(x271);
}
{
Num x272 = Stack.pop();
SymVal x273 = SymStack.pop();
Num x274 = Stack.pop();
SymVal x275 = SymStack.pop();
Num x276 = x274.i32_add(x272);
Stack.push(x276);
bool x277 = allConcrete(x275, x273);
SymVal x278 = x277 ? Concrete(x276, 32) : x275.add(x273);
SymStack.push(x278);
}
{
Num x279 = Stack.pop();
SymStack.pop();
Num x280 = I32V(Memory.loadInt(x279.toInt(), 0));
SymVal x281 = SymMemory.loadSym(x279.toInt(), 0);
Stack.push(x280);
SymStack.push(x281);
}
{
Num x282 = Stack.pop();
SymVal x283 = SymStack.pop();
Num x284 = Stack.pop();
SymStack.pop();
int x285 = x284.toInt();
Memory.storeInt(x285, 8, x282.toInt());
SymMemory.storeSym(x285, 8, x283);
}
Stack.push(Frames.get(4));
SymStack.push(SymFrames.get(4));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x286 = Stack.pop();
SymStack.pop();
Num x287 = I32V(Memory.loadInt(x286.toInt(), 16));
SymVal x288 = SymMemory.loadSym(x286.toInt(), 16);
Stack.push(x287);
SymStack.push(x288);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x289 = Stack.pop();
SymVal x290 = SymStack.pop();
Num x291 = Stack.pop();
SymVal x292 = SymStack.pop();
Num x293 = x291.i32_shl(x289);
Stack.push(x293);
bool x294 = allConcrete(x292, x290);
SymVal x295 = x294 ? Concrete(x293, 32) : x292.shl(x290);
SymStack.push(x295);
}
{
Num x296 = Stack.pop();
SymVal x297 = SymStack.pop();
Num x298 = Stack.pop();
SymVal x299 = SymStack.pop();
Num x300 = x298.i32_add(x296);
Stack.push(x300);
bool x301 = allConcrete(x299, x297);
SymVal x302 = x301 ? Concrete(x300, 32) : x299.add(x297);
SymStack.push(x302);
}
{
Num x303 = Stack.pop();
SymStack.pop();
Num x304 = I32V(Memory.loadInt(x303.toInt(), 0));
SymVal x305 = SymMemory.loadSym(x303.toInt(), 0);
Stack.push(x304);
SymStack.push(x305);
}
{
Num x306 = Stack.pop();
SymVal x307 = SymStack.pop();
Frames.set(6, x306);
SymFrames.set(6, x307);
}
Stack.push(Frames.get(4));
SymStack.push(SymFrames.get(4));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x308 = Stack.pop();
SymStack.pop();
Num x309 = I32V(Memory.loadInt(x308.toInt(), 12));
SymVal x310 = SymMemory.loadSym(x308.toInt(), 12);
Stack.push(x309);
SymStack.push(x310);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x311 = Stack.pop();
SymVal x312 = SymStack.pop();
Num x313 = Stack.pop();
SymVal x314 = SymStack.pop();
Num x315 = x313.i32_shl(x311);
Stack.push(x315);
bool x316 = allConcrete(x314, x312);
SymVal x317 = x316 ? Concrete(x315, 32) : x314.shl(x312);
SymStack.push(x317);
}
{
Num x318 = Stack.pop();
SymVal x319 = SymStack.pop();
Num x320 = Stack.pop();
SymVal x321 = SymStack.pop();
Num x322 = x320.i32_add(x318);
Stack.push(x322);
bool x323 = allConcrete(x321, x319);
SymVal x324 = x323 ? Concrete(x322, 32) : x321.add(x319);
SymStack.push(x324);
}
Stack.push(Frames.get(6));
SymStack.push(SymFrames.get(6));
{
Num x325 = Stack.pop();
SymVal x326 = SymStack.pop();
Num x327 = Stack.pop();
SymStack.pop();
int x328 = x327.toInt();
Memory.storeInt(x328, 0, x325.toInt());
SymMemory.storeSym(x328, 0, x326);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x329 = Stack.pop();
SymStack.pop();
Num x330 = I32V(Memory.loadInt(x329.toInt(), 8));
SymVal x331 = SymMemory.loadSym(x329.toInt(), 8);
Stack.push(x330);
SymStack.push(x331);
}
{
Num x332 = Stack.pop();
SymVal x333 = SymStack.pop();
Frames.set(7, x332);
SymFrames.set(7, x333);
}
Stack.push(Frames.get(4));
SymStack.push(SymFrames.get(4));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x334 = Stack.pop();
SymStack.pop();
Num x335 = I32V(Memory.loadInt(x334.toInt(), 16));
SymVal x336 = SymMemory.loadSym(x334.toInt(), 16);
Stack.push(x335);
SymStack.push(x336);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x337 = Stack.pop();
SymVal x338 = SymStack.pop();
Num x339 = Stack.pop();
SymVal x340 = SymStack.pop();
Num x341 = x339.i32_shl(x337);
Stack.push(x341);
bool x342 = allConcrete(x340, x338);
SymVal x343 = x342 ? Concrete(x341, 32) : x340.shl(x338);
SymStack.push(x343);
}
{
Num x344 = Stack.pop();
SymVal x345 = SymStack.pop();
Num x346 = Stack.pop();
SymVal x347 = SymStack.pop();
Num x348 = x346.i32_add(x344);
Stack.push(x348);
bool x349 = allConcrete(x347, x345);
SymVal x350 = x349 ? Concrete(x348, 32) : x347.add(x345);
SymStack.push(x350);
}
Stack.push(Frames.get(7));
SymStack.push(SymFrames.get(7));
{
Num x351 = Stack.pop();
SymVal x352 = SymStack.pop();
Num x353 = Stack.pop();
SymStack.pop();
int x354 = x353.toInt();
Memory.storeInt(x354, 0, x351.toInt());
SymMemory.storeSym(x354, 0, x352);
}
__attribute__((musttail)) return x216(std::monostate{});
return std::monostate{};
}
std::monostate x216(std::monostate x217) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x218 = Stack.pop();
SymStack.pop();
Num x219 = I32V(Memory.loadInt(x218.toInt(), 12));
SymVal x220 = SymMemory.loadSym(x218.toInt(), 12);
Stack.push(x219);
SymStack.push(x220);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
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
SymStack.pop();
int x231 = x230.toInt();
Memory.storeInt(x231, 12, x228.toInt());
SymMemory.storeSym(x231, 12, x229);
}
info("Jump to 0");
__attribute__((musttail)) return x232(std::monostate{});
return std::monostate{};
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
Stack.push(I32V(40));
SymStack.push(Concrete(I32V(40), 32));
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
Stack.push(I32V(40));
SymStack.push(Concrete(I32V(40), 32));
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
Globals.set(0, I32V(66720));
SymGlobals.set(0, Concrete(I32V(66720), 32));
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
infoWhen("CALL", "Taking arguments from stack to call function at ", 5);
Frames.pushFrameCaller(0);
SymFrames.pushFramePtr();
updateCurrentMCont(prependCont(x645, CURRENT_MCONT));
}
__attribute__((musttail)) return x626(std::monostate{});
return std::monostate{};
}

/*****************************************
End of Generated Code
*******************************************/
int main(int argc, char *argv[]) {
  start_concolic_execution_with(Snippet, 2);
  return 0;
}
