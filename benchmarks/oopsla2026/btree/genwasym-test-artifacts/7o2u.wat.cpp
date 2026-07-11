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
std::monostate x15(std::monostate);
std::monostate x18(std::monostate);
std::monostate x20(std::monostate);
std::monostate x22(std::monostate);
std::monostate x24(std::monostate);
std::monostate x26(std::monostate);
std::monostate x36(std::monostate);
std::monostate x65(std::monostate);
std::monostate x67(std::monostate);
std::monostate x69(std::monostate);
std::monostate x71(std::monostate);
std::monostate x119(std::monostate);
std::monostate x121(std::monostate);
std::monostate x123(std::monostate);
std::monostate x125(std::monostate);
std::monostate x127(std::monostate);
std::monostate x232(std::monostate);
std::monostate x334(std::monostate);
std::monostate x388(std::monostate);
std::monostate x497(std::monostate);
std::monostate x387(std::monostate);
std::monostate x517(std::monostate);
std::monostate x580(std::monostate);
std::monostate x333(std::monostate);
std::monostate x593(std::monostate);
std::monostate x662(std::monostate);
std::monostate x664(std::monostate);
std::monostate x817(std::monostate);
std::monostate x819(std::monostate);
std::monostate x816(std::monostate);
std::monostate x842(std::monostate);
std::monostate x844(std::monostate);
std::monostate x902(std::monostate);
std::monostate x231(std::monostate);
std::monostate x932(std::monostate);
std::monostate x957(std::monostate);
std::monostate x963(std::monostate);
std::monostate x969(std::monostate);
std::monostate x1058(std::monostate);
std::monostate x1073(std::monostate);
std::monostate x1075(std::monostate);
std::monostate x1077(std::monostate);
std::monostate x1088(std::monostate);
std::monostate x1117(std::monostate);
std::monostate x1123(std::monostate);
std::monostate x1198(std::monostate);
std::monostate x1200(std::monostate);
std::monostate x1202(std::monostate);
std::monostate x1214(std::monostate);
std::monostate x1213(std::monostate);
std::monostate x1254(std::monostate);
std::monostate x1256(std::monostate);
std::monostate x1297(std::monostate);
std::monostate x1299(std::monostate);
std::monostate x1353(std::monostate);
std::monostate x1372(std::monostate);
std::monostate x1374(std::monostate);
std::monostate x1352(std::monostate);
std::monostate x1411(std::monostate);
std::monostate x118(std::monostate);
std::monostate x1439(std::monostate);
std::monostate x1441(std::monostate);
std::monostate x1443(std::monostate);
std::monostate x1445(std::monostate);
std::monostate x1451(std::monostate);
std::monostate x1453(std::monostate);
std::monostate x1455(std::monostate);
std::monostate x1457(std::monostate);
std::monostate x1459(std::monostate);
std::monostate x1461(std::monostate);
std::monostate x1463(std::monostate);
std::monostate x1465(std::monostate);
std::monostate x1467(std::monostate);
std::monostate x1473(std::monostate);
std::monostate x1566(std::monostate);
std::monostate x1581(std::monostate);
std::monostate x1611(std::monostate);
std::monostate x1613(std::monostate);
std::monostate x1615(std::monostate);
std::monostate x1617(std::monostate);
std::monostate x1619(std::monostate);
std::monostate x1667(std::monostate);
std::monostate x1669(std::monostate);
std::monostate x1671(std::monostate);
std::monostate x1673(std::monostate);
std::monostate x1675(std::monostate);
std::monostate x1690(std::monostate);
std::monostate x1692(std::monostate);
std::monostate x1694(std::monostate);
std::monostate x1696(std::monostate);
std::monostate x1743(std::monostate);
std::monostate x1745(std::monostate);
std::monostate x1747(std::monostate);
std::monostate x1749(std::monostate);
std::monostate x1764(std::monostate);
std::monostate x1787(std::monostate);
std::monostate x1816(std::monostate);
std::monostate x1838(std::monostate);
std::monostate x1840(std::monostate);
std::monostate x1842(std::monostate);
std::monostate x1854(std::monostate);
std::monostate x1853(std::monostate);
std::monostate x1666(std::monostate);
std::monostate x1907(std::monostate);
std::monostate x1909(std::monostate);
std::monostate x1914(std::monostate);
std::monostate x1916(std::monostate);
std::monostate x1966(std::monostate);
std::monostate x1984(std::monostate);
std::monostate x1986(std::monostate);
std::monostate x1988(std::monostate);
std::monostate x1990(std::monostate);
std::monostate x1997(std::monostate);
std::monostate x1999(std::monostate);
std::monostate x2053(std::monostate);
std::monostate x2065(std::monostate);
std::monostate x2067(std::monostate);
std::monostate x2069(std::monostate);
std::monostate x2071(std::monostate);
std::monostate x2073(std::monostate);
std::monostate x2075(std::monostate);
std::monostate x2235(std::monostate);
std::monostate x2337(std::monostate);
std::monostate x2391(std::monostate);
std::monostate x2407(std::monostate);
std::monostate x2390(std::monostate);
std::monostate x2430(std::monostate);
std::monostate x2441(std::monostate);
std::monostate x2336(std::monostate);
std::monostate x2457(std::monostate);
std::monostate x2581(std::monostate);
std::monostate x2583(std::monostate);
std::monostate x2791(std::monostate);
std::monostate x2793(std::monostate);
std::monostate x2790(std::monostate);
std::monostate x2816(std::monostate);
std::monostate x2820(std::monostate);
std::monostate x2835(std::monostate);
std::monostate x2234(std::monostate);
std::monostate x2851(std::monostate);
std::monostate x2996(std::monostate);
std::monostate x3029(std::monostate);
std::monostate x3031(std::monostate);
std::monostate x3143(std::monostate);
std::monostate x3245(std::monostate);
std::monostate x3299(std::monostate);
std::monostate x3317(std::monostate);
std::monostate x3298(std::monostate);
std::monostate x3340(std::monostate);
std::monostate x3344(std::monostate);
std::monostate x3244(std::monostate);
std::monostate x3360(std::monostate);
std::monostate x3443(std::monostate);
std::monostate x3445(std::monostate);
std::monostate x3605(std::monostate);
std::monostate x3607(std::monostate);
std::monostate x3604(std::monostate);
std::monostate x3678(std::monostate);
std::monostate x3682(std::monostate);
std::monostate x3697(std::monostate);
std::monostate x3142(std::monostate);
std::monostate x3761(std::monostate);
std::monostate x3803(std::monostate);
std::monostate x3825(std::monostate);
std::monostate x3837(std::monostate);
std::monostate x3839(std::monostate);
std::monostate x3841(std::monostate);
std::monostate x3843(std::monostate);
std::monostate x3897(std::monostate);
std::monostate x3901(std::monostate);
std::monostate x3903(std::monostate);
std::monostate x4094(std::monostate);
std::monostate x4416(std::monostate);
std::monostate x4418(std::monostate);
std::monostate x4520(std::monostate);
std::monostate x4522(std::monostate);
std::monostate x4519(std::monostate);
std::monostate x4535(std::monostate);
std::monostate x4549(std::monostate);
std::monostate x4564(std::monostate);
std::monostate x3896(std::monostate);
std::monostate x4577(std::monostate);
std::monostate x4584(std::monostate);
std::monostate x4657(std::monostate);
std::monostate x4690(std::monostate);
std::monostate x4692(std::monostate);
std::monostate x4694(std::monostate);
std::monostate x4696(std::monostate);
std::monostate x4846(std::monostate);
std::monostate x4960(std::monostate);
std::monostate x4962(std::monostate);
std::monostate x5160(std::monostate);
std::monostate x5162(std::monostate);
std::monostate x5159(std::monostate);
std::monostate x5226(std::monostate);
std::monostate x5230(std::monostate);
std::monostate x5245(std::monostate);
std::monostate x4845(std::monostate);
std::monostate x5316(std::monostate);
std::monostate x5334(std::monostate);
std::monostate x5336(std::monostate);
std::monostate x5479(std::monostate);
std::monostate x5643(std::monostate);
std::monostate x5716(std::monostate);
std::monostate x5740(std::monostate);
std::monostate x5815(std::monostate);
std::monostate x5927(std::monostate);
std::monostate x5981(std::monostate);
std::monostate x6083(std::monostate);
std::monostate x6085(std::monostate);
std::monostate x6087(std::monostate);
std::monostate x6089(std::monostate);
std::monostate x6109(std::monostate);
std::monostate x6082(std::monostate);
std::monostate x6125(std::monostate);
std::monostate x6136(std::monostate);
std::monostate x5980(std::monostate);
std::monostate x6159(std::monostate);
std::monostate x6163(std::monostate);
std::monostate x6165(std::monostate);
std::monostate x6325(std::monostate);
std::monostate x6327(std::monostate);
std::monostate x6324(std::monostate);
std::monostate x6398(std::monostate);
std::monostate x6402(std::monostate);
std::monostate x6489(std::monostate);
std::monostate x5926(std::monostate);
std::monostate x6553(std::monostate);
std::monostate x6621(std::monostate);
std::monostate x6627(std::monostate);
std::monostate x6681(std::monostate);
std::monostate x6756(std::monostate);
std::monostate x6762(std::monostate);
std::monostate x6836(std::monostate);
std::monostate x6904(std::monostate);
std::monostate x6909(std::monostate);
std::monostate x6911(std::monostate);
std::monostate x6937(std::monostate);
std::monostate x6952(std::monostate);
std::monostate x6954(std::monostate);
std::monostate x6966(std::monostate);
std::monostate x6995(std::monostate);
std::monostate x6997(std::monostate);
std::monostate x6965(std::monostate);
std::monostate x7044(std::monostate);
std::monostate x7048(std::monostate);
std::monostate x7060(std::monostate);
std::monostate x7062(std::monostate);
std::monostate x7064(std::monostate);
std::monostate x7118(std::monostate);
std::monostate x7120(std::monostate);
std::monostate x7136(std::monostate);
std::monostate x7117(std::monostate);
std::monostate x7159(std::monostate);
std::monostate x7163(std::monostate);
std::monostate x7192(std::monostate);
std::monostate x7059(std::monostate);
std::monostate x7208(std::monostate);
std::monostate x1996(std::monostate);
std::monostate x7226(std::monostate);
std::monostate x7286(std::monostate);
std::monostate x7296(std::monostate);
std::monostate x7311(std::monostate);
std::monostate x7321(std::monostate);
std::monostate x7336(std::monostate);
std::monostate x7346(std::monostate);
std::monostate x7361(std::monostate);
std::monostate x7371(std::monostate);
std::monostate x7386(std::monostate);
std::monostate x7396(std::monostate);
std::monostate x7411(std::monostate);
std::monostate x7421(std::monostate);
std::monostate x7436(std::monostate);
std::monostate x7446(std::monostate);
std::monostate x7461(std::monostate);
std::monostate x7471(std::monostate);
std::monostate x7544(std::monostate);
std::monostate x7559(std::monostate);
std::monostate x7574(std::monostate);
std::monostate x7589(std::monostate);
std::monostate x7604(std::monostate);
std::monostate x7619(std::monostate);
std::monostate x7634(std::monostate);
std::monostate x7649(std::monostate);
std::monostate x7664(std::monostate);
std::monostate x7674(std::monostate);
std::monostate x7682(std::monostate);
std::monostate x7690(std::monostate);
std::monostate x7698(std::monostate);
std::monostate x7706(std::monostate);
std::monostate x7714(std::monostate);
std::monostate x7722(std::monostate);
std::monostate x7730(std::monostate);
std::monostate x7738(std::monostate);
std::monostate x7744(std::monostate);

/************* Functions **************/
std::monostate x7744(std::monostate x7745) {
infoWhen("CALL", "Returning from the function at 7, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
{
Num x7746 = Stack.pop();
SymVal x7747 = SymStack.pop();
Frames.set(0, x7746);
SymFrames.set(0, x7747);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
{
Stack.pop();
SymVal x7748 = SymStack.pop();
SymVal x7749 = x7748.makeI32Symbol();
Stack.push(SymEnv.read(x7749));
SymStack.push(x7749);
}
Stack.push(I32V(1026));
SymStack.push(Concrete(I32V(1026), 32));
{
Stack.pop();
SymVal x7750 = SymStack.pop();
SymVal x7751 = x7750.makeI32Symbol();
Stack.push(SymEnv.read(x7751));
SymStack.push(x7751);
}
{
Num x7752 = Stack.pop();
SymVal x7753 = SymStack.pop();
Num x7754 = Stack.pop();
SymVal x7755 = SymStack.pop();
Num x7756 = x7754.i32_ne(x7752);
Stack.push(x7756);
bool x7757 = allConcrete(x7755, x7753);
SymVal x7758 = x7757 ? Concrete(x7756, 32) : x7755.neq(x7753).bool2bv();
SymStack.push(x7758);
}
Stack.push(I32V(1028));
SymStack.push(Concrete(I32V(1028), 32));
{
Stack.pop();
SymVal x7759 = SymStack.pop();
SymVal x7760 = x7759.makeI32Symbol();
Stack.push(SymEnv.read(x7760));
SymStack.push(x7760);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
{
Stack.pop();
SymVal x7761 = SymStack.pop();
SymVal x7762 = x7761.makeI32Symbol();
Stack.push(SymEnv.read(x7762));
SymStack.push(x7762);
}
{
Num x7763 = Stack.pop();
SymVal x7764 = SymStack.pop();
Num x7765 = Stack.pop();
SymVal x7766 = SymStack.pop();
Num x7767 = x7765.i32_ne(x7763);
Stack.push(x7767);
bool x7768 = allConcrete(x7766, x7764);
SymVal x7769 = x7768 ? Concrete(x7767, 32) : x7766.neq(x7764).bool2bv();
SymStack.push(x7769);
}
Stack.push(I32V(1028));
SymStack.push(Concrete(I32V(1028), 32));
{
Stack.pop();
SymVal x7770 = SymStack.pop();
SymVal x7771 = x7770.makeI32Symbol();
Stack.push(SymEnv.read(x7771));
SymStack.push(x7771);
}
Stack.push(I32V(1026));
SymStack.push(Concrete(I32V(1026), 32));
{
Stack.pop();
SymVal x7772 = SymStack.pop();
SymVal x7773 = x7772.makeI32Symbol();
Stack.push(SymEnv.read(x7773));
SymStack.push(x7773);
}
{
Num x7774 = Stack.pop();
SymVal x7775 = SymStack.pop();
Num x7776 = Stack.pop();
SymVal x7777 = SymStack.pop();
Num x7778 = x7776.i32_ne(x7774);
Stack.push(x7778);
bool x7779 = allConcrete(x7777, x7775);
SymVal x7780 = x7779 ? Concrete(x7778, 32) : x7777.neq(x7775).bool2bv();
SymStack.push(x7780);
}
Stack.push(I32V(1030));
SymStack.push(Concrete(I32V(1030), 32));
{
Stack.pop();
SymVal x7781 = SymStack.pop();
SymVal x7782 = x7781.makeI32Symbol();
Stack.push(SymEnv.read(x7782));
SymStack.push(x7782);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
{
Stack.pop();
SymVal x7783 = SymStack.pop();
SymVal x7784 = x7783.makeI32Symbol();
Stack.push(SymEnv.read(x7784));
SymStack.push(x7784);
}
{
Num x7785 = Stack.pop();
SymVal x7786 = SymStack.pop();
Num x7787 = Stack.pop();
SymVal x7788 = SymStack.pop();
Num x7789 = x7787.i32_ne(x7785);
Stack.push(x7789);
bool x7790 = allConcrete(x7788, x7786);
SymVal x7791 = x7790 ? Concrete(x7789, 32) : x7788.neq(x7786).bool2bv();
SymStack.push(x7791);
}
Stack.push(I32V(1030));
SymStack.push(Concrete(I32V(1030), 32));
{
Stack.pop();
SymVal x7792 = SymStack.pop();
SymVal x7793 = x7792.makeI32Symbol();
Stack.push(SymEnv.read(x7793));
SymStack.push(x7793);
}
Stack.push(I32V(1026));
SymStack.push(Concrete(I32V(1026), 32));
{
Stack.pop();
SymVal x7794 = SymStack.pop();
SymVal x7795 = x7794.makeI32Symbol();
Stack.push(SymEnv.read(x7795));
SymStack.push(x7795);
}
{
Num x7796 = Stack.pop();
SymVal x7797 = SymStack.pop();
Num x7798 = Stack.pop();
SymVal x7799 = SymStack.pop();
Num x7800 = x7798.i32_ne(x7796);
Stack.push(x7800);
bool x7801 = allConcrete(x7799, x7797);
SymVal x7802 = x7801 ? Concrete(x7800, 32) : x7799.neq(x7797).bool2bv();
SymStack.push(x7802);
}
Stack.push(I32V(1030));
SymStack.push(Concrete(I32V(1030), 32));
{
Stack.pop();
SymVal x7803 = SymStack.pop();
SymVal x7804 = x7803.makeI32Symbol();
Stack.push(SymEnv.read(x7804));
SymStack.push(x7804);
}
Stack.push(I32V(1028));
SymStack.push(Concrete(I32V(1028), 32));
{
Stack.pop();
SymVal x7805 = SymStack.pop();
SymVal x7806 = x7805.makeI32Symbol();
Stack.push(SymEnv.read(x7806));
SymStack.push(x7806);
}
{
Num x7807 = Stack.pop();
SymVal x7808 = SymStack.pop();
Num x7809 = Stack.pop();
SymVal x7810 = SymStack.pop();
Num x7811 = x7809.i32_ne(x7807);
Stack.push(x7811);
bool x7812 = allConcrete(x7810, x7808);
SymVal x7813 = x7812 ? Concrete(x7811, 32) : x7810.neq(x7808).bool2bv();
SymStack.push(x7813);
}
Stack.push(I32V(1032));
SymStack.push(Concrete(I32V(1032), 32));
{
Stack.pop();
SymVal x7814 = SymStack.pop();
SymVal x7815 = x7814.makeI32Symbol();
Stack.push(SymEnv.read(x7815));
SymStack.push(x7815);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
{
Stack.pop();
SymVal x7816 = SymStack.pop();
SymVal x7817 = x7816.makeI32Symbol();
Stack.push(SymEnv.read(x7817));
SymStack.push(x7817);
}
{
Num x7818 = Stack.pop();
SymVal x7819 = SymStack.pop();
Num x7820 = Stack.pop();
SymVal x7821 = SymStack.pop();
Num x7822 = x7820.i32_ne(x7818);
Stack.push(x7822);
bool x7823 = allConcrete(x7821, x7819);
SymVal x7824 = x7823 ? Concrete(x7822, 32) : x7821.neq(x7819).bool2bv();
SymStack.push(x7824);
}
Stack.push(I32V(1032));
SymStack.push(Concrete(I32V(1032), 32));
{
Stack.pop();
SymVal x7825 = SymStack.pop();
SymVal x7826 = x7825.makeI32Symbol();
Stack.push(SymEnv.read(x7826));
SymStack.push(x7826);
}
Stack.push(I32V(1026));
SymStack.push(Concrete(I32V(1026), 32));
{
Stack.pop();
SymVal x7827 = SymStack.pop();
SymVal x7828 = x7827.makeI32Symbol();
Stack.push(SymEnv.read(x7828));
SymStack.push(x7828);
}
{
Num x7829 = Stack.pop();
SymVal x7830 = SymStack.pop();
Num x7831 = Stack.pop();
SymVal x7832 = SymStack.pop();
Num x7833 = x7831.i32_ne(x7829);
Stack.push(x7833);
bool x7834 = allConcrete(x7832, x7830);
SymVal x7835 = x7834 ? Concrete(x7833, 32) : x7832.neq(x7830).bool2bv();
SymStack.push(x7835);
}
Stack.push(I32V(1032));
SymStack.push(Concrete(I32V(1032), 32));
{
Stack.pop();
SymVal x7836 = SymStack.pop();
SymVal x7837 = x7836.makeI32Symbol();
Stack.push(SymEnv.read(x7837));
SymStack.push(x7837);
}
Stack.push(I32V(1028));
SymStack.push(Concrete(I32V(1028), 32));
{
Stack.pop();
SymVal x7838 = SymStack.pop();
SymVal x7839 = x7838.makeI32Symbol();
Stack.push(SymEnv.read(x7839));
SymStack.push(x7839);
}
{
Num x7840 = Stack.pop();
SymVal x7841 = SymStack.pop();
Num x7842 = Stack.pop();
SymVal x7843 = SymStack.pop();
Num x7844 = x7842.i32_ne(x7840);
Stack.push(x7844);
bool x7845 = allConcrete(x7843, x7841);
SymVal x7846 = x7845 ? Concrete(x7844, 32) : x7843.neq(x7841).bool2bv();
SymStack.push(x7846);
}
Stack.push(I32V(1032));
SymStack.push(Concrete(I32V(1032), 32));
{
Stack.pop();
SymVal x7847 = SymStack.pop();
SymVal x7848 = x7847.makeI32Symbol();
Stack.push(SymEnv.read(x7848));
SymStack.push(x7848);
}
Stack.push(I32V(1030));
SymStack.push(Concrete(I32V(1030), 32));
{
Stack.pop();
SymVal x7849 = SymStack.pop();
SymVal x7850 = x7849.makeI32Symbol();
Stack.push(SymEnv.read(x7850));
SymStack.push(x7850);
}
{
Num x7851 = Stack.pop();
SymVal x7852 = SymStack.pop();
Num x7853 = Stack.pop();
SymVal x7854 = SymStack.pop();
Num x7855 = x7853.i32_ne(x7851);
Stack.push(x7855);
bool x7856 = allConcrete(x7854, x7852);
SymVal x7857 = x7856 ? Concrete(x7855, 32) : x7854.neq(x7852).bool2bv();
SymStack.push(x7857);
}
Stack.push(I32V(1034));
SymStack.push(Concrete(I32V(1034), 32));
{
Stack.pop();
SymVal x7858 = SymStack.pop();
SymVal x7859 = x7858.makeI32Symbol();
Stack.push(SymEnv.read(x7859));
SymStack.push(x7859);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
{
Stack.pop();
SymVal x7860 = SymStack.pop();
SymVal x7861 = x7860.makeI32Symbol();
Stack.push(SymEnv.read(x7861));
SymStack.push(x7861);
}
{
Num x7862 = Stack.pop();
SymVal x7863 = SymStack.pop();
Num x7864 = Stack.pop();
SymVal x7865 = SymStack.pop();
Num x7866 = x7864.i32_ne(x7862);
Stack.push(x7866);
bool x7867 = allConcrete(x7865, x7863);
SymVal x7868 = x7867 ? Concrete(x7866, 32) : x7865.neq(x7863).bool2bv();
SymStack.push(x7868);
}
Stack.push(I32V(1034));
SymStack.push(Concrete(I32V(1034), 32));
{
Stack.pop();
SymVal x7869 = SymStack.pop();
SymVal x7870 = x7869.makeI32Symbol();
Stack.push(SymEnv.read(x7870));
SymStack.push(x7870);
}
Stack.push(I32V(1026));
SymStack.push(Concrete(I32V(1026), 32));
{
Stack.pop();
SymVal x7871 = SymStack.pop();
SymVal x7872 = x7871.makeI32Symbol();
Stack.push(SymEnv.read(x7872));
SymStack.push(x7872);
}
{
Num x7873 = Stack.pop();
SymVal x7874 = SymStack.pop();
Num x7875 = Stack.pop();
SymVal x7876 = SymStack.pop();
Num x7877 = x7875.i32_ne(x7873);
Stack.push(x7877);
bool x7878 = allConcrete(x7876, x7874);
SymVal x7879 = x7878 ? Concrete(x7877, 32) : x7876.neq(x7874).bool2bv();
SymStack.push(x7879);
}
Stack.push(I32V(1034));
SymStack.push(Concrete(I32V(1034), 32));
{
Stack.pop();
SymVal x7880 = SymStack.pop();
SymVal x7881 = x7880.makeI32Symbol();
Stack.push(SymEnv.read(x7881));
SymStack.push(x7881);
}
Stack.push(I32V(1028));
SymStack.push(Concrete(I32V(1028), 32));
{
Stack.pop();
SymVal x7882 = SymStack.pop();
SymVal x7883 = x7882.makeI32Symbol();
Stack.push(SymEnv.read(x7883));
SymStack.push(x7883);
}
{
Num x7884 = Stack.pop();
SymVal x7885 = SymStack.pop();
Num x7886 = Stack.pop();
SymVal x7887 = SymStack.pop();
Num x7888 = x7886.i32_ne(x7884);
Stack.push(x7888);
bool x7889 = allConcrete(x7887, x7885);
SymVal x7890 = x7889 ? Concrete(x7888, 32) : x7887.neq(x7885).bool2bv();
SymStack.push(x7890);
}
Stack.push(I32V(1034));
SymStack.push(Concrete(I32V(1034), 32));
{
Stack.pop();
SymVal x7891 = SymStack.pop();
SymVal x7892 = x7891.makeI32Symbol();
Stack.push(SymEnv.read(x7892));
SymStack.push(x7892);
}
Stack.push(I32V(1030));
SymStack.push(Concrete(I32V(1030), 32));
{
Stack.pop();
SymVal x7893 = SymStack.pop();
SymVal x7894 = x7893.makeI32Symbol();
Stack.push(SymEnv.read(x7894));
SymStack.push(x7894);
}
{
Num x7895 = Stack.pop();
SymVal x7896 = SymStack.pop();
Num x7897 = Stack.pop();
SymVal x7898 = SymStack.pop();
Num x7899 = x7897.i32_ne(x7895);
Stack.push(x7899);
bool x7900 = allConcrete(x7898, x7896);
SymVal x7901 = x7900 ? Concrete(x7899, 32) : x7898.neq(x7896).bool2bv();
SymStack.push(x7901);
}
Stack.push(I32V(1034));
SymStack.push(Concrete(I32V(1034), 32));
{
Stack.pop();
SymVal x7902 = SymStack.pop();
SymVal x7903 = x7902.makeI32Symbol();
Stack.push(SymEnv.read(x7903));
SymStack.push(x7903);
}
Stack.push(I32V(1032));
SymStack.push(Concrete(I32V(1032), 32));
{
Stack.pop();
SymVal x7904 = SymStack.pop();
SymVal x7905 = x7904.makeI32Symbol();
Stack.push(SymEnv.read(x7905));
SymStack.push(x7905);
}
{
Num x7906 = Stack.pop();
SymVal x7907 = SymStack.pop();
Num x7908 = Stack.pop();
SymVal x7909 = SymStack.pop();
Num x7910 = x7908.i32_ne(x7906);
Stack.push(x7910);
bool x7911 = allConcrete(x7909, x7907);
SymVal x7912 = x7911 ? Concrete(x7910, 32) : x7909.neq(x7907).bool2bv();
SymStack.push(x7912);
}
Stack.push(I32V(1036));
SymStack.push(Concrete(I32V(1036), 32));
{
Stack.pop();
SymVal x7913 = SymStack.pop();
SymVal x7914 = x7913.makeI32Symbol();
Stack.push(SymEnv.read(x7914));
SymStack.push(x7914);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
{
Stack.pop();
SymVal x7915 = SymStack.pop();
SymVal x7916 = x7915.makeI32Symbol();
Stack.push(SymEnv.read(x7916));
SymStack.push(x7916);
}
{
Num x7917 = Stack.pop();
SymVal x7918 = SymStack.pop();
Num x7919 = Stack.pop();
SymVal x7920 = SymStack.pop();
Num x7921 = x7919.i32_ne(x7917);
Stack.push(x7921);
bool x7922 = allConcrete(x7920, x7918);
SymVal x7923 = x7922 ? Concrete(x7921, 32) : x7920.neq(x7918).bool2bv();
SymStack.push(x7923);
}
Stack.push(I32V(1036));
SymStack.push(Concrete(I32V(1036), 32));
{
Stack.pop();
SymVal x7924 = SymStack.pop();
SymVal x7925 = x7924.makeI32Symbol();
Stack.push(SymEnv.read(x7925));
SymStack.push(x7925);
}
Stack.push(I32V(1026));
SymStack.push(Concrete(I32V(1026), 32));
{
Stack.pop();
SymVal x7926 = SymStack.pop();
SymVal x7927 = x7926.makeI32Symbol();
Stack.push(SymEnv.read(x7927));
SymStack.push(x7927);
}
{
Num x7928 = Stack.pop();
SymVal x7929 = SymStack.pop();
Num x7930 = Stack.pop();
SymVal x7931 = SymStack.pop();
Num x7932 = x7930.i32_ne(x7928);
Stack.push(x7932);
bool x7933 = allConcrete(x7931, x7929);
SymVal x7934 = x7933 ? Concrete(x7932, 32) : x7931.neq(x7929).bool2bv();
SymStack.push(x7934);
}
Stack.push(I32V(1036));
SymStack.push(Concrete(I32V(1036), 32));
{
Stack.pop();
SymVal x7935 = SymStack.pop();
SymVal x7936 = x7935.makeI32Symbol();
Stack.push(SymEnv.read(x7936));
SymStack.push(x7936);
}
Stack.push(I32V(1028));
SymStack.push(Concrete(I32V(1028), 32));
{
Stack.pop();
SymVal x7937 = SymStack.pop();
SymVal x7938 = x7937.makeI32Symbol();
Stack.push(SymEnv.read(x7938));
SymStack.push(x7938);
}
{
Num x7939 = Stack.pop();
SymVal x7940 = SymStack.pop();
Num x7941 = Stack.pop();
SymVal x7942 = SymStack.pop();
Num x7943 = x7941.i32_ne(x7939);
Stack.push(x7943);
bool x7944 = allConcrete(x7942, x7940);
SymVal x7945 = x7944 ? Concrete(x7943, 32) : x7942.neq(x7940).bool2bv();
SymStack.push(x7945);
}
Stack.push(I32V(1036));
SymStack.push(Concrete(I32V(1036), 32));
{
Stack.pop();
SymVal x7946 = SymStack.pop();
SymVal x7947 = x7946.makeI32Symbol();
Stack.push(SymEnv.read(x7947));
SymStack.push(x7947);
}
Stack.push(I32V(1030));
SymStack.push(Concrete(I32V(1030), 32));
{
Stack.pop();
SymVal x7948 = SymStack.pop();
SymVal x7949 = x7948.makeI32Symbol();
Stack.push(SymEnv.read(x7949));
SymStack.push(x7949);
}
{
Num x7950 = Stack.pop();
SymVal x7951 = SymStack.pop();
Num x7952 = Stack.pop();
SymVal x7953 = SymStack.pop();
Num x7954 = x7952.i32_ne(x7950);
Stack.push(x7954);
bool x7955 = allConcrete(x7953, x7951);
SymVal x7956 = x7955 ? Concrete(x7954, 32) : x7953.neq(x7951).bool2bv();
SymStack.push(x7956);
}
Stack.push(I32V(1036));
SymStack.push(Concrete(I32V(1036), 32));
{
Stack.pop();
SymVal x7957 = SymStack.pop();
SymVal x7958 = x7957.makeI32Symbol();
Stack.push(SymEnv.read(x7958));
SymStack.push(x7958);
}
Stack.push(I32V(1032));
SymStack.push(Concrete(I32V(1032), 32));
{
Stack.pop();
SymVal x7959 = SymStack.pop();
SymVal x7960 = x7959.makeI32Symbol();
Stack.push(SymEnv.read(x7960));
SymStack.push(x7960);
}
{
Num x7961 = Stack.pop();
SymVal x7962 = SymStack.pop();
Num x7963 = Stack.pop();
SymVal x7964 = SymStack.pop();
Num x7965 = x7963.i32_ne(x7961);
Stack.push(x7965);
bool x7966 = allConcrete(x7964, x7962);
SymVal x7967 = x7966 ? Concrete(x7965, 32) : x7964.neq(x7962).bool2bv();
SymStack.push(x7967);
}
Stack.push(I32V(1036));
SymStack.push(Concrete(I32V(1036), 32));
{
Stack.pop();
SymVal x7968 = SymStack.pop();
SymVal x7969 = x7968.makeI32Symbol();
Stack.push(SymEnv.read(x7969));
SymStack.push(x7969);
}
Stack.push(I32V(1034));
SymStack.push(Concrete(I32V(1034), 32));
{
Stack.pop();
SymVal x7970 = SymStack.pop();
SymVal x7971 = x7970.makeI32Symbol();
Stack.push(SymEnv.read(x7971));
SymStack.push(x7971);
}
{
Num x7972 = Stack.pop();
SymVal x7973 = SymStack.pop();
Num x7974 = Stack.pop();
SymVal x7975 = SymStack.pop();
Num x7976 = x7974.i32_ne(x7972);
Stack.push(x7976);
bool x7977 = allConcrete(x7975, x7973);
SymVal x7978 = x7977 ? Concrete(x7976, 32) : x7975.neq(x7973).bool2bv();
SymStack.push(x7978);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
{
Stack.pop();
SymVal x7979 = SymStack.pop();
SymVal x7980 = x7979.makeI32Symbol();
Stack.push(SymEnv.read(x7980));
SymStack.push(x7980);
}
Stack.push(I32V(1026));
SymStack.push(Concrete(I32V(1026), 32));
{
Stack.pop();
SymVal x7981 = SymStack.pop();
SymVal x7982 = x7981.makeI32Symbol();
Stack.push(SymEnv.read(x7982));
SymStack.push(x7982);
}
{
Num x7983 = Stack.pop();
SymVal x7984 = SymStack.pop();
Num x7985 = Stack.pop();
SymVal x7986 = SymStack.pop();
Num x7987 = x7985.i32_gt_s(x7983);
Stack.push(x7987);
bool x7988 = allConcrete(x7986, x7984);
SymVal x7989 = x7988 ? Concrete(x7987, 32) : x7986.gt(x7984).bool2bv();
SymStack.push(x7989);
}
Stack.push(I32V(1026));
SymStack.push(Concrete(I32V(1026), 32));
{
Stack.pop();
SymVal x7990 = SymStack.pop();
SymVal x7991 = x7990.makeI32Symbol();
Stack.push(SymEnv.read(x7991));
SymStack.push(x7991);
}
Stack.push(I32V(1028));
SymStack.push(Concrete(I32V(1028), 32));
{
Stack.pop();
SymVal x7992 = SymStack.pop();
SymVal x7993 = x7992.makeI32Symbol();
Stack.push(SymEnv.read(x7993));
SymStack.push(x7993);
}
{
Num x7994 = Stack.pop();
SymVal x7995 = SymStack.pop();
Num x7996 = Stack.pop();
SymVal x7997 = SymStack.pop();
Num x7998 = x7996.i32_gt_s(x7994);
Stack.push(x7998);
bool x7999 = allConcrete(x7997, x7995);
SymVal x8000 = x7999 ? Concrete(x7998, 32) : x7997.gt(x7995).bool2bv();
SymStack.push(x8000);
}
Stack.push(I32V(1028));
SymStack.push(Concrete(I32V(1028), 32));
{
Stack.pop();
SymVal x8001 = SymStack.pop();
SymVal x8002 = x8001.makeI32Symbol();
Stack.push(SymEnv.read(x8002));
SymStack.push(x8002);
}
Stack.push(I32V(1030));
SymStack.push(Concrete(I32V(1030), 32));
{
Stack.pop();
SymVal x8003 = SymStack.pop();
SymVal x8004 = x8003.makeI32Symbol();
Stack.push(SymEnv.read(x8004));
SymStack.push(x8004);
}
{
Num x8005 = Stack.pop();
SymVal x8006 = SymStack.pop();
Num x8007 = Stack.pop();
SymVal x8008 = SymStack.pop();
Num x8009 = x8007.i32_gt_s(x8005);
Stack.push(x8009);
bool x8010 = allConcrete(x8008, x8006);
SymVal x8011 = x8010 ? Concrete(x8009, 32) : x8008.gt(x8006).bool2bv();
SymStack.push(x8011);
}
Stack.push(I32V(1030));
SymStack.push(Concrete(I32V(1030), 32));
{
Stack.pop();
SymVal x8012 = SymStack.pop();
SymVal x8013 = x8012.makeI32Symbol();
Stack.push(SymEnv.read(x8013));
SymStack.push(x8013);
}
Stack.push(I32V(1032));
SymStack.push(Concrete(I32V(1032), 32));
{
Stack.pop();
SymVal x8014 = SymStack.pop();
SymVal x8015 = x8014.makeI32Symbol();
Stack.push(SymEnv.read(x8015));
SymStack.push(x8015);
}
{
Num x8016 = Stack.pop();
SymVal x8017 = SymStack.pop();
Num x8018 = Stack.pop();
SymVal x8019 = SymStack.pop();
Num x8020 = x8018.i32_gt_s(x8016);
Stack.push(x8020);
bool x8021 = allConcrete(x8019, x8017);
SymVal x8022 = x8021 ? Concrete(x8020, 32) : x8019.gt(x8017).bool2bv();
SymStack.push(x8022);
}
Stack.push(I32V(1032));
SymStack.push(Concrete(I32V(1032), 32));
{
Stack.pop();
SymVal x8023 = SymStack.pop();
SymVal x8024 = x8023.makeI32Symbol();
Stack.push(SymEnv.read(x8024));
SymStack.push(x8024);
}
Stack.push(I32V(1034));
SymStack.push(Concrete(I32V(1034), 32));
{
Stack.pop();
SymVal x8025 = SymStack.pop();
SymVal x8026 = x8025.makeI32Symbol();
Stack.push(SymEnv.read(x8026));
SymStack.push(x8026);
}
{
Num x8027 = Stack.pop();
SymVal x8028 = SymStack.pop();
Num x8029 = Stack.pop();
SymVal x8030 = SymStack.pop();
Num x8031 = x8029.i32_gt_s(x8027);
Stack.push(x8031);
bool x8032 = allConcrete(x8030, x8028);
SymVal x8033 = x8032 ? Concrete(x8031, 32) : x8030.gt(x8028).bool2bv();
SymStack.push(x8033);
}
Stack.push(I32V(1034));
SymStack.push(Concrete(I32V(1034), 32));
{
Stack.pop();
SymVal x8034 = SymStack.pop();
SymVal x8035 = x8034.makeI32Symbol();
Stack.push(SymEnv.read(x8035));
SymStack.push(x8035);
}
Stack.push(I32V(1036));
SymStack.push(Concrete(I32V(1036), 32));
{
Stack.pop();
SymVal x8036 = SymStack.pop();
SymVal x8037 = x8036.makeI32Symbol();
Stack.push(SymEnv.read(x8037));
SymStack.push(x8037);
}
{
Num x8038 = Stack.pop();
SymVal x8039 = SymStack.pop();
Num x8040 = Stack.pop();
SymVal x8041 = SymStack.pop();
Num x8042 = x8040.i32_gt_s(x8038);
Stack.push(x8042);
bool x8043 = allConcrete(x8041, x8039);
SymVal x8044 = x8043 ? Concrete(x8042, 32) : x8041.gt(x8039).bool2bv();
SymStack.push(x8044);
}
{
Num x8045 = Stack.pop();
SymVal x8046 = SymStack.pop();
Num x8047 = Stack.pop();
SymVal x8048 = SymStack.pop();
Num x8049 = x8047.i32_and(x8045);
Stack.push(x8049);
bool x8050 = allConcrete(x8048, x8046);
SymVal x8051 = x8050 ? Concrete(x8049, 32) : x8048.bitwise_and(x8046);
SymStack.push(x8051);
}
{
Num x8052 = Stack.pop();
SymVal x8053 = SymStack.pop();
Num x8054 = Stack.pop();
SymVal x8055 = SymStack.pop();
Num x8056 = x8054.i32_and(x8052);
Stack.push(x8056);
bool x8057 = allConcrete(x8055, x8053);
SymVal x8058 = x8057 ? Concrete(x8056, 32) : x8055.bitwise_and(x8053);
SymStack.push(x8058);
}
{
Num x8059 = Stack.pop();
SymVal x8060 = SymStack.pop();
Num x8061 = Stack.pop();
SymVal x8062 = SymStack.pop();
Num x8063 = x8061.i32_and(x8059);
Stack.push(x8063);
bool x8064 = allConcrete(x8062, x8060);
SymVal x8065 = x8064 ? Concrete(x8063, 32) : x8062.bitwise_and(x8060);
SymStack.push(x8065);
}
{
Num x8066 = Stack.pop();
SymVal x8067 = SymStack.pop();
Num x8068 = Stack.pop();
SymVal x8069 = SymStack.pop();
Num x8070 = x8068.i32_and(x8066);
Stack.push(x8070);
bool x8071 = allConcrete(x8069, x8067);
SymVal x8072 = x8071 ? Concrete(x8070, 32) : x8069.bitwise_and(x8067);
SymStack.push(x8072);
}
{
Num x8073 = Stack.pop();
SymVal x8074 = SymStack.pop();
Num x8075 = Stack.pop();
SymVal x8076 = SymStack.pop();
Num x8077 = x8075.i32_and(x8073);
Stack.push(x8077);
bool x8078 = allConcrete(x8076, x8074);
SymVal x8079 = x8078 ? Concrete(x8077, 32) : x8076.bitwise_and(x8074);
SymStack.push(x8079);
}
{
Num x8080 = Stack.pop();
SymVal x8081 = SymStack.pop();
Num x8082 = Stack.pop();
SymVal x8083 = SymStack.pop();
Num x8084 = x8082.i32_and(x8080);
Stack.push(x8084);
bool x8085 = allConcrete(x8083, x8081);
SymVal x8086 = x8085 ? Concrete(x8084, 32) : x8083.bitwise_and(x8081);
SymStack.push(x8086);
}
{
Num x8087 = Stack.pop();
SymVal x8088 = SymStack.pop();
Num x8089 = Stack.pop();
SymVal x8090 = SymStack.pop();
Num x8091 = x8089.i32_and(x8087);
Stack.push(x8091);
bool x8092 = allConcrete(x8090, x8088);
SymVal x8093 = x8092 ? Concrete(x8091, 32) : x8090.bitwise_and(x8088);
SymStack.push(x8093);
}
{
Num x8094 = Stack.pop();
SymVal x8095 = SymStack.pop();
Num x8096 = Stack.pop();
SymVal x8097 = SymStack.pop();
Num x8098 = x8096.i32_and(x8094);
Stack.push(x8098);
bool x8099 = allConcrete(x8097, x8095);
SymVal x8100 = x8099 ? Concrete(x8098, 32) : x8097.bitwise_and(x8095);
SymStack.push(x8100);
}
{
Num x8101 = Stack.pop();
SymVal x8102 = SymStack.pop();
Num x8103 = Stack.pop();
SymVal x8104 = SymStack.pop();
Num x8105 = x8103.i32_and(x8101);
Stack.push(x8105);
bool x8106 = allConcrete(x8104, x8102);
SymVal x8107 = x8106 ? Concrete(x8105, 32) : x8104.bitwise_and(x8102);
SymStack.push(x8107);
}
{
Num x8108 = Stack.pop();
SymVal x8109 = SymStack.pop();
Num x8110 = Stack.pop();
SymVal x8111 = SymStack.pop();
Num x8112 = x8110.i32_and(x8108);
Stack.push(x8112);
bool x8113 = allConcrete(x8111, x8109);
SymVal x8114 = x8113 ? Concrete(x8112, 32) : x8111.bitwise_and(x8109);
SymStack.push(x8114);
}
{
Num x8115 = Stack.pop();
SymVal x8116 = SymStack.pop();
Num x8117 = Stack.pop();
SymVal x8118 = SymStack.pop();
Num x8119 = x8117.i32_and(x8115);
Stack.push(x8119);
bool x8120 = allConcrete(x8118, x8116);
SymVal x8121 = x8120 ? Concrete(x8119, 32) : x8118.bitwise_and(x8116);
SymStack.push(x8121);
}
{
Num x8122 = Stack.pop();
SymVal x8123 = SymStack.pop();
Num x8124 = Stack.pop();
SymVal x8125 = SymStack.pop();
Num x8126 = x8124.i32_and(x8122);
Stack.push(x8126);
bool x8127 = allConcrete(x8125, x8123);
SymVal x8128 = x8127 ? Concrete(x8126, 32) : x8125.bitwise_and(x8123);
SymStack.push(x8128);
}
{
Num x8129 = Stack.pop();
SymVal x8130 = SymStack.pop();
Num x8131 = Stack.pop();
SymVal x8132 = SymStack.pop();
Num x8133 = x8131.i32_and(x8129);
Stack.push(x8133);
bool x8134 = allConcrete(x8132, x8130);
SymVal x8135 = x8134 ? Concrete(x8133, 32) : x8132.bitwise_and(x8130);
SymStack.push(x8135);
}
{
Num x8136 = Stack.pop();
SymVal x8137 = SymStack.pop();
Num x8138 = Stack.pop();
SymVal x8139 = SymStack.pop();
Num x8140 = x8138.i32_and(x8136);
Stack.push(x8140);
bool x8141 = allConcrete(x8139, x8137);
SymVal x8142 = x8141 ? Concrete(x8140, 32) : x8139.bitwise_and(x8137);
SymStack.push(x8142);
}
{
Num x8143 = Stack.pop();
SymVal x8144 = SymStack.pop();
Num x8145 = Stack.pop();
SymVal x8146 = SymStack.pop();
Num x8147 = x8145.i32_and(x8143);
Stack.push(x8147);
bool x8148 = allConcrete(x8146, x8144);
SymVal x8149 = x8148 ? Concrete(x8147, 32) : x8146.bitwise_and(x8144);
SymStack.push(x8149);
}
{
Num x8150 = Stack.pop();
SymVal x8151 = SymStack.pop();
Num x8152 = Stack.pop();
SymVal x8153 = SymStack.pop();
Num x8154 = x8152.i32_and(x8150);
Stack.push(x8154);
bool x8155 = allConcrete(x8153, x8151);
SymVal x8156 = x8155 ? Concrete(x8154, 32) : x8153.bitwise_and(x8151);
SymStack.push(x8156);
}
{
Num x8157 = Stack.pop();
SymVal x8158 = SymStack.pop();
Num x8159 = Stack.pop();
SymVal x8160 = SymStack.pop();
Num x8161 = x8159.i32_and(x8157);
Stack.push(x8161);
bool x8162 = allConcrete(x8160, x8158);
SymVal x8163 = x8162 ? Concrete(x8161, 32) : x8160.bitwise_and(x8158);
SymStack.push(x8163);
}
{
Num x8164 = Stack.pop();
SymVal x8165 = SymStack.pop();
Num x8166 = Stack.pop();
SymVal x8167 = SymStack.pop();
Num x8168 = x8166.i32_and(x8164);
Stack.push(x8168);
bool x8169 = allConcrete(x8167, x8165);
SymVal x8170 = x8169 ? Concrete(x8168, 32) : x8167.bitwise_and(x8165);
SymStack.push(x8170);
}
{
Num x8171 = Stack.pop();
SymVal x8172 = SymStack.pop();
Num x8173 = Stack.pop();
SymVal x8174 = SymStack.pop();
Num x8175 = x8173.i32_and(x8171);
Stack.push(x8175);
bool x8176 = allConcrete(x8174, x8172);
SymVal x8177 = x8176 ? Concrete(x8175, 32) : x8174.bitwise_and(x8172);
SymStack.push(x8177);
}
{
Num x8178 = Stack.pop();
SymVal x8179 = SymStack.pop();
Num x8180 = Stack.pop();
SymVal x8181 = SymStack.pop();
Num x8182 = x8180.i32_and(x8178);
Stack.push(x8182);
bool x8183 = allConcrete(x8181, x8179);
SymVal x8184 = x8183 ? Concrete(x8182, 32) : x8181.bitwise_and(x8179);
SymStack.push(x8184);
}
{
Num x8185 = Stack.pop();
SymVal x8186 = SymStack.pop();
Num x8187 = Stack.pop();
SymVal x8188 = SymStack.pop();
Num x8189 = x8187.i32_and(x8185);
Stack.push(x8189);
bool x8190 = allConcrete(x8188, x8186);
SymVal x8191 = x8190 ? Concrete(x8189, 32) : x8188.bitwise_and(x8186);
SymStack.push(x8191);
}
{
Num x8192 = Stack.pop();
SymVal x8193 = SymStack.pop();
Num x8194 = Stack.pop();
SymVal x8195 = SymStack.pop();
Num x8196 = x8194.i32_and(x8192);
Stack.push(x8196);
bool x8197 = allConcrete(x8195, x8193);
SymVal x8198 = x8197 ? Concrete(x8196, 32) : x8195.bitwise_and(x8193);
SymStack.push(x8198);
}
{
Num x8199 = Stack.pop();
SymVal x8200 = SymStack.pop();
Num x8201 = Stack.pop();
SymVal x8202 = SymStack.pop();
Num x8203 = x8201.i32_and(x8199);
Stack.push(x8203);
bool x8204 = allConcrete(x8202, x8200);
SymVal x8205 = x8204 ? Concrete(x8203, 32) : x8202.bitwise_and(x8200);
SymStack.push(x8205);
}
{
Num x8206 = Stack.pop();
SymVal x8207 = SymStack.pop();
Num x8208 = Stack.pop();
SymVal x8209 = SymStack.pop();
Num x8210 = x8208.i32_and(x8206);
Stack.push(x8210);
bool x8211 = allConcrete(x8209, x8207);
SymVal x8212 = x8211 ? Concrete(x8210, 32) : x8209.bitwise_and(x8207);
SymStack.push(x8212);
}
{
Num x8213 = Stack.pop();
SymVal x8214 = SymStack.pop();
Num x8215 = Stack.pop();
SymVal x8216 = SymStack.pop();
Num x8217 = x8215.i32_and(x8213);
Stack.push(x8217);
bool x8218 = allConcrete(x8216, x8214);
SymVal x8219 = x8218 ? Concrete(x8217, 32) : x8216.bitwise_and(x8214);
SymStack.push(x8219);
}
{
Num x8220 = Stack.pop();
SymVal x8221 = SymStack.pop();
Num x8222 = Stack.pop();
SymVal x8223 = SymStack.pop();
Num x8224 = x8222.i32_and(x8220);
Stack.push(x8224);
bool x8225 = allConcrete(x8223, x8221);
SymVal x8226 = x8225 ? Concrete(x8224, 32) : x8223.bitwise_and(x8221);
SymStack.push(x8226);
}
Stack.push(I32V(1038));
SymStack.push(Concrete(I32V(1038), 32));
{
Stack.pop();
SymVal x8227 = SymStack.pop();
SymVal x8228 = x8227.makeI32Symbol();
Stack.push(SymEnv.read(x8228));
SymStack.push(x8228);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
{
Stack.pop();
SymVal x8229 = SymStack.pop();
SymVal x8230 = x8229.makeI32Symbol();
Stack.push(SymEnv.read(x8230));
SymStack.push(x8230);
}
{
Num x8231 = Stack.pop();
SymVal x8232 = SymStack.pop();
Num x8233 = Stack.pop();
SymVal x8234 = SymStack.pop();
Num x8235 = x8233.i32_ne(x8231);
Stack.push(x8235);
bool x8236 = allConcrete(x8234, x8232);
SymVal x8237 = x8236 ? Concrete(x8235, 32) : x8234.neq(x8232).bool2bv();
SymStack.push(x8237);
}
Stack.push(I32V(1038));
SymStack.push(Concrete(I32V(1038), 32));
{
Stack.pop();
SymVal x8238 = SymStack.pop();
SymVal x8239 = x8238.makeI32Symbol();
Stack.push(SymEnv.read(x8239));
SymStack.push(x8239);
}
Stack.push(I32V(1026));
SymStack.push(Concrete(I32V(1026), 32));
{
Stack.pop();
SymVal x8240 = SymStack.pop();
SymVal x8241 = x8240.makeI32Symbol();
Stack.push(SymEnv.read(x8241));
SymStack.push(x8241);
}
{
Num x8242 = Stack.pop();
SymVal x8243 = SymStack.pop();
Num x8244 = Stack.pop();
SymVal x8245 = SymStack.pop();
Num x8246 = x8244.i32_ne(x8242);
Stack.push(x8246);
bool x8247 = allConcrete(x8245, x8243);
SymVal x8248 = x8247 ? Concrete(x8246, 32) : x8245.neq(x8243).bool2bv();
SymStack.push(x8248);
}
Stack.push(I32V(1038));
SymStack.push(Concrete(I32V(1038), 32));
{
Stack.pop();
SymVal x8249 = SymStack.pop();
SymVal x8250 = x8249.makeI32Symbol();
Stack.push(SymEnv.read(x8250));
SymStack.push(x8250);
}
Stack.push(I32V(1028));
SymStack.push(Concrete(I32V(1028), 32));
{
Stack.pop();
SymVal x8251 = SymStack.pop();
SymVal x8252 = x8251.makeI32Symbol();
Stack.push(SymEnv.read(x8252));
SymStack.push(x8252);
}
{
Num x8253 = Stack.pop();
SymVal x8254 = SymStack.pop();
Num x8255 = Stack.pop();
SymVal x8256 = SymStack.pop();
Num x8257 = x8255.i32_ne(x8253);
Stack.push(x8257);
bool x8258 = allConcrete(x8256, x8254);
SymVal x8259 = x8258 ? Concrete(x8257, 32) : x8256.neq(x8254).bool2bv();
SymStack.push(x8259);
}
Stack.push(I32V(1038));
SymStack.push(Concrete(I32V(1038), 32));
{
Stack.pop();
SymVal x8260 = SymStack.pop();
SymVal x8261 = x8260.makeI32Symbol();
Stack.push(SymEnv.read(x8261));
SymStack.push(x8261);
}
Stack.push(I32V(1030));
SymStack.push(Concrete(I32V(1030), 32));
{
Stack.pop();
SymVal x8262 = SymStack.pop();
SymVal x8263 = x8262.makeI32Symbol();
Stack.push(SymEnv.read(x8263));
SymStack.push(x8263);
}
{
Num x8264 = Stack.pop();
SymVal x8265 = SymStack.pop();
Num x8266 = Stack.pop();
SymVal x8267 = SymStack.pop();
Num x8268 = x8266.i32_ne(x8264);
Stack.push(x8268);
bool x8269 = allConcrete(x8267, x8265);
SymVal x8270 = x8269 ? Concrete(x8268, 32) : x8267.neq(x8265).bool2bv();
SymStack.push(x8270);
}
Stack.push(I32V(1038));
SymStack.push(Concrete(I32V(1038), 32));
{
Stack.pop();
SymVal x8271 = SymStack.pop();
SymVal x8272 = x8271.makeI32Symbol();
Stack.push(SymEnv.read(x8272));
SymStack.push(x8272);
}
Stack.push(I32V(1032));
SymStack.push(Concrete(I32V(1032), 32));
{
Stack.pop();
SymVal x8273 = SymStack.pop();
SymVal x8274 = x8273.makeI32Symbol();
Stack.push(SymEnv.read(x8274));
SymStack.push(x8274);
}
{
Num x8275 = Stack.pop();
SymVal x8276 = SymStack.pop();
Num x8277 = Stack.pop();
SymVal x8278 = SymStack.pop();
Num x8279 = x8277.i32_ne(x8275);
Stack.push(x8279);
bool x8280 = allConcrete(x8278, x8276);
SymVal x8281 = x8280 ? Concrete(x8279, 32) : x8278.neq(x8276).bool2bv();
SymStack.push(x8281);
}
Stack.push(I32V(1038));
SymStack.push(Concrete(I32V(1038), 32));
{
Stack.pop();
SymVal x8282 = SymStack.pop();
SymVal x8283 = x8282.makeI32Symbol();
Stack.push(SymEnv.read(x8283));
SymStack.push(x8283);
}
Stack.push(I32V(1034));
SymStack.push(Concrete(I32V(1034), 32));
{
Stack.pop();
SymVal x8284 = SymStack.pop();
SymVal x8285 = x8284.makeI32Symbol();
Stack.push(SymEnv.read(x8285));
SymStack.push(x8285);
}
{
Num x8286 = Stack.pop();
SymVal x8287 = SymStack.pop();
Num x8288 = Stack.pop();
SymVal x8289 = SymStack.pop();
Num x8290 = x8288.i32_ne(x8286);
Stack.push(x8290);
bool x8291 = allConcrete(x8289, x8287);
SymVal x8292 = x8291 ? Concrete(x8290, 32) : x8289.neq(x8287).bool2bv();
SymStack.push(x8292);
}
Stack.push(I32V(1038));
SymStack.push(Concrete(I32V(1038), 32));
{
Stack.pop();
SymVal x8293 = SymStack.pop();
SymVal x8294 = x8293.makeI32Symbol();
Stack.push(SymEnv.read(x8294));
SymStack.push(x8294);
}
Stack.push(I32V(1036));
SymStack.push(Concrete(I32V(1036), 32));
{
Stack.pop();
SymVal x8295 = SymStack.pop();
SymVal x8296 = x8295.makeI32Symbol();
Stack.push(SymEnv.read(x8296));
SymStack.push(x8296);
}
{
Num x8297 = Stack.pop();
SymVal x8298 = SymStack.pop();
Num x8299 = Stack.pop();
SymVal x8300 = SymStack.pop();
Num x8301 = x8299.i32_ne(x8297);
Stack.push(x8301);
bool x8302 = allConcrete(x8300, x8298);
SymVal x8303 = x8302 ? Concrete(x8301, 32) : x8300.neq(x8298).bool2bv();
SymStack.push(x8303);
}
Stack.push(I32V(1040));
SymStack.push(Concrete(I32V(1040), 32));
{
Stack.pop();
SymVal x8304 = SymStack.pop();
SymVal x8305 = x8304.makeI32Symbol();
Stack.push(SymEnv.read(x8305));
SymStack.push(x8305);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
{
Stack.pop();
SymVal x8306 = SymStack.pop();
SymVal x8307 = x8306.makeI32Symbol();
Stack.push(SymEnv.read(x8307));
SymStack.push(x8307);
}
{
Num x8308 = Stack.pop();
SymVal x8309 = SymStack.pop();
Num x8310 = Stack.pop();
SymVal x8311 = SymStack.pop();
Num x8312 = x8310.i32_ne(x8308);
Stack.push(x8312);
bool x8313 = allConcrete(x8311, x8309);
SymVal x8314 = x8313 ? Concrete(x8312, 32) : x8311.neq(x8309).bool2bv();
SymStack.push(x8314);
}
Stack.push(I32V(1040));
SymStack.push(Concrete(I32V(1040), 32));
{
Stack.pop();
SymVal x8315 = SymStack.pop();
SymVal x8316 = x8315.makeI32Symbol();
Stack.push(SymEnv.read(x8316));
SymStack.push(x8316);
}
Stack.push(I32V(1026));
SymStack.push(Concrete(I32V(1026), 32));
{
Stack.pop();
SymVal x8317 = SymStack.pop();
SymVal x8318 = x8317.makeI32Symbol();
Stack.push(SymEnv.read(x8318));
SymStack.push(x8318);
}
{
Num x8319 = Stack.pop();
SymVal x8320 = SymStack.pop();
Num x8321 = Stack.pop();
SymVal x8322 = SymStack.pop();
Num x8323 = x8321.i32_ne(x8319);
Stack.push(x8323);
bool x8324 = allConcrete(x8322, x8320);
SymVal x8325 = x8324 ? Concrete(x8323, 32) : x8322.neq(x8320).bool2bv();
SymStack.push(x8325);
}
Stack.push(I32V(1040));
SymStack.push(Concrete(I32V(1040), 32));
{
Stack.pop();
SymVal x8326 = SymStack.pop();
SymVal x8327 = x8326.makeI32Symbol();
Stack.push(SymEnv.read(x8327));
SymStack.push(x8327);
}
Stack.push(I32V(1028));
SymStack.push(Concrete(I32V(1028), 32));
{
Stack.pop();
SymVal x8328 = SymStack.pop();
SymVal x8329 = x8328.makeI32Symbol();
Stack.push(SymEnv.read(x8329));
SymStack.push(x8329);
}
{
Num x8330 = Stack.pop();
SymVal x8331 = SymStack.pop();
Num x8332 = Stack.pop();
SymVal x8333 = SymStack.pop();
Num x8334 = x8332.i32_ne(x8330);
Stack.push(x8334);
bool x8335 = allConcrete(x8333, x8331);
SymVal x8336 = x8335 ? Concrete(x8334, 32) : x8333.neq(x8331).bool2bv();
SymStack.push(x8336);
}
Stack.push(I32V(1040));
SymStack.push(Concrete(I32V(1040), 32));
{
Stack.pop();
SymVal x8337 = SymStack.pop();
SymVal x8338 = x8337.makeI32Symbol();
Stack.push(SymEnv.read(x8338));
SymStack.push(x8338);
}
Stack.push(I32V(1030));
SymStack.push(Concrete(I32V(1030), 32));
{
Stack.pop();
SymVal x8339 = SymStack.pop();
SymVal x8340 = x8339.makeI32Symbol();
Stack.push(SymEnv.read(x8340));
SymStack.push(x8340);
}
{
Num x8341 = Stack.pop();
SymVal x8342 = SymStack.pop();
Num x8343 = Stack.pop();
SymVal x8344 = SymStack.pop();
Num x8345 = x8343.i32_ne(x8341);
Stack.push(x8345);
bool x8346 = allConcrete(x8344, x8342);
SymVal x8347 = x8346 ? Concrete(x8345, 32) : x8344.neq(x8342).bool2bv();
SymStack.push(x8347);
}
Stack.push(I32V(1040));
SymStack.push(Concrete(I32V(1040), 32));
{
Stack.pop();
SymVal x8348 = SymStack.pop();
SymVal x8349 = x8348.makeI32Symbol();
Stack.push(SymEnv.read(x8349));
SymStack.push(x8349);
}
Stack.push(I32V(1032));
SymStack.push(Concrete(I32V(1032), 32));
{
Stack.pop();
SymVal x8350 = SymStack.pop();
SymVal x8351 = x8350.makeI32Symbol();
Stack.push(SymEnv.read(x8351));
SymStack.push(x8351);
}
{
Num x8352 = Stack.pop();
SymVal x8353 = SymStack.pop();
Num x8354 = Stack.pop();
SymVal x8355 = SymStack.pop();
Num x8356 = x8354.i32_ne(x8352);
Stack.push(x8356);
bool x8357 = allConcrete(x8355, x8353);
SymVal x8358 = x8357 ? Concrete(x8356, 32) : x8355.neq(x8353).bool2bv();
SymStack.push(x8358);
}
Stack.push(I32V(1040));
SymStack.push(Concrete(I32V(1040), 32));
{
Stack.pop();
SymVal x8359 = SymStack.pop();
SymVal x8360 = x8359.makeI32Symbol();
Stack.push(SymEnv.read(x8360));
SymStack.push(x8360);
}
Stack.push(I32V(1034));
SymStack.push(Concrete(I32V(1034), 32));
{
Stack.pop();
SymVal x8361 = SymStack.pop();
SymVal x8362 = x8361.makeI32Symbol();
Stack.push(SymEnv.read(x8362));
SymStack.push(x8362);
}
{
Num x8363 = Stack.pop();
SymVal x8364 = SymStack.pop();
Num x8365 = Stack.pop();
SymVal x8366 = SymStack.pop();
Num x8367 = x8365.i32_ne(x8363);
Stack.push(x8367);
bool x8368 = allConcrete(x8366, x8364);
SymVal x8369 = x8368 ? Concrete(x8367, 32) : x8366.neq(x8364).bool2bv();
SymStack.push(x8369);
}
Stack.push(I32V(1040));
SymStack.push(Concrete(I32V(1040), 32));
{
Stack.pop();
SymVal x8370 = SymStack.pop();
SymVal x8371 = x8370.makeI32Symbol();
Stack.push(SymEnv.read(x8371));
SymStack.push(x8371);
}
Stack.push(I32V(1036));
SymStack.push(Concrete(I32V(1036), 32));
{
Stack.pop();
SymVal x8372 = SymStack.pop();
SymVal x8373 = x8372.makeI32Symbol();
Stack.push(SymEnv.read(x8373));
SymStack.push(x8373);
}
{
Num x8374 = Stack.pop();
SymVal x8375 = SymStack.pop();
Num x8376 = Stack.pop();
SymVal x8377 = SymStack.pop();
Num x8378 = x8376.i32_ne(x8374);
Stack.push(x8378);
bool x8379 = allConcrete(x8377, x8375);
SymVal x8380 = x8379 ? Concrete(x8378, 32) : x8377.neq(x8375).bool2bv();
SymStack.push(x8380);
}
Stack.push(I32V(1040));
SymStack.push(Concrete(I32V(1040), 32));
{
Stack.pop();
SymVal x8381 = SymStack.pop();
SymVal x8382 = x8381.makeI32Symbol();
Stack.push(SymEnv.read(x8382));
SymStack.push(x8382);
}
Stack.push(I32V(1038));
SymStack.push(Concrete(I32V(1038), 32));
{
Stack.pop();
SymVal x8383 = SymStack.pop();
SymVal x8384 = x8383.makeI32Symbol();
Stack.push(SymEnv.read(x8384));
SymStack.push(x8384);
}
{
Num x8385 = Stack.pop();
SymVal x8386 = SymStack.pop();
Num x8387 = Stack.pop();
SymVal x8388 = SymStack.pop();
Num x8389 = x8387.i32_ne(x8385);
Stack.push(x8389);
bool x8390 = allConcrete(x8388, x8386);
SymVal x8391 = x8390 ? Concrete(x8389, 32) : x8388.neq(x8386).bool2bv();
SymStack.push(x8391);
}
{
Num x8392 = Stack.pop();
SymVal x8393 = SymStack.pop();
Num x8394 = Stack.pop();
SymVal x8395 = SymStack.pop();
Num x8396 = x8394.i32_and(x8392);
Stack.push(x8396);
bool x8397 = allConcrete(x8395, x8393);
SymVal x8398 = x8397 ? Concrete(x8396, 32) : x8395.bitwise_and(x8393);
SymStack.push(x8398);
}
{
Num x8399 = Stack.pop();
SymVal x8400 = SymStack.pop();
Num x8401 = Stack.pop();
SymVal x8402 = SymStack.pop();
Num x8403 = x8401.i32_and(x8399);
Stack.push(x8403);
bool x8404 = allConcrete(x8402, x8400);
SymVal x8405 = x8404 ? Concrete(x8403, 32) : x8402.bitwise_and(x8400);
SymStack.push(x8405);
}
{
Num x8406 = Stack.pop();
SymVal x8407 = SymStack.pop();
Num x8408 = Stack.pop();
SymVal x8409 = SymStack.pop();
Num x8410 = x8408.i32_and(x8406);
Stack.push(x8410);
bool x8411 = allConcrete(x8409, x8407);
SymVal x8412 = x8411 ? Concrete(x8410, 32) : x8409.bitwise_and(x8407);
SymStack.push(x8412);
}
{
Num x8413 = Stack.pop();
SymVal x8414 = SymStack.pop();
Num x8415 = Stack.pop();
SymVal x8416 = SymStack.pop();
Num x8417 = x8415.i32_and(x8413);
Stack.push(x8417);
bool x8418 = allConcrete(x8416, x8414);
SymVal x8419 = x8418 ? Concrete(x8417, 32) : x8416.bitwise_and(x8414);
SymStack.push(x8419);
}
{
Num x8420 = Stack.pop();
SymVal x8421 = SymStack.pop();
Num x8422 = Stack.pop();
SymVal x8423 = SymStack.pop();
Num x8424 = x8422.i32_and(x8420);
Stack.push(x8424);
bool x8425 = allConcrete(x8423, x8421);
SymVal x8426 = x8425 ? Concrete(x8424, 32) : x8423.bitwise_and(x8421);
SymStack.push(x8426);
}
{
Num x8427 = Stack.pop();
SymVal x8428 = SymStack.pop();
Num x8429 = Stack.pop();
SymVal x8430 = SymStack.pop();
Num x8431 = x8429.i32_and(x8427);
Stack.push(x8431);
bool x8432 = allConcrete(x8430, x8428);
SymVal x8433 = x8432 ? Concrete(x8431, 32) : x8430.bitwise_and(x8428);
SymStack.push(x8433);
}
{
Num x8434 = Stack.pop();
SymVal x8435 = SymStack.pop();
Num x8436 = Stack.pop();
SymVal x8437 = SymStack.pop();
Num x8438 = x8436.i32_and(x8434);
Stack.push(x8438);
bool x8439 = allConcrete(x8437, x8435);
SymVal x8440 = x8439 ? Concrete(x8438, 32) : x8437.bitwise_and(x8435);
SymStack.push(x8440);
}
{
Num x8441 = Stack.pop();
SymVal x8442 = SymStack.pop();
Num x8443 = Stack.pop();
SymVal x8444 = SymStack.pop();
Num x8445 = x8443.i32_and(x8441);
Stack.push(x8445);
bool x8446 = allConcrete(x8444, x8442);
SymVal x8447 = x8446 ? Concrete(x8445, 32) : x8444.bitwise_and(x8442);
SymStack.push(x8447);
}
{
Num x8448 = Stack.pop();
SymVal x8449 = SymStack.pop();
Num x8450 = Stack.pop();
SymVal x8451 = SymStack.pop();
Num x8452 = x8450.i32_and(x8448);
Stack.push(x8452);
bool x8453 = allConcrete(x8451, x8449);
SymVal x8454 = x8453 ? Concrete(x8452, 32) : x8451.bitwise_and(x8449);
SymStack.push(x8454);
}
{
Num x8455 = Stack.pop();
SymVal x8456 = SymStack.pop();
Num x8457 = Stack.pop();
SymVal x8458 = SymStack.pop();
Num x8459 = x8457.i32_and(x8455);
Stack.push(x8459);
bool x8460 = allConcrete(x8458, x8456);
SymVal x8461 = x8460 ? Concrete(x8459, 32) : x8458.bitwise_and(x8456);
SymStack.push(x8461);
}
{
Num x8462 = Stack.pop();
SymVal x8463 = SymStack.pop();
Num x8464 = Stack.pop();
SymVal x8465 = SymStack.pop();
Num x8466 = x8464.i32_and(x8462);
Stack.push(x8466);
bool x8467 = allConcrete(x8465, x8463);
SymVal x8468 = x8467 ? Concrete(x8466, 32) : x8465.bitwise_and(x8463);
SymStack.push(x8468);
}
{
Num x8469 = Stack.pop();
SymVal x8470 = SymStack.pop();
Num x8471 = Stack.pop();
SymVal x8472 = SymStack.pop();
Num x8473 = x8471.i32_and(x8469);
Stack.push(x8473);
bool x8474 = allConcrete(x8472, x8470);
SymVal x8475 = x8474 ? Concrete(x8473, 32) : x8472.bitwise_and(x8470);
SymStack.push(x8475);
}
{
Num x8476 = Stack.pop();
SymVal x8477 = SymStack.pop();
Num x8478 = Stack.pop();
SymVal x8479 = SymStack.pop();
Num x8480 = x8478.i32_and(x8476);
Stack.push(x8480);
bool x8481 = allConcrete(x8479, x8477);
SymVal x8482 = x8481 ? Concrete(x8480, 32) : x8479.bitwise_and(x8477);
SymStack.push(x8482);
}
{
Num x8483 = Stack.pop();
SymVal x8484 = SymStack.pop();
Num x8485 = Stack.pop();
SymVal x8486 = SymStack.pop();
Num x8487 = x8485.i32_and(x8483);
Stack.push(x8487);
bool x8488 = allConcrete(x8486, x8484);
SymVal x8489 = x8488 ? Concrete(x8487, 32) : x8486.bitwise_and(x8484);
SymStack.push(x8489);
}
{
Num x8490 = Stack.pop();
SymVal x8491 = SymStack.pop();
Num x8492 = Stack.pop();
SymVal x8493 = SymStack.pop();
Num x8494 = x8492.i32_and(x8490);
Stack.push(x8494);
bool x8495 = allConcrete(x8493, x8491);
SymVal x8496 = x8495 ? Concrete(x8494, 32) : x8493.bitwise_and(x8491);
SymStack.push(x8496);
}
Num x8497 = Stack.pop();
{
SymVal x8498 = SymStack.pop();
ExploreTree.fillIfElseNode(x8498, 1);
}
int x8499 = x8497.toInt();
if (x8499 != 0) {
ExploreTree.moveCursorNoControl(true);
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
{
Stack.pop();
SymVal x8500 = SymStack.pop();
SymVal x8501 = x8500.makeI32Symbol();
Stack.push(SymEnv.read(x8501));
SymStack.push(x8501);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 11);
Num x8502 = Stack.pop();
SymVal x8503 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x8502);
SymFrames.set(0, x8503);
updateCurrentMCont(prependCont(x7730, CURRENT_MCONT));
}
__attribute__((musttail)) return x1581(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x7738, CURRENT_MCONT));
ExploreTree.fillNotToExploredNode();
}
return std::monostate{};
}
std::monostate x7738(std::monostate x7739) {
info("Successfully assumed condition at 1");
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
{
Stack.pop();
SymVal x7740 = SymStack.pop();
SymVal x7741 = x7740.makeI32Symbol();
Stack.push(SymEnv.read(x7741));
SymStack.push(x7741);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 11);
Num x7742 = Stack.pop();
SymVal x7743 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x7742);
SymFrames.set(0, x7743);
updateCurrentMCont(prependCont(x7730, CURRENT_MCONT));
}
__attribute__((musttail)) return x1581(std::monostate{});
return std::monostate{};
}
std::monostate x7730(std::monostate x7731) {
infoWhen("CALL", "Returning from the function at 11, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
{
Num x7732 = Stack.pop();
SymVal x7733 = SymStack.pop();
Frames.set(0, x7732);
SymFrames.set(0, x7733);
}
Stack.push(I32V(1026));
SymStack.push(Concrete(I32V(1026), 32));
{
Stack.pop();
SymVal x7734 = SymStack.pop();
SymVal x7735 = x7734.makeI32Symbol();
Stack.push(SymEnv.read(x7735));
SymStack.push(x7735);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 11);
Num x7736 = Stack.pop();
SymVal x7737 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x7736);
SymFrames.set(0, x7737);
updateCurrentMCont(prependCont(x7722, CURRENT_MCONT));
}
__attribute__((musttail)) return x1581(std::monostate{});
return std::monostate{};
}
std::monostate x7722(std::monostate x7723) {
infoWhen("CALL", "Returning from the function at 11, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
{
Num x7724 = Stack.pop();
SymVal x7725 = SymStack.pop();
Frames.set(0, x7724);
SymFrames.set(0, x7725);
}
Stack.push(I32V(1028));
SymStack.push(Concrete(I32V(1028), 32));
{
Stack.pop();
SymVal x7726 = SymStack.pop();
SymVal x7727 = x7726.makeI32Symbol();
Stack.push(SymEnv.read(x7727));
SymStack.push(x7727);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 11);
Num x7728 = Stack.pop();
SymVal x7729 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x7728);
SymFrames.set(0, x7729);
updateCurrentMCont(prependCont(x7714, CURRENT_MCONT));
}
__attribute__((musttail)) return x1581(std::monostate{});
return std::monostate{};
}
std::monostate x7714(std::monostate x7715) {
infoWhen("CALL", "Returning from the function at 11, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
{
Num x7716 = Stack.pop();
SymVal x7717 = SymStack.pop();
Frames.set(0, x7716);
SymFrames.set(0, x7717);
}
Stack.push(I32V(1030));
SymStack.push(Concrete(I32V(1030), 32));
{
Stack.pop();
SymVal x7718 = SymStack.pop();
SymVal x7719 = x7718.makeI32Symbol();
Stack.push(SymEnv.read(x7719));
SymStack.push(x7719);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 11);
Num x7720 = Stack.pop();
SymVal x7721 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x7720);
SymFrames.set(0, x7721);
updateCurrentMCont(prependCont(x7706, CURRENT_MCONT));
}
__attribute__((musttail)) return x1581(std::monostate{});
return std::monostate{};
}
std::monostate x7706(std::monostate x7707) {
infoWhen("CALL", "Returning from the function at 11, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
{
Num x7708 = Stack.pop();
SymVal x7709 = SymStack.pop();
Frames.set(0, x7708);
SymFrames.set(0, x7709);
}
Stack.push(I32V(1032));
SymStack.push(Concrete(I32V(1032), 32));
{
Stack.pop();
SymVal x7710 = SymStack.pop();
SymVal x7711 = x7710.makeI32Symbol();
Stack.push(SymEnv.read(x7711));
SymStack.push(x7711);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 11);
Num x7712 = Stack.pop();
SymVal x7713 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x7712);
SymFrames.set(0, x7713);
updateCurrentMCont(prependCont(x7698, CURRENT_MCONT));
}
__attribute__((musttail)) return x1581(std::monostate{});
return std::monostate{};
}
std::monostate x7698(std::monostate x7699) {
infoWhen("CALL", "Returning from the function at 11, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
{
Num x7700 = Stack.pop();
SymVal x7701 = SymStack.pop();
Frames.set(0, x7700);
SymFrames.set(0, x7701);
}
Stack.push(I32V(1034));
SymStack.push(Concrete(I32V(1034), 32));
{
Stack.pop();
SymVal x7702 = SymStack.pop();
SymVal x7703 = x7702.makeI32Symbol();
Stack.push(SymEnv.read(x7703));
SymStack.push(x7703);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 11);
Num x7704 = Stack.pop();
SymVal x7705 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x7704);
SymFrames.set(0, x7705);
updateCurrentMCont(prependCont(x7690, CURRENT_MCONT));
}
__attribute__((musttail)) return x1581(std::monostate{});
return std::monostate{};
}
std::monostate x7690(std::monostate x7691) {
infoWhen("CALL", "Returning from the function at 11, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
{
Num x7692 = Stack.pop();
SymVal x7693 = SymStack.pop();
Frames.set(0, x7692);
SymFrames.set(0, x7693);
}
Stack.push(I32V(1036));
SymStack.push(Concrete(I32V(1036), 32));
{
Stack.pop();
SymVal x7694 = SymStack.pop();
SymVal x7695 = x7694.makeI32Symbol();
Stack.push(SymEnv.read(x7695));
SymStack.push(x7695);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 11);
Num x7696 = Stack.pop();
SymVal x7697 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x7696);
SymFrames.set(0, x7697);
updateCurrentMCont(prependCont(x7682, CURRENT_MCONT));
}
__attribute__((musttail)) return x1581(std::monostate{});
return std::monostate{};
}
std::monostate x7682(std::monostate x7683) {
infoWhen("CALL", "Returning from the function at 11, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
{
Num x7684 = Stack.pop();
SymVal x7685 = SymStack.pop();
Frames.set(0, x7684);
SymFrames.set(0, x7685);
}
Stack.push(I32V(1038));
SymStack.push(Concrete(I32V(1038), 32));
{
Stack.pop();
SymVal x7686 = SymStack.pop();
SymVal x7687 = x7686.makeI32Symbol();
Stack.push(SymEnv.read(x7687));
SymStack.push(x7687);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 11);
Num x7688 = Stack.pop();
SymVal x7689 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x7688);
SymFrames.set(0, x7689);
updateCurrentMCont(prependCont(x7674, CURRENT_MCONT));
}
__attribute__((musttail)) return x1581(std::monostate{});
return std::monostate{};
}
std::monostate x7674(std::monostate x7675) {
infoWhen("CALL", "Returning from the function at 11, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
{
Num x7676 = Stack.pop();
SymVal x7677 = SymStack.pop();
Frames.set(0, x7676);
SymFrames.set(0, x7677);
}
Stack.push(I32V(1040));
SymStack.push(Concrete(I32V(1040), 32));
{
Stack.pop();
SymVal x7678 = SymStack.pop();
SymVal x7679 = x7678.makeI32Symbol();
Stack.push(SymEnv.read(x7679));
SymStack.push(x7679);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 11);
Num x7680 = Stack.pop();
SymVal x7681 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x7680);
SymFrames.set(0, x7681);
updateCurrentMCont(prependCont(x7664, CURRENT_MCONT));
}
__attribute__((musttail)) return x1581(std::monostate{});
return std::monostate{};
}
std::monostate x7664(std::monostate x7665) {
infoWhen("CALL", "Returning from the function at 11, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
{
Num x7666 = Stack.pop();
SymVal x7667 = SymStack.pop();
Frames.set(0, x7666);
SymFrames.set(0, x7667);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
{
Stack.pop();
SymVal x7668 = SymStack.pop();
SymVal x7669 = x7668.makeI32Symbol();
Stack.push(SymEnv.read(x7669));
SymStack.push(x7669);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7670 = Stack.pop();
Num x7671 = Stack.pop();
SymVal x7672 = SymStack.pop();
SymVal x7673 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7671);
Frames.set(1, x7670);
SymFrames.set(0, x7673);
SymFrames.set(1, x7672);
updateCurrentMCont(prependCont(x7649, CURRENT_MCONT));
}
__attribute__((musttail)) return x1666(std::monostate{});
return std::monostate{};
}
std::monostate x7649(std::monostate x7650) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7651 = Stack.pop();
SymVal x7652 = SymStack.pop();
Num x7653 = Stack.pop();
SymVal x7654 = SymStack.pop();
Num x7655 = x7653.i32_ne(x7651);
Stack.push(x7655);
bool x7656 = allConcrete(x7654, x7652);
SymVal x7657 = x7656 ? Concrete(x7655, 32) : x7654.neq(x7652).bool2bv();
SymStack.push(x7657);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1026));
SymStack.push(Concrete(I32V(1026), 32));
{
Stack.pop();
SymVal x7658 = SymStack.pop();
SymVal x7659 = x7658.makeI32Symbol();
Stack.push(SymEnv.read(x7659));
SymStack.push(x7659);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7660 = Stack.pop();
Num x7661 = Stack.pop();
SymVal x7662 = SymStack.pop();
SymVal x7663 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7661);
Frames.set(1, x7660);
SymFrames.set(0, x7663);
SymFrames.set(1, x7662);
updateCurrentMCont(prependCont(x7634, CURRENT_MCONT));
}
__attribute__((musttail)) return x1666(std::monostate{});
return std::monostate{};
}
std::monostate x7634(std::monostate x7635) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7636 = Stack.pop();
SymVal x7637 = SymStack.pop();
Num x7638 = Stack.pop();
SymVal x7639 = SymStack.pop();
Num x7640 = x7638.i32_ne(x7636);
Stack.push(x7640);
bool x7641 = allConcrete(x7639, x7637);
SymVal x7642 = x7641 ? Concrete(x7640, 32) : x7639.neq(x7637).bool2bv();
SymStack.push(x7642);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1028));
SymStack.push(Concrete(I32V(1028), 32));
{
Stack.pop();
SymVal x7643 = SymStack.pop();
SymVal x7644 = x7643.makeI32Symbol();
Stack.push(SymEnv.read(x7644));
SymStack.push(x7644);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7645 = Stack.pop();
Num x7646 = Stack.pop();
SymVal x7647 = SymStack.pop();
SymVal x7648 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7646);
Frames.set(1, x7645);
SymFrames.set(0, x7648);
SymFrames.set(1, x7647);
updateCurrentMCont(prependCont(x7619, CURRENT_MCONT));
}
__attribute__((musttail)) return x1666(std::monostate{});
return std::monostate{};
}
std::monostate x7619(std::monostate x7620) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7621 = Stack.pop();
SymVal x7622 = SymStack.pop();
Num x7623 = Stack.pop();
SymVal x7624 = SymStack.pop();
Num x7625 = x7623.i32_ne(x7621);
Stack.push(x7625);
bool x7626 = allConcrete(x7624, x7622);
SymVal x7627 = x7626 ? Concrete(x7625, 32) : x7624.neq(x7622).bool2bv();
SymStack.push(x7627);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1030));
SymStack.push(Concrete(I32V(1030), 32));
{
Stack.pop();
SymVal x7628 = SymStack.pop();
SymVal x7629 = x7628.makeI32Symbol();
Stack.push(SymEnv.read(x7629));
SymStack.push(x7629);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7630 = Stack.pop();
Num x7631 = Stack.pop();
SymVal x7632 = SymStack.pop();
SymVal x7633 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7631);
Frames.set(1, x7630);
SymFrames.set(0, x7633);
SymFrames.set(1, x7632);
updateCurrentMCont(prependCont(x7604, CURRENT_MCONT));
}
__attribute__((musttail)) return x1666(std::monostate{});
return std::monostate{};
}
std::monostate x7604(std::monostate x7605) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7606 = Stack.pop();
SymVal x7607 = SymStack.pop();
Num x7608 = Stack.pop();
SymVal x7609 = SymStack.pop();
Num x7610 = x7608.i32_ne(x7606);
Stack.push(x7610);
bool x7611 = allConcrete(x7609, x7607);
SymVal x7612 = x7611 ? Concrete(x7610, 32) : x7609.neq(x7607).bool2bv();
SymStack.push(x7612);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1032));
SymStack.push(Concrete(I32V(1032), 32));
{
Stack.pop();
SymVal x7613 = SymStack.pop();
SymVal x7614 = x7613.makeI32Symbol();
Stack.push(SymEnv.read(x7614));
SymStack.push(x7614);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7615 = Stack.pop();
Num x7616 = Stack.pop();
SymVal x7617 = SymStack.pop();
SymVal x7618 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7616);
Frames.set(1, x7615);
SymFrames.set(0, x7618);
SymFrames.set(1, x7617);
updateCurrentMCont(prependCont(x7589, CURRENT_MCONT));
}
__attribute__((musttail)) return x1666(std::monostate{});
return std::monostate{};
}
std::monostate x7589(std::monostate x7590) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7591 = Stack.pop();
SymVal x7592 = SymStack.pop();
Num x7593 = Stack.pop();
SymVal x7594 = SymStack.pop();
Num x7595 = x7593.i32_ne(x7591);
Stack.push(x7595);
bool x7596 = allConcrete(x7594, x7592);
SymVal x7597 = x7596 ? Concrete(x7595, 32) : x7594.neq(x7592).bool2bv();
SymStack.push(x7597);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1034));
SymStack.push(Concrete(I32V(1034), 32));
{
Stack.pop();
SymVal x7598 = SymStack.pop();
SymVal x7599 = x7598.makeI32Symbol();
Stack.push(SymEnv.read(x7599));
SymStack.push(x7599);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7600 = Stack.pop();
Num x7601 = Stack.pop();
SymVal x7602 = SymStack.pop();
SymVal x7603 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7601);
Frames.set(1, x7600);
SymFrames.set(0, x7603);
SymFrames.set(1, x7602);
updateCurrentMCont(prependCont(x7574, CURRENT_MCONT));
}
__attribute__((musttail)) return x1666(std::monostate{});
return std::monostate{};
}
std::monostate x7574(std::monostate x7575) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7576 = Stack.pop();
SymVal x7577 = SymStack.pop();
Num x7578 = Stack.pop();
SymVal x7579 = SymStack.pop();
Num x7580 = x7578.i32_ne(x7576);
Stack.push(x7580);
bool x7581 = allConcrete(x7579, x7577);
SymVal x7582 = x7581 ? Concrete(x7580, 32) : x7579.neq(x7577).bool2bv();
SymStack.push(x7582);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1036));
SymStack.push(Concrete(I32V(1036), 32));
{
Stack.pop();
SymVal x7583 = SymStack.pop();
SymVal x7584 = x7583.makeI32Symbol();
Stack.push(SymEnv.read(x7584));
SymStack.push(x7584);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7585 = Stack.pop();
Num x7586 = Stack.pop();
SymVal x7587 = SymStack.pop();
SymVal x7588 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7586);
Frames.set(1, x7585);
SymFrames.set(0, x7588);
SymFrames.set(1, x7587);
updateCurrentMCont(prependCont(x7559, CURRENT_MCONT));
}
__attribute__((musttail)) return x1666(std::monostate{});
return std::monostate{};
}
std::monostate x7559(std::monostate x7560) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7561 = Stack.pop();
SymVal x7562 = SymStack.pop();
Num x7563 = Stack.pop();
SymVal x7564 = SymStack.pop();
Num x7565 = x7563.i32_ne(x7561);
Stack.push(x7565);
bool x7566 = allConcrete(x7564, x7562);
SymVal x7567 = x7566 ? Concrete(x7565, 32) : x7564.neq(x7562).bool2bv();
SymStack.push(x7567);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1038));
SymStack.push(Concrete(I32V(1038), 32));
{
Stack.pop();
SymVal x7568 = SymStack.pop();
SymVal x7569 = x7568.makeI32Symbol();
Stack.push(SymEnv.read(x7569));
SymStack.push(x7569);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7570 = Stack.pop();
Num x7571 = Stack.pop();
SymVal x7572 = SymStack.pop();
SymVal x7573 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7571);
Frames.set(1, x7570);
SymFrames.set(0, x7573);
SymFrames.set(1, x7572);
updateCurrentMCont(prependCont(x7544, CURRENT_MCONT));
}
__attribute__((musttail)) return x1666(std::monostate{});
return std::monostate{};
}
std::monostate x7544(std::monostate x7545) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7546 = Stack.pop();
SymVal x7547 = SymStack.pop();
Num x7548 = Stack.pop();
SymVal x7549 = SymStack.pop();
Num x7550 = x7548.i32_ne(x7546);
Stack.push(x7550);
bool x7551 = allConcrete(x7549, x7547);
SymVal x7552 = x7551 ? Concrete(x7550, 32) : x7549.neq(x7547).bool2bv();
SymStack.push(x7552);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1040));
SymStack.push(Concrete(I32V(1040), 32));
{
Stack.pop();
SymVal x7553 = SymStack.pop();
SymVal x7554 = x7553.makeI32Symbol();
Stack.push(SymEnv.read(x7554));
SymStack.push(x7554);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7555 = Stack.pop();
Num x7556 = Stack.pop();
SymVal x7557 = SymStack.pop();
SymVal x7558 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7556);
Frames.set(1, x7555);
SymFrames.set(0, x7558);
SymFrames.set(1, x7557);
updateCurrentMCont(prependCont(x7471, CURRENT_MCONT));
}
__attribute__((musttail)) return x1666(std::monostate{});
return std::monostate{};
}
std::monostate x7471(std::monostate x7472) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7473 = Stack.pop();
SymVal x7474 = SymStack.pop();
Num x7475 = Stack.pop();
SymVal x7476 = SymStack.pop();
Num x7477 = x7475.i32_ne(x7473);
Stack.push(x7477);
bool x7478 = allConcrete(x7476, x7474);
SymVal x7479 = x7478 ? Concrete(x7477, 32) : x7476.neq(x7474).bool2bv();
SymStack.push(x7479);
}
{
Num x7480 = Stack.pop();
SymVal x7481 = SymStack.pop();
Num x7482 = Stack.pop();
SymVal x7483 = SymStack.pop();
Num x7484 = x7482.i32_and(x7480);
Stack.push(x7484);
bool x7485 = allConcrete(x7483, x7481);
SymVal x7486 = x7485 ? Concrete(x7484, 32) : x7483.bitwise_and(x7481);
SymStack.push(x7486);
}
{
Num x7487 = Stack.pop();
SymVal x7488 = SymStack.pop();
Num x7489 = Stack.pop();
SymVal x7490 = SymStack.pop();
Num x7491 = x7489.i32_and(x7487);
Stack.push(x7491);
bool x7492 = allConcrete(x7490, x7488);
SymVal x7493 = x7492 ? Concrete(x7491, 32) : x7490.bitwise_and(x7488);
SymStack.push(x7493);
}
{
Num x7494 = Stack.pop();
SymVal x7495 = SymStack.pop();
Num x7496 = Stack.pop();
SymVal x7497 = SymStack.pop();
Num x7498 = x7496.i32_and(x7494);
Stack.push(x7498);
bool x7499 = allConcrete(x7497, x7495);
SymVal x7500 = x7499 ? Concrete(x7498, 32) : x7497.bitwise_and(x7495);
SymStack.push(x7500);
}
{
Num x7501 = Stack.pop();
SymVal x7502 = SymStack.pop();
Num x7503 = Stack.pop();
SymVal x7504 = SymStack.pop();
Num x7505 = x7503.i32_and(x7501);
Stack.push(x7505);
bool x7506 = allConcrete(x7504, x7502);
SymVal x7507 = x7506 ? Concrete(x7505, 32) : x7504.bitwise_and(x7502);
SymStack.push(x7507);
}
{
Num x7508 = Stack.pop();
SymVal x7509 = SymStack.pop();
Num x7510 = Stack.pop();
SymVal x7511 = SymStack.pop();
Num x7512 = x7510.i32_and(x7508);
Stack.push(x7512);
bool x7513 = allConcrete(x7511, x7509);
SymVal x7514 = x7513 ? Concrete(x7512, 32) : x7511.bitwise_and(x7509);
SymStack.push(x7514);
}
{
Num x7515 = Stack.pop();
SymVal x7516 = SymStack.pop();
Num x7517 = Stack.pop();
SymVal x7518 = SymStack.pop();
Num x7519 = x7517.i32_and(x7515);
Stack.push(x7519);
bool x7520 = allConcrete(x7518, x7516);
SymVal x7521 = x7520 ? Concrete(x7519, 32) : x7518.bitwise_and(x7516);
SymStack.push(x7521);
}
{
Num x7522 = Stack.pop();
SymVal x7523 = SymStack.pop();
Num x7524 = Stack.pop();
SymVal x7525 = SymStack.pop();
Num x7526 = x7524.i32_and(x7522);
Stack.push(x7526);
bool x7527 = allConcrete(x7525, x7523);
SymVal x7528 = x7527 ? Concrete(x7526, 32) : x7525.bitwise_and(x7523);
SymStack.push(x7528);
}
{
Num x7529 = Stack.pop();
SymVal x7530 = SymStack.pop();
Num x7531 = Stack.pop();
SymVal x7532 = SymStack.pop();
Num x7533 = x7531.i32_and(x7529);
Stack.push(x7533);
bool x7534 = allConcrete(x7532, x7530);
SymVal x7535 = x7534 ? Concrete(x7533, 32) : x7532.bitwise_and(x7530);
SymStack.push(x7535);
}
{
Num x7536 = Stack.pop();
SymVal x7537 = SymStack.pop();
GENSYM_SYM_ASSERT(x7537);
GENSYM_ASSERT(x7536.toInt() != 0);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
{
Stack.pop();
SymVal x7538 = SymStack.pop();
SymVal x7539 = x7538.makeI32Symbol();
Stack.push(SymEnv.read(x7539));
SymStack.push(x7539);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 12);
Num x7540 = Stack.pop();
Num x7541 = Stack.pop();
SymVal x7542 = SymStack.pop();
SymVal x7543 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7541);
Frames.set(1, x7540);
SymFrames.set(0, x7543);
SymFrames.set(1, x7542);
updateCurrentMCont(prependCont(x7461, CURRENT_MCONT));
}
__attribute__((musttail)) return x1996(std::monostate{});
return std::monostate{};
}
std::monostate x7461(std::monostate x7462) {
infoWhen("CALL", "Returning from the function at 12, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
{
Num x7463 = Stack.pop();
SymVal x7464 = SymStack.pop();
Frames.set(0, x7463);
SymFrames.set(0, x7464);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
{
Stack.pop();
SymVal x7465 = SymStack.pop();
SymVal x7466 = x7465.makeI32Symbol();
Stack.push(SymEnv.read(x7466));
SymStack.push(x7466);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7467 = Stack.pop();
Num x7468 = Stack.pop();
SymVal x7469 = SymStack.pop();
SymVal x7470 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7468);
Frames.set(1, x7467);
SymFrames.set(0, x7470);
SymFrames.set(1, x7469);
updateCurrentMCont(prependCont(x7446, CURRENT_MCONT));
}
__attribute__((musttail)) return x1666(std::monostate{});
return std::monostate{};
}
std::monostate x7446(std::monostate x7447) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7448 = Stack.pop();
SymVal x7449 = SymStack.pop();
Num x7450 = Stack.pop();
SymVal x7451 = SymStack.pop();
Num x7452 = x7450.i32_eq(x7448);
Stack.push(x7452);
bool x7453 = allConcrete(x7451, x7449);
SymVal x7454 = x7453 ? Concrete(x7452, 32) : x7451.eq(x7449).bool2bv();
SymStack.push(x7454);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1026));
SymStack.push(Concrete(I32V(1026), 32));
{
Stack.pop();
SymVal x7455 = SymStack.pop();
SymVal x7456 = x7455.makeI32Symbol();
Stack.push(SymEnv.read(x7456));
SymStack.push(x7456);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 12);
Num x7457 = Stack.pop();
Num x7458 = Stack.pop();
SymVal x7459 = SymStack.pop();
SymVal x7460 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7458);
Frames.set(1, x7457);
SymFrames.set(0, x7460);
SymFrames.set(1, x7459);
updateCurrentMCont(prependCont(x7436, CURRENT_MCONT));
}
__attribute__((musttail)) return x1996(std::monostate{});
return std::monostate{};
}
std::monostate x7436(std::monostate x7437) {
infoWhen("CALL", "Returning from the function at 12, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
{
Num x7438 = Stack.pop();
SymVal x7439 = SymStack.pop();
Frames.set(0, x7438);
SymFrames.set(0, x7439);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1026));
SymStack.push(Concrete(I32V(1026), 32));
{
Stack.pop();
SymVal x7440 = SymStack.pop();
SymVal x7441 = x7440.makeI32Symbol();
Stack.push(SymEnv.read(x7441));
SymStack.push(x7441);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7442 = Stack.pop();
Num x7443 = Stack.pop();
SymVal x7444 = SymStack.pop();
SymVal x7445 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7443);
Frames.set(1, x7442);
SymFrames.set(0, x7445);
SymFrames.set(1, x7444);
updateCurrentMCont(prependCont(x7421, CURRENT_MCONT));
}
__attribute__((musttail)) return x1666(std::monostate{});
return std::monostate{};
}
std::monostate x7421(std::monostate x7422) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7423 = Stack.pop();
SymVal x7424 = SymStack.pop();
Num x7425 = Stack.pop();
SymVal x7426 = SymStack.pop();
Num x7427 = x7425.i32_eq(x7423);
Stack.push(x7427);
bool x7428 = allConcrete(x7426, x7424);
SymVal x7429 = x7428 ? Concrete(x7427, 32) : x7426.eq(x7424).bool2bv();
SymStack.push(x7429);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1028));
SymStack.push(Concrete(I32V(1028), 32));
{
Stack.pop();
SymVal x7430 = SymStack.pop();
SymVal x7431 = x7430.makeI32Symbol();
Stack.push(SymEnv.read(x7431));
SymStack.push(x7431);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 12);
Num x7432 = Stack.pop();
Num x7433 = Stack.pop();
SymVal x7434 = SymStack.pop();
SymVal x7435 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7433);
Frames.set(1, x7432);
SymFrames.set(0, x7435);
SymFrames.set(1, x7434);
updateCurrentMCont(prependCont(x7411, CURRENT_MCONT));
}
__attribute__((musttail)) return x1996(std::monostate{});
return std::monostate{};
}
std::monostate x7411(std::monostate x7412) {
infoWhen("CALL", "Returning from the function at 12, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
{
Num x7413 = Stack.pop();
SymVal x7414 = SymStack.pop();
Frames.set(0, x7413);
SymFrames.set(0, x7414);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1028));
SymStack.push(Concrete(I32V(1028), 32));
{
Stack.pop();
SymVal x7415 = SymStack.pop();
SymVal x7416 = x7415.makeI32Symbol();
Stack.push(SymEnv.read(x7416));
SymStack.push(x7416);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7417 = Stack.pop();
Num x7418 = Stack.pop();
SymVal x7419 = SymStack.pop();
SymVal x7420 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7418);
Frames.set(1, x7417);
SymFrames.set(0, x7420);
SymFrames.set(1, x7419);
updateCurrentMCont(prependCont(x7396, CURRENT_MCONT));
}
__attribute__((musttail)) return x1666(std::monostate{});
return std::monostate{};
}
std::monostate x7396(std::monostate x7397) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7398 = Stack.pop();
SymVal x7399 = SymStack.pop();
Num x7400 = Stack.pop();
SymVal x7401 = SymStack.pop();
Num x7402 = x7400.i32_eq(x7398);
Stack.push(x7402);
bool x7403 = allConcrete(x7401, x7399);
SymVal x7404 = x7403 ? Concrete(x7402, 32) : x7401.eq(x7399).bool2bv();
SymStack.push(x7404);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1030));
SymStack.push(Concrete(I32V(1030), 32));
{
Stack.pop();
SymVal x7405 = SymStack.pop();
SymVal x7406 = x7405.makeI32Symbol();
Stack.push(SymEnv.read(x7406));
SymStack.push(x7406);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 12);
Num x7407 = Stack.pop();
Num x7408 = Stack.pop();
SymVal x7409 = SymStack.pop();
SymVal x7410 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7408);
Frames.set(1, x7407);
SymFrames.set(0, x7410);
SymFrames.set(1, x7409);
updateCurrentMCont(prependCont(x7386, CURRENT_MCONT));
}
__attribute__((musttail)) return x1996(std::monostate{});
return std::monostate{};
}
std::monostate x7386(std::monostate x7387) {
infoWhen("CALL", "Returning from the function at 12, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
{
Num x7388 = Stack.pop();
SymVal x7389 = SymStack.pop();
Frames.set(0, x7388);
SymFrames.set(0, x7389);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1030));
SymStack.push(Concrete(I32V(1030), 32));
{
Stack.pop();
SymVal x7390 = SymStack.pop();
SymVal x7391 = x7390.makeI32Symbol();
Stack.push(SymEnv.read(x7391));
SymStack.push(x7391);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7392 = Stack.pop();
Num x7393 = Stack.pop();
SymVal x7394 = SymStack.pop();
SymVal x7395 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7393);
Frames.set(1, x7392);
SymFrames.set(0, x7395);
SymFrames.set(1, x7394);
updateCurrentMCont(prependCont(x7371, CURRENT_MCONT));
}
__attribute__((musttail)) return x1666(std::monostate{});
return std::monostate{};
}
std::monostate x7371(std::monostate x7372) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7373 = Stack.pop();
SymVal x7374 = SymStack.pop();
Num x7375 = Stack.pop();
SymVal x7376 = SymStack.pop();
Num x7377 = x7375.i32_eq(x7373);
Stack.push(x7377);
bool x7378 = allConcrete(x7376, x7374);
SymVal x7379 = x7378 ? Concrete(x7377, 32) : x7376.eq(x7374).bool2bv();
SymStack.push(x7379);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1032));
SymStack.push(Concrete(I32V(1032), 32));
{
Stack.pop();
SymVal x7380 = SymStack.pop();
SymVal x7381 = x7380.makeI32Symbol();
Stack.push(SymEnv.read(x7381));
SymStack.push(x7381);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 12);
Num x7382 = Stack.pop();
Num x7383 = Stack.pop();
SymVal x7384 = SymStack.pop();
SymVal x7385 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7383);
Frames.set(1, x7382);
SymFrames.set(0, x7385);
SymFrames.set(1, x7384);
updateCurrentMCont(prependCont(x7361, CURRENT_MCONT));
}
__attribute__((musttail)) return x1996(std::monostate{});
return std::monostate{};
}
std::monostate x7361(std::monostate x7362) {
infoWhen("CALL", "Returning from the function at 12, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
{
Num x7363 = Stack.pop();
SymVal x7364 = SymStack.pop();
Frames.set(0, x7363);
SymFrames.set(0, x7364);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1032));
SymStack.push(Concrete(I32V(1032), 32));
{
Stack.pop();
SymVal x7365 = SymStack.pop();
SymVal x7366 = x7365.makeI32Symbol();
Stack.push(SymEnv.read(x7366));
SymStack.push(x7366);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7367 = Stack.pop();
Num x7368 = Stack.pop();
SymVal x7369 = SymStack.pop();
SymVal x7370 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7368);
Frames.set(1, x7367);
SymFrames.set(0, x7370);
SymFrames.set(1, x7369);
updateCurrentMCont(prependCont(x7346, CURRENT_MCONT));
}
__attribute__((musttail)) return x1666(std::monostate{});
return std::monostate{};
}
std::monostate x7346(std::monostate x7347) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7348 = Stack.pop();
SymVal x7349 = SymStack.pop();
Num x7350 = Stack.pop();
SymVal x7351 = SymStack.pop();
Num x7352 = x7350.i32_eq(x7348);
Stack.push(x7352);
bool x7353 = allConcrete(x7351, x7349);
SymVal x7354 = x7353 ? Concrete(x7352, 32) : x7351.eq(x7349).bool2bv();
SymStack.push(x7354);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1034));
SymStack.push(Concrete(I32V(1034), 32));
{
Stack.pop();
SymVal x7355 = SymStack.pop();
SymVal x7356 = x7355.makeI32Symbol();
Stack.push(SymEnv.read(x7356));
SymStack.push(x7356);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 12);
Num x7357 = Stack.pop();
Num x7358 = Stack.pop();
SymVal x7359 = SymStack.pop();
SymVal x7360 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7358);
Frames.set(1, x7357);
SymFrames.set(0, x7360);
SymFrames.set(1, x7359);
updateCurrentMCont(prependCont(x7336, CURRENT_MCONT));
}
__attribute__((musttail)) return x1996(std::monostate{});
return std::monostate{};
}
std::monostate x7336(std::monostate x7337) {
infoWhen("CALL", "Returning from the function at 12, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
{
Num x7338 = Stack.pop();
SymVal x7339 = SymStack.pop();
Frames.set(0, x7338);
SymFrames.set(0, x7339);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1034));
SymStack.push(Concrete(I32V(1034), 32));
{
Stack.pop();
SymVal x7340 = SymStack.pop();
SymVal x7341 = x7340.makeI32Symbol();
Stack.push(SymEnv.read(x7341));
SymStack.push(x7341);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7342 = Stack.pop();
Num x7343 = Stack.pop();
SymVal x7344 = SymStack.pop();
SymVal x7345 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7343);
Frames.set(1, x7342);
SymFrames.set(0, x7345);
SymFrames.set(1, x7344);
updateCurrentMCont(prependCont(x7321, CURRENT_MCONT));
}
__attribute__((musttail)) return x1666(std::monostate{});
return std::monostate{};
}
std::monostate x7321(std::monostate x7322) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7323 = Stack.pop();
SymVal x7324 = SymStack.pop();
Num x7325 = Stack.pop();
SymVal x7326 = SymStack.pop();
Num x7327 = x7325.i32_eq(x7323);
Stack.push(x7327);
bool x7328 = allConcrete(x7326, x7324);
SymVal x7329 = x7328 ? Concrete(x7327, 32) : x7326.eq(x7324).bool2bv();
SymStack.push(x7329);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1036));
SymStack.push(Concrete(I32V(1036), 32));
{
Stack.pop();
SymVal x7330 = SymStack.pop();
SymVal x7331 = x7330.makeI32Symbol();
Stack.push(SymEnv.read(x7331));
SymStack.push(x7331);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 12);
Num x7332 = Stack.pop();
Num x7333 = Stack.pop();
SymVal x7334 = SymStack.pop();
SymVal x7335 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7333);
Frames.set(1, x7332);
SymFrames.set(0, x7335);
SymFrames.set(1, x7334);
updateCurrentMCont(prependCont(x7311, CURRENT_MCONT));
}
__attribute__((musttail)) return x1996(std::monostate{});
return std::monostate{};
}
std::monostate x7311(std::monostate x7312) {
infoWhen("CALL", "Returning from the function at 12, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
{
Num x7313 = Stack.pop();
SymVal x7314 = SymStack.pop();
Frames.set(0, x7313);
SymFrames.set(0, x7314);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1036));
SymStack.push(Concrete(I32V(1036), 32));
{
Stack.pop();
SymVal x7315 = SymStack.pop();
SymVal x7316 = x7315.makeI32Symbol();
Stack.push(SymEnv.read(x7316));
SymStack.push(x7316);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7317 = Stack.pop();
Num x7318 = Stack.pop();
SymVal x7319 = SymStack.pop();
SymVal x7320 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7318);
Frames.set(1, x7317);
SymFrames.set(0, x7320);
SymFrames.set(1, x7319);
updateCurrentMCont(prependCont(x7296, CURRENT_MCONT));
}
__attribute__((musttail)) return x1666(std::monostate{});
return std::monostate{};
}
std::monostate x7296(std::monostate x7297) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7298 = Stack.pop();
SymVal x7299 = SymStack.pop();
Num x7300 = Stack.pop();
SymVal x7301 = SymStack.pop();
Num x7302 = x7300.i32_eq(x7298);
Stack.push(x7302);
bool x7303 = allConcrete(x7301, x7299);
SymVal x7304 = x7303 ? Concrete(x7302, 32) : x7301.eq(x7299).bool2bv();
SymStack.push(x7304);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1038));
SymStack.push(Concrete(I32V(1038), 32));
{
Stack.pop();
SymVal x7305 = SymStack.pop();
SymVal x7306 = x7305.makeI32Symbol();
Stack.push(SymEnv.read(x7306));
SymStack.push(x7306);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 12);
Num x7307 = Stack.pop();
Num x7308 = Stack.pop();
SymVal x7309 = SymStack.pop();
SymVal x7310 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7308);
Frames.set(1, x7307);
SymFrames.set(0, x7310);
SymFrames.set(1, x7309);
updateCurrentMCont(prependCont(x7286, CURRENT_MCONT));
}
__attribute__((musttail)) return x1996(std::monostate{});
return std::monostate{};
}
std::monostate x7286(std::monostate x7287) {
infoWhen("CALL", "Returning from the function at 12, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
{
Num x7288 = Stack.pop();
SymVal x7289 = SymStack.pop();
Frames.set(0, x7288);
SymFrames.set(0, x7289);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1038));
SymStack.push(Concrete(I32V(1038), 32));
{
Stack.pop();
SymVal x7290 = SymStack.pop();
SymVal x7291 = x7290.makeI32Symbol();
Stack.push(SymEnv.read(x7291));
SymStack.push(x7291);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7292 = Stack.pop();
Num x7293 = Stack.pop();
SymVal x7294 = SymStack.pop();
SymVal x7295 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7293);
Frames.set(1, x7292);
SymFrames.set(0, x7295);
SymFrames.set(1, x7294);
updateCurrentMCont(prependCont(x7226, CURRENT_MCONT));
}
__attribute__((musttail)) return x1666(std::monostate{});
return std::monostate{};
}
std::monostate x7226(std::monostate x7227) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7228 = Stack.pop();
SymVal x7229 = SymStack.pop();
Num x7230 = Stack.pop();
SymVal x7231 = SymStack.pop();
Num x7232 = x7230.i32_eq(x7228);
Stack.push(x7232);
bool x7233 = allConcrete(x7231, x7229);
SymVal x7234 = x7233 ? Concrete(x7232, 32) : x7231.eq(x7229).bool2bv();
SymStack.push(x7234);
}
{
Num x7235 = Stack.pop();
SymVal x7236 = SymStack.pop();
Num x7237 = Stack.pop();
SymVal x7238 = SymStack.pop();
Num x7239 = x7237.i32_and(x7235);
Stack.push(x7239);
bool x7240 = allConcrete(x7238, x7236);
SymVal x7241 = x7240 ? Concrete(x7239, 32) : x7238.bitwise_and(x7236);
SymStack.push(x7241);
}
{
Num x7242 = Stack.pop();
SymVal x7243 = SymStack.pop();
Num x7244 = Stack.pop();
SymVal x7245 = SymStack.pop();
Num x7246 = x7244.i32_and(x7242);
Stack.push(x7246);
bool x7247 = allConcrete(x7245, x7243);
SymVal x7248 = x7247 ? Concrete(x7246, 32) : x7245.bitwise_and(x7243);
SymStack.push(x7248);
}
{
Num x7249 = Stack.pop();
SymVal x7250 = SymStack.pop();
Num x7251 = Stack.pop();
SymVal x7252 = SymStack.pop();
Num x7253 = x7251.i32_and(x7249);
Stack.push(x7253);
bool x7254 = allConcrete(x7252, x7250);
SymVal x7255 = x7254 ? Concrete(x7253, 32) : x7252.bitwise_and(x7250);
SymStack.push(x7255);
}
{
Num x7256 = Stack.pop();
SymVal x7257 = SymStack.pop();
Num x7258 = Stack.pop();
SymVal x7259 = SymStack.pop();
Num x7260 = x7258.i32_and(x7256);
Stack.push(x7260);
bool x7261 = allConcrete(x7259, x7257);
SymVal x7262 = x7261 ? Concrete(x7260, 32) : x7259.bitwise_and(x7257);
SymStack.push(x7262);
}
{
Num x7263 = Stack.pop();
SymVal x7264 = SymStack.pop();
Num x7265 = Stack.pop();
SymVal x7266 = SymStack.pop();
Num x7267 = x7265.i32_and(x7263);
Stack.push(x7267);
bool x7268 = allConcrete(x7266, x7264);
SymVal x7269 = x7268 ? Concrete(x7267, 32) : x7266.bitwise_and(x7264);
SymStack.push(x7269);
}
{
Num x7270 = Stack.pop();
SymVal x7271 = SymStack.pop();
Num x7272 = Stack.pop();
SymVal x7273 = SymStack.pop();
Num x7274 = x7272.i32_and(x7270);
Stack.push(x7274);
bool x7275 = allConcrete(x7273, x7271);
SymVal x7276 = x7275 ? Concrete(x7274, 32) : x7273.bitwise_and(x7271);
SymStack.push(x7276);
}
{
Num x7277 = Stack.pop();
SymVal x7278 = SymStack.pop();
Num x7279 = Stack.pop();
SymVal x7280 = SymStack.pop();
Num x7281 = x7279.i32_and(x7277);
Stack.push(x7281);
bool x7282 = allConcrete(x7280, x7278);
SymVal x7283 = x7282 ? Concrete(x7281, 32) : x7280.bitwise_and(x7278);
SymStack.push(x7283);
}
{
Num x7284 = Stack.pop();
SymVal x7285 = SymStack.pop();
GENSYM_SYM_ASSERT(x7285);
GENSYM_ASSERT(x7284.toInt() != 0);
}
__attribute__((musttail)) return x11(std::monostate{});
return std::monostate{};
}
std::monostate x1996(std::monostate x7212) {
infoWhen("CALL", "Entered the function at 12, stackSize =", Stack.size());
Frames.pushFrameCallee(4);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x7213 = Stack.pop();
SymStack.pop();
Num x7214 = I32V(Memory.loadInt(x7213.toInt(), 0));
SymVal x7215 = SymMemory.loadSym(x7213.toInt(), 0);
Stack.push(x7214);
SymStack.push(x7215);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x7216 = Stack.pop();
SymVal x7217 = SymStack.pop();
Num x7218 = Stack.pop();
SymVal x7219 = SymStack.pop();
Num x7220 = x7218.i32_eq(x7216);
Stack.push(x7220);
bool x7221 = allConcrete(x7219, x7217);
SymVal x7222 = x7221 ? Concrete(x7220, 32) : x7219.eq(x7217).bool2bv();
SymStack.push(x7222);
}
Num x7223 = Stack.pop();
{
SymVal x7224 = SymStack.pop();
ExploreTree.fillIfElseNode(x7224, 23);
}
int x7225 = x7223.toInt();
if (x7225 != 0) {
ExploreTree.moveCursor(true, makeControl(x7044, CURRENT_MCONT));
__attribute__((musttail)) return x7208(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x7208, CURRENT_MCONT));
__attribute__((musttail)) return x7044(std::monostate{});
}
return std::monostate{};
}
std::monostate x7208(std::monostate x7209) {
info("Entering the true branch 23 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x7210 = Stack.pop();
SymVal x7211 = SymStack.pop();
Frames.set(2, x7210);
SymFrames.set(2, x7211);
}
__attribute__((musttail)) return x7059(std::monostate{});
return std::monostate{};
}
std::monostate x7059(std::monostate x7194) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x7195 = Stack.pop();
SymStack.pop();
Num x7196 = I32V(Memory.loadInt(x7195.toInt(), 4));
SymVal x7197 = SymMemory.loadSym(x7195.toInt(), 4);
Stack.push(x7196);
SymStack.push(x7197);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x7198 = Stack.pop();
SymVal x7199 = SymStack.pop();
Num x7200 = Stack.pop();
SymVal x7201 = SymStack.pop();
Num x7202 = x7200.i32_eq(x7198);
Stack.push(x7202);
bool x7203 = allConcrete(x7201, x7199);
SymVal x7204 = x7203 ? Concrete(x7202, 32) : x7201.eq(x7199).bool2bv();
SymStack.push(x7204);
}
Num x7205 = Stack.pop();
{
SymVal x7206 = SymStack.pop();
ExploreTree.fillIfElseNode(x7206, 56);
}
int x7207 = x7205.toInt();
if (x7207 != 0) {
ExploreTree.moveCursor(true, makeControl(x7163, CURRENT_MCONT));
__attribute__((musttail)) return x7192(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x7192, CURRENT_MCONT));
__attribute__((musttail)) return x7163(std::monostate{});
}
return std::monostate{};
}
std::monostate x7192(std::monostate x7193) {
info("Entering the true branch 56 of the if");
info("Jump to 2");
__attribute__((musttail)) return x7118(std::monostate{});
return std::monostate{};
}
std::monostate x7163(std::monostate x7164) {
info("Entering the false branch 56 of the if");
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x7165 = Stack.pop();
SymVal x7166 = SymStack.pop();
Num x7167 = Stack.pop();
SymVal x7168 = SymStack.pop();
Num x7169 = x7167.i32_mul(x7165);
Stack.push(x7169);
bool x7170 = allConcrete(x7168, x7166);
SymVal x7171 = x7170 ? Concrete(x7169, 32) : x7168.mul(x7166);
SymStack.push(x7171);
}
{
Num x7172 = Stack.pop();
SymVal x7173 = SymStack.pop();
Num x7174 = Stack.pop();
SymVal x7175 = SymStack.pop();
Num x7176 = x7174.i32_add(x7172);
Stack.push(x7176);
bool x7177 = allConcrete(x7175, x7173);
SymVal x7178 = x7177 ? Concrete(x7176, 32) : x7175.add(x7173);
SymStack.push(x7178);
}
{
Num x7179 = Stack.pop();
SymStack.pop();
Num x7180 = I32V(Memory.loadInt(x7179.toInt(), 8));
SymVal x7181 = SymMemory.loadSym(x7179.toInt(), 8);
Stack.push(x7180);
SymStack.push(x7181);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x7182 = Stack.pop();
SymVal x7183 = SymStack.pop();
Num x7184 = Stack.pop();
SymVal x7185 = SymStack.pop();
Num x7186 = x7184.i32_eq(x7182);
Stack.push(x7186);
bool x7187 = allConcrete(x7185, x7183);
SymVal x7188 = x7187 ? Concrete(x7186, 32) : x7185.eq(x7183).bool2bv();
SymStack.push(x7188);
}
Num x7189 = Stack.pop();
{
SymVal x7190 = SymStack.pop();
ExploreTree.fillIfElseNode(x7190, 57);
}
int x7191 = x7189.toInt();
if (x7191 != 0) {
ExploreTree.moveCursor(true, makeControl(x7062, CURRENT_MCONT));
__attribute__((musttail)) return x7159(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x7159, CURRENT_MCONT));
__attribute__((musttail)) return x7062(std::monostate{});
}
return std::monostate{};
}
std::monostate x7159(std::monostate x7160) {
info("Entering the true branch 57 of the if");
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x7161 = Stack.pop();
SymVal x7162 = SymStack.pop();
Frames.set(3, x7161);
SymFrames.set(3, x7162);
}
__attribute__((musttail)) return x7117(std::monostate{});
return std::monostate{};
}
std::monostate x7117(std::monostate x7138) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x7139 = Stack.pop();
SymStack.pop();
Num x7140 = I32V(Memory.loadInt(x7139.toInt(), 4));
SymVal x7141 = SymMemory.loadSym(x7139.toInt(), 4);
Stack.push(x7140);
SymStack.push(x7141);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x7142 = Stack.pop();
SymVal x7143 = SymStack.pop();
Num x7144 = Stack.pop();
SymVal x7145 = SymStack.pop();
Num x7146 = x7144.i32_sub(x7142);
Stack.push(x7146);
bool x7147 = allConcrete(x7145, x7143);
SymVal x7148 = x7147 ? Concrete(x7146, 32) : x7145.minus(x7143);
SymStack.push(x7148);
}
{
Num x7149 = Stack.pop();
SymVal x7150 = SymStack.pop();
Num x7151 = Stack.pop();
SymVal x7152 = SymStack.pop();
Num x7153 = x7151.i32_eq(x7149);
Stack.push(x7153);
bool x7154 = allConcrete(x7152, x7150);
SymVal x7155 = x7154 ? Concrete(x7153, 32) : x7152.eq(x7150).bool2bv();
SymStack.push(x7155);
}
Num x7156 = Stack.pop();
{
SymVal x7157 = SymStack.pop();
ExploreTree.fillIfElseNode(x7157, 39);
}
int x7158 = x7156.toInt();
if (x7158 != 0) {
ExploreTree.moveCursor(true, makeControl(x7064, CURRENT_MCONT));
__attribute__((musttail)) return x7136(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x7136, CURRENT_MCONT));
__attribute__((musttail)) return x7064(std::monostate{});
}
return std::monostate{};
}
std::monostate x7136(std::monostate x7137) {
info("Entering the true branch 39 of the if");
info("Jump to 2");
__attribute__((musttail)) return x7120(std::monostate{});
return std::monostate{};
}
std::monostate x7120(std::monostate x7121) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x7122 = Stack.pop();
SymStack.pop();
Num x7123 = I32V(Memory.loadInt(x7122.toInt(), 4));
SymVal x7124 = SymMemory.loadSym(x7122.toInt(), 4);
Stack.push(x7123);
SymStack.push(x7124);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x7125 = Stack.pop();
SymVal x7126 = SymStack.pop();
Num x7127 = Stack.pop();
SymVal x7128 = SymStack.pop();
Num x7129 = x7127.i32_sub(x7125);
Stack.push(x7129);
bool x7130 = allConcrete(x7128, x7126);
SymVal x7131 = x7130 ? Concrete(x7129, 32) : x7128.minus(x7126);
SymStack.push(x7131);
}
{
Num x7132 = Stack.pop();
SymVal x7133 = SymStack.pop();
Num x7134 = Stack.pop();
SymStack.pop();
int x7135 = x7134.toInt();
Memory.storeInt(x7135, 4, x7132.toInt());
SymMemory.storeSym(x7135, 4, x7133);
}
info("Jump to 3");
__attribute__((musttail)) return x7118(std::monostate{});
return std::monostate{};
}
std::monostate x7118(std::monostate x7119) {
info("Exiting the block, stackSize =", Stack.size());
__attribute__((musttail)) return x1966(std::monostate{});
return std::monostate{};
}
std::monostate x7064(std::monostate x7065) {
info("Entering the false branch 39 of the if");
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x7066 = Stack.pop();
SymVal x7067 = SymStack.pop();
Num x7068 = Stack.pop();
SymVal x7069 = SymStack.pop();
Num x7070 = x7068.i32_mul(x7066);
Stack.push(x7070);
bool x7071 = allConcrete(x7069, x7067);
SymVal x7072 = x7071 ? Concrete(x7070, 32) : x7069.mul(x7067);
SymStack.push(x7072);
}
{
Num x7073 = Stack.pop();
SymVal x7074 = SymStack.pop();
Num x7075 = Stack.pop();
SymVal x7076 = SymStack.pop();
Num x7077 = x7075.i32_add(x7073);
Stack.push(x7077);
bool x7078 = allConcrete(x7076, x7074);
SymVal x7079 = x7078 ? Concrete(x7077, 32) : x7076.add(x7074);
SymStack.push(x7079);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x7080 = Stack.pop();
SymVal x7081 = SymStack.pop();
Num x7082 = Stack.pop();
SymVal x7083 = SymStack.pop();
Num x7084 = x7082.i32_add(x7080);
Stack.push(x7084);
bool x7085 = allConcrete(x7083, x7081);
SymVal x7086 = x7085 ? Concrete(x7084, 32) : x7083.add(x7081);
SymStack.push(x7086);
}
{
Num x7087 = Stack.pop();
SymVal x7088 = SymStack.pop();
Num x7089 = Stack.pop();
SymVal x7090 = SymStack.pop();
Num x7091 = x7089.i32_mul(x7087);
Stack.push(x7091);
bool x7092 = allConcrete(x7090, x7088);
SymVal x7093 = x7092 ? Concrete(x7091, 32) : x7090.mul(x7088);
SymStack.push(x7093);
}
{
Num x7094 = Stack.pop();
SymVal x7095 = SymStack.pop();
Num x7096 = Stack.pop();
SymVal x7097 = SymStack.pop();
Num x7098 = x7096.i32_add(x7094);
Stack.push(x7098);
bool x7099 = allConcrete(x7097, x7095);
SymVal x7100 = x7099 ? Concrete(x7098, 32) : x7097.add(x7095);
SymStack.push(x7100);
}
{
Num x7101 = Stack.pop();
SymStack.pop();
Num x7102 = I32V(Memory.loadInt(x7101.toInt(), 8));
SymVal x7103 = SymMemory.loadSym(x7101.toInt(), 8);
Stack.push(x7102);
SymStack.push(x7103);
}
{
Num x7104 = Stack.pop();
SymVal x7105 = SymStack.pop();
Num x7106 = Stack.pop();
SymStack.pop();
int x7107 = x7106.toInt();
Memory.storeInt(x7107, 8, x7104.toInt());
SymMemory.storeSym(x7107, 8, x7105);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x7108 = Stack.pop();
SymVal x7109 = SymStack.pop();
Num x7110 = Stack.pop();
SymVal x7111 = SymStack.pop();
Num x7112 = x7110.i32_add(x7108);
Stack.push(x7112);
bool x7113 = allConcrete(x7111, x7109);
SymVal x7114 = x7113 ? Concrete(x7112, 32) : x7111.add(x7109);
SymStack.push(x7114);
}
{
Num x7115 = Stack.pop();
SymVal x7116 = SymStack.pop();
Frames.set(3, x7115);
SymFrames.set(3, x7116);
}
info("Jump to 1");
__attribute__((musttail)) return x7117(std::monostate{});
return std::monostate{};
}
std::monostate x7062(std::monostate x7063) {
info("Entering the false branch 57 of the if");
__attribute__((musttail)) return x7060(std::monostate{});
return std::monostate{};
}
std::monostate x7060(std::monostate x7061) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x7048(std::monostate{});
return std::monostate{};
}
std::monostate x7048(std::monostate x7049) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x7050 = Stack.pop();
SymVal x7051 = SymStack.pop();
Num x7052 = Stack.pop();
SymVal x7053 = SymStack.pop();
Num x7054 = x7052.i32_add(x7050);
Stack.push(x7054);
bool x7055 = allConcrete(x7053, x7051);
SymVal x7056 = x7055 ? Concrete(x7054, 32) : x7053.add(x7051);
SymStack.push(x7056);
}
{
Num x7057 = Stack.pop();
SymVal x7058 = SymStack.pop();
Frames.set(2, x7057);
SymFrames.set(2, x7058);
}
info("Jump to 0");
__attribute__((musttail)) return x7059(std::monostate{});
return std::monostate{};
}
std::monostate x7044(std::monostate x7045) {
info("Entering the false branch 23 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x7046 = Stack.pop();
SymVal x7047 = SymStack.pop();
Frames.set(2, x7046);
SymFrames.set(2, x7047);
}
__attribute__((musttail)) return x6965(std::monostate{});
return std::monostate{};
}
std::monostate x6965(std::monostate x7023) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x7024 = Stack.pop();
SymStack.pop();
Num x7025 = I32V(Memory.loadInt(x7024.toInt(), 4));
SymVal x7026 = SymMemory.loadSym(x7024.toInt(), 4);
Stack.push(x7025);
SymStack.push(x7026);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x7027 = Stack.pop();
SymVal x7028 = SymStack.pop();
Num x7029 = Stack.pop();
SymVal x7030 = SymStack.pop();
Num x7031 = x7029.i32_sub(x7027);
Stack.push(x7031);
bool x7032 = allConcrete(x7030, x7028);
SymVal x7033 = x7032 ? Concrete(x7031, 32) : x7030.minus(x7028);
SymStack.push(x7033);
}
{
Num x7034 = Stack.pop();
SymVal x7035 = SymStack.pop();
Num x7036 = Stack.pop();
SymVal x7037 = SymStack.pop();
Num x7038 = x7036.i32_le_s(x7034);
Stack.push(x7038);
bool x7039 = allConcrete(x7037, x7035);
SymVal x7040 = x7039 ? Concrete(x7038, 32) : x7037.le(x7035).bool2bv();
SymStack.push(x7040);
}
Num x7041 = Stack.pop();
{
SymVal x7042 = SymStack.pop();
ExploreTree.fillIfElseNode(x7042, 24);
}
int x7043 = x7041.toInt();
if (x7043 != 0) {
ExploreTree.moveCursor(true, makeControl(x6995, CURRENT_MCONT));
__attribute__((musttail)) return x6997(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x6997, CURRENT_MCONT));
__attribute__((musttail)) return x6995(std::monostate{});
}
return std::monostate{};
}
std::monostate x6997(std::monostate x6998) {
info("Entering the true branch 24 of the if");
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x6999 = Stack.pop();
SymVal x7000 = SymStack.pop();
Num x7001 = Stack.pop();
SymVal x7002 = SymStack.pop();
Num x7003 = x7001.i32_mul(x6999);
Stack.push(x7003);
bool x7004 = allConcrete(x7002, x7000);
SymVal x7005 = x7004 ? Concrete(x7003, 32) : x7002.mul(x7000);
SymStack.push(x7005);
}
{
Num x7006 = Stack.pop();
SymVal x7007 = SymStack.pop();
Num x7008 = Stack.pop();
SymVal x7009 = SymStack.pop();
Num x7010 = x7008.i32_add(x7006);
Stack.push(x7010);
bool x7011 = allConcrete(x7009, x7007);
SymVal x7012 = x7011 ? Concrete(x7010, 32) : x7009.add(x7007);
SymStack.push(x7012);
}
{
Num x7013 = Stack.pop();
SymStack.pop();
Num x7014 = I32V(Memory.loadInt(x7013.toInt(), 8));
SymVal x7015 = SymMemory.loadSym(x7013.toInt(), 8);
Stack.push(x7014);
SymStack.push(x7015);
}
{
Num x7016 = Stack.pop();
SymVal x7017 = SymStack.pop();
Num x7018 = Stack.pop();
SymVal x7019 = SymStack.pop();
Num x7020 = x7018.i32_gt_s(x7016);
Stack.push(x7020);
bool x7021 = allConcrete(x7019, x7017);
SymVal x7022 = x7021 ? Concrete(x7020, 32) : x7019.gt(x7017).bool2bv();
SymStack.push(x7022);
}
__attribute__((musttail)) return x6966(std::monostate{});
return std::monostate{};
}
std::monostate x6995(std::monostate x6996) {
info("Entering the false branch 24 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
__attribute__((musttail)) return x6966(std::monostate{});
return std::monostate{};
}
std::monostate x6966(std::monostate x6967) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x6968 = Stack.pop();
SymStack.pop();
Num x6969 = I32V(Memory.loadInt(x6968.toInt(), 4));
SymVal x6970 = SymMemory.loadSym(x6968.toInt(), 4);
Stack.push(x6969);
SymStack.push(x6970);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6971 = Stack.pop();
SymVal x6972 = SymStack.pop();
Num x6973 = Stack.pop();
SymVal x6974 = SymStack.pop();
Num x6975 = x6973.i32_sub(x6971);
Stack.push(x6975);
bool x6976 = allConcrete(x6974, x6972);
SymVal x6977 = x6976 ? Concrete(x6975, 32) : x6974.minus(x6972);
SymStack.push(x6977);
}
{
Num x6978 = Stack.pop();
SymVal x6979 = SymStack.pop();
Num x6980 = Stack.pop();
SymVal x6981 = SymStack.pop();
Num x6982 = x6980.i32_le_s(x6978);
Stack.push(x6982);
bool x6983 = allConcrete(x6981, x6979);
SymVal x6984 = x6983 ? Concrete(x6982, 32) : x6981.le(x6979).bool2bv();
SymStack.push(x6984);
}
{
Num x6985 = Stack.pop();
SymVal x6986 = SymStack.pop();
Num x6987 = Stack.pop();
SymVal x6988 = SymStack.pop();
Num x6989 = x6987.i32_and(x6985);
Stack.push(x6989);
bool x6990 = allConcrete(x6988, x6986);
SymVal x6991 = x6990 ? Concrete(x6989, 32) : x6988.bitwise_and(x6986);
SymStack.push(x6991);
}
Num x6992 = Stack.pop();
{
SymVal x6993 = SymStack.pop();
ExploreTree.fillIfElseNode(x6993, 25);
}
int x6994 = x6992.toInt();
if (x6994 != 0) {
ExploreTree.moveCursor(true, makeControl(x6952, CURRENT_MCONT));
__attribute__((musttail)) return x6954(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x6954, CURRENT_MCONT));
__attribute__((musttail)) return x6952(std::monostate{});
}
return std::monostate{};
}
std::monostate x6954(std::monostate x6955) {
info("Entering the true branch 25 of the if");
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6956 = Stack.pop();
SymVal x6957 = SymStack.pop();
Num x6958 = Stack.pop();
SymVal x6959 = SymStack.pop();
Num x6960 = x6958.i32_add(x6956);
Stack.push(x6960);
bool x6961 = allConcrete(x6959, x6957);
SymVal x6962 = x6961 ? Concrete(x6960, 32) : x6959.add(x6957);
SymStack.push(x6962);
}
{
Num x6963 = Stack.pop();
SymVal x6964 = SymStack.pop();
Frames.set(2, x6963);
SymFrames.set(2, x6964);
}
info("Jump to 1");
__attribute__((musttail)) return x6965(std::monostate{});
return std::monostate{};
}
std::monostate x6952(std::monostate x6953) {
info("Entering the false branch 25 of the if");
info("Jump to 2");
__attribute__((musttail)) return x6937(std::monostate{});
return std::monostate{};
}
std::monostate x6937(std::monostate x6938) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x6939 = Stack.pop();
SymStack.pop();
Num x6940 = I32V(Memory.loadInt(x6939.toInt(), 4));
SymVal x6941 = SymMemory.loadSym(x6939.toInt(), 4);
Stack.push(x6940);
SymStack.push(x6941);
}
{
Num x6942 = Stack.pop();
SymVal x6943 = SymStack.pop();
Num x6944 = Stack.pop();
SymVal x6945 = SymStack.pop();
Num x6946 = x6944.i32_lt_s(x6942);
Stack.push(x6946);
bool x6947 = allConcrete(x6945, x6943);
SymVal x6948 = x6947 ? Concrete(x6946, 32) : x6945.lt(x6943).bool2bv();
SymStack.push(x6948);
}
Num x6949 = Stack.pop();
{
SymVal x6950 = SymStack.pop();
ExploreTree.fillIfElseNode(x6950, 26);
}
int x6951 = x6949.toInt();
if (x6951 != 0) {
ExploreTree.moveCursor(true, makeControl(x6909, CURRENT_MCONT));
__attribute__((musttail)) return x6911(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x6911, CURRENT_MCONT));
__attribute__((musttail)) return x6909(std::monostate{});
}
return std::monostate{};
}
std::monostate x6911(std::monostate x6912) {
info("Entering the true branch 26 of the if");
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x6913 = Stack.pop();
SymVal x6914 = SymStack.pop();
Num x6915 = Stack.pop();
SymVal x6916 = SymStack.pop();
Num x6917 = x6915.i32_mul(x6913);
Stack.push(x6917);
bool x6918 = allConcrete(x6916, x6914);
SymVal x6919 = x6918 ? Concrete(x6917, 32) : x6916.mul(x6914);
SymStack.push(x6919);
}
{
Num x6920 = Stack.pop();
SymVal x6921 = SymStack.pop();
Num x6922 = Stack.pop();
SymVal x6923 = SymStack.pop();
Num x6924 = x6922.i32_add(x6920);
Stack.push(x6924);
bool x6925 = allConcrete(x6923, x6921);
SymVal x6926 = x6925 ? Concrete(x6924, 32) : x6923.add(x6921);
SymStack.push(x6926);
}
{
Num x6927 = Stack.pop();
SymStack.pop();
Num x6928 = I32V(Memory.loadInt(x6927.toInt(), 8));
SymVal x6929 = SymMemory.loadSym(x6927.toInt(), 8);
Stack.push(x6928);
SymStack.push(x6929);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x6930 = Stack.pop();
SymVal x6931 = SymStack.pop();
Num x6932 = Stack.pop();
SymVal x6933 = SymStack.pop();
Num x6934 = x6932.i32_eq(x6930);
Stack.push(x6934);
bool x6935 = allConcrete(x6933, x6931);
SymVal x6936 = x6935 ? Concrete(x6934, 32) : x6933.eq(x6931).bool2bv();
SymStack.push(x6936);
}
__attribute__((musttail)) return x6904(std::monostate{});
return std::monostate{};
}
std::monostate x6909(std::monostate x6910) {
info("Entering the false branch 26 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
__attribute__((musttail)) return x6904(std::monostate{});
return std::monostate{};
}
std::monostate x6904(std::monostate x6905) {
info("Exiting the if, stackSize =", Stack.size());
Num x6906 = Stack.pop();
{
SymVal x6907 = SymStack.pop();
ExploreTree.fillIfElseNode(x6907, 27);
}
int x6908 = x6906.toInt();
if (x6908 != 0) {
ExploreTree.moveCursor(true, makeControl(x5740, CURRENT_MCONT));
__attribute__((musttail)) return x6836(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x6836, CURRENT_MCONT));
__attribute__((musttail)) return x5740(std::monostate{});
}
return std::monostate{};
}
std::monostate x6836(std::monostate x6837) {
info("Entering the true branch 27 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x6838 = Stack.pop();
SymStack.pop();
Num x6839 = I32V(Memory.loadInt(x6838.toInt(), 0));
SymVal x6840 = SymMemory.loadSym(x6838.toInt(), 0);
Stack.push(x6839);
SymStack.push(x6840);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6841 = Stack.pop();
SymVal x6842 = SymStack.pop();
Num x6843 = Stack.pop();
SymVal x6844 = SymStack.pop();
Num x6845 = x6843.i32_sub(x6841);
Stack.push(x6845);
bool x6846 = allConcrete(x6844, x6842);
SymVal x6847 = x6846 ? Concrete(x6845, 32) : x6844.minus(x6842);
SymStack.push(x6847);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6848 = Stack.pop();
SymVal x6849 = SymStack.pop();
Num x6850 = Stack.pop();
SymVal x6851 = SymStack.pop();
Num x6852 = x6850.i32_mul(x6848);
Stack.push(x6852);
bool x6853 = allConcrete(x6851, x6849);
SymVal x6854 = x6853 ? Concrete(x6852, 32) : x6851.mul(x6849);
SymStack.push(x6854);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6855 = Stack.pop();
SymVal x6856 = SymStack.pop();
Num x6857 = Stack.pop();
SymVal x6858 = SymStack.pop();
Num x6859 = x6857.i32_mul(x6855);
Stack.push(x6859);
bool x6860 = allConcrete(x6858, x6856);
SymVal x6861 = x6860 ? Concrete(x6859, 32) : x6858.mul(x6856);
SymStack.push(x6861);
}
{
Num x6862 = Stack.pop();
SymVal x6863 = SymStack.pop();
Num x6864 = Stack.pop();
SymVal x6865 = SymStack.pop();
Num x6866 = x6864.i32_add(x6862);
Stack.push(x6866);
bool x6867 = allConcrete(x6865, x6863);
SymVal x6868 = x6867 ? Concrete(x6866, 32) : x6865.add(x6863);
SymStack.push(x6868);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x6869 = Stack.pop();
SymVal x6870 = SymStack.pop();
Num x6871 = Stack.pop();
SymVal x6872 = SymStack.pop();
Num x6873 = x6871.i32_add(x6869);
Stack.push(x6873);
bool x6874 = allConcrete(x6872, x6870);
SymVal x6875 = x6874 ? Concrete(x6873, 32) : x6872.add(x6870);
SymStack.push(x6875);
}
{
Num x6876 = Stack.pop();
SymStack.pop();
Num x6877 = I32V(Memory.loadInt(x6876.toInt(), 8));
SymVal x6878 = SymMemory.loadSym(x6876.toInt(), 8);
Stack.push(x6877);
SymStack.push(x6878);
}
{
Num x6879 = Stack.peek();
SymVal x6880 = SymStack.peek();
Frames.set(5, x6879);
SymFrames.set(5, x6880);
}
{
Num x6881 = Stack.pop();
SymStack.pop();
Num x6882 = I32V(Memory.loadInt(x6881.toInt(), 4));
SymVal x6883 = SymMemory.loadSym(x6881.toInt(), 4);
Stack.push(x6882);
SymStack.push(x6883);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x6884 = Stack.pop();
SymStack.pop();
Num x6885 = I32V(Memory.loadInt(x6884.toInt(), 0));
SymVal x6886 = SymMemory.loadSym(x6884.toInt(), 0);
Stack.push(x6885);
SymStack.push(x6886);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x6887 = Stack.pop();
SymVal x6888 = SymStack.pop();
Num x6889 = Stack.pop();
SymVal x6890 = SymStack.pop();
Num x6891 = x6889.i32_div_s(x6887);
Stack.push(x6891);
bool x6892 = allConcrete(x6890, x6888);
SymVal x6893 = x6892 ? Concrete(x6891, 32) : x6890.div(x6888);
SymStack.push(x6893);
}
{
Num x6894 = Stack.pop();
SymVal x6895 = SymStack.pop();
Num x6896 = Stack.pop();
SymVal x6897 = SymStack.pop();
Num x6898 = x6896.i32_ge_s(x6894);
Stack.push(x6898);
bool x6899 = allConcrete(x6897, x6895);
SymVal x6900 = x6899 ? Concrete(x6898, 32) : x6897.ge(x6895).bool2bv();
SymStack.push(x6900);
}
Num x6901 = Stack.pop();
{
SymVal x6902 = SymStack.pop();
ExploreTree.fillIfElseNode(x6902, 54);
}
int x6903 = x6901.toInt();
if (x6903 != 0) {
ExploreTree.moveCursor(true, makeControl(x6681, CURRENT_MCONT));
__attribute__((musttail)) return x6762(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x6762, CURRENT_MCONT));
__attribute__((musttail)) return x6681(std::monostate{});
}
return std::monostate{};
}
std::monostate x6762(std::monostate x6763) {
info("Entering the true branch 54 of the if");
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x6764 = Stack.pop();
SymVal x6765 = SymStack.pop();
Num x6766 = Stack.pop();
SymVal x6767 = SymStack.pop();
Num x6768 = x6766.i32_mul(x6764);
Stack.push(x6768);
bool x6769 = allConcrete(x6767, x6765);
SymVal x6770 = x6769 ? Concrete(x6768, 32) : x6767.mul(x6765);
SymStack.push(x6770);
}
{
Num x6771 = Stack.pop();
SymVal x6772 = SymStack.pop();
Num x6773 = Stack.pop();
SymVal x6774 = SymStack.pop();
Num x6775 = x6773.i32_add(x6771);
Stack.push(x6775);
bool x6776 = allConcrete(x6774, x6772);
SymVal x6777 = x6776 ? Concrete(x6775, 32) : x6774.add(x6772);
SymStack.push(x6777);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x6778 = Stack.pop();
SymStack.pop();
Num x6779 = I32V(Memory.loadInt(x6778.toInt(), 4));
SymVal x6780 = SymMemory.loadSym(x6778.toInt(), 4);
Stack.push(x6779);
SymStack.push(x6780);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6781 = Stack.pop();
SymVal x6782 = SymStack.pop();
Num x6783 = Stack.pop();
SymVal x6784 = SymStack.pop();
Num x6785 = x6783.i32_sub(x6781);
Stack.push(x6785);
bool x6786 = allConcrete(x6784, x6782);
SymVal x6787 = x6786 ? Concrete(x6785, 32) : x6784.minus(x6782);
SymStack.push(x6787);
}
{
Num x6788 = Stack.pop();
SymVal x6789 = SymStack.pop();
Num x6790 = Stack.pop();
SymVal x6791 = SymStack.pop();
Num x6792 = x6790.i32_mul(x6788);
Stack.push(x6792);
bool x6793 = allConcrete(x6791, x6789);
SymVal x6794 = x6793 ? Concrete(x6792, 32) : x6791.mul(x6789);
SymStack.push(x6794);
}
{
Num x6795 = Stack.pop();
SymVal x6796 = SymStack.pop();
Num x6797 = Stack.pop();
SymVal x6798 = SymStack.pop();
Num x6799 = x6797.i32_add(x6795);
Stack.push(x6799);
bool x6800 = allConcrete(x6798, x6796);
SymVal x6801 = x6800 ? Concrete(x6799, 32) : x6798.add(x6796);
SymStack.push(x6801);
}
{
Num x6802 = Stack.pop();
SymStack.pop();
Num x6803 = I32V(Memory.loadInt(x6802.toInt(), 8));
SymVal x6804 = SymMemory.loadSym(x6802.toInt(), 8);
Stack.push(x6803);
SymStack.push(x6804);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x6805 = Stack.pop();
SymStack.pop();
Num x6806 = I32V(Memory.loadInt(x6805.toInt(), 4));
SymVal x6807 = SymMemory.loadSym(x6805.toInt(), 4);
Stack.push(x6806);
SymStack.push(x6807);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6808 = Stack.pop();
SymVal x6809 = SymStack.pop();
Num x6810 = Stack.pop();
SymVal x6811 = SymStack.pop();
Num x6812 = x6810.i32_sub(x6808);
Stack.push(x6812);
bool x6813 = allConcrete(x6811, x6809);
SymVal x6814 = x6813 ? Concrete(x6812, 32) : x6811.minus(x6809);
SymStack.push(x6814);
}
{
Num x6815 = Stack.pop();
SymVal x6816 = SymStack.pop();
Num x6817 = Stack.pop();
SymVal x6818 = SymStack.pop();
Num x6819 = x6817.i32_mul(x6815);
Stack.push(x6819);
bool x6820 = allConcrete(x6818, x6816);
SymVal x6821 = x6820 ? Concrete(x6819, 32) : x6818.mul(x6816);
SymStack.push(x6821);
}
{
Num x6822 = Stack.pop();
SymVal x6823 = SymStack.pop();
Num x6824 = Stack.pop();
SymVal x6825 = SymStack.pop();
Num x6826 = x6824.i32_add(x6822);
Stack.push(x6826);
bool x6827 = allConcrete(x6825, x6823);
SymVal x6828 = x6827 ? Concrete(x6826, 32) : x6825.add(x6823);
SymStack.push(x6828);
}
{
Num x6829 = Stack.pop();
SymStack.pop();
Num x6830 = I32V(Memory.loadInt(x6829.toInt(), 8));
SymVal x6831 = SymMemory.loadSym(x6829.toInt(), 8);
Stack.push(x6830);
SymStack.push(x6831);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 12);
Num x6832 = Stack.pop();
Num x6833 = Stack.pop();
SymVal x6834 = SymStack.pop();
SymVal x6835 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x6833);
Frames.set(1, x6832);
SymFrames.set(0, x6835);
SymFrames.set(1, x6834);
updateCurrentMCont(prependCont(x6756, CURRENT_MCONT));
}
__attribute__((musttail)) return x1996(std::monostate{});
return std::monostate{};
}
std::monostate x6756(std::monostate x6757) {
infoWhen("CALL", "Returning from the function at 12, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.pop();
SymStack.pop();
{
Num x6758 = Stack.pop();
SymVal x6759 = SymStack.pop();
Num x6760 = Stack.pop();
SymStack.pop();
int x6761 = x6760.toInt();
Memory.storeInt(x6761, 8, x6758.toInt());
SymMemory.storeSym(x6761, 8, x6759);
}
__attribute__((musttail)) return x6083(std::monostate{});
return std::monostate{};
}
std::monostate x6681(std::monostate x6682) {
info("Entering the false branch 54 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x6683 = Stack.pop();
SymStack.pop();
Num x6684 = I32V(Memory.loadInt(x6683.toInt(), 0));
SymVal x6685 = SymMemory.loadSym(x6683.toInt(), 0);
Stack.push(x6684);
SymStack.push(x6685);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6686 = Stack.pop();
SymVal x6687 = SymStack.pop();
Num x6688 = Stack.pop();
SymVal x6689 = SymStack.pop();
Num x6690 = x6688.i32_sub(x6686);
Stack.push(x6690);
bool x6691 = allConcrete(x6689, x6687);
SymVal x6692 = x6691 ? Concrete(x6690, 32) : x6689.minus(x6687);
SymStack.push(x6692);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6693 = Stack.pop();
SymVal x6694 = SymStack.pop();
Num x6695 = Stack.pop();
SymVal x6696 = SymStack.pop();
Num x6697 = x6695.i32_mul(x6693);
Stack.push(x6697);
bool x6698 = allConcrete(x6696, x6694);
SymVal x6699 = x6698 ? Concrete(x6697, 32) : x6696.mul(x6694);
SymStack.push(x6699);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6700 = Stack.pop();
SymVal x6701 = SymStack.pop();
Num x6702 = Stack.pop();
SymVal x6703 = SymStack.pop();
Num x6704 = x6702.i32_add(x6700);
Stack.push(x6704);
bool x6705 = allConcrete(x6703, x6701);
SymVal x6706 = x6705 ? Concrete(x6704, 32) : x6703.add(x6701);
SymStack.push(x6706);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6707 = Stack.pop();
SymVal x6708 = SymStack.pop();
Num x6709 = Stack.pop();
SymVal x6710 = SymStack.pop();
Num x6711 = x6709.i32_mul(x6707);
Stack.push(x6711);
bool x6712 = allConcrete(x6710, x6708);
SymVal x6713 = x6712 ? Concrete(x6711, 32) : x6710.mul(x6708);
SymStack.push(x6713);
}
{
Num x6714 = Stack.pop();
SymVal x6715 = SymStack.pop();
Num x6716 = Stack.pop();
SymVal x6717 = SymStack.pop();
Num x6718 = x6716.i32_add(x6714);
Stack.push(x6718);
bool x6719 = allConcrete(x6717, x6715);
SymVal x6720 = x6719 ? Concrete(x6718, 32) : x6717.add(x6715);
SymStack.push(x6720);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x6721 = Stack.pop();
SymVal x6722 = SymStack.pop();
Num x6723 = Stack.pop();
SymVal x6724 = SymStack.pop();
Num x6725 = x6723.i32_add(x6721);
Stack.push(x6725);
bool x6726 = allConcrete(x6724, x6722);
SymVal x6727 = x6726 ? Concrete(x6725, 32) : x6724.add(x6722);
SymStack.push(x6727);
}
{
Num x6728 = Stack.pop();
SymStack.pop();
Num x6729 = I32V(Memory.loadInt(x6728.toInt(), 8));
SymVal x6730 = SymMemory.loadSym(x6728.toInt(), 8);
Stack.push(x6729);
SymStack.push(x6730);
}
{
Num x6731 = Stack.peek();
SymVal x6732 = SymStack.peek();
Frames.set(5, x6731);
SymFrames.set(5, x6732);
}
{
Num x6733 = Stack.pop();
SymStack.pop();
Num x6734 = I32V(Memory.loadInt(x6733.toInt(), 4));
SymVal x6735 = SymMemory.loadSym(x6733.toInt(), 4);
Stack.push(x6734);
SymStack.push(x6735);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x6736 = Stack.pop();
SymStack.pop();
Num x6737 = I32V(Memory.loadInt(x6736.toInt(), 0));
SymVal x6738 = SymMemory.loadSym(x6736.toInt(), 0);
Stack.push(x6737);
SymStack.push(x6738);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x6739 = Stack.pop();
SymVal x6740 = SymStack.pop();
Num x6741 = Stack.pop();
SymVal x6742 = SymStack.pop();
Num x6743 = x6741.i32_div_s(x6739);
Stack.push(x6743);
bool x6744 = allConcrete(x6742, x6740);
SymVal x6745 = x6744 ? Concrete(x6743, 32) : x6742.div(x6740);
SymStack.push(x6745);
}
{
Num x6746 = Stack.pop();
SymVal x6747 = SymStack.pop();
Num x6748 = Stack.pop();
SymVal x6749 = SymStack.pop();
Num x6750 = x6748.i32_ge_s(x6746);
Stack.push(x6750);
bool x6751 = allConcrete(x6749, x6747);
SymVal x6752 = x6751 ? Concrete(x6750, 32) : x6749.ge(x6747).bool2bv();
SymStack.push(x6752);
}
Num x6753 = Stack.pop();
{
SymVal x6754 = SymStack.pop();
ExploreTree.fillIfElseNode(x6754, 55);
}
int x6755 = x6753.toInt();
if (x6755 != 0) {
ExploreTree.moveCursor(true, makeControl(x6553, CURRENT_MCONT));
__attribute__((musttail)) return x6627(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x6627, CURRENT_MCONT));
__attribute__((musttail)) return x6553(std::monostate{});
}
return std::monostate{};
}
std::monostate x6627(std::monostate x6628) {
info("Entering the true branch 55 of the if");
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x6629 = Stack.pop();
SymVal x6630 = SymStack.pop();
Num x6631 = Stack.pop();
SymVal x6632 = SymStack.pop();
Num x6633 = x6631.i32_mul(x6629);
Stack.push(x6633);
bool x6634 = allConcrete(x6632, x6630);
SymVal x6635 = x6634 ? Concrete(x6633, 32) : x6632.mul(x6630);
SymStack.push(x6635);
}
{
Num x6636 = Stack.pop();
SymVal x6637 = SymStack.pop();
Num x6638 = Stack.pop();
SymVal x6639 = SymStack.pop();
Num x6640 = x6638.i32_add(x6636);
Stack.push(x6640);
bool x6641 = allConcrete(x6639, x6637);
SymVal x6642 = x6641 ? Concrete(x6640, 32) : x6639.add(x6637);
SymStack.push(x6642);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x6643 = Stack.pop();
SymVal x6644 = SymStack.pop();
Num x6645 = Stack.pop();
SymVal x6646 = SymStack.pop();
Num x6647 = x6645.i32_mul(x6643);
Stack.push(x6647);
bool x6648 = allConcrete(x6646, x6644);
SymVal x6649 = x6648 ? Concrete(x6647, 32) : x6646.mul(x6644);
SymStack.push(x6649);
}
{
Num x6650 = Stack.pop();
SymVal x6651 = SymStack.pop();
Num x6652 = Stack.pop();
SymVal x6653 = SymStack.pop();
Num x6654 = x6652.i32_add(x6650);
Stack.push(x6654);
bool x6655 = allConcrete(x6653, x6651);
SymVal x6656 = x6655 ? Concrete(x6654, 32) : x6653.add(x6651);
SymStack.push(x6656);
}
{
Num x6657 = Stack.pop();
SymStack.pop();
Num x6658 = I32V(Memory.loadInt(x6657.toInt(), 8));
SymVal x6659 = SymMemory.loadSym(x6657.toInt(), 8);
Stack.push(x6658);
SymStack.push(x6659);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x6660 = Stack.pop();
SymVal x6661 = SymStack.pop();
Num x6662 = Stack.pop();
SymVal x6663 = SymStack.pop();
Num x6664 = x6662.i32_mul(x6660);
Stack.push(x6664);
bool x6665 = allConcrete(x6663, x6661);
SymVal x6666 = x6665 ? Concrete(x6664, 32) : x6663.mul(x6661);
SymStack.push(x6666);
}
{
Num x6667 = Stack.pop();
SymVal x6668 = SymStack.pop();
Num x6669 = Stack.pop();
SymVal x6670 = SymStack.pop();
Num x6671 = x6669.i32_add(x6667);
Stack.push(x6671);
bool x6672 = allConcrete(x6670, x6668);
SymVal x6673 = x6672 ? Concrete(x6671, 32) : x6670.add(x6668);
SymStack.push(x6673);
}
{
Num x6674 = Stack.pop();
SymStack.pop();
Num x6675 = I32V(Memory.loadInt(x6674.toInt(), 8));
SymVal x6676 = SymMemory.loadSym(x6674.toInt(), 8);
Stack.push(x6675);
SymStack.push(x6676);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 12);
Num x6677 = Stack.pop();
Num x6678 = Stack.pop();
SymVal x6679 = SymStack.pop();
SymVal x6680 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x6678);
Frames.set(1, x6677);
SymFrames.set(0, x6680);
SymFrames.set(1, x6679);
updateCurrentMCont(prependCont(x6621, CURRENT_MCONT));
}
__attribute__((musttail)) return x1996(std::monostate{});
return std::monostate{};
}
std::monostate x6621(std::monostate x6622) {
infoWhen("CALL", "Returning from the function at 12, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.pop();
SymStack.pop();
{
Num x6623 = Stack.pop();
SymVal x6624 = SymStack.pop();
Num x6625 = Stack.pop();
SymStack.pop();
int x6626 = x6625.toInt();
Memory.storeInt(x6626, 8, x6623.toInt());
SymMemory.storeSym(x6626, 8, x6624);
}
__attribute__((musttail)) return x6085(std::monostate{});
return std::monostate{};
}
std::monostate x6553(std::monostate x6554) {
info("Entering the false branch 55 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x6555 = Stack.pop();
SymStack.pop();
Num x6556 = I32V(Memory.loadInt(x6555.toInt(), 0));
SymVal x6557 = SymMemory.loadSym(x6555.toInt(), 0);
Stack.push(x6556);
SymStack.push(x6557);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6558 = Stack.pop();
SymVal x6559 = SymStack.pop();
Num x6560 = Stack.pop();
SymVal x6561 = SymStack.pop();
Num x6562 = x6560.i32_sub(x6558);
Stack.push(x6562);
bool x6563 = allConcrete(x6561, x6559);
SymVal x6564 = x6563 ? Concrete(x6562, 32) : x6561.minus(x6559);
SymStack.push(x6564);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6565 = Stack.pop();
SymVal x6566 = SymStack.pop();
Num x6567 = Stack.pop();
SymVal x6568 = SymStack.pop();
Num x6569 = x6567.i32_mul(x6565);
Stack.push(x6569);
bool x6570 = allConcrete(x6568, x6566);
SymVal x6571 = x6570 ? Concrete(x6569, 32) : x6568.mul(x6566);
SymStack.push(x6571);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6572 = Stack.pop();
SymVal x6573 = SymStack.pop();
Num x6574 = Stack.pop();
SymVal x6575 = SymStack.pop();
Num x6576 = x6574.i32_mul(x6572);
Stack.push(x6576);
bool x6577 = allConcrete(x6575, x6573);
SymVal x6578 = x6577 ? Concrete(x6576, 32) : x6575.mul(x6573);
SymStack.push(x6578);
}
{
Num x6579 = Stack.pop();
SymVal x6580 = SymStack.pop();
Num x6581 = Stack.pop();
SymVal x6582 = SymStack.pop();
Num x6583 = x6581.i32_add(x6579);
Stack.push(x6583);
bool x6584 = allConcrete(x6582, x6580);
SymVal x6585 = x6584 ? Concrete(x6583, 32) : x6582.add(x6580);
SymStack.push(x6585);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x6586 = Stack.pop();
SymVal x6587 = SymStack.pop();
Num x6588 = Stack.pop();
SymVal x6589 = SymStack.pop();
Num x6590 = x6588.i32_add(x6586);
Stack.push(x6590);
bool x6591 = allConcrete(x6589, x6587);
SymVal x6592 = x6591 ? Concrete(x6590, 32) : x6589.add(x6587);
SymStack.push(x6592);
}
{
Num x6593 = Stack.pop();
SymStack.pop();
Num x6594 = I32V(Memory.loadInt(x6593.toInt(), 8));
SymVal x6595 = SymMemory.loadSym(x6593.toInt(), 8);
Stack.push(x6594);
SymStack.push(x6595);
}
{
Num x6596 = Stack.pop();
SymVal x6597 = SymStack.pop();
Frames.set(5, x6596);
SymFrames.set(5, x6597);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x6598 = Stack.pop();
SymStack.pop();
Num x6599 = I32V(Memory.loadInt(x6598.toInt(), 4));
SymVal x6600 = SymMemory.loadSym(x6598.toInt(), 4);
Stack.push(x6599);
SymStack.push(x6600);
}
{
Num x6601 = Stack.pop();
SymVal x6602 = SymStack.pop();
Num x6603 = Stack.pop();
SymVal x6604 = SymStack.pop();
Num x6605 = x6603.i32_mul(x6601);
Stack.push(x6605);
bool x6606 = allConcrete(x6604, x6602);
SymVal x6607 = x6606 ? Concrete(x6605, 32) : x6604.mul(x6602);
SymStack.push(x6607);
}
{
Num x6608 = Stack.pop();
SymVal x6609 = SymStack.pop();
Num x6610 = Stack.pop();
SymVal x6611 = SymStack.pop();
Num x6612 = x6610.i32_add(x6608);
Stack.push(x6612);
bool x6613 = allConcrete(x6611, x6609);
SymVal x6614 = x6613 ? Concrete(x6612, 32) : x6611.add(x6609);
SymStack.push(x6614);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x6615 = Stack.pop();
SymVal x6616 = SymStack.pop();
Num x6617 = Stack.pop();
SymStack.pop();
int x6618 = x6617.toInt();
Memory.storeInt(x6618, 8, x6615.toInt());
SymMemory.storeSym(x6618, 8, x6616);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x6619 = Stack.pop();
SymVal x6620 = SymStack.pop();
Frames.set(3, x6619);
SymFrames.set(3, x6620);
}
__attribute__((musttail)) return x5926(std::monostate{});
return std::monostate{};
}
std::monostate x5926(std::monostate x6491) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x6492 = Stack.pop();
SymStack.pop();
Num x6493 = I32V(Memory.loadInt(x6492.toInt(), 0));
SymVal x6494 = SymMemory.loadSym(x6492.toInt(), 0);
Stack.push(x6493);
SymStack.push(x6494);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6495 = Stack.pop();
SymVal x6496 = SymStack.pop();
Num x6497 = Stack.pop();
SymVal x6498 = SymStack.pop();
Num x6499 = x6497.i32_sub(x6495);
Stack.push(x6499);
bool x6500 = allConcrete(x6498, x6496);
SymVal x6501 = x6500 ? Concrete(x6499, 32) : x6498.minus(x6496);
SymStack.push(x6501);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6502 = Stack.pop();
SymVal x6503 = SymStack.pop();
Num x6504 = Stack.pop();
SymVal x6505 = SymStack.pop();
Num x6506 = x6504.i32_mul(x6502);
Stack.push(x6506);
bool x6507 = allConcrete(x6505, x6503);
SymVal x6508 = x6507 ? Concrete(x6506, 32) : x6505.mul(x6503);
SymStack.push(x6508);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6509 = Stack.pop();
SymVal x6510 = SymStack.pop();
Num x6511 = Stack.pop();
SymVal x6512 = SymStack.pop();
Num x6513 = x6511.i32_add(x6509);
Stack.push(x6513);
bool x6514 = allConcrete(x6512, x6510);
SymVal x6515 = x6514 ? Concrete(x6513, 32) : x6512.add(x6510);
SymStack.push(x6515);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6516 = Stack.pop();
SymVal x6517 = SymStack.pop();
Num x6518 = Stack.pop();
SymVal x6519 = SymStack.pop();
Num x6520 = x6518.i32_mul(x6516);
Stack.push(x6520);
bool x6521 = allConcrete(x6519, x6517);
SymVal x6522 = x6521 ? Concrete(x6520, 32) : x6519.mul(x6517);
SymStack.push(x6522);
}
{
Num x6523 = Stack.pop();
SymVal x6524 = SymStack.pop();
Num x6525 = Stack.pop();
SymVal x6526 = SymStack.pop();
Num x6527 = x6525.i32_add(x6523);
Stack.push(x6527);
bool x6528 = allConcrete(x6526, x6524);
SymVal x6529 = x6528 ? Concrete(x6527, 32) : x6526.add(x6524);
SymStack.push(x6529);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x6530 = Stack.pop();
SymVal x6531 = SymStack.pop();
Num x6532 = Stack.pop();
SymVal x6533 = SymStack.pop();
Num x6534 = x6532.i32_add(x6530);
Stack.push(x6534);
bool x6535 = allConcrete(x6533, x6531);
SymVal x6536 = x6535 ? Concrete(x6534, 32) : x6533.add(x6531);
SymStack.push(x6536);
}
{
Num x6537 = Stack.pop();
SymStack.pop();
Num x6538 = I32V(Memory.loadInt(x6537.toInt(), 8));
SymVal x6539 = SymMemory.loadSym(x6537.toInt(), 8);
Stack.push(x6538);
SymStack.push(x6539);
}
{
Num x6540 = Stack.pop();
SymStack.pop();
Num x6541 = I32V(Memory.loadInt(x6540.toInt(), 4));
SymVal x6542 = SymMemory.loadSym(x6540.toInt(), 4);
Stack.push(x6541);
SymStack.push(x6542);
}
{
Num x6543 = Stack.pop();
SymVal x6544 = SymStack.pop();
Num x6545 = Stack.pop();
SymVal x6546 = SymStack.pop();
Num x6547 = x6545.i32_eq(x6543);
Stack.push(x6547);
bool x6548 = allConcrete(x6546, x6544);
SymVal x6549 = x6548 ? Concrete(x6547, 32) : x6546.eq(x6544).bool2bv();
SymStack.push(x6549);
}
Num x6550 = Stack.pop();
{
SymVal x6551 = SymStack.pop();
ExploreTree.fillIfElseNode(x6551, 41);
}
int x6552 = x6550.toInt();
if (x6552 != 0) {
ExploreTree.moveCursor(true, makeControl(x5815, CURRENT_MCONT));
__attribute__((musttail)) return x6489(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x6489, CURRENT_MCONT));
__attribute__((musttail)) return x5815(std::monostate{});
}
return std::monostate{};
}
std::monostate x6489(std::monostate x6490) {
info("Entering the true branch 41 of the if");
info("Jump to 2");
__attribute__((musttail)) return x6402(std::monostate{});
return std::monostate{};
}
std::monostate x6402(std::monostate x6403) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x6404 = Stack.pop();
SymStack.pop();
Num x6405 = I32V(Memory.loadInt(x6404.toInt(), 4));
SymVal x6406 = SymMemory.loadSym(x6404.toInt(), 4);
Stack.push(x6405);
SymStack.push(x6406);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6407 = Stack.pop();
SymVal x6408 = SymStack.pop();
Num x6409 = Stack.pop();
SymVal x6410 = SymStack.pop();
Num x6411 = x6409.i32_add(x6407);
Stack.push(x6411);
bool x6412 = allConcrete(x6410, x6408);
SymVal x6413 = x6412 ? Concrete(x6411, 32) : x6410.add(x6408);
SymStack.push(x6413);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x6414 = Stack.pop();
SymStack.pop();
Num x6415 = I32V(Memory.loadInt(x6414.toInt(), 0));
SymVal x6416 = SymMemory.loadSym(x6414.toInt(), 0);
Stack.push(x6415);
SymStack.push(x6416);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6417 = Stack.pop();
SymVal x6418 = SymStack.pop();
Num x6419 = Stack.pop();
SymVal x6420 = SymStack.pop();
Num x6421 = x6419.i32_sub(x6417);
Stack.push(x6421);
bool x6422 = allConcrete(x6420, x6418);
SymVal x6423 = x6422 ? Concrete(x6421, 32) : x6420.minus(x6418);
SymStack.push(x6423);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6424 = Stack.pop();
SymVal x6425 = SymStack.pop();
Num x6426 = Stack.pop();
SymVal x6427 = SymStack.pop();
Num x6428 = x6426.i32_mul(x6424);
Stack.push(x6428);
bool x6429 = allConcrete(x6427, x6425);
SymVal x6430 = x6429 ? Concrete(x6428, 32) : x6427.mul(x6425);
SymStack.push(x6430);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6431 = Stack.pop();
SymVal x6432 = SymStack.pop();
Num x6433 = Stack.pop();
SymVal x6434 = SymStack.pop();
Num x6435 = x6433.i32_add(x6431);
Stack.push(x6435);
bool x6436 = allConcrete(x6434, x6432);
SymVal x6437 = x6436 ? Concrete(x6435, 32) : x6434.add(x6432);
SymStack.push(x6437);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6438 = Stack.pop();
SymVal x6439 = SymStack.pop();
Num x6440 = Stack.pop();
SymVal x6441 = SymStack.pop();
Num x6442 = x6440.i32_mul(x6438);
Stack.push(x6442);
bool x6443 = allConcrete(x6441, x6439);
SymVal x6444 = x6443 ? Concrete(x6442, 32) : x6441.mul(x6439);
SymStack.push(x6444);
}
{
Num x6445 = Stack.pop();
SymVal x6446 = SymStack.pop();
Num x6447 = Stack.pop();
SymVal x6448 = SymStack.pop();
Num x6449 = x6447.i32_add(x6445);
Stack.push(x6449);
bool x6450 = allConcrete(x6448, x6446);
SymVal x6451 = x6450 ? Concrete(x6449, 32) : x6448.add(x6446);
SymStack.push(x6451);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x6452 = Stack.pop();
SymVal x6453 = SymStack.pop();
Num x6454 = Stack.pop();
SymVal x6455 = SymStack.pop();
Num x6456 = x6454.i32_add(x6452);
Stack.push(x6456);
bool x6457 = allConcrete(x6455, x6453);
SymVal x6458 = x6457 ? Concrete(x6456, 32) : x6455.add(x6453);
SymStack.push(x6458);
}
{
Num x6459 = Stack.pop();
SymStack.pop();
Num x6460 = I32V(Memory.loadInt(x6459.toInt(), 8));
SymVal x6461 = SymMemory.loadSym(x6459.toInt(), 8);
Stack.push(x6460);
SymStack.push(x6461);
}
{
Num x6462 = Stack.pop();
SymStack.pop();
Num x6463 = I32V(Memory.loadInt(x6462.toInt(), 4));
SymVal x6464 = SymMemory.loadSym(x6462.toInt(), 4);
Stack.push(x6463);
SymStack.push(x6464);
}
{
Num x6465 = Stack.pop();
SymVal x6466 = SymStack.pop();
Num x6467 = Stack.pop();
SymVal x6468 = SymStack.pop();
Num x6469 = x6467.i32_add(x6465);
Stack.push(x6469);
bool x6470 = allConcrete(x6468, x6466);
SymVal x6471 = x6470 ? Concrete(x6469, 32) : x6468.add(x6466);
SymStack.push(x6471);
}
{
Num x6472 = Stack.pop();
SymVal x6473 = SymStack.pop();
Num x6474 = Stack.pop();
SymStack.pop();
int x6475 = x6474.toInt();
Memory.storeInt(x6475, 4, x6472.toInt());
SymMemory.storeSym(x6475, 4, x6473);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x6476 = Stack.pop();
SymStack.pop();
Num x6477 = I32V(Memory.loadInt(x6476.toInt(), 0));
SymVal x6478 = SymMemory.loadSym(x6476.toInt(), 0);
Stack.push(x6477);
SymStack.push(x6478);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6479 = Stack.pop();
SymVal x6480 = SymStack.pop();
Num x6481 = Stack.pop();
SymVal x6482 = SymStack.pop();
Num x6483 = x6481.i32_ne(x6479);
Stack.push(x6483);
bool x6484 = allConcrete(x6482, x6480);
SymVal x6485 = x6484 ? Concrete(x6483, 32) : x6482.neq(x6480).bool2bv();
SymStack.push(x6485);
}
Num x6486 = Stack.pop();
{
SymVal x6487 = SymStack.pop();
ExploreTree.fillIfElseNode(x6487, 42);
}
int x6488 = x6486.toInt();
if (x6488 != 0) {
ExploreTree.moveCursor(true, makeControl(x6163, CURRENT_MCONT));
__attribute__((musttail)) return x6398(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x6398, CURRENT_MCONT));
__attribute__((musttail)) return x6163(std::monostate{});
}
return std::monostate{};
}
std::monostate x6398(std::monostate x6399) {
info("Entering the true branch 42 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x6400 = Stack.pop();
SymVal x6401 = SymStack.pop();
Frames.set(3, x6400);
SymFrames.set(3, x6401);
}
__attribute__((musttail)) return x6324(std::monostate{});
return std::monostate{};
}
std::monostate x6324(std::monostate x6329) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x6330 = Stack.pop();
SymStack.pop();
Num x6331 = I32V(Memory.loadInt(x6330.toInt(), 0));
SymVal x6332 = SymMemory.loadSym(x6330.toInt(), 0);
Stack.push(x6331);
SymStack.push(x6332);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6333 = Stack.pop();
SymVal x6334 = SymStack.pop();
Num x6335 = Stack.pop();
SymVal x6336 = SymStack.pop();
Num x6337 = x6335.i32_sub(x6333);
Stack.push(x6337);
bool x6338 = allConcrete(x6336, x6334);
SymVal x6339 = x6338 ? Concrete(x6337, 32) : x6336.minus(x6334);
SymStack.push(x6339);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6340 = Stack.pop();
SymVal x6341 = SymStack.pop();
Num x6342 = Stack.pop();
SymVal x6343 = SymStack.pop();
Num x6344 = x6342.i32_mul(x6340);
Stack.push(x6344);
bool x6345 = allConcrete(x6343, x6341);
SymVal x6346 = x6345 ? Concrete(x6344, 32) : x6343.mul(x6341);
SymStack.push(x6346);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6347 = Stack.pop();
SymVal x6348 = SymStack.pop();
Num x6349 = Stack.pop();
SymVal x6350 = SymStack.pop();
Num x6351 = x6349.i32_add(x6347);
Stack.push(x6351);
bool x6352 = allConcrete(x6350, x6348);
SymVal x6353 = x6352 ? Concrete(x6351, 32) : x6350.add(x6348);
SymStack.push(x6353);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6354 = Stack.pop();
SymVal x6355 = SymStack.pop();
Num x6356 = Stack.pop();
SymVal x6357 = SymStack.pop();
Num x6358 = x6356.i32_mul(x6354);
Stack.push(x6358);
bool x6359 = allConcrete(x6357, x6355);
SymVal x6360 = x6359 ? Concrete(x6358, 32) : x6357.mul(x6355);
SymStack.push(x6360);
}
{
Num x6361 = Stack.pop();
SymVal x6362 = SymStack.pop();
Num x6363 = Stack.pop();
SymVal x6364 = SymStack.pop();
Num x6365 = x6363.i32_add(x6361);
Stack.push(x6365);
bool x6366 = allConcrete(x6364, x6362);
SymVal x6367 = x6366 ? Concrete(x6365, 32) : x6364.add(x6362);
SymStack.push(x6367);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x6368 = Stack.pop();
SymVal x6369 = SymStack.pop();
Num x6370 = Stack.pop();
SymVal x6371 = SymStack.pop();
Num x6372 = x6370.i32_add(x6368);
Stack.push(x6372);
bool x6373 = allConcrete(x6371, x6369);
SymVal x6374 = x6373 ? Concrete(x6372, 32) : x6371.add(x6369);
SymStack.push(x6374);
}
{
Num x6375 = Stack.pop();
SymStack.pop();
Num x6376 = I32V(Memory.loadInt(x6375.toInt(), 8));
SymVal x6377 = SymMemory.loadSym(x6375.toInt(), 8);
Stack.push(x6376);
SymStack.push(x6377);
}
{
Num x6378 = Stack.pop();
SymStack.pop();
Num x6379 = I32V(Memory.loadInt(x6378.toInt(), 4));
SymVal x6380 = SymMemory.loadSym(x6378.toInt(), 4);
Stack.push(x6379);
SymStack.push(x6380);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6381 = Stack.pop();
SymVal x6382 = SymStack.pop();
Num x6383 = Stack.pop();
SymVal x6384 = SymStack.pop();
Num x6385 = x6383.i32_add(x6381);
Stack.push(x6385);
bool x6386 = allConcrete(x6384, x6382);
SymVal x6387 = x6386 ? Concrete(x6385, 32) : x6384.add(x6382);
SymStack.push(x6387);
}
{
Num x6388 = Stack.pop();
SymVal x6389 = SymStack.pop();
Num x6390 = Stack.pop();
SymVal x6391 = SymStack.pop();
Num x6392 = x6390.i32_eq(x6388);
Stack.push(x6392);
bool x6393 = allConcrete(x6391, x6389);
SymVal x6394 = x6393 ? Concrete(x6392, 32) : x6391.eq(x6389).bool2bv();
SymStack.push(x6394);
}
Num x6395 = Stack.pop();
{
SymVal x6396 = SymStack.pop();
ExploreTree.fillIfElseNode(x6396, 43);
}
int x6397 = x6395.toInt();
if (x6397 != 0) {
ExploreTree.moveCursor(true, makeControl(x6165, CURRENT_MCONT));
__attribute__((musttail)) return x6327(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x6327, CURRENT_MCONT));
__attribute__((musttail)) return x6165(std::monostate{});
}
return std::monostate{};
}
std::monostate x6327(std::monostate x6328) {
info("Entering the true branch 43 of the if");
info("Jump to 2");
__attribute__((musttail)) return x6325(std::monostate{});
return std::monostate{};
}
std::monostate x6325(std::monostate x6326) {
info("Exiting the block, stackSize =", Stack.size());
__attribute__((musttail)) return x6159(std::monostate{});
return std::monostate{};
}
std::monostate x6165(std::monostate x6166) {
info("Entering the false branch 43 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x6167 = Stack.pop();
SymStack.pop();
Num x6168 = I32V(Memory.loadInt(x6167.toInt(), 0));
SymVal x6169 = SymMemory.loadSym(x6167.toInt(), 0);
Stack.push(x6168);
SymStack.push(x6169);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6170 = Stack.pop();
SymVal x6171 = SymStack.pop();
Num x6172 = Stack.pop();
SymVal x6173 = SymStack.pop();
Num x6174 = x6172.i32_sub(x6170);
Stack.push(x6174);
bool x6175 = allConcrete(x6173, x6171);
SymVal x6176 = x6175 ? Concrete(x6174, 32) : x6173.minus(x6171);
SymStack.push(x6176);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6177 = Stack.pop();
SymVal x6178 = SymStack.pop();
Num x6179 = Stack.pop();
SymVal x6180 = SymStack.pop();
Num x6181 = x6179.i32_mul(x6177);
Stack.push(x6181);
bool x6182 = allConcrete(x6180, x6178);
SymVal x6183 = x6182 ? Concrete(x6181, 32) : x6180.mul(x6178);
SymStack.push(x6183);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x6184 = Stack.pop();
SymStack.pop();
Num x6185 = I32V(Memory.loadInt(x6184.toInt(), 4));
SymVal x6186 = SymMemory.loadSym(x6184.toInt(), 4);
Stack.push(x6185);
SymStack.push(x6186);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x6187 = Stack.pop();
SymVal x6188 = SymStack.pop();
Num x6189 = Stack.pop();
SymVal x6190 = SymStack.pop();
Num x6191 = x6189.i32_add(x6187);
Stack.push(x6191);
bool x6192 = allConcrete(x6190, x6188);
SymVal x6193 = x6192 ? Concrete(x6191, 32) : x6190.add(x6188);
SymStack.push(x6193);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6194 = Stack.pop();
SymVal x6195 = SymStack.pop();
Num x6196 = Stack.pop();
SymVal x6197 = SymStack.pop();
Num x6198 = x6196.i32_add(x6194);
Stack.push(x6198);
bool x6199 = allConcrete(x6197, x6195);
SymVal x6200 = x6199 ? Concrete(x6198, 32) : x6197.add(x6195);
SymStack.push(x6200);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6201 = Stack.pop();
SymVal x6202 = SymStack.pop();
Num x6203 = Stack.pop();
SymVal x6204 = SymStack.pop();
Num x6205 = x6203.i32_mul(x6201);
Stack.push(x6205);
bool x6206 = allConcrete(x6204, x6202);
SymVal x6207 = x6206 ? Concrete(x6205, 32) : x6204.mul(x6202);
SymStack.push(x6207);
}
{
Num x6208 = Stack.pop();
SymVal x6209 = SymStack.pop();
Num x6210 = Stack.pop();
SymVal x6211 = SymStack.pop();
Num x6212 = x6210.i32_add(x6208);
Stack.push(x6212);
bool x6213 = allConcrete(x6211, x6209);
SymVal x6214 = x6213 ? Concrete(x6212, 32) : x6211.add(x6209);
SymStack.push(x6214);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x6215 = Stack.pop();
SymVal x6216 = SymStack.pop();
Num x6217 = Stack.pop();
SymVal x6218 = SymStack.pop();
Num x6219 = x6217.i32_add(x6215);
Stack.push(x6219);
bool x6220 = allConcrete(x6218, x6216);
SymVal x6221 = x6220 ? Concrete(x6219, 32) : x6218.add(x6216);
SymStack.push(x6221);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x6222 = Stack.pop();
SymStack.pop();
Num x6223 = I32V(Memory.loadInt(x6222.toInt(), 0));
SymVal x6224 = SymMemory.loadSym(x6222.toInt(), 0);
Stack.push(x6223);
SymStack.push(x6224);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6225 = Stack.pop();
SymVal x6226 = SymStack.pop();
Num x6227 = Stack.pop();
SymVal x6228 = SymStack.pop();
Num x6229 = x6227.i32_sub(x6225);
Stack.push(x6229);
bool x6230 = allConcrete(x6228, x6226);
SymVal x6231 = x6230 ? Concrete(x6229, 32) : x6228.minus(x6226);
SymStack.push(x6231);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6232 = Stack.pop();
SymVal x6233 = SymStack.pop();
Num x6234 = Stack.pop();
SymVal x6235 = SymStack.pop();
Num x6236 = x6234.i32_mul(x6232);
Stack.push(x6236);
bool x6237 = allConcrete(x6235, x6233);
SymVal x6238 = x6237 ? Concrete(x6236, 32) : x6235.mul(x6233);
SymStack.push(x6238);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6239 = Stack.pop();
SymVal x6240 = SymStack.pop();
Num x6241 = Stack.pop();
SymVal x6242 = SymStack.pop();
Num x6243 = x6241.i32_mul(x6239);
Stack.push(x6243);
bool x6244 = allConcrete(x6242, x6240);
SymVal x6245 = x6244 ? Concrete(x6243, 32) : x6242.mul(x6240);
SymStack.push(x6245);
}
{
Num x6246 = Stack.pop();
SymVal x6247 = SymStack.pop();
Num x6248 = Stack.pop();
SymVal x6249 = SymStack.pop();
Num x6250 = x6248.i32_add(x6246);
Stack.push(x6250);
bool x6251 = allConcrete(x6249, x6247);
SymVal x6252 = x6251 ? Concrete(x6250, 32) : x6249.add(x6247);
SymStack.push(x6252);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x6253 = Stack.pop();
SymStack.pop();
Num x6254 = I32V(Memory.loadInt(x6253.toInt(), 0));
SymVal x6255 = SymMemory.loadSym(x6253.toInt(), 0);
Stack.push(x6254);
SymStack.push(x6255);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6256 = Stack.pop();
SymVal x6257 = SymStack.pop();
Num x6258 = Stack.pop();
SymVal x6259 = SymStack.pop();
Num x6260 = x6258.i32_sub(x6256);
Stack.push(x6260);
bool x6261 = allConcrete(x6259, x6257);
SymVal x6262 = x6261 ? Concrete(x6260, 32) : x6259.minus(x6257);
SymStack.push(x6262);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6263 = Stack.pop();
SymVal x6264 = SymStack.pop();
Num x6265 = Stack.pop();
SymVal x6266 = SymStack.pop();
Num x6267 = x6265.i32_mul(x6263);
Stack.push(x6267);
bool x6268 = allConcrete(x6266, x6264);
SymVal x6269 = x6268 ? Concrete(x6267, 32) : x6266.mul(x6264);
SymStack.push(x6269);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6270 = Stack.pop();
SymVal x6271 = SymStack.pop();
Num x6272 = Stack.pop();
SymVal x6273 = SymStack.pop();
Num x6274 = x6272.i32_add(x6270);
Stack.push(x6274);
bool x6275 = allConcrete(x6273, x6271);
SymVal x6276 = x6275 ? Concrete(x6274, 32) : x6273.add(x6271);
SymStack.push(x6276);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6277 = Stack.pop();
SymVal x6278 = SymStack.pop();
Num x6279 = Stack.pop();
SymVal x6280 = SymStack.pop();
Num x6281 = x6279.i32_mul(x6277);
Stack.push(x6281);
bool x6282 = allConcrete(x6280, x6278);
SymVal x6283 = x6282 ? Concrete(x6281, 32) : x6280.mul(x6278);
SymStack.push(x6283);
}
{
Num x6284 = Stack.pop();
SymVal x6285 = SymStack.pop();
Num x6286 = Stack.pop();
SymVal x6287 = SymStack.pop();
Num x6288 = x6286.i32_add(x6284);
Stack.push(x6288);
bool x6289 = allConcrete(x6287, x6285);
SymVal x6290 = x6289 ? Concrete(x6288, 32) : x6287.add(x6285);
SymStack.push(x6290);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x6291 = Stack.pop();
SymVal x6292 = SymStack.pop();
Num x6293 = Stack.pop();
SymVal x6294 = SymStack.pop();
Num x6295 = x6293.i32_add(x6291);
Stack.push(x6295);
bool x6296 = allConcrete(x6294, x6292);
SymVal x6297 = x6296 ? Concrete(x6295, 32) : x6294.add(x6292);
SymStack.push(x6297);
}
{
Num x6298 = Stack.pop();
SymStack.pop();
Num x6299 = I32V(Memory.loadInt(x6298.toInt(), 8));
SymVal x6300 = SymMemory.loadSym(x6298.toInt(), 8);
Stack.push(x6299);
SymStack.push(x6300);
}
{
Num x6301 = Stack.pop();
SymVal x6302 = SymStack.pop();
Num x6303 = Stack.pop();
SymVal x6304 = SymStack.pop();
Num x6305 = x6303.i32_add(x6301);
Stack.push(x6305);
bool x6306 = allConcrete(x6304, x6302);
SymVal x6307 = x6306 ? Concrete(x6305, 32) : x6304.add(x6302);
SymStack.push(x6307);
}
{
Num x6308 = Stack.pop();
SymStack.pop();
Num x6309 = I32V(Memory.loadInt(x6308.toInt(), 8));
SymVal x6310 = SymMemory.loadSym(x6308.toInt(), 8);
Stack.push(x6309);
SymStack.push(x6310);
}
{
Num x6311 = Stack.pop();
SymVal x6312 = SymStack.pop();
Num x6313 = Stack.pop();
SymStack.pop();
int x6314 = x6313.toInt();
Memory.storeInt(x6314, 8, x6311.toInt());
SymMemory.storeSym(x6314, 8, x6312);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6315 = Stack.pop();
SymVal x6316 = SymStack.pop();
Num x6317 = Stack.pop();
SymVal x6318 = SymStack.pop();
Num x6319 = x6317.i32_add(x6315);
Stack.push(x6319);
bool x6320 = allConcrete(x6318, x6316);
SymVal x6321 = x6320 ? Concrete(x6319, 32) : x6318.add(x6316);
SymStack.push(x6321);
}
{
Num x6322 = Stack.pop();
SymVal x6323 = SymStack.pop();
Frames.set(3, x6322);
SymFrames.set(3, x6323);
}
info("Jump to 1");
__attribute__((musttail)) return x6324(std::monostate{});
return std::monostate{};
}
std::monostate x6163(std::monostate x6164) {
info("Entering the false branch 42 of the if");
__attribute__((musttail)) return x6159(std::monostate{});
return std::monostate{};
}
std::monostate x6159(std::monostate x6160) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x6161 = Stack.pop();
SymVal x6162 = SymStack.pop();
Frames.set(3, x6161);
SymFrames.set(3, x6162);
}
__attribute__((musttail)) return x5980(std::monostate{});
return std::monostate{};
}
std::monostate x5980(std::monostate x6138) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x6139 = Stack.pop();
SymStack.pop();
Num x6140 = I32V(Memory.loadInt(x6139.toInt(), 4));
SymVal x6141 = SymMemory.loadSym(x6139.toInt(), 4);
Stack.push(x6140);
SymStack.push(x6141);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6142 = Stack.pop();
SymVal x6143 = SymStack.pop();
Num x6144 = Stack.pop();
SymVal x6145 = SymStack.pop();
Num x6146 = x6144.i32_sub(x6142);
Stack.push(x6146);
bool x6147 = allConcrete(x6145, x6143);
SymVal x6148 = x6147 ? Concrete(x6146, 32) : x6145.minus(x6143);
SymStack.push(x6148);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x6149 = Stack.pop();
SymVal x6150 = SymStack.pop();
Num x6151 = Stack.pop();
SymVal x6152 = SymStack.pop();
Num x6153 = x6151.i32_eq(x6149);
Stack.push(x6153);
bool x6154 = allConcrete(x6152, x6150);
SymVal x6155 = x6154 ? Concrete(x6153, 32) : x6152.eq(x6150).bool2bv();
SymStack.push(x6155);
}
Num x6156 = Stack.pop();
{
SymVal x6157 = SymStack.pop();
ExploreTree.fillIfElseNode(x6157, 39);
}
int x6158 = x6156.toInt();
if (x6158 != 0) {
ExploreTree.moveCursor(true, makeControl(x5927, CURRENT_MCONT));
__attribute__((musttail)) return x6136(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x6136, CURRENT_MCONT));
__attribute__((musttail)) return x5927(std::monostate{});
}
return std::monostate{};
}
std::monostate x6136(std::monostate x6137) {
info("Entering the true branch 39 of the if");
info("Jump to 2");
__attribute__((musttail)) return x6125(std::monostate{});
return std::monostate{};
}
std::monostate x6125(std::monostate x6126) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6127 = Stack.pop();
SymVal x6128 = SymStack.pop();
Num x6129 = Stack.pop();
SymVal x6130 = SymStack.pop();
Num x6131 = x6129.i32_add(x6127);
Stack.push(x6131);
bool x6132 = allConcrete(x6130, x6128);
SymVal x6133 = x6132 ? Concrete(x6131, 32) : x6130.add(x6128);
SymStack.push(x6133);
}
{
Num x6134 = Stack.pop();
SymVal x6135 = SymStack.pop();
Frames.set(3, x6134);
SymFrames.set(3, x6135);
}
__attribute__((musttail)) return x6082(std::monostate{});
return std::monostate{};
}
std::monostate x6082(std::monostate x6111) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x6112 = Stack.pop();
SymStack.pop();
Num x6113 = I32V(Memory.loadInt(x6112.toInt(), 4));
SymVal x6114 = SymMemory.loadSym(x6112.toInt(), 4);
Stack.push(x6113);
SymStack.push(x6114);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x6115 = Stack.pop();
SymVal x6116 = SymStack.pop();
Num x6117 = Stack.pop();
SymVal x6118 = SymStack.pop();
Num x6119 = x6117.i32_eq(x6115);
Stack.push(x6119);
bool x6120 = allConcrete(x6118, x6116);
SymVal x6121 = x6120 ? Concrete(x6119, 32) : x6118.eq(x6116).bool2bv();
SymStack.push(x6121);
}
Num x6122 = Stack.pop();
{
SymVal x6123 = SymStack.pop();
ExploreTree.fillIfElseNode(x6123, 38);
}
int x6124 = x6122.toInt();
if (x6124 != 0) {
ExploreTree.moveCursor(true, makeControl(x5981, CURRENT_MCONT));
__attribute__((musttail)) return x6109(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x6109, CURRENT_MCONT));
__attribute__((musttail)) return x5981(std::monostate{});
}
return std::monostate{};
}
std::monostate x6109(std::monostate x6110) {
info("Entering the true branch 38 of the if");
info("Jump to 2");
__attribute__((musttail)) return x6089(std::monostate{});
return std::monostate{};
}
std::monostate x6089(std::monostate x6090) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x6091 = Stack.pop();
SymStack.pop();
Num x6092 = I32V(Memory.loadInt(x6091.toInt(), 4));
SymVal x6093 = SymMemory.loadSym(x6091.toInt(), 4);
Stack.push(x6092);
SymStack.push(x6093);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6094 = Stack.pop();
SymVal x6095 = SymStack.pop();
Num x6096 = Stack.pop();
SymVal x6097 = SymStack.pop();
Num x6098 = x6096.i32_sub(x6094);
Stack.push(x6098);
bool x6099 = allConcrete(x6097, x6095);
SymVal x6100 = x6099 ? Concrete(x6098, 32) : x6097.minus(x6095);
SymStack.push(x6100);
}
{
Num x6101 = Stack.pop();
SymVal x6102 = SymStack.pop();
Num x6103 = Stack.pop();
SymStack.pop();
int x6104 = x6103.toInt();
Memory.storeInt(x6104, 4, x6101.toInt());
SymMemory.storeSym(x6104, 4, x6102);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 12);
Num x6105 = Stack.pop();
Num x6106 = Stack.pop();
SymVal x6107 = SymStack.pop();
SymVal x6108 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x6106);
Frames.set(1, x6105);
SymFrames.set(0, x6108);
SymFrames.set(1, x6107);
updateCurrentMCont(prependCont(x6087, CURRENT_MCONT));
}
__attribute__((musttail)) return x1996(std::monostate{});
return std::monostate{};
}
std::monostate x6087(std::monostate x6088) {
infoWhen("CALL", "Returning from the function at 12, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.pop();
SymStack.pop();
__attribute__((musttail)) return x6085(std::monostate{});
return std::monostate{};
}
std::monostate x6085(std::monostate x6086) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x6083(std::monostate{});
return std::monostate{};
}
std::monostate x6083(std::monostate x6084) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x1984(std::monostate{});
return std::monostate{};
}
std::monostate x5981(std::monostate x5982) {
info("Entering the false branch 38 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5983 = Stack.pop();
SymStack.pop();
Num x5984 = I32V(Memory.loadInt(x5983.toInt(), 0));
SymVal x5985 = SymMemory.loadSym(x5983.toInt(), 0);
Stack.push(x5984);
SymStack.push(x5985);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5986 = Stack.pop();
SymVal x5987 = SymStack.pop();
Num x5988 = Stack.pop();
SymVal x5989 = SymStack.pop();
Num x5990 = x5988.i32_sub(x5986);
Stack.push(x5990);
bool x5991 = allConcrete(x5989, x5987);
SymVal x5992 = x5991 ? Concrete(x5990, 32) : x5989.minus(x5987);
SymStack.push(x5992);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5993 = Stack.pop();
SymVal x5994 = SymStack.pop();
Num x5995 = Stack.pop();
SymVal x5996 = SymStack.pop();
Num x5997 = x5995.i32_mul(x5993);
Stack.push(x5997);
bool x5998 = allConcrete(x5996, x5994);
SymVal x5999 = x5998 ? Concrete(x5997, 32) : x5996.mul(x5994);
SymStack.push(x5999);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6000 = Stack.pop();
SymVal x6001 = SymStack.pop();
Num x6002 = Stack.pop();
SymVal x6003 = SymStack.pop();
Num x6004 = x6002.i32_mul(x6000);
Stack.push(x6004);
bool x6005 = allConcrete(x6003, x6001);
SymVal x6006 = x6005 ? Concrete(x6004, 32) : x6003.mul(x6001);
SymStack.push(x6006);
}
{
Num x6007 = Stack.pop();
SymVal x6008 = SymStack.pop();
Num x6009 = Stack.pop();
SymVal x6010 = SymStack.pop();
Num x6011 = x6009.i32_add(x6007);
Stack.push(x6011);
bool x6012 = allConcrete(x6010, x6008);
SymVal x6013 = x6012 ? Concrete(x6011, 32) : x6010.add(x6008);
SymStack.push(x6013);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x6014 = Stack.pop();
SymVal x6015 = SymStack.pop();
Num x6016 = Stack.pop();
SymVal x6017 = SymStack.pop();
Num x6018 = x6016.i32_add(x6014);
Stack.push(x6018);
bool x6019 = allConcrete(x6017, x6015);
SymVal x6020 = x6019 ? Concrete(x6018, 32) : x6017.add(x6015);
SymStack.push(x6020);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x6021 = Stack.pop();
SymStack.pop();
Num x6022 = I32V(Memory.loadInt(x6021.toInt(), 0));
SymVal x6023 = SymMemory.loadSym(x6021.toInt(), 0);
Stack.push(x6022);
SymStack.push(x6023);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6024 = Stack.pop();
SymVal x6025 = SymStack.pop();
Num x6026 = Stack.pop();
SymVal x6027 = SymStack.pop();
Num x6028 = x6026.i32_sub(x6024);
Stack.push(x6028);
bool x6029 = allConcrete(x6027, x6025);
SymVal x6030 = x6029 ? Concrete(x6028, 32) : x6027.minus(x6025);
SymStack.push(x6030);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6031 = Stack.pop();
SymVal x6032 = SymStack.pop();
Num x6033 = Stack.pop();
SymVal x6034 = SymStack.pop();
Num x6035 = x6033.i32_mul(x6031);
Stack.push(x6035);
bool x6036 = allConcrete(x6034, x6032);
SymVal x6037 = x6036 ? Concrete(x6035, 32) : x6034.mul(x6032);
SymStack.push(x6037);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6038 = Stack.pop();
SymVal x6039 = SymStack.pop();
Num x6040 = Stack.pop();
SymVal x6041 = SymStack.pop();
Num x6042 = x6040.i32_add(x6038);
Stack.push(x6042);
bool x6043 = allConcrete(x6041, x6039);
SymVal x6044 = x6043 ? Concrete(x6042, 32) : x6041.add(x6039);
SymStack.push(x6044);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6045 = Stack.pop();
SymVal x6046 = SymStack.pop();
Num x6047 = Stack.pop();
SymVal x6048 = SymStack.pop();
Num x6049 = x6047.i32_mul(x6045);
Stack.push(x6049);
bool x6050 = allConcrete(x6048, x6046);
SymVal x6051 = x6050 ? Concrete(x6049, 32) : x6048.mul(x6046);
SymStack.push(x6051);
}
{
Num x6052 = Stack.pop();
SymVal x6053 = SymStack.pop();
Num x6054 = Stack.pop();
SymVal x6055 = SymStack.pop();
Num x6056 = x6054.i32_add(x6052);
Stack.push(x6056);
bool x6057 = allConcrete(x6055, x6053);
SymVal x6058 = x6057 ? Concrete(x6056, 32) : x6055.add(x6053);
SymStack.push(x6058);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x6059 = Stack.pop();
SymVal x6060 = SymStack.pop();
Num x6061 = Stack.pop();
SymVal x6062 = SymStack.pop();
Num x6063 = x6061.i32_add(x6059);
Stack.push(x6063);
bool x6064 = allConcrete(x6062, x6060);
SymVal x6065 = x6064 ? Concrete(x6063, 32) : x6062.add(x6060);
SymStack.push(x6065);
}
{
Num x6066 = Stack.pop();
SymStack.pop();
Num x6067 = I32V(Memory.loadInt(x6066.toInt(), 8));
SymVal x6068 = SymMemory.loadSym(x6066.toInt(), 8);
Stack.push(x6067);
SymStack.push(x6068);
}
{
Num x6069 = Stack.pop();
SymVal x6070 = SymStack.pop();
Num x6071 = Stack.pop();
SymStack.pop();
int x6072 = x6071.toInt();
Memory.storeInt(x6072, 8, x6069.toInt());
SymMemory.storeSym(x6072, 8, x6070);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6073 = Stack.pop();
SymVal x6074 = SymStack.pop();
Num x6075 = Stack.pop();
SymVal x6076 = SymStack.pop();
Num x6077 = x6075.i32_add(x6073);
Stack.push(x6077);
bool x6078 = allConcrete(x6076, x6074);
SymVal x6079 = x6078 ? Concrete(x6077, 32) : x6076.add(x6074);
SymStack.push(x6079);
}
{
Num x6080 = Stack.pop();
SymVal x6081 = SymStack.pop();
Frames.set(3, x6080);
SymFrames.set(3, x6081);
}
info("Jump to 1");
__attribute__((musttail)) return x6082(std::monostate{});
return std::monostate{};
}
std::monostate x5927(std::monostate x5928) {
info("Entering the false branch 39 of the if");
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x5929 = Stack.pop();
SymVal x5930 = SymStack.pop();
Num x5931 = Stack.pop();
SymVal x5932 = SymStack.pop();
Num x5933 = x5931.i32_mul(x5929);
Stack.push(x5933);
bool x5934 = allConcrete(x5932, x5930);
SymVal x5935 = x5934 ? Concrete(x5933, 32) : x5932.mul(x5930);
SymStack.push(x5935);
}
{
Num x5936 = Stack.pop();
SymVal x5937 = SymStack.pop();
Num x5938 = Stack.pop();
SymVal x5939 = SymStack.pop();
Num x5940 = x5938.i32_add(x5936);
Stack.push(x5940);
bool x5941 = allConcrete(x5939, x5937);
SymVal x5942 = x5941 ? Concrete(x5940, 32) : x5939.add(x5937);
SymStack.push(x5942);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5943 = Stack.pop();
SymVal x5944 = SymStack.pop();
Num x5945 = Stack.pop();
SymVal x5946 = SymStack.pop();
Num x5947 = x5945.i32_add(x5943);
Stack.push(x5947);
bool x5948 = allConcrete(x5946, x5944);
SymVal x5949 = x5948 ? Concrete(x5947, 32) : x5946.add(x5944);
SymStack.push(x5949);
}
{
Num x5950 = Stack.pop();
SymVal x5951 = SymStack.pop();
Num x5952 = Stack.pop();
SymVal x5953 = SymStack.pop();
Num x5954 = x5952.i32_mul(x5950);
Stack.push(x5954);
bool x5955 = allConcrete(x5953, x5951);
SymVal x5956 = x5955 ? Concrete(x5954, 32) : x5953.mul(x5951);
SymStack.push(x5956);
}
{
Num x5957 = Stack.pop();
SymVal x5958 = SymStack.pop();
Num x5959 = Stack.pop();
SymVal x5960 = SymStack.pop();
Num x5961 = x5959.i32_add(x5957);
Stack.push(x5961);
bool x5962 = allConcrete(x5960, x5958);
SymVal x5963 = x5962 ? Concrete(x5961, 32) : x5960.add(x5958);
SymStack.push(x5963);
}
{
Num x5964 = Stack.pop();
SymStack.pop();
Num x5965 = I32V(Memory.loadInt(x5964.toInt(), 8));
SymVal x5966 = SymMemory.loadSym(x5964.toInt(), 8);
Stack.push(x5965);
SymStack.push(x5966);
}
{
Num x5967 = Stack.pop();
SymVal x5968 = SymStack.pop();
Num x5969 = Stack.pop();
SymStack.pop();
int x5970 = x5969.toInt();
Memory.storeInt(x5970, 8, x5967.toInt());
SymMemory.storeSym(x5970, 8, x5968);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5971 = Stack.pop();
SymVal x5972 = SymStack.pop();
Num x5973 = Stack.pop();
SymVal x5974 = SymStack.pop();
Num x5975 = x5973.i32_add(x5971);
Stack.push(x5975);
bool x5976 = allConcrete(x5974, x5972);
SymVal x5977 = x5976 ? Concrete(x5975, 32) : x5974.add(x5972);
SymStack.push(x5977);
}
{
Num x5978 = Stack.pop();
SymVal x5979 = SymStack.pop();
Frames.set(3, x5978);
SymFrames.set(3, x5979);
}
info("Jump to 1");
__attribute__((musttail)) return x5980(std::monostate{});
return std::monostate{};
}
std::monostate x5815(std::monostate x5816) {
info("Entering the false branch 41 of the if");
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x5817 = Stack.pop();
SymStack.pop();
Num x5818 = I32V(Memory.loadInt(x5817.toInt(), 4));
SymVal x5819 = SymMemory.loadSym(x5817.toInt(), 4);
Stack.push(x5818);
SymStack.push(x5819);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x5820 = Stack.pop();
SymVal x5821 = SymStack.pop();
Num x5822 = Stack.pop();
SymVal x5823 = SymStack.pop();
Num x5824 = x5822.i32_add(x5820);
Stack.push(x5824);
bool x5825 = allConcrete(x5823, x5821);
SymVal x5826 = x5825 ? Concrete(x5824, 32) : x5823.add(x5821);
SymStack.push(x5826);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5827 = Stack.pop();
SymVal x5828 = SymStack.pop();
Num x5829 = Stack.pop();
SymVal x5830 = SymStack.pop();
Num x5831 = x5829.i32_add(x5827);
Stack.push(x5831);
bool x5832 = allConcrete(x5830, x5828);
SymVal x5833 = x5832 ? Concrete(x5831, 32) : x5830.add(x5828);
SymStack.push(x5833);
}
{
Num x5834 = Stack.pop();
SymVal x5835 = SymStack.pop();
Num x5836 = Stack.pop();
SymVal x5837 = SymStack.pop();
Num x5838 = x5836.i32_mul(x5834);
Stack.push(x5838);
bool x5839 = allConcrete(x5837, x5835);
SymVal x5840 = x5839 ? Concrete(x5838, 32) : x5837.mul(x5835);
SymStack.push(x5840);
}
{
Num x5841 = Stack.pop();
SymVal x5842 = SymStack.pop();
Num x5843 = Stack.pop();
SymVal x5844 = SymStack.pop();
Num x5845 = x5843.i32_add(x5841);
Stack.push(x5845);
bool x5846 = allConcrete(x5844, x5842);
SymVal x5847 = x5846 ? Concrete(x5845, 32) : x5844.add(x5842);
SymStack.push(x5847);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5848 = Stack.pop();
SymStack.pop();
Num x5849 = I32V(Memory.loadInt(x5848.toInt(), 0));
SymVal x5850 = SymMemory.loadSym(x5848.toInt(), 0);
Stack.push(x5849);
SymStack.push(x5850);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5851 = Stack.pop();
SymVal x5852 = SymStack.pop();
Num x5853 = Stack.pop();
SymVal x5854 = SymStack.pop();
Num x5855 = x5853.i32_sub(x5851);
Stack.push(x5855);
bool x5856 = allConcrete(x5854, x5852);
SymVal x5857 = x5856 ? Concrete(x5855, 32) : x5854.minus(x5852);
SymStack.push(x5857);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5858 = Stack.pop();
SymVal x5859 = SymStack.pop();
Num x5860 = Stack.pop();
SymVal x5861 = SymStack.pop();
Num x5862 = x5860.i32_mul(x5858);
Stack.push(x5862);
bool x5863 = allConcrete(x5861, x5859);
SymVal x5864 = x5863 ? Concrete(x5862, 32) : x5861.mul(x5859);
SymStack.push(x5864);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5865 = Stack.pop();
SymVal x5866 = SymStack.pop();
Num x5867 = Stack.pop();
SymVal x5868 = SymStack.pop();
Num x5869 = x5867.i32_add(x5865);
Stack.push(x5869);
bool x5870 = allConcrete(x5868, x5866);
SymVal x5871 = x5870 ? Concrete(x5869, 32) : x5868.add(x5866);
SymStack.push(x5871);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5872 = Stack.pop();
SymVal x5873 = SymStack.pop();
Num x5874 = Stack.pop();
SymVal x5875 = SymStack.pop();
Num x5876 = x5874.i32_mul(x5872);
Stack.push(x5876);
bool x5877 = allConcrete(x5875, x5873);
SymVal x5878 = x5877 ? Concrete(x5876, 32) : x5875.mul(x5873);
SymStack.push(x5878);
}
{
Num x5879 = Stack.pop();
SymVal x5880 = SymStack.pop();
Num x5881 = Stack.pop();
SymVal x5882 = SymStack.pop();
Num x5883 = x5881.i32_add(x5879);
Stack.push(x5883);
bool x5884 = allConcrete(x5882, x5880);
SymVal x5885 = x5884 ? Concrete(x5883, 32) : x5882.add(x5880);
SymStack.push(x5885);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x5886 = Stack.pop();
SymVal x5887 = SymStack.pop();
Num x5888 = Stack.pop();
SymVal x5889 = SymStack.pop();
Num x5890 = x5888.i32_add(x5886);
Stack.push(x5890);
bool x5891 = allConcrete(x5889, x5887);
SymVal x5892 = x5891 ? Concrete(x5890, 32) : x5889.add(x5887);
SymStack.push(x5892);
}
{
Num x5893 = Stack.pop();
SymStack.pop();
Num x5894 = I32V(Memory.loadInt(x5893.toInt(), 8));
SymVal x5895 = SymMemory.loadSym(x5893.toInt(), 8);
Stack.push(x5894);
SymStack.push(x5895);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x5896 = Stack.pop();
SymVal x5897 = SymStack.pop();
Num x5898 = Stack.pop();
SymVal x5899 = SymStack.pop();
Num x5900 = x5898.i32_mul(x5896);
Stack.push(x5900);
bool x5901 = allConcrete(x5899, x5897);
SymVal x5902 = x5901 ? Concrete(x5900, 32) : x5899.mul(x5897);
SymStack.push(x5902);
}
{
Num x5903 = Stack.pop();
SymVal x5904 = SymStack.pop();
Num x5905 = Stack.pop();
SymVal x5906 = SymStack.pop();
Num x5907 = x5905.i32_add(x5903);
Stack.push(x5907);
bool x5908 = allConcrete(x5906, x5904);
SymVal x5909 = x5908 ? Concrete(x5907, 32) : x5906.add(x5904);
SymStack.push(x5909);
}
{
Num x5910 = Stack.pop();
SymStack.pop();
Num x5911 = I32V(Memory.loadInt(x5910.toInt(), 8));
SymVal x5912 = SymMemory.loadSym(x5910.toInt(), 8);
Stack.push(x5911);
SymStack.push(x5912);
}
{
Num x5913 = Stack.pop();
SymVal x5914 = SymStack.pop();
Num x5915 = Stack.pop();
SymStack.pop();
int x5916 = x5915.toInt();
Memory.storeInt(x5916, 8, x5913.toInt());
SymMemory.storeSym(x5916, 8, x5914);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5917 = Stack.pop();
SymVal x5918 = SymStack.pop();
Num x5919 = Stack.pop();
SymVal x5920 = SymStack.pop();
Num x5921 = x5919.i32_add(x5917);
Stack.push(x5921);
bool x5922 = allConcrete(x5920, x5918);
SymVal x5923 = x5922 ? Concrete(x5921, 32) : x5920.add(x5918);
SymStack.push(x5923);
}
{
Num x5924 = Stack.pop();
SymVal x5925 = SymStack.pop();
Frames.set(3, x5924);
SymFrames.set(3, x5925);
}
info("Jump to 1");
__attribute__((musttail)) return x5926(std::monostate{});
return std::monostate{};
}
std::monostate x5740(std::monostate x5741) {
info("Entering the false branch 27 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5742 = Stack.pop();
SymStack.pop();
Num x5743 = I32V(Memory.loadInt(x5742.toInt(), 0));
SymVal x5744 = SymMemory.loadSym(x5742.toInt(), 0);
Stack.push(x5743);
SymStack.push(x5744);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5745 = Stack.pop();
SymVal x5746 = SymStack.pop();
Num x5747 = Stack.pop();
SymVal x5748 = SymStack.pop();
Num x5749 = x5747.i32_sub(x5745);
Stack.push(x5749);
bool x5750 = allConcrete(x5748, x5746);
SymVal x5751 = x5750 ? Concrete(x5749, 32) : x5748.minus(x5746);
SymStack.push(x5751);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5752 = Stack.pop();
SymVal x5753 = SymStack.pop();
Num x5754 = Stack.pop();
SymVal x5755 = SymStack.pop();
Num x5756 = x5754.i32_mul(x5752);
Stack.push(x5756);
bool x5757 = allConcrete(x5755, x5753);
SymVal x5758 = x5757 ? Concrete(x5756, 32) : x5755.mul(x5753);
SymStack.push(x5758);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5759 = Stack.pop();
SymVal x5760 = SymStack.pop();
Num x5761 = Stack.pop();
SymVal x5762 = SymStack.pop();
Num x5763 = x5761.i32_mul(x5759);
Stack.push(x5763);
bool x5764 = allConcrete(x5762, x5760);
SymVal x5765 = x5764 ? Concrete(x5763, 32) : x5762.mul(x5760);
SymStack.push(x5765);
}
{
Num x5766 = Stack.pop();
SymVal x5767 = SymStack.pop();
Num x5768 = Stack.pop();
SymVal x5769 = SymStack.pop();
Num x5770 = x5768.i32_add(x5766);
Stack.push(x5770);
bool x5771 = allConcrete(x5769, x5767);
SymVal x5772 = x5771 ? Concrete(x5770, 32) : x5769.add(x5767);
SymStack.push(x5772);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x5773 = Stack.pop();
SymVal x5774 = SymStack.pop();
Num x5775 = Stack.pop();
SymVal x5776 = SymStack.pop();
Num x5777 = x5775.i32_add(x5773);
Stack.push(x5777);
bool x5778 = allConcrete(x5776, x5774);
SymVal x5779 = x5778 ? Concrete(x5777, 32) : x5776.add(x5774);
SymStack.push(x5779);
}
{
Num x5780 = Stack.pop();
SymStack.pop();
Num x5781 = I32V(Memory.loadInt(x5780.toInt(), 8));
SymVal x5782 = SymMemory.loadSym(x5780.toInt(), 8);
Stack.push(x5781);
SymStack.push(x5782);
}
{
Num x5783 = Stack.pop();
SymVal x5784 = SymStack.pop();
Frames.set(5, x5783);
SymFrames.set(5, x5784);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x5785 = Stack.pop();
SymStack.pop();
Num x5786 = I32V(Memory.loadInt(x5785.toInt(), 4));
SymVal x5787 = SymMemory.loadSym(x5785.toInt(), 4);
Stack.push(x5786);
SymStack.push(x5787);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5788 = Stack.pop();
SymStack.pop();
Num x5789 = I32V(Memory.loadInt(x5788.toInt(), 0));
SymVal x5790 = SymMemory.loadSym(x5788.toInt(), 0);
Stack.push(x5789);
SymStack.push(x5790);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x5791 = Stack.pop();
SymVal x5792 = SymStack.pop();
Num x5793 = Stack.pop();
SymVal x5794 = SymStack.pop();
Num x5795 = x5793.i32_div_s(x5791);
Stack.push(x5795);
bool x5796 = allConcrete(x5794, x5792);
SymVal x5797 = x5796 ? Concrete(x5795, 32) : x5794.div(x5792);
SymStack.push(x5797);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5798 = Stack.pop();
SymVal x5799 = SymStack.pop();
Num x5800 = Stack.pop();
SymVal x5801 = SymStack.pop();
Num x5802 = x5800.i32_sub(x5798);
Stack.push(x5802);
bool x5803 = allConcrete(x5801, x5799);
SymVal x5804 = x5803 ? Concrete(x5802, 32) : x5801.minus(x5799);
SymStack.push(x5804);
}
{
Num x5805 = Stack.pop();
SymVal x5806 = SymStack.pop();
Num x5807 = Stack.pop();
SymVal x5808 = SymStack.pop();
Num x5809 = x5807.i32_eq(x5805);
Stack.push(x5809);
bool x5810 = allConcrete(x5808, x5806);
SymVal x5811 = x5810 ? Concrete(x5809, 32) : x5808.eq(x5806).bool2bv();
SymStack.push(x5811);
}
Num x5812 = Stack.pop();
{
SymVal x5813 = SymStack.pop();
ExploreTree.fillIfElseNode(x5813, 28);
}
int x5814 = x5812.toInt();
if (x5814 != 0) {
ExploreTree.moveCursor(true, makeControl(x2065, CURRENT_MCONT));
__attribute__((musttail)) return x5716(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x5716, CURRENT_MCONT));
__attribute__((musttail)) return x2065(std::monostate{});
}
return std::monostate{};
}
std::monostate x5716(std::monostate x5717) {
info("Entering the true branch 28 of the if");
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x5718 = Stack.pop();
SymVal x5719 = SymStack.pop();
Frames.set(4, x5718);
SymFrames.set(4, x5719);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5720 = Stack.pop();
SymVal x5721 = SymStack.pop();
Num x5722 = Stack.pop();
SymVal x5723 = SymStack.pop();
Num x5724 = x5722.i32_add(x5720);
Stack.push(x5724);
bool x5725 = allConcrete(x5723, x5721);
SymVal x5726 = x5725 ? Concrete(x5724, 32) : x5723.add(x5721);
SymStack.push(x5726);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x5727 = Stack.pop();
SymStack.pop();
Num x5728 = I32V(Memory.loadInt(x5727.toInt(), 4));
SymVal x5729 = SymMemory.loadSym(x5727.toInt(), 4);
Stack.push(x5728);
SymStack.push(x5729);
}
{
Num x5730 = Stack.pop();
SymVal x5731 = SymStack.pop();
Num x5732 = Stack.pop();
SymVal x5733 = SymStack.pop();
Num x5734 = x5732.i32_le_s(x5730);
Stack.push(x5734);
bool x5735 = allConcrete(x5733, x5731);
SymVal x5736 = x5735 ? Concrete(x5734, 32) : x5733.le(x5731).bool2bv();
SymStack.push(x5736);
}
Num x5737 = Stack.pop();
{
SymVal x5738 = SymStack.pop();
ExploreTree.fillIfElseNode(x5738, 31);
}
int x5739 = x5737.toInt();
if (x5739 != 0) {
ExploreTree.moveCursor(true, makeControl(x4690, CURRENT_MCONT));
__attribute__((musttail)) return x5643(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x5643, CURRENT_MCONT));
__attribute__((musttail)) return x4690(std::monostate{});
}
return std::monostate{};
}
std::monostate x5643(std::monostate x5644) {
info("Entering the true branch 31 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5645 = Stack.pop();
SymStack.pop();
Num x5646 = I32V(Memory.loadInt(x5645.toInt(), 0));
SymVal x5647 = SymMemory.loadSym(x5645.toInt(), 0);
Stack.push(x5646);
SymStack.push(x5647);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5648 = Stack.pop();
SymVal x5649 = SymStack.pop();
Num x5650 = Stack.pop();
SymVal x5651 = SymStack.pop();
Num x5652 = x5650.i32_sub(x5648);
Stack.push(x5652);
bool x5653 = allConcrete(x5651, x5649);
SymVal x5654 = x5653 ? Concrete(x5652, 32) : x5651.minus(x5649);
SymStack.push(x5654);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5655 = Stack.pop();
SymVal x5656 = SymStack.pop();
Num x5657 = Stack.pop();
SymVal x5658 = SymStack.pop();
Num x5659 = x5657.i32_mul(x5655);
Stack.push(x5659);
bool x5660 = allConcrete(x5658, x5656);
SymVal x5661 = x5660 ? Concrete(x5659, 32) : x5658.mul(x5656);
SymStack.push(x5661);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5662 = Stack.pop();
SymVal x5663 = SymStack.pop();
Num x5664 = Stack.pop();
SymVal x5665 = SymStack.pop();
Num x5666 = x5664.i32_add(x5662);
Stack.push(x5666);
bool x5667 = allConcrete(x5665, x5663);
SymVal x5668 = x5667 ? Concrete(x5666, 32) : x5665.add(x5663);
SymStack.push(x5668);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5669 = Stack.pop();
SymVal x5670 = SymStack.pop();
Num x5671 = Stack.pop();
SymVal x5672 = SymStack.pop();
Num x5673 = x5671.i32_mul(x5669);
Stack.push(x5673);
bool x5674 = allConcrete(x5672, x5670);
SymVal x5675 = x5674 ? Concrete(x5673, 32) : x5672.mul(x5670);
SymStack.push(x5675);
}
{
Num x5676 = Stack.pop();
SymVal x5677 = SymStack.pop();
Num x5678 = Stack.pop();
SymVal x5679 = SymStack.pop();
Num x5680 = x5678.i32_add(x5676);
Stack.push(x5680);
bool x5681 = allConcrete(x5679, x5677);
SymVal x5682 = x5681 ? Concrete(x5680, 32) : x5679.add(x5677);
SymStack.push(x5682);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x5683 = Stack.pop();
SymVal x5684 = SymStack.pop();
Num x5685 = Stack.pop();
SymVal x5686 = SymStack.pop();
Num x5687 = x5685.i32_add(x5683);
Stack.push(x5687);
bool x5688 = allConcrete(x5686, x5684);
SymVal x5689 = x5688 ? Concrete(x5687, 32) : x5686.add(x5684);
SymStack.push(x5689);
}
{
Num x5690 = Stack.pop();
SymStack.pop();
Num x5691 = I32V(Memory.loadInt(x5690.toInt(), 8));
SymVal x5692 = SymMemory.loadSym(x5690.toInt(), 8);
Stack.push(x5691);
SymStack.push(x5692);
}
{
Num x5693 = Stack.pop();
SymStack.pop();
Num x5694 = I32V(Memory.loadInt(x5693.toInt(), 4));
SymVal x5695 = SymMemory.loadSym(x5693.toInt(), 4);
Stack.push(x5694);
SymStack.push(x5695);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5696 = Stack.pop();
SymStack.pop();
Num x5697 = I32V(Memory.loadInt(x5696.toInt(), 0));
SymVal x5698 = SymMemory.loadSym(x5696.toInt(), 0);
Stack.push(x5697);
SymStack.push(x5698);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x5699 = Stack.pop();
SymVal x5700 = SymStack.pop();
Num x5701 = Stack.pop();
SymVal x5702 = SymStack.pop();
Num x5703 = x5701.i32_div_s(x5699);
Stack.push(x5703);
bool x5704 = allConcrete(x5702, x5700);
SymVal x5705 = x5704 ? Concrete(x5703, 32) : x5702.div(x5700);
SymStack.push(x5705);
}
{
Num x5706 = Stack.pop();
SymVal x5707 = SymStack.pop();
Num x5708 = Stack.pop();
SymVal x5709 = SymStack.pop();
Num x5710 = x5708.i32_ge_s(x5706);
Stack.push(x5710);
bool x5711 = allConcrete(x5709, x5707);
SymVal x5712 = x5711 ? Concrete(x5710, 32) : x5709.ge(x5707).bool2bv();
SymStack.push(x5712);
}
Num x5713 = Stack.pop();
{
SymVal x5714 = SymStack.pop();
ExploreTree.fillIfElseNode(x5714, 49);
}
int x5715 = x5713.toInt();
if (x5715 != 0) {
ExploreTree.moveCursor(true, makeControl(x4694, CURRENT_MCONT));
__attribute__((musttail)) return x5479(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x5479, CURRENT_MCONT));
__attribute__((musttail)) return x4694(std::monostate{});
}
return std::monostate{};
}
std::monostate x5479(std::monostate x5480) {
info("Entering the true branch 49 of the if");
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5481 = Stack.pop();
SymStack.pop();
Num x5482 = I32V(Memory.loadInt(x5481.toInt(), 0));
SymVal x5483 = SymMemory.loadSym(x5481.toInt(), 0);
Stack.push(x5482);
SymStack.push(x5483);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x5484 = Stack.pop();
SymVal x5485 = SymStack.pop();
Num x5486 = Stack.pop();
SymVal x5487 = SymStack.pop();
Num x5488 = x5486.i32_div_s(x5484);
Stack.push(x5488);
bool x5489 = allConcrete(x5487, x5485);
SymVal x5490 = x5489 ? Concrete(x5488, 32) : x5487.div(x5485);
SymStack.push(x5490);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5491 = Stack.pop();
SymVal x5492 = SymStack.pop();
Num x5493 = Stack.pop();
SymVal x5494 = SymStack.pop();
Num x5495 = x5493.i32_sub(x5491);
Stack.push(x5495);
bool x5496 = allConcrete(x5494, x5492);
SymVal x5497 = x5496 ? Concrete(x5495, 32) : x5494.minus(x5492);
SymStack.push(x5497);
}
{
Num x5498 = Stack.pop();
SymVal x5499 = SymStack.pop();
Num x5500 = Stack.pop();
SymVal x5501 = SymStack.pop();
Num x5502 = x5500.i32_mul(x5498);
Stack.push(x5502);
bool x5503 = allConcrete(x5501, x5499);
SymVal x5504 = x5503 ? Concrete(x5502, 32) : x5501.mul(x5499);
SymStack.push(x5504);
}
{
Num x5505 = Stack.pop();
SymVal x5506 = SymStack.pop();
Num x5507 = Stack.pop();
SymVal x5508 = SymStack.pop();
Num x5509 = x5507.i32_add(x5505);
Stack.push(x5509);
bool x5510 = allConcrete(x5508, x5506);
SymVal x5511 = x5510 ? Concrete(x5509, 32) : x5508.add(x5506);
SymStack.push(x5511);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x5512 = Stack.pop();
SymVal x5513 = SymStack.pop();
Num x5514 = Stack.pop();
SymVal x5515 = SymStack.pop();
Num x5516 = x5514.i32_mul(x5512);
Stack.push(x5516);
bool x5517 = allConcrete(x5515, x5513);
SymVal x5518 = x5517 ? Concrete(x5516, 32) : x5515.mul(x5513);
SymStack.push(x5518);
}
{
Num x5519 = Stack.pop();
SymVal x5520 = SymStack.pop();
Num x5521 = Stack.pop();
SymVal x5522 = SymStack.pop();
Num x5523 = x5521.i32_add(x5519);
Stack.push(x5523);
bool x5524 = allConcrete(x5522, x5520);
SymVal x5525 = x5524 ? Concrete(x5523, 32) : x5522.add(x5520);
SymStack.push(x5525);
}
{
Num x5526 = Stack.pop();
SymStack.pop();
Num x5527 = I32V(Memory.loadInt(x5526.toInt(), 8));
SymVal x5528 = SymMemory.loadSym(x5526.toInt(), 8);
Stack.push(x5527);
SymStack.push(x5528);
}
{
Num x5529 = Stack.pop();
SymVal x5530 = SymStack.pop();
Num x5531 = Stack.pop();
SymStack.pop();
int x5532 = x5531.toInt();
Memory.storeInt(x5532, 8, x5529.toInt());
SymMemory.storeSym(x5532, 8, x5530);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x5533 = Stack.pop();
SymStack.pop();
Num x5534 = I32V(Memory.loadInt(x5533.toInt(), 4));
SymVal x5535 = SymMemory.loadSym(x5533.toInt(), 4);
Stack.push(x5534);
SymStack.push(x5535);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5536 = Stack.pop();
SymVal x5537 = SymStack.pop();
Num x5538 = Stack.pop();
SymVal x5539 = SymStack.pop();
Num x5540 = x5538.i32_add(x5536);
Stack.push(x5540);
bool x5541 = allConcrete(x5539, x5537);
SymVal x5542 = x5541 ? Concrete(x5540, 32) : x5539.add(x5537);
SymStack.push(x5542);
}
{
Num x5543 = Stack.pop();
SymVal x5544 = SymStack.pop();
Num x5545 = Stack.pop();
SymStack.pop();
int x5546 = x5545.toInt();
Memory.storeInt(x5546, 4, x5543.toInt());
SymMemory.storeSym(x5546, 4, x5544);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x5547 = Stack.pop();
SymVal x5548 = SymStack.pop();
Num x5549 = Stack.pop();
SymVal x5550 = SymStack.pop();
Num x5551 = x5549.i32_mul(x5547);
Stack.push(x5551);
bool x5552 = allConcrete(x5550, x5548);
SymVal x5553 = x5552 ? Concrete(x5551, 32) : x5550.mul(x5548);
SymStack.push(x5553);
}
{
Num x5554 = Stack.pop();
SymVal x5555 = SymStack.pop();
Num x5556 = Stack.pop();
SymVal x5557 = SymStack.pop();
Num x5558 = x5556.i32_add(x5554);
Stack.push(x5558);
bool x5559 = allConcrete(x5557, x5555);
SymVal x5560 = x5559 ? Concrete(x5558, 32) : x5557.add(x5555);
SymStack.push(x5560);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5561 = Stack.pop();
SymStack.pop();
Num x5562 = I32V(Memory.loadInt(x5561.toInt(), 0));
SymVal x5563 = SymMemory.loadSym(x5561.toInt(), 0);
Stack.push(x5562);
SymStack.push(x5563);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5564 = Stack.pop();
SymVal x5565 = SymStack.pop();
Num x5566 = Stack.pop();
SymVal x5567 = SymStack.pop();
Num x5568 = x5566.i32_sub(x5564);
Stack.push(x5568);
bool x5569 = allConcrete(x5567, x5565);
SymVal x5570 = x5569 ? Concrete(x5568, 32) : x5567.minus(x5565);
SymStack.push(x5570);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5571 = Stack.pop();
SymVal x5572 = SymStack.pop();
Num x5573 = Stack.pop();
SymVal x5574 = SymStack.pop();
Num x5575 = x5573.i32_mul(x5571);
Stack.push(x5575);
bool x5576 = allConcrete(x5574, x5572);
SymVal x5577 = x5576 ? Concrete(x5575, 32) : x5574.mul(x5572);
SymStack.push(x5577);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5578 = Stack.pop();
SymVal x5579 = SymStack.pop();
Num x5580 = Stack.pop();
SymVal x5581 = SymStack.pop();
Num x5582 = x5580.i32_add(x5578);
Stack.push(x5582);
bool x5583 = allConcrete(x5581, x5579);
SymVal x5584 = x5583 ? Concrete(x5582, 32) : x5581.add(x5579);
SymStack.push(x5584);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5585 = Stack.pop();
SymVal x5586 = SymStack.pop();
Num x5587 = Stack.pop();
SymVal x5588 = SymStack.pop();
Num x5589 = x5587.i32_mul(x5585);
Stack.push(x5589);
bool x5590 = allConcrete(x5588, x5586);
SymVal x5591 = x5590 ? Concrete(x5589, 32) : x5588.mul(x5586);
SymStack.push(x5591);
}
{
Num x5592 = Stack.pop();
SymVal x5593 = SymStack.pop();
Num x5594 = Stack.pop();
SymVal x5595 = SymStack.pop();
Num x5596 = x5594.i32_add(x5592);
Stack.push(x5596);
bool x5597 = allConcrete(x5595, x5593);
SymVal x5598 = x5597 ? Concrete(x5596, 32) : x5595.add(x5593);
SymStack.push(x5598);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x5599 = Stack.pop();
SymVal x5600 = SymStack.pop();
Num x5601 = Stack.pop();
SymVal x5602 = SymStack.pop();
Num x5603 = x5601.i32_add(x5599);
Stack.push(x5603);
bool x5604 = allConcrete(x5602, x5600);
SymVal x5605 = x5604 ? Concrete(x5603, 32) : x5602.add(x5600);
SymStack.push(x5605);
}
{
Num x5606 = Stack.pop();
SymStack.pop();
Num x5607 = I32V(Memory.loadInt(x5606.toInt(), 8));
SymVal x5608 = SymMemory.loadSym(x5606.toInt(), 8);
Stack.push(x5607);
SymStack.push(x5608);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5609 = Stack.pop();
SymVal x5610 = SymStack.pop();
Num x5611 = Stack.pop();
SymVal x5612 = SymStack.pop();
Num x5613 = x5611.i32_mul(x5609);
Stack.push(x5613);
bool x5614 = allConcrete(x5612, x5610);
SymVal x5615 = x5614 ? Concrete(x5613, 32) : x5612.mul(x5610);
SymStack.push(x5615);
}
{
Num x5616 = Stack.pop();
SymVal x5617 = SymStack.pop();
Num x5618 = Stack.pop();
SymVal x5619 = SymStack.pop();
Num x5620 = x5618.i32_add(x5616);
Stack.push(x5620);
bool x5621 = allConcrete(x5619, x5617);
SymVal x5622 = x5621 ? Concrete(x5620, 32) : x5619.add(x5617);
SymStack.push(x5622);
}
{
Num x5623 = Stack.pop();
SymStack.pop();
Num x5624 = I32V(Memory.loadInt(x5623.toInt(), 8));
SymVal x5625 = SymMemory.loadSym(x5623.toInt(), 8);
Stack.push(x5624);
SymStack.push(x5625);
}
{
Num x5626 = Stack.pop();
SymVal x5627 = SymStack.pop();
Num x5628 = Stack.pop();
SymStack.pop();
int x5629 = x5628.toInt();
Memory.storeInt(x5629, 8, x5626.toInt());
SymMemory.storeSym(x5629, 8, x5627);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x5630 = Stack.pop();
SymStack.pop();
Num x5631 = I32V(Memory.loadInt(x5630.toInt(), 0));
SymVal x5632 = SymMemory.loadSym(x5630.toInt(), 0);
Stack.push(x5631);
SymStack.push(x5632);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5633 = Stack.pop();
SymVal x5634 = SymStack.pop();
Num x5635 = Stack.pop();
SymVal x5636 = SymStack.pop();
Num x5637 = x5635.i32_ne(x5633);
Stack.push(x5637);
bool x5638 = allConcrete(x5636, x5634);
SymVal x5639 = x5638 ? Concrete(x5637, 32) : x5636.neq(x5634).bool2bv();
SymStack.push(x5639);
}
Num x5640 = Stack.pop();
{
SymVal x5641 = SymStack.pop();
ExploreTree.fillIfElseNode(x5641, 50);
}
int x5642 = x5640.toInt();
if (x5642 != 0) {
ExploreTree.moveCursor(true, makeControl(x5334, CURRENT_MCONT));
__attribute__((musttail)) return x5336(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x5336, CURRENT_MCONT));
__attribute__((musttail)) return x5334(std::monostate{});
}
return std::monostate{};
}
std::monostate x5336(std::monostate x5337) {
info("Entering the true branch 50 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5338 = Stack.pop();
SymStack.pop();
Num x5339 = I32V(Memory.loadInt(x5338.toInt(), 0));
SymVal x5340 = SymMemory.loadSym(x5338.toInt(), 0);
Stack.push(x5339);
SymStack.push(x5340);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5341 = Stack.pop();
SymVal x5342 = SymStack.pop();
Num x5343 = Stack.pop();
SymVal x5344 = SymStack.pop();
Num x5345 = x5343.i32_sub(x5341);
Stack.push(x5345);
bool x5346 = allConcrete(x5344, x5342);
SymVal x5347 = x5346 ? Concrete(x5345, 32) : x5344.minus(x5342);
SymStack.push(x5347);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5348 = Stack.pop();
SymVal x5349 = SymStack.pop();
Num x5350 = Stack.pop();
SymVal x5351 = SymStack.pop();
Num x5352 = x5350.i32_mul(x5348);
Stack.push(x5352);
bool x5353 = allConcrete(x5351, x5349);
SymVal x5354 = x5353 ? Concrete(x5352, 32) : x5351.mul(x5349);
SymStack.push(x5354);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5355 = Stack.pop();
SymStack.pop();
Num x5356 = I32V(Memory.loadInt(x5355.toInt(), 0));
SymVal x5357 = SymMemory.loadSym(x5355.toInt(), 0);
Stack.push(x5356);
SymStack.push(x5357);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x5358 = Stack.pop();
SymVal x5359 = SymStack.pop();
Num x5360 = Stack.pop();
SymVal x5361 = SymStack.pop();
Num x5362 = x5360.i32_div_s(x5358);
Stack.push(x5362);
bool x5363 = allConcrete(x5361, x5359);
SymVal x5364 = x5363 ? Concrete(x5362, 32) : x5361.div(x5359);
SymStack.push(x5364);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5365 = Stack.pop();
SymVal x5366 = SymStack.pop();
Num x5367 = Stack.pop();
SymVal x5368 = SymStack.pop();
Num x5369 = x5367.i32_mul(x5365);
Stack.push(x5369);
bool x5370 = allConcrete(x5368, x5366);
SymVal x5371 = x5370 ? Concrete(x5369, 32) : x5368.mul(x5366);
SymStack.push(x5371);
}
{
Num x5372 = Stack.pop();
SymVal x5373 = SymStack.pop();
Num x5374 = Stack.pop();
SymVal x5375 = SymStack.pop();
Num x5376 = x5374.i32_add(x5372);
Stack.push(x5376);
bool x5377 = allConcrete(x5375, x5373);
SymVal x5378 = x5377 ? Concrete(x5376, 32) : x5375.add(x5373);
SymStack.push(x5378);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x5379 = Stack.pop();
SymVal x5380 = SymStack.pop();
Num x5381 = Stack.pop();
SymVal x5382 = SymStack.pop();
Num x5383 = x5381.i32_add(x5379);
Stack.push(x5383);
bool x5384 = allConcrete(x5382, x5380);
SymVal x5385 = x5384 ? Concrete(x5383, 32) : x5382.add(x5380);
SymStack.push(x5385);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5386 = Stack.pop();
SymStack.pop();
Num x5387 = I32V(Memory.loadInt(x5386.toInt(), 0));
SymVal x5388 = SymMemory.loadSym(x5386.toInt(), 0);
Stack.push(x5387);
SymStack.push(x5388);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5389 = Stack.pop();
SymVal x5390 = SymStack.pop();
Num x5391 = Stack.pop();
SymVal x5392 = SymStack.pop();
Num x5393 = x5391.i32_sub(x5389);
Stack.push(x5393);
bool x5394 = allConcrete(x5392, x5390);
SymVal x5395 = x5394 ? Concrete(x5393, 32) : x5392.minus(x5390);
SymStack.push(x5395);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5396 = Stack.pop();
SymVal x5397 = SymStack.pop();
Num x5398 = Stack.pop();
SymVal x5399 = SymStack.pop();
Num x5400 = x5398.i32_mul(x5396);
Stack.push(x5400);
bool x5401 = allConcrete(x5399, x5397);
SymVal x5402 = x5401 ? Concrete(x5400, 32) : x5399.mul(x5397);
SymStack.push(x5402);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5403 = Stack.pop();
SymVal x5404 = SymStack.pop();
Num x5405 = Stack.pop();
SymVal x5406 = SymStack.pop();
Num x5407 = x5405.i32_mul(x5403);
Stack.push(x5407);
bool x5408 = allConcrete(x5406, x5404);
SymVal x5409 = x5408 ? Concrete(x5407, 32) : x5406.mul(x5404);
SymStack.push(x5409);
}
{
Num x5410 = Stack.pop();
SymVal x5411 = SymStack.pop();
Num x5412 = Stack.pop();
SymVal x5413 = SymStack.pop();
Num x5414 = x5412.i32_add(x5410);
Stack.push(x5414);
bool x5415 = allConcrete(x5413, x5411);
SymVal x5416 = x5415 ? Concrete(x5414, 32) : x5413.add(x5411);
SymStack.push(x5416);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5417 = Stack.pop();
SymStack.pop();
Num x5418 = I32V(Memory.loadInt(x5417.toInt(), 0));
SymVal x5419 = SymMemory.loadSym(x5417.toInt(), 0);
Stack.push(x5418);
SymStack.push(x5419);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5420 = Stack.pop();
SymVal x5421 = SymStack.pop();
Num x5422 = Stack.pop();
SymVal x5423 = SymStack.pop();
Num x5424 = x5422.i32_sub(x5420);
Stack.push(x5424);
bool x5425 = allConcrete(x5423, x5421);
SymVal x5426 = x5425 ? Concrete(x5424, 32) : x5423.minus(x5421);
SymStack.push(x5426);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5427 = Stack.pop();
SymVal x5428 = SymStack.pop();
Num x5429 = Stack.pop();
SymVal x5430 = SymStack.pop();
Num x5431 = x5429.i32_mul(x5427);
Stack.push(x5431);
bool x5432 = allConcrete(x5430, x5428);
SymVal x5433 = x5432 ? Concrete(x5431, 32) : x5430.mul(x5428);
SymStack.push(x5433);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5434 = Stack.pop();
SymVal x5435 = SymStack.pop();
Num x5436 = Stack.pop();
SymVal x5437 = SymStack.pop();
Num x5438 = x5436.i32_add(x5434);
Stack.push(x5438);
bool x5439 = allConcrete(x5437, x5435);
SymVal x5440 = x5439 ? Concrete(x5438, 32) : x5437.add(x5435);
SymStack.push(x5440);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5441 = Stack.pop();
SymVal x5442 = SymStack.pop();
Num x5443 = Stack.pop();
SymVal x5444 = SymStack.pop();
Num x5445 = x5443.i32_mul(x5441);
Stack.push(x5445);
bool x5446 = allConcrete(x5444, x5442);
SymVal x5447 = x5446 ? Concrete(x5445, 32) : x5444.mul(x5442);
SymStack.push(x5447);
}
{
Num x5448 = Stack.pop();
SymVal x5449 = SymStack.pop();
Num x5450 = Stack.pop();
SymVal x5451 = SymStack.pop();
Num x5452 = x5450.i32_add(x5448);
Stack.push(x5452);
bool x5453 = allConcrete(x5451, x5449);
SymVal x5454 = x5453 ? Concrete(x5452, 32) : x5451.add(x5449);
SymStack.push(x5454);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x5455 = Stack.pop();
SymVal x5456 = SymStack.pop();
Num x5457 = Stack.pop();
SymVal x5458 = SymStack.pop();
Num x5459 = x5457.i32_add(x5455);
Stack.push(x5459);
bool x5460 = allConcrete(x5458, x5456);
SymVal x5461 = x5460 ? Concrete(x5459, 32) : x5458.add(x5456);
SymStack.push(x5461);
}
{
Num x5462 = Stack.pop();
SymStack.pop();
Num x5463 = I32V(Memory.loadInt(x5462.toInt(), 8));
SymVal x5464 = SymMemory.loadSym(x5462.toInt(), 8);
Stack.push(x5463);
SymStack.push(x5464);
}
{
Num x5465 = Stack.pop();
SymVal x5466 = SymStack.pop();
Num x5467 = Stack.pop();
SymVal x5468 = SymStack.pop();
Num x5469 = x5467.i32_add(x5465);
Stack.push(x5469);
bool x5470 = allConcrete(x5468, x5466);
SymVal x5471 = x5470 ? Concrete(x5469, 32) : x5468.add(x5466);
SymStack.push(x5471);
}
{
Num x5472 = Stack.pop();
SymStack.pop();
Num x5473 = I32V(Memory.loadInt(x5472.toInt(), 8));
SymVal x5474 = SymMemory.loadSym(x5472.toInt(), 8);
Stack.push(x5473);
SymStack.push(x5474);
}
{
Num x5475 = Stack.pop();
SymVal x5476 = SymStack.pop();
Num x5477 = Stack.pop();
SymStack.pop();
int x5478 = x5477.toInt();
Memory.storeInt(x5478, 8, x5475.toInt());
SymMemory.storeSym(x5478, 8, x5476);
}
__attribute__((musttail)) return x5316(std::monostate{});
return std::monostate{};
}
std::monostate x5334(std::monostate x5335) {
info("Entering the false branch 50 of the if");
__attribute__((musttail)) return x5316(std::monostate{});
return std::monostate{};
}
std::monostate x5316(std::monostate x5317) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5318 = Stack.pop();
SymStack.pop();
Num x5319 = I32V(Memory.loadInt(x5318.toInt(), 0));
SymVal x5320 = SymMemory.loadSym(x5318.toInt(), 0);
Stack.push(x5319);
SymStack.push(x5320);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x5321 = Stack.pop();
SymVal x5322 = SymStack.pop();
Num x5323 = Stack.pop();
SymVal x5324 = SymStack.pop();
Num x5325 = x5323.i32_div_s(x5321);
Stack.push(x5325);
bool x5326 = allConcrete(x5324, x5322);
SymVal x5327 = x5326 ? Concrete(x5325, 32) : x5324.div(x5322);
SymStack.push(x5327);
}
{
Num x5328 = Stack.pop();
SymVal x5329 = SymStack.pop();
Num x5330 = Stack.pop();
SymStack.pop();
int x5331 = x5330.toInt();
Memory.storeInt(x5331, 4, x5328.toInt());
SymMemory.storeSym(x5331, 4, x5329);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5332 = Stack.pop();
SymVal x5333 = SymStack.pop();
Frames.set(3, x5332);
SymFrames.set(3, x5333);
}
__attribute__((musttail)) return x4845(std::monostate{});
return std::monostate{};
}
std::monostate x4845(std::monostate x5247) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5248 = Stack.pop();
SymStack.pop();
Num x5249 = I32V(Memory.loadInt(x5248.toInt(), 0));
SymVal x5250 = SymMemory.loadSym(x5248.toInt(), 0);
Stack.push(x5249);
SymStack.push(x5250);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5251 = Stack.pop();
SymVal x5252 = SymStack.pop();
Num x5253 = Stack.pop();
SymVal x5254 = SymStack.pop();
Num x5255 = x5253.i32_sub(x5251);
Stack.push(x5255);
bool x5256 = allConcrete(x5254, x5252);
SymVal x5257 = x5256 ? Concrete(x5255, 32) : x5254.minus(x5252);
SymStack.push(x5257);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5258 = Stack.pop();
SymVal x5259 = SymStack.pop();
Num x5260 = Stack.pop();
SymVal x5261 = SymStack.pop();
Num x5262 = x5260.i32_mul(x5258);
Stack.push(x5262);
bool x5263 = allConcrete(x5261, x5259);
SymVal x5264 = x5263 ? Concrete(x5262, 32) : x5261.mul(x5259);
SymStack.push(x5264);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5265 = Stack.pop();
SymVal x5266 = SymStack.pop();
Num x5267 = Stack.pop();
SymVal x5268 = SymStack.pop();
Num x5269 = x5267.i32_add(x5265);
Stack.push(x5269);
bool x5270 = allConcrete(x5268, x5266);
SymVal x5271 = x5270 ? Concrete(x5269, 32) : x5268.add(x5266);
SymStack.push(x5271);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5272 = Stack.pop();
SymVal x5273 = SymStack.pop();
Num x5274 = Stack.pop();
SymVal x5275 = SymStack.pop();
Num x5276 = x5274.i32_mul(x5272);
Stack.push(x5276);
bool x5277 = allConcrete(x5275, x5273);
SymVal x5278 = x5277 ? Concrete(x5276, 32) : x5275.mul(x5273);
SymStack.push(x5278);
}
{
Num x5279 = Stack.pop();
SymVal x5280 = SymStack.pop();
Num x5281 = Stack.pop();
SymVal x5282 = SymStack.pop();
Num x5283 = x5281.i32_add(x5279);
Stack.push(x5283);
bool x5284 = allConcrete(x5282, x5280);
SymVal x5285 = x5284 ? Concrete(x5283, 32) : x5282.add(x5280);
SymStack.push(x5285);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x5286 = Stack.pop();
SymVal x5287 = SymStack.pop();
Num x5288 = Stack.pop();
SymVal x5289 = SymStack.pop();
Num x5290 = x5288.i32_add(x5286);
Stack.push(x5290);
bool x5291 = allConcrete(x5289, x5287);
SymVal x5292 = x5291 ? Concrete(x5290, 32) : x5289.add(x5287);
SymStack.push(x5292);
}
{
Num x5293 = Stack.pop();
SymStack.pop();
Num x5294 = I32V(Memory.loadInt(x5293.toInt(), 8));
SymVal x5295 = SymMemory.loadSym(x5293.toInt(), 8);
Stack.push(x5294);
SymStack.push(x5295);
}
{
Num x5296 = Stack.pop();
SymStack.pop();
Num x5297 = I32V(Memory.loadInt(x5296.toInt(), 4));
SymVal x5298 = SymMemory.loadSym(x5296.toInt(), 4);
Stack.push(x5297);
SymStack.push(x5298);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5299 = Stack.pop();
SymVal x5300 = SymStack.pop();
Num x5301 = Stack.pop();
SymVal x5302 = SymStack.pop();
Num x5303 = x5301.i32_sub(x5299);
Stack.push(x5303);
bool x5304 = allConcrete(x5302, x5300);
SymVal x5305 = x5304 ? Concrete(x5303, 32) : x5302.minus(x5300);
SymStack.push(x5305);
}
{
Num x5306 = Stack.pop();
SymVal x5307 = SymStack.pop();
Num x5308 = Stack.pop();
SymVal x5309 = SymStack.pop();
Num x5310 = x5308.i32_eq(x5306);
Stack.push(x5310);
bool x5311 = allConcrete(x5309, x5307);
SymVal x5312 = x5311 ? Concrete(x5310, 32) : x5309.eq(x5307).bool2bv();
SymStack.push(x5312);
}
Num x5313 = Stack.pop();
{
SymVal x5314 = SymStack.pop();
ExploreTree.fillIfElseNode(x5314, 51);
}
int x5315 = x5313.toInt();
if (x5315 != 0) {
ExploreTree.moveCursor(true, makeControl(x4696, CURRENT_MCONT));
__attribute__((musttail)) return x5245(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x5245, CURRENT_MCONT));
__attribute__((musttail)) return x4696(std::monostate{});
}
return std::monostate{};
}
std::monostate x5245(std::monostate x5246) {
info("Entering the true branch 51 of the if");
info("Jump to 2");
__attribute__((musttail)) return x5230(std::monostate{});
return std::monostate{};
}
std::monostate x5230(std::monostate x5231) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x5232 = Stack.pop();
SymStack.pop();
Num x5233 = I32V(Memory.loadInt(x5232.toInt(), 0));
SymVal x5234 = SymMemory.loadSym(x5232.toInt(), 0);
Stack.push(x5233);
SymStack.push(x5234);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5235 = Stack.pop();
SymVal x5236 = SymStack.pop();
Num x5237 = Stack.pop();
SymVal x5238 = SymStack.pop();
Num x5239 = x5237.i32_ne(x5235);
Stack.push(x5239);
bool x5240 = allConcrete(x5238, x5236);
SymVal x5241 = x5240 ? Concrete(x5239, 32) : x5238.neq(x5236).bool2bv();
SymStack.push(x5241);
}
Num x5242 = Stack.pop();
{
SymVal x5243 = SymStack.pop();
ExploreTree.fillIfElseNode(x5243, 52);
}
int x5244 = x5242.toInt();
if (x5244 != 0) {
ExploreTree.moveCursor(true, makeControl(x4960, CURRENT_MCONT));
__attribute__((musttail)) return x5226(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x5226, CURRENT_MCONT));
__attribute__((musttail)) return x4960(std::monostate{});
}
return std::monostate{};
}
std::monostate x5226(std::monostate x5227) {
info("Entering the true branch 52 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5228 = Stack.pop();
SymVal x5229 = SymStack.pop();
Frames.set(3, x5228);
SymFrames.set(3, x5229);
}
__attribute__((musttail)) return x5159(std::monostate{});
return std::monostate{};
}
std::monostate x5159(std::monostate x5164) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5165 = Stack.pop();
SymStack.pop();
Num x5166 = I32V(Memory.loadInt(x5165.toInt(), 0));
SymVal x5167 = SymMemory.loadSym(x5165.toInt(), 0);
Stack.push(x5166);
SymStack.push(x5167);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5168 = Stack.pop();
SymVal x5169 = SymStack.pop();
Num x5170 = Stack.pop();
SymVal x5171 = SymStack.pop();
Num x5172 = x5170.i32_sub(x5168);
Stack.push(x5172);
bool x5173 = allConcrete(x5171, x5169);
SymVal x5174 = x5173 ? Concrete(x5172, 32) : x5171.minus(x5169);
SymStack.push(x5174);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5175 = Stack.pop();
SymVal x5176 = SymStack.pop();
Num x5177 = Stack.pop();
SymVal x5178 = SymStack.pop();
Num x5179 = x5177.i32_mul(x5175);
Stack.push(x5179);
bool x5180 = allConcrete(x5178, x5176);
SymVal x5181 = x5180 ? Concrete(x5179, 32) : x5178.mul(x5176);
SymStack.push(x5181);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5182 = Stack.pop();
SymVal x5183 = SymStack.pop();
Num x5184 = Stack.pop();
SymVal x5185 = SymStack.pop();
Num x5186 = x5184.i32_add(x5182);
Stack.push(x5186);
bool x5187 = allConcrete(x5185, x5183);
SymVal x5188 = x5187 ? Concrete(x5186, 32) : x5185.add(x5183);
SymStack.push(x5188);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5189 = Stack.pop();
SymVal x5190 = SymStack.pop();
Num x5191 = Stack.pop();
SymVal x5192 = SymStack.pop();
Num x5193 = x5191.i32_mul(x5189);
Stack.push(x5193);
bool x5194 = allConcrete(x5192, x5190);
SymVal x5195 = x5194 ? Concrete(x5193, 32) : x5192.mul(x5190);
SymStack.push(x5195);
}
{
Num x5196 = Stack.pop();
SymVal x5197 = SymStack.pop();
Num x5198 = Stack.pop();
SymVal x5199 = SymStack.pop();
Num x5200 = x5198.i32_add(x5196);
Stack.push(x5200);
bool x5201 = allConcrete(x5199, x5197);
SymVal x5202 = x5201 ? Concrete(x5200, 32) : x5199.add(x5197);
SymStack.push(x5202);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x5203 = Stack.pop();
SymVal x5204 = SymStack.pop();
Num x5205 = Stack.pop();
SymVal x5206 = SymStack.pop();
Num x5207 = x5205.i32_add(x5203);
Stack.push(x5207);
bool x5208 = allConcrete(x5206, x5204);
SymVal x5209 = x5208 ? Concrete(x5207, 32) : x5206.add(x5204);
SymStack.push(x5209);
}
{
Num x5210 = Stack.pop();
SymStack.pop();
Num x5211 = I32V(Memory.loadInt(x5210.toInt(), 8));
SymVal x5212 = SymMemory.loadSym(x5210.toInt(), 8);
Stack.push(x5211);
SymStack.push(x5212);
}
{
Num x5213 = Stack.pop();
SymStack.pop();
Num x5214 = I32V(Memory.loadInt(x5213.toInt(), 4));
SymVal x5215 = SymMemory.loadSym(x5213.toInt(), 4);
Stack.push(x5214);
SymStack.push(x5215);
}
{
Num x5216 = Stack.pop();
SymVal x5217 = SymStack.pop();
Num x5218 = Stack.pop();
SymVal x5219 = SymStack.pop();
Num x5220 = x5218.i32_eq(x5216);
Stack.push(x5220);
bool x5221 = allConcrete(x5219, x5217);
SymVal x5222 = x5221 ? Concrete(x5220, 32) : x5219.eq(x5217).bool2bv();
SymStack.push(x5222);
}
Num x5223 = Stack.pop();
{
SymVal x5224 = SymStack.pop();
ExploreTree.fillIfElseNode(x5224, 53);
}
int x5225 = x5223.toInt();
if (x5225 != 0) {
ExploreTree.moveCursor(true, makeControl(x4962, CURRENT_MCONT));
__attribute__((musttail)) return x5162(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x5162, CURRENT_MCONT));
__attribute__((musttail)) return x4962(std::monostate{});
}
return std::monostate{};
}
std::monostate x5162(std::monostate x5163) {
info("Entering the true branch 53 of the if");
info("Jump to 2");
__attribute__((musttail)) return x5160(std::monostate{});
return std::monostate{};
}
std::monostate x5160(std::monostate x5161) {
info("Exiting the block, stackSize =", Stack.size());
__attribute__((musttail)) return x4846(std::monostate{});
return std::monostate{};
}
std::monostate x4962(std::monostate x4963) {
info("Entering the false branch 53 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4964 = Stack.pop();
SymStack.pop();
Num x4965 = I32V(Memory.loadInt(x4964.toInt(), 0));
SymVal x4966 = SymMemory.loadSym(x4964.toInt(), 0);
Stack.push(x4965);
SymStack.push(x4966);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4967 = Stack.pop();
SymVal x4968 = SymStack.pop();
Num x4969 = Stack.pop();
SymVal x4970 = SymStack.pop();
Num x4971 = x4969.i32_sub(x4967);
Stack.push(x4971);
bool x4972 = allConcrete(x4970, x4968);
SymVal x4973 = x4972 ? Concrete(x4971, 32) : x4970.minus(x4968);
SymStack.push(x4973);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4974 = Stack.pop();
SymVal x4975 = SymStack.pop();
Num x4976 = Stack.pop();
SymVal x4977 = SymStack.pop();
Num x4978 = x4976.i32_mul(x4974);
Stack.push(x4978);
bool x4979 = allConcrete(x4977, x4975);
SymVal x4980 = x4979 ? Concrete(x4978, 32) : x4977.mul(x4975);
SymStack.push(x4980);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4981 = Stack.pop();
SymVal x4982 = SymStack.pop();
Num x4983 = Stack.pop();
SymVal x4984 = SymStack.pop();
Num x4985 = x4983.i32_mul(x4981);
Stack.push(x4985);
bool x4986 = allConcrete(x4984, x4982);
SymVal x4987 = x4986 ? Concrete(x4985, 32) : x4984.mul(x4982);
SymStack.push(x4987);
}
{
Num x4988 = Stack.pop();
SymVal x4989 = SymStack.pop();
Num x4990 = Stack.pop();
SymVal x4991 = SymStack.pop();
Num x4992 = x4990.i32_add(x4988);
Stack.push(x4992);
bool x4993 = allConcrete(x4991, x4989);
SymVal x4994 = x4993 ? Concrete(x4992, 32) : x4991.add(x4989);
SymStack.push(x4994);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4995 = Stack.pop();
SymStack.pop();
Num x4996 = I32V(Memory.loadInt(x4995.toInt(), 0));
SymVal x4997 = SymMemory.loadSym(x4995.toInt(), 0);
Stack.push(x4996);
SymStack.push(x4997);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4998 = Stack.pop();
SymVal x4999 = SymStack.pop();
Num x5000 = Stack.pop();
SymVal x5001 = SymStack.pop();
Num x5002 = x5000.i32_sub(x4998);
Stack.push(x5002);
bool x5003 = allConcrete(x5001, x4999);
SymVal x5004 = x5003 ? Concrete(x5002, 32) : x5001.minus(x4999);
SymStack.push(x5004);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5005 = Stack.pop();
SymVal x5006 = SymStack.pop();
Num x5007 = Stack.pop();
SymVal x5008 = SymStack.pop();
Num x5009 = x5007.i32_mul(x5005);
Stack.push(x5009);
bool x5010 = allConcrete(x5008, x5006);
SymVal x5011 = x5010 ? Concrete(x5009, 32) : x5008.mul(x5006);
SymStack.push(x5011);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5012 = Stack.pop();
SymVal x5013 = SymStack.pop();
Num x5014 = Stack.pop();
SymVal x5015 = SymStack.pop();
Num x5016 = x5014.i32_add(x5012);
Stack.push(x5016);
bool x5017 = allConcrete(x5015, x5013);
SymVal x5018 = x5017 ? Concrete(x5016, 32) : x5015.add(x5013);
SymStack.push(x5018);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5019 = Stack.pop();
SymVal x5020 = SymStack.pop();
Num x5021 = Stack.pop();
SymVal x5022 = SymStack.pop();
Num x5023 = x5021.i32_mul(x5019);
Stack.push(x5023);
bool x5024 = allConcrete(x5022, x5020);
SymVal x5025 = x5024 ? Concrete(x5023, 32) : x5022.mul(x5020);
SymStack.push(x5025);
}
{
Num x5026 = Stack.pop();
SymVal x5027 = SymStack.pop();
Num x5028 = Stack.pop();
SymVal x5029 = SymStack.pop();
Num x5030 = x5028.i32_add(x5026);
Stack.push(x5030);
bool x5031 = allConcrete(x5029, x5027);
SymVal x5032 = x5031 ? Concrete(x5030, 32) : x5029.add(x5027);
SymStack.push(x5032);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x5033 = Stack.pop();
SymVal x5034 = SymStack.pop();
Num x5035 = Stack.pop();
SymVal x5036 = SymStack.pop();
Num x5037 = x5035.i32_add(x5033);
Stack.push(x5037);
bool x5038 = allConcrete(x5036, x5034);
SymVal x5039 = x5038 ? Concrete(x5037, 32) : x5036.add(x5034);
SymStack.push(x5039);
}
{
Num x5040 = Stack.pop();
SymStack.pop();
Num x5041 = I32V(Memory.loadInt(x5040.toInt(), 8));
SymVal x5042 = SymMemory.loadSym(x5040.toInt(), 8);
Stack.push(x5041);
SymStack.push(x5042);
}
{
Num x5043 = Stack.pop();
SymVal x5044 = SymStack.pop();
Num x5045 = Stack.pop();
SymVal x5046 = SymStack.pop();
Num x5047 = x5045.i32_add(x5043);
Stack.push(x5047);
bool x5048 = allConcrete(x5046, x5044);
SymVal x5049 = x5048 ? Concrete(x5047, 32) : x5046.add(x5044);
SymStack.push(x5049);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5050 = Stack.pop();
SymStack.pop();
Num x5051 = I32V(Memory.loadInt(x5050.toInt(), 0));
SymVal x5052 = SymMemory.loadSym(x5050.toInt(), 0);
Stack.push(x5051);
SymStack.push(x5052);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5053 = Stack.pop();
SymVal x5054 = SymStack.pop();
Num x5055 = Stack.pop();
SymVal x5056 = SymStack.pop();
Num x5057 = x5055.i32_sub(x5053);
Stack.push(x5057);
bool x5058 = allConcrete(x5056, x5054);
SymVal x5059 = x5058 ? Concrete(x5057, 32) : x5056.minus(x5054);
SymStack.push(x5059);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5060 = Stack.pop();
SymVal x5061 = SymStack.pop();
Num x5062 = Stack.pop();
SymVal x5063 = SymStack.pop();
Num x5064 = x5062.i32_mul(x5060);
Stack.push(x5064);
bool x5065 = allConcrete(x5063, x5061);
SymVal x5066 = x5065 ? Concrete(x5064, 32) : x5063.mul(x5061);
SymStack.push(x5066);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5067 = Stack.pop();
SymVal x5068 = SymStack.pop();
Num x5069 = Stack.pop();
SymVal x5070 = SymStack.pop();
Num x5071 = x5069.i32_add(x5067);
Stack.push(x5071);
bool x5072 = allConcrete(x5070, x5068);
SymVal x5073 = x5072 ? Concrete(x5071, 32) : x5070.add(x5068);
SymStack.push(x5073);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5074 = Stack.pop();
SymVal x5075 = SymStack.pop();
Num x5076 = Stack.pop();
SymVal x5077 = SymStack.pop();
Num x5078 = x5076.i32_mul(x5074);
Stack.push(x5078);
bool x5079 = allConcrete(x5077, x5075);
SymVal x5080 = x5079 ? Concrete(x5078, 32) : x5077.mul(x5075);
SymStack.push(x5080);
}
{
Num x5081 = Stack.pop();
SymVal x5082 = SymStack.pop();
Num x5083 = Stack.pop();
SymVal x5084 = SymStack.pop();
Num x5085 = x5083.i32_add(x5081);
Stack.push(x5085);
bool x5086 = allConcrete(x5084, x5082);
SymVal x5087 = x5086 ? Concrete(x5085, 32) : x5084.add(x5082);
SymStack.push(x5087);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5088 = Stack.pop();
SymStack.pop();
Num x5089 = I32V(Memory.loadInt(x5088.toInt(), 0));
SymVal x5090 = SymMemory.loadSym(x5088.toInt(), 0);
Stack.push(x5089);
SymStack.push(x5090);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5091 = Stack.pop();
SymVal x5092 = SymStack.pop();
Num x5093 = Stack.pop();
SymVal x5094 = SymStack.pop();
Num x5095 = x5093.i32_sub(x5091);
Stack.push(x5095);
bool x5096 = allConcrete(x5094, x5092);
SymVal x5097 = x5096 ? Concrete(x5095, 32) : x5094.minus(x5092);
SymStack.push(x5097);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5098 = Stack.pop();
SymVal x5099 = SymStack.pop();
Num x5100 = Stack.pop();
SymVal x5101 = SymStack.pop();
Num x5102 = x5100.i32_mul(x5098);
Stack.push(x5102);
bool x5103 = allConcrete(x5101, x5099);
SymVal x5104 = x5103 ? Concrete(x5102, 32) : x5101.mul(x5099);
SymStack.push(x5104);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5105 = Stack.pop();
SymVal x5106 = SymStack.pop();
Num x5107 = Stack.pop();
SymVal x5108 = SymStack.pop();
Num x5109 = x5107.i32_add(x5105);
Stack.push(x5109);
bool x5110 = allConcrete(x5108, x5106);
SymVal x5111 = x5110 ? Concrete(x5109, 32) : x5108.add(x5106);
SymStack.push(x5111);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5112 = Stack.pop();
SymVal x5113 = SymStack.pop();
Num x5114 = Stack.pop();
SymVal x5115 = SymStack.pop();
Num x5116 = x5114.i32_mul(x5112);
Stack.push(x5116);
bool x5117 = allConcrete(x5115, x5113);
SymVal x5118 = x5117 ? Concrete(x5116, 32) : x5115.mul(x5113);
SymStack.push(x5118);
}
{
Num x5119 = Stack.pop();
SymVal x5120 = SymStack.pop();
Num x5121 = Stack.pop();
SymVal x5122 = SymStack.pop();
Num x5123 = x5121.i32_add(x5119);
Stack.push(x5123);
bool x5124 = allConcrete(x5122, x5120);
SymVal x5125 = x5124 ? Concrete(x5123, 32) : x5122.add(x5120);
SymStack.push(x5125);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x5126 = Stack.pop();
SymVal x5127 = SymStack.pop();
Num x5128 = Stack.pop();
SymVal x5129 = SymStack.pop();
Num x5130 = x5128.i32_add(x5126);
Stack.push(x5130);
bool x5131 = allConcrete(x5129, x5127);
SymVal x5132 = x5131 ? Concrete(x5130, 32) : x5129.add(x5127);
SymStack.push(x5132);
}
{
Num x5133 = Stack.pop();
SymStack.pop();
Num x5134 = I32V(Memory.loadInt(x5133.toInt(), 8));
SymVal x5135 = SymMemory.loadSym(x5133.toInt(), 8);
Stack.push(x5134);
SymStack.push(x5135);
}
{
Num x5136 = Stack.pop();
SymVal x5137 = SymStack.pop();
Num x5138 = Stack.pop();
SymVal x5139 = SymStack.pop();
Num x5140 = x5138.i32_add(x5136);
Stack.push(x5140);
bool x5141 = allConcrete(x5139, x5137);
SymVal x5142 = x5141 ? Concrete(x5140, 32) : x5139.add(x5137);
SymStack.push(x5142);
}
{
Num x5143 = Stack.pop();
SymStack.pop();
Num x5144 = I32V(Memory.loadInt(x5143.toInt(), 8));
SymVal x5145 = SymMemory.loadSym(x5143.toInt(), 8);
Stack.push(x5144);
SymStack.push(x5145);
}
{
Num x5146 = Stack.pop();
SymVal x5147 = SymStack.pop();
Num x5148 = Stack.pop();
SymStack.pop();
int x5149 = x5148.toInt();
Memory.storeInt(x5149, 8, x5146.toInt());
SymMemory.storeSym(x5149, 8, x5147);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5150 = Stack.pop();
SymVal x5151 = SymStack.pop();
Num x5152 = Stack.pop();
SymVal x5153 = SymStack.pop();
Num x5154 = x5152.i32_add(x5150);
Stack.push(x5154);
bool x5155 = allConcrete(x5153, x5151);
SymVal x5156 = x5155 ? Concrete(x5154, 32) : x5153.add(x5151);
SymStack.push(x5156);
}
{
Num x5157 = Stack.pop();
SymVal x5158 = SymStack.pop();
Frames.set(3, x5157);
SymFrames.set(3, x5158);
}
info("Jump to 1");
__attribute__((musttail)) return x5159(std::monostate{});
return std::monostate{};
}
std::monostate x4960(std::monostate x4961) {
info("Entering the false branch 52 of the if");
__attribute__((musttail)) return x4846(std::monostate{});
return std::monostate{};
}
std::monostate x4846(std::monostate x4847) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4848 = Stack.pop();
SymStack.pop();
Num x4849 = I32V(Memory.loadInt(x4848.toInt(), 0));
SymVal x4850 = SymMemory.loadSym(x4848.toInt(), 0);
Stack.push(x4849);
SymStack.push(x4850);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4851 = Stack.pop();
SymVal x4852 = SymStack.pop();
Num x4853 = Stack.pop();
SymVal x4854 = SymStack.pop();
Num x4855 = x4853.i32_sub(x4851);
Stack.push(x4855);
bool x4856 = allConcrete(x4854, x4852);
SymVal x4857 = x4856 ? Concrete(x4855, 32) : x4854.minus(x4852);
SymStack.push(x4857);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4858 = Stack.pop();
SymVal x4859 = SymStack.pop();
Num x4860 = Stack.pop();
SymVal x4861 = SymStack.pop();
Num x4862 = x4860.i32_mul(x4858);
Stack.push(x4862);
bool x4863 = allConcrete(x4861, x4859);
SymVal x4864 = x4863 ? Concrete(x4862, 32) : x4861.mul(x4859);
SymStack.push(x4864);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4865 = Stack.pop();
SymVal x4866 = SymStack.pop();
Num x4867 = Stack.pop();
SymVal x4868 = SymStack.pop();
Num x4869 = x4867.i32_add(x4865);
Stack.push(x4869);
bool x4870 = allConcrete(x4868, x4866);
SymVal x4871 = x4870 ? Concrete(x4869, 32) : x4868.add(x4866);
SymStack.push(x4871);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4872 = Stack.pop();
SymVal x4873 = SymStack.pop();
Num x4874 = Stack.pop();
SymVal x4875 = SymStack.pop();
Num x4876 = x4874.i32_mul(x4872);
Stack.push(x4876);
bool x4877 = allConcrete(x4875, x4873);
SymVal x4878 = x4877 ? Concrete(x4876, 32) : x4875.mul(x4873);
SymStack.push(x4878);
}
{
Num x4879 = Stack.pop();
SymVal x4880 = SymStack.pop();
Num x4881 = Stack.pop();
SymVal x4882 = SymStack.pop();
Num x4883 = x4881.i32_add(x4879);
Stack.push(x4883);
bool x4884 = allConcrete(x4882, x4880);
SymVal x4885 = x4884 ? Concrete(x4883, 32) : x4882.add(x4880);
SymStack.push(x4885);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x4886 = Stack.pop();
SymVal x4887 = SymStack.pop();
Num x4888 = Stack.pop();
SymVal x4889 = SymStack.pop();
Num x4890 = x4888.i32_add(x4886);
Stack.push(x4890);
bool x4891 = allConcrete(x4889, x4887);
SymVal x4892 = x4891 ? Concrete(x4890, 32) : x4889.add(x4887);
SymStack.push(x4892);
}
{
Num x4893 = Stack.pop();
SymStack.pop();
Num x4894 = I32V(Memory.loadInt(x4893.toInt(), 8));
SymVal x4895 = SymMemory.loadSym(x4893.toInt(), 8);
Stack.push(x4894);
SymStack.push(x4895);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4896 = Stack.pop();
SymStack.pop();
Num x4897 = I32V(Memory.loadInt(x4896.toInt(), 0));
SymVal x4898 = SymMemory.loadSym(x4896.toInt(), 0);
Stack.push(x4897);
SymStack.push(x4898);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4899 = Stack.pop();
SymVal x4900 = SymStack.pop();
Num x4901 = Stack.pop();
SymVal x4902 = SymStack.pop();
Num x4903 = x4901.i32_sub(x4899);
Stack.push(x4903);
bool x4904 = allConcrete(x4902, x4900);
SymVal x4905 = x4904 ? Concrete(x4903, 32) : x4902.minus(x4900);
SymStack.push(x4905);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4906 = Stack.pop();
SymVal x4907 = SymStack.pop();
Num x4908 = Stack.pop();
SymVal x4909 = SymStack.pop();
Num x4910 = x4908.i32_mul(x4906);
Stack.push(x4910);
bool x4911 = allConcrete(x4909, x4907);
SymVal x4912 = x4911 ? Concrete(x4910, 32) : x4909.mul(x4907);
SymStack.push(x4912);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4913 = Stack.pop();
SymVal x4914 = SymStack.pop();
Num x4915 = Stack.pop();
SymVal x4916 = SymStack.pop();
Num x4917 = x4915.i32_add(x4913);
Stack.push(x4917);
bool x4918 = allConcrete(x4916, x4914);
SymVal x4919 = x4918 ? Concrete(x4917, 32) : x4916.add(x4914);
SymStack.push(x4919);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4920 = Stack.pop();
SymVal x4921 = SymStack.pop();
Num x4922 = Stack.pop();
SymVal x4923 = SymStack.pop();
Num x4924 = x4922.i32_mul(x4920);
Stack.push(x4924);
bool x4925 = allConcrete(x4923, x4921);
SymVal x4926 = x4925 ? Concrete(x4924, 32) : x4923.mul(x4921);
SymStack.push(x4926);
}
{
Num x4927 = Stack.pop();
SymVal x4928 = SymStack.pop();
Num x4929 = Stack.pop();
SymVal x4930 = SymStack.pop();
Num x4931 = x4929.i32_add(x4927);
Stack.push(x4931);
bool x4932 = allConcrete(x4930, x4928);
SymVal x4933 = x4932 ? Concrete(x4931, 32) : x4930.add(x4928);
SymStack.push(x4933);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x4934 = Stack.pop();
SymVal x4935 = SymStack.pop();
Num x4936 = Stack.pop();
SymVal x4937 = SymStack.pop();
Num x4938 = x4936.i32_add(x4934);
Stack.push(x4938);
bool x4939 = allConcrete(x4937, x4935);
SymVal x4940 = x4939 ? Concrete(x4938, 32) : x4937.add(x4935);
SymStack.push(x4940);
}
{
Num x4941 = Stack.pop();
SymStack.pop();
Num x4942 = I32V(Memory.loadInt(x4941.toInt(), 8));
SymVal x4943 = SymMemory.loadSym(x4941.toInt(), 8);
Stack.push(x4942);
SymStack.push(x4943);
}
{
Num x4944 = Stack.pop();
SymStack.pop();
Num x4945 = I32V(Memory.loadInt(x4944.toInt(), 4));
SymVal x4946 = SymMemory.loadSym(x4944.toInt(), 4);
Stack.push(x4945);
SymStack.push(x4946);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4947 = Stack.pop();
SymVal x4948 = SymStack.pop();
Num x4949 = Stack.pop();
SymVal x4950 = SymStack.pop();
Num x4951 = x4949.i32_sub(x4947);
Stack.push(x4951);
bool x4952 = allConcrete(x4950, x4948);
SymVal x4953 = x4952 ? Concrete(x4951, 32) : x4950.minus(x4948);
SymStack.push(x4953);
}
{
Num x4954 = Stack.pop();
SymVal x4955 = SymStack.pop();
Num x4956 = Stack.pop();
SymStack.pop();
int x4957 = x4956.toInt();
Memory.storeInt(x4957, 4, x4954.toInt());
SymMemory.storeSym(x4957, 4, x4955);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4958 = Stack.pop();
SymVal x4959 = SymStack.pop();
Frames.set(4, x4958);
SymFrames.set(4, x4959);
}
__attribute__((musttail)) return x4692(std::monostate{});
return std::monostate{};
}
std::monostate x4696(std::monostate x4697) {
info("Entering the false branch 51 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4698 = Stack.pop();
SymStack.pop();
Num x4699 = I32V(Memory.loadInt(x4698.toInt(), 0));
SymVal x4700 = SymMemory.loadSym(x4698.toInt(), 0);
Stack.push(x4699);
SymStack.push(x4700);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4701 = Stack.pop();
SymVal x4702 = SymStack.pop();
Num x4703 = Stack.pop();
SymVal x4704 = SymStack.pop();
Num x4705 = x4703.i32_sub(x4701);
Stack.push(x4705);
bool x4706 = allConcrete(x4704, x4702);
SymVal x4707 = x4706 ? Concrete(x4705, 32) : x4704.minus(x4702);
SymStack.push(x4707);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4708 = Stack.pop();
SymVal x4709 = SymStack.pop();
Num x4710 = Stack.pop();
SymVal x4711 = SymStack.pop();
Num x4712 = x4710.i32_mul(x4708);
Stack.push(x4712);
bool x4713 = allConcrete(x4711, x4709);
SymVal x4714 = x4713 ? Concrete(x4712, 32) : x4711.mul(x4709);
SymStack.push(x4714);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4715 = Stack.pop();
SymVal x4716 = SymStack.pop();
Num x4717 = Stack.pop();
SymVal x4718 = SymStack.pop();
Num x4719 = x4717.i32_add(x4715);
Stack.push(x4719);
bool x4720 = allConcrete(x4718, x4716);
SymVal x4721 = x4720 ? Concrete(x4719, 32) : x4718.add(x4716);
SymStack.push(x4721);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4722 = Stack.pop();
SymVal x4723 = SymStack.pop();
Num x4724 = Stack.pop();
SymVal x4725 = SymStack.pop();
Num x4726 = x4724.i32_mul(x4722);
Stack.push(x4726);
bool x4727 = allConcrete(x4725, x4723);
SymVal x4728 = x4727 ? Concrete(x4726, 32) : x4725.mul(x4723);
SymStack.push(x4728);
}
{
Num x4729 = Stack.pop();
SymVal x4730 = SymStack.pop();
Num x4731 = Stack.pop();
SymVal x4732 = SymStack.pop();
Num x4733 = x4731.i32_add(x4729);
Stack.push(x4733);
bool x4734 = allConcrete(x4732, x4730);
SymVal x4735 = x4734 ? Concrete(x4733, 32) : x4732.add(x4730);
SymStack.push(x4735);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x4736 = Stack.pop();
SymVal x4737 = SymStack.pop();
Num x4738 = Stack.pop();
SymVal x4739 = SymStack.pop();
Num x4740 = x4738.i32_add(x4736);
Stack.push(x4740);
bool x4741 = allConcrete(x4739, x4737);
SymVal x4742 = x4741 ? Concrete(x4740, 32) : x4739.add(x4737);
SymStack.push(x4742);
}
{
Num x4743 = Stack.pop();
SymStack.pop();
Num x4744 = I32V(Memory.loadInt(x4743.toInt(), 8));
SymVal x4745 = SymMemory.loadSym(x4743.toInt(), 8);
Stack.push(x4744);
SymStack.push(x4745);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x4746 = Stack.pop();
SymVal x4747 = SymStack.pop();
Num x4748 = Stack.pop();
SymVal x4749 = SymStack.pop();
Num x4750 = x4748.i32_mul(x4746);
Stack.push(x4750);
bool x4751 = allConcrete(x4749, x4747);
SymVal x4752 = x4751 ? Concrete(x4750, 32) : x4749.mul(x4747);
SymStack.push(x4752);
}
{
Num x4753 = Stack.pop();
SymVal x4754 = SymStack.pop();
Num x4755 = Stack.pop();
SymVal x4756 = SymStack.pop();
Num x4757 = x4755.i32_add(x4753);
Stack.push(x4757);
bool x4758 = allConcrete(x4756, x4754);
SymVal x4759 = x4758 ? Concrete(x4757, 32) : x4756.add(x4754);
SymStack.push(x4759);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4760 = Stack.pop();
SymStack.pop();
Num x4761 = I32V(Memory.loadInt(x4760.toInt(), 0));
SymVal x4762 = SymMemory.loadSym(x4760.toInt(), 0);
Stack.push(x4761);
SymStack.push(x4762);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4763 = Stack.pop();
SymVal x4764 = SymStack.pop();
Num x4765 = Stack.pop();
SymVal x4766 = SymStack.pop();
Num x4767 = x4765.i32_sub(x4763);
Stack.push(x4767);
bool x4768 = allConcrete(x4766, x4764);
SymVal x4769 = x4768 ? Concrete(x4767, 32) : x4766.minus(x4764);
SymStack.push(x4769);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4770 = Stack.pop();
SymVal x4771 = SymStack.pop();
Num x4772 = Stack.pop();
SymVal x4773 = SymStack.pop();
Num x4774 = x4772.i32_mul(x4770);
Stack.push(x4774);
bool x4775 = allConcrete(x4773, x4771);
SymVal x4776 = x4775 ? Concrete(x4774, 32) : x4773.mul(x4771);
SymStack.push(x4776);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4777 = Stack.pop();
SymVal x4778 = SymStack.pop();
Num x4779 = Stack.pop();
SymVal x4780 = SymStack.pop();
Num x4781 = x4779.i32_add(x4777);
Stack.push(x4781);
bool x4782 = allConcrete(x4780, x4778);
SymVal x4783 = x4782 ? Concrete(x4781, 32) : x4780.add(x4778);
SymStack.push(x4783);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4784 = Stack.pop();
SymVal x4785 = SymStack.pop();
Num x4786 = Stack.pop();
SymVal x4787 = SymStack.pop();
Num x4788 = x4786.i32_mul(x4784);
Stack.push(x4788);
bool x4789 = allConcrete(x4787, x4785);
SymVal x4790 = x4789 ? Concrete(x4788, 32) : x4787.mul(x4785);
SymStack.push(x4790);
}
{
Num x4791 = Stack.pop();
SymVal x4792 = SymStack.pop();
Num x4793 = Stack.pop();
SymVal x4794 = SymStack.pop();
Num x4795 = x4793.i32_add(x4791);
Stack.push(x4795);
bool x4796 = allConcrete(x4794, x4792);
SymVal x4797 = x4796 ? Concrete(x4795, 32) : x4794.add(x4792);
SymStack.push(x4797);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x4798 = Stack.pop();
SymVal x4799 = SymStack.pop();
Num x4800 = Stack.pop();
SymVal x4801 = SymStack.pop();
Num x4802 = x4800.i32_add(x4798);
Stack.push(x4802);
bool x4803 = allConcrete(x4801, x4799);
SymVal x4804 = x4803 ? Concrete(x4802, 32) : x4801.add(x4799);
SymStack.push(x4804);
}
{
Num x4805 = Stack.pop();
SymStack.pop();
Num x4806 = I32V(Memory.loadInt(x4805.toInt(), 8));
SymVal x4807 = SymMemory.loadSym(x4805.toInt(), 8);
Stack.push(x4806);
SymStack.push(x4807);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4808 = Stack.pop();
SymVal x4809 = SymStack.pop();
Num x4810 = Stack.pop();
SymVal x4811 = SymStack.pop();
Num x4812 = x4810.i32_add(x4808);
Stack.push(x4812);
bool x4813 = allConcrete(x4811, x4809);
SymVal x4814 = x4813 ? Concrete(x4812, 32) : x4811.add(x4809);
SymStack.push(x4814);
}
{
Num x4815 = Stack.pop();
SymVal x4816 = SymStack.pop();
Num x4817 = Stack.pop();
SymVal x4818 = SymStack.pop();
Num x4819 = x4817.i32_mul(x4815);
Stack.push(x4819);
bool x4820 = allConcrete(x4818, x4816);
SymVal x4821 = x4820 ? Concrete(x4819, 32) : x4818.mul(x4816);
SymStack.push(x4821);
}
{
Num x4822 = Stack.pop();
SymVal x4823 = SymStack.pop();
Num x4824 = Stack.pop();
SymVal x4825 = SymStack.pop();
Num x4826 = x4824.i32_add(x4822);
Stack.push(x4826);
bool x4827 = allConcrete(x4825, x4823);
SymVal x4828 = x4827 ? Concrete(x4826, 32) : x4825.add(x4823);
SymStack.push(x4828);
}
{
Num x4829 = Stack.pop();
SymStack.pop();
Num x4830 = I32V(Memory.loadInt(x4829.toInt(), 8));
SymVal x4831 = SymMemory.loadSym(x4829.toInt(), 8);
Stack.push(x4830);
SymStack.push(x4831);
}
{
Num x4832 = Stack.pop();
SymVal x4833 = SymStack.pop();
Num x4834 = Stack.pop();
SymStack.pop();
int x4835 = x4834.toInt();
Memory.storeInt(x4835, 8, x4832.toInt());
SymMemory.storeSym(x4835, 8, x4833);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4836 = Stack.pop();
SymVal x4837 = SymStack.pop();
Num x4838 = Stack.pop();
SymVal x4839 = SymStack.pop();
Num x4840 = x4838.i32_add(x4836);
Stack.push(x4840);
bool x4841 = allConcrete(x4839, x4837);
SymVal x4842 = x4841 ? Concrete(x4840, 32) : x4839.add(x4837);
SymStack.push(x4842);
}
{
Num x4843 = Stack.pop();
SymVal x4844 = SymStack.pop();
Frames.set(3, x4843);
SymFrames.set(3, x4844);
}
info("Jump to 1");
__attribute__((musttail)) return x4845(std::monostate{});
return std::monostate{};
}
std::monostate x4694(std::monostate x4695) {
info("Entering the false branch 49 of the if");
__attribute__((musttail)) return x4692(std::monostate{});
return std::monostate{};
}
std::monostate x4692(std::monostate x4693) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x4657(std::monostate{});
return std::monostate{};
}
std::monostate x4690(std::monostate x4691) {
info("Entering the false branch 31 of the if");
__attribute__((musttail)) return x4657(std::monostate{});
return std::monostate{};
}
std::monostate x4657(std::monostate x4658) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(Frames.get(4));
SymStack.push(SymFrames.get(4));
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x4659 = Stack.pop();
SymVal x4660 = SymStack.pop();
Num x4661 = Stack.pop();
SymVal x4662 = SymStack.pop();
Num x4663 = x4661.i32_eq(x4659);
Stack.push(x4663);
bool x4664 = allConcrete(x4662, x4660);
SymVal x4665 = x4664 ? Concrete(x4663, 32) : x4662.eq(x4660).bool2bv();
SymStack.push(x4665);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4666 = Stack.pop();
SymVal x4667 = SymStack.pop();
Num x4668 = Stack.pop();
SymVal x4669 = SymStack.pop();
Num x4670 = x4668.i32_sub(x4666);
Stack.push(x4670);
bool x4671 = allConcrete(x4669, x4667);
SymVal x4672 = x4671 ? Concrete(x4670, 32) : x4669.minus(x4667);
SymStack.push(x4672);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4673 = Stack.pop();
SymVal x4674 = SymStack.pop();
Num x4675 = Stack.pop();
SymVal x4676 = SymStack.pop();
Num x4677 = x4675.i32_ge_s(x4673);
Stack.push(x4677);
bool x4678 = allConcrete(x4676, x4674);
SymVal x4679 = x4678 ? Concrete(x4677, 32) : x4676.ge(x4674).bool2bv();
SymStack.push(x4679);
}
{
Num x4680 = Stack.pop();
SymVal x4681 = SymStack.pop();
Num x4682 = Stack.pop();
SymVal x4683 = SymStack.pop();
Num x4684 = x4682.i32_and(x4680);
Stack.push(x4684);
bool x4685 = allConcrete(x4683, x4681);
SymVal x4686 = x4685 ? Concrete(x4684, 32) : x4683.bitwise_and(x4681);
SymStack.push(x4686);
}
Num x4687 = Stack.pop();
{
SymVal x4688 = SymStack.pop();
ExploreTree.fillIfElseNode(x4688, 32);
}
int x4689 = x4687.toInt();
if (x4689 != 0) {
ExploreTree.moveCursor(true, makeControl(x3837, CURRENT_MCONT));
__attribute__((musttail)) return x4584(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x4584, CURRENT_MCONT));
__attribute__((musttail)) return x3837(std::monostate{});
}
return std::monostate{};
}
std::monostate x4584(std::monostate x4585) {
info("Entering the true branch 32 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4586 = Stack.pop();
SymStack.pop();
Num x4587 = I32V(Memory.loadInt(x4586.toInt(), 0));
SymVal x4588 = SymMemory.loadSym(x4586.toInt(), 0);
Stack.push(x4587);
SymStack.push(x4588);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4589 = Stack.pop();
SymVal x4590 = SymStack.pop();
Num x4591 = Stack.pop();
SymVal x4592 = SymStack.pop();
Num x4593 = x4591.i32_sub(x4589);
Stack.push(x4593);
bool x4594 = allConcrete(x4592, x4590);
SymVal x4595 = x4594 ? Concrete(x4593, 32) : x4592.minus(x4590);
SymStack.push(x4595);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4596 = Stack.pop();
SymVal x4597 = SymStack.pop();
Num x4598 = Stack.pop();
SymVal x4599 = SymStack.pop();
Num x4600 = x4598.i32_mul(x4596);
Stack.push(x4600);
bool x4601 = allConcrete(x4599, x4597);
SymVal x4602 = x4601 ? Concrete(x4600, 32) : x4599.mul(x4597);
SymStack.push(x4602);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4603 = Stack.pop();
SymVal x4604 = SymStack.pop();
Num x4605 = Stack.pop();
SymVal x4606 = SymStack.pop();
Num x4607 = x4605.i32_sub(x4603);
Stack.push(x4607);
bool x4608 = allConcrete(x4606, x4604);
SymVal x4609 = x4608 ? Concrete(x4607, 32) : x4606.minus(x4604);
SymStack.push(x4609);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4610 = Stack.pop();
SymVal x4611 = SymStack.pop();
Num x4612 = Stack.pop();
SymVal x4613 = SymStack.pop();
Num x4614 = x4612.i32_mul(x4610);
Stack.push(x4614);
bool x4615 = allConcrete(x4613, x4611);
SymVal x4616 = x4615 ? Concrete(x4614, 32) : x4613.mul(x4611);
SymStack.push(x4616);
}
{
Num x4617 = Stack.pop();
SymVal x4618 = SymStack.pop();
Num x4619 = Stack.pop();
SymVal x4620 = SymStack.pop();
Num x4621 = x4619.i32_add(x4617);
Stack.push(x4621);
bool x4622 = allConcrete(x4620, x4618);
SymVal x4623 = x4622 ? Concrete(x4621, 32) : x4620.add(x4618);
SymStack.push(x4623);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x4624 = Stack.pop();
SymVal x4625 = SymStack.pop();
Num x4626 = Stack.pop();
SymVal x4627 = SymStack.pop();
Num x4628 = x4626.i32_add(x4624);
Stack.push(x4628);
bool x4629 = allConcrete(x4627, x4625);
SymVal x4630 = x4629 ? Concrete(x4628, 32) : x4627.add(x4625);
SymStack.push(x4630);
}
{
Num x4631 = Stack.pop();
SymStack.pop();
Num x4632 = I32V(Memory.loadInt(x4631.toInt(), 8));
SymVal x4633 = SymMemory.loadSym(x4631.toInt(), 8);
Stack.push(x4632);
SymStack.push(x4633);
}
{
Num x4634 = Stack.pop();
SymStack.pop();
Num x4635 = I32V(Memory.loadInt(x4634.toInt(), 4));
SymVal x4636 = SymMemory.loadSym(x4634.toInt(), 4);
Stack.push(x4635);
SymStack.push(x4636);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4637 = Stack.pop();
SymStack.pop();
Num x4638 = I32V(Memory.loadInt(x4637.toInt(), 0));
SymVal x4639 = SymMemory.loadSym(x4637.toInt(), 0);
Stack.push(x4638);
SymStack.push(x4639);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x4640 = Stack.pop();
SymVal x4641 = SymStack.pop();
Num x4642 = Stack.pop();
SymVal x4643 = SymStack.pop();
Num x4644 = x4642.i32_div_s(x4640);
Stack.push(x4644);
bool x4645 = allConcrete(x4643, x4641);
SymVal x4646 = x4645 ? Concrete(x4644, 32) : x4643.div(x4641);
SymStack.push(x4646);
}
{
Num x4647 = Stack.pop();
SymVal x4648 = SymStack.pop();
Num x4649 = Stack.pop();
SymVal x4650 = SymStack.pop();
Num x4651 = x4649.i32_ge_s(x4647);
Stack.push(x4651);
bool x4652 = allConcrete(x4650, x4648);
SymVal x4653 = x4652 ? Concrete(x4651, 32) : x4650.ge(x4648).bool2bv();
SymStack.push(x4653);
}
Num x4654 = Stack.pop();
{
SymVal x4655 = SymStack.pop();
ExploreTree.fillIfElseNode(x4655, 44);
}
int x4656 = x4654.toInt();
if (x4656 != 0) {
ExploreTree.moveCursor(true, makeControl(x3841, CURRENT_MCONT));
__attribute__((musttail)) return x4577(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x4577, CURRENT_MCONT));
__attribute__((musttail)) return x3841(std::monostate{});
}
return std::monostate{};
}
std::monostate x4577(std::monostate x4578) {
info("Entering the true branch 44 of the if");
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x4579 = Stack.pop();
SymStack.pop();
Num x4580 = I32V(Memory.loadInt(x4579.toInt(), 4));
SymVal x4581 = SymMemory.loadSym(x4579.toInt(), 4);
Stack.push(x4580);
SymStack.push(x4581);
}
{
Num x4582 = Stack.pop();
SymVal x4583 = SymStack.pop();
Frames.set(3, x4582);
SymFrames.set(3, x4583);
}
__attribute__((musttail)) return x3896(std::monostate{});
return std::monostate{};
}
std::monostate x3896(std::monostate x4566) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4567 = Stack.pop();
SymVal x4568 = SymStack.pop();
Num x4569 = Stack.pop();
SymVal x4570 = SymStack.pop();
Num x4571 = x4569.i32_eq(x4567);
Stack.push(x4571);
bool x4572 = allConcrete(x4570, x4568);
SymVal x4573 = x4572 ? Concrete(x4571, 32) : x4570.eq(x4568).bool2bv();
SymStack.push(x4573);
}
Num x4574 = Stack.pop();
{
SymVal x4575 = SymStack.pop();
ExploreTree.fillIfElseNode(x4575, 45);
}
int x4576 = x4574.toInt();
if (x4576 != 0) {
ExploreTree.moveCursor(true, makeControl(x3843, CURRENT_MCONT));
__attribute__((musttail)) return x4564(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x4564, CURRENT_MCONT));
__attribute__((musttail)) return x3843(std::monostate{});
}
return std::monostate{};
}
std::monostate x4564(std::monostate x4565) {
info("Entering the true branch 45 of the if");
info("Jump to 2");
__attribute__((musttail)) return x4549(std::monostate{});
return std::monostate{};
}
std::monostate x4549(std::monostate x4550) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x4551 = Stack.pop();
SymStack.pop();
Num x4552 = I32V(Memory.loadInt(x4551.toInt(), 0));
SymVal x4553 = SymMemory.loadSym(x4551.toInt(), 0);
Stack.push(x4552);
SymStack.push(x4553);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4554 = Stack.pop();
SymVal x4555 = SymStack.pop();
Num x4556 = Stack.pop();
SymVal x4557 = SymStack.pop();
Num x4558 = x4556.i32_ne(x4554);
Stack.push(x4558);
bool x4559 = allConcrete(x4557, x4555);
SymVal x4560 = x4559 ? Concrete(x4558, 32) : x4557.neq(x4555).bool2bv();
SymStack.push(x4560);
}
Num x4561 = Stack.pop();
{
SymVal x4562 = SymStack.pop();
ExploreTree.fillIfElseNode(x4562, 46);
}
int x4563 = x4561.toInt();
if (x4563 != 0) {
ExploreTree.moveCursor(true, makeControl(x4416, CURRENT_MCONT));
__attribute__((musttail)) return x4535(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x4535, CURRENT_MCONT));
__attribute__((musttail)) return x4416(std::monostate{});
}
return std::monostate{};
}
std::monostate x4535(std::monostate x4536) {
info("Entering the true branch 46 of the if");
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x4537 = Stack.pop();
SymStack.pop();
Num x4538 = I32V(Memory.loadInt(x4537.toInt(), 4));
SymVal x4539 = SymMemory.loadSym(x4537.toInt(), 4);
Stack.push(x4538);
SymStack.push(x4539);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4540 = Stack.pop();
SymVal x4541 = SymStack.pop();
Num x4542 = Stack.pop();
SymVal x4543 = SymStack.pop();
Num x4544 = x4542.i32_add(x4540);
Stack.push(x4544);
bool x4545 = allConcrete(x4543, x4541);
SymVal x4546 = x4545 ? Concrete(x4544, 32) : x4543.add(x4541);
SymStack.push(x4546);
}
{
Num x4547 = Stack.pop();
SymVal x4548 = SymStack.pop();
Frames.set(3, x4547);
SymFrames.set(3, x4548);
}
__attribute__((musttail)) return x4519(std::monostate{});
return std::monostate{};
}
std::monostate x4519(std::monostate x4524) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4525 = Stack.pop();
SymVal x4526 = SymStack.pop();
Num x4527 = Stack.pop();
SymVal x4528 = SymStack.pop();
Num x4529 = x4527.i32_eq(x4525);
Stack.push(x4529);
bool x4530 = allConcrete(x4528, x4526);
SymVal x4531 = x4530 ? Concrete(x4529, 32) : x4528.eq(x4526).bool2bv();
SymStack.push(x4531);
}
Num x4532 = Stack.pop();
{
SymVal x4533 = SymStack.pop();
ExploreTree.fillIfElseNode(x4533, 48);
}
int x4534 = x4532.toInt();
if (x4534 != 0) {
ExploreTree.moveCursor(true, makeControl(x4418, CURRENT_MCONT));
__attribute__((musttail)) return x4522(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x4522, CURRENT_MCONT));
__attribute__((musttail)) return x4418(std::monostate{});
}
return std::monostate{};
}
std::monostate x4522(std::monostate x4523) {
info("Entering the true branch 48 of the if");
info("Jump to 2");
__attribute__((musttail)) return x4520(std::monostate{});
return std::monostate{};
}
std::monostate x4520(std::monostate x4521) {
info("Exiting the block, stackSize =", Stack.size());
__attribute__((musttail)) return x4094(std::monostate{});
return std::monostate{};
}
std::monostate x4418(std::monostate x4419) {
info("Entering the false branch 48 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4420 = Stack.pop();
SymStack.pop();
Num x4421 = I32V(Memory.loadInt(x4420.toInt(), 0));
SymVal x4422 = SymMemory.loadSym(x4420.toInt(), 0);
Stack.push(x4421);
SymStack.push(x4422);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4423 = Stack.pop();
SymVal x4424 = SymStack.pop();
Num x4425 = Stack.pop();
SymVal x4426 = SymStack.pop();
Num x4427 = x4425.i32_sub(x4423);
Stack.push(x4427);
bool x4428 = allConcrete(x4426, x4424);
SymVal x4429 = x4428 ? Concrete(x4427, 32) : x4426.minus(x4424);
SymStack.push(x4429);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4430 = Stack.pop();
SymVal x4431 = SymStack.pop();
Num x4432 = Stack.pop();
SymVal x4433 = SymStack.pop();
Num x4434 = x4432.i32_mul(x4430);
Stack.push(x4434);
bool x4435 = allConcrete(x4433, x4431);
SymVal x4436 = x4435 ? Concrete(x4434, 32) : x4433.mul(x4431);
SymStack.push(x4436);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4437 = Stack.pop();
SymVal x4438 = SymStack.pop();
Num x4439 = Stack.pop();
SymVal x4440 = SymStack.pop();
Num x4441 = x4439.i32_mul(x4437);
Stack.push(x4441);
bool x4442 = allConcrete(x4440, x4438);
SymVal x4443 = x4442 ? Concrete(x4441, 32) : x4440.mul(x4438);
SymStack.push(x4443);
}
{
Num x4444 = Stack.pop();
SymVal x4445 = SymStack.pop();
Num x4446 = Stack.pop();
SymVal x4447 = SymStack.pop();
Num x4448 = x4446.i32_add(x4444);
Stack.push(x4448);
bool x4449 = allConcrete(x4447, x4445);
SymVal x4450 = x4449 ? Concrete(x4448, 32) : x4447.add(x4445);
SymStack.push(x4450);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x4451 = Stack.pop();
SymVal x4452 = SymStack.pop();
Num x4453 = Stack.pop();
SymVal x4454 = SymStack.pop();
Num x4455 = x4453.i32_add(x4451);
Stack.push(x4455);
bool x4456 = allConcrete(x4454, x4452);
SymVal x4457 = x4456 ? Concrete(x4455, 32) : x4454.add(x4452);
SymStack.push(x4457);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4458 = Stack.pop();
SymStack.pop();
Num x4459 = I32V(Memory.loadInt(x4458.toInt(), 0));
SymVal x4460 = SymMemory.loadSym(x4458.toInt(), 0);
Stack.push(x4459);
SymStack.push(x4460);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4461 = Stack.pop();
SymVal x4462 = SymStack.pop();
Num x4463 = Stack.pop();
SymVal x4464 = SymStack.pop();
Num x4465 = x4463.i32_sub(x4461);
Stack.push(x4465);
bool x4466 = allConcrete(x4464, x4462);
SymVal x4467 = x4466 ? Concrete(x4465, 32) : x4464.minus(x4462);
SymStack.push(x4467);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4468 = Stack.pop();
SymVal x4469 = SymStack.pop();
Num x4470 = Stack.pop();
SymVal x4471 = SymStack.pop();
Num x4472 = x4470.i32_mul(x4468);
Stack.push(x4472);
bool x4473 = allConcrete(x4471, x4469);
SymVal x4474 = x4473 ? Concrete(x4472, 32) : x4471.mul(x4469);
SymStack.push(x4474);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4475 = Stack.pop();
SymVal x4476 = SymStack.pop();
Num x4477 = Stack.pop();
SymVal x4478 = SymStack.pop();
Num x4479 = x4477.i32_sub(x4475);
Stack.push(x4479);
bool x4480 = allConcrete(x4478, x4476);
SymVal x4481 = x4480 ? Concrete(x4479, 32) : x4478.minus(x4476);
SymStack.push(x4481);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4482 = Stack.pop();
SymVal x4483 = SymStack.pop();
Num x4484 = Stack.pop();
SymVal x4485 = SymStack.pop();
Num x4486 = x4484.i32_mul(x4482);
Stack.push(x4486);
bool x4487 = allConcrete(x4485, x4483);
SymVal x4488 = x4487 ? Concrete(x4486, 32) : x4485.mul(x4483);
SymStack.push(x4488);
}
{
Num x4489 = Stack.pop();
SymVal x4490 = SymStack.pop();
Num x4491 = Stack.pop();
SymVal x4492 = SymStack.pop();
Num x4493 = x4491.i32_add(x4489);
Stack.push(x4493);
bool x4494 = allConcrete(x4492, x4490);
SymVal x4495 = x4494 ? Concrete(x4493, 32) : x4492.add(x4490);
SymStack.push(x4495);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x4496 = Stack.pop();
SymVal x4497 = SymStack.pop();
Num x4498 = Stack.pop();
SymVal x4499 = SymStack.pop();
Num x4500 = x4498.i32_add(x4496);
Stack.push(x4500);
bool x4501 = allConcrete(x4499, x4497);
SymVal x4502 = x4501 ? Concrete(x4500, 32) : x4499.add(x4497);
SymStack.push(x4502);
}
{
Num x4503 = Stack.pop();
SymStack.pop();
Num x4504 = I32V(Memory.loadInt(x4503.toInt(), 8));
SymVal x4505 = SymMemory.loadSym(x4503.toInt(), 8);
Stack.push(x4504);
SymStack.push(x4505);
}
{
Num x4506 = Stack.pop();
SymVal x4507 = SymStack.pop();
Num x4508 = Stack.pop();
SymStack.pop();
int x4509 = x4508.toInt();
Memory.storeInt(x4509, 8, x4506.toInt());
SymMemory.storeSym(x4509, 8, x4507);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4510 = Stack.pop();
SymVal x4511 = SymStack.pop();
Num x4512 = Stack.pop();
SymVal x4513 = SymStack.pop();
Num x4514 = x4512.i32_sub(x4510);
Stack.push(x4514);
bool x4515 = allConcrete(x4513, x4511);
SymVal x4516 = x4515 ? Concrete(x4514, 32) : x4513.minus(x4511);
SymStack.push(x4516);
}
{
Num x4517 = Stack.pop();
SymVal x4518 = SymStack.pop();
Frames.set(3, x4517);
SymFrames.set(3, x4518);
}
info("Jump to 1");
__attribute__((musttail)) return x4519(std::monostate{});
return std::monostate{};
}
std::monostate x4416(std::monostate x4417) {
info("Entering the false branch 46 of the if");
__attribute__((musttail)) return x4094(std::monostate{});
return std::monostate{};
}
std::monostate x4094(std::monostate x4095) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x4096 = Stack.pop();
SymStack.pop();
Num x4097 = I32V(Memory.loadInt(x4096.toInt(), 4));
SymVal x4098 = SymMemory.loadSym(x4096.toInt(), 4);
Stack.push(x4097);
SymStack.push(x4098);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4099 = Stack.pop();
SymVal x4100 = SymStack.pop();
Num x4101 = Stack.pop();
SymVal x4102 = SymStack.pop();
Num x4103 = x4101.i32_add(x4099);
Stack.push(x4103);
bool x4104 = allConcrete(x4102, x4100);
SymVal x4105 = x4104 ? Concrete(x4103, 32) : x4102.add(x4100);
SymStack.push(x4105);
}
{
Num x4106 = Stack.pop();
SymVal x4107 = SymStack.pop();
Num x4108 = Stack.pop();
SymStack.pop();
int x4109 = x4108.toInt();
Memory.storeInt(x4109, 4, x4106.toInt());
SymMemory.storeSym(x4109, 4, x4107);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4110 = Stack.pop();
SymVal x4111 = SymStack.pop();
Num x4112 = Stack.pop();
SymVal x4113 = SymStack.pop();
Num x4114 = x4112.i32_mul(x4110);
Stack.push(x4114);
bool x4115 = allConcrete(x4113, x4111);
SymVal x4116 = x4115 ? Concrete(x4114, 32) : x4113.mul(x4111);
SymStack.push(x4116);
}
{
Num x4117 = Stack.pop();
SymVal x4118 = SymStack.pop();
Num x4119 = Stack.pop();
SymVal x4120 = SymStack.pop();
Num x4121 = x4119.i32_add(x4117);
Stack.push(x4121);
bool x4122 = allConcrete(x4120, x4118);
SymVal x4123 = x4122 ? Concrete(x4121, 32) : x4120.add(x4118);
SymStack.push(x4123);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4124 = Stack.pop();
SymVal x4125 = SymStack.pop();
Num x4126 = Stack.pop();
SymVal x4127 = SymStack.pop();
Num x4128 = x4126.i32_sub(x4124);
Stack.push(x4128);
bool x4129 = allConcrete(x4127, x4125);
SymVal x4130 = x4129 ? Concrete(x4128, 32) : x4127.minus(x4125);
SymStack.push(x4130);
}
{
Num x4131 = Stack.pop();
SymVal x4132 = SymStack.pop();
Num x4133 = Stack.pop();
SymVal x4134 = SymStack.pop();
Num x4135 = x4133.i32_mul(x4131);
Stack.push(x4135);
bool x4136 = allConcrete(x4134, x4132);
SymVal x4137 = x4136 ? Concrete(x4135, 32) : x4134.mul(x4132);
SymStack.push(x4137);
}
{
Num x4138 = Stack.pop();
SymVal x4139 = SymStack.pop();
Num x4140 = Stack.pop();
SymVal x4141 = SymStack.pop();
Num x4142 = x4140.i32_add(x4138);
Stack.push(x4142);
bool x4143 = allConcrete(x4141, x4139);
SymVal x4144 = x4143 ? Concrete(x4142, 32) : x4141.add(x4139);
SymStack.push(x4144);
}
{
Num x4145 = Stack.pop();
SymStack.pop();
Num x4146 = I32V(Memory.loadInt(x4145.toInt(), 8));
SymVal x4147 = SymMemory.loadSym(x4145.toInt(), 8);
Stack.push(x4146);
SymStack.push(x4147);
}
{
Num x4148 = Stack.pop();
SymVal x4149 = SymStack.pop();
Num x4150 = Stack.pop();
SymStack.pop();
int x4151 = x4150.toInt();
Memory.storeInt(x4151, 8, x4148.toInt());
SymMemory.storeSym(x4151, 8, x4149);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4152 = Stack.pop();
SymStack.pop();
Num x4153 = I32V(Memory.loadInt(x4152.toInt(), 0));
SymVal x4154 = SymMemory.loadSym(x4152.toInt(), 0);
Stack.push(x4153);
SymStack.push(x4154);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4155 = Stack.pop();
SymVal x4156 = SymStack.pop();
Num x4157 = Stack.pop();
SymVal x4158 = SymStack.pop();
Num x4159 = x4157.i32_sub(x4155);
Stack.push(x4159);
bool x4160 = allConcrete(x4158, x4156);
SymVal x4161 = x4160 ? Concrete(x4159, 32) : x4158.minus(x4156);
SymStack.push(x4161);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4162 = Stack.pop();
SymVal x4163 = SymStack.pop();
Num x4164 = Stack.pop();
SymVal x4165 = SymStack.pop();
Num x4166 = x4164.i32_mul(x4162);
Stack.push(x4166);
bool x4167 = allConcrete(x4165, x4163);
SymVal x4168 = x4167 ? Concrete(x4166, 32) : x4165.mul(x4163);
SymStack.push(x4168);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4169 = Stack.pop();
SymVal x4170 = SymStack.pop();
Num x4171 = Stack.pop();
SymVal x4172 = SymStack.pop();
Num x4173 = x4171.i32_sub(x4169);
Stack.push(x4173);
bool x4174 = allConcrete(x4172, x4170);
SymVal x4175 = x4174 ? Concrete(x4173, 32) : x4172.minus(x4170);
SymStack.push(x4175);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4176 = Stack.pop();
SymVal x4177 = SymStack.pop();
Num x4178 = Stack.pop();
SymVal x4179 = SymStack.pop();
Num x4180 = x4178.i32_mul(x4176);
Stack.push(x4180);
bool x4181 = allConcrete(x4179, x4177);
SymVal x4182 = x4181 ? Concrete(x4180, 32) : x4179.mul(x4177);
SymStack.push(x4182);
}
{
Num x4183 = Stack.pop();
SymVal x4184 = SymStack.pop();
Num x4185 = Stack.pop();
SymVal x4186 = SymStack.pop();
Num x4187 = x4185.i32_add(x4183);
Stack.push(x4187);
bool x4188 = allConcrete(x4186, x4184);
SymVal x4189 = x4188 ? Concrete(x4187, 32) : x4186.add(x4184);
SymStack.push(x4189);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x4190 = Stack.pop();
SymVal x4191 = SymStack.pop();
Num x4192 = Stack.pop();
SymVal x4193 = SymStack.pop();
Num x4194 = x4192.i32_add(x4190);
Stack.push(x4194);
bool x4195 = allConcrete(x4193, x4191);
SymVal x4196 = x4195 ? Concrete(x4194, 32) : x4193.add(x4191);
SymStack.push(x4196);
}
{
Num x4197 = Stack.pop();
SymStack.pop();
Num x4198 = I32V(Memory.loadInt(x4197.toInt(), 8));
SymVal x4199 = SymMemory.loadSym(x4197.toInt(), 8);
Stack.push(x4198);
SymStack.push(x4199);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4200 = Stack.pop();
SymStack.pop();
Num x4201 = I32V(Memory.loadInt(x4200.toInt(), 0));
SymVal x4202 = SymMemory.loadSym(x4200.toInt(), 0);
Stack.push(x4201);
SymStack.push(x4202);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4203 = Stack.pop();
SymVal x4204 = SymStack.pop();
Num x4205 = Stack.pop();
SymVal x4206 = SymStack.pop();
Num x4207 = x4205.i32_sub(x4203);
Stack.push(x4207);
bool x4208 = allConcrete(x4206, x4204);
SymVal x4209 = x4208 ? Concrete(x4207, 32) : x4206.minus(x4204);
SymStack.push(x4209);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4210 = Stack.pop();
SymVal x4211 = SymStack.pop();
Num x4212 = Stack.pop();
SymVal x4213 = SymStack.pop();
Num x4214 = x4212.i32_mul(x4210);
Stack.push(x4214);
bool x4215 = allConcrete(x4213, x4211);
SymVal x4216 = x4215 ? Concrete(x4214, 32) : x4213.mul(x4211);
SymStack.push(x4216);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4217 = Stack.pop();
SymVal x4218 = SymStack.pop();
Num x4219 = Stack.pop();
SymVal x4220 = SymStack.pop();
Num x4221 = x4219.i32_sub(x4217);
Stack.push(x4221);
bool x4222 = allConcrete(x4220, x4218);
SymVal x4223 = x4222 ? Concrete(x4221, 32) : x4220.minus(x4218);
SymStack.push(x4223);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4224 = Stack.pop();
SymVal x4225 = SymStack.pop();
Num x4226 = Stack.pop();
SymVal x4227 = SymStack.pop();
Num x4228 = x4226.i32_mul(x4224);
Stack.push(x4228);
bool x4229 = allConcrete(x4227, x4225);
SymVal x4230 = x4229 ? Concrete(x4228, 32) : x4227.mul(x4225);
SymStack.push(x4230);
}
{
Num x4231 = Stack.pop();
SymVal x4232 = SymStack.pop();
Num x4233 = Stack.pop();
SymVal x4234 = SymStack.pop();
Num x4235 = x4233.i32_add(x4231);
Stack.push(x4235);
bool x4236 = allConcrete(x4234, x4232);
SymVal x4237 = x4236 ? Concrete(x4235, 32) : x4234.add(x4232);
SymStack.push(x4237);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x4238 = Stack.pop();
SymVal x4239 = SymStack.pop();
Num x4240 = Stack.pop();
SymVal x4241 = SymStack.pop();
Num x4242 = x4240.i32_add(x4238);
Stack.push(x4242);
bool x4243 = allConcrete(x4241, x4239);
SymVal x4244 = x4243 ? Concrete(x4242, 32) : x4241.add(x4239);
SymStack.push(x4244);
}
{
Num x4245 = Stack.pop();
SymStack.pop();
Num x4246 = I32V(Memory.loadInt(x4245.toInt(), 8));
SymVal x4247 = SymMemory.loadSym(x4245.toInt(), 8);
Stack.push(x4246);
SymStack.push(x4247);
}
{
Num x4248 = Stack.pop();
SymStack.pop();
Num x4249 = I32V(Memory.loadInt(x4248.toInt(), 4));
SymVal x4250 = SymMemory.loadSym(x4248.toInt(), 4);
Stack.push(x4249);
SymStack.push(x4250);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4251 = Stack.pop();
SymVal x4252 = SymStack.pop();
Num x4253 = Stack.pop();
SymVal x4254 = SymStack.pop();
Num x4255 = x4253.i32_sub(x4251);
Stack.push(x4255);
bool x4256 = allConcrete(x4254, x4252);
SymVal x4257 = x4256 ? Concrete(x4255, 32) : x4254.minus(x4252);
SymStack.push(x4257);
}
{
Num x4258 = Stack.pop();
SymVal x4259 = SymStack.pop();
Num x4260 = Stack.pop();
SymStack.pop();
int x4261 = x4260.toInt();
Memory.storeInt(x4261, 4, x4258.toInt());
SymMemory.storeSym(x4261, 4, x4259);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4262 = Stack.pop();
SymVal x4263 = SymStack.pop();
Num x4264 = Stack.pop();
SymVal x4265 = SymStack.pop();
Num x4266 = x4264.i32_sub(x4262);
Stack.push(x4266);
bool x4267 = allConcrete(x4265, x4263);
SymVal x4268 = x4267 ? Concrete(x4266, 32) : x4265.minus(x4263);
SymStack.push(x4268);
}
{
Num x4269 = Stack.pop();
SymVal x4270 = SymStack.pop();
Num x4271 = Stack.pop();
SymVal x4272 = SymStack.pop();
Num x4273 = x4271.i32_mul(x4269);
Stack.push(x4273);
bool x4274 = allConcrete(x4272, x4270);
SymVal x4275 = x4274 ? Concrete(x4273, 32) : x4272.mul(x4270);
SymStack.push(x4275);
}
{
Num x4276 = Stack.pop();
SymVal x4277 = SymStack.pop();
Num x4278 = Stack.pop();
SymVal x4279 = SymStack.pop();
Num x4280 = x4278.i32_add(x4276);
Stack.push(x4280);
bool x4281 = allConcrete(x4279, x4277);
SymVal x4282 = x4281 ? Concrete(x4280, 32) : x4279.add(x4277);
SymStack.push(x4282);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4283 = Stack.pop();
SymStack.pop();
Num x4284 = I32V(Memory.loadInt(x4283.toInt(), 0));
SymVal x4285 = SymMemory.loadSym(x4283.toInt(), 0);
Stack.push(x4284);
SymStack.push(x4285);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4286 = Stack.pop();
SymVal x4287 = SymStack.pop();
Num x4288 = Stack.pop();
SymVal x4289 = SymStack.pop();
Num x4290 = x4288.i32_sub(x4286);
Stack.push(x4290);
bool x4291 = allConcrete(x4289, x4287);
SymVal x4292 = x4291 ? Concrete(x4290, 32) : x4289.minus(x4287);
SymStack.push(x4292);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4293 = Stack.pop();
SymVal x4294 = SymStack.pop();
Num x4295 = Stack.pop();
SymVal x4296 = SymStack.pop();
Num x4297 = x4295.i32_mul(x4293);
Stack.push(x4297);
bool x4298 = allConcrete(x4296, x4294);
SymVal x4299 = x4298 ? Concrete(x4297, 32) : x4296.mul(x4294);
SymStack.push(x4299);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4300 = Stack.pop();
SymVal x4301 = SymStack.pop();
Num x4302 = Stack.pop();
SymVal x4303 = SymStack.pop();
Num x4304 = x4302.i32_sub(x4300);
Stack.push(x4304);
bool x4305 = allConcrete(x4303, x4301);
SymVal x4306 = x4305 ? Concrete(x4304, 32) : x4303.minus(x4301);
SymStack.push(x4306);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4307 = Stack.pop();
SymVal x4308 = SymStack.pop();
Num x4309 = Stack.pop();
SymVal x4310 = SymStack.pop();
Num x4311 = x4309.i32_mul(x4307);
Stack.push(x4311);
bool x4312 = allConcrete(x4310, x4308);
SymVal x4313 = x4312 ? Concrete(x4311, 32) : x4310.mul(x4308);
SymStack.push(x4313);
}
{
Num x4314 = Stack.pop();
SymVal x4315 = SymStack.pop();
Num x4316 = Stack.pop();
SymVal x4317 = SymStack.pop();
Num x4318 = x4316.i32_add(x4314);
Stack.push(x4318);
bool x4319 = allConcrete(x4317, x4315);
SymVal x4320 = x4319 ? Concrete(x4318, 32) : x4317.add(x4315);
SymStack.push(x4320);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x4321 = Stack.pop();
SymVal x4322 = SymStack.pop();
Num x4323 = Stack.pop();
SymVal x4324 = SymStack.pop();
Num x4325 = x4323.i32_add(x4321);
Stack.push(x4325);
bool x4326 = allConcrete(x4324, x4322);
SymVal x4327 = x4326 ? Concrete(x4325, 32) : x4324.add(x4322);
SymStack.push(x4327);
}
{
Num x4328 = Stack.pop();
SymStack.pop();
Num x4329 = I32V(Memory.loadInt(x4328.toInt(), 8));
SymVal x4330 = SymMemory.loadSym(x4328.toInt(), 8);
Stack.push(x4329);
SymStack.push(x4330);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4331 = Stack.pop();
SymStack.pop();
Num x4332 = I32V(Memory.loadInt(x4331.toInt(), 0));
SymVal x4333 = SymMemory.loadSym(x4331.toInt(), 0);
Stack.push(x4332);
SymStack.push(x4333);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4334 = Stack.pop();
SymVal x4335 = SymStack.pop();
Num x4336 = Stack.pop();
SymVal x4337 = SymStack.pop();
Num x4338 = x4336.i32_sub(x4334);
Stack.push(x4338);
bool x4339 = allConcrete(x4337, x4335);
SymVal x4340 = x4339 ? Concrete(x4338, 32) : x4337.minus(x4335);
SymStack.push(x4340);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4341 = Stack.pop();
SymVal x4342 = SymStack.pop();
Num x4343 = Stack.pop();
SymVal x4344 = SymStack.pop();
Num x4345 = x4343.i32_mul(x4341);
Stack.push(x4345);
bool x4346 = allConcrete(x4344, x4342);
SymVal x4347 = x4346 ? Concrete(x4345, 32) : x4344.mul(x4342);
SymStack.push(x4347);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4348 = Stack.pop();
SymVal x4349 = SymStack.pop();
Num x4350 = Stack.pop();
SymVal x4351 = SymStack.pop();
Num x4352 = x4350.i32_sub(x4348);
Stack.push(x4352);
bool x4353 = allConcrete(x4351, x4349);
SymVal x4354 = x4353 ? Concrete(x4352, 32) : x4351.minus(x4349);
SymStack.push(x4354);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4355 = Stack.pop();
SymVal x4356 = SymStack.pop();
Num x4357 = Stack.pop();
SymVal x4358 = SymStack.pop();
Num x4359 = x4357.i32_mul(x4355);
Stack.push(x4359);
bool x4360 = allConcrete(x4358, x4356);
SymVal x4361 = x4360 ? Concrete(x4359, 32) : x4358.mul(x4356);
SymStack.push(x4361);
}
{
Num x4362 = Stack.pop();
SymVal x4363 = SymStack.pop();
Num x4364 = Stack.pop();
SymVal x4365 = SymStack.pop();
Num x4366 = x4364.i32_add(x4362);
Stack.push(x4366);
bool x4367 = allConcrete(x4365, x4363);
SymVal x4368 = x4367 ? Concrete(x4366, 32) : x4365.add(x4363);
SymStack.push(x4368);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x4369 = Stack.pop();
SymVal x4370 = SymStack.pop();
Num x4371 = Stack.pop();
SymVal x4372 = SymStack.pop();
Num x4373 = x4371.i32_add(x4369);
Stack.push(x4373);
bool x4374 = allConcrete(x4372, x4370);
SymVal x4375 = x4374 ? Concrete(x4373, 32) : x4372.add(x4370);
SymStack.push(x4375);
}
{
Num x4376 = Stack.pop();
SymStack.pop();
Num x4377 = I32V(Memory.loadInt(x4376.toInt(), 8));
SymVal x4378 = SymMemory.loadSym(x4376.toInt(), 8);
Stack.push(x4377);
SymStack.push(x4378);
}
{
Num x4379 = Stack.pop();
SymStack.pop();
Num x4380 = I32V(Memory.loadInt(x4379.toInt(), 4));
SymVal x4381 = SymMemory.loadSym(x4379.toInt(), 4);
Stack.push(x4380);
SymStack.push(x4381);
}
{
Num x4382 = Stack.pop();
SymVal x4383 = SymStack.pop();
Num x4384 = Stack.pop();
SymVal x4385 = SymStack.pop();
Num x4386 = x4384.i32_mul(x4382);
Stack.push(x4386);
bool x4387 = allConcrete(x4385, x4383);
SymVal x4388 = x4387 ? Concrete(x4386, 32) : x4385.mul(x4383);
SymStack.push(x4388);
}
{
Num x4389 = Stack.pop();
SymVal x4390 = SymStack.pop();
Num x4391 = Stack.pop();
SymVal x4392 = SymStack.pop();
Num x4393 = x4391.i32_add(x4389);
Stack.push(x4393);
bool x4394 = allConcrete(x4392, x4390);
SymVal x4395 = x4394 ? Concrete(x4393, 32) : x4392.add(x4390);
SymStack.push(x4395);
}
{
Num x4396 = Stack.pop();
SymStack.pop();
Num x4397 = I32V(Memory.loadInt(x4396.toInt(), 8));
SymVal x4398 = SymMemory.loadSym(x4396.toInt(), 8);
Stack.push(x4397);
SymStack.push(x4398);
}
{
Num x4399 = Stack.pop();
SymVal x4400 = SymStack.pop();
Num x4401 = Stack.pop();
SymStack.pop();
int x4402 = x4401.toInt();
Memory.storeInt(x4402, 8, x4399.toInt());
SymMemory.storeSym(x4402, 8, x4400);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x4403 = Stack.pop();
SymStack.pop();
Num x4404 = I32V(Memory.loadInt(x4403.toInt(), 0));
SymVal x4405 = SymMemory.loadSym(x4403.toInt(), 0);
Stack.push(x4404);
SymStack.push(x4405);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4406 = Stack.pop();
SymVal x4407 = SymStack.pop();
Num x4408 = Stack.pop();
SymVal x4409 = SymStack.pop();
Num x4410 = x4408.i32_ne(x4406);
Stack.push(x4410);
bool x4411 = allConcrete(x4409, x4407);
SymVal x4412 = x4411 ? Concrete(x4410, 32) : x4409.neq(x4407).bool2bv();
SymStack.push(x4412);
}
Num x4413 = Stack.pop();
{
SymVal x4414 = SymStack.pop();
ExploreTree.fillIfElseNode(x4414, 47);
}
int x4415 = x4413.toInt();
if (x4415 != 0) {
ExploreTree.moveCursor(true, makeControl(x3901, CURRENT_MCONT));
__attribute__((musttail)) return x3903(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x3903, CURRENT_MCONT));
__attribute__((musttail)) return x3901(std::monostate{});
}
return std::monostate{};
}
std::monostate x3903(std::monostate x3904) {
info("Entering the true branch 47 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3905 = Stack.pop();
SymStack.pop();
Num x3906 = I32V(Memory.loadInt(x3905.toInt(), 0));
SymVal x3907 = SymMemory.loadSym(x3905.toInt(), 0);
Stack.push(x3906);
SymStack.push(x3907);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3908 = Stack.pop();
SymVal x3909 = SymStack.pop();
Num x3910 = Stack.pop();
SymVal x3911 = SymStack.pop();
Num x3912 = x3910.i32_sub(x3908);
Stack.push(x3912);
bool x3913 = allConcrete(x3911, x3909);
SymVal x3914 = x3913 ? Concrete(x3912, 32) : x3911.minus(x3909);
SymStack.push(x3914);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3915 = Stack.pop();
SymVal x3916 = SymStack.pop();
Num x3917 = Stack.pop();
SymVal x3918 = SymStack.pop();
Num x3919 = x3917.i32_mul(x3915);
Stack.push(x3919);
bool x3920 = allConcrete(x3918, x3916);
SymVal x3921 = x3920 ? Concrete(x3919, 32) : x3918.mul(x3916);
SymStack.push(x3921);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3922 = Stack.pop();
SymVal x3923 = SymStack.pop();
Num x3924 = Stack.pop();
SymVal x3925 = SymStack.pop();
Num x3926 = x3924.i32_mul(x3922);
Stack.push(x3926);
bool x3927 = allConcrete(x3925, x3923);
SymVal x3928 = x3927 ? Concrete(x3926, 32) : x3925.mul(x3923);
SymStack.push(x3928);
}
{
Num x3929 = Stack.pop();
SymVal x3930 = SymStack.pop();
Num x3931 = Stack.pop();
SymVal x3932 = SymStack.pop();
Num x3933 = x3931.i32_add(x3929);
Stack.push(x3933);
bool x3934 = allConcrete(x3932, x3930);
SymVal x3935 = x3934 ? Concrete(x3933, 32) : x3932.add(x3930);
SymStack.push(x3935);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x3936 = Stack.pop();
SymVal x3937 = SymStack.pop();
Num x3938 = Stack.pop();
SymVal x3939 = SymStack.pop();
Num x3940 = x3938.i32_add(x3936);
Stack.push(x3940);
bool x3941 = allConcrete(x3939, x3937);
SymVal x3942 = x3941 ? Concrete(x3940, 32) : x3939.add(x3937);
SymStack.push(x3942);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3943 = Stack.pop();
SymStack.pop();
Num x3944 = I32V(Memory.loadInt(x3943.toInt(), 0));
SymVal x3945 = SymMemory.loadSym(x3943.toInt(), 0);
Stack.push(x3944);
SymStack.push(x3945);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3946 = Stack.pop();
SymVal x3947 = SymStack.pop();
Num x3948 = Stack.pop();
SymVal x3949 = SymStack.pop();
Num x3950 = x3948.i32_sub(x3946);
Stack.push(x3950);
bool x3951 = allConcrete(x3949, x3947);
SymVal x3952 = x3951 ? Concrete(x3950, 32) : x3949.minus(x3947);
SymStack.push(x3952);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3953 = Stack.pop();
SymVal x3954 = SymStack.pop();
Num x3955 = Stack.pop();
SymVal x3956 = SymStack.pop();
Num x3957 = x3955.i32_mul(x3953);
Stack.push(x3957);
bool x3958 = allConcrete(x3956, x3954);
SymVal x3959 = x3958 ? Concrete(x3957, 32) : x3956.mul(x3954);
SymStack.push(x3959);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3960 = Stack.pop();
SymStack.pop();
Num x3961 = I32V(Memory.loadInt(x3960.toInt(), 0));
SymVal x3962 = SymMemory.loadSym(x3960.toInt(), 0);
Stack.push(x3961);
SymStack.push(x3962);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3963 = Stack.pop();
SymVal x3964 = SymStack.pop();
Num x3965 = Stack.pop();
SymVal x3966 = SymStack.pop();
Num x3967 = x3965.i32_sub(x3963);
Stack.push(x3967);
bool x3968 = allConcrete(x3966, x3964);
SymVal x3969 = x3968 ? Concrete(x3967, 32) : x3966.minus(x3964);
SymStack.push(x3969);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3970 = Stack.pop();
SymVal x3971 = SymStack.pop();
Num x3972 = Stack.pop();
SymVal x3973 = SymStack.pop();
Num x3974 = x3972.i32_mul(x3970);
Stack.push(x3974);
bool x3975 = allConcrete(x3973, x3971);
SymVal x3976 = x3975 ? Concrete(x3974, 32) : x3973.mul(x3971);
SymStack.push(x3976);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3977 = Stack.pop();
SymVal x3978 = SymStack.pop();
Num x3979 = Stack.pop();
SymVal x3980 = SymStack.pop();
Num x3981 = x3979.i32_sub(x3977);
Stack.push(x3981);
bool x3982 = allConcrete(x3980, x3978);
SymVal x3983 = x3982 ? Concrete(x3981, 32) : x3980.minus(x3978);
SymStack.push(x3983);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3984 = Stack.pop();
SymVal x3985 = SymStack.pop();
Num x3986 = Stack.pop();
SymVal x3987 = SymStack.pop();
Num x3988 = x3986.i32_mul(x3984);
Stack.push(x3988);
bool x3989 = allConcrete(x3987, x3985);
SymVal x3990 = x3989 ? Concrete(x3988, 32) : x3987.mul(x3985);
SymStack.push(x3990);
}
{
Num x3991 = Stack.pop();
SymVal x3992 = SymStack.pop();
Num x3993 = Stack.pop();
SymVal x3994 = SymStack.pop();
Num x3995 = x3993.i32_add(x3991);
Stack.push(x3995);
bool x3996 = allConcrete(x3994, x3992);
SymVal x3997 = x3996 ? Concrete(x3995, 32) : x3994.add(x3992);
SymStack.push(x3997);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x3998 = Stack.pop();
SymVal x3999 = SymStack.pop();
Num x4000 = Stack.pop();
SymVal x4001 = SymStack.pop();
Num x4002 = x4000.i32_add(x3998);
Stack.push(x4002);
bool x4003 = allConcrete(x4001, x3999);
SymVal x4004 = x4003 ? Concrete(x4002, 32) : x4001.add(x3999);
SymStack.push(x4004);
}
{
Num x4005 = Stack.pop();
SymStack.pop();
Num x4006 = I32V(Memory.loadInt(x4005.toInt(), 8));
SymVal x4007 = SymMemory.loadSym(x4005.toInt(), 8);
Stack.push(x4006);
SymStack.push(x4007);
}
{
Num x4008 = Stack.pop();
SymStack.pop();
Num x4009 = I32V(Memory.loadInt(x4008.toInt(), 4));
SymVal x4010 = SymMemory.loadSym(x4008.toInt(), 4);
Stack.push(x4009);
SymStack.push(x4010);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4011 = Stack.pop();
SymVal x4012 = SymStack.pop();
Num x4013 = Stack.pop();
SymVal x4014 = SymStack.pop();
Num x4015 = x4013.i32_add(x4011);
Stack.push(x4015);
bool x4016 = allConcrete(x4014, x4012);
SymVal x4017 = x4016 ? Concrete(x4015, 32) : x4014.add(x4012);
SymStack.push(x4017);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4018 = Stack.pop();
SymVal x4019 = SymStack.pop();
Num x4020 = Stack.pop();
SymVal x4021 = SymStack.pop();
Num x4022 = x4020.i32_mul(x4018);
Stack.push(x4022);
bool x4023 = allConcrete(x4021, x4019);
SymVal x4024 = x4023 ? Concrete(x4022, 32) : x4021.mul(x4019);
SymStack.push(x4024);
}
{
Num x4025 = Stack.pop();
SymVal x4026 = SymStack.pop();
Num x4027 = Stack.pop();
SymVal x4028 = SymStack.pop();
Num x4029 = x4027.i32_add(x4025);
Stack.push(x4029);
bool x4030 = allConcrete(x4028, x4026);
SymVal x4031 = x4030 ? Concrete(x4029, 32) : x4028.add(x4026);
SymStack.push(x4031);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4032 = Stack.pop();
SymStack.pop();
Num x4033 = I32V(Memory.loadInt(x4032.toInt(), 0));
SymVal x4034 = SymMemory.loadSym(x4032.toInt(), 0);
Stack.push(x4033);
SymStack.push(x4034);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4035 = Stack.pop();
SymVal x4036 = SymStack.pop();
Num x4037 = Stack.pop();
SymVal x4038 = SymStack.pop();
Num x4039 = x4037.i32_sub(x4035);
Stack.push(x4039);
bool x4040 = allConcrete(x4038, x4036);
SymVal x4041 = x4040 ? Concrete(x4039, 32) : x4038.minus(x4036);
SymStack.push(x4041);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4042 = Stack.pop();
SymVal x4043 = SymStack.pop();
Num x4044 = Stack.pop();
SymVal x4045 = SymStack.pop();
Num x4046 = x4044.i32_mul(x4042);
Stack.push(x4046);
bool x4047 = allConcrete(x4045, x4043);
SymVal x4048 = x4047 ? Concrete(x4046, 32) : x4045.mul(x4043);
SymStack.push(x4048);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4049 = Stack.pop();
SymVal x4050 = SymStack.pop();
Num x4051 = Stack.pop();
SymVal x4052 = SymStack.pop();
Num x4053 = x4051.i32_sub(x4049);
Stack.push(x4053);
bool x4054 = allConcrete(x4052, x4050);
SymVal x4055 = x4054 ? Concrete(x4053, 32) : x4052.minus(x4050);
SymStack.push(x4055);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4056 = Stack.pop();
SymVal x4057 = SymStack.pop();
Num x4058 = Stack.pop();
SymVal x4059 = SymStack.pop();
Num x4060 = x4058.i32_mul(x4056);
Stack.push(x4060);
bool x4061 = allConcrete(x4059, x4057);
SymVal x4062 = x4061 ? Concrete(x4060, 32) : x4059.mul(x4057);
SymStack.push(x4062);
}
{
Num x4063 = Stack.pop();
SymVal x4064 = SymStack.pop();
Num x4065 = Stack.pop();
SymVal x4066 = SymStack.pop();
Num x4067 = x4065.i32_add(x4063);
Stack.push(x4067);
bool x4068 = allConcrete(x4066, x4064);
SymVal x4069 = x4068 ? Concrete(x4067, 32) : x4066.add(x4064);
SymStack.push(x4069);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x4070 = Stack.pop();
SymVal x4071 = SymStack.pop();
Num x4072 = Stack.pop();
SymVal x4073 = SymStack.pop();
Num x4074 = x4072.i32_add(x4070);
Stack.push(x4074);
bool x4075 = allConcrete(x4073, x4071);
SymVal x4076 = x4075 ? Concrete(x4074, 32) : x4073.add(x4071);
SymStack.push(x4076);
}
{
Num x4077 = Stack.pop();
SymStack.pop();
Num x4078 = I32V(Memory.loadInt(x4077.toInt(), 8));
SymVal x4079 = SymMemory.loadSym(x4077.toInt(), 8);
Stack.push(x4078);
SymStack.push(x4079);
}
{
Num x4080 = Stack.pop();
SymVal x4081 = SymStack.pop();
Num x4082 = Stack.pop();
SymVal x4083 = SymStack.pop();
Num x4084 = x4082.i32_add(x4080);
Stack.push(x4084);
bool x4085 = allConcrete(x4083, x4081);
SymVal x4086 = x4085 ? Concrete(x4084, 32) : x4083.add(x4081);
SymStack.push(x4086);
}
{
Num x4087 = Stack.pop();
SymStack.pop();
Num x4088 = I32V(Memory.loadInt(x4087.toInt(), 8));
SymVal x4089 = SymMemory.loadSym(x4087.toInt(), 8);
Stack.push(x4088);
SymStack.push(x4089);
}
{
Num x4090 = Stack.pop();
SymVal x4091 = SymStack.pop();
Num x4092 = Stack.pop();
SymStack.pop();
int x4093 = x4092.toInt();
Memory.storeInt(x4093, 8, x4090.toInt());
SymMemory.storeSym(x4093, 8, x4091);
}
__attribute__((musttail)) return x3897(std::monostate{});
return std::monostate{};
}
std::monostate x3901(std::monostate x3902) {
info("Entering the false branch 47 of the if");
__attribute__((musttail)) return x3897(std::monostate{});
return std::monostate{};
}
std::monostate x3897(std::monostate x3898) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3899 = Stack.pop();
SymVal x3900 = SymStack.pop();
Frames.set(4, x3899);
SymFrames.set(4, x3900);
}
__attribute__((musttail)) return x3839(std::monostate{});
return std::monostate{};
}
std::monostate x3843(std::monostate x3844) {
info("Entering the false branch 45 of the if");
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x3845 = Stack.pop();
SymVal x3846 = SymStack.pop();
Num x3847 = Stack.pop();
SymVal x3848 = SymStack.pop();
Num x3849 = x3847.i32_mul(x3845);
Stack.push(x3849);
bool x3850 = allConcrete(x3848, x3846);
SymVal x3851 = x3850 ? Concrete(x3849, 32) : x3848.mul(x3846);
SymStack.push(x3851);
}
{
Num x3852 = Stack.pop();
SymVal x3853 = SymStack.pop();
Num x3854 = Stack.pop();
SymVal x3855 = SymStack.pop();
Num x3856 = x3854.i32_add(x3852);
Stack.push(x3856);
bool x3857 = allConcrete(x3855, x3853);
SymVal x3858 = x3857 ? Concrete(x3856, 32) : x3855.add(x3853);
SymStack.push(x3858);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3859 = Stack.pop();
SymVal x3860 = SymStack.pop();
Num x3861 = Stack.pop();
SymVal x3862 = SymStack.pop();
Num x3863 = x3861.i32_sub(x3859);
Stack.push(x3863);
bool x3864 = allConcrete(x3862, x3860);
SymVal x3865 = x3864 ? Concrete(x3863, 32) : x3862.minus(x3860);
SymStack.push(x3865);
}
{
Num x3866 = Stack.pop();
SymVal x3867 = SymStack.pop();
Num x3868 = Stack.pop();
SymVal x3869 = SymStack.pop();
Num x3870 = x3868.i32_mul(x3866);
Stack.push(x3870);
bool x3871 = allConcrete(x3869, x3867);
SymVal x3872 = x3871 ? Concrete(x3870, 32) : x3869.mul(x3867);
SymStack.push(x3872);
}
{
Num x3873 = Stack.pop();
SymVal x3874 = SymStack.pop();
Num x3875 = Stack.pop();
SymVal x3876 = SymStack.pop();
Num x3877 = x3875.i32_add(x3873);
Stack.push(x3877);
bool x3878 = allConcrete(x3876, x3874);
SymVal x3879 = x3878 ? Concrete(x3877, 32) : x3876.add(x3874);
SymStack.push(x3879);
}
{
Num x3880 = Stack.pop();
SymStack.pop();
Num x3881 = I32V(Memory.loadInt(x3880.toInt(), 8));
SymVal x3882 = SymMemory.loadSym(x3880.toInt(), 8);
Stack.push(x3881);
SymStack.push(x3882);
}
{
Num x3883 = Stack.pop();
SymVal x3884 = SymStack.pop();
Num x3885 = Stack.pop();
SymStack.pop();
int x3886 = x3885.toInt();
Memory.storeInt(x3886, 8, x3883.toInt());
SymMemory.storeSym(x3886, 8, x3884);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3887 = Stack.pop();
SymVal x3888 = SymStack.pop();
Num x3889 = Stack.pop();
SymVal x3890 = SymStack.pop();
Num x3891 = x3889.i32_sub(x3887);
Stack.push(x3891);
bool x3892 = allConcrete(x3890, x3888);
SymVal x3893 = x3892 ? Concrete(x3891, 32) : x3890.minus(x3888);
SymStack.push(x3893);
}
{
Num x3894 = Stack.pop();
SymVal x3895 = SymStack.pop();
Frames.set(3, x3894);
SymFrames.set(3, x3895);
}
info("Jump to 1");
__attribute__((musttail)) return x3896(std::monostate{});
return std::monostate{};
}
std::monostate x3841(std::monostate x3842) {
info("Entering the false branch 44 of the if");
__attribute__((musttail)) return x3839(std::monostate{});
return std::monostate{};
}
std::monostate x3839(std::monostate x3840) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x3825(std::monostate{});
return std::monostate{};
}
std::monostate x3837(std::monostate x3838) {
info("Entering the false branch 32 of the if");
__attribute__((musttail)) return x3825(std::monostate{});
return std::monostate{};
}
std::monostate x3825(std::monostate x3826) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(Frames.get(4));
SymStack.push(SymFrames.get(4));
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x3827 = Stack.pop();
SymVal x3828 = SymStack.pop();
Num x3829 = Stack.pop();
SymVal x3830 = SymStack.pop();
Num x3831 = x3829.i32_eq(x3827);
Stack.push(x3831);
bool x3832 = allConcrete(x3830, x3828);
SymVal x3833 = x3832 ? Concrete(x3831, 32) : x3830.eq(x3828).bool2bv();
SymStack.push(x3833);
}
Num x3834 = Stack.pop();
{
SymVal x3835 = SymStack.pop();
ExploreTree.fillIfElseNode(x3835, 33);
}
int x3836 = x3834.toInt();
if (x3836 != 0) {
ExploreTree.moveCursor(true, makeControl(x2069, CURRENT_MCONT));
__attribute__((musttail)) return x3803(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x3803, CURRENT_MCONT));
__attribute__((musttail)) return x2069(std::monostate{});
}
return std::monostate{};
}
std::monostate x3803(std::monostate x3804) {
info("Entering the true branch 33 of the if");
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3805 = Stack.pop();
SymVal x3806 = SymStack.pop();
Num x3807 = Stack.pop();
SymVal x3808 = SymStack.pop();
Num x3809 = x3807.i32_add(x3805);
Stack.push(x3809);
bool x3810 = allConcrete(x3808, x3806);
SymVal x3811 = x3810 ? Concrete(x3809, 32) : x3808.add(x3806);
SymStack.push(x3811);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x3812 = Stack.pop();
SymStack.pop();
Num x3813 = I32V(Memory.loadInt(x3812.toInt(), 4));
SymVal x3814 = SymMemory.loadSym(x3812.toInt(), 4);
Stack.push(x3813);
SymStack.push(x3814);
}
{
Num x3815 = Stack.pop();
SymVal x3816 = SymStack.pop();
Num x3817 = Stack.pop();
SymVal x3818 = SymStack.pop();
Num x3819 = x3817.i32_le_s(x3815);
Stack.push(x3819);
bool x3820 = allConcrete(x3818, x3816);
SymVal x3821 = x3820 ? Concrete(x3819, 32) : x3818.le(x3816).bool2bv();
SymStack.push(x3821);
}
Num x3822 = Stack.pop();
{
SymVal x3823 = SymStack.pop();
ExploreTree.fillIfElseNode(x3823, 34);
}
int x3824 = x3822.toInt();
if (x3824 != 0) {
ExploreTree.moveCursor(true, makeControl(x3029, CURRENT_MCONT));
__attribute__((musttail)) return x3761(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x3761, CURRENT_MCONT));
__attribute__((musttail)) return x3029(std::monostate{});
}
return std::monostate{};
}
std::monostate x3761(std::monostate x3762) {
info("Entering the true branch 34 of the if");
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x3763 = Stack.pop();
SymStack.pop();
Num x3764 = I32V(Memory.loadInt(x3763.toInt(), 4));
SymVal x3765 = SymMemory.loadSym(x3763.toInt(), 4);
Stack.push(x3764);
SymStack.push(x3765);
}
{
Num x3766 = Stack.pop();
SymVal x3767 = SymStack.pop();
Num x3768 = Stack.pop();
SymVal x3769 = SymStack.pop();
Num x3770 = x3768.i32_mul(x3766);
Stack.push(x3770);
bool x3771 = allConcrete(x3769, x3767);
SymVal x3772 = x3771 ? Concrete(x3770, 32) : x3769.mul(x3767);
SymStack.push(x3772);
}
{
Num x3773 = Stack.pop();
SymVal x3774 = SymStack.pop();
Num x3775 = Stack.pop();
SymVal x3776 = SymStack.pop();
Num x3777 = x3775.i32_add(x3773);
Stack.push(x3777);
bool x3778 = allConcrete(x3776, x3774);
SymVal x3779 = x3778 ? Concrete(x3777, 32) : x3776.add(x3774);
SymStack.push(x3779);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x3780 = Stack.pop();
SymVal x3781 = SymStack.pop();
Num x3782 = Stack.pop();
SymVal x3783 = SymStack.pop();
Num x3784 = x3782.i32_mul(x3780);
Stack.push(x3784);
bool x3785 = allConcrete(x3783, x3781);
SymVal x3786 = x3785 ? Concrete(x3784, 32) : x3783.mul(x3781);
SymStack.push(x3786);
}
{
Num x3787 = Stack.pop();
SymVal x3788 = SymStack.pop();
Num x3789 = Stack.pop();
SymVal x3790 = SymStack.pop();
Num x3791 = x3789.i32_add(x3787);
Stack.push(x3791);
bool x3792 = allConcrete(x3790, x3788);
SymVal x3793 = x3792 ? Concrete(x3791, 32) : x3790.add(x3788);
SymStack.push(x3793);
}
{
Num x3794 = Stack.pop();
SymStack.pop();
Num x3795 = I32V(Memory.loadInt(x3794.toInt(), 8));
SymVal x3796 = SymMemory.loadSym(x3794.toInt(), 8);
Stack.push(x3795);
SymStack.push(x3796);
}
{
Num x3797 = Stack.pop();
SymVal x3798 = SymStack.pop();
Num x3799 = Stack.pop();
SymStack.pop();
int x3800 = x3799.toInt();
Memory.storeInt(x3800, 8, x3797.toInt());
SymMemory.storeSym(x3800, 8, x3798);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3801 = Stack.pop();
SymVal x3802 = SymStack.pop();
Frames.set(3, x3801);
SymFrames.set(3, x3802);
}
__attribute__((musttail)) return x3142(std::monostate{});
return std::monostate{};
}
std::monostate x3142(std::monostate x3699) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3700 = Stack.pop();
SymStack.pop();
Num x3701 = I32V(Memory.loadInt(x3700.toInt(), 0));
SymVal x3702 = SymMemory.loadSym(x3700.toInt(), 0);
Stack.push(x3701);
SymStack.push(x3702);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3703 = Stack.pop();
SymVal x3704 = SymStack.pop();
Num x3705 = Stack.pop();
SymVal x3706 = SymStack.pop();
Num x3707 = x3705.i32_sub(x3703);
Stack.push(x3707);
bool x3708 = allConcrete(x3706, x3704);
SymVal x3709 = x3708 ? Concrete(x3707, 32) : x3706.minus(x3704);
SymStack.push(x3709);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3710 = Stack.pop();
SymVal x3711 = SymStack.pop();
Num x3712 = Stack.pop();
SymVal x3713 = SymStack.pop();
Num x3714 = x3712.i32_mul(x3710);
Stack.push(x3714);
bool x3715 = allConcrete(x3713, x3711);
SymVal x3716 = x3715 ? Concrete(x3714, 32) : x3713.mul(x3711);
SymStack.push(x3716);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3717 = Stack.pop();
SymVal x3718 = SymStack.pop();
Num x3719 = Stack.pop();
SymVal x3720 = SymStack.pop();
Num x3721 = x3719.i32_add(x3717);
Stack.push(x3721);
bool x3722 = allConcrete(x3720, x3718);
SymVal x3723 = x3722 ? Concrete(x3721, 32) : x3720.add(x3718);
SymStack.push(x3723);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3724 = Stack.pop();
SymVal x3725 = SymStack.pop();
Num x3726 = Stack.pop();
SymVal x3727 = SymStack.pop();
Num x3728 = x3726.i32_mul(x3724);
Stack.push(x3728);
bool x3729 = allConcrete(x3727, x3725);
SymVal x3730 = x3729 ? Concrete(x3728, 32) : x3727.mul(x3725);
SymStack.push(x3730);
}
{
Num x3731 = Stack.pop();
SymVal x3732 = SymStack.pop();
Num x3733 = Stack.pop();
SymVal x3734 = SymStack.pop();
Num x3735 = x3733.i32_add(x3731);
Stack.push(x3735);
bool x3736 = allConcrete(x3734, x3732);
SymVal x3737 = x3736 ? Concrete(x3735, 32) : x3734.add(x3732);
SymStack.push(x3737);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x3738 = Stack.pop();
SymVal x3739 = SymStack.pop();
Num x3740 = Stack.pop();
SymVal x3741 = SymStack.pop();
Num x3742 = x3740.i32_add(x3738);
Stack.push(x3742);
bool x3743 = allConcrete(x3741, x3739);
SymVal x3744 = x3743 ? Concrete(x3742, 32) : x3741.add(x3739);
SymStack.push(x3744);
}
{
Num x3745 = Stack.pop();
SymStack.pop();
Num x3746 = I32V(Memory.loadInt(x3745.toInt(), 8));
SymVal x3747 = SymMemory.loadSym(x3745.toInt(), 8);
Stack.push(x3746);
SymStack.push(x3747);
}
{
Num x3748 = Stack.pop();
SymStack.pop();
Num x3749 = I32V(Memory.loadInt(x3748.toInt(), 4));
SymVal x3750 = SymMemory.loadSym(x3748.toInt(), 4);
Stack.push(x3749);
SymStack.push(x3750);
}
{
Num x3751 = Stack.pop();
SymVal x3752 = SymStack.pop();
Num x3753 = Stack.pop();
SymVal x3754 = SymStack.pop();
Num x3755 = x3753.i32_eq(x3751);
Stack.push(x3755);
bool x3756 = allConcrete(x3754, x3752);
SymVal x3757 = x3756 ? Concrete(x3755, 32) : x3754.eq(x3752).bool2bv();
SymStack.push(x3757);
}
Num x3758 = Stack.pop();
{
SymVal x3759 = SymStack.pop();
ExploreTree.fillIfElseNode(x3759, 41);
}
int x3760 = x3758.toInt();
if (x3760 != 0) {
ExploreTree.moveCursor(true, makeControl(x3031, CURRENT_MCONT));
__attribute__((musttail)) return x3697(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x3697, CURRENT_MCONT));
__attribute__((musttail)) return x3031(std::monostate{});
}
return std::monostate{};
}
std::monostate x3697(std::monostate x3698) {
info("Entering the true branch 41 of the if");
info("Jump to 2");
__attribute__((musttail)) return x3682(std::monostate{});
return std::monostate{};
}
std::monostate x3682(std::monostate x3683) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x3684 = Stack.pop();
SymStack.pop();
Num x3685 = I32V(Memory.loadInt(x3684.toInt(), 0));
SymVal x3686 = SymMemory.loadSym(x3684.toInt(), 0);
Stack.push(x3685);
SymStack.push(x3686);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3687 = Stack.pop();
SymVal x3688 = SymStack.pop();
Num x3689 = Stack.pop();
SymVal x3690 = SymStack.pop();
Num x3691 = x3689.i32_ne(x3687);
Stack.push(x3691);
bool x3692 = allConcrete(x3690, x3688);
SymVal x3693 = x3692 ? Concrete(x3691, 32) : x3690.neq(x3688).bool2bv();
SymStack.push(x3693);
}
Num x3694 = Stack.pop();
{
SymVal x3695 = SymStack.pop();
ExploreTree.fillIfElseNode(x3695, 42);
}
int x3696 = x3694.toInt();
if (x3696 != 0) {
ExploreTree.moveCursor(true, makeControl(x3443, CURRENT_MCONT));
__attribute__((musttail)) return x3678(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x3678, CURRENT_MCONT));
__attribute__((musttail)) return x3443(std::monostate{});
}
return std::monostate{};
}
std::monostate x3678(std::monostate x3679) {
info("Entering the true branch 42 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3680 = Stack.pop();
SymVal x3681 = SymStack.pop();
Frames.set(3, x3680);
SymFrames.set(3, x3681);
}
__attribute__((musttail)) return x3604(std::monostate{});
return std::monostate{};
}
std::monostate x3604(std::monostate x3609) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3610 = Stack.pop();
SymStack.pop();
Num x3611 = I32V(Memory.loadInt(x3610.toInt(), 0));
SymVal x3612 = SymMemory.loadSym(x3610.toInt(), 0);
Stack.push(x3611);
SymStack.push(x3612);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3613 = Stack.pop();
SymVal x3614 = SymStack.pop();
Num x3615 = Stack.pop();
SymVal x3616 = SymStack.pop();
Num x3617 = x3615.i32_sub(x3613);
Stack.push(x3617);
bool x3618 = allConcrete(x3616, x3614);
SymVal x3619 = x3618 ? Concrete(x3617, 32) : x3616.minus(x3614);
SymStack.push(x3619);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3620 = Stack.pop();
SymVal x3621 = SymStack.pop();
Num x3622 = Stack.pop();
SymVal x3623 = SymStack.pop();
Num x3624 = x3622.i32_mul(x3620);
Stack.push(x3624);
bool x3625 = allConcrete(x3623, x3621);
SymVal x3626 = x3625 ? Concrete(x3624, 32) : x3623.mul(x3621);
SymStack.push(x3626);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3627 = Stack.pop();
SymVal x3628 = SymStack.pop();
Num x3629 = Stack.pop();
SymVal x3630 = SymStack.pop();
Num x3631 = x3629.i32_add(x3627);
Stack.push(x3631);
bool x3632 = allConcrete(x3630, x3628);
SymVal x3633 = x3632 ? Concrete(x3631, 32) : x3630.add(x3628);
SymStack.push(x3633);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3634 = Stack.pop();
SymVal x3635 = SymStack.pop();
Num x3636 = Stack.pop();
SymVal x3637 = SymStack.pop();
Num x3638 = x3636.i32_mul(x3634);
Stack.push(x3638);
bool x3639 = allConcrete(x3637, x3635);
SymVal x3640 = x3639 ? Concrete(x3638, 32) : x3637.mul(x3635);
SymStack.push(x3640);
}
{
Num x3641 = Stack.pop();
SymVal x3642 = SymStack.pop();
Num x3643 = Stack.pop();
SymVal x3644 = SymStack.pop();
Num x3645 = x3643.i32_add(x3641);
Stack.push(x3645);
bool x3646 = allConcrete(x3644, x3642);
SymVal x3647 = x3646 ? Concrete(x3645, 32) : x3644.add(x3642);
SymStack.push(x3647);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x3648 = Stack.pop();
SymVal x3649 = SymStack.pop();
Num x3650 = Stack.pop();
SymVal x3651 = SymStack.pop();
Num x3652 = x3650.i32_add(x3648);
Stack.push(x3652);
bool x3653 = allConcrete(x3651, x3649);
SymVal x3654 = x3653 ? Concrete(x3652, 32) : x3651.add(x3649);
SymStack.push(x3654);
}
{
Num x3655 = Stack.pop();
SymStack.pop();
Num x3656 = I32V(Memory.loadInt(x3655.toInt(), 8));
SymVal x3657 = SymMemory.loadSym(x3655.toInt(), 8);
Stack.push(x3656);
SymStack.push(x3657);
}
{
Num x3658 = Stack.pop();
SymStack.pop();
Num x3659 = I32V(Memory.loadInt(x3658.toInt(), 4));
SymVal x3660 = SymMemory.loadSym(x3658.toInt(), 4);
Stack.push(x3659);
SymStack.push(x3660);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3661 = Stack.pop();
SymVal x3662 = SymStack.pop();
Num x3663 = Stack.pop();
SymVal x3664 = SymStack.pop();
Num x3665 = x3663.i32_add(x3661);
Stack.push(x3665);
bool x3666 = allConcrete(x3664, x3662);
SymVal x3667 = x3666 ? Concrete(x3665, 32) : x3664.add(x3662);
SymStack.push(x3667);
}
{
Num x3668 = Stack.pop();
SymVal x3669 = SymStack.pop();
Num x3670 = Stack.pop();
SymVal x3671 = SymStack.pop();
Num x3672 = x3670.i32_eq(x3668);
Stack.push(x3672);
bool x3673 = allConcrete(x3671, x3669);
SymVal x3674 = x3673 ? Concrete(x3672, 32) : x3671.eq(x3669).bool2bv();
SymStack.push(x3674);
}
Num x3675 = Stack.pop();
{
SymVal x3676 = SymStack.pop();
ExploreTree.fillIfElseNode(x3676, 43);
}
int x3677 = x3675.toInt();
if (x3677 != 0) {
ExploreTree.moveCursor(true, makeControl(x3445, CURRENT_MCONT));
__attribute__((musttail)) return x3607(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x3607, CURRENT_MCONT));
__attribute__((musttail)) return x3445(std::monostate{});
}
return std::monostate{};
}
std::monostate x3607(std::monostate x3608) {
info("Entering the true branch 43 of the if");
info("Jump to 2");
__attribute__((musttail)) return x3605(std::monostate{});
return std::monostate{};
}
std::monostate x3605(std::monostate x3606) {
info("Exiting the block, stackSize =", Stack.size());
__attribute__((musttail)) return x3360(std::monostate{});
return std::monostate{};
}
std::monostate x3445(std::monostate x3446) {
info("Entering the false branch 43 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3447 = Stack.pop();
SymStack.pop();
Num x3448 = I32V(Memory.loadInt(x3447.toInt(), 0));
SymVal x3449 = SymMemory.loadSym(x3447.toInt(), 0);
Stack.push(x3448);
SymStack.push(x3449);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3450 = Stack.pop();
SymVal x3451 = SymStack.pop();
Num x3452 = Stack.pop();
SymVal x3453 = SymStack.pop();
Num x3454 = x3452.i32_sub(x3450);
Stack.push(x3454);
bool x3455 = allConcrete(x3453, x3451);
SymVal x3456 = x3455 ? Concrete(x3454, 32) : x3453.minus(x3451);
SymStack.push(x3456);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3457 = Stack.pop();
SymVal x3458 = SymStack.pop();
Num x3459 = Stack.pop();
SymVal x3460 = SymStack.pop();
Num x3461 = x3459.i32_mul(x3457);
Stack.push(x3461);
bool x3462 = allConcrete(x3460, x3458);
SymVal x3463 = x3462 ? Concrete(x3461, 32) : x3460.mul(x3458);
SymStack.push(x3463);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x3464 = Stack.pop();
SymStack.pop();
Num x3465 = I32V(Memory.loadInt(x3464.toInt(), 4));
SymVal x3466 = SymMemory.loadSym(x3464.toInt(), 4);
Stack.push(x3465);
SymStack.push(x3466);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x3467 = Stack.pop();
SymVal x3468 = SymStack.pop();
Num x3469 = Stack.pop();
SymVal x3470 = SymStack.pop();
Num x3471 = x3469.i32_add(x3467);
Stack.push(x3471);
bool x3472 = allConcrete(x3470, x3468);
SymVal x3473 = x3472 ? Concrete(x3471, 32) : x3470.add(x3468);
SymStack.push(x3473);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3474 = Stack.pop();
SymVal x3475 = SymStack.pop();
Num x3476 = Stack.pop();
SymVal x3477 = SymStack.pop();
Num x3478 = x3476.i32_add(x3474);
Stack.push(x3478);
bool x3479 = allConcrete(x3477, x3475);
SymVal x3480 = x3479 ? Concrete(x3478, 32) : x3477.add(x3475);
SymStack.push(x3480);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3481 = Stack.pop();
SymVal x3482 = SymStack.pop();
Num x3483 = Stack.pop();
SymVal x3484 = SymStack.pop();
Num x3485 = x3483.i32_mul(x3481);
Stack.push(x3485);
bool x3486 = allConcrete(x3484, x3482);
SymVal x3487 = x3486 ? Concrete(x3485, 32) : x3484.mul(x3482);
SymStack.push(x3487);
}
{
Num x3488 = Stack.pop();
SymVal x3489 = SymStack.pop();
Num x3490 = Stack.pop();
SymVal x3491 = SymStack.pop();
Num x3492 = x3490.i32_add(x3488);
Stack.push(x3492);
bool x3493 = allConcrete(x3491, x3489);
SymVal x3494 = x3493 ? Concrete(x3492, 32) : x3491.add(x3489);
SymStack.push(x3494);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x3495 = Stack.pop();
SymVal x3496 = SymStack.pop();
Num x3497 = Stack.pop();
SymVal x3498 = SymStack.pop();
Num x3499 = x3497.i32_add(x3495);
Stack.push(x3499);
bool x3500 = allConcrete(x3498, x3496);
SymVal x3501 = x3500 ? Concrete(x3499, 32) : x3498.add(x3496);
SymStack.push(x3501);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3502 = Stack.pop();
SymStack.pop();
Num x3503 = I32V(Memory.loadInt(x3502.toInt(), 0));
SymVal x3504 = SymMemory.loadSym(x3502.toInt(), 0);
Stack.push(x3503);
SymStack.push(x3504);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3505 = Stack.pop();
SymVal x3506 = SymStack.pop();
Num x3507 = Stack.pop();
SymVal x3508 = SymStack.pop();
Num x3509 = x3507.i32_sub(x3505);
Stack.push(x3509);
bool x3510 = allConcrete(x3508, x3506);
SymVal x3511 = x3510 ? Concrete(x3509, 32) : x3508.minus(x3506);
SymStack.push(x3511);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3512 = Stack.pop();
SymVal x3513 = SymStack.pop();
Num x3514 = Stack.pop();
SymVal x3515 = SymStack.pop();
Num x3516 = x3514.i32_mul(x3512);
Stack.push(x3516);
bool x3517 = allConcrete(x3515, x3513);
SymVal x3518 = x3517 ? Concrete(x3516, 32) : x3515.mul(x3513);
SymStack.push(x3518);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3519 = Stack.pop();
SymVal x3520 = SymStack.pop();
Num x3521 = Stack.pop();
SymVal x3522 = SymStack.pop();
Num x3523 = x3521.i32_mul(x3519);
Stack.push(x3523);
bool x3524 = allConcrete(x3522, x3520);
SymVal x3525 = x3524 ? Concrete(x3523, 32) : x3522.mul(x3520);
SymStack.push(x3525);
}
{
Num x3526 = Stack.pop();
SymVal x3527 = SymStack.pop();
Num x3528 = Stack.pop();
SymVal x3529 = SymStack.pop();
Num x3530 = x3528.i32_add(x3526);
Stack.push(x3530);
bool x3531 = allConcrete(x3529, x3527);
SymVal x3532 = x3531 ? Concrete(x3530, 32) : x3529.add(x3527);
SymStack.push(x3532);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3533 = Stack.pop();
SymStack.pop();
Num x3534 = I32V(Memory.loadInt(x3533.toInt(), 0));
SymVal x3535 = SymMemory.loadSym(x3533.toInt(), 0);
Stack.push(x3534);
SymStack.push(x3535);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3536 = Stack.pop();
SymVal x3537 = SymStack.pop();
Num x3538 = Stack.pop();
SymVal x3539 = SymStack.pop();
Num x3540 = x3538.i32_sub(x3536);
Stack.push(x3540);
bool x3541 = allConcrete(x3539, x3537);
SymVal x3542 = x3541 ? Concrete(x3540, 32) : x3539.minus(x3537);
SymStack.push(x3542);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3543 = Stack.pop();
SymVal x3544 = SymStack.pop();
Num x3545 = Stack.pop();
SymVal x3546 = SymStack.pop();
Num x3547 = x3545.i32_mul(x3543);
Stack.push(x3547);
bool x3548 = allConcrete(x3546, x3544);
SymVal x3549 = x3548 ? Concrete(x3547, 32) : x3546.mul(x3544);
SymStack.push(x3549);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3550 = Stack.pop();
SymVal x3551 = SymStack.pop();
Num x3552 = Stack.pop();
SymVal x3553 = SymStack.pop();
Num x3554 = x3552.i32_add(x3550);
Stack.push(x3554);
bool x3555 = allConcrete(x3553, x3551);
SymVal x3556 = x3555 ? Concrete(x3554, 32) : x3553.add(x3551);
SymStack.push(x3556);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3557 = Stack.pop();
SymVal x3558 = SymStack.pop();
Num x3559 = Stack.pop();
SymVal x3560 = SymStack.pop();
Num x3561 = x3559.i32_mul(x3557);
Stack.push(x3561);
bool x3562 = allConcrete(x3560, x3558);
SymVal x3563 = x3562 ? Concrete(x3561, 32) : x3560.mul(x3558);
SymStack.push(x3563);
}
{
Num x3564 = Stack.pop();
SymVal x3565 = SymStack.pop();
Num x3566 = Stack.pop();
SymVal x3567 = SymStack.pop();
Num x3568 = x3566.i32_add(x3564);
Stack.push(x3568);
bool x3569 = allConcrete(x3567, x3565);
SymVal x3570 = x3569 ? Concrete(x3568, 32) : x3567.add(x3565);
SymStack.push(x3570);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x3571 = Stack.pop();
SymVal x3572 = SymStack.pop();
Num x3573 = Stack.pop();
SymVal x3574 = SymStack.pop();
Num x3575 = x3573.i32_add(x3571);
Stack.push(x3575);
bool x3576 = allConcrete(x3574, x3572);
SymVal x3577 = x3576 ? Concrete(x3575, 32) : x3574.add(x3572);
SymStack.push(x3577);
}
{
Num x3578 = Stack.pop();
SymStack.pop();
Num x3579 = I32V(Memory.loadInt(x3578.toInt(), 8));
SymVal x3580 = SymMemory.loadSym(x3578.toInt(), 8);
Stack.push(x3579);
SymStack.push(x3580);
}
{
Num x3581 = Stack.pop();
SymVal x3582 = SymStack.pop();
Num x3583 = Stack.pop();
SymVal x3584 = SymStack.pop();
Num x3585 = x3583.i32_add(x3581);
Stack.push(x3585);
bool x3586 = allConcrete(x3584, x3582);
SymVal x3587 = x3586 ? Concrete(x3585, 32) : x3584.add(x3582);
SymStack.push(x3587);
}
{
Num x3588 = Stack.pop();
SymStack.pop();
Num x3589 = I32V(Memory.loadInt(x3588.toInt(), 8));
SymVal x3590 = SymMemory.loadSym(x3588.toInt(), 8);
Stack.push(x3589);
SymStack.push(x3590);
}
{
Num x3591 = Stack.pop();
SymVal x3592 = SymStack.pop();
Num x3593 = Stack.pop();
SymStack.pop();
int x3594 = x3593.toInt();
Memory.storeInt(x3594, 8, x3591.toInt());
SymMemory.storeSym(x3594, 8, x3592);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3595 = Stack.pop();
SymVal x3596 = SymStack.pop();
Num x3597 = Stack.pop();
SymVal x3598 = SymStack.pop();
Num x3599 = x3597.i32_add(x3595);
Stack.push(x3599);
bool x3600 = allConcrete(x3598, x3596);
SymVal x3601 = x3600 ? Concrete(x3599, 32) : x3598.add(x3596);
SymStack.push(x3601);
}
{
Num x3602 = Stack.pop();
SymVal x3603 = SymStack.pop();
Frames.set(3, x3602);
SymFrames.set(3, x3603);
}
info("Jump to 1");
__attribute__((musttail)) return x3604(std::monostate{});
return std::monostate{};
}
std::monostate x3443(std::monostate x3444) {
info("Entering the false branch 42 of the if");
__attribute__((musttail)) return x3360(std::monostate{});
return std::monostate{};
}
std::monostate x3360(std::monostate x3361) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x3362 = Stack.pop();
SymStack.pop();
Num x3363 = I32V(Memory.loadInt(x3362.toInt(), 4));
SymVal x3364 = SymMemory.loadSym(x3362.toInt(), 4);
Stack.push(x3363);
SymStack.push(x3364);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3365 = Stack.pop();
SymStack.pop();
Num x3366 = I32V(Memory.loadInt(x3365.toInt(), 0));
SymVal x3367 = SymMemory.loadSym(x3365.toInt(), 0);
Stack.push(x3366);
SymStack.push(x3367);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3368 = Stack.pop();
SymVal x3369 = SymStack.pop();
Num x3370 = Stack.pop();
SymVal x3371 = SymStack.pop();
Num x3372 = x3370.i32_sub(x3368);
Stack.push(x3372);
bool x3373 = allConcrete(x3371, x3369);
SymVal x3374 = x3373 ? Concrete(x3372, 32) : x3371.minus(x3369);
SymStack.push(x3374);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3375 = Stack.pop();
SymVal x3376 = SymStack.pop();
Num x3377 = Stack.pop();
SymVal x3378 = SymStack.pop();
Num x3379 = x3377.i32_mul(x3375);
Stack.push(x3379);
bool x3380 = allConcrete(x3378, x3376);
SymVal x3381 = x3380 ? Concrete(x3379, 32) : x3378.mul(x3376);
SymStack.push(x3381);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3382 = Stack.pop();
SymVal x3383 = SymStack.pop();
Num x3384 = Stack.pop();
SymVal x3385 = SymStack.pop();
Num x3386 = x3384.i32_add(x3382);
Stack.push(x3386);
bool x3387 = allConcrete(x3385, x3383);
SymVal x3388 = x3387 ? Concrete(x3386, 32) : x3385.add(x3383);
SymStack.push(x3388);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3389 = Stack.pop();
SymVal x3390 = SymStack.pop();
Num x3391 = Stack.pop();
SymVal x3392 = SymStack.pop();
Num x3393 = x3391.i32_mul(x3389);
Stack.push(x3393);
bool x3394 = allConcrete(x3392, x3390);
SymVal x3395 = x3394 ? Concrete(x3393, 32) : x3392.mul(x3390);
SymStack.push(x3395);
}
{
Num x3396 = Stack.pop();
SymVal x3397 = SymStack.pop();
Num x3398 = Stack.pop();
SymVal x3399 = SymStack.pop();
Num x3400 = x3398.i32_add(x3396);
Stack.push(x3400);
bool x3401 = allConcrete(x3399, x3397);
SymVal x3402 = x3401 ? Concrete(x3400, 32) : x3399.add(x3397);
SymStack.push(x3402);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x3403 = Stack.pop();
SymVal x3404 = SymStack.pop();
Num x3405 = Stack.pop();
SymVal x3406 = SymStack.pop();
Num x3407 = x3405.i32_add(x3403);
Stack.push(x3407);
bool x3408 = allConcrete(x3406, x3404);
SymVal x3409 = x3408 ? Concrete(x3407, 32) : x3406.add(x3404);
SymStack.push(x3409);
}
{
Num x3410 = Stack.pop();
SymStack.pop();
Num x3411 = I32V(Memory.loadInt(x3410.toInt(), 8));
SymVal x3412 = SymMemory.loadSym(x3410.toInt(), 8);
Stack.push(x3411);
SymStack.push(x3412);
}
{
Num x3413 = Stack.pop();
SymStack.pop();
Num x3414 = I32V(Memory.loadInt(x3413.toInt(), 4));
SymVal x3415 = SymMemory.loadSym(x3413.toInt(), 4);
Stack.push(x3414);
SymStack.push(x3415);
}
{
Num x3416 = Stack.pop();
SymVal x3417 = SymStack.pop();
Num x3418 = Stack.pop();
SymVal x3419 = SymStack.pop();
Num x3420 = x3418.i32_add(x3416);
Stack.push(x3420);
bool x3421 = allConcrete(x3419, x3417);
SymVal x3422 = x3421 ? Concrete(x3420, 32) : x3419.add(x3417);
SymStack.push(x3422);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3423 = Stack.pop();
SymVal x3424 = SymStack.pop();
Num x3425 = Stack.pop();
SymVal x3426 = SymStack.pop();
Num x3427 = x3425.i32_add(x3423);
Stack.push(x3427);
bool x3428 = allConcrete(x3426, x3424);
SymVal x3429 = x3428 ? Concrete(x3427, 32) : x3426.add(x3424);
SymStack.push(x3429);
}
{
Num x3430 = Stack.pop();
SymVal x3431 = SymStack.pop();
Num x3432 = Stack.pop();
SymStack.pop();
int x3433 = x3432.toInt();
Memory.storeInt(x3433, 4, x3430.toInt());
SymMemory.storeSym(x3433, 4, x3431);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3434 = Stack.pop();
SymVal x3435 = SymStack.pop();
Num x3436 = Stack.pop();
SymVal x3437 = SymStack.pop();
Num x3438 = x3436.i32_add(x3434);
Stack.push(x3438);
bool x3439 = allConcrete(x3437, x3435);
SymVal x3440 = x3439 ? Concrete(x3438, 32) : x3437.add(x3435);
SymStack.push(x3440);
}
{
Num x3441 = Stack.pop();
SymVal x3442 = SymStack.pop();
Frames.set(3, x3441);
SymFrames.set(3, x3442);
}
__attribute__((musttail)) return x3244(std::monostate{});
return std::monostate{};
}
std::monostate x3244(std::monostate x3346) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x3347 = Stack.pop();
SymStack.pop();
Num x3348 = I32V(Memory.loadInt(x3347.toInt(), 4));
SymVal x3349 = SymMemory.loadSym(x3347.toInt(), 4);
Stack.push(x3348);
SymStack.push(x3349);
}
{
Num x3350 = Stack.pop();
SymVal x3351 = SymStack.pop();
Num x3352 = Stack.pop();
SymVal x3353 = SymStack.pop();
Num x3354 = x3352.i32_eq(x3350);
Stack.push(x3354);
bool x3355 = allConcrete(x3353, x3351);
SymVal x3356 = x3355 ? Concrete(x3354, 32) : x3353.eq(x3351).bool2bv();
SymStack.push(x3356);
}
Num x3357 = Stack.pop();
{
SymVal x3358 = SymStack.pop();
ExploreTree.fillIfElseNode(x3358, 38);
}
int x3359 = x3357.toInt();
if (x3359 != 0) {
ExploreTree.moveCursor(true, makeControl(x3143, CURRENT_MCONT));
__attribute__((musttail)) return x3344(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x3344, CURRENT_MCONT));
__attribute__((musttail)) return x3143(std::monostate{});
}
return std::monostate{};
}
std::monostate x3344(std::monostate x3345) {
info("Entering the true branch 38 of the if");
info("Jump to 2");
__attribute__((musttail)) return x3340(std::monostate{});
return std::monostate{};
}
std::monostate x3340(std::monostate x3341) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x3342 = Stack.pop();
SymVal x3343 = SymStack.pop();
Frames.set(3, x3342);
SymFrames.set(3, x3343);
}
__attribute__((musttail)) return x3298(std::monostate{});
return std::monostate{};
}
std::monostate x3298(std::monostate x3319) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x3320 = Stack.pop();
SymStack.pop();
Num x3321 = I32V(Memory.loadInt(x3320.toInt(), 4));
SymVal x3322 = SymMemory.loadSym(x3320.toInt(), 4);
Stack.push(x3321);
SymStack.push(x3322);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3323 = Stack.pop();
SymVal x3324 = SymStack.pop();
Num x3325 = Stack.pop();
SymVal x3326 = SymStack.pop();
Num x3327 = x3325.i32_sub(x3323);
Stack.push(x3327);
bool x3328 = allConcrete(x3326, x3324);
SymVal x3329 = x3328 ? Concrete(x3327, 32) : x3326.minus(x3324);
SymStack.push(x3329);
}
{
Num x3330 = Stack.pop();
SymVal x3331 = SymStack.pop();
Num x3332 = Stack.pop();
SymVal x3333 = SymStack.pop();
Num x3334 = x3332.i32_eq(x3330);
Stack.push(x3334);
bool x3335 = allConcrete(x3333, x3331);
SymVal x3336 = x3335 ? Concrete(x3334, 32) : x3333.eq(x3331).bool2bv();
SymStack.push(x3336);
}
Num x3337 = Stack.pop();
{
SymVal x3338 = SymStack.pop();
ExploreTree.fillIfElseNode(x3338, 39);
}
int x3339 = x3337.toInt();
if (x3339 != 0) {
ExploreTree.moveCursor(true, makeControl(x3245, CURRENT_MCONT));
__attribute__((musttail)) return x3317(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x3317, CURRENT_MCONT));
__attribute__((musttail)) return x3245(std::monostate{});
}
return std::monostate{};
}
std::monostate x3317(std::monostate x3318) {
info("Entering the true branch 39 of the if");
info("Jump to 2");
__attribute__((musttail)) return x3299(std::monostate{});
return std::monostate{};
}
std::monostate x3299(std::monostate x3300) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x3301 = Stack.pop();
SymStack.pop();
Num x3302 = I32V(Memory.loadInt(x3301.toInt(), 4));
SymVal x3303 = SymMemory.loadSym(x3301.toInt(), 4);
Stack.push(x3302);
SymStack.push(x3303);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3304 = Stack.pop();
SymVal x3305 = SymStack.pop();
Num x3306 = Stack.pop();
SymVal x3307 = SymStack.pop();
Num x3308 = x3306.i32_sub(x3304);
Stack.push(x3308);
bool x3309 = allConcrete(x3307, x3305);
SymVal x3310 = x3309 ? Concrete(x3308, 32) : x3307.minus(x3305);
SymStack.push(x3310);
}
{
Num x3311 = Stack.pop();
SymVal x3312 = SymStack.pop();
Num x3313 = Stack.pop();
SymStack.pop();
int x3314 = x3313.toInt();
Memory.storeInt(x3314, 4, x3311.toInt());
SymMemory.storeSym(x3314, 4, x3312);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3315 = Stack.pop();
SymVal x3316 = SymStack.pop();
Frames.set(4, x3315);
SymFrames.set(4, x3316);
}
__attribute__((musttail)) return x2996(std::monostate{});
return std::monostate{};
}
std::monostate x3245(std::monostate x3246) {
info("Entering the false branch 39 of the if");
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x3247 = Stack.pop();
SymVal x3248 = SymStack.pop();
Num x3249 = Stack.pop();
SymVal x3250 = SymStack.pop();
Num x3251 = x3249.i32_mul(x3247);
Stack.push(x3251);
bool x3252 = allConcrete(x3250, x3248);
SymVal x3253 = x3252 ? Concrete(x3251, 32) : x3250.mul(x3248);
SymStack.push(x3253);
}
{
Num x3254 = Stack.pop();
SymVal x3255 = SymStack.pop();
Num x3256 = Stack.pop();
SymVal x3257 = SymStack.pop();
Num x3258 = x3256.i32_add(x3254);
Stack.push(x3258);
bool x3259 = allConcrete(x3257, x3255);
SymVal x3260 = x3259 ? Concrete(x3258, 32) : x3257.add(x3255);
SymStack.push(x3260);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3261 = Stack.pop();
SymVal x3262 = SymStack.pop();
Num x3263 = Stack.pop();
SymVal x3264 = SymStack.pop();
Num x3265 = x3263.i32_add(x3261);
Stack.push(x3265);
bool x3266 = allConcrete(x3264, x3262);
SymVal x3267 = x3266 ? Concrete(x3265, 32) : x3264.add(x3262);
SymStack.push(x3267);
}
{
Num x3268 = Stack.pop();
SymVal x3269 = SymStack.pop();
Num x3270 = Stack.pop();
SymVal x3271 = SymStack.pop();
Num x3272 = x3270.i32_mul(x3268);
Stack.push(x3272);
bool x3273 = allConcrete(x3271, x3269);
SymVal x3274 = x3273 ? Concrete(x3272, 32) : x3271.mul(x3269);
SymStack.push(x3274);
}
{
Num x3275 = Stack.pop();
SymVal x3276 = SymStack.pop();
Num x3277 = Stack.pop();
SymVal x3278 = SymStack.pop();
Num x3279 = x3277.i32_add(x3275);
Stack.push(x3279);
bool x3280 = allConcrete(x3278, x3276);
SymVal x3281 = x3280 ? Concrete(x3279, 32) : x3278.add(x3276);
SymStack.push(x3281);
}
{
Num x3282 = Stack.pop();
SymStack.pop();
Num x3283 = I32V(Memory.loadInt(x3282.toInt(), 8));
SymVal x3284 = SymMemory.loadSym(x3282.toInt(), 8);
Stack.push(x3283);
SymStack.push(x3284);
}
{
Num x3285 = Stack.pop();
SymVal x3286 = SymStack.pop();
Num x3287 = Stack.pop();
SymStack.pop();
int x3288 = x3287.toInt();
Memory.storeInt(x3288, 8, x3285.toInt());
SymMemory.storeSym(x3288, 8, x3286);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3289 = Stack.pop();
SymVal x3290 = SymStack.pop();
Num x3291 = Stack.pop();
SymVal x3292 = SymStack.pop();
Num x3293 = x3291.i32_add(x3289);
Stack.push(x3293);
bool x3294 = allConcrete(x3292, x3290);
SymVal x3295 = x3294 ? Concrete(x3293, 32) : x3292.add(x3290);
SymStack.push(x3295);
}
{
Num x3296 = Stack.pop();
SymVal x3297 = SymStack.pop();
Frames.set(3, x3296);
SymFrames.set(3, x3297);
}
info("Jump to 1");
__attribute__((musttail)) return x3298(std::monostate{});
return std::monostate{};
}
std::monostate x3143(std::monostate x3144) {
info("Entering the false branch 38 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3145 = Stack.pop();
SymStack.pop();
Num x3146 = I32V(Memory.loadInt(x3145.toInt(), 0));
SymVal x3147 = SymMemory.loadSym(x3145.toInt(), 0);
Stack.push(x3146);
SymStack.push(x3147);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3148 = Stack.pop();
SymVal x3149 = SymStack.pop();
Num x3150 = Stack.pop();
SymVal x3151 = SymStack.pop();
Num x3152 = x3150.i32_sub(x3148);
Stack.push(x3152);
bool x3153 = allConcrete(x3151, x3149);
SymVal x3154 = x3153 ? Concrete(x3152, 32) : x3151.minus(x3149);
SymStack.push(x3154);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3155 = Stack.pop();
SymVal x3156 = SymStack.pop();
Num x3157 = Stack.pop();
SymVal x3158 = SymStack.pop();
Num x3159 = x3157.i32_mul(x3155);
Stack.push(x3159);
bool x3160 = allConcrete(x3158, x3156);
SymVal x3161 = x3160 ? Concrete(x3159, 32) : x3158.mul(x3156);
SymStack.push(x3161);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3162 = Stack.pop();
SymVal x3163 = SymStack.pop();
Num x3164 = Stack.pop();
SymVal x3165 = SymStack.pop();
Num x3166 = x3164.i32_mul(x3162);
Stack.push(x3166);
bool x3167 = allConcrete(x3165, x3163);
SymVal x3168 = x3167 ? Concrete(x3166, 32) : x3165.mul(x3163);
SymStack.push(x3168);
}
{
Num x3169 = Stack.pop();
SymVal x3170 = SymStack.pop();
Num x3171 = Stack.pop();
SymVal x3172 = SymStack.pop();
Num x3173 = x3171.i32_add(x3169);
Stack.push(x3173);
bool x3174 = allConcrete(x3172, x3170);
SymVal x3175 = x3174 ? Concrete(x3173, 32) : x3172.add(x3170);
SymStack.push(x3175);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x3176 = Stack.pop();
SymVal x3177 = SymStack.pop();
Num x3178 = Stack.pop();
SymVal x3179 = SymStack.pop();
Num x3180 = x3178.i32_add(x3176);
Stack.push(x3180);
bool x3181 = allConcrete(x3179, x3177);
SymVal x3182 = x3181 ? Concrete(x3180, 32) : x3179.add(x3177);
SymStack.push(x3182);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3183 = Stack.pop();
SymStack.pop();
Num x3184 = I32V(Memory.loadInt(x3183.toInt(), 0));
SymVal x3185 = SymMemory.loadSym(x3183.toInt(), 0);
Stack.push(x3184);
SymStack.push(x3185);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3186 = Stack.pop();
SymVal x3187 = SymStack.pop();
Num x3188 = Stack.pop();
SymVal x3189 = SymStack.pop();
Num x3190 = x3188.i32_sub(x3186);
Stack.push(x3190);
bool x3191 = allConcrete(x3189, x3187);
SymVal x3192 = x3191 ? Concrete(x3190, 32) : x3189.minus(x3187);
SymStack.push(x3192);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3193 = Stack.pop();
SymVal x3194 = SymStack.pop();
Num x3195 = Stack.pop();
SymVal x3196 = SymStack.pop();
Num x3197 = x3195.i32_mul(x3193);
Stack.push(x3197);
bool x3198 = allConcrete(x3196, x3194);
SymVal x3199 = x3198 ? Concrete(x3197, 32) : x3196.mul(x3194);
SymStack.push(x3199);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3200 = Stack.pop();
SymVal x3201 = SymStack.pop();
Num x3202 = Stack.pop();
SymVal x3203 = SymStack.pop();
Num x3204 = x3202.i32_add(x3200);
Stack.push(x3204);
bool x3205 = allConcrete(x3203, x3201);
SymVal x3206 = x3205 ? Concrete(x3204, 32) : x3203.add(x3201);
SymStack.push(x3206);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3207 = Stack.pop();
SymVal x3208 = SymStack.pop();
Num x3209 = Stack.pop();
SymVal x3210 = SymStack.pop();
Num x3211 = x3209.i32_mul(x3207);
Stack.push(x3211);
bool x3212 = allConcrete(x3210, x3208);
SymVal x3213 = x3212 ? Concrete(x3211, 32) : x3210.mul(x3208);
SymStack.push(x3213);
}
{
Num x3214 = Stack.pop();
SymVal x3215 = SymStack.pop();
Num x3216 = Stack.pop();
SymVal x3217 = SymStack.pop();
Num x3218 = x3216.i32_add(x3214);
Stack.push(x3218);
bool x3219 = allConcrete(x3217, x3215);
SymVal x3220 = x3219 ? Concrete(x3218, 32) : x3217.add(x3215);
SymStack.push(x3220);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x3221 = Stack.pop();
SymVal x3222 = SymStack.pop();
Num x3223 = Stack.pop();
SymVal x3224 = SymStack.pop();
Num x3225 = x3223.i32_add(x3221);
Stack.push(x3225);
bool x3226 = allConcrete(x3224, x3222);
SymVal x3227 = x3226 ? Concrete(x3225, 32) : x3224.add(x3222);
SymStack.push(x3227);
}
{
Num x3228 = Stack.pop();
SymStack.pop();
Num x3229 = I32V(Memory.loadInt(x3228.toInt(), 8));
SymVal x3230 = SymMemory.loadSym(x3228.toInt(), 8);
Stack.push(x3229);
SymStack.push(x3230);
}
{
Num x3231 = Stack.pop();
SymVal x3232 = SymStack.pop();
Num x3233 = Stack.pop();
SymStack.pop();
int x3234 = x3233.toInt();
Memory.storeInt(x3234, 8, x3231.toInt());
SymMemory.storeSym(x3234, 8, x3232);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3235 = Stack.pop();
SymVal x3236 = SymStack.pop();
Num x3237 = Stack.pop();
SymVal x3238 = SymStack.pop();
Num x3239 = x3237.i32_add(x3235);
Stack.push(x3239);
bool x3240 = allConcrete(x3238, x3236);
SymVal x3241 = x3240 ? Concrete(x3239, 32) : x3238.add(x3236);
SymStack.push(x3241);
}
{
Num x3242 = Stack.pop();
SymVal x3243 = SymStack.pop();
Frames.set(3, x3242);
SymFrames.set(3, x3243);
}
info("Jump to 1");
__attribute__((musttail)) return x3244(std::monostate{});
return std::monostate{};
}
std::monostate x3031(std::monostate x3032) {
info("Entering the false branch 41 of the if");
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x3033 = Stack.pop();
SymStack.pop();
Num x3034 = I32V(Memory.loadInt(x3033.toInt(), 4));
SymVal x3035 = SymMemory.loadSym(x3033.toInt(), 4);
Stack.push(x3034);
SymStack.push(x3035);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x3036 = Stack.pop();
SymVal x3037 = SymStack.pop();
Num x3038 = Stack.pop();
SymVal x3039 = SymStack.pop();
Num x3040 = x3038.i32_add(x3036);
Stack.push(x3040);
bool x3041 = allConcrete(x3039, x3037);
SymVal x3042 = x3041 ? Concrete(x3040, 32) : x3039.add(x3037);
SymStack.push(x3042);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3043 = Stack.pop();
SymVal x3044 = SymStack.pop();
Num x3045 = Stack.pop();
SymVal x3046 = SymStack.pop();
Num x3047 = x3045.i32_add(x3043);
Stack.push(x3047);
bool x3048 = allConcrete(x3046, x3044);
SymVal x3049 = x3048 ? Concrete(x3047, 32) : x3046.add(x3044);
SymStack.push(x3049);
}
{
Num x3050 = Stack.pop();
SymVal x3051 = SymStack.pop();
Num x3052 = Stack.pop();
SymVal x3053 = SymStack.pop();
Num x3054 = x3052.i32_mul(x3050);
Stack.push(x3054);
bool x3055 = allConcrete(x3053, x3051);
SymVal x3056 = x3055 ? Concrete(x3054, 32) : x3053.mul(x3051);
SymStack.push(x3056);
}
{
Num x3057 = Stack.pop();
SymVal x3058 = SymStack.pop();
Num x3059 = Stack.pop();
SymVal x3060 = SymStack.pop();
Num x3061 = x3059.i32_add(x3057);
Stack.push(x3061);
bool x3062 = allConcrete(x3060, x3058);
SymVal x3063 = x3062 ? Concrete(x3061, 32) : x3060.add(x3058);
SymStack.push(x3063);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3064 = Stack.pop();
SymStack.pop();
Num x3065 = I32V(Memory.loadInt(x3064.toInt(), 0));
SymVal x3066 = SymMemory.loadSym(x3064.toInt(), 0);
Stack.push(x3065);
SymStack.push(x3066);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3067 = Stack.pop();
SymVal x3068 = SymStack.pop();
Num x3069 = Stack.pop();
SymVal x3070 = SymStack.pop();
Num x3071 = x3069.i32_sub(x3067);
Stack.push(x3071);
bool x3072 = allConcrete(x3070, x3068);
SymVal x3073 = x3072 ? Concrete(x3071, 32) : x3070.minus(x3068);
SymStack.push(x3073);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3074 = Stack.pop();
SymVal x3075 = SymStack.pop();
Num x3076 = Stack.pop();
SymVal x3077 = SymStack.pop();
Num x3078 = x3076.i32_mul(x3074);
Stack.push(x3078);
bool x3079 = allConcrete(x3077, x3075);
SymVal x3080 = x3079 ? Concrete(x3078, 32) : x3077.mul(x3075);
SymStack.push(x3080);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3081 = Stack.pop();
SymVal x3082 = SymStack.pop();
Num x3083 = Stack.pop();
SymVal x3084 = SymStack.pop();
Num x3085 = x3083.i32_add(x3081);
Stack.push(x3085);
bool x3086 = allConcrete(x3084, x3082);
SymVal x3087 = x3086 ? Concrete(x3085, 32) : x3084.add(x3082);
SymStack.push(x3087);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3088 = Stack.pop();
SymVal x3089 = SymStack.pop();
Num x3090 = Stack.pop();
SymVal x3091 = SymStack.pop();
Num x3092 = x3090.i32_mul(x3088);
Stack.push(x3092);
bool x3093 = allConcrete(x3091, x3089);
SymVal x3094 = x3093 ? Concrete(x3092, 32) : x3091.mul(x3089);
SymStack.push(x3094);
}
{
Num x3095 = Stack.pop();
SymVal x3096 = SymStack.pop();
Num x3097 = Stack.pop();
SymVal x3098 = SymStack.pop();
Num x3099 = x3097.i32_add(x3095);
Stack.push(x3099);
bool x3100 = allConcrete(x3098, x3096);
SymVal x3101 = x3100 ? Concrete(x3099, 32) : x3098.add(x3096);
SymStack.push(x3101);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x3102 = Stack.pop();
SymVal x3103 = SymStack.pop();
Num x3104 = Stack.pop();
SymVal x3105 = SymStack.pop();
Num x3106 = x3104.i32_add(x3102);
Stack.push(x3106);
bool x3107 = allConcrete(x3105, x3103);
SymVal x3108 = x3107 ? Concrete(x3106, 32) : x3105.add(x3103);
SymStack.push(x3108);
}
{
Num x3109 = Stack.pop();
SymStack.pop();
Num x3110 = I32V(Memory.loadInt(x3109.toInt(), 8));
SymVal x3111 = SymMemory.loadSym(x3109.toInt(), 8);
Stack.push(x3110);
SymStack.push(x3111);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x3112 = Stack.pop();
SymVal x3113 = SymStack.pop();
Num x3114 = Stack.pop();
SymVal x3115 = SymStack.pop();
Num x3116 = x3114.i32_mul(x3112);
Stack.push(x3116);
bool x3117 = allConcrete(x3115, x3113);
SymVal x3118 = x3117 ? Concrete(x3116, 32) : x3115.mul(x3113);
SymStack.push(x3118);
}
{
Num x3119 = Stack.pop();
SymVal x3120 = SymStack.pop();
Num x3121 = Stack.pop();
SymVal x3122 = SymStack.pop();
Num x3123 = x3121.i32_add(x3119);
Stack.push(x3123);
bool x3124 = allConcrete(x3122, x3120);
SymVal x3125 = x3124 ? Concrete(x3123, 32) : x3122.add(x3120);
SymStack.push(x3125);
}
{
Num x3126 = Stack.pop();
SymStack.pop();
Num x3127 = I32V(Memory.loadInt(x3126.toInt(), 8));
SymVal x3128 = SymMemory.loadSym(x3126.toInt(), 8);
Stack.push(x3127);
SymStack.push(x3128);
}
{
Num x3129 = Stack.pop();
SymVal x3130 = SymStack.pop();
Num x3131 = Stack.pop();
SymStack.pop();
int x3132 = x3131.toInt();
Memory.storeInt(x3132, 8, x3129.toInt());
SymMemory.storeSym(x3132, 8, x3130);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3133 = Stack.pop();
SymVal x3134 = SymStack.pop();
Num x3135 = Stack.pop();
SymVal x3136 = SymStack.pop();
Num x3137 = x3135.i32_add(x3133);
Stack.push(x3137);
bool x3138 = allConcrete(x3136, x3134);
SymVal x3139 = x3138 ? Concrete(x3137, 32) : x3136.add(x3134);
SymStack.push(x3139);
}
{
Num x3140 = Stack.pop();
SymVal x3141 = SymStack.pop();
Frames.set(3, x3140);
SymFrames.set(3, x3141);
}
info("Jump to 1");
__attribute__((musttail)) return x3142(std::monostate{});
return std::monostate{};
}
std::monostate x3029(std::monostate x3030) {
info("Entering the false branch 34 of the if");
__attribute__((musttail)) return x2996(std::monostate{});
return std::monostate{};
}
std::monostate x2996(std::monostate x2997) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(Frames.get(4));
SymStack.push(SymFrames.get(4));
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x2998 = Stack.pop();
SymVal x2999 = SymStack.pop();
Num x3000 = Stack.pop();
SymVal x3001 = SymStack.pop();
Num x3002 = x3000.i32_eq(x2998);
Stack.push(x3002);
bool x3003 = allConcrete(x3001, x2999);
SymVal x3004 = x3003 ? Concrete(x3002, 32) : x3001.eq(x2999).bool2bv();
SymStack.push(x3004);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3005 = Stack.pop();
SymVal x3006 = SymStack.pop();
Num x3007 = Stack.pop();
SymVal x3008 = SymStack.pop();
Num x3009 = x3007.i32_sub(x3005);
Stack.push(x3009);
bool x3010 = allConcrete(x3008, x3006);
SymVal x3011 = x3010 ? Concrete(x3009, 32) : x3008.minus(x3006);
SymStack.push(x3011);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3012 = Stack.pop();
SymVal x3013 = SymStack.pop();
Num x3014 = Stack.pop();
SymVal x3015 = SymStack.pop();
Num x3016 = x3014.i32_ge_s(x3012);
Stack.push(x3016);
bool x3017 = allConcrete(x3015, x3013);
SymVal x3018 = x3017 ? Concrete(x3016, 32) : x3015.ge(x3013).bool2bv();
SymStack.push(x3018);
}
{
Num x3019 = Stack.pop();
SymVal x3020 = SymStack.pop();
Num x3021 = Stack.pop();
SymVal x3022 = SymStack.pop();
Num x3023 = x3021.i32_and(x3019);
Stack.push(x3023);
bool x3024 = allConcrete(x3022, x3020);
SymVal x3025 = x3024 ? Concrete(x3023, 32) : x3022.bitwise_and(x3020);
SymStack.push(x3025);
}
Num x3026 = Stack.pop();
{
SymVal x3027 = SymStack.pop();
ExploreTree.fillIfElseNode(x3027, 35);
}
int x3028 = x3026.toInt();
if (x3028 != 0) {
ExploreTree.moveCursor(true, makeControl(x2073, CURRENT_MCONT));
__attribute__((musttail)) return x2851(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x2851, CURRENT_MCONT));
__attribute__((musttail)) return x2073(std::monostate{});
}
return std::monostate{};
}
std::monostate x2851(std::monostate x2852) {
info("Entering the true branch 35 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x2853 = Stack.pop();
SymStack.pop();
Num x2854 = I32V(Memory.loadInt(x2853.toInt(), 0));
SymVal x2855 = SymMemory.loadSym(x2853.toInt(), 0);
Stack.push(x2854);
SymStack.push(x2855);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2856 = Stack.pop();
SymVal x2857 = SymStack.pop();
Num x2858 = Stack.pop();
SymVal x2859 = SymStack.pop();
Num x2860 = x2858.i32_sub(x2856);
Stack.push(x2860);
bool x2861 = allConcrete(x2859, x2857);
SymVal x2862 = x2861 ? Concrete(x2860, 32) : x2859.minus(x2857);
SymStack.push(x2862);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2863 = Stack.pop();
SymVal x2864 = SymStack.pop();
Num x2865 = Stack.pop();
SymVal x2866 = SymStack.pop();
Num x2867 = x2865.i32_mul(x2863);
Stack.push(x2867);
bool x2868 = allConcrete(x2866, x2864);
SymVal x2869 = x2868 ? Concrete(x2867, 32) : x2866.mul(x2864);
SymStack.push(x2869);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2870 = Stack.pop();
SymVal x2871 = SymStack.pop();
Num x2872 = Stack.pop();
SymVal x2873 = SymStack.pop();
Num x2874 = x2872.i32_sub(x2870);
Stack.push(x2874);
bool x2875 = allConcrete(x2873, x2871);
SymVal x2876 = x2875 ? Concrete(x2874, 32) : x2873.minus(x2871);
SymStack.push(x2876);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2877 = Stack.pop();
SymVal x2878 = SymStack.pop();
Num x2879 = Stack.pop();
SymVal x2880 = SymStack.pop();
Num x2881 = x2879.i32_mul(x2877);
Stack.push(x2881);
bool x2882 = allConcrete(x2880, x2878);
SymVal x2883 = x2882 ? Concrete(x2881, 32) : x2880.mul(x2878);
SymStack.push(x2883);
}
{
Num x2884 = Stack.pop();
SymVal x2885 = SymStack.pop();
Num x2886 = Stack.pop();
SymVal x2887 = SymStack.pop();
Num x2888 = x2886.i32_add(x2884);
Stack.push(x2888);
bool x2889 = allConcrete(x2887, x2885);
SymVal x2890 = x2889 ? Concrete(x2888, 32) : x2887.add(x2885);
SymStack.push(x2890);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x2891 = Stack.pop();
SymVal x2892 = SymStack.pop();
Num x2893 = Stack.pop();
SymVal x2894 = SymStack.pop();
Num x2895 = x2893.i32_add(x2891);
Stack.push(x2895);
bool x2896 = allConcrete(x2894, x2892);
SymVal x2897 = x2896 ? Concrete(x2895, 32) : x2894.add(x2892);
SymStack.push(x2897);
}
{
Num x2898 = Stack.pop();
SymStack.pop();
Num x2899 = I32V(Memory.loadInt(x2898.toInt(), 8));
SymVal x2900 = SymMemory.loadSym(x2898.toInt(), 8);
Stack.push(x2899);
SymStack.push(x2900);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x2901 = Stack.pop();
SymStack.pop();
Num x2902 = I32V(Memory.loadInt(x2901.toInt(), 0));
SymVal x2903 = SymMemory.loadSym(x2901.toInt(), 0);
Stack.push(x2902);
SymStack.push(x2903);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2904 = Stack.pop();
SymVal x2905 = SymStack.pop();
Num x2906 = Stack.pop();
SymVal x2907 = SymStack.pop();
Num x2908 = x2906.i32_sub(x2904);
Stack.push(x2908);
bool x2909 = allConcrete(x2907, x2905);
SymVal x2910 = x2909 ? Concrete(x2908, 32) : x2907.minus(x2905);
SymStack.push(x2910);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2911 = Stack.pop();
SymVal x2912 = SymStack.pop();
Num x2913 = Stack.pop();
SymVal x2914 = SymStack.pop();
Num x2915 = x2913.i32_mul(x2911);
Stack.push(x2915);
bool x2916 = allConcrete(x2914, x2912);
SymVal x2917 = x2916 ? Concrete(x2915, 32) : x2914.mul(x2912);
SymStack.push(x2917);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2918 = Stack.pop();
SymVal x2919 = SymStack.pop();
Num x2920 = Stack.pop();
SymVal x2921 = SymStack.pop();
Num x2922 = x2920.i32_sub(x2918);
Stack.push(x2922);
bool x2923 = allConcrete(x2921, x2919);
SymVal x2924 = x2923 ? Concrete(x2922, 32) : x2921.minus(x2919);
SymStack.push(x2924);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2925 = Stack.pop();
SymVal x2926 = SymStack.pop();
Num x2927 = Stack.pop();
SymVal x2928 = SymStack.pop();
Num x2929 = x2927.i32_mul(x2925);
Stack.push(x2929);
bool x2930 = allConcrete(x2928, x2926);
SymVal x2931 = x2930 ? Concrete(x2929, 32) : x2928.mul(x2926);
SymStack.push(x2931);
}
{
Num x2932 = Stack.pop();
SymVal x2933 = SymStack.pop();
Num x2934 = Stack.pop();
SymVal x2935 = SymStack.pop();
Num x2936 = x2934.i32_add(x2932);
Stack.push(x2936);
bool x2937 = allConcrete(x2935, x2933);
SymVal x2938 = x2937 ? Concrete(x2936, 32) : x2935.add(x2933);
SymStack.push(x2938);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x2939 = Stack.pop();
SymVal x2940 = SymStack.pop();
Num x2941 = Stack.pop();
SymVal x2942 = SymStack.pop();
Num x2943 = x2941.i32_add(x2939);
Stack.push(x2943);
bool x2944 = allConcrete(x2942, x2940);
SymVal x2945 = x2944 ? Concrete(x2943, 32) : x2942.add(x2940);
SymStack.push(x2945);
}
{
Num x2946 = Stack.pop();
SymStack.pop();
Num x2947 = I32V(Memory.loadInt(x2946.toInt(), 8));
SymVal x2948 = SymMemory.loadSym(x2946.toInt(), 8);
Stack.push(x2947);
SymStack.push(x2948);
}
{
Num x2949 = Stack.pop();
SymStack.pop();
Num x2950 = I32V(Memory.loadInt(x2949.toInt(), 4));
SymVal x2951 = SymMemory.loadSym(x2949.toInt(), 4);
Stack.push(x2950);
SymStack.push(x2951);
}
{
Num x2952 = Stack.pop();
SymVal x2953 = SymStack.pop();
Num x2954 = Stack.pop();
SymVal x2955 = SymStack.pop();
Num x2956 = x2954.i32_mul(x2952);
Stack.push(x2956);
bool x2957 = allConcrete(x2955, x2953);
SymVal x2958 = x2957 ? Concrete(x2956, 32) : x2955.mul(x2953);
SymStack.push(x2958);
}
{
Num x2959 = Stack.pop();
SymVal x2960 = SymStack.pop();
Num x2961 = Stack.pop();
SymVal x2962 = SymStack.pop();
Num x2963 = x2961.i32_add(x2959);
Stack.push(x2963);
bool x2964 = allConcrete(x2962, x2960);
SymVal x2965 = x2964 ? Concrete(x2963, 32) : x2962.add(x2960);
SymStack.push(x2965);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2966 = Stack.pop();
SymVal x2967 = SymStack.pop();
Num x2968 = Stack.pop();
SymVal x2969 = SymStack.pop();
Num x2970 = x2968.i32_sub(x2966);
Stack.push(x2970);
bool x2971 = allConcrete(x2969, x2967);
SymVal x2972 = x2971 ? Concrete(x2970, 32) : x2969.minus(x2967);
SymStack.push(x2972);
}
{
Num x2973 = Stack.pop();
SymVal x2974 = SymStack.pop();
Num x2975 = Stack.pop();
SymVal x2976 = SymStack.pop();
Num x2977 = x2975.i32_mul(x2973);
Stack.push(x2977);
bool x2978 = allConcrete(x2976, x2974);
SymVal x2979 = x2978 ? Concrete(x2977, 32) : x2976.mul(x2974);
SymStack.push(x2979);
}
{
Num x2980 = Stack.pop();
SymVal x2981 = SymStack.pop();
Num x2982 = Stack.pop();
SymVal x2983 = SymStack.pop();
Num x2984 = x2982.i32_add(x2980);
Stack.push(x2984);
bool x2985 = allConcrete(x2983, x2981);
SymVal x2986 = x2985 ? Concrete(x2984, 32) : x2983.add(x2981);
SymStack.push(x2986);
}
{
Num x2987 = Stack.pop();
SymStack.pop();
Num x2988 = I32V(Memory.loadInt(x2987.toInt(), 8));
SymVal x2989 = SymMemory.loadSym(x2987.toInt(), 8);
Stack.push(x2988);
SymStack.push(x2989);
}
{
Num x2990 = Stack.pop();
SymVal x2991 = SymStack.pop();
Num x2992 = Stack.pop();
SymStack.pop();
int x2993 = x2992.toInt();
Memory.storeInt(x2993, 8, x2990.toInt());
SymMemory.storeSym(x2993, 8, x2991);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x2994 = Stack.pop();
SymVal x2995 = SymStack.pop();
Frames.set(3, x2994);
SymFrames.set(3, x2995);
}
__attribute__((musttail)) return x2234(std::monostate{});
return std::monostate{};
}
std::monostate x2234(std::monostate x2837) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x2838 = Stack.pop();
SymStack.pop();
Num x2839 = I32V(Memory.loadInt(x2838.toInt(), 4));
SymVal x2840 = SymMemory.loadSym(x2838.toInt(), 4);
Stack.push(x2839);
SymStack.push(x2840);
}
{
Num x2841 = Stack.pop();
SymVal x2842 = SymStack.pop();
Num x2843 = Stack.pop();
SymVal x2844 = SymStack.pop();
Num x2845 = x2843.i32_eq(x2841);
Stack.push(x2845);
bool x2846 = allConcrete(x2844, x2842);
SymVal x2847 = x2846 ? Concrete(x2845, 32) : x2844.eq(x2842).bool2bv();
SymStack.push(x2847);
}
Num x2848 = Stack.pop();
{
SymVal x2849 = SymStack.pop();
ExploreTree.fillIfElseNode(x2849, 36);
}
int x2850 = x2848.toInt();
if (x2850 != 0) {
ExploreTree.moveCursor(true, makeControl(x2075, CURRENT_MCONT));
__attribute__((musttail)) return x2835(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x2835, CURRENT_MCONT));
__attribute__((musttail)) return x2075(std::monostate{});
}
return std::monostate{};
}
std::monostate x2835(std::monostate x2836) {
info("Entering the true branch 36 of the if");
info("Jump to 2");
__attribute__((musttail)) return x2820(std::monostate{});
return std::monostate{};
}
std::monostate x2820(std::monostate x2821) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x2822 = Stack.pop();
SymStack.pop();
Num x2823 = I32V(Memory.loadInt(x2822.toInt(), 0));
SymVal x2824 = SymMemory.loadSym(x2822.toInt(), 0);
Stack.push(x2823);
SymStack.push(x2824);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2825 = Stack.pop();
SymVal x2826 = SymStack.pop();
Num x2827 = Stack.pop();
SymVal x2828 = SymStack.pop();
Num x2829 = x2827.i32_ne(x2825);
Stack.push(x2829);
bool x2830 = allConcrete(x2828, x2826);
SymVal x2831 = x2830 ? Concrete(x2829, 32) : x2828.neq(x2826).bool2bv();
SymStack.push(x2831);
}
Num x2832 = Stack.pop();
{
SymVal x2833 = SymStack.pop();
ExploreTree.fillIfElseNode(x2833, 37);
}
int x2834 = x2832.toInt();
if (x2834 != 0) {
ExploreTree.moveCursor(true, makeControl(x2581, CURRENT_MCONT));
__attribute__((musttail)) return x2816(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x2816, CURRENT_MCONT));
__attribute__((musttail)) return x2581(std::monostate{});
}
return std::monostate{};
}
std::monostate x2816(std::monostate x2817) {
info("Entering the true branch 37 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x2818 = Stack.pop();
SymVal x2819 = SymStack.pop();
Frames.set(3, x2818);
SymFrames.set(3, x2819);
}
__attribute__((musttail)) return x2790(std::monostate{});
return std::monostate{};
}
std::monostate x2790(std::monostate x2795) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x2796 = Stack.pop();
SymStack.pop();
Num x2797 = I32V(Memory.loadInt(x2796.toInt(), 4));
SymVal x2798 = SymMemory.loadSym(x2796.toInt(), 4);
Stack.push(x2797);
SymStack.push(x2798);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2799 = Stack.pop();
SymVal x2800 = SymStack.pop();
Num x2801 = Stack.pop();
SymVal x2802 = SymStack.pop();
Num x2803 = x2801.i32_add(x2799);
Stack.push(x2803);
bool x2804 = allConcrete(x2802, x2800);
SymVal x2805 = x2804 ? Concrete(x2803, 32) : x2802.add(x2800);
SymStack.push(x2805);
}
{
Num x2806 = Stack.pop();
SymVal x2807 = SymStack.pop();
Num x2808 = Stack.pop();
SymVal x2809 = SymStack.pop();
Num x2810 = x2808.i32_eq(x2806);
Stack.push(x2810);
bool x2811 = allConcrete(x2809, x2807);
SymVal x2812 = x2811 ? Concrete(x2810, 32) : x2809.eq(x2807).bool2bv();
SymStack.push(x2812);
}
Num x2813 = Stack.pop();
{
SymVal x2814 = SymStack.pop();
ExploreTree.fillIfElseNode(x2814, 40);
}
int x2815 = x2813.toInt();
if (x2815 != 0) {
ExploreTree.moveCursor(true, makeControl(x2583, CURRENT_MCONT));
__attribute__((musttail)) return x2793(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x2793, CURRENT_MCONT));
__attribute__((musttail)) return x2583(std::monostate{});
}
return std::monostate{};
}
std::monostate x2793(std::monostate x2794) {
info("Entering the true branch 40 of the if");
info("Jump to 2");
__attribute__((musttail)) return x2791(std::monostate{});
return std::monostate{};
}
std::monostate x2791(std::monostate x2792) {
info("Exiting the block, stackSize =", Stack.size());
__attribute__((musttail)) return x2457(std::monostate{});
return std::monostate{};
}
std::monostate x2583(std::monostate x2584) {
info("Entering the false branch 40 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x2585 = Stack.pop();
SymStack.pop();
Num x2586 = I32V(Memory.loadInt(x2585.toInt(), 0));
SymVal x2587 = SymMemory.loadSym(x2585.toInt(), 0);
Stack.push(x2586);
SymStack.push(x2587);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2588 = Stack.pop();
SymVal x2589 = SymStack.pop();
Num x2590 = Stack.pop();
SymVal x2591 = SymStack.pop();
Num x2592 = x2590.i32_sub(x2588);
Stack.push(x2592);
bool x2593 = allConcrete(x2591, x2589);
SymVal x2594 = x2593 ? Concrete(x2592, 32) : x2591.minus(x2589);
SymStack.push(x2594);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2595 = Stack.pop();
SymVal x2596 = SymStack.pop();
Num x2597 = Stack.pop();
SymVal x2598 = SymStack.pop();
Num x2599 = x2597.i32_mul(x2595);
Stack.push(x2599);
bool x2600 = allConcrete(x2598, x2596);
SymVal x2601 = x2600 ? Concrete(x2599, 32) : x2598.mul(x2596);
SymStack.push(x2601);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x2602 = Stack.pop();
SymStack.pop();
Num x2603 = I32V(Memory.loadInt(x2602.toInt(), 0));
SymVal x2604 = SymMemory.loadSym(x2602.toInt(), 0);
Stack.push(x2603);
SymStack.push(x2604);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2605 = Stack.pop();
SymVal x2606 = SymStack.pop();
Num x2607 = Stack.pop();
SymVal x2608 = SymStack.pop();
Num x2609 = x2607.i32_sub(x2605);
Stack.push(x2609);
bool x2610 = allConcrete(x2608, x2606);
SymVal x2611 = x2610 ? Concrete(x2609, 32) : x2608.minus(x2606);
SymStack.push(x2611);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2612 = Stack.pop();
SymVal x2613 = SymStack.pop();
Num x2614 = Stack.pop();
SymVal x2615 = SymStack.pop();
Num x2616 = x2614.i32_mul(x2612);
Stack.push(x2616);
bool x2617 = allConcrete(x2615, x2613);
SymVal x2618 = x2617 ? Concrete(x2616, 32) : x2615.mul(x2613);
SymStack.push(x2618);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2619 = Stack.pop();
SymVal x2620 = SymStack.pop();
Num x2621 = Stack.pop();
SymVal x2622 = SymStack.pop();
Num x2623 = x2621.i32_sub(x2619);
Stack.push(x2623);
bool x2624 = allConcrete(x2622, x2620);
SymVal x2625 = x2624 ? Concrete(x2623, 32) : x2622.minus(x2620);
SymStack.push(x2625);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2626 = Stack.pop();
SymVal x2627 = SymStack.pop();
Num x2628 = Stack.pop();
SymVal x2629 = SymStack.pop();
Num x2630 = x2628.i32_mul(x2626);
Stack.push(x2630);
bool x2631 = allConcrete(x2629, x2627);
SymVal x2632 = x2631 ? Concrete(x2630, 32) : x2629.mul(x2627);
SymStack.push(x2632);
}
{
Num x2633 = Stack.pop();
SymVal x2634 = SymStack.pop();
Num x2635 = Stack.pop();
SymVal x2636 = SymStack.pop();
Num x2637 = x2635.i32_add(x2633);
Stack.push(x2637);
bool x2638 = allConcrete(x2636, x2634);
SymVal x2639 = x2638 ? Concrete(x2637, 32) : x2636.add(x2634);
SymStack.push(x2639);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x2640 = Stack.pop();
SymVal x2641 = SymStack.pop();
Num x2642 = Stack.pop();
SymVal x2643 = SymStack.pop();
Num x2644 = x2642.i32_add(x2640);
Stack.push(x2644);
bool x2645 = allConcrete(x2643, x2641);
SymVal x2646 = x2645 ? Concrete(x2644, 32) : x2643.add(x2641);
SymStack.push(x2646);
}
{
Num x2647 = Stack.pop();
SymStack.pop();
Num x2648 = I32V(Memory.loadInt(x2647.toInt(), 8));
SymVal x2649 = SymMemory.loadSym(x2647.toInt(), 8);
Stack.push(x2648);
SymStack.push(x2649);
}
{
Num x2650 = Stack.pop();
SymStack.pop();
Num x2651 = I32V(Memory.loadInt(x2650.toInt(), 4));
SymVal x2652 = SymMemory.loadSym(x2650.toInt(), 4);
Stack.push(x2651);
SymStack.push(x2652);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x2653 = Stack.pop();
SymVal x2654 = SymStack.pop();
Num x2655 = Stack.pop();
SymVal x2656 = SymStack.pop();
Num x2657 = x2655.i32_add(x2653);
Stack.push(x2657);
bool x2658 = allConcrete(x2656, x2654);
SymVal x2659 = x2658 ? Concrete(x2657, 32) : x2656.add(x2654);
SymStack.push(x2659);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2660 = Stack.pop();
SymVal x2661 = SymStack.pop();
Num x2662 = Stack.pop();
SymVal x2663 = SymStack.pop();
Num x2664 = x2662.i32_add(x2660);
Stack.push(x2664);
bool x2665 = allConcrete(x2663, x2661);
SymVal x2666 = x2665 ? Concrete(x2664, 32) : x2663.add(x2661);
SymStack.push(x2666);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2667 = Stack.pop();
SymVal x2668 = SymStack.pop();
Num x2669 = Stack.pop();
SymVal x2670 = SymStack.pop();
Num x2671 = x2669.i32_mul(x2667);
Stack.push(x2671);
bool x2672 = allConcrete(x2670, x2668);
SymVal x2673 = x2672 ? Concrete(x2671, 32) : x2670.mul(x2668);
SymStack.push(x2673);
}
{
Num x2674 = Stack.pop();
SymVal x2675 = SymStack.pop();
Num x2676 = Stack.pop();
SymVal x2677 = SymStack.pop();
Num x2678 = x2676.i32_add(x2674);
Stack.push(x2678);
bool x2679 = allConcrete(x2677, x2675);
SymVal x2680 = x2679 ? Concrete(x2678, 32) : x2677.add(x2675);
SymStack.push(x2680);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x2681 = Stack.pop();
SymStack.pop();
Num x2682 = I32V(Memory.loadInt(x2681.toInt(), 0));
SymVal x2683 = SymMemory.loadSym(x2681.toInt(), 0);
Stack.push(x2682);
SymStack.push(x2683);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2684 = Stack.pop();
SymVal x2685 = SymStack.pop();
Num x2686 = Stack.pop();
SymVal x2687 = SymStack.pop();
Num x2688 = x2686.i32_sub(x2684);
Stack.push(x2688);
bool x2689 = allConcrete(x2687, x2685);
SymVal x2690 = x2689 ? Concrete(x2688, 32) : x2687.minus(x2685);
SymStack.push(x2690);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2691 = Stack.pop();
SymVal x2692 = SymStack.pop();
Num x2693 = Stack.pop();
SymVal x2694 = SymStack.pop();
Num x2695 = x2693.i32_mul(x2691);
Stack.push(x2695);
bool x2696 = allConcrete(x2694, x2692);
SymVal x2697 = x2696 ? Concrete(x2695, 32) : x2694.mul(x2692);
SymStack.push(x2697);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2698 = Stack.pop();
SymVal x2699 = SymStack.pop();
Num x2700 = Stack.pop();
SymVal x2701 = SymStack.pop();
Num x2702 = x2700.i32_sub(x2698);
Stack.push(x2702);
bool x2703 = allConcrete(x2701, x2699);
SymVal x2704 = x2703 ? Concrete(x2702, 32) : x2701.minus(x2699);
SymStack.push(x2704);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2705 = Stack.pop();
SymVal x2706 = SymStack.pop();
Num x2707 = Stack.pop();
SymVal x2708 = SymStack.pop();
Num x2709 = x2707.i32_mul(x2705);
Stack.push(x2709);
bool x2710 = allConcrete(x2708, x2706);
SymVal x2711 = x2710 ? Concrete(x2709, 32) : x2708.mul(x2706);
SymStack.push(x2711);
}
{
Num x2712 = Stack.pop();
SymVal x2713 = SymStack.pop();
Num x2714 = Stack.pop();
SymVal x2715 = SymStack.pop();
Num x2716 = x2714.i32_add(x2712);
Stack.push(x2716);
bool x2717 = allConcrete(x2715, x2713);
SymVal x2718 = x2717 ? Concrete(x2716, 32) : x2715.add(x2713);
SymStack.push(x2718);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x2719 = Stack.pop();
SymVal x2720 = SymStack.pop();
Num x2721 = Stack.pop();
SymVal x2722 = SymStack.pop();
Num x2723 = x2721.i32_add(x2719);
Stack.push(x2723);
bool x2724 = allConcrete(x2722, x2720);
SymVal x2725 = x2724 ? Concrete(x2723, 32) : x2722.add(x2720);
SymStack.push(x2725);
}
{
Num x2726 = Stack.pop();
SymStack.pop();
Num x2727 = I32V(Memory.loadInt(x2726.toInt(), 8));
SymVal x2728 = SymMemory.loadSym(x2726.toInt(), 8);
Stack.push(x2727);
SymStack.push(x2728);
}
{
Num x2729 = Stack.pop();
SymVal x2730 = SymStack.pop();
Num x2731 = Stack.pop();
SymVal x2732 = SymStack.pop();
Num x2733 = x2731.i32_add(x2729);
Stack.push(x2733);
bool x2734 = allConcrete(x2732, x2730);
SymVal x2735 = x2734 ? Concrete(x2733, 32) : x2732.add(x2730);
SymStack.push(x2735);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x2736 = Stack.pop();
SymStack.pop();
Num x2737 = I32V(Memory.loadInt(x2736.toInt(), 0));
SymVal x2738 = SymMemory.loadSym(x2736.toInt(), 0);
Stack.push(x2737);
SymStack.push(x2738);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2739 = Stack.pop();
SymVal x2740 = SymStack.pop();
Num x2741 = Stack.pop();
SymVal x2742 = SymStack.pop();
Num x2743 = x2741.i32_sub(x2739);
Stack.push(x2743);
bool x2744 = allConcrete(x2742, x2740);
SymVal x2745 = x2744 ? Concrete(x2743, 32) : x2742.minus(x2740);
SymStack.push(x2745);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2746 = Stack.pop();
SymVal x2747 = SymStack.pop();
Num x2748 = Stack.pop();
SymVal x2749 = SymStack.pop();
Num x2750 = x2748.i32_mul(x2746);
Stack.push(x2750);
bool x2751 = allConcrete(x2749, x2747);
SymVal x2752 = x2751 ? Concrete(x2750, 32) : x2749.mul(x2747);
SymStack.push(x2752);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2753 = Stack.pop();
SymVal x2754 = SymStack.pop();
Num x2755 = Stack.pop();
SymVal x2756 = SymStack.pop();
Num x2757 = x2755.i32_mul(x2753);
Stack.push(x2757);
bool x2758 = allConcrete(x2756, x2754);
SymVal x2759 = x2758 ? Concrete(x2757, 32) : x2756.mul(x2754);
SymStack.push(x2759);
}
{
Num x2760 = Stack.pop();
SymVal x2761 = SymStack.pop();
Num x2762 = Stack.pop();
SymVal x2763 = SymStack.pop();
Num x2764 = x2762.i32_add(x2760);
Stack.push(x2764);
bool x2765 = allConcrete(x2763, x2761);
SymVal x2766 = x2765 ? Concrete(x2764, 32) : x2763.add(x2761);
SymStack.push(x2766);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x2767 = Stack.pop();
SymVal x2768 = SymStack.pop();
Num x2769 = Stack.pop();
SymVal x2770 = SymStack.pop();
Num x2771 = x2769.i32_add(x2767);
Stack.push(x2771);
bool x2772 = allConcrete(x2770, x2768);
SymVal x2773 = x2772 ? Concrete(x2771, 32) : x2770.add(x2768);
SymStack.push(x2773);
}
{
Num x2774 = Stack.pop();
SymStack.pop();
Num x2775 = I32V(Memory.loadInt(x2774.toInt(), 8));
SymVal x2776 = SymMemory.loadSym(x2774.toInt(), 8);
Stack.push(x2775);
SymStack.push(x2776);
}
{
Num x2777 = Stack.pop();
SymVal x2778 = SymStack.pop();
Num x2779 = Stack.pop();
SymStack.pop();
int x2780 = x2779.toInt();
Memory.storeInt(x2780, 8, x2777.toInt());
SymMemory.storeSym(x2780, 8, x2778);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2781 = Stack.pop();
SymVal x2782 = SymStack.pop();
Num x2783 = Stack.pop();
SymVal x2784 = SymStack.pop();
Num x2785 = x2783.i32_add(x2781);
Stack.push(x2785);
bool x2786 = allConcrete(x2784, x2782);
SymVal x2787 = x2786 ? Concrete(x2785, 32) : x2784.add(x2782);
SymStack.push(x2787);
}
{
Num x2788 = Stack.pop();
SymVal x2789 = SymStack.pop();
Frames.set(3, x2788);
SymFrames.set(3, x2789);
}
info("Jump to 1");
__attribute__((musttail)) return x2790(std::monostate{});
return std::monostate{};
}
std::monostate x2581(std::monostate x2582) {
info("Entering the false branch 37 of the if");
__attribute__((musttail)) return x2457(std::monostate{});
return std::monostate{};
}
std::monostate x2457(std::monostate x2458) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x2459 = Stack.pop();
SymStack.pop();
Num x2460 = I32V(Memory.loadInt(x2459.toInt(), 0));
SymVal x2461 = SymMemory.loadSym(x2459.toInt(), 0);
Stack.push(x2460);
SymStack.push(x2461);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2462 = Stack.pop();
SymVal x2463 = SymStack.pop();
Num x2464 = Stack.pop();
SymVal x2465 = SymStack.pop();
Num x2466 = x2464.i32_sub(x2462);
Stack.push(x2466);
bool x2467 = allConcrete(x2465, x2463);
SymVal x2468 = x2467 ? Concrete(x2466, 32) : x2465.minus(x2463);
SymStack.push(x2468);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2469 = Stack.pop();
SymVal x2470 = SymStack.pop();
Num x2471 = Stack.pop();
SymVal x2472 = SymStack.pop();
Num x2473 = x2471.i32_mul(x2469);
Stack.push(x2473);
bool x2474 = allConcrete(x2472, x2470);
SymVal x2475 = x2474 ? Concrete(x2473, 32) : x2472.mul(x2470);
SymStack.push(x2475);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2476 = Stack.pop();
SymVal x2477 = SymStack.pop();
Num x2478 = Stack.pop();
SymVal x2479 = SymStack.pop();
Num x2480 = x2478.i32_sub(x2476);
Stack.push(x2480);
bool x2481 = allConcrete(x2479, x2477);
SymVal x2482 = x2481 ? Concrete(x2480, 32) : x2479.minus(x2477);
SymStack.push(x2482);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2483 = Stack.pop();
SymVal x2484 = SymStack.pop();
Num x2485 = Stack.pop();
SymVal x2486 = SymStack.pop();
Num x2487 = x2485.i32_mul(x2483);
Stack.push(x2487);
bool x2488 = allConcrete(x2486, x2484);
SymVal x2489 = x2488 ? Concrete(x2487, 32) : x2486.mul(x2484);
SymStack.push(x2489);
}
{
Num x2490 = Stack.pop();
SymVal x2491 = SymStack.pop();
Num x2492 = Stack.pop();
SymVal x2493 = SymStack.pop();
Num x2494 = x2492.i32_add(x2490);
Stack.push(x2494);
bool x2495 = allConcrete(x2493, x2491);
SymVal x2496 = x2495 ? Concrete(x2494, 32) : x2493.add(x2491);
SymStack.push(x2496);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x2497 = Stack.pop();
SymVal x2498 = SymStack.pop();
Num x2499 = Stack.pop();
SymVal x2500 = SymStack.pop();
Num x2501 = x2499.i32_add(x2497);
Stack.push(x2501);
bool x2502 = allConcrete(x2500, x2498);
SymVal x2503 = x2502 ? Concrete(x2501, 32) : x2500.add(x2498);
SymStack.push(x2503);
}
{
Num x2504 = Stack.pop();
SymStack.pop();
Num x2505 = I32V(Memory.loadInt(x2504.toInt(), 8));
SymVal x2506 = SymMemory.loadSym(x2504.toInt(), 8);
Stack.push(x2505);
SymStack.push(x2506);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x2507 = Stack.pop();
SymStack.pop();
Num x2508 = I32V(Memory.loadInt(x2507.toInt(), 0));
SymVal x2509 = SymMemory.loadSym(x2507.toInt(), 0);
Stack.push(x2508);
SymStack.push(x2509);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2510 = Stack.pop();
SymVal x2511 = SymStack.pop();
Num x2512 = Stack.pop();
SymVal x2513 = SymStack.pop();
Num x2514 = x2512.i32_sub(x2510);
Stack.push(x2514);
bool x2515 = allConcrete(x2513, x2511);
SymVal x2516 = x2515 ? Concrete(x2514, 32) : x2513.minus(x2511);
SymStack.push(x2516);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2517 = Stack.pop();
SymVal x2518 = SymStack.pop();
Num x2519 = Stack.pop();
SymVal x2520 = SymStack.pop();
Num x2521 = x2519.i32_mul(x2517);
Stack.push(x2521);
bool x2522 = allConcrete(x2520, x2518);
SymVal x2523 = x2522 ? Concrete(x2521, 32) : x2520.mul(x2518);
SymStack.push(x2523);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2524 = Stack.pop();
SymVal x2525 = SymStack.pop();
Num x2526 = Stack.pop();
SymVal x2527 = SymStack.pop();
Num x2528 = x2526.i32_sub(x2524);
Stack.push(x2528);
bool x2529 = allConcrete(x2527, x2525);
SymVal x2530 = x2529 ? Concrete(x2528, 32) : x2527.minus(x2525);
SymStack.push(x2530);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2531 = Stack.pop();
SymVal x2532 = SymStack.pop();
Num x2533 = Stack.pop();
SymVal x2534 = SymStack.pop();
Num x2535 = x2533.i32_mul(x2531);
Stack.push(x2535);
bool x2536 = allConcrete(x2534, x2532);
SymVal x2537 = x2536 ? Concrete(x2535, 32) : x2534.mul(x2532);
SymStack.push(x2537);
}
{
Num x2538 = Stack.pop();
SymVal x2539 = SymStack.pop();
Num x2540 = Stack.pop();
SymVal x2541 = SymStack.pop();
Num x2542 = x2540.i32_add(x2538);
Stack.push(x2542);
bool x2543 = allConcrete(x2541, x2539);
SymVal x2544 = x2543 ? Concrete(x2542, 32) : x2541.add(x2539);
SymStack.push(x2544);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x2545 = Stack.pop();
SymVal x2546 = SymStack.pop();
Num x2547 = Stack.pop();
SymVal x2548 = SymStack.pop();
Num x2549 = x2547.i32_add(x2545);
Stack.push(x2549);
bool x2550 = allConcrete(x2548, x2546);
SymVal x2551 = x2550 ? Concrete(x2549, 32) : x2548.add(x2546);
SymStack.push(x2551);
}
{
Num x2552 = Stack.pop();
SymStack.pop();
Num x2553 = I32V(Memory.loadInt(x2552.toInt(), 8));
SymVal x2554 = SymMemory.loadSym(x2552.toInt(), 8);
Stack.push(x2553);
SymStack.push(x2554);
}
{
Num x2555 = Stack.pop();
SymStack.pop();
Num x2556 = I32V(Memory.loadInt(x2555.toInt(), 4));
SymVal x2557 = SymMemory.loadSym(x2555.toInt(), 4);
Stack.push(x2556);
SymStack.push(x2557);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x2558 = Stack.pop();
SymStack.pop();
Num x2559 = I32V(Memory.loadInt(x2558.toInt(), 4));
SymVal x2560 = SymMemory.loadSym(x2558.toInt(), 4);
Stack.push(x2559);
SymStack.push(x2560);
}
{
Num x2561 = Stack.pop();
SymVal x2562 = SymStack.pop();
Num x2563 = Stack.pop();
SymVal x2564 = SymStack.pop();
Num x2565 = x2563.i32_add(x2561);
Stack.push(x2565);
bool x2566 = allConcrete(x2564, x2562);
SymVal x2567 = x2566 ? Concrete(x2565, 32) : x2564.add(x2562);
SymStack.push(x2567);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2568 = Stack.pop();
SymVal x2569 = SymStack.pop();
Num x2570 = Stack.pop();
SymVal x2571 = SymStack.pop();
Num x2572 = x2570.i32_add(x2568);
Stack.push(x2572);
bool x2573 = allConcrete(x2571, x2569);
SymVal x2574 = x2573 ? Concrete(x2572, 32) : x2571.add(x2569);
SymStack.push(x2574);
}
{
Num x2575 = Stack.pop();
SymVal x2576 = SymStack.pop();
Num x2577 = Stack.pop();
SymStack.pop();
int x2578 = x2577.toInt();
Memory.storeInt(x2578, 4, x2575.toInt());
SymMemory.storeSym(x2578, 4, x2576);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x2579 = Stack.pop();
SymVal x2580 = SymStack.pop();
Frames.set(3, x2579);
SymFrames.set(3, x2580);
}
__attribute__((musttail)) return x2336(std::monostate{});
return std::monostate{};
}
std::monostate x2336(std::monostate x2443) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x2444 = Stack.pop();
SymStack.pop();
Num x2445 = I32V(Memory.loadInt(x2444.toInt(), 4));
SymVal x2446 = SymMemory.loadSym(x2444.toInt(), 4);
Stack.push(x2445);
SymStack.push(x2446);
}
{
Num x2447 = Stack.pop();
SymVal x2448 = SymStack.pop();
Num x2449 = Stack.pop();
SymVal x2450 = SymStack.pop();
Num x2451 = x2449.i32_eq(x2447);
Stack.push(x2451);
bool x2452 = allConcrete(x2450, x2448);
SymVal x2453 = x2452 ? Concrete(x2451, 32) : x2450.eq(x2448).bool2bv();
SymStack.push(x2453);
}
Num x2454 = Stack.pop();
{
SymVal x2455 = SymStack.pop();
ExploreTree.fillIfElseNode(x2455, 38);
}
int x2456 = x2454.toInt();
if (x2456 != 0) {
ExploreTree.moveCursor(true, makeControl(x2235, CURRENT_MCONT));
__attribute__((musttail)) return x2441(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x2441, CURRENT_MCONT));
__attribute__((musttail)) return x2235(std::monostate{});
}
return std::monostate{};
}
std::monostate x2441(std::monostate x2442) {
info("Entering the true branch 38 of the if");
info("Jump to 2");
__attribute__((musttail)) return x2430(std::monostate{});
return std::monostate{};
}
std::monostate x2430(std::monostate x2431) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2432 = Stack.pop();
SymVal x2433 = SymStack.pop();
Num x2434 = Stack.pop();
SymVal x2435 = SymStack.pop();
Num x2436 = x2434.i32_sub(x2432);
Stack.push(x2436);
bool x2437 = allConcrete(x2435, x2433);
SymVal x2438 = x2437 ? Concrete(x2436, 32) : x2435.minus(x2433);
SymStack.push(x2438);
}
{
Num x2439 = Stack.pop();
SymVal x2440 = SymStack.pop();
Frames.set(3, x2439);
SymFrames.set(3, x2440);
}
__attribute__((musttail)) return x2390(std::monostate{});
return std::monostate{};
}
std::monostate x2390(std::monostate x2409) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x2410 = Stack.pop();
SymStack.pop();
Num x2411 = I32V(Memory.loadInt(x2410.toInt(), 4));
SymVal x2412 = SymMemory.loadSym(x2410.toInt(), 4);
Stack.push(x2411);
SymStack.push(x2412);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2413 = Stack.pop();
SymVal x2414 = SymStack.pop();
Num x2415 = Stack.pop();
SymVal x2416 = SymStack.pop();
Num x2417 = x2415.i32_sub(x2413);
Stack.push(x2417);
bool x2418 = allConcrete(x2416, x2414);
SymVal x2419 = x2418 ? Concrete(x2417, 32) : x2416.minus(x2414);
SymStack.push(x2419);
}
{
Num x2420 = Stack.pop();
SymVal x2421 = SymStack.pop();
Num x2422 = Stack.pop();
SymVal x2423 = SymStack.pop();
Num x2424 = x2422.i32_eq(x2420);
Stack.push(x2424);
bool x2425 = allConcrete(x2423, x2421);
SymVal x2426 = x2425 ? Concrete(x2424, 32) : x2423.eq(x2421).bool2bv();
SymStack.push(x2426);
}
Num x2427 = Stack.pop();
{
SymVal x2428 = SymStack.pop();
ExploreTree.fillIfElseNode(x2428, 39);
}
int x2429 = x2427.toInt();
if (x2429 != 0) {
ExploreTree.moveCursor(true, makeControl(x2337, CURRENT_MCONT));
__attribute__((musttail)) return x2407(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x2407, CURRENT_MCONT));
__attribute__((musttail)) return x2337(std::monostate{});
}
return std::monostate{};
}
std::monostate x2407(std::monostate x2408) {
info("Entering the true branch 39 of the if");
info("Jump to 2");
__attribute__((musttail)) return x2391(std::monostate{});
return std::monostate{};
}
std::monostate x2391(std::monostate x2392) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x2393 = Stack.pop();
SymStack.pop();
Num x2394 = I32V(Memory.loadInt(x2393.toInt(), 4));
SymVal x2395 = SymMemory.loadSym(x2393.toInt(), 4);
Stack.push(x2394);
SymStack.push(x2395);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2396 = Stack.pop();
SymVal x2397 = SymStack.pop();
Num x2398 = Stack.pop();
SymVal x2399 = SymStack.pop();
Num x2400 = x2398.i32_sub(x2396);
Stack.push(x2400);
bool x2401 = allConcrete(x2399, x2397);
SymVal x2402 = x2401 ? Concrete(x2400, 32) : x2399.minus(x2397);
SymStack.push(x2402);
}
{
Num x2403 = Stack.pop();
SymVal x2404 = SymStack.pop();
Num x2405 = Stack.pop();
SymStack.pop();
int x2406 = x2405.toInt();
Memory.storeInt(x2406, 4, x2403.toInt());
SymMemory.storeSym(x2406, 4, x2404);
}
__attribute__((musttail)) return x2071(std::monostate{});
return std::monostate{};
}
std::monostate x2337(std::monostate x2338) {
info("Entering the false branch 39 of the if");
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x2339 = Stack.pop();
SymVal x2340 = SymStack.pop();
Num x2341 = Stack.pop();
SymVal x2342 = SymStack.pop();
Num x2343 = x2341.i32_mul(x2339);
Stack.push(x2343);
bool x2344 = allConcrete(x2342, x2340);
SymVal x2345 = x2344 ? Concrete(x2343, 32) : x2342.mul(x2340);
SymStack.push(x2345);
}
{
Num x2346 = Stack.pop();
SymVal x2347 = SymStack.pop();
Num x2348 = Stack.pop();
SymVal x2349 = SymStack.pop();
Num x2350 = x2348.i32_add(x2346);
Stack.push(x2350);
bool x2351 = allConcrete(x2349, x2347);
SymVal x2352 = x2351 ? Concrete(x2350, 32) : x2349.add(x2347);
SymStack.push(x2352);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2353 = Stack.pop();
SymVal x2354 = SymStack.pop();
Num x2355 = Stack.pop();
SymVal x2356 = SymStack.pop();
Num x2357 = x2355.i32_add(x2353);
Stack.push(x2357);
bool x2358 = allConcrete(x2356, x2354);
SymVal x2359 = x2358 ? Concrete(x2357, 32) : x2356.add(x2354);
SymStack.push(x2359);
}
{
Num x2360 = Stack.pop();
SymVal x2361 = SymStack.pop();
Num x2362 = Stack.pop();
SymVal x2363 = SymStack.pop();
Num x2364 = x2362.i32_mul(x2360);
Stack.push(x2364);
bool x2365 = allConcrete(x2363, x2361);
SymVal x2366 = x2365 ? Concrete(x2364, 32) : x2363.mul(x2361);
SymStack.push(x2366);
}
{
Num x2367 = Stack.pop();
SymVal x2368 = SymStack.pop();
Num x2369 = Stack.pop();
SymVal x2370 = SymStack.pop();
Num x2371 = x2369.i32_add(x2367);
Stack.push(x2371);
bool x2372 = allConcrete(x2370, x2368);
SymVal x2373 = x2372 ? Concrete(x2371, 32) : x2370.add(x2368);
SymStack.push(x2373);
}
{
Num x2374 = Stack.pop();
SymStack.pop();
Num x2375 = I32V(Memory.loadInt(x2374.toInt(), 8));
SymVal x2376 = SymMemory.loadSym(x2374.toInt(), 8);
Stack.push(x2375);
SymStack.push(x2376);
}
{
Num x2377 = Stack.pop();
SymVal x2378 = SymStack.pop();
Num x2379 = Stack.pop();
SymStack.pop();
int x2380 = x2379.toInt();
Memory.storeInt(x2380, 8, x2377.toInt());
SymMemory.storeSym(x2380, 8, x2378);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2381 = Stack.pop();
SymVal x2382 = SymStack.pop();
Num x2383 = Stack.pop();
SymVal x2384 = SymStack.pop();
Num x2385 = x2383.i32_add(x2381);
Stack.push(x2385);
bool x2386 = allConcrete(x2384, x2382);
SymVal x2387 = x2386 ? Concrete(x2385, 32) : x2384.add(x2382);
SymStack.push(x2387);
}
{
Num x2388 = Stack.pop();
SymVal x2389 = SymStack.pop();
Frames.set(3, x2388);
SymFrames.set(3, x2389);
}
info("Jump to 1");
__attribute__((musttail)) return x2390(std::monostate{});
return std::monostate{};
}
std::monostate x2235(std::monostate x2236) {
info("Entering the false branch 38 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x2237 = Stack.pop();
SymStack.pop();
Num x2238 = I32V(Memory.loadInt(x2237.toInt(), 0));
SymVal x2239 = SymMemory.loadSym(x2237.toInt(), 0);
Stack.push(x2238);
SymStack.push(x2239);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2240 = Stack.pop();
SymVal x2241 = SymStack.pop();
Num x2242 = Stack.pop();
SymVal x2243 = SymStack.pop();
Num x2244 = x2242.i32_sub(x2240);
Stack.push(x2244);
bool x2245 = allConcrete(x2243, x2241);
SymVal x2246 = x2245 ? Concrete(x2244, 32) : x2243.minus(x2241);
SymStack.push(x2246);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2247 = Stack.pop();
SymVal x2248 = SymStack.pop();
Num x2249 = Stack.pop();
SymVal x2250 = SymStack.pop();
Num x2251 = x2249.i32_mul(x2247);
Stack.push(x2251);
bool x2252 = allConcrete(x2250, x2248);
SymVal x2253 = x2252 ? Concrete(x2251, 32) : x2250.mul(x2248);
SymStack.push(x2253);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2254 = Stack.pop();
SymVal x2255 = SymStack.pop();
Num x2256 = Stack.pop();
SymVal x2257 = SymStack.pop();
Num x2258 = x2256.i32_mul(x2254);
Stack.push(x2258);
bool x2259 = allConcrete(x2257, x2255);
SymVal x2260 = x2259 ? Concrete(x2258, 32) : x2257.mul(x2255);
SymStack.push(x2260);
}
{
Num x2261 = Stack.pop();
SymVal x2262 = SymStack.pop();
Num x2263 = Stack.pop();
SymVal x2264 = SymStack.pop();
Num x2265 = x2263.i32_add(x2261);
Stack.push(x2265);
bool x2266 = allConcrete(x2264, x2262);
SymVal x2267 = x2266 ? Concrete(x2265, 32) : x2264.add(x2262);
SymStack.push(x2267);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x2268 = Stack.pop();
SymVal x2269 = SymStack.pop();
Num x2270 = Stack.pop();
SymVal x2271 = SymStack.pop();
Num x2272 = x2270.i32_add(x2268);
Stack.push(x2272);
bool x2273 = allConcrete(x2271, x2269);
SymVal x2274 = x2273 ? Concrete(x2272, 32) : x2271.add(x2269);
SymStack.push(x2274);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x2275 = Stack.pop();
SymStack.pop();
Num x2276 = I32V(Memory.loadInt(x2275.toInt(), 0));
SymVal x2277 = SymMemory.loadSym(x2275.toInt(), 0);
Stack.push(x2276);
SymStack.push(x2277);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2278 = Stack.pop();
SymVal x2279 = SymStack.pop();
Num x2280 = Stack.pop();
SymVal x2281 = SymStack.pop();
Num x2282 = x2280.i32_sub(x2278);
Stack.push(x2282);
bool x2283 = allConcrete(x2281, x2279);
SymVal x2284 = x2283 ? Concrete(x2282, 32) : x2281.minus(x2279);
SymStack.push(x2284);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2285 = Stack.pop();
SymVal x2286 = SymStack.pop();
Num x2287 = Stack.pop();
SymVal x2288 = SymStack.pop();
Num x2289 = x2287.i32_mul(x2285);
Stack.push(x2289);
bool x2290 = allConcrete(x2288, x2286);
SymVal x2291 = x2290 ? Concrete(x2289, 32) : x2288.mul(x2286);
SymStack.push(x2291);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2292 = Stack.pop();
SymVal x2293 = SymStack.pop();
Num x2294 = Stack.pop();
SymVal x2295 = SymStack.pop();
Num x2296 = x2294.i32_add(x2292);
Stack.push(x2296);
bool x2297 = allConcrete(x2295, x2293);
SymVal x2298 = x2297 ? Concrete(x2296, 32) : x2295.add(x2293);
SymStack.push(x2298);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2299 = Stack.pop();
SymVal x2300 = SymStack.pop();
Num x2301 = Stack.pop();
SymVal x2302 = SymStack.pop();
Num x2303 = x2301.i32_mul(x2299);
Stack.push(x2303);
bool x2304 = allConcrete(x2302, x2300);
SymVal x2305 = x2304 ? Concrete(x2303, 32) : x2302.mul(x2300);
SymStack.push(x2305);
}
{
Num x2306 = Stack.pop();
SymVal x2307 = SymStack.pop();
Num x2308 = Stack.pop();
SymVal x2309 = SymStack.pop();
Num x2310 = x2308.i32_add(x2306);
Stack.push(x2310);
bool x2311 = allConcrete(x2309, x2307);
SymVal x2312 = x2311 ? Concrete(x2310, 32) : x2309.add(x2307);
SymStack.push(x2312);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x2313 = Stack.pop();
SymVal x2314 = SymStack.pop();
Num x2315 = Stack.pop();
SymVal x2316 = SymStack.pop();
Num x2317 = x2315.i32_add(x2313);
Stack.push(x2317);
bool x2318 = allConcrete(x2316, x2314);
SymVal x2319 = x2318 ? Concrete(x2317, 32) : x2316.add(x2314);
SymStack.push(x2319);
}
{
Num x2320 = Stack.pop();
SymStack.pop();
Num x2321 = I32V(Memory.loadInt(x2320.toInt(), 8));
SymVal x2322 = SymMemory.loadSym(x2320.toInt(), 8);
Stack.push(x2321);
SymStack.push(x2322);
}
{
Num x2323 = Stack.pop();
SymVal x2324 = SymStack.pop();
Num x2325 = Stack.pop();
SymStack.pop();
int x2326 = x2325.toInt();
Memory.storeInt(x2326, 8, x2323.toInt());
SymMemory.storeSym(x2326, 8, x2324);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2327 = Stack.pop();
SymVal x2328 = SymStack.pop();
Num x2329 = Stack.pop();
SymVal x2330 = SymStack.pop();
Num x2331 = x2329.i32_add(x2327);
Stack.push(x2331);
bool x2332 = allConcrete(x2330, x2328);
SymVal x2333 = x2332 ? Concrete(x2331, 32) : x2330.add(x2328);
SymStack.push(x2333);
}
{
Num x2334 = Stack.pop();
SymVal x2335 = SymStack.pop();
Frames.set(3, x2334);
SymFrames.set(3, x2335);
}
info("Jump to 1");
__attribute__((musttail)) return x2336(std::monostate{});
return std::monostate{};
}
std::monostate x2075(std::monostate x2076) {
info("Entering the false branch 36 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x2077 = Stack.pop();
SymStack.pop();
Num x2078 = I32V(Memory.loadInt(x2077.toInt(), 0));
SymVal x2079 = SymMemory.loadSym(x2077.toInt(), 0);
Stack.push(x2078);
SymStack.push(x2079);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2080 = Stack.pop();
SymVal x2081 = SymStack.pop();
Num x2082 = Stack.pop();
SymVal x2083 = SymStack.pop();
Num x2084 = x2082.i32_sub(x2080);
Stack.push(x2084);
bool x2085 = allConcrete(x2083, x2081);
SymVal x2086 = x2085 ? Concrete(x2084, 32) : x2083.minus(x2081);
SymStack.push(x2086);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2087 = Stack.pop();
SymVal x2088 = SymStack.pop();
Num x2089 = Stack.pop();
SymVal x2090 = SymStack.pop();
Num x2091 = x2089.i32_mul(x2087);
Stack.push(x2091);
bool x2092 = allConcrete(x2090, x2088);
SymVal x2093 = x2092 ? Concrete(x2091, 32) : x2090.mul(x2088);
SymStack.push(x2093);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2094 = Stack.pop();
SymVal x2095 = SymStack.pop();
Num x2096 = Stack.pop();
SymVal x2097 = SymStack.pop();
Num x2098 = x2096.i32_sub(x2094);
Stack.push(x2098);
bool x2099 = allConcrete(x2097, x2095);
SymVal x2100 = x2099 ? Concrete(x2098, 32) : x2097.minus(x2095);
SymStack.push(x2100);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2101 = Stack.pop();
SymVal x2102 = SymStack.pop();
Num x2103 = Stack.pop();
SymVal x2104 = SymStack.pop();
Num x2105 = x2103.i32_mul(x2101);
Stack.push(x2105);
bool x2106 = allConcrete(x2104, x2102);
SymVal x2107 = x2106 ? Concrete(x2105, 32) : x2104.mul(x2102);
SymStack.push(x2107);
}
{
Num x2108 = Stack.pop();
SymVal x2109 = SymStack.pop();
Num x2110 = Stack.pop();
SymVal x2111 = SymStack.pop();
Num x2112 = x2110.i32_add(x2108);
Stack.push(x2112);
bool x2113 = allConcrete(x2111, x2109);
SymVal x2114 = x2113 ? Concrete(x2112, 32) : x2111.add(x2109);
SymStack.push(x2114);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x2115 = Stack.pop();
SymVal x2116 = SymStack.pop();
Num x2117 = Stack.pop();
SymVal x2118 = SymStack.pop();
Num x2119 = x2117.i32_add(x2115);
Stack.push(x2119);
bool x2120 = allConcrete(x2118, x2116);
SymVal x2121 = x2120 ? Concrete(x2119, 32) : x2118.add(x2116);
SymStack.push(x2121);
}
{
Num x2122 = Stack.pop();
SymStack.pop();
Num x2123 = I32V(Memory.loadInt(x2122.toInt(), 8));
SymVal x2124 = SymMemory.loadSym(x2122.toInt(), 8);
Stack.push(x2123);
SymStack.push(x2124);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x2125 = Stack.pop();
SymStack.pop();
Num x2126 = I32V(Memory.loadInt(x2125.toInt(), 0));
SymVal x2127 = SymMemory.loadSym(x2125.toInt(), 0);
Stack.push(x2126);
SymStack.push(x2127);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2128 = Stack.pop();
SymVal x2129 = SymStack.pop();
Num x2130 = Stack.pop();
SymVal x2131 = SymStack.pop();
Num x2132 = x2130.i32_sub(x2128);
Stack.push(x2132);
bool x2133 = allConcrete(x2131, x2129);
SymVal x2134 = x2133 ? Concrete(x2132, 32) : x2131.minus(x2129);
SymStack.push(x2134);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2135 = Stack.pop();
SymVal x2136 = SymStack.pop();
Num x2137 = Stack.pop();
SymVal x2138 = SymStack.pop();
Num x2139 = x2137.i32_mul(x2135);
Stack.push(x2139);
bool x2140 = allConcrete(x2138, x2136);
SymVal x2141 = x2140 ? Concrete(x2139, 32) : x2138.mul(x2136);
SymStack.push(x2141);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2142 = Stack.pop();
SymVal x2143 = SymStack.pop();
Num x2144 = Stack.pop();
SymVal x2145 = SymStack.pop();
Num x2146 = x2144.i32_sub(x2142);
Stack.push(x2146);
bool x2147 = allConcrete(x2145, x2143);
SymVal x2148 = x2147 ? Concrete(x2146, 32) : x2145.minus(x2143);
SymStack.push(x2148);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2149 = Stack.pop();
SymVal x2150 = SymStack.pop();
Num x2151 = Stack.pop();
SymVal x2152 = SymStack.pop();
Num x2153 = x2151.i32_mul(x2149);
Stack.push(x2153);
bool x2154 = allConcrete(x2152, x2150);
SymVal x2155 = x2154 ? Concrete(x2153, 32) : x2152.mul(x2150);
SymStack.push(x2155);
}
{
Num x2156 = Stack.pop();
SymVal x2157 = SymStack.pop();
Num x2158 = Stack.pop();
SymVal x2159 = SymStack.pop();
Num x2160 = x2158.i32_add(x2156);
Stack.push(x2160);
bool x2161 = allConcrete(x2159, x2157);
SymVal x2162 = x2161 ? Concrete(x2160, 32) : x2159.add(x2157);
SymStack.push(x2162);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x2163 = Stack.pop();
SymVal x2164 = SymStack.pop();
Num x2165 = Stack.pop();
SymVal x2166 = SymStack.pop();
Num x2167 = x2165.i32_add(x2163);
Stack.push(x2167);
bool x2168 = allConcrete(x2166, x2164);
SymVal x2169 = x2168 ? Concrete(x2167, 32) : x2166.add(x2164);
SymStack.push(x2169);
}
{
Num x2170 = Stack.pop();
SymStack.pop();
Num x2171 = I32V(Memory.loadInt(x2170.toInt(), 8));
SymVal x2172 = SymMemory.loadSym(x2170.toInt(), 8);
Stack.push(x2171);
SymStack.push(x2172);
}
{
Num x2173 = Stack.pop();
SymStack.pop();
Num x2174 = I32V(Memory.loadInt(x2173.toInt(), 4));
SymVal x2175 = SymMemory.loadSym(x2173.toInt(), 4);
Stack.push(x2174);
SymStack.push(x2175);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x2176 = Stack.pop();
SymVal x2177 = SymStack.pop();
Num x2178 = Stack.pop();
SymVal x2179 = SymStack.pop();
Num x2180 = x2178.i32_add(x2176);
Stack.push(x2180);
bool x2181 = allConcrete(x2179, x2177);
SymVal x2182 = x2181 ? Concrete(x2180, 32) : x2179.add(x2177);
SymStack.push(x2182);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2183 = Stack.pop();
SymVal x2184 = SymStack.pop();
Num x2185 = Stack.pop();
SymVal x2186 = SymStack.pop();
Num x2187 = x2185.i32_add(x2183);
Stack.push(x2187);
bool x2188 = allConcrete(x2186, x2184);
SymVal x2189 = x2188 ? Concrete(x2187, 32) : x2186.add(x2184);
SymStack.push(x2189);
}
{
Num x2190 = Stack.pop();
SymVal x2191 = SymStack.pop();
Num x2192 = Stack.pop();
SymVal x2193 = SymStack.pop();
Num x2194 = x2192.i32_mul(x2190);
Stack.push(x2194);
bool x2195 = allConcrete(x2193, x2191);
SymVal x2196 = x2195 ? Concrete(x2194, 32) : x2193.mul(x2191);
SymStack.push(x2196);
}
{
Num x2197 = Stack.pop();
SymVal x2198 = SymStack.pop();
Num x2199 = Stack.pop();
SymVal x2200 = SymStack.pop();
Num x2201 = x2199.i32_add(x2197);
Stack.push(x2201);
bool x2202 = allConcrete(x2200, x2198);
SymVal x2203 = x2202 ? Concrete(x2201, 32) : x2200.add(x2198);
SymStack.push(x2203);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x2204 = Stack.pop();
SymVal x2205 = SymStack.pop();
Num x2206 = Stack.pop();
SymVal x2207 = SymStack.pop();
Num x2208 = x2206.i32_mul(x2204);
Stack.push(x2208);
bool x2209 = allConcrete(x2207, x2205);
SymVal x2210 = x2209 ? Concrete(x2208, 32) : x2207.mul(x2205);
SymStack.push(x2210);
}
{
Num x2211 = Stack.pop();
SymVal x2212 = SymStack.pop();
Num x2213 = Stack.pop();
SymVal x2214 = SymStack.pop();
Num x2215 = x2213.i32_add(x2211);
Stack.push(x2215);
bool x2216 = allConcrete(x2214, x2212);
SymVal x2217 = x2216 ? Concrete(x2215, 32) : x2214.add(x2212);
SymStack.push(x2217);
}
{
Num x2218 = Stack.pop();
SymStack.pop();
Num x2219 = I32V(Memory.loadInt(x2218.toInt(), 8));
SymVal x2220 = SymMemory.loadSym(x2218.toInt(), 8);
Stack.push(x2219);
SymStack.push(x2220);
}
{
Num x2221 = Stack.pop();
SymVal x2222 = SymStack.pop();
Num x2223 = Stack.pop();
SymStack.pop();
int x2224 = x2223.toInt();
Memory.storeInt(x2224, 8, x2221.toInt());
SymMemory.storeSym(x2224, 8, x2222);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2225 = Stack.pop();
SymVal x2226 = SymStack.pop();
Num x2227 = Stack.pop();
SymVal x2228 = SymStack.pop();
Num x2229 = x2227.i32_add(x2225);
Stack.push(x2229);
bool x2230 = allConcrete(x2228, x2226);
SymVal x2231 = x2230 ? Concrete(x2229, 32) : x2228.add(x2226);
SymStack.push(x2231);
}
{
Num x2232 = Stack.pop();
SymVal x2233 = SymStack.pop();
Frames.set(3, x2232);
SymFrames.set(3, x2233);
}
info("Jump to 1");
__attribute__((musttail)) return x2234(std::monostate{});
return std::monostate{};
}
std::monostate x2073(std::monostate x2074) {
info("Entering the false branch 35 of the if");
__attribute__((musttail)) return x2071(std::monostate{});
return std::monostate{};
}
std::monostate x2071(std::monostate x2072) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x2067(std::monostate{});
return std::monostate{};
}
std::monostate x2069(std::monostate x2070) {
info("Entering the false branch 33 of the if");
__attribute__((musttail)) return x2067(std::monostate{});
return std::monostate{};
}
std::monostate x2067(std::monostate x2068) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x2053(std::monostate{});
return std::monostate{};
}
std::monostate x2065(std::monostate x2066) {
info("Entering the false branch 28 of the if");
__attribute__((musttail)) return x2053(std::monostate{});
return std::monostate{};
}
std::monostate x2053(std::monostate x2054) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(Frames.get(4));
SymStack.push(SymFrames.get(4));
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x2055 = Stack.pop();
SymVal x2056 = SymStack.pop();
Num x2057 = Stack.pop();
SymVal x2058 = SymStack.pop();
Num x2059 = x2057.i32_eq(x2055);
Stack.push(x2059);
bool x2060 = allConcrete(x2058, x2056);
SymVal x2061 = x2060 ? Concrete(x2059, 32) : x2058.eq(x2056).bool2bv();
SymStack.push(x2061);
}
Num x2062 = Stack.pop();
{
SymVal x2063 = SymStack.pop();
ExploreTree.fillIfElseNode(x2063, 29);
}
int x2064 = x2062.toInt();
if (x2064 != 0) {
ExploreTree.moveCursor(true, makeControl(x1990, CURRENT_MCONT));
__attribute__((musttail)) return x1999(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1999, CURRENT_MCONT));
__attribute__((musttail)) return x1990(std::monostate{});
}
return std::monostate{};
}
std::monostate x1999(std::monostate x2000) {
info("Entering the true branch 29 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x2001 = Stack.pop();
SymStack.pop();
Num x2002 = I32V(Memory.loadInt(x2001.toInt(), 0));
SymVal x2003 = SymMemory.loadSym(x2001.toInt(), 0);
Stack.push(x2002);
SymStack.push(x2003);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2004 = Stack.pop();
SymVal x2005 = SymStack.pop();
Num x2006 = Stack.pop();
SymVal x2007 = SymStack.pop();
Num x2008 = x2006.i32_sub(x2004);
Stack.push(x2008);
bool x2009 = allConcrete(x2007, x2005);
SymVal x2010 = x2009 ? Concrete(x2008, 32) : x2007.minus(x2005);
SymStack.push(x2010);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2011 = Stack.pop();
SymVal x2012 = SymStack.pop();
Num x2013 = Stack.pop();
SymVal x2014 = SymStack.pop();
Num x2015 = x2013.i32_mul(x2011);
Stack.push(x2015);
bool x2016 = allConcrete(x2014, x2012);
SymVal x2017 = x2016 ? Concrete(x2015, 32) : x2014.mul(x2012);
SymStack.push(x2017);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2018 = Stack.pop();
SymVal x2019 = SymStack.pop();
Num x2020 = Stack.pop();
SymVal x2021 = SymStack.pop();
Num x2022 = x2020.i32_sub(x2018);
Stack.push(x2022);
bool x2023 = allConcrete(x2021, x2019);
SymVal x2024 = x2023 ? Concrete(x2022, 32) : x2021.minus(x2019);
SymStack.push(x2024);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2025 = Stack.pop();
SymVal x2026 = SymStack.pop();
Num x2027 = Stack.pop();
SymVal x2028 = SymStack.pop();
Num x2029 = x2027.i32_mul(x2025);
Stack.push(x2029);
bool x2030 = allConcrete(x2028, x2026);
SymVal x2031 = x2030 ? Concrete(x2029, 32) : x2028.mul(x2026);
SymStack.push(x2031);
}
{
Num x2032 = Stack.pop();
SymVal x2033 = SymStack.pop();
Num x2034 = Stack.pop();
SymVal x2035 = SymStack.pop();
Num x2036 = x2034.i32_add(x2032);
Stack.push(x2036);
bool x2037 = allConcrete(x2035, x2033);
SymVal x2038 = x2037 ? Concrete(x2036, 32) : x2035.add(x2033);
SymStack.push(x2038);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x2039 = Stack.pop();
SymVal x2040 = SymStack.pop();
Num x2041 = Stack.pop();
SymVal x2042 = SymStack.pop();
Num x2043 = x2041.i32_add(x2039);
Stack.push(x2043);
bool x2044 = allConcrete(x2042, x2040);
SymVal x2045 = x2044 ? Concrete(x2043, 32) : x2042.add(x2040);
SymStack.push(x2045);
}
{
Num x2046 = Stack.pop();
SymStack.pop();
Num x2047 = I32V(Memory.loadInt(x2046.toInt(), 8));
SymVal x2048 = SymMemory.loadSym(x2046.toInt(), 8);
Stack.push(x2047);
SymStack.push(x2048);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 12);
Num x2049 = Stack.pop();
Num x2050 = Stack.pop();
SymVal x2051 = SymStack.pop();
SymVal x2052 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x2050);
Frames.set(1, x2049);
SymFrames.set(0, x2052);
SymFrames.set(1, x2051);
updateCurrentMCont(prependCont(x1997, CURRENT_MCONT));
}
__attribute__((musttail)) return x1996(std::monostate{});
return std::monostate{};
}
std::monostate x1997(std::monostate x1998) {
infoWhen("CALL", "Returning from the function at 12, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.pop();
SymStack.pop();
__attribute__((musttail)) return x1986(std::monostate{});
return std::monostate{};
}
std::monostate x1990(std::monostate x1991) {
info("Entering the false branch 29 of the if");
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 12);
Num x1992 = Stack.pop();
Num x1993 = Stack.pop();
SymVal x1994 = SymStack.pop();
SymVal x1995 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x1993);
Frames.set(1, x1992);
SymFrames.set(0, x1995);
SymFrames.set(1, x1994);
updateCurrentMCont(prependCont(x1988, CURRENT_MCONT));
}
__attribute__((musttail)) return x1996(std::monostate{});
return std::monostate{};
}
std::monostate x1988(std::monostate x1989) {
infoWhen("CALL", "Returning from the function at 12, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.pop();
SymStack.pop();
__attribute__((musttail)) return x1986(std::monostate{});
return std::monostate{};
}
std::monostate x1986(std::monostate x1987) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x1984(std::monostate{});
return std::monostate{};
}
std::monostate x1984(std::monostate x1985) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x1966(std::monostate{});
return std::monostate{};
}
std::monostate x1966(std::monostate x1967) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1968 = Stack.pop();
SymStack.pop();
Num x1969 = I32V(Memory.loadInt(x1968.toInt(), 8));
SymVal x1970 = SymMemory.loadSym(x1968.toInt(), 8);
Stack.push(x1969);
SymStack.push(x1970);
}
{
Num x1971 = Stack.pop();
SymStack.pop();
Num x1972 = I32V(Memory.loadInt(x1971.toInt(), 4));
SymVal x1973 = SymMemory.loadSym(x1971.toInt(), 4);
Stack.push(x1972);
SymStack.push(x1973);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1974 = Stack.pop();
SymVal x1975 = SymStack.pop();
Num x1976 = Stack.pop();
SymVal x1977 = SymStack.pop();
Num x1978 = x1976.i32_eq(x1974);
Stack.push(x1978);
bool x1979 = allConcrete(x1977, x1975);
SymVal x1980 = x1979 ? Concrete(x1978, 32) : x1977.eq(x1975).bool2bv();
SymStack.push(x1980);
}
Num x1981 = Stack.pop();
{
SymVal x1982 = SymStack.pop();
ExploreTree.fillIfElseNode(x1982, 30);
}
int x1983 = x1981.toInt();
if (x1983 != 0) {
ExploreTree.moveCursor(true, makeControl(x1914, CURRENT_MCONT));
__attribute__((musttail)) return x1916(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1916, CURRENT_MCONT));
__attribute__((musttail)) return x1914(std::monostate{});
}
return std::monostate{};
}
std::monostate x1916(std::monostate x1917) {
info("Entering the true branch 30 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1918 = Stack.pop();
SymStack.pop();
Num x1919 = I32V(Memory.loadInt(x1918.toInt(), 0));
SymVal x1920 = SymMemory.loadSym(x1918.toInt(), 0);
Stack.push(x1919);
SymStack.push(x1920);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1921 = Stack.pop();
SymVal x1922 = SymStack.pop();
Num x1923 = Stack.pop();
SymVal x1924 = SymStack.pop();
Num x1925 = x1923.i32_sub(x1921);
Stack.push(x1925);
bool x1926 = allConcrete(x1924, x1922);
SymVal x1927 = x1926 ? Concrete(x1925, 32) : x1924.minus(x1922);
SymStack.push(x1927);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x1928 = Stack.pop();
SymVal x1929 = SymStack.pop();
Num x1930 = Stack.pop();
SymVal x1931 = SymStack.pop();
Num x1932 = x1930.i32_mul(x1928);
Stack.push(x1932);
bool x1933 = allConcrete(x1931, x1929);
SymVal x1934 = x1933 ? Concrete(x1932, 32) : x1931.mul(x1929);
SymStack.push(x1934);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x1935 = Stack.pop();
SymVal x1936 = SymStack.pop();
Num x1937 = Stack.pop();
SymVal x1938 = SymStack.pop();
Num x1939 = x1937.i32_mul(x1935);
Stack.push(x1939);
bool x1940 = allConcrete(x1938, x1936);
SymVal x1941 = x1940 ? Concrete(x1939, 32) : x1938.mul(x1936);
SymStack.push(x1941);
}
{
Num x1942 = Stack.pop();
SymVal x1943 = SymStack.pop();
Num x1944 = Stack.pop();
SymVal x1945 = SymStack.pop();
Num x1946 = x1944.i32_add(x1942);
Stack.push(x1946);
bool x1947 = allConcrete(x1945, x1943);
SymVal x1948 = x1947 ? Concrete(x1946, 32) : x1945.add(x1943);
SymStack.push(x1948);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1949 = Stack.pop();
SymStack.pop();
Num x1950 = I32V(Memory.loadInt(x1949.toInt(), 8));
SymVal x1951 = SymMemory.loadSym(x1949.toInt(), 8);
Stack.push(x1950);
SymStack.push(x1951);
}
{
Num x1952 = Stack.pop();
SymVal x1953 = SymStack.pop();
Num x1954 = Stack.pop();
SymVal x1955 = SymStack.pop();
Num x1956 = x1954.i32_add(x1952);
Stack.push(x1956);
bool x1957 = allConcrete(x1955, x1953);
SymVal x1958 = x1957 ? Concrete(x1956, 32) : x1955.add(x1953);
SymStack.push(x1958);
}
{
Num x1959 = Stack.pop();
SymStack.pop();
Num x1960 = I32V(Memory.loadInt(x1959.toInt(), 8));
SymVal x1961 = SymMemory.loadSym(x1959.toInt(), 8);
Stack.push(x1960);
SymStack.push(x1961);
}
{
Num x1962 = Stack.pop();
SymVal x1963 = SymStack.pop();
Num x1964 = Stack.pop();
SymStack.pop();
int x1965 = x1964.toInt();
Memory.storeInt(x1965, 8, x1962.toInt());
SymMemory.storeSym(x1965, 8, x1963);
}
__attribute__((musttail)) return x1909(std::monostate{});
return std::monostate{};
}
std::monostate x1914(std::monostate x1915) {
info("Entering the false branch 30 of the if");
__attribute__((musttail)) return x1909(std::monostate{});
return std::monostate{};
}
std::monostate x1909(std::monostate x1910) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1911 = Stack.pop();
SymStack.pop();
Num x1912 = I32V(Memory.loadInt(x1911.toInt(), 8));
SymVal x1913 = SymMemory.loadSym(x1911.toInt(), 8);
Stack.push(x1912);
SymStack.push(x1913);
}
__attribute__((musttail)) return x1907(std::monostate{});
return std::monostate{};
}
std::monostate x1907(std::monostate x1908) {
infoWhen("CALL", "Exiting the function at 12, stackSize =", Stack.size());
Frames.popFrameCallee(4);
SymFrames.popFrameCallee(4);
return enterCC(std::monostate());
}
std::monostate x1666(std::monostate x1904) {
infoWhen("CALL", "Entered the function at 8, stackSize =", Stack.size());
Frames.pushFrameCallee(1);
SymFrames.pushFrameSlot(32);
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1905 = Stack.pop();
SymVal x1906 = SymStack.pop();
Frames.set(2, x1905);
SymFrames.set(2, x1906);
}
__attribute__((musttail)) return x1853(std::monostate{});
return std::monostate{};
}
std::monostate x1853(std::monostate x1883) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x1884 = Stack.pop();
SymStack.pop();
Num x1885 = I32V(Memory.loadInt(x1884.toInt(), 4));
SymVal x1886 = SymMemory.loadSym(x1884.toInt(), 4);
Stack.push(x1885);
SymStack.push(x1886);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1887 = Stack.pop();
SymVal x1888 = SymStack.pop();
Num x1889 = Stack.pop();
SymVal x1890 = SymStack.pop();
Num x1891 = x1889.i32_sub(x1887);
Stack.push(x1891);
bool x1892 = allConcrete(x1890, x1888);
SymVal x1893 = x1892 ? Concrete(x1891, 32) : x1890.minus(x1888);
SymStack.push(x1893);
}
{
Num x1894 = Stack.pop();
SymVal x1895 = SymStack.pop();
Num x1896 = Stack.pop();
SymVal x1897 = SymStack.pop();
Num x1898 = x1896.i32_le_s(x1894);
Stack.push(x1898);
bool x1899 = allConcrete(x1897, x1895);
SymVal x1900 = x1899 ? Concrete(x1898, 32) : x1897.le(x1895).bool2bv();
SymStack.push(x1900);
}
Num x1901 = Stack.pop();
{
SymVal x1902 = SymStack.pop();
ExploreTree.fillIfElseNode(x1902, 18);
}
int x1903 = x1901.toInt();
if (x1903 != 0) {
ExploreTree.moveCursor(true, makeControl(x1838, CURRENT_MCONT));
__attribute__((musttail)) return x1854(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1854, CURRENT_MCONT));
__attribute__((musttail)) return x1838(std::monostate{});
}
return std::monostate{};
}
std::monostate x1854(std::monostate x1855) {
info("Entering the true branch 18 of the if");
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x1856 = Stack.pop();
SymVal x1857 = SymStack.pop();
Num x1858 = Stack.pop();
SymVal x1859 = SymStack.pop();
Num x1860 = x1858.i32_mul(x1856);
Stack.push(x1860);
bool x1861 = allConcrete(x1859, x1857);
SymVal x1862 = x1861 ? Concrete(x1860, 32) : x1859.mul(x1857);
SymStack.push(x1862);
}
{
Num x1863 = Stack.pop();
SymVal x1864 = SymStack.pop();
Num x1865 = Stack.pop();
SymVal x1866 = SymStack.pop();
Num x1867 = x1865.i32_add(x1863);
Stack.push(x1867);
bool x1868 = allConcrete(x1866, x1864);
SymVal x1869 = x1868 ? Concrete(x1867, 32) : x1866.add(x1864);
SymStack.push(x1869);
}
{
Num x1870 = Stack.pop();
SymStack.pop();
Num x1871 = I32V(Memory.loadInt(x1870.toInt(), 8));
SymVal x1872 = SymMemory.loadSym(x1870.toInt(), 8);
Stack.push(x1871);
SymStack.push(x1872);
}
{
Num x1873 = Stack.pop();
SymVal x1874 = SymStack.pop();
Num x1875 = Stack.pop();
SymVal x1876 = SymStack.pop();
Num x1877 = x1875.i32_gt_s(x1873);
Stack.push(x1877);
bool x1878 = allConcrete(x1876, x1874);
SymVal x1879 = x1878 ? Concrete(x1877, 32) : x1876.gt(x1874).bool2bv();
SymStack.push(x1879);
}
Num x1880 = Stack.pop();
{
SymVal x1881 = SymStack.pop();
ExploreTree.fillIfElseNode(x1881, 22);
}
int x1882 = x1880.toInt();
if (x1882 != 0) {
ExploreTree.moveCursor(true, makeControl(x1840, CURRENT_MCONT));
__attribute__((musttail)) return x1842(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1842, CURRENT_MCONT));
__attribute__((musttail)) return x1840(std::monostate{});
}
return std::monostate{};
}
std::monostate x1842(std::monostate x1843) {
info("Entering the true branch 22 of the if");
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x1844 = Stack.pop();
SymVal x1845 = SymStack.pop();
Num x1846 = Stack.pop();
SymVal x1847 = SymStack.pop();
Num x1848 = x1846.i32_add(x1844);
Stack.push(x1848);
bool x1849 = allConcrete(x1847, x1845);
SymVal x1850 = x1849 ? Concrete(x1848, 32) : x1847.add(x1845);
SymStack.push(x1850);
}
{
Num x1851 = Stack.pop();
SymVal x1852 = SymStack.pop();
Frames.set(2, x1851);
SymFrames.set(2, x1852);
}
info("Jump to 2");
__attribute__((musttail)) return x1853(std::monostate{});
return std::monostate{};
}
std::monostate x1840(std::monostate x1841) {
info("Entering the false branch 22 of the if");
info("Jump to 3");
__attribute__((musttail)) return x1816(std::monostate{});
return std::monostate{};
}
std::monostate x1838(std::monostate x1839) {
info("Entering the false branch 18 of the if");
info("Jump to 2");
__attribute__((musttail)) return x1816(std::monostate{});
return std::monostate{};
}
std::monostate x1816(std::monostate x1817) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x1818 = Stack.pop();
SymStack.pop();
Num x1819 = I32V(Memory.loadInt(x1818.toInt(), 4));
SymVal x1820 = SymMemory.loadSym(x1818.toInt(), 4);
Stack.push(x1819);
SymStack.push(x1820);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1821 = Stack.pop();
SymVal x1822 = SymStack.pop();
Num x1823 = Stack.pop();
SymVal x1824 = SymStack.pop();
Num x1825 = x1823.i32_sub(x1821);
Stack.push(x1825);
bool x1826 = allConcrete(x1824, x1822);
SymVal x1827 = x1826 ? Concrete(x1825, 32) : x1824.minus(x1822);
SymStack.push(x1827);
}
{
Num x1828 = Stack.pop();
SymVal x1829 = SymStack.pop();
Num x1830 = Stack.pop();
SymVal x1831 = SymStack.pop();
Num x1832 = x1830.i32_le_s(x1828);
Stack.push(x1832);
bool x1833 = allConcrete(x1831, x1829);
SymVal x1834 = x1833 ? Concrete(x1832, 32) : x1831.le(x1829).bool2bv();
SymStack.push(x1834);
}
Num x1835 = Stack.pop();
{
SymVal x1836 = SymStack.pop();
ExploreTree.fillIfElseNode(x1836, 19);
}
int x1837 = x1835.toInt();
if (x1837 != 0) {
ExploreTree.moveCursor(true, makeControl(x1675, CURRENT_MCONT));
__attribute__((musttail)) return x1787(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1787, CURRENT_MCONT));
__attribute__((musttail)) return x1675(std::monostate{});
}
return std::monostate{};
}
std::monostate x1787(std::monostate x1788) {
info("Entering the true branch 19 of the if");
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x1789 = Stack.pop();
SymVal x1790 = SymStack.pop();
Num x1791 = Stack.pop();
SymVal x1792 = SymStack.pop();
Num x1793 = x1791.i32_mul(x1789);
Stack.push(x1793);
bool x1794 = allConcrete(x1792, x1790);
SymVal x1795 = x1794 ? Concrete(x1793, 32) : x1792.mul(x1790);
SymStack.push(x1795);
}
{
Num x1796 = Stack.pop();
SymVal x1797 = SymStack.pop();
Num x1798 = Stack.pop();
SymVal x1799 = SymStack.pop();
Num x1800 = x1798.i32_add(x1796);
Stack.push(x1800);
bool x1801 = allConcrete(x1799, x1797);
SymVal x1802 = x1801 ? Concrete(x1800, 32) : x1799.add(x1797);
SymStack.push(x1802);
}
{
Num x1803 = Stack.pop();
SymStack.pop();
Num x1804 = I32V(Memory.loadInt(x1803.toInt(), 8));
SymVal x1805 = SymMemory.loadSym(x1803.toInt(), 8);
Stack.push(x1804);
SymStack.push(x1805);
}
{
Num x1806 = Stack.pop();
SymVal x1807 = SymStack.pop();
Num x1808 = Stack.pop();
SymVal x1809 = SymStack.pop();
Num x1810 = x1808.i32_eq(x1806);
Stack.push(x1810);
bool x1811 = allConcrete(x1809, x1807);
SymVal x1812 = x1811 ? Concrete(x1810, 32) : x1809.eq(x1807).bool2bv();
SymStack.push(x1812);
}
Num x1813 = Stack.pop();
{
SymVal x1814 = SymStack.pop();
ExploreTree.fillIfElseNode(x1814, 21);
}
int x1815 = x1813.toInt();
if (x1815 != 0) {
ExploreTree.moveCursor(true, makeControl(x1749, CURRENT_MCONT));
__attribute__((musttail)) return x1764(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1764, CURRENT_MCONT));
__attribute__((musttail)) return x1749(std::monostate{});
}
return std::monostate{};
}
std::monostate x1764(std::monostate x1765) {
info("Entering the true branch 21 of the if");
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(8));
SymStack.push(Concrete(I32V(8), 32));
{
Num x1766 = Stack.pop();
SymVal x1767 = SymStack.pop();
Num x1768 = Stack.pop();
SymVal x1769 = SymStack.pop();
Num x1770 = x1768.i32_add(x1766);
Stack.push(x1770);
bool x1771 = allConcrete(x1769, x1767);
SymVal x1772 = x1771 ? Concrete(x1770, 32) : x1769.add(x1767);
SymStack.push(x1772);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x1773 = Stack.pop();
SymVal x1774 = SymStack.pop();
Num x1775 = Stack.pop();
SymVal x1776 = SymStack.pop();
Num x1777 = x1775.i32_mul(x1773);
Stack.push(x1777);
bool x1778 = allConcrete(x1776, x1774);
SymVal x1779 = x1778 ? Concrete(x1777, 32) : x1776.mul(x1774);
SymStack.push(x1779);
}
{
Num x1780 = Stack.pop();
SymVal x1781 = SymStack.pop();
Num x1782 = Stack.pop();
SymVal x1783 = SymStack.pop();
Num x1784 = x1782.i32_add(x1780);
Stack.push(x1784);
bool x1785 = allConcrete(x1783, x1781);
SymVal x1786 = x1785 ? Concrete(x1784, 32) : x1783.add(x1781);
SymStack.push(x1786);
}
__attribute__((musttail)) return x1743(std::monostate{});
return std::monostate{};
}
std::monostate x1749(std::monostate x1750) {
info("Entering the false branch 21 of the if");
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x1751 = Stack.pop();
SymStack.pop();
Num x1752 = I32V(Memory.loadInt(x1751.toInt(), 0));
SymVal x1753 = SymMemory.loadSym(x1751.toInt(), 0);
Stack.push(x1752);
SymStack.push(x1753);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1754 = Stack.pop();
SymVal x1755 = SymStack.pop();
Num x1756 = Stack.pop();
SymVal x1757 = SymStack.pop();
Num x1758 = x1756.i32_eq(x1754);
Stack.push(x1758);
bool x1759 = allConcrete(x1757, x1755);
SymVal x1760 = x1759 ? Concrete(x1758, 32) : x1757.eq(x1755).bool2bv();
SymStack.push(x1760);
}
Num x1761 = Stack.pop();
{
SymVal x1762 = SymStack.pop();
ExploreTree.fillIfElseNode(x1762, 20);
}
int x1763 = x1761.toInt();
if (x1763 != 0) {
ExploreTree.moveCursor(true, makeControl(x1696, CURRENT_MCONT));
__attribute__((musttail)) return x1747(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1747, CURRENT_MCONT));
__attribute__((musttail)) return x1696(std::monostate{});
}
return std::monostate{};
}
std::monostate x1747(std::monostate x1748) {
info("Entering the true branch 20 of the if");
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
__attribute__((musttail)) return x1745(std::monostate{});
return std::monostate{};
}
std::monostate x1745(std::monostate x1746) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x1743(std::monostate{});
return std::monostate{};
}
std::monostate x1743(std::monostate x1744) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x1669(std::monostate{});
return std::monostate{};
}
std::monostate x1696(std::monostate x1697) {
info("Entering the false branch 20 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1698 = Stack.pop();
SymStack.pop();
Num x1699 = I32V(Memory.loadInt(x1698.toInt(), 0));
SymVal x1700 = SymMemory.loadSym(x1698.toInt(), 0);
Stack.push(x1699);
SymStack.push(x1700);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1701 = Stack.pop();
SymVal x1702 = SymStack.pop();
Num x1703 = Stack.pop();
SymVal x1704 = SymStack.pop();
Num x1705 = x1703.i32_sub(x1701);
Stack.push(x1705);
bool x1706 = allConcrete(x1704, x1702);
SymVal x1707 = x1706 ? Concrete(x1705, 32) : x1704.minus(x1702);
SymStack.push(x1707);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x1708 = Stack.pop();
SymVal x1709 = SymStack.pop();
Num x1710 = Stack.pop();
SymVal x1711 = SymStack.pop();
Num x1712 = x1710.i32_mul(x1708);
Stack.push(x1712);
bool x1713 = allConcrete(x1711, x1709);
SymVal x1714 = x1713 ? Concrete(x1712, 32) : x1711.mul(x1709);
SymStack.push(x1714);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x1715 = Stack.pop();
SymVal x1716 = SymStack.pop();
Num x1717 = Stack.pop();
SymVal x1718 = SymStack.pop();
Num x1719 = x1717.i32_mul(x1715);
Stack.push(x1719);
bool x1720 = allConcrete(x1718, x1716);
SymVal x1721 = x1720 ? Concrete(x1719, 32) : x1718.mul(x1716);
SymStack.push(x1721);
}
{
Num x1722 = Stack.pop();
SymVal x1723 = SymStack.pop();
Num x1724 = Stack.pop();
SymVal x1725 = SymStack.pop();
Num x1726 = x1724.i32_add(x1722);
Stack.push(x1726);
bool x1727 = allConcrete(x1725, x1723);
SymVal x1728 = x1727 ? Concrete(x1726, 32) : x1725.add(x1723);
SymStack.push(x1728);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x1729 = Stack.pop();
SymVal x1730 = SymStack.pop();
Num x1731 = Stack.pop();
SymVal x1732 = SymStack.pop();
Num x1733 = x1731.i32_add(x1729);
Stack.push(x1733);
bool x1734 = allConcrete(x1732, x1730);
SymVal x1735 = x1734 ? Concrete(x1733, 32) : x1732.add(x1730);
SymStack.push(x1735);
}
{
Num x1736 = Stack.pop();
SymStack.pop();
Num x1737 = I32V(Memory.loadInt(x1736.toInt(), 8));
SymVal x1738 = SymMemory.loadSym(x1736.toInt(), 8);
Stack.push(x1737);
SymStack.push(x1738);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x1739 = Stack.pop();
Num x1740 = Stack.pop();
SymVal x1741 = SymStack.pop();
SymVal x1742 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x1740);
Frames.set(1, x1739);
SymFrames.set(0, x1742);
SymFrames.set(1, x1741);
updateCurrentMCont(prependCont(x1694, CURRENT_MCONT));
}
__attribute__((musttail)) return x1666(std::monostate{});
return std::monostate{};
}
std::monostate x1694(std::monostate x1695) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
__attribute__((musttail)) return x1692(std::monostate{});
return std::monostate{};
}
std::monostate x1692(std::monostate x1693) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x1690(std::monostate{});
return std::monostate{};
}
std::monostate x1690(std::monostate x1691) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x1613(std::monostate{});
return std::monostate{};
}
std::monostate x1675(std::monostate x1676) {
info("Entering the false branch 19 of the if");
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x1677 = Stack.pop();
SymStack.pop();
Num x1678 = I32V(Memory.loadInt(x1677.toInt(), 0));
SymVal x1679 = SymMemory.loadSym(x1677.toInt(), 0);
Stack.push(x1678);
SymStack.push(x1679);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1680 = Stack.pop();
SymVal x1681 = SymStack.pop();
Num x1682 = Stack.pop();
SymVal x1683 = SymStack.pop();
Num x1684 = x1682.i32_eq(x1680);
Stack.push(x1684);
bool x1685 = allConcrete(x1683, x1681);
SymVal x1686 = x1685 ? Concrete(x1684, 32) : x1683.eq(x1681).bool2bv();
SymStack.push(x1686);
}
Num x1687 = Stack.pop();
{
SymVal x1688 = SymStack.pop();
ExploreTree.fillIfElseNode(x1688, 20);
}
int x1689 = x1687.toInt();
if (x1689 != 0) {
ExploreTree.moveCursor(true, makeControl(x1619, CURRENT_MCONT));
__attribute__((musttail)) return x1673(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1673, CURRENT_MCONT));
__attribute__((musttail)) return x1619(std::monostate{});
}
return std::monostate{};
}
std::monostate x1673(std::monostate x1674) {
info("Entering the true branch 20 of the if");
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
__attribute__((musttail)) return x1671(std::monostate{});
return std::monostate{};
}
std::monostate x1671(std::monostate x1672) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x1669(std::monostate{});
return std::monostate{};
}
std::monostate x1669(std::monostate x1670) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x1667(std::monostate{});
return std::monostate{};
}
std::monostate x1667(std::monostate x1668) {
infoWhen("CALL", "Exiting the function at 8, stackSize =", Stack.size());
Frames.popFrameCallee(1);
SymFrames.popFrameCallee(1);
return enterCC(std::monostate());
}
std::monostate x1619(std::monostate x1620) {
info("Entering the false branch 20 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1621 = Stack.pop();
SymStack.pop();
Num x1622 = I32V(Memory.loadInt(x1621.toInt(), 0));
SymVal x1623 = SymMemory.loadSym(x1621.toInt(), 0);
Stack.push(x1622);
SymStack.push(x1623);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1624 = Stack.pop();
SymVal x1625 = SymStack.pop();
Num x1626 = Stack.pop();
SymVal x1627 = SymStack.pop();
Num x1628 = x1626.i32_sub(x1624);
Stack.push(x1628);
bool x1629 = allConcrete(x1627, x1625);
SymVal x1630 = x1629 ? Concrete(x1628, 32) : x1627.minus(x1625);
SymStack.push(x1630);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x1631 = Stack.pop();
SymVal x1632 = SymStack.pop();
Num x1633 = Stack.pop();
SymVal x1634 = SymStack.pop();
Num x1635 = x1633.i32_mul(x1631);
Stack.push(x1635);
bool x1636 = allConcrete(x1634, x1632);
SymVal x1637 = x1636 ? Concrete(x1635, 32) : x1634.mul(x1632);
SymStack.push(x1637);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x1638 = Stack.pop();
SymVal x1639 = SymStack.pop();
Num x1640 = Stack.pop();
SymVal x1641 = SymStack.pop();
Num x1642 = x1640.i32_mul(x1638);
Stack.push(x1642);
bool x1643 = allConcrete(x1641, x1639);
SymVal x1644 = x1643 ? Concrete(x1642, 32) : x1641.mul(x1639);
SymStack.push(x1644);
}
{
Num x1645 = Stack.pop();
SymVal x1646 = SymStack.pop();
Num x1647 = Stack.pop();
SymVal x1648 = SymStack.pop();
Num x1649 = x1647.i32_add(x1645);
Stack.push(x1649);
bool x1650 = allConcrete(x1648, x1646);
SymVal x1651 = x1650 ? Concrete(x1649, 32) : x1648.add(x1646);
SymStack.push(x1651);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x1652 = Stack.pop();
SymVal x1653 = SymStack.pop();
Num x1654 = Stack.pop();
SymVal x1655 = SymStack.pop();
Num x1656 = x1654.i32_add(x1652);
Stack.push(x1656);
bool x1657 = allConcrete(x1655, x1653);
SymVal x1658 = x1657 ? Concrete(x1656, 32) : x1655.add(x1653);
SymStack.push(x1658);
}
{
Num x1659 = Stack.pop();
SymStack.pop();
Num x1660 = I32V(Memory.loadInt(x1659.toInt(), 8));
SymVal x1661 = SymMemory.loadSym(x1659.toInt(), 8);
Stack.push(x1660);
SymStack.push(x1661);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x1662 = Stack.pop();
Num x1663 = Stack.pop();
SymVal x1664 = SymStack.pop();
SymVal x1665 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x1663);
Frames.set(1, x1662);
SymFrames.set(0, x1665);
SymFrames.set(1, x1664);
updateCurrentMCont(prependCont(x1617, CURRENT_MCONT));
}
__attribute__((musttail)) return x1666(std::monostate{});
return std::monostate{};
}
std::monostate x1617(std::monostate x1618) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
__attribute__((musttail)) return x1615(std::monostate{});
return std::monostate{};
}
std::monostate x1615(std::monostate x1616) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x1613(std::monostate{});
return std::monostate{};
}
std::monostate x1613(std::monostate x1614) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x1611(std::monostate{});
return std::monostate{};
}
std::monostate x1611(std::monostate x1612) {
infoWhen("CALL", "Exiting the function at 8, stackSize =", Stack.size());
Frames.popFrameCallee(1);
SymFrames.popFrameCallee(1);
return enterCC(std::monostate());
}
std::monostate x1581(std::monostate x1582) {
infoWhen("CALL", "Entered the function at 11, stackSize =", Stack.size());
Frames.pushFrameCallee(2);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1583 = Stack.pop();
SymStack.pop();
Num x1584 = I32V(Memory.loadInt(x1583.toInt(), 8));
SymVal x1585 = SymMemory.loadSym(x1583.toInt(), 8);
Stack.push(x1584);
SymStack.push(x1585);
}
{
Num x1586 = Stack.peek();
SymVal x1587 = SymStack.peek();
Frames.set(2, x1586);
SymFrames.set(2, x1587);
}
{
Num x1588 = Stack.pop();
SymStack.pop();
Num x1589 = I32V(Memory.loadInt(x1588.toInt(), 4));
SymVal x1590 = SymMemory.loadSym(x1588.toInt(), 4);
Stack.push(x1589);
SymStack.push(x1590);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1591 = Stack.pop();
SymStack.pop();
Num x1592 = I32V(Memory.loadInt(x1591.toInt(), 0));
SymVal x1593 = SymMemory.loadSym(x1591.toInt(), 0);
Stack.push(x1592);
SymStack.push(x1593);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1594 = Stack.pop();
SymVal x1595 = SymStack.pop();
Num x1596 = Stack.pop();
SymVal x1597 = SymStack.pop();
Num x1598 = x1596.i32_sub(x1594);
Stack.push(x1598);
bool x1599 = allConcrete(x1597, x1595);
SymVal x1600 = x1599 ? Concrete(x1598, 32) : x1597.minus(x1595);
SymStack.push(x1600);
}
{
Num x1601 = Stack.pop();
SymVal x1602 = SymStack.pop();
Num x1603 = Stack.pop();
SymVal x1604 = SymStack.pop();
Num x1605 = x1603.i32_eq(x1601);
Stack.push(x1605);
bool x1606 = allConcrete(x1604, x1602);
SymVal x1607 = x1606 ? Concrete(x1605, 32) : x1604.eq(x1602).bool2bv();
SymStack.push(x1607);
}
Num x1608 = Stack.pop();
{
SymVal x1609 = SymStack.pop();
ExploreTree.fillIfElseNode(x1609, 2);
}
int x1610 = x1608.toInt();
if (x1610 != 0) {
ExploreTree.moveCursor(true, makeControl(x1445, CURRENT_MCONT));
__attribute__((musttail)) return x1566(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1566, CURRENT_MCONT));
__attribute__((musttail)) return x1445(std::monostate{});
}
return std::monostate{};
}
std::monostate x1566(std::monostate x1567) {
info("Entering the true branch 2 of the if");
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1568 = Stack.pop();
SymStack.pop();
Num x1569 = I32V(Memory.grow(x1568.toInt()));
SymVal x1570 = Concrete(x1569, 32);
Stack.push(x1569);
SymStack.push(x1570);
}
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x1571 = Stack.pop();
SymVal x1572 = SymStack.pop();
Num x1573 = Stack.pop();
SymVal x1574 = SymStack.pop();
Num x1575 = x1573.i32_ne(x1571);
Stack.push(x1575);
bool x1576 = allConcrete(x1574, x1572);
SymVal x1577 = x1576 ? Concrete(x1575, 32) : x1574.neq(x1572).bool2bv();
SymStack.push(x1577);
}
Num x1578 = Stack.pop();
{
SymVal x1579 = SymStack.pop();
ExploreTree.fillIfElseNode(x1579, 17);
}
int x1580 = x1578.toInt();
if (x1580 != 0) {
ExploreTree.moveCursor(true, makeControl(x1457, CURRENT_MCONT));
__attribute__((musttail)) return x1473(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1473, CURRENT_MCONT));
__attribute__((musttail)) return x1457(std::monostate{});
}
return std::monostate{};
}
std::monostate x1473(std::monostate x1474) {
info("Entering the true branch 17 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1475 = Stack.pop();
SymStack.pop();
Num x1476 = I32V(Memory.loadInt(x1475.toInt(), 4));
SymVal x1477 = SymMemory.loadSym(x1475.toInt(), 4);
Stack.push(x1476);
SymStack.push(x1477);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1478 = Stack.pop();
SymVal x1479 = SymStack.pop();
Num x1480 = Stack.pop();
SymVal x1481 = SymStack.pop();
Num x1482 = x1480.i32_add(x1478);
Stack.push(x1482);
bool x1483 = allConcrete(x1481, x1479);
SymVal x1484 = x1483 ? Concrete(x1482, 32) : x1481.add(x1479);
SymStack.push(x1484);
}
Stack.push(I32V(65536));
SymStack.push(Concrete(I32V(65536), 32));
{
Num x1485 = Stack.pop();
SymVal x1486 = SymStack.pop();
Num x1487 = Stack.pop();
SymVal x1488 = SymStack.pop();
Num x1489 = x1487.i32_mul(x1485);
Stack.push(x1489);
bool x1490 = allConcrete(x1488, x1486);
SymVal x1491 = x1490 ? Concrete(x1489, 32) : x1488.mul(x1486);
SymStack.push(x1491);
}
{
Num x1492 = Stack.pop();
SymVal x1493 = SymStack.pop();
Frames.set(1, x1492);
SymFrames.set(1, x1493);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1494 = Stack.pop();
SymStack.pop();
Num x1495 = I32V(Memory.loadInt(x1494.toInt(), 4));
SymVal x1496 = SymMemory.loadSym(x1494.toInt(), 4);
Stack.push(x1495);
SymStack.push(x1496);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1497 = Stack.pop();
SymVal x1498 = SymStack.pop();
Num x1499 = Stack.pop();
SymVal x1500 = SymStack.pop();
Num x1501 = x1499.i32_add(x1497);
Stack.push(x1501);
bool x1502 = allConcrete(x1500, x1498);
SymVal x1503 = x1502 ? Concrete(x1501, 32) : x1500.add(x1498);
SymStack.push(x1503);
}
{
Num x1504 = Stack.pop();
SymVal x1505 = SymStack.pop();
Num x1506 = Stack.pop();
SymStack.pop();
int x1507 = x1506.toInt();
Memory.storeInt(x1507, 4, x1504.toInt());
SymMemory.storeSym(x1507, 4, x1505);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x1508 = Stack.pop();
SymVal x1509 = SymStack.pop();
Num x1510 = Stack.pop();
SymStack.pop();
int x1511 = x1510.toInt();
Memory.storeInt(x1511, 8, x1508.toInt());
SymMemory.storeSym(x1511, 8, x1509);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1512 = Stack.pop();
SymVal x1513 = SymStack.pop();
Num x1514 = Stack.pop();
SymStack.pop();
int x1515 = x1514.toInt();
Memory.storeInt(x1515, 0, x1512.toInt());
SymMemory.storeSym(x1515, 0, x1513);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1516 = Stack.pop();
SymVal x1517 = SymStack.pop();
Num x1518 = Stack.pop();
SymStack.pop();
int x1519 = x1518.toInt();
Memory.storeInt(x1519, 4, x1516.toInt());
SymMemory.storeSym(x1519, 4, x1517);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1520 = Stack.pop();
SymStack.pop();
Num x1521 = I32V(Memory.loadInt(x1520.toInt(), 0));
SymVal x1522 = SymMemory.loadSym(x1520.toInt(), 0);
Stack.push(x1521);
SymStack.push(x1522);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1523 = Stack.pop();
SymVal x1524 = SymStack.pop();
Num x1525 = Stack.pop();
SymVal x1526 = SymStack.pop();
Num x1527 = x1525.i32_sub(x1523);
Stack.push(x1527);
bool x1528 = allConcrete(x1526, x1524);
SymVal x1529 = x1528 ? Concrete(x1527, 32) : x1526.minus(x1524);
SymStack.push(x1529);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x1530 = Stack.pop();
SymVal x1531 = SymStack.pop();
Num x1532 = Stack.pop();
SymVal x1533 = SymStack.pop();
Num x1534 = x1532.i32_mul(x1530);
Stack.push(x1534);
bool x1535 = allConcrete(x1533, x1531);
SymVal x1536 = x1535 ? Concrete(x1534, 32) : x1533.mul(x1531);
SymStack.push(x1536);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x1537 = Stack.pop();
SymVal x1538 = SymStack.pop();
Num x1539 = Stack.pop();
SymVal x1540 = SymStack.pop();
Num x1541 = x1539.i32_mul(x1537);
Stack.push(x1541);
bool x1542 = allConcrete(x1540, x1538);
SymVal x1543 = x1542 ? Concrete(x1541, 32) : x1540.mul(x1538);
SymStack.push(x1543);
}
{
Num x1544 = Stack.pop();
SymVal x1545 = SymStack.pop();
Num x1546 = Stack.pop();
SymVal x1547 = SymStack.pop();
Num x1548 = x1546.i32_add(x1544);
Stack.push(x1548);
bool x1549 = allConcrete(x1547, x1545);
SymVal x1550 = x1549 ? Concrete(x1548, 32) : x1547.add(x1545);
SymStack.push(x1550);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x1551 = Stack.pop();
SymVal x1552 = SymStack.pop();
Num x1553 = Stack.pop();
SymVal x1554 = SymStack.pop();
Num x1555 = x1553.i32_add(x1551);
Stack.push(x1555);
bool x1556 = allConcrete(x1554, x1552);
SymVal x1557 = x1556 ? Concrete(x1555, 32) : x1554.add(x1552);
SymStack.push(x1557);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x1558 = Stack.pop();
SymVal x1559 = SymStack.pop();
Num x1560 = Stack.pop();
SymStack.pop();
int x1561 = x1560.toInt();
Memory.storeInt(x1561, 8, x1558.toInt());
SymMemory.storeSym(x1561, 8, x1559);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 9);
Num x1562 = Stack.pop();
Num x1563 = Stack.pop();
SymVal x1564 = SymStack.pop();
SymVal x1565 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x1563);
Frames.set(1, x1562);
SymFrames.set(0, x1565);
SymFrames.set(1, x1564);
updateCurrentMCont(prependCont(x1467, CURRENT_MCONT));
}
__attribute__((musttail)) return x1058(std::monostate{});
return std::monostate{};
}
std::monostate x1467(std::monostate x1468) {
infoWhen("CALL", "Returning from the function at 9, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 10);
Num x1469 = Stack.pop();
Num x1470 = Stack.pop();
SymVal x1471 = SymStack.pop();
SymVal x1472 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x1470);
Frames.set(1, x1469);
SymFrames.set(0, x1472);
SymFrames.set(1, x1471);
updateCurrentMCont(prependCont(x1465, CURRENT_MCONT));
}
__attribute__((musttail)) return x118(std::monostate{});
return std::monostate{};
}
std::monostate x1465(std::monostate x1466) {
infoWhen("CALL", "Returning from the function at 10, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
__attribute__((musttail)) return x1463(std::monostate{});
return std::monostate{};
}
std::monostate x1463(std::monostate x1464) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x1461(std::monostate{});
return std::monostate{};
}
std::monostate x1461(std::monostate x1462) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x1459(std::monostate{});
return std::monostate{};
}
std::monostate x1459(std::monostate x1460) {
infoWhen("CALL", "Exiting the function at 11, stackSize =", Stack.size());
Frames.popFrameCallee(2);
SymFrames.popFrameCallee(2);
return enterCC(std::monostate());
}
std::monostate x1457(std::monostate x1458) {
info("Entering the false branch 17 of the if");
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
__attribute__((musttail)) return x1455(std::monostate{});
return std::monostate{};
}
std::monostate x1455(std::monostate x1456) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x1453(std::monostate{});
return std::monostate{};
}
std::monostate x1453(std::monostate x1454) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x1451(std::monostate{});
return std::monostate{};
}
std::monostate x1451(std::monostate x1452) {
infoWhen("CALL", "Exiting the function at 11, stackSize =", Stack.size());
Frames.popFrameCallee(2);
SymFrames.popFrameCallee(2);
return enterCC(std::monostate());
}
std::monostate x1445(std::monostate x1446) {
info("Entering the false branch 2 of the if");
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 10);
Num x1447 = Stack.pop();
Num x1448 = Stack.pop();
SymVal x1449 = SymStack.pop();
SymVal x1450 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x1448);
Frames.set(1, x1447);
SymFrames.set(0, x1450);
SymFrames.set(1, x1449);
updateCurrentMCont(prependCont(x1443, CURRENT_MCONT));
}
__attribute__((musttail)) return x118(std::monostate{});
return std::monostate{};
}
std::monostate x1443(std::monostate x1444) {
infoWhen("CALL", "Returning from the function at 10, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
__attribute__((musttail)) return x1441(std::monostate{});
return std::monostate{};
}
std::monostate x1441(std::monostate x1442) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x1439(std::monostate{});
return std::monostate{};
}
std::monostate x1439(std::monostate x1440) {
infoWhen("CALL", "Exiting the function at 11, stackSize =", Stack.size());
Frames.popFrameCallee(2);
SymFrames.popFrameCallee(2);
return enterCC(std::monostate());
}
std::monostate x118(std::monostate x1413) {
infoWhen("CALL", "Entered the function at 10, stackSize =", Stack.size());
Frames.pushFrameCallee(1);
SymFrames.pushFrameSlot(32);
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x1414 = Stack.pop();
SymStack.pop();
Num x1415 = I32V(Memory.loadInt(x1414.toInt(), 4));
SymVal x1416 = SymMemory.loadSym(x1414.toInt(), 4);
Stack.push(x1415);
SymStack.push(x1416);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1417 = Stack.pop();
SymVal x1418 = SymStack.pop();
Num x1419 = Stack.pop();
SymVal x1420 = SymStack.pop();
Num x1421 = x1419.i32_sub(x1417);
Stack.push(x1421);
bool x1422 = allConcrete(x1420, x1418);
SymVal x1423 = x1422 ? Concrete(x1421, 32) : x1420.minus(x1418);
SymStack.push(x1423);
}
{
Num x1424 = Stack.pop();
SymVal x1425 = SymStack.pop();
Frames.set(2, x1424);
SymFrames.set(2, x1425);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x1426 = Stack.pop();
SymStack.pop();
Num x1427 = I32V(Memory.loadInt(x1426.toInt(), 0));
SymVal x1428 = SymMemory.loadSym(x1426.toInt(), 0);
Stack.push(x1427);
SymStack.push(x1428);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1429 = Stack.pop();
SymVal x1430 = SymStack.pop();
Num x1431 = Stack.pop();
SymVal x1432 = SymStack.pop();
Num x1433 = x1431.i32_eq(x1429);
Stack.push(x1433);
bool x1434 = allConcrete(x1432, x1430);
SymVal x1435 = x1434 ? Concrete(x1433, 32) : x1432.eq(x1430).bool2bv();
SymStack.push(x1435);
}
Num x1436 = Stack.pop();
{
SymVal x1437 = SymStack.pop();
ExploreTree.fillIfElseNode(x1437, 3);
}
int x1438 = x1436.toInt();
if (x1438 != 0) {
ExploreTree.moveCursor(true, makeControl(x1254, CURRENT_MCONT));
__attribute__((musttail)) return x1411(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1411, CURRENT_MCONT));
__attribute__((musttail)) return x1254(std::monostate{});
}
return std::monostate{};
}
std::monostate x1411(std::monostate x1412) {
info("Entering the true branch 3 of the if");
__attribute__((musttail)) return x1352(std::monostate{});
return std::monostate{};
}
std::monostate x1352(std::monostate x1400) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1401 = Stack.pop();
SymVal x1402 = SymStack.pop();
Num x1403 = Stack.pop();
SymVal x1404 = SymStack.pop();
Num x1405 = x1403.i32_ge_s(x1401);
Stack.push(x1405);
bool x1406 = allConcrete(x1404, x1402);
SymVal x1407 = x1406 ? Concrete(x1405, 32) : x1404.ge(x1402).bool2bv();
SymStack.push(x1407);
}
Num x1408 = Stack.pop();
{
SymVal x1409 = SymStack.pop();
ExploreTree.fillIfElseNode(x1409, 15);
}
int x1410 = x1408.toInt();
if (x1410 != 0) {
ExploreTree.moveCursor(true, makeControl(x1372, CURRENT_MCONT));
__attribute__((musttail)) return x1374(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1374, CURRENT_MCONT));
__attribute__((musttail)) return x1372(std::monostate{});
}
return std::monostate{};
}
std::monostate x1374(std::monostate x1375) {
info("Entering the true branch 15 of the if");
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x1376 = Stack.pop();
SymVal x1377 = SymStack.pop();
Num x1378 = Stack.pop();
SymVal x1379 = SymStack.pop();
Num x1380 = x1378.i32_mul(x1376);
Stack.push(x1380);
bool x1381 = allConcrete(x1379, x1377);
SymVal x1382 = x1381 ? Concrete(x1380, 32) : x1379.mul(x1377);
SymStack.push(x1382);
}
{
Num x1383 = Stack.pop();
SymVal x1384 = SymStack.pop();
Num x1385 = Stack.pop();
SymVal x1386 = SymStack.pop();
Num x1387 = x1385.i32_add(x1383);
Stack.push(x1387);
bool x1388 = allConcrete(x1386, x1384);
SymVal x1389 = x1388 ? Concrete(x1387, 32) : x1386.add(x1384);
SymStack.push(x1389);
}
{
Num x1390 = Stack.pop();
SymStack.pop();
Num x1391 = I32V(Memory.loadInt(x1390.toInt(), 8));
SymVal x1392 = SymMemory.loadSym(x1390.toInt(), 8);
Stack.push(x1391);
SymStack.push(x1392);
}
{
Num x1393 = Stack.pop();
SymVal x1394 = SymStack.pop();
Num x1395 = Stack.pop();
SymVal x1396 = SymStack.pop();
Num x1397 = x1395.i32_lt_s(x1393);
Stack.push(x1397);
bool x1398 = allConcrete(x1396, x1394);
SymVal x1399 = x1398 ? Concrete(x1397, 32) : x1396.lt(x1394).bool2bv();
SymStack.push(x1399);
}
__attribute__((musttail)) return x1353(std::monostate{});
return std::monostate{};
}
std::monostate x1372(std::monostate x1373) {
info("Entering the false branch 15 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
__attribute__((musttail)) return x1353(std::monostate{});
return std::monostate{};
}
std::monostate x1353(std::monostate x1354) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1355 = Stack.pop();
SymVal x1356 = SymStack.pop();
Num x1357 = Stack.pop();
SymVal x1358 = SymStack.pop();
Num x1359 = x1357.i32_ge_s(x1355);
Stack.push(x1359);
bool x1360 = allConcrete(x1358, x1356);
SymVal x1361 = x1360 ? Concrete(x1359, 32) : x1358.ge(x1356).bool2bv();
SymStack.push(x1361);
}
{
Num x1362 = Stack.pop();
SymVal x1363 = SymStack.pop();
Num x1364 = Stack.pop();
SymVal x1365 = SymStack.pop();
Num x1366 = x1364.i32_and(x1362);
Stack.push(x1366);
bool x1367 = allConcrete(x1365, x1363);
SymVal x1368 = x1367 ? Concrete(x1366, 32) : x1365.bitwise_and(x1363);
SymStack.push(x1368);
}
Num x1369 = Stack.pop();
{
SymVal x1370 = SymStack.pop();
ExploreTree.fillIfElseNode(x1370, 16);
}
int x1371 = x1369.toInt();
if (x1371 != 0) {
ExploreTree.moveCursor(true, makeControl(x1297, CURRENT_MCONT));
__attribute__((musttail)) return x1299(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1299, CURRENT_MCONT));
__attribute__((musttail)) return x1297(std::monostate{});
}
return std::monostate{};
}
std::monostate x1299(std::monostate x1300) {
info("Entering the true branch 16 of the if");
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1301 = Stack.pop();
SymVal x1302 = SymStack.pop();
Num x1303 = Stack.pop();
SymVal x1304 = SymStack.pop();
Num x1305 = x1303.i32_add(x1301);
Stack.push(x1305);
bool x1306 = allConcrete(x1304, x1302);
SymVal x1307 = x1306 ? Concrete(x1305, 32) : x1304.add(x1302);
SymStack.push(x1307);
}
{
Num x1308 = Stack.pop();
SymVal x1309 = SymStack.pop();
Num x1310 = Stack.pop();
SymVal x1311 = SymStack.pop();
Num x1312 = x1310.i32_mul(x1308);
Stack.push(x1312);
bool x1313 = allConcrete(x1311, x1309);
SymVal x1314 = x1313 ? Concrete(x1312, 32) : x1311.mul(x1309);
SymStack.push(x1314);
}
{
Num x1315 = Stack.pop();
SymVal x1316 = SymStack.pop();
Num x1317 = Stack.pop();
SymVal x1318 = SymStack.pop();
Num x1319 = x1317.i32_add(x1315);
Stack.push(x1319);
bool x1320 = allConcrete(x1318, x1316);
SymVal x1321 = x1320 ? Concrete(x1319, 32) : x1318.add(x1316);
SymStack.push(x1321);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x1322 = Stack.pop();
SymVal x1323 = SymStack.pop();
Num x1324 = Stack.pop();
SymVal x1325 = SymStack.pop();
Num x1326 = x1324.i32_mul(x1322);
Stack.push(x1326);
bool x1327 = allConcrete(x1325, x1323);
SymVal x1328 = x1327 ? Concrete(x1326, 32) : x1325.mul(x1323);
SymStack.push(x1328);
}
{
Num x1329 = Stack.pop();
SymVal x1330 = SymStack.pop();
Num x1331 = Stack.pop();
SymVal x1332 = SymStack.pop();
Num x1333 = x1331.i32_add(x1329);
Stack.push(x1333);
bool x1334 = allConcrete(x1332, x1330);
SymVal x1335 = x1334 ? Concrete(x1333, 32) : x1332.add(x1330);
SymStack.push(x1335);
}
{
Num x1336 = Stack.pop();
SymStack.pop();
Num x1337 = I32V(Memory.loadInt(x1336.toInt(), 8));
SymVal x1338 = SymMemory.loadSym(x1336.toInt(), 8);
Stack.push(x1337);
SymStack.push(x1338);
}
{
Num x1339 = Stack.pop();
SymVal x1340 = SymStack.pop();
Num x1341 = Stack.pop();
SymStack.pop();
int x1342 = x1341.toInt();
Memory.storeInt(x1342, 8, x1339.toInt());
SymMemory.storeSym(x1342, 8, x1340);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1343 = Stack.pop();
SymVal x1344 = SymStack.pop();
Num x1345 = Stack.pop();
SymVal x1346 = SymStack.pop();
Num x1347 = x1345.i32_sub(x1343);
Stack.push(x1347);
bool x1348 = allConcrete(x1346, x1344);
SymVal x1349 = x1348 ? Concrete(x1347, 32) : x1346.minus(x1344);
SymStack.push(x1349);
}
{
Num x1350 = Stack.pop();
SymVal x1351 = SymStack.pop();
Frames.set(2, x1350);
SymFrames.set(2, x1351);
}
info("Jump to 1");
__attribute__((musttail)) return x1352(std::monostate{});
return std::monostate{};
}
std::monostate x1297(std::monostate x1298) {
info("Entering the false branch 16 of the if");
info("Jump to 2");
__attribute__((musttail)) return x1256(std::monostate{});
return std::monostate{};
}
std::monostate x1256(std::monostate x1257) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1258 = Stack.pop();
SymVal x1259 = SymStack.pop();
Num x1260 = Stack.pop();
SymVal x1261 = SymStack.pop();
Num x1262 = x1260.i32_add(x1258);
Stack.push(x1262);
bool x1263 = allConcrete(x1261, x1259);
SymVal x1264 = x1263 ? Concrete(x1262, 32) : x1261.add(x1259);
SymStack.push(x1264);
}
{
Num x1265 = Stack.pop();
SymVal x1266 = SymStack.pop();
Num x1267 = Stack.pop();
SymVal x1268 = SymStack.pop();
Num x1269 = x1267.i32_mul(x1265);
Stack.push(x1269);
bool x1270 = allConcrete(x1268, x1266);
SymVal x1271 = x1270 ? Concrete(x1269, 32) : x1268.mul(x1266);
SymStack.push(x1271);
}
{
Num x1272 = Stack.pop();
SymVal x1273 = SymStack.pop();
Num x1274 = Stack.pop();
SymVal x1275 = SymStack.pop();
Num x1276 = x1274.i32_add(x1272);
Stack.push(x1276);
bool x1277 = allConcrete(x1275, x1273);
SymVal x1278 = x1277 ? Concrete(x1276, 32) : x1275.add(x1273);
SymStack.push(x1278);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x1279 = Stack.pop();
SymVal x1280 = SymStack.pop();
Num x1281 = Stack.pop();
SymStack.pop();
int x1282 = x1281.toInt();
Memory.storeInt(x1282, 8, x1279.toInt());
SymMemory.storeSym(x1282, 8, x1280);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x1283 = Stack.pop();
SymStack.pop();
Num x1284 = I32V(Memory.loadInt(x1283.toInt(), 4));
SymVal x1285 = SymMemory.loadSym(x1283.toInt(), 4);
Stack.push(x1284);
SymStack.push(x1285);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1286 = Stack.pop();
SymVal x1287 = SymStack.pop();
Num x1288 = Stack.pop();
SymVal x1289 = SymStack.pop();
Num x1290 = x1288.i32_add(x1286);
Stack.push(x1290);
bool x1291 = allConcrete(x1289, x1287);
SymVal x1292 = x1291 ? Concrete(x1290, 32) : x1289.add(x1287);
SymStack.push(x1292);
}
{
Num x1293 = Stack.pop();
SymVal x1294 = SymStack.pop();
Num x1295 = Stack.pop();
SymStack.pop();
int x1296 = x1295.toInt();
Memory.storeInt(x1296, 4, x1293.toInt());
SymMemory.storeSym(x1296, 4, x1294);
}
__attribute__((musttail)) return x67(std::monostate{});
return std::monostate{};
}
std::monostate x1254(std::monostate x1255) {
info("Entering the false branch 3 of the if");
__attribute__((musttail)) return x1213(std::monostate{});
return std::monostate{};
}
std::monostate x1213(std::monostate x1243) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1244 = Stack.pop();
SymVal x1245 = SymStack.pop();
Num x1246 = Stack.pop();
SymVal x1247 = SymStack.pop();
Num x1248 = x1246.i32_ge_s(x1244);
Stack.push(x1248);
bool x1249 = allConcrete(x1247, x1245);
SymVal x1250 = x1249 ? Concrete(x1248, 32) : x1247.ge(x1245).bool2bv();
SymStack.push(x1250);
}
Num x1251 = Stack.pop();
{
SymVal x1252 = SymStack.pop();
ExploreTree.fillIfElseNode(x1252, 4);
}
int x1253 = x1251.toInt();
if (x1253 != 0) {
ExploreTree.moveCursor(true, makeControl(x1198, CURRENT_MCONT));
__attribute__((musttail)) return x1214(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1214, CURRENT_MCONT));
__attribute__((musttail)) return x1198(std::monostate{});
}
return std::monostate{};
}
std::monostate x1214(std::monostate x1215) {
info("Entering the true branch 4 of the if");
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x1216 = Stack.pop();
SymVal x1217 = SymStack.pop();
Num x1218 = Stack.pop();
SymVal x1219 = SymStack.pop();
Num x1220 = x1218.i32_mul(x1216);
Stack.push(x1220);
bool x1221 = allConcrete(x1219, x1217);
SymVal x1222 = x1221 ? Concrete(x1220, 32) : x1219.mul(x1217);
SymStack.push(x1222);
}
{
Num x1223 = Stack.pop();
SymVal x1224 = SymStack.pop();
Num x1225 = Stack.pop();
SymVal x1226 = SymStack.pop();
Num x1227 = x1225.i32_add(x1223);
Stack.push(x1227);
bool x1228 = allConcrete(x1226, x1224);
SymVal x1229 = x1228 ? Concrete(x1227, 32) : x1226.add(x1224);
SymStack.push(x1229);
}
{
Num x1230 = Stack.pop();
SymStack.pop();
Num x1231 = I32V(Memory.loadInt(x1230.toInt(), 8));
SymVal x1232 = SymMemory.loadSym(x1230.toInt(), 8);
Stack.push(x1231);
SymStack.push(x1232);
}
{
Num x1233 = Stack.pop();
SymVal x1234 = SymStack.pop();
Num x1235 = Stack.pop();
SymVal x1236 = SymStack.pop();
Num x1237 = x1235.i32_lt_s(x1233);
Stack.push(x1237);
bool x1238 = allConcrete(x1236, x1234);
SymVal x1239 = x1238 ? Concrete(x1237, 32) : x1236.lt(x1234).bool2bv();
SymStack.push(x1239);
}
Num x1240 = Stack.pop();
{
SymVal x1241 = SymStack.pop();
ExploreTree.fillIfElseNode(x1241, 14);
}
int x1242 = x1240.toInt();
if (x1242 != 0) {
ExploreTree.moveCursor(true, makeControl(x1200, CURRENT_MCONT));
__attribute__((musttail)) return x1202(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1202, CURRENT_MCONT));
__attribute__((musttail)) return x1200(std::monostate{});
}
return std::monostate{};
}
std::monostate x1202(std::monostate x1203) {
info("Entering the true branch 14 of the if");
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1204 = Stack.pop();
SymVal x1205 = SymStack.pop();
Num x1206 = Stack.pop();
SymVal x1207 = SymStack.pop();
Num x1208 = x1206.i32_sub(x1204);
Stack.push(x1208);
bool x1209 = allConcrete(x1207, x1205);
SymVal x1210 = x1209 ? Concrete(x1208, 32) : x1207.minus(x1205);
SymStack.push(x1210);
}
{
Num x1211 = Stack.pop();
SymVal x1212 = SymStack.pop();
Frames.set(2, x1211);
SymFrames.set(2, x1212);
}
info("Jump to 2");
__attribute__((musttail)) return x1213(std::monostate{});
return std::monostate{};
}
std::monostate x1200(std::monostate x1201) {
info("Entering the false branch 14 of the if");
info("Jump to 3");
__attribute__((musttail)) return x1123(std::monostate{});
return std::monostate{};
}
std::monostate x1198(std::monostate x1199) {
info("Entering the false branch 4 of the if");
info("Jump to 2");
__attribute__((musttail)) return x1123(std::monostate{});
return std::monostate{};
}
std::monostate x1123(std::monostate x1124) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1125 = Stack.pop();
SymVal x1126 = SymStack.pop();
Num x1127 = Stack.pop();
SymVal x1128 = SymStack.pop();
Num x1129 = x1127.i32_add(x1125);
Stack.push(x1129);
bool x1130 = allConcrete(x1128, x1126);
SymVal x1131 = x1130 ? Concrete(x1129, 32) : x1128.add(x1126);
SymStack.push(x1131);
}
{
Num x1132 = Stack.pop();
SymVal x1133 = SymStack.pop();
Frames.set(2, x1132);
SymFrames.set(2, x1133);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1134 = Stack.pop();
SymStack.pop();
Num x1135 = I32V(Memory.loadInt(x1134.toInt(), 0));
SymVal x1136 = SymMemory.loadSym(x1134.toInt(), 0);
Stack.push(x1135);
SymStack.push(x1136);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1137 = Stack.pop();
SymVal x1138 = SymStack.pop();
Num x1139 = Stack.pop();
SymVal x1140 = SymStack.pop();
Num x1141 = x1139.i32_sub(x1137);
Stack.push(x1141);
bool x1142 = allConcrete(x1140, x1138);
SymVal x1143 = x1142 ? Concrete(x1141, 32) : x1140.minus(x1138);
SymStack.push(x1143);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x1144 = Stack.pop();
SymVal x1145 = SymStack.pop();
Num x1146 = Stack.pop();
SymVal x1147 = SymStack.pop();
Num x1148 = x1146.i32_mul(x1144);
Stack.push(x1148);
bool x1149 = allConcrete(x1147, x1145);
SymVal x1150 = x1149 ? Concrete(x1148, 32) : x1147.mul(x1145);
SymStack.push(x1150);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x1151 = Stack.pop();
SymVal x1152 = SymStack.pop();
Num x1153 = Stack.pop();
SymVal x1154 = SymStack.pop();
Num x1155 = x1153.i32_mul(x1151);
Stack.push(x1155);
bool x1156 = allConcrete(x1154, x1152);
SymVal x1157 = x1156 ? Concrete(x1155, 32) : x1154.mul(x1152);
SymStack.push(x1157);
}
{
Num x1158 = Stack.pop();
SymVal x1159 = SymStack.pop();
Num x1160 = Stack.pop();
SymVal x1161 = SymStack.pop();
Num x1162 = x1160.i32_add(x1158);
Stack.push(x1162);
bool x1163 = allConcrete(x1161, x1159);
SymVal x1164 = x1163 ? Concrete(x1162, 32) : x1161.add(x1159);
SymStack.push(x1164);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x1165 = Stack.pop();
SymVal x1166 = SymStack.pop();
Num x1167 = Stack.pop();
SymVal x1168 = SymStack.pop();
Num x1169 = x1167.i32_add(x1165);
Stack.push(x1169);
bool x1170 = allConcrete(x1168, x1166);
SymVal x1171 = x1170 ? Concrete(x1169, 32) : x1168.add(x1166);
SymStack.push(x1171);
}
{
Num x1172 = Stack.pop();
SymStack.pop();
Num x1173 = I32V(Memory.loadInt(x1172.toInt(), 8));
SymVal x1174 = SymMemory.loadSym(x1172.toInt(), 8);
Stack.push(x1173);
SymStack.push(x1174);
}
{
Num x1175 = Stack.pop();
SymStack.pop();
Num x1176 = I32V(Memory.loadInt(x1175.toInt(), 4));
SymVal x1177 = SymMemory.loadSym(x1175.toInt(), 4);
Stack.push(x1176);
SymStack.push(x1177);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1178 = Stack.pop();
SymStack.pop();
Num x1179 = I32V(Memory.loadInt(x1178.toInt(), 0));
SymVal x1180 = SymMemory.loadSym(x1178.toInt(), 0);
Stack.push(x1179);
SymStack.push(x1180);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1181 = Stack.pop();
SymVal x1182 = SymStack.pop();
Num x1183 = Stack.pop();
SymVal x1184 = SymStack.pop();
Num x1185 = x1183.i32_sub(x1181);
Stack.push(x1185);
bool x1186 = allConcrete(x1184, x1182);
SymVal x1187 = x1186 ? Concrete(x1185, 32) : x1184.minus(x1182);
SymStack.push(x1187);
}
{
Num x1188 = Stack.pop();
SymVal x1189 = SymStack.pop();
Num x1190 = Stack.pop();
SymVal x1191 = SymStack.pop();
Num x1192 = x1190.i32_eq(x1188);
Stack.push(x1192);
bool x1193 = allConcrete(x1191, x1189);
SymVal x1194 = x1193 ? Concrete(x1192, 32) : x1191.eq(x1189).bool2bv();
SymStack.push(x1194);
}
Num x1195 = Stack.pop();
{
SymVal x1196 = SymStack.pop();
ExploreTree.fillIfElseNode(x1196, 5);
}
int x1197 = x1195.toInt();
if (x1197 != 0) {
ExploreTree.moveCursor(true, makeControl(x119, CURRENT_MCONT));
__attribute__((musttail)) return x1117(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1117, CURRENT_MCONT));
__attribute__((musttail)) return x119(std::monostate{});
}
return std::monostate{};
}
std::monostate x1117(std::monostate x1118) {
info("Entering the true branch 5 of the if");
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 9);
Num x1119 = Stack.pop();
Num x1120 = Stack.pop();
SymVal x1121 = SymStack.pop();
SymVal x1122 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x1120);
Frames.set(1, x1119);
SymFrames.set(0, x1122);
SymFrames.set(1, x1121);
updateCurrentMCont(prependCont(x1088, CURRENT_MCONT));
}
__attribute__((musttail)) return x1058(std::monostate{});
return std::monostate{};
}
std::monostate x1088(std::monostate x1089) {
infoWhen("CALL", "Returning from the function at 9, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x1090 = Stack.pop();
SymVal x1091 = SymStack.pop();
Num x1092 = Stack.pop();
SymVal x1093 = SymStack.pop();
Num x1094 = x1092.i32_mul(x1090);
Stack.push(x1094);
bool x1095 = allConcrete(x1093, x1091);
SymVal x1096 = x1095 ? Concrete(x1094, 32) : x1093.mul(x1091);
SymStack.push(x1096);
}
{
Num x1097 = Stack.pop();
SymVal x1098 = SymStack.pop();
Num x1099 = Stack.pop();
SymVal x1100 = SymStack.pop();
Num x1101 = x1099.i32_add(x1097);
Stack.push(x1101);
bool x1102 = allConcrete(x1100, x1098);
SymVal x1103 = x1102 ? Concrete(x1101, 32) : x1100.add(x1098);
SymStack.push(x1103);
}
{
Num x1104 = Stack.pop();
SymStack.pop();
Num x1105 = I32V(Memory.loadInt(x1104.toInt(), 8));
SymVal x1106 = SymMemory.loadSym(x1104.toInt(), 8);
Stack.push(x1105);
SymStack.push(x1106);
}
{
Num x1107 = Stack.pop();
SymVal x1108 = SymStack.pop();
Num x1109 = Stack.pop();
SymVal x1110 = SymStack.pop();
Num x1111 = x1109.i32_gt_s(x1107);
Stack.push(x1111);
bool x1112 = allConcrete(x1110, x1108);
SymVal x1113 = x1112 ? Concrete(x1111, 32) : x1110.gt(x1108).bool2bv();
SymStack.push(x1113);
}
Num x1114 = Stack.pop();
{
SymVal x1115 = SymStack.pop();
ExploreTree.fillIfElseNode(x1115, 13);
}
int x1116 = x1114.toInt();
if (x1116 != 0) {
ExploreTree.moveCursor(true, makeControl(x1075, CURRENT_MCONT));
__attribute__((musttail)) return x1077(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1077, CURRENT_MCONT));
__attribute__((musttail)) return x1075(std::monostate{});
}
return std::monostate{};
}
std::monostate x1077(std::monostate x1078) {
info("Entering the true branch 13 of the if");
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1079 = Stack.pop();
SymVal x1080 = SymStack.pop();
Num x1081 = Stack.pop();
SymVal x1082 = SymStack.pop();
Num x1083 = x1081.i32_add(x1079);
Stack.push(x1083);
bool x1084 = allConcrete(x1082, x1080);
SymVal x1085 = x1084 ? Concrete(x1083, 32) : x1082.add(x1080);
SymStack.push(x1085);
}
{
Num x1086 = Stack.pop();
SymVal x1087 = SymStack.pop();
Frames.set(2, x1086);
SymFrames.set(2, x1087);
}
__attribute__((musttail)) return x1073(std::monostate{});
return std::monostate{};
}
std::monostate x1075(std::monostate x1076) {
info("Entering the false branch 13 of the if");
__attribute__((musttail)) return x1073(std::monostate{});
return std::monostate{};
}
std::monostate x1073(std::monostate x1074) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x71(std::monostate{});
return std::monostate{};
}
std::monostate x1058(std::monostate x1059) {
infoWhen("CALL", "Entered the function at 9, stackSize =", Stack.size());
Frames.pushFrameCallee(2);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1060 = Stack.pop();
SymStack.pop();
Num x1061 = I32V(Memory.grow(x1060.toInt()));
SymVal x1062 = Concrete(x1061, 32);
Stack.push(x1061);
SymStack.push(x1062);
}
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x1063 = Stack.pop();
SymVal x1064 = SymStack.pop();
Num x1065 = Stack.pop();
SymVal x1066 = SymStack.pop();
Num x1067 = x1065.i32_ne(x1063);
Stack.push(x1067);
bool x1068 = allConcrete(x1066, x1064);
SymVal x1069 = x1068 ? Concrete(x1067, 32) : x1066.neq(x1064).bool2bv();
SymStack.push(x1069);
}
Num x1070 = Stack.pop();
{
SymVal x1071 = SymStack.pop();
ExploreTree.fillIfElseNode(x1071, 6);
}
int x1072 = x1070.toInt();
if (x1072 != 0) {
ExploreTree.moveCursor(true, makeControl(x125, CURRENT_MCONT));
__attribute__((musttail)) return x969(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x969, CURRENT_MCONT));
__attribute__((musttail)) return x125(std::monostate{});
}
return std::monostate{};
}
std::monostate x969(std::monostate x970) {
info("Entering the true branch 6 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x971 = Stack.pop();
SymStack.pop();
Num x972 = I32V(Memory.loadInt(x971.toInt(), 4));
SymVal x973 = SymMemory.loadSym(x971.toInt(), 4);
Stack.push(x972);
SymStack.push(x973);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x974 = Stack.pop();
SymVal x975 = SymStack.pop();
Num x976 = Stack.pop();
SymVal x977 = SymStack.pop();
Num x978 = x976.i32_add(x974);
Stack.push(x978);
bool x979 = allConcrete(x977, x975);
SymVal x980 = x979 ? Concrete(x978, 32) : x977.add(x975);
SymStack.push(x980);
}
Stack.push(I32V(65536));
SymStack.push(Concrete(I32V(65536), 32));
{
Num x981 = Stack.pop();
SymVal x982 = SymStack.pop();
Num x983 = Stack.pop();
SymVal x984 = SymStack.pop();
Num x985 = x983.i32_mul(x981);
Stack.push(x985);
bool x986 = allConcrete(x984, x982);
SymVal x987 = x986 ? Concrete(x985, 32) : x984.mul(x982);
SymStack.push(x987);
}
{
Num x988 = Stack.pop();
SymVal x989 = SymStack.pop();
Frames.set(2, x988);
SymFrames.set(2, x989);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x990 = Stack.pop();
SymStack.pop();
Num x991 = I32V(Memory.loadInt(x990.toInt(), 4));
SymVal x992 = SymMemory.loadSym(x990.toInt(), 4);
Stack.push(x991);
SymStack.push(x992);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x993 = Stack.pop();
SymVal x994 = SymStack.pop();
Num x995 = Stack.pop();
SymVal x996 = SymStack.pop();
Num x997 = x995.i32_add(x993);
Stack.push(x997);
bool x998 = allConcrete(x996, x994);
SymVal x999 = x998 ? Concrete(x997, 32) : x996.add(x994);
SymStack.push(x999);
}
{
Num x1000 = Stack.pop();
SymVal x1001 = SymStack.pop();
Num x1002 = Stack.pop();
SymStack.pop();
int x1003 = x1002.toInt();
Memory.storeInt(x1003, 4, x1000.toInt());
SymMemory.storeSym(x1003, 4, x1001);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1004 = Stack.pop();
SymStack.pop();
Num x1005 = I32V(Memory.loadInt(x1004.toInt(), 0));
SymVal x1006 = SymMemory.loadSym(x1004.toInt(), 0);
Stack.push(x1005);
SymStack.push(x1006);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1007 = Stack.pop();
SymVal x1008 = SymStack.pop();
Num x1009 = Stack.pop();
SymVal x1010 = SymStack.pop();
Num x1011 = x1009.i32_sub(x1007);
Stack.push(x1011);
bool x1012 = allConcrete(x1010, x1008);
SymVal x1013 = x1012 ? Concrete(x1011, 32) : x1010.minus(x1008);
SymStack.push(x1013);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x1014 = Stack.pop();
SymVal x1015 = SymStack.pop();
Num x1016 = Stack.pop();
SymVal x1017 = SymStack.pop();
Num x1018 = x1016.i32_mul(x1014);
Stack.push(x1018);
bool x1019 = allConcrete(x1017, x1015);
SymVal x1020 = x1019 ? Concrete(x1018, 32) : x1017.mul(x1015);
SymStack.push(x1020);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x1021 = Stack.pop();
SymVal x1022 = SymStack.pop();
Num x1023 = Stack.pop();
SymVal x1024 = SymStack.pop();
Num x1025 = x1023.i32_mul(x1021);
Stack.push(x1025);
bool x1026 = allConcrete(x1024, x1022);
SymVal x1027 = x1026 ? Concrete(x1025, 32) : x1024.mul(x1022);
SymStack.push(x1027);
}
{
Num x1028 = Stack.pop();
SymVal x1029 = SymStack.pop();
Num x1030 = Stack.pop();
SymVal x1031 = SymStack.pop();
Num x1032 = x1030.i32_add(x1028);
Stack.push(x1032);
bool x1033 = allConcrete(x1031, x1029);
SymVal x1034 = x1033 ? Concrete(x1032, 32) : x1031.add(x1029);
SymStack.push(x1034);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x1035 = Stack.pop();
SymVal x1036 = SymStack.pop();
Num x1037 = Stack.pop();
SymVal x1038 = SymStack.pop();
Num x1039 = x1037.i32_add(x1035);
Stack.push(x1039);
bool x1040 = allConcrete(x1038, x1036);
SymVal x1041 = x1040 ? Concrete(x1039, 32) : x1038.add(x1036);
SymStack.push(x1041);
}
{
Num x1042 = Stack.pop();
SymStack.pop();
Num x1043 = I32V(Memory.loadInt(x1042.toInt(), 8));
SymVal x1044 = SymMemory.loadSym(x1042.toInt(), 8);
Stack.push(x1043);
SymStack.push(x1044);
}
{
Num x1045 = Stack.pop();
SymStack.pop();
Num x1046 = I32V(Memory.loadInt(x1045.toInt(), 0));
SymVal x1047 = SymMemory.loadSym(x1045.toInt(), 0);
Stack.push(x1046);
SymStack.push(x1047);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1048 = Stack.pop();
SymVal x1049 = SymStack.pop();
Num x1050 = Stack.pop();
SymVal x1051 = SymStack.pop();
Num x1052 = x1050.i32_eq(x1048);
Stack.push(x1052);
bool x1053 = allConcrete(x1051, x1049);
SymVal x1054 = x1053 ? Concrete(x1052, 32) : x1051.eq(x1049).bool2bv();
SymStack.push(x1054);
}
Num x1055 = Stack.pop();
{
SymVal x1056 = SymStack.pop();
ExploreTree.fillIfElseNode(x1056, 7);
}
int x1057 = x1055.toInt();
if (x1057 != 0) {
ExploreTree.moveCursor(true, makeControl(x957, CURRENT_MCONT));
__attribute__((musttail)) return x963(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x963, CURRENT_MCONT));
__attribute__((musttail)) return x957(std::monostate{});
}
return std::monostate{};
}
std::monostate x963(std::monostate x964) {
info("Entering the true branch 7 of the if");
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x965 = Stack.pop();
SymVal x966 = SymStack.pop();
Num x967 = Stack.pop();
SymStack.pop();
int x968 = x967.toInt();
Memory.storeInt(x968, 0, x965.toInt());
SymMemory.storeSym(x968, 0, x966);
}
__attribute__((musttail)) return x932(std::monostate{});
return std::monostate{};
}
std::monostate x957(std::monostate x958) {
info("Entering the false branch 7 of the if");
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x959 = Stack.pop();
SymVal x960 = SymStack.pop();
Num x961 = Stack.pop();
SymStack.pop();
int x962 = x961.toInt();
Memory.storeInt(x962, 0, x959.toInt());
SymMemory.storeSym(x962, 0, x960);
}
__attribute__((musttail)) return x932(std::monostate{});
return std::monostate{};
}
std::monostate x932(std::monostate x933) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x934 = Stack.pop();
SymStack.pop();
Num x935 = I32V(Memory.loadInt(x934.toInt(), 0));
SymVal x936 = SymMemory.loadSym(x934.toInt(), 0);
Stack.push(x935);
SymStack.push(x936);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x937 = Stack.pop();
SymVal x938 = SymStack.pop();
Num x939 = Stack.pop();
SymVal x940 = SymStack.pop();
Num x941 = x939.i32_div_s(x937);
Stack.push(x941);
bool x942 = allConcrete(x940, x938);
SymVal x943 = x942 ? Concrete(x941, 32) : x940.div(x938);
SymStack.push(x943);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x944 = Stack.pop();
SymVal x945 = SymStack.pop();
Num x946 = Stack.pop();
SymVal x947 = SymStack.pop();
Num x948 = x946.i32_sub(x944);
Stack.push(x948);
bool x949 = allConcrete(x947, x945);
SymVal x950 = x949 ? Concrete(x948, 32) : x947.minus(x945);
SymStack.push(x950);
}
{
Num x951 = Stack.pop();
SymVal x952 = SymStack.pop();
Num x953 = Stack.pop();
SymStack.pop();
int x954 = x953.toInt();
Memory.storeInt(x954, 4, x951.toInt());
SymMemory.storeSym(x954, 4, x952);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x955 = Stack.pop();
SymVal x956 = SymStack.pop();
Frames.set(3, x955);
SymFrames.set(3, x956);
}
__attribute__((musttail)) return x231(std::monostate{});
return std::monostate{};
}
std::monostate x231(std::monostate x904) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x905 = Stack.pop();
SymStack.pop();
Num x906 = I32V(Memory.loadInt(x905.toInt(), 0));
SymVal x907 = SymMemory.loadSym(x905.toInt(), 0);
Stack.push(x906);
SymStack.push(x907);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x908 = Stack.pop();
SymVal x909 = SymStack.pop();
Num x910 = Stack.pop();
SymVal x911 = SymStack.pop();
Num x912 = x910.i32_div_s(x908);
Stack.push(x912);
bool x913 = allConcrete(x911, x909);
SymVal x914 = x913 ? Concrete(x912, 32) : x911.div(x909);
SymStack.push(x914);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x915 = Stack.pop();
SymVal x916 = SymStack.pop();
Num x917 = Stack.pop();
SymVal x918 = SymStack.pop();
Num x919 = x917.i32_sub(x915);
Stack.push(x919);
bool x920 = allConcrete(x918, x916);
SymVal x921 = x920 ? Concrete(x919, 32) : x918.minus(x916);
SymStack.push(x921);
}
{
Num x922 = Stack.pop();
SymVal x923 = SymStack.pop();
Num x924 = Stack.pop();
SymVal x925 = SymStack.pop();
Num x926 = x924.i32_eq(x922);
Stack.push(x926);
bool x927 = allConcrete(x925, x923);
SymVal x928 = x927 ? Concrete(x926, 32) : x925.eq(x923).bool2bv();
SymStack.push(x928);
}
Num x929 = Stack.pop();
{
SymVal x930 = SymStack.pop();
ExploreTree.fillIfElseNode(x930, 8);
}
int x931 = x929.toInt();
if (x931 != 0) {
ExploreTree.moveCursor(true, makeControl(x127, CURRENT_MCONT));
__attribute__((musttail)) return x902(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x902, CURRENT_MCONT));
__attribute__((musttail)) return x127(std::monostate{});
}
return std::monostate{};
}
std::monostate x902(std::monostate x903) {
info("Entering the true branch 8 of the if");
info("Jump to 2");
__attribute__((musttail)) return x844(std::monostate{});
return std::monostate{};
}
std::monostate x844(std::monostate x845) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x846 = Stack.pop();
SymVal x847 = SymStack.pop();
Frames.set(3, x846);
SymFrames.set(3, x847);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x848 = Stack.pop();
SymStack.pop();
Num x849 = I32V(Memory.loadInt(x848.toInt(), 0));
SymVal x850 = SymMemory.loadSym(x848.toInt(), 0);
Stack.push(x849);
SymStack.push(x850);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x851 = Stack.pop();
SymVal x852 = SymStack.pop();
Num x853 = Stack.pop();
SymVal x854 = SymStack.pop();
Num x855 = x853.i32_sub(x851);
Stack.push(x855);
bool x856 = allConcrete(x854, x852);
SymVal x857 = x856 ? Concrete(x855, 32) : x854.minus(x852);
SymStack.push(x857);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x858 = Stack.pop();
SymVal x859 = SymStack.pop();
Num x860 = Stack.pop();
SymVal x861 = SymStack.pop();
Num x862 = x860.i32_mul(x858);
Stack.push(x862);
bool x863 = allConcrete(x861, x859);
SymVal x864 = x863 ? Concrete(x862, 32) : x861.mul(x859);
SymStack.push(x864);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x865 = Stack.pop();
SymVal x866 = SymStack.pop();
Num x867 = Stack.pop();
SymVal x868 = SymStack.pop();
Num x869 = x867.i32_mul(x865);
Stack.push(x869);
bool x870 = allConcrete(x868, x866);
SymVal x871 = x870 ? Concrete(x869, 32) : x868.mul(x866);
SymStack.push(x871);
}
{
Num x872 = Stack.pop();
SymVal x873 = SymStack.pop();
Num x874 = Stack.pop();
SymVal x875 = SymStack.pop();
Num x876 = x874.i32_add(x872);
Stack.push(x876);
bool x877 = allConcrete(x875, x873);
SymVal x878 = x877 ? Concrete(x876, 32) : x875.add(x873);
SymStack.push(x878);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x879 = Stack.pop();
SymVal x880 = SymStack.pop();
Num x881 = Stack.pop();
SymVal x882 = SymStack.pop();
Num x883 = x881.i32_add(x879);
Stack.push(x883);
bool x884 = allConcrete(x882, x880);
SymVal x885 = x884 ? Concrete(x883, 32) : x882.add(x880);
SymStack.push(x885);
}
{
Num x886 = Stack.pop();
SymStack.pop();
Num x887 = I32V(Memory.loadInt(x886.toInt(), 8));
SymVal x888 = SymMemory.loadSym(x886.toInt(), 8);
Stack.push(x887);
SymStack.push(x888);
}
{
Num x889 = Stack.pop();
SymStack.pop();
Num x890 = I32V(Memory.loadInt(x889.toInt(), 0));
SymVal x891 = SymMemory.loadSym(x889.toInt(), 0);
Stack.push(x890);
SymStack.push(x891);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x892 = Stack.pop();
SymVal x893 = SymStack.pop();
Num x894 = Stack.pop();
SymVal x895 = SymStack.pop();
Num x896 = x894.i32_ne(x892);
Stack.push(x896);
bool x897 = allConcrete(x895, x893);
SymVal x898 = x897 ? Concrete(x896, 32) : x895.neq(x893).bool2bv();
SymStack.push(x898);
}
Num x899 = Stack.pop();
{
SymVal x900 = SymStack.pop();
ExploreTree.fillIfElseNode(x900, 9);
}
int x901 = x899.toInt();
if (x901 != 0) {
ExploreTree.moveCursor(true, makeControl(x662, CURRENT_MCONT));
__attribute__((musttail)) return x842(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x842, CURRENT_MCONT));
__attribute__((musttail)) return x662(std::monostate{});
}
return std::monostate{};
}
std::monostate x842(std::monostate x843) {
info("Entering the true branch 9 of the if");
__attribute__((musttail)) return x816(std::monostate{});
return std::monostate{};
}
std::monostate x816(std::monostate x821) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x822 = Stack.pop();
SymStack.pop();
Num x823 = I32V(Memory.loadInt(x822.toInt(), 0));
SymVal x824 = SymMemory.loadSym(x822.toInt(), 0);
Stack.push(x823);
SymStack.push(x824);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x825 = Stack.pop();
SymVal x826 = SymStack.pop();
Num x827 = Stack.pop();
SymVal x828 = SymStack.pop();
Num x829 = x827.i32_div_s(x825);
Stack.push(x829);
bool x830 = allConcrete(x828, x826);
SymVal x831 = x830 ? Concrete(x829, 32) : x828.div(x826);
SymStack.push(x831);
}
{
Num x832 = Stack.pop();
SymVal x833 = SymStack.pop();
Num x834 = Stack.pop();
SymVal x835 = SymStack.pop();
Num x836 = x834.i32_eq(x832);
Stack.push(x836);
bool x837 = allConcrete(x835, x833);
SymVal x838 = x837 ? Concrete(x836, 32) : x835.eq(x833).bool2bv();
SymStack.push(x838);
}
Num x839 = Stack.pop();
{
SymVal x840 = SymStack.pop();
ExploreTree.fillIfElseNode(x840, 12);
}
int x841 = x839.toInt();
if (x841 != 0) {
ExploreTree.moveCursor(true, makeControl(x664, CURRENT_MCONT));
__attribute__((musttail)) return x819(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x819, CURRENT_MCONT));
__attribute__((musttail)) return x664(std::monostate{});
}
return std::monostate{};
}
std::monostate x819(std::monostate x820) {
info("Entering the true branch 12 of the if");
info("Jump to 2");
__attribute__((musttail)) return x817(std::monostate{});
return std::monostate{};
}
std::monostate x817(std::monostate x818) {
info("Exiting the block, stackSize =", Stack.size());
__attribute__((musttail)) return x593(std::monostate{});
return std::monostate{};
}
std::monostate x664(std::monostate x665) {
info("Entering the false branch 12 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x666 = Stack.pop();
SymStack.pop();
Num x667 = I32V(Memory.loadInt(x666.toInt(), 0));
SymVal x668 = SymMemory.loadSym(x666.toInt(), 0);
Stack.push(x667);
SymStack.push(x668);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x669 = Stack.pop();
SymVal x670 = SymStack.pop();
Num x671 = Stack.pop();
SymVal x672 = SymStack.pop();
Num x673 = x671.i32_sub(x669);
Stack.push(x673);
bool x674 = allConcrete(x672, x670);
SymVal x675 = x674 ? Concrete(x673, 32) : x672.minus(x670);
SymStack.push(x675);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x676 = Stack.pop();
SymVal x677 = SymStack.pop();
Num x678 = Stack.pop();
SymVal x679 = SymStack.pop();
Num x680 = x678.i32_mul(x676);
Stack.push(x680);
bool x681 = allConcrete(x679, x677);
SymVal x682 = x681 ? Concrete(x680, 32) : x679.mul(x677);
SymStack.push(x682);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x683 = Stack.pop();
SymVal x684 = SymStack.pop();
Num x685 = Stack.pop();
SymVal x686 = SymStack.pop();
Num x687 = x685.i32_mul(x683);
Stack.push(x687);
bool x688 = allConcrete(x686, x684);
SymVal x689 = x688 ? Concrete(x687, 32) : x686.mul(x684);
SymStack.push(x689);
}
{
Num x690 = Stack.pop();
SymVal x691 = SymStack.pop();
Num x692 = Stack.pop();
SymVal x693 = SymStack.pop();
Num x694 = x692.i32_add(x690);
Stack.push(x694);
bool x695 = allConcrete(x693, x691);
SymVal x696 = x695 ? Concrete(x694, 32) : x693.add(x691);
SymStack.push(x696);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x697 = Stack.pop();
SymVal x698 = SymStack.pop();
Num x699 = Stack.pop();
SymVal x700 = SymStack.pop();
Num x701 = x699.i32_add(x697);
Stack.push(x701);
bool x702 = allConcrete(x700, x698);
SymVal x703 = x702 ? Concrete(x701, 32) : x700.add(x698);
SymStack.push(x703);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x704 = Stack.pop();
SymStack.pop();
Num x705 = I32V(Memory.loadInt(x704.toInt(), 0));
SymVal x706 = SymMemory.loadSym(x704.toInt(), 0);
Stack.push(x705);
SymStack.push(x706);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x707 = Stack.pop();
SymVal x708 = SymStack.pop();
Num x709 = Stack.pop();
SymVal x710 = SymStack.pop();
Num x711 = x709.i32_sub(x707);
Stack.push(x711);
bool x712 = allConcrete(x710, x708);
SymVal x713 = x712 ? Concrete(x711, 32) : x710.minus(x708);
SymStack.push(x713);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x714 = Stack.pop();
SymVal x715 = SymStack.pop();
Num x716 = Stack.pop();
SymVal x717 = SymStack.pop();
Num x718 = x716.i32_mul(x714);
Stack.push(x718);
bool x719 = allConcrete(x717, x715);
SymVal x720 = x719 ? Concrete(x718, 32) : x717.mul(x715);
SymStack.push(x720);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x721 = Stack.pop();
SymVal x722 = SymStack.pop();
Num x723 = Stack.pop();
SymVal x724 = SymStack.pop();
Num x725 = x723.i32_mul(x721);
Stack.push(x725);
bool x726 = allConcrete(x724, x722);
SymVal x727 = x726 ? Concrete(x725, 32) : x724.mul(x722);
SymStack.push(x727);
}
{
Num x728 = Stack.pop();
SymVal x729 = SymStack.pop();
Num x730 = Stack.pop();
SymVal x731 = SymStack.pop();
Num x732 = x730.i32_add(x728);
Stack.push(x732);
bool x733 = allConcrete(x731, x729);
SymVal x734 = x733 ? Concrete(x732, 32) : x731.add(x729);
SymStack.push(x734);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x735 = Stack.pop();
SymVal x736 = SymStack.pop();
Num x737 = Stack.pop();
SymVal x738 = SymStack.pop();
Num x739 = x737.i32_add(x735);
Stack.push(x739);
bool x740 = allConcrete(x738, x736);
SymVal x741 = x740 ? Concrete(x739, 32) : x738.add(x736);
SymStack.push(x741);
}
{
Num x742 = Stack.pop();
SymStack.pop();
Num x743 = I32V(Memory.loadInt(x742.toInt(), 8));
SymVal x744 = SymMemory.loadSym(x742.toInt(), 8);
Stack.push(x743);
SymStack.push(x744);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x745 = Stack.pop();
SymStack.pop();
Num x746 = I32V(Memory.loadInt(x745.toInt(), 0));
SymVal x747 = SymMemory.loadSym(x745.toInt(), 0);
Stack.push(x746);
SymStack.push(x747);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x748 = Stack.pop();
SymVal x749 = SymStack.pop();
Num x750 = Stack.pop();
SymVal x751 = SymStack.pop();
Num x752 = x750.i32_sub(x748);
Stack.push(x752);
bool x753 = allConcrete(x751, x749);
SymVal x754 = x753 ? Concrete(x752, 32) : x751.minus(x749);
SymStack.push(x754);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x755 = Stack.pop();
SymVal x756 = SymStack.pop();
Num x757 = Stack.pop();
SymVal x758 = SymStack.pop();
Num x759 = x757.i32_mul(x755);
Stack.push(x759);
bool x760 = allConcrete(x758, x756);
SymVal x761 = x760 ? Concrete(x759, 32) : x758.mul(x756);
SymStack.push(x761);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x762 = Stack.pop();
SymStack.pop();
Num x763 = I32V(Memory.loadInt(x762.toInt(), 0));
SymVal x764 = SymMemory.loadSym(x762.toInt(), 0);
Stack.push(x763);
SymStack.push(x764);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x765 = Stack.pop();
SymVal x766 = SymStack.pop();
Num x767 = Stack.pop();
SymVal x768 = SymStack.pop();
Num x769 = x767.i32_div_s(x765);
Stack.push(x769);
bool x770 = allConcrete(x768, x766);
SymVal x771 = x770 ? Concrete(x769, 32) : x768.div(x766);
SymStack.push(x771);
}
{
Num x772 = Stack.pop();
SymVal x773 = SymStack.pop();
Num x774 = Stack.pop();
SymVal x775 = SymStack.pop();
Num x776 = x774.i32_add(x772);
Stack.push(x776);
bool x777 = allConcrete(x775, x773);
SymVal x778 = x777 ? Concrete(x776, 32) : x775.add(x773);
SymStack.push(x778);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x779 = Stack.pop();
SymVal x780 = SymStack.pop();
Num x781 = Stack.pop();
SymVal x782 = SymStack.pop();
Num x783 = x781.i32_mul(x779);
Stack.push(x783);
bool x784 = allConcrete(x782, x780);
SymVal x785 = x784 ? Concrete(x783, 32) : x782.mul(x780);
SymStack.push(x785);
}
{
Num x786 = Stack.pop();
SymVal x787 = SymStack.pop();
Num x788 = Stack.pop();
SymVal x789 = SymStack.pop();
Num x790 = x788.i32_add(x786);
Stack.push(x790);
bool x791 = allConcrete(x789, x787);
SymVal x792 = x791 ? Concrete(x790, 32) : x789.add(x787);
SymStack.push(x792);
}
{
Num x793 = Stack.pop();
SymVal x794 = SymStack.pop();
Num x795 = Stack.pop();
SymVal x796 = SymStack.pop();
Num x797 = x795.i32_add(x793);
Stack.push(x797);
bool x798 = allConcrete(x796, x794);
SymVal x799 = x798 ? Concrete(x797, 32) : x796.add(x794);
SymStack.push(x799);
}
{
Num x800 = Stack.pop();
SymStack.pop();
Num x801 = I32V(Memory.loadInt(x800.toInt(), 8));
SymVal x802 = SymMemory.loadSym(x800.toInt(), 8);
Stack.push(x801);
SymStack.push(x802);
}
{
Num x803 = Stack.pop();
SymVal x804 = SymStack.pop();
Num x805 = Stack.pop();
SymStack.pop();
int x806 = x805.toInt();
Memory.storeInt(x806, 8, x803.toInt());
SymMemory.storeSym(x806, 8, x804);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x807 = Stack.pop();
SymVal x808 = SymStack.pop();
Num x809 = Stack.pop();
SymVal x810 = SymStack.pop();
Num x811 = x809.i32_add(x807);
Stack.push(x811);
bool x812 = allConcrete(x810, x808);
SymVal x813 = x812 ? Concrete(x811, 32) : x810.add(x808);
SymStack.push(x813);
}
{
Num x814 = Stack.pop();
SymVal x815 = SymStack.pop();
Frames.set(3, x814);
SymFrames.set(3, x815);
}
info("Jump to 1");
__attribute__((musttail)) return x816(std::monostate{});
return std::monostate{};
}
std::monostate x662(std::monostate x663) {
info("Entering the false branch 9 of the if");
__attribute__((musttail)) return x593(std::monostate{});
return std::monostate{};
}
std::monostate x593(std::monostate x594) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x595 = Stack.pop();
SymStack.pop();
Num x596 = I32V(Memory.loadInt(x595.toInt(), 0));
SymVal x597 = SymMemory.loadSym(x595.toInt(), 0);
Stack.push(x596);
SymStack.push(x597);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x598 = Stack.pop();
SymVal x599 = SymStack.pop();
Num x600 = Stack.pop();
SymVal x601 = SymStack.pop();
Num x602 = x600.i32_sub(x598);
Stack.push(x602);
bool x603 = allConcrete(x601, x599);
SymVal x604 = x603 ? Concrete(x602, 32) : x601.minus(x599);
SymStack.push(x604);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x605 = Stack.pop();
SymVal x606 = SymStack.pop();
Num x607 = Stack.pop();
SymVal x608 = SymStack.pop();
Num x609 = x607.i32_mul(x605);
Stack.push(x609);
bool x610 = allConcrete(x608, x606);
SymVal x611 = x610 ? Concrete(x609, 32) : x608.mul(x606);
SymStack.push(x611);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x612 = Stack.pop();
SymVal x613 = SymStack.pop();
Num x614 = Stack.pop();
SymVal x615 = SymStack.pop();
Num x616 = x614.i32_mul(x612);
Stack.push(x616);
bool x617 = allConcrete(x615, x613);
SymVal x618 = x617 ? Concrete(x616, 32) : x615.mul(x613);
SymStack.push(x618);
}
{
Num x619 = Stack.pop();
SymVal x620 = SymStack.pop();
Num x621 = Stack.pop();
SymVal x622 = SymStack.pop();
Num x623 = x621.i32_add(x619);
Stack.push(x623);
bool x624 = allConcrete(x622, x620);
SymVal x625 = x624 ? Concrete(x623, 32) : x622.add(x620);
SymStack.push(x625);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x626 = Stack.pop();
SymVal x627 = SymStack.pop();
Num x628 = Stack.pop();
SymVal x629 = SymStack.pop();
Num x630 = x628.i32_add(x626);
Stack.push(x630);
bool x631 = allConcrete(x629, x627);
SymVal x632 = x631 ? Concrete(x630, 32) : x629.add(x627);
SymStack.push(x632);
}
{
Num x633 = Stack.pop();
SymStack.pop();
Num x634 = I32V(Memory.loadInt(x633.toInt(), 8));
SymVal x635 = SymMemory.loadSym(x633.toInt(), 8);
Stack.push(x634);
SymStack.push(x635);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x636 = Stack.pop();
SymStack.pop();
Num x637 = I32V(Memory.loadInt(x636.toInt(), 0));
SymVal x638 = SymMemory.loadSym(x636.toInt(), 0);
Stack.push(x637);
SymStack.push(x638);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x639 = Stack.pop();
SymVal x640 = SymStack.pop();
Num x641 = Stack.pop();
SymVal x642 = SymStack.pop();
Num x643 = x641.i32_div_s(x639);
Stack.push(x643);
bool x644 = allConcrete(x642, x640);
SymVal x645 = x644 ? Concrete(x643, 32) : x642.div(x640);
SymStack.push(x645);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x646 = Stack.pop();
SymVal x647 = SymStack.pop();
Num x648 = Stack.pop();
SymVal x649 = SymStack.pop();
Num x650 = x648.i32_sub(x646);
Stack.push(x650);
bool x651 = allConcrete(x649, x647);
SymVal x652 = x651 ? Concrete(x650, 32) : x649.minus(x647);
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
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x657 = Stack.pop();
SymStack.pop();
Num x658 = I32V(Memory.loadInt(x657.toInt(), 4));
SymVal x659 = SymMemory.loadSym(x657.toInt(), 4);
Stack.push(x658);
SymStack.push(x659);
}
{
Num x660 = Stack.pop();
SymVal x661 = SymStack.pop();
Frames.set(3, x660);
SymFrames.set(3, x661);
}
__attribute__((musttail)) return x333(std::monostate{});
return std::monostate{};
}
std::monostate x333(std::monostate x582) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x583 = Stack.pop();
SymVal x584 = SymStack.pop();
Num x585 = Stack.pop();
SymVal x586 = SymStack.pop();
Num x587 = x585.i32_eq(x583);
Stack.push(x587);
bool x588 = allConcrete(x586, x584);
SymVal x589 = x588 ? Concrete(x587, 32) : x586.eq(x584).bool2bv();
SymStack.push(x589);
}
Num x590 = Stack.pop();
{
SymVal x591 = SymStack.pop();
ExploreTree.fillIfElseNode(x591, 10);
}
int x592 = x590.toInt();
if (x592 != 0) {
ExploreTree.moveCursor(true, makeControl(x232, CURRENT_MCONT));
__attribute__((musttail)) return x580(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x580, CURRENT_MCONT));
__attribute__((musttail)) return x232(std::monostate{});
}
return std::monostate{};
}
std::monostate x580(std::monostate x581) {
info("Entering the true branch 10 of the if");
info("Jump to 2");
__attribute__((musttail)) return x517(std::monostate{});
return std::monostate{};
}
std::monostate x517(std::monostate x518) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x519 = Stack.pop();
SymStack.pop();
Num x520 = I32V(Memory.loadInt(x519.toInt(), 0));
SymVal x521 = SymMemory.loadSym(x519.toInt(), 0);
Stack.push(x520);
SymStack.push(x521);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x522 = Stack.pop();
SymVal x523 = SymStack.pop();
Num x524 = Stack.pop();
SymVal x525 = SymStack.pop();
Num x526 = x524.i32_sub(x522);
Stack.push(x526);
bool x527 = allConcrete(x525, x523);
SymVal x528 = x527 ? Concrete(x526, 32) : x525.minus(x523);
SymStack.push(x528);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x529 = Stack.pop();
SymVal x530 = SymStack.pop();
Num x531 = Stack.pop();
SymVal x532 = SymStack.pop();
Num x533 = x531.i32_mul(x529);
Stack.push(x533);
bool x534 = allConcrete(x532, x530);
SymVal x535 = x534 ? Concrete(x533, 32) : x532.mul(x530);
SymStack.push(x535);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x536 = Stack.pop();
SymVal x537 = SymStack.pop();
Num x538 = Stack.pop();
SymVal x539 = SymStack.pop();
Num x540 = x538.i32_add(x536);
Stack.push(x540);
bool x541 = allConcrete(x539, x537);
SymVal x542 = x541 ? Concrete(x540, 32) : x539.add(x537);
SymStack.push(x542);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x543 = Stack.pop();
SymVal x544 = SymStack.pop();
Num x545 = Stack.pop();
SymVal x546 = SymStack.pop();
Num x547 = x545.i32_mul(x543);
Stack.push(x547);
bool x548 = allConcrete(x546, x544);
SymVal x549 = x548 ? Concrete(x547, 32) : x546.mul(x544);
SymStack.push(x549);
}
{
Num x550 = Stack.pop();
SymVal x551 = SymStack.pop();
Num x552 = Stack.pop();
SymVal x553 = SymStack.pop();
Num x554 = x552.i32_add(x550);
Stack.push(x554);
bool x555 = allConcrete(x553, x551);
SymVal x556 = x555 ? Concrete(x554, 32) : x553.add(x551);
SymStack.push(x556);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x557 = Stack.pop();
SymVal x558 = SymStack.pop();
Num x559 = Stack.pop();
SymVal x560 = SymStack.pop();
Num x561 = x559.i32_add(x557);
Stack.push(x561);
bool x562 = allConcrete(x560, x558);
SymVal x563 = x562 ? Concrete(x561, 32) : x560.add(x558);
SymStack.push(x563);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x564 = Stack.pop();
SymVal x565 = SymStack.pop();
Num x566 = Stack.pop();
SymStack.pop();
int x567 = x566.toInt();
Memory.storeInt(x567, 8, x564.toInt());
SymMemory.storeSym(x567, 8, x565);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x568 = Stack.pop();
SymStack.pop();
Num x569 = I32V(Memory.loadInt(x568.toInt(), 4));
SymVal x570 = SymMemory.loadSym(x568.toInt(), 4);
Stack.push(x569);
SymStack.push(x570);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x571 = Stack.pop();
SymVal x572 = SymStack.pop();
Num x573 = Stack.pop();
SymVal x574 = SymStack.pop();
Num x575 = x573.i32_sub(x571);
Stack.push(x575);
bool x576 = allConcrete(x574, x572);
SymVal x577 = x576 ? Concrete(x575, 32) : x574.minus(x572);
SymStack.push(x577);
}
{
Num x578 = Stack.pop();
SymVal x579 = SymStack.pop();
Frames.set(3, x578);
SymFrames.set(3, x579);
}
__attribute__((musttail)) return x387(std::monostate{});
return std::monostate{};
}
std::monostate x387(std::monostate x499) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x500 = Stack.pop();
SymVal x501 = SymStack.pop();
Num x502 = Stack.pop();
SymVal x503 = SymStack.pop();
Num x504 = x502.i32_sub(x500);
Stack.push(x504);
bool x505 = allConcrete(x503, x501);
SymVal x506 = x505 ? Concrete(x504, 32) : x503.minus(x501);
SymStack.push(x506);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x507 = Stack.pop();
SymVal x508 = SymStack.pop();
Num x509 = Stack.pop();
SymVal x510 = SymStack.pop();
Num x511 = x509.i32_eq(x507);
Stack.push(x511);
bool x512 = allConcrete(x510, x508);
SymVal x513 = x512 ? Concrete(x511, 32) : x510.eq(x508).bool2bv();
SymStack.push(x513);
}
Num x514 = Stack.pop();
{
SymVal x515 = SymStack.pop();
ExploreTree.fillIfElseNode(x515, 11);
}
int x516 = x514.toInt();
if (x516 != 0) {
ExploreTree.moveCursor(true, makeControl(x334, CURRENT_MCONT));
__attribute__((musttail)) return x497(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x497, CURRENT_MCONT));
__attribute__((musttail)) return x334(std::monostate{});
}
return std::monostate{};
}
std::monostate x497(std::monostate x498) {
info("Entering the true branch 11 of the if");
info("Jump to 2");
__attribute__((musttail)) return x388(std::monostate{});
return std::monostate{};
}
std::monostate x388(std::monostate x389) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x390 = Stack.pop();
SymVal x391 = SymStack.pop();
Num x392 = Stack.pop();
SymVal x393 = SymStack.pop();
Num x394 = x392.i32_mul(x390);
Stack.push(x394);
bool x395 = allConcrete(x393, x391);
SymVal x396 = x395 ? Concrete(x394, 32) : x393.mul(x391);
SymStack.push(x396);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x397 = Stack.pop();
SymVal x398 = SymStack.pop();
Num x399 = Stack.pop();
SymVal x400 = SymStack.pop();
Num x401 = x399.i32_add(x397);
Stack.push(x401);
bool x402 = allConcrete(x400, x398);
SymVal x403 = x402 ? Concrete(x401, 32) : x400.add(x398);
SymStack.push(x403);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x404 = Stack.pop();
SymStack.pop();
Num x405 = I32V(Memory.loadInt(x404.toInt(), 0));
SymVal x406 = SymMemory.loadSym(x404.toInt(), 0);
Stack.push(x405);
SymStack.push(x406);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x407 = Stack.pop();
SymVal x408 = SymStack.pop();
Num x409 = Stack.pop();
SymVal x410 = SymStack.pop();
Num x411 = x409.i32_div_s(x407);
Stack.push(x411);
bool x412 = allConcrete(x410, x408);
SymVal x413 = x412 ? Concrete(x411, 32) : x410.div(x408);
SymStack.push(x413);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x414 = Stack.pop();
SymVal x415 = SymStack.pop();
Num x416 = Stack.pop();
SymVal x417 = SymStack.pop();
Num x418 = x416.i32_sub(x414);
Stack.push(x418);
bool x419 = allConcrete(x417, x415);
SymVal x420 = x419 ? Concrete(x418, 32) : x417.minus(x415);
SymStack.push(x420);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x421 = Stack.pop();
SymVal x422 = SymStack.pop();
Num x423 = Stack.pop();
SymVal x424 = SymStack.pop();
Num x425 = x423.i32_mul(x421);
Stack.push(x425);
bool x426 = allConcrete(x424, x422);
SymVal x427 = x426 ? Concrete(x425, 32) : x424.mul(x422);
SymStack.push(x427);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x428 = Stack.pop();
SymStack.pop();
Num x429 = I32V(Memory.loadInt(x428.toInt(), 0));
SymVal x430 = SymMemory.loadSym(x428.toInt(), 0);
Stack.push(x429);
SymStack.push(x430);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x431 = Stack.pop();
SymVal x432 = SymStack.pop();
Num x433 = Stack.pop();
SymVal x434 = SymStack.pop();
Num x435 = x433.i32_sub(x431);
Stack.push(x435);
bool x436 = allConcrete(x434, x432);
SymVal x437 = x436 ? Concrete(x435, 32) : x434.minus(x432);
SymStack.push(x437);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x438 = Stack.pop();
SymVal x439 = SymStack.pop();
Num x440 = Stack.pop();
SymVal x441 = SymStack.pop();
Num x442 = x440.i32_mul(x438);
Stack.push(x442);
bool x443 = allConcrete(x441, x439);
SymVal x444 = x443 ? Concrete(x442, 32) : x441.mul(x439);
SymStack.push(x444);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x445 = Stack.pop();
SymVal x446 = SymStack.pop();
Num x447 = Stack.pop();
SymVal x448 = SymStack.pop();
Num x449 = x447.i32_mul(x445);
Stack.push(x449);
bool x450 = allConcrete(x448, x446);
SymVal x451 = x450 ? Concrete(x449, 32) : x448.mul(x446);
SymStack.push(x451);
}
{
Num x452 = Stack.pop();
SymVal x453 = SymStack.pop();
Num x454 = Stack.pop();
SymVal x455 = SymStack.pop();
Num x456 = x454.i32_add(x452);
Stack.push(x456);
bool x457 = allConcrete(x455, x453);
SymVal x458 = x457 ? Concrete(x456, 32) : x455.add(x453);
SymStack.push(x458);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x459 = Stack.pop();
SymVal x460 = SymStack.pop();
Num x461 = Stack.pop();
SymVal x462 = SymStack.pop();
Num x463 = x461.i32_add(x459);
Stack.push(x463);
bool x464 = allConcrete(x462, x460);
SymVal x465 = x464 ? Concrete(x463, 32) : x462.add(x460);
SymStack.push(x465);
}
{
Num x466 = Stack.pop();
SymStack.pop();
Num x467 = I32V(Memory.loadInt(x466.toInt(), 8));
SymVal x468 = SymMemory.loadSym(x466.toInt(), 8);
Stack.push(x467);
SymStack.push(x468);
}
{
Num x469 = Stack.pop();
SymVal x470 = SymStack.pop();
Num x471 = Stack.pop();
SymVal x472 = SymStack.pop();
Num x473 = x471.i32_add(x469);
Stack.push(x473);
bool x474 = allConcrete(x472, x470);
SymVal x475 = x474 ? Concrete(x473, 32) : x472.add(x470);
SymStack.push(x475);
}
{
Num x476 = Stack.pop();
SymStack.pop();
Num x477 = I32V(Memory.loadInt(x476.toInt(), 8));
SymVal x478 = SymMemory.loadSym(x476.toInt(), 8);
Stack.push(x477);
SymStack.push(x478);
}
{
Num x479 = Stack.pop();
SymVal x480 = SymStack.pop();
Num x481 = Stack.pop();
SymStack.pop();
int x482 = x481.toInt();
Memory.storeInt(x482, 8, x479.toInt());
SymMemory.storeSym(x482, 8, x480);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x483 = Stack.pop();
SymStack.pop();
Num x484 = I32V(Memory.loadInt(x483.toInt(), 4));
SymVal x485 = SymMemory.loadSym(x483.toInt(), 4);
Stack.push(x484);
SymStack.push(x485);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x486 = Stack.pop();
SymVal x487 = SymStack.pop();
Num x488 = Stack.pop();
SymVal x489 = SymStack.pop();
Num x490 = x488.i32_add(x486);
Stack.push(x490);
bool x491 = allConcrete(x489, x487);
SymVal x492 = x491 ? Concrete(x490, 32) : x489.add(x487);
SymStack.push(x492);
}
{
Num x493 = Stack.pop();
SymVal x494 = SymStack.pop();
Num x495 = Stack.pop();
SymStack.pop();
int x496 = x495.toInt();
Memory.storeInt(x496, 4, x493.toInt());
SymMemory.storeSym(x496, 4, x494);
}
__attribute__((musttail)) return x123(std::monostate{});
return std::monostate{};
}
std::monostate x334(std::monostate x335) {
info("Entering the false branch 11 of the if");
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x336 = Stack.pop();
SymVal x337 = SymStack.pop();
Num x338 = Stack.pop();
SymVal x339 = SymStack.pop();
Num x340 = x338.i32_add(x336);
Stack.push(x340);
bool x341 = allConcrete(x339, x337);
SymVal x342 = x341 ? Concrete(x340, 32) : x339.add(x337);
SymStack.push(x342);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x343 = Stack.pop();
SymVal x344 = SymStack.pop();
Num x345 = Stack.pop();
SymVal x346 = SymStack.pop();
Num x347 = x345.i32_mul(x343);
Stack.push(x347);
bool x348 = allConcrete(x346, x344);
SymVal x349 = x348 ? Concrete(x347, 32) : x346.mul(x344);
SymStack.push(x349);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x350 = Stack.pop();
SymVal x351 = SymStack.pop();
Num x352 = Stack.pop();
SymVal x353 = SymStack.pop();
Num x354 = x352.i32_add(x350);
Stack.push(x354);
bool x355 = allConcrete(x353, x351);
SymVal x356 = x355 ? Concrete(x354, 32) : x353.add(x351);
SymStack.push(x356);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x357 = Stack.pop();
SymVal x358 = SymStack.pop();
Num x359 = Stack.pop();
SymVal x360 = SymStack.pop();
Num x361 = x359.i32_mul(x357);
Stack.push(x361);
bool x362 = allConcrete(x360, x358);
SymVal x363 = x362 ? Concrete(x361, 32) : x360.mul(x358);
SymStack.push(x363);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x364 = Stack.pop();
SymVal x365 = SymStack.pop();
Num x366 = Stack.pop();
SymVal x367 = SymStack.pop();
Num x368 = x366.i32_add(x364);
Stack.push(x368);
bool x369 = allConcrete(x367, x365);
SymVal x370 = x369 ? Concrete(x368, 32) : x367.add(x365);
SymStack.push(x370);
}
{
Num x371 = Stack.pop();
SymStack.pop();
Num x372 = I32V(Memory.loadInt(x371.toInt(), 8));
SymVal x373 = SymMemory.loadSym(x371.toInt(), 8);
Stack.push(x372);
SymStack.push(x373);
}
{
Num x374 = Stack.pop();
SymVal x375 = SymStack.pop();
Num x376 = Stack.pop();
SymStack.pop();
int x377 = x376.toInt();
Memory.storeInt(x377, 8, x374.toInt());
SymMemory.storeSym(x377, 8, x375);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x378 = Stack.pop();
SymVal x379 = SymStack.pop();
Num x380 = Stack.pop();
SymVal x381 = SymStack.pop();
Num x382 = x380.i32_sub(x378);
Stack.push(x382);
bool x383 = allConcrete(x381, x379);
SymVal x384 = x383 ? Concrete(x382, 32) : x381.minus(x379);
SymStack.push(x384);
}
{
Num x385 = Stack.pop();
SymVal x386 = SymStack.pop();
Frames.set(3, x385);
SymFrames.set(3, x386);
}
info("Jump to 1");
__attribute__((musttail)) return x387(std::monostate{});
return std::monostate{};
}
std::monostate x232(std::monostate x233) {
info("Entering the false branch 10 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x234 = Stack.pop();
SymStack.pop();
Num x235 = I32V(Memory.loadInt(x234.toInt(), 0));
SymVal x236 = SymMemory.loadSym(x234.toInt(), 0);
Stack.push(x235);
SymStack.push(x236);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x237 = Stack.pop();
SymVal x238 = SymStack.pop();
Num x239 = Stack.pop();
SymVal x240 = SymStack.pop();
Num x241 = x239.i32_sub(x237);
Stack.push(x241);
bool x242 = allConcrete(x240, x238);
SymVal x243 = x242 ? Concrete(x241, 32) : x240.minus(x238);
SymStack.push(x243);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x244 = Stack.pop();
SymVal x245 = SymStack.pop();
Num x246 = Stack.pop();
SymVal x247 = SymStack.pop();
Num x248 = x246.i32_mul(x244);
Stack.push(x248);
bool x249 = allConcrete(x247, x245);
SymVal x250 = x249 ? Concrete(x248, 32) : x247.mul(x245);
SymStack.push(x250);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x251 = Stack.pop();
SymVal x252 = SymStack.pop();
Num x253 = Stack.pop();
SymVal x254 = SymStack.pop();
Num x255 = x253.i32_add(x251);
Stack.push(x255);
bool x256 = allConcrete(x254, x252);
SymVal x257 = x256 ? Concrete(x255, 32) : x254.add(x252);
SymStack.push(x257);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x258 = Stack.pop();
SymVal x259 = SymStack.pop();
Num x260 = Stack.pop();
SymVal x261 = SymStack.pop();
Num x262 = x260.i32_mul(x258);
Stack.push(x262);
bool x263 = allConcrete(x261, x259);
SymVal x264 = x263 ? Concrete(x262, 32) : x261.mul(x259);
SymStack.push(x264);
}
{
Num x265 = Stack.pop();
SymVal x266 = SymStack.pop();
Num x267 = Stack.pop();
SymVal x268 = SymStack.pop();
Num x269 = x267.i32_add(x265);
Stack.push(x269);
bool x270 = allConcrete(x268, x266);
SymVal x271 = x270 ? Concrete(x269, 32) : x268.add(x266);
SymStack.push(x271);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
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
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x279 = Stack.pop();
SymStack.pop();
Num x280 = I32V(Memory.loadInt(x279.toInt(), 0));
SymVal x281 = SymMemory.loadSym(x279.toInt(), 0);
Stack.push(x280);
SymStack.push(x281);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x282 = Stack.pop();
SymVal x283 = SymStack.pop();
Num x284 = Stack.pop();
SymVal x285 = SymStack.pop();
Num x286 = x284.i32_sub(x282);
Stack.push(x286);
bool x287 = allConcrete(x285, x283);
SymVal x288 = x287 ? Concrete(x286, 32) : x285.minus(x283);
SymStack.push(x288);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x289 = Stack.pop();
SymVal x290 = SymStack.pop();
Num x291 = Stack.pop();
SymVal x292 = SymStack.pop();
Num x293 = x291.i32_mul(x289);
Stack.push(x293);
bool x294 = allConcrete(x292, x290);
SymVal x295 = x294 ? Concrete(x293, 32) : x292.mul(x290);
SymStack.push(x295);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x296 = Stack.pop();
SymVal x297 = SymStack.pop();
Num x298 = Stack.pop();
SymVal x299 = SymStack.pop();
Num x300 = x298.i32_mul(x296);
Stack.push(x300);
bool x301 = allConcrete(x299, x297);
SymVal x302 = x301 ? Concrete(x300, 32) : x299.mul(x297);
SymStack.push(x302);
}
{
Num x303 = Stack.pop();
SymVal x304 = SymStack.pop();
Num x305 = Stack.pop();
SymVal x306 = SymStack.pop();
Num x307 = x305.i32_add(x303);
Stack.push(x307);
bool x308 = allConcrete(x306, x304);
SymVal x309 = x308 ? Concrete(x307, 32) : x306.add(x304);
SymStack.push(x309);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x310 = Stack.pop();
SymVal x311 = SymStack.pop();
Num x312 = Stack.pop();
SymVal x313 = SymStack.pop();
Num x314 = x312.i32_add(x310);
Stack.push(x314);
bool x315 = allConcrete(x313, x311);
SymVal x316 = x315 ? Concrete(x314, 32) : x313.add(x311);
SymStack.push(x316);
}
{
Num x317 = Stack.pop();
SymStack.pop();
Num x318 = I32V(Memory.loadInt(x317.toInt(), 8));
SymVal x319 = SymMemory.loadSym(x317.toInt(), 8);
Stack.push(x318);
SymStack.push(x319);
}
{
Num x320 = Stack.pop();
SymVal x321 = SymStack.pop();
Num x322 = Stack.pop();
SymStack.pop();
int x323 = x322.toInt();
Memory.storeInt(x323, 8, x320.toInt());
SymMemory.storeSym(x323, 8, x321);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x324 = Stack.pop();
SymVal x325 = SymStack.pop();
Num x326 = Stack.pop();
SymVal x327 = SymStack.pop();
Num x328 = x326.i32_sub(x324);
Stack.push(x328);
bool x329 = allConcrete(x327, x325);
SymVal x330 = x329 ? Concrete(x328, 32) : x327.minus(x325);
SymStack.push(x330);
}
{
Num x331 = Stack.pop();
SymVal x332 = SymStack.pop();
Frames.set(3, x331);
SymFrames.set(3, x332);
}
info("Jump to 1");
__attribute__((musttail)) return x333(std::monostate{});
return std::monostate{};
}
std::monostate x127(std::monostate x128) {
info("Entering the false branch 8 of the if");
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x129 = Stack.pop();
SymVal x130 = SymStack.pop();
Num x131 = Stack.pop();
SymVal x132 = SymStack.pop();
Num x133 = x131.i32_mul(x129);
Stack.push(x133);
bool x134 = allConcrete(x132, x130);
SymVal x135 = x134 ? Concrete(x133, 32) : x132.mul(x130);
SymStack.push(x135);
}
{
Num x136 = Stack.pop();
SymVal x137 = SymStack.pop();
Num x138 = Stack.pop();
SymVal x139 = SymStack.pop();
Num x140 = x138.i32_add(x136);
Stack.push(x140);
bool x141 = allConcrete(x139, x137);
SymVal x142 = x141 ? Concrete(x140, 32) : x139.add(x137);
SymStack.push(x142);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x143 = Stack.pop();
SymStack.pop();
Num x144 = I32V(Memory.loadInt(x143.toInt(), 0));
SymVal x145 = SymMemory.loadSym(x143.toInt(), 0);
Stack.push(x144);
SymStack.push(x145);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
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
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x153 = Stack.pop();
SymVal x154 = SymStack.pop();
Num x155 = Stack.pop();
SymVal x156 = SymStack.pop();
Num x157 = x155.i32_mul(x153);
Stack.push(x157);
bool x158 = allConcrete(x156, x154);
SymVal x159 = x158 ? Concrete(x157, 32) : x156.mul(x154);
SymStack.push(x159);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x160 = Stack.pop();
SymVal x161 = SymStack.pop();
Num x162 = Stack.pop();
SymVal x163 = SymStack.pop();
Num x164 = x162.i32_mul(x160);
Stack.push(x164);
bool x165 = allConcrete(x163, x161);
SymVal x166 = x165 ? Concrete(x164, 32) : x163.mul(x161);
SymStack.push(x166);
}
{
Num x167 = Stack.pop();
SymVal x168 = SymStack.pop();
Num x169 = Stack.pop();
SymVal x170 = SymStack.pop();
Num x171 = x169.i32_add(x167);
Stack.push(x171);
bool x172 = allConcrete(x170, x168);
SymVal x173 = x172 ? Concrete(x171, 32) : x170.add(x168);
SymStack.push(x173);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x174 = Stack.pop();
SymVal x175 = SymStack.pop();
Num x176 = Stack.pop();
SymVal x177 = SymStack.pop();
Num x178 = x176.i32_add(x174);
Stack.push(x178);
bool x179 = allConcrete(x177, x175);
SymVal x180 = x179 ? Concrete(x178, 32) : x177.add(x175);
SymStack.push(x180);
}
{
Num x181 = Stack.pop();
SymStack.pop();
Num x182 = I32V(Memory.loadInt(x181.toInt(), 8));
SymVal x183 = SymMemory.loadSym(x181.toInt(), 8);
Stack.push(x182);
SymStack.push(x183);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x184 = Stack.pop();
SymStack.pop();
Num x185 = I32V(Memory.loadInt(x184.toInt(), 0));
SymVal x186 = SymMemory.loadSym(x184.toInt(), 0);
Stack.push(x185);
SymStack.push(x186);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x187 = Stack.pop();
SymVal x188 = SymStack.pop();
Num x189 = Stack.pop();
SymVal x190 = SymStack.pop();
Num x191 = x189.i32_div_s(x187);
Stack.push(x191);
bool x192 = allConcrete(x190, x188);
SymVal x193 = x192 ? Concrete(x191, 32) : x190.div(x188);
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
{
Num x201 = Stack.pop();
SymVal x202 = SymStack.pop();
Num x203 = Stack.pop();
SymVal x204 = SymStack.pop();
Num x205 = x203.i32_mul(x201);
Stack.push(x205);
bool x206 = allConcrete(x204, x202);
SymVal x207 = x206 ? Concrete(x205, 32) : x204.mul(x202);
SymStack.push(x207);
}
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
SymStack.pop();
Num x216 = I32V(Memory.loadInt(x215.toInt(), 8));
SymVal x217 = SymMemory.loadSym(x215.toInt(), 8);
Stack.push(x216);
SymStack.push(x217);
}
{
Num x218 = Stack.pop();
SymVal x219 = SymStack.pop();
Num x220 = Stack.pop();
SymStack.pop();
int x221 = x220.toInt();
Memory.storeInt(x221, 8, x218.toInt());
SymMemory.storeSym(x221, 8, x219);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
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
Frames.set(3, x229);
SymFrames.set(3, x230);
}
info("Jump to 1");
__attribute__((musttail)) return x231(std::monostate{});
return std::monostate{};
}
std::monostate x125(std::monostate x126) {
info("Entering the false branch 6 of the if");
__attribute__((musttail)) return x123(std::monostate{});
return std::monostate{};
}
std::monostate x123(std::monostate x124) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x121(std::monostate{});
return std::monostate{};
}
std::monostate x121(std::monostate x122) {
infoWhen("CALL", "Exiting the function at 9, stackSize =", Stack.size());
Frames.popFrameCallee(2);
SymFrames.popFrameCallee(2);
return enterCC(std::monostate());
}
std::monostate x119(std::monostate x120) {
info("Entering the false branch 5 of the if");
__attribute__((musttail)) return x71(std::monostate{});
return std::monostate{};
}
std::monostate x71(std::monostate x72) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x73 = Stack.pop();
SymStack.pop();
Num x74 = I32V(Memory.loadInt(x73.toInt(), 0));
SymVal x75 = SymMemory.loadSym(x73.toInt(), 0);
Stack.push(x74);
SymStack.push(x75);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x76 = Stack.pop();
SymVal x77 = SymStack.pop();
Num x78 = Stack.pop();
SymVal x79 = SymStack.pop();
Num x80 = x78.i32_sub(x76);
Stack.push(x80);
bool x81 = allConcrete(x79, x77);
SymVal x82 = x81 ? Concrete(x80, 32) : x79.minus(x77);
SymStack.push(x82);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x83 = Stack.pop();
SymVal x84 = SymStack.pop();
Num x85 = Stack.pop();
SymVal x86 = SymStack.pop();
Num x87 = x85.i32_mul(x83);
Stack.push(x87);
bool x88 = allConcrete(x86, x84);
SymVal x89 = x88 ? Concrete(x87, 32) : x86.mul(x84);
SymStack.push(x89);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x90 = Stack.pop();
SymVal x91 = SymStack.pop();
Num x92 = Stack.pop();
SymVal x93 = SymStack.pop();
Num x94 = x92.i32_mul(x90);
Stack.push(x94);
bool x95 = allConcrete(x93, x91);
SymVal x96 = x95 ? Concrete(x94, 32) : x93.mul(x91);
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
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x104 = Stack.pop();
SymVal x105 = SymStack.pop();
Num x106 = Stack.pop();
SymVal x107 = SymStack.pop();
Num x108 = x106.i32_add(x104);
Stack.push(x108);
bool x109 = allConcrete(x107, x105);
SymVal x110 = x109 ? Concrete(x108, 32) : x107.add(x105);
SymStack.push(x110);
}
{
Num x111 = Stack.pop();
SymStack.pop();
Num x112 = I32V(Memory.loadInt(x111.toInt(), 8));
SymVal x113 = SymMemory.loadSym(x111.toInt(), 8);
Stack.push(x112);
SymStack.push(x113);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 10);
Num x114 = Stack.pop();
Num x115 = Stack.pop();
SymVal x116 = SymStack.pop();
SymVal x117 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x115);
Frames.set(1, x114);
SymFrames.set(0, x117);
SymFrames.set(1, x116);
updateCurrentMCont(prependCont(x69, CURRENT_MCONT));
}
__attribute__((musttail)) return x118(std::monostate{});
return std::monostate{};
}
std::monostate x69(std::monostate x70) {
infoWhen("CALL", "Returning from the function at 10, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
__attribute__((musttail)) return x67(std::monostate{});
return std::monostate{};
}
std::monostate x67(std::monostate x68) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x65(std::monostate{});
return std::monostate{};
}
std::monostate x65(std::monostate x66) {
infoWhen("CALL", "Exiting the function at 10, stackSize =", Stack.size());
Frames.popFrameCallee(1);
SymFrames.popFrameCallee(1);
return enterCC(std::monostate());
}
std::monostate x36(std::monostate x37) {
infoWhen("CALL", "Entered the function at 7, stackSize =", Stack.size());
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x38 = Stack.pop();
SymVal x39 = SymStack.pop();
Num x40 = Stack.pop();
SymStack.pop();
int x41 = x40.toInt();
Memory.storeInt(x41, 0, x38.toInt());
SymMemory.storeSym(x41, 0, x39);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x42 = Stack.pop();
SymVal x43 = SymStack.pop();
Num x44 = Stack.pop();
SymStack.pop();
int x45 = x44.toInt();
Memory.storeInt(x45, 4, x42.toInt());
SymMemory.storeSym(x45, 4, x43);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
Stack.push(I32V(65536));
SymStack.push(Concrete(I32V(65536), 32));
{
Num x46 = Stack.pop();
SymVal x47 = SymStack.pop();
Num x48 = Stack.pop();
SymStack.pop();
int x49 = x48.toInt();
Memory.storeInt(x49, 8, x46.toInt());
SymMemory.storeSym(x49, 8, x47);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x50 = Stack.pop();
SymStack.pop();
Num x51 = I32V(Memory.grow(x50.toInt()));
SymVal x52 = Concrete(x51, 32);
Stack.push(x51);
SymStack.push(x52);
}
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x53 = Stack.pop();
SymVal x54 = SymStack.pop();
Num x55 = Stack.pop();
SymVal x56 = SymStack.pop();
Num x57 = x55.i32_ne(x53);
Stack.push(x57);
bool x58 = allConcrete(x56, x54);
SymVal x59 = x58 ? Concrete(x57, 32) : x56.neq(x54).bool2bv();
SymStack.push(x59);
}
Num x60 = Stack.pop();
{
SymVal x61 = SymStack.pop();
ExploreTree.fillIfElseNode(x61, 0);
}
int x62 = x60.toInt();
if (x62 != 0) {
ExploreTree.moveCursor(true, makeControl(x24, CURRENT_MCONT));
__attribute__((musttail)) return x26(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x26, CURRENT_MCONT));
__attribute__((musttail)) return x24(std::monostate{});
}
return std::monostate{};
}
std::monostate x26(std::monostate x27) {
info("Entering the true branch 0 of the if");
Stack.push(I32V(65536));
SymStack.push(Concrete(I32V(65536), 32));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x28 = Stack.pop();
SymVal x29 = SymStack.pop();
Num x30 = Stack.pop();
SymStack.pop();
int x31 = x30.toInt();
Memory.storeInt(x31, 0, x28.toInt());
SymMemory.storeSym(x31, 0, x29);
}
Stack.push(I32V(65536));
SymStack.push(Concrete(I32V(65536), 32));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x32 = Stack.pop();
SymVal x33 = SymStack.pop();
Num x34 = Stack.pop();
SymStack.pop();
int x35 = x34.toInt();
Memory.storeInt(x35, 4, x32.toInt());
SymMemory.storeSym(x35, 4, x33);
}
__attribute__((musttail)) return x22(std::monostate{});
return std::monostate{};
}
std::monostate x24(std::monostate x25) {
info("Entering the false branch 0 of the if");
__attribute__((musttail)) return x22(std::monostate{});
return std::monostate{};
}
std::monostate x22(std::monostate x23) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(I32V(65536));
SymStack.push(Concrete(I32V(65536), 32));
__attribute__((musttail)) return x20(std::monostate{});
return std::monostate{};
}
std::monostate x20(std::monostate x21) {
infoWhen("CALL", "Exiting the function at 7, stackSize =", Stack.size());
Frames.popFrameCallee(0);
SymFrames.popFrameCallee(0);
return enterCC(std::monostate());
}
std::monostate x18(std::monostate x19) {
info("Exiting the entry function");
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
return enterCC(std::monostate());
}
std::monostate x15(std::monostate x16) {
info("Initializing memory...");
updateCurrentMCont(MCont_t(x9));
x13(std::monostate{});
Num x17 = Stack.pop();
SymStack.pop();
memoryInitialize(x17.toInt(), "a\x00b\x00c\x00d\x00e\x00f\x00g\x00h\x00i\x00j\x00");
return std::monostate{};
}
std::monostate x13(std::monostate x14) {
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
__attribute__((musttail)) return x11(std::monostate{});
return std::monostate{};
}
std::monostate x11(std::monostate x12) {
return enterCC(std::monostate());
}
std::monostate x9(std::monostate x10) {
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
Globals.pushFrameCaller(0);
SymGlobals.pushFramePtr();
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
x15(std::monostate{});
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
{
updateCurrentMCont(prependCont(x18, MCont_t(x1)));
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 7);
Num x63 = Stack.pop();
SymVal x64 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x63);
SymFrames.set(0, x64);
updateCurrentMCont(prependCont(x7744, CURRENT_MCONT));
}
__attribute__((musttail)) return x36(std::monostate{});
return std::monostate{};
}

/*****************************************
End of Generated Code
*******************************************/
int main(int argc, char *argv[]) {
  start_concolic_execution_with(Snippet, 58);
  return 0;
}
