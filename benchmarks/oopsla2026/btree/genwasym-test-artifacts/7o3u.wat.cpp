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
std::monostate x519(std::monostate);
std::monostate x582(std::monostate);
std::monostate x333(std::monostate);
std::monostate x595(std::monostate);
std::monostate x597(std::monostate);
std::monostate x666(std::monostate);
std::monostate x668(std::monostate);
std::monostate x821(std::monostate);
std::monostate x823(std::monostate);
std::monostate x820(std::monostate);
std::monostate x846(std::monostate);
std::monostate x848(std::monostate);
std::monostate x850(std::monostate);
std::monostate x908(std::monostate);
std::monostate x231(std::monostate);
std::monostate x938(std::monostate);
std::monostate x940(std::monostate);
std::monostate x965(std::monostate);
std::monostate x971(std::monostate);
std::monostate x977(std::monostate);
std::monostate x1066(std::monostate);
std::monostate x1081(std::monostate);
std::monostate x1083(std::monostate);
std::monostate x1085(std::monostate);
std::monostate x1096(std::monostate);
std::monostate x1125(std::monostate);
std::monostate x1131(std::monostate);
std::monostate x1206(std::monostate);
std::monostate x1208(std::monostate);
std::monostate x1210(std::monostate);
std::monostate x1222(std::monostate);
std::monostate x1221(std::monostate);
std::monostate x1262(std::monostate);
std::monostate x1264(std::monostate);
std::monostate x1266(std::monostate);
std::monostate x1307(std::monostate);
std::monostate x1309(std::monostate);
std::monostate x1363(std::monostate);
std::monostate x1382(std::monostate);
std::monostate x1384(std::monostate);
std::monostate x1362(std::monostate);
std::monostate x1421(std::monostate);
std::monostate x1423(std::monostate);
std::monostate x118(std::monostate);
std::monostate x1451(std::monostate);
std::monostate x1453(std::monostate);
std::monostate x1455(std::monostate);
std::monostate x1457(std::monostate);
std::monostate x1463(std::monostate);
std::monostate x1465(std::monostate);
std::monostate x1467(std::monostate);
std::monostate x1469(std::monostate);
std::monostate x1471(std::monostate);
std::monostate x1473(std::monostate);
std::monostate x1475(std::monostate);
std::monostate x1477(std::monostate);
std::monostate x1479(std::monostate);
std::monostate x1485(std::monostate);
std::monostate x1578(std::monostate);
std::monostate x1593(std::monostate);
std::monostate x1623(std::monostate);
std::monostate x1625(std::monostate);
std::monostate x1627(std::monostate);
std::monostate x1629(std::monostate);
std::monostate x1631(std::monostate);
std::monostate x1679(std::monostate);
std::monostate x1681(std::monostate);
std::monostate x1683(std::monostate);
std::monostate x1685(std::monostate);
std::monostate x1687(std::monostate);
std::monostate x1702(std::monostate);
std::monostate x1704(std::monostate);
std::monostate x1706(std::monostate);
std::monostate x1708(std::monostate);
std::monostate x1755(std::monostate);
std::monostate x1757(std::monostate);
std::monostate x1759(std::monostate);
std::monostate x1761(std::monostate);
std::monostate x1776(std::monostate);
std::monostate x1799(std::monostate);
std::monostate x1828(std::monostate);
std::monostate x1850(std::monostate);
std::monostate x1852(std::monostate);
std::monostate x1854(std::monostate);
std::monostate x1866(std::monostate);
std::monostate x1865(std::monostate);
std::monostate x1916(std::monostate);
std::monostate x1678(std::monostate);
std::monostate x1921(std::monostate);
std::monostate x1923(std::monostate);
std::monostate x1928(std::monostate);
std::monostate x1930(std::monostate);
std::monostate x1980(std::monostate);
std::monostate x1998(std::monostate);
std::monostate x2000(std::monostate);
std::monostate x2002(std::monostate);
std::monostate x2004(std::monostate);
std::monostate x2011(std::monostate);
std::monostate x2013(std::monostate);
std::monostate x2067(std::monostate);
std::monostate x2079(std::monostate);
std::monostate x2081(std::monostate);
std::monostate x2083(std::monostate);
std::monostate x2085(std::monostate);
std::monostate x2087(std::monostate);
std::monostate x2089(std::monostate);
std::monostate x2249(std::monostate);
std::monostate x2351(std::monostate);
std::monostate x2405(std::monostate);
std::monostate x2421(std::monostate);
std::monostate x2404(std::monostate);
std::monostate x2444(std::monostate);
std::monostate x2446(std::monostate);
std::monostate x2457(std::monostate);
std::monostate x2350(std::monostate);
std::monostate x2473(std::monostate);
std::monostate x2475(std::monostate);
std::monostate x2599(std::monostate);
std::monostate x2601(std::monostate);
std::monostate x2809(std::monostate);
std::monostate x2811(std::monostate);
std::monostate x2808(std::monostate);
std::monostate x2834(std::monostate);
std::monostate x2836(std::monostate);
std::monostate x2840(std::monostate);
std::monostate x2855(std::monostate);
std::monostate x2248(std::monostate);
std::monostate x2871(std::monostate);
std::monostate x2873(std::monostate);
std::monostate x3018(std::monostate);
std::monostate x3051(std::monostate);
std::monostate x3053(std::monostate);
std::monostate x3165(std::monostate);
std::monostate x3267(std::monostate);
std::monostate x3321(std::monostate);
std::monostate x3339(std::monostate);
std::monostate x3320(std::monostate);
std::monostate x3362(std::monostate);
std::monostate x3364(std::monostate);
std::monostate x3368(std::monostate);
std::monostate x3266(std::monostate);
std::monostate x3384(std::monostate);
std::monostate x3386(std::monostate);
std::monostate x3469(std::monostate);
std::monostate x3471(std::monostate);
std::monostate x3631(std::monostate);
std::monostate x3633(std::monostate);
std::monostate x3630(std::monostate);
std::monostate x3704(std::monostate);
std::monostate x3706(std::monostate);
std::monostate x3710(std::monostate);
std::monostate x3725(std::monostate);
std::monostate x3164(std::monostate);
std::monostate x3789(std::monostate);
std::monostate x3791(std::monostate);
std::monostate x3833(std::monostate);
std::monostate x3855(std::monostate);
std::monostate x3867(std::monostate);
std::monostate x3869(std::monostate);
std::monostate x3871(std::monostate);
std::monostate x3873(std::monostate);
std::monostate x3927(std::monostate);
std::monostate x3931(std::monostate);
std::monostate x3933(std::monostate);
std::monostate x4124(std::monostate);
std::monostate x4446(std::monostate);
std::monostate x4448(std::monostate);
std::monostate x4550(std::monostate);
std::monostate x4552(std::monostate);
std::monostate x4549(std::monostate);
std::monostate x4565(std::monostate);
std::monostate x4567(std::monostate);
std::monostate x4581(std::monostate);
std::monostate x4596(std::monostate);
std::monostate x3926(std::monostate);
std::monostate x4609(std::monostate);
std::monostate x4611(std::monostate);
std::monostate x4618(std::monostate);
std::monostate x4691(std::monostate);
std::monostate x4724(std::monostate);
std::monostate x4726(std::monostate);
std::monostate x4728(std::monostate);
std::monostate x4730(std::monostate);
std::monostate x4880(std::monostate);
std::monostate x4994(std::monostate);
std::monostate x4996(std::monostate);
std::monostate x5194(std::monostate);
std::monostate x5196(std::monostate);
std::monostate x5193(std::monostate);
std::monostate x5260(std::monostate);
std::monostate x5262(std::monostate);
std::monostate x5266(std::monostate);
std::monostate x5281(std::monostate);
std::monostate x4879(std::monostate);
std::monostate x5352(std::monostate);
std::monostate x5354(std::monostate);
std::monostate x5372(std::monostate);
std::monostate x5374(std::monostate);
std::monostate x5517(std::monostate);
std::monostate x5681(std::monostate);
std::monostate x5754(std::monostate);
std::monostate x5778(std::monostate);
std::monostate x5853(std::monostate);
std::monostate x5965(std::monostate);
std::monostate x6019(std::monostate);
std::monostate x6121(std::monostate);
std::monostate x6123(std::monostate);
std::monostate x6125(std::monostate);
std::monostate x6127(std::monostate);
std::monostate x6147(std::monostate);
std::monostate x6120(std::monostate);
std::monostate x6163(std::monostate);
std::monostate x6165(std::monostate);
std::monostate x6176(std::monostate);
std::monostate x6018(std::monostate);
std::monostate x6199(std::monostate);
std::monostate x6201(std::monostate);
std::monostate x6205(std::monostate);
std::monostate x6207(std::monostate);
std::monostate x6367(std::monostate);
std::monostate x6369(std::monostate);
std::monostate x6366(std::monostate);
std::monostate x6440(std::monostate);
std::monostate x6442(std::monostate);
std::monostate x6446(std::monostate);
std::monostate x6533(std::monostate);
std::monostate x5964(std::monostate);
std::monostate x6597(std::monostate);
std::monostate x6599(std::monostate);
std::monostate x6667(std::monostate);
std::monostate x6673(std::monostate);
std::monostate x6727(std::monostate);
std::monostate x6802(std::monostate);
std::monostate x6808(std::monostate);
std::monostate x6882(std::monostate);
std::monostate x6950(std::monostate);
std::monostate x6955(std::monostate);
std::monostate x6957(std::monostate);
std::monostate x6983(std::monostate);
std::monostate x6998(std::monostate);
std::monostate x7000(std::monostate);
std::monostate x7012(std::monostate);
std::monostate x7041(std::monostate);
std::monostate x7043(std::monostate);
std::monostate x7011(std::monostate);
std::monostate x7090(std::monostate);
std::monostate x7092(std::monostate);
std::monostate x7096(std::monostate);
std::monostate x7108(std::monostate);
std::monostate x7110(std::monostate);
std::monostate x7112(std::monostate);
std::monostate x7166(std::monostate);
std::monostate x7168(std::monostate);
std::monostate x7184(std::monostate);
std::monostate x7165(std::monostate);
std::monostate x7207(std::monostate);
std::monostate x7209(std::monostate);
std::monostate x7213(std::monostate);
std::monostate x7242(std::monostate);
std::monostate x7107(std::monostate);
std::monostate x7258(std::monostate);
std::monostate x7260(std::monostate);
std::monostate x2010(std::monostate);
std::monostate x7278(std::monostate);
std::monostate x7345(std::monostate);
std::monostate x7355(std::monostate);
std::monostate x7370(std::monostate);
std::monostate x7380(std::monostate);
std::monostate x7395(std::monostate);
std::monostate x7405(std::monostate);
std::monostate x7420(std::monostate);
std::monostate x7430(std::monostate);
std::monostate x7445(std::monostate);
std::monostate x7455(std::monostate);
std::monostate x7470(std::monostate);
std::monostate x7480(std::monostate);
std::monostate x7495(std::monostate);
std::monostate x7505(std::monostate);
std::monostate x7520(std::monostate);
std::monostate x7530(std::monostate);
std::monostate x7545(std::monostate);
std::monostate x7555(std::monostate);
std::monostate x7635(std::monostate);
std::monostate x7650(std::monostate);
std::monostate x7665(std::monostate);
std::monostate x7680(std::monostate);
std::monostate x7695(std::monostate);
std::monostate x7710(std::monostate);
std::monostate x7725(std::monostate);
std::monostate x7740(std::monostate);
std::monostate x7755(std::monostate);
std::monostate x7770(std::monostate);
std::monostate x7780(std::monostate);
std::monostate x7788(std::monostate);
std::monostate x7796(std::monostate);
std::monostate x7804(std::monostate);
std::monostate x7812(std::monostate);
std::monostate x7820(std::monostate);
std::monostate x7828(std::monostate);
std::monostate x7836(std::monostate);
std::monostate x7844(std::monostate);
std::monostate x7852(std::monostate);
std::monostate x7858(std::monostate);

/************* Functions **************/
std::monostate x7858(std::monostate x7859) {
infoWhen("CALL", "Returning from the function at 7, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
{
Num x7860 = Stack.pop();
SymVal x7861 = SymStack.pop();
Frames.set(0, x7860);
SymFrames.set(0, x7861);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
{
Stack.pop();
SymVal x7862 = SymStack.pop();
SymVal x7863 = x7862.makeI32Symbol();
Stack.push(SymEnv.read(x7863));
SymStack.push(x7863);
}
Stack.push(I32V(1026));
SymStack.push(Concrete(I32V(1026), 32));
{
Stack.pop();
SymVal x7864 = SymStack.pop();
SymVal x7865 = x7864.makeI32Symbol();
Stack.push(SymEnv.read(x7865));
SymStack.push(x7865);
}
{
Num x7866 = Stack.pop();
SymVal x7867 = SymStack.pop();
Num x7868 = Stack.pop();
SymVal x7869 = SymStack.pop();
Num x7870 = x7868.i32_ne(x7866);
Stack.push(x7870);
bool x7871 = allConcrete(x7869, x7867);
SymVal x7872 = x7871 ? Concrete(x7870, 32) : x7869.neq(x7867).bool2bv();
SymStack.push(x7872);
}
Stack.push(I32V(1028));
SymStack.push(Concrete(I32V(1028), 32));
{
Stack.pop();
SymVal x7873 = SymStack.pop();
SymVal x7874 = x7873.makeI32Symbol();
Stack.push(SymEnv.read(x7874));
SymStack.push(x7874);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
{
Stack.pop();
SymVal x7875 = SymStack.pop();
SymVal x7876 = x7875.makeI32Symbol();
Stack.push(SymEnv.read(x7876));
SymStack.push(x7876);
}
{
Num x7877 = Stack.pop();
SymVal x7878 = SymStack.pop();
Num x7879 = Stack.pop();
SymVal x7880 = SymStack.pop();
Num x7881 = x7879.i32_ne(x7877);
Stack.push(x7881);
bool x7882 = allConcrete(x7880, x7878);
SymVal x7883 = x7882 ? Concrete(x7881, 32) : x7880.neq(x7878).bool2bv();
SymStack.push(x7883);
}
Stack.push(I32V(1028));
SymStack.push(Concrete(I32V(1028), 32));
{
Stack.pop();
SymVal x7884 = SymStack.pop();
SymVal x7885 = x7884.makeI32Symbol();
Stack.push(SymEnv.read(x7885));
SymStack.push(x7885);
}
Stack.push(I32V(1026));
SymStack.push(Concrete(I32V(1026), 32));
{
Stack.pop();
SymVal x7886 = SymStack.pop();
SymVal x7887 = x7886.makeI32Symbol();
Stack.push(SymEnv.read(x7887));
SymStack.push(x7887);
}
{
Num x7888 = Stack.pop();
SymVal x7889 = SymStack.pop();
Num x7890 = Stack.pop();
SymVal x7891 = SymStack.pop();
Num x7892 = x7890.i32_ne(x7888);
Stack.push(x7892);
bool x7893 = allConcrete(x7891, x7889);
SymVal x7894 = x7893 ? Concrete(x7892, 32) : x7891.neq(x7889).bool2bv();
SymStack.push(x7894);
}
Stack.push(I32V(1030));
SymStack.push(Concrete(I32V(1030), 32));
{
Stack.pop();
SymVal x7895 = SymStack.pop();
SymVal x7896 = x7895.makeI32Symbol();
Stack.push(SymEnv.read(x7896));
SymStack.push(x7896);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
{
Stack.pop();
SymVal x7897 = SymStack.pop();
SymVal x7898 = x7897.makeI32Symbol();
Stack.push(SymEnv.read(x7898));
SymStack.push(x7898);
}
{
Num x7899 = Stack.pop();
SymVal x7900 = SymStack.pop();
Num x7901 = Stack.pop();
SymVal x7902 = SymStack.pop();
Num x7903 = x7901.i32_ne(x7899);
Stack.push(x7903);
bool x7904 = allConcrete(x7902, x7900);
SymVal x7905 = x7904 ? Concrete(x7903, 32) : x7902.neq(x7900).bool2bv();
SymStack.push(x7905);
}
Stack.push(I32V(1030));
SymStack.push(Concrete(I32V(1030), 32));
{
Stack.pop();
SymVal x7906 = SymStack.pop();
SymVal x7907 = x7906.makeI32Symbol();
Stack.push(SymEnv.read(x7907));
SymStack.push(x7907);
}
Stack.push(I32V(1026));
SymStack.push(Concrete(I32V(1026), 32));
{
Stack.pop();
SymVal x7908 = SymStack.pop();
SymVal x7909 = x7908.makeI32Symbol();
Stack.push(SymEnv.read(x7909));
SymStack.push(x7909);
}
{
Num x7910 = Stack.pop();
SymVal x7911 = SymStack.pop();
Num x7912 = Stack.pop();
SymVal x7913 = SymStack.pop();
Num x7914 = x7912.i32_ne(x7910);
Stack.push(x7914);
bool x7915 = allConcrete(x7913, x7911);
SymVal x7916 = x7915 ? Concrete(x7914, 32) : x7913.neq(x7911).bool2bv();
SymStack.push(x7916);
}
Stack.push(I32V(1030));
SymStack.push(Concrete(I32V(1030), 32));
{
Stack.pop();
SymVal x7917 = SymStack.pop();
SymVal x7918 = x7917.makeI32Symbol();
Stack.push(SymEnv.read(x7918));
SymStack.push(x7918);
}
Stack.push(I32V(1028));
SymStack.push(Concrete(I32V(1028), 32));
{
Stack.pop();
SymVal x7919 = SymStack.pop();
SymVal x7920 = x7919.makeI32Symbol();
Stack.push(SymEnv.read(x7920));
SymStack.push(x7920);
}
{
Num x7921 = Stack.pop();
SymVal x7922 = SymStack.pop();
Num x7923 = Stack.pop();
SymVal x7924 = SymStack.pop();
Num x7925 = x7923.i32_ne(x7921);
Stack.push(x7925);
bool x7926 = allConcrete(x7924, x7922);
SymVal x7927 = x7926 ? Concrete(x7925, 32) : x7924.neq(x7922).bool2bv();
SymStack.push(x7927);
}
Stack.push(I32V(1032));
SymStack.push(Concrete(I32V(1032), 32));
{
Stack.pop();
SymVal x7928 = SymStack.pop();
SymVal x7929 = x7928.makeI32Symbol();
Stack.push(SymEnv.read(x7929));
SymStack.push(x7929);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
{
Stack.pop();
SymVal x7930 = SymStack.pop();
SymVal x7931 = x7930.makeI32Symbol();
Stack.push(SymEnv.read(x7931));
SymStack.push(x7931);
}
{
Num x7932 = Stack.pop();
SymVal x7933 = SymStack.pop();
Num x7934 = Stack.pop();
SymVal x7935 = SymStack.pop();
Num x7936 = x7934.i32_ne(x7932);
Stack.push(x7936);
bool x7937 = allConcrete(x7935, x7933);
SymVal x7938 = x7937 ? Concrete(x7936, 32) : x7935.neq(x7933).bool2bv();
SymStack.push(x7938);
}
Stack.push(I32V(1032));
SymStack.push(Concrete(I32V(1032), 32));
{
Stack.pop();
SymVal x7939 = SymStack.pop();
SymVal x7940 = x7939.makeI32Symbol();
Stack.push(SymEnv.read(x7940));
SymStack.push(x7940);
}
Stack.push(I32V(1026));
SymStack.push(Concrete(I32V(1026), 32));
{
Stack.pop();
SymVal x7941 = SymStack.pop();
SymVal x7942 = x7941.makeI32Symbol();
Stack.push(SymEnv.read(x7942));
SymStack.push(x7942);
}
{
Num x7943 = Stack.pop();
SymVal x7944 = SymStack.pop();
Num x7945 = Stack.pop();
SymVal x7946 = SymStack.pop();
Num x7947 = x7945.i32_ne(x7943);
Stack.push(x7947);
bool x7948 = allConcrete(x7946, x7944);
SymVal x7949 = x7948 ? Concrete(x7947, 32) : x7946.neq(x7944).bool2bv();
SymStack.push(x7949);
}
Stack.push(I32V(1032));
SymStack.push(Concrete(I32V(1032), 32));
{
Stack.pop();
SymVal x7950 = SymStack.pop();
SymVal x7951 = x7950.makeI32Symbol();
Stack.push(SymEnv.read(x7951));
SymStack.push(x7951);
}
Stack.push(I32V(1028));
SymStack.push(Concrete(I32V(1028), 32));
{
Stack.pop();
SymVal x7952 = SymStack.pop();
SymVal x7953 = x7952.makeI32Symbol();
Stack.push(SymEnv.read(x7953));
SymStack.push(x7953);
}
{
Num x7954 = Stack.pop();
SymVal x7955 = SymStack.pop();
Num x7956 = Stack.pop();
SymVal x7957 = SymStack.pop();
Num x7958 = x7956.i32_ne(x7954);
Stack.push(x7958);
bool x7959 = allConcrete(x7957, x7955);
SymVal x7960 = x7959 ? Concrete(x7958, 32) : x7957.neq(x7955).bool2bv();
SymStack.push(x7960);
}
Stack.push(I32V(1032));
SymStack.push(Concrete(I32V(1032), 32));
{
Stack.pop();
SymVal x7961 = SymStack.pop();
SymVal x7962 = x7961.makeI32Symbol();
Stack.push(SymEnv.read(x7962));
SymStack.push(x7962);
}
Stack.push(I32V(1030));
SymStack.push(Concrete(I32V(1030), 32));
{
Stack.pop();
SymVal x7963 = SymStack.pop();
SymVal x7964 = x7963.makeI32Symbol();
Stack.push(SymEnv.read(x7964));
SymStack.push(x7964);
}
{
Num x7965 = Stack.pop();
SymVal x7966 = SymStack.pop();
Num x7967 = Stack.pop();
SymVal x7968 = SymStack.pop();
Num x7969 = x7967.i32_ne(x7965);
Stack.push(x7969);
bool x7970 = allConcrete(x7968, x7966);
SymVal x7971 = x7970 ? Concrete(x7969, 32) : x7968.neq(x7966).bool2bv();
SymStack.push(x7971);
}
Stack.push(I32V(1034));
SymStack.push(Concrete(I32V(1034), 32));
{
Stack.pop();
SymVal x7972 = SymStack.pop();
SymVal x7973 = x7972.makeI32Symbol();
Stack.push(SymEnv.read(x7973));
SymStack.push(x7973);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
{
Stack.pop();
SymVal x7974 = SymStack.pop();
SymVal x7975 = x7974.makeI32Symbol();
Stack.push(SymEnv.read(x7975));
SymStack.push(x7975);
}
{
Num x7976 = Stack.pop();
SymVal x7977 = SymStack.pop();
Num x7978 = Stack.pop();
SymVal x7979 = SymStack.pop();
Num x7980 = x7978.i32_ne(x7976);
Stack.push(x7980);
bool x7981 = allConcrete(x7979, x7977);
SymVal x7982 = x7981 ? Concrete(x7980, 32) : x7979.neq(x7977).bool2bv();
SymStack.push(x7982);
}
Stack.push(I32V(1034));
SymStack.push(Concrete(I32V(1034), 32));
{
Stack.pop();
SymVal x7983 = SymStack.pop();
SymVal x7984 = x7983.makeI32Symbol();
Stack.push(SymEnv.read(x7984));
SymStack.push(x7984);
}
Stack.push(I32V(1026));
SymStack.push(Concrete(I32V(1026), 32));
{
Stack.pop();
SymVal x7985 = SymStack.pop();
SymVal x7986 = x7985.makeI32Symbol();
Stack.push(SymEnv.read(x7986));
SymStack.push(x7986);
}
{
Num x7987 = Stack.pop();
SymVal x7988 = SymStack.pop();
Num x7989 = Stack.pop();
SymVal x7990 = SymStack.pop();
Num x7991 = x7989.i32_ne(x7987);
Stack.push(x7991);
bool x7992 = allConcrete(x7990, x7988);
SymVal x7993 = x7992 ? Concrete(x7991, 32) : x7990.neq(x7988).bool2bv();
SymStack.push(x7993);
}
Stack.push(I32V(1034));
SymStack.push(Concrete(I32V(1034), 32));
{
Stack.pop();
SymVal x7994 = SymStack.pop();
SymVal x7995 = x7994.makeI32Symbol();
Stack.push(SymEnv.read(x7995));
SymStack.push(x7995);
}
Stack.push(I32V(1028));
SymStack.push(Concrete(I32V(1028), 32));
{
Stack.pop();
SymVal x7996 = SymStack.pop();
SymVal x7997 = x7996.makeI32Symbol();
Stack.push(SymEnv.read(x7997));
SymStack.push(x7997);
}
{
Num x7998 = Stack.pop();
SymVal x7999 = SymStack.pop();
Num x8000 = Stack.pop();
SymVal x8001 = SymStack.pop();
Num x8002 = x8000.i32_ne(x7998);
Stack.push(x8002);
bool x8003 = allConcrete(x8001, x7999);
SymVal x8004 = x8003 ? Concrete(x8002, 32) : x8001.neq(x7999).bool2bv();
SymStack.push(x8004);
}
Stack.push(I32V(1034));
SymStack.push(Concrete(I32V(1034), 32));
{
Stack.pop();
SymVal x8005 = SymStack.pop();
SymVal x8006 = x8005.makeI32Symbol();
Stack.push(SymEnv.read(x8006));
SymStack.push(x8006);
}
Stack.push(I32V(1030));
SymStack.push(Concrete(I32V(1030), 32));
{
Stack.pop();
SymVal x8007 = SymStack.pop();
SymVal x8008 = x8007.makeI32Symbol();
Stack.push(SymEnv.read(x8008));
SymStack.push(x8008);
}
{
Num x8009 = Stack.pop();
SymVal x8010 = SymStack.pop();
Num x8011 = Stack.pop();
SymVal x8012 = SymStack.pop();
Num x8013 = x8011.i32_ne(x8009);
Stack.push(x8013);
bool x8014 = allConcrete(x8012, x8010);
SymVal x8015 = x8014 ? Concrete(x8013, 32) : x8012.neq(x8010).bool2bv();
SymStack.push(x8015);
}
Stack.push(I32V(1034));
SymStack.push(Concrete(I32V(1034), 32));
{
Stack.pop();
SymVal x8016 = SymStack.pop();
SymVal x8017 = x8016.makeI32Symbol();
Stack.push(SymEnv.read(x8017));
SymStack.push(x8017);
}
Stack.push(I32V(1032));
SymStack.push(Concrete(I32V(1032), 32));
{
Stack.pop();
SymVal x8018 = SymStack.pop();
SymVal x8019 = x8018.makeI32Symbol();
Stack.push(SymEnv.read(x8019));
SymStack.push(x8019);
}
{
Num x8020 = Stack.pop();
SymVal x8021 = SymStack.pop();
Num x8022 = Stack.pop();
SymVal x8023 = SymStack.pop();
Num x8024 = x8022.i32_ne(x8020);
Stack.push(x8024);
bool x8025 = allConcrete(x8023, x8021);
SymVal x8026 = x8025 ? Concrete(x8024, 32) : x8023.neq(x8021).bool2bv();
SymStack.push(x8026);
}
Stack.push(I32V(1036));
SymStack.push(Concrete(I32V(1036), 32));
{
Stack.pop();
SymVal x8027 = SymStack.pop();
SymVal x8028 = x8027.makeI32Symbol();
Stack.push(SymEnv.read(x8028));
SymStack.push(x8028);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
{
Stack.pop();
SymVal x8029 = SymStack.pop();
SymVal x8030 = x8029.makeI32Symbol();
Stack.push(SymEnv.read(x8030));
SymStack.push(x8030);
}
{
Num x8031 = Stack.pop();
SymVal x8032 = SymStack.pop();
Num x8033 = Stack.pop();
SymVal x8034 = SymStack.pop();
Num x8035 = x8033.i32_ne(x8031);
Stack.push(x8035);
bool x8036 = allConcrete(x8034, x8032);
SymVal x8037 = x8036 ? Concrete(x8035, 32) : x8034.neq(x8032).bool2bv();
SymStack.push(x8037);
}
Stack.push(I32V(1036));
SymStack.push(Concrete(I32V(1036), 32));
{
Stack.pop();
SymVal x8038 = SymStack.pop();
SymVal x8039 = x8038.makeI32Symbol();
Stack.push(SymEnv.read(x8039));
SymStack.push(x8039);
}
Stack.push(I32V(1026));
SymStack.push(Concrete(I32V(1026), 32));
{
Stack.pop();
SymVal x8040 = SymStack.pop();
SymVal x8041 = x8040.makeI32Symbol();
Stack.push(SymEnv.read(x8041));
SymStack.push(x8041);
}
{
Num x8042 = Stack.pop();
SymVal x8043 = SymStack.pop();
Num x8044 = Stack.pop();
SymVal x8045 = SymStack.pop();
Num x8046 = x8044.i32_ne(x8042);
Stack.push(x8046);
bool x8047 = allConcrete(x8045, x8043);
SymVal x8048 = x8047 ? Concrete(x8046, 32) : x8045.neq(x8043).bool2bv();
SymStack.push(x8048);
}
Stack.push(I32V(1036));
SymStack.push(Concrete(I32V(1036), 32));
{
Stack.pop();
SymVal x8049 = SymStack.pop();
SymVal x8050 = x8049.makeI32Symbol();
Stack.push(SymEnv.read(x8050));
SymStack.push(x8050);
}
Stack.push(I32V(1028));
SymStack.push(Concrete(I32V(1028), 32));
{
Stack.pop();
SymVal x8051 = SymStack.pop();
SymVal x8052 = x8051.makeI32Symbol();
Stack.push(SymEnv.read(x8052));
SymStack.push(x8052);
}
{
Num x8053 = Stack.pop();
SymVal x8054 = SymStack.pop();
Num x8055 = Stack.pop();
SymVal x8056 = SymStack.pop();
Num x8057 = x8055.i32_ne(x8053);
Stack.push(x8057);
bool x8058 = allConcrete(x8056, x8054);
SymVal x8059 = x8058 ? Concrete(x8057, 32) : x8056.neq(x8054).bool2bv();
SymStack.push(x8059);
}
Stack.push(I32V(1036));
SymStack.push(Concrete(I32V(1036), 32));
{
Stack.pop();
SymVal x8060 = SymStack.pop();
SymVal x8061 = x8060.makeI32Symbol();
Stack.push(SymEnv.read(x8061));
SymStack.push(x8061);
}
Stack.push(I32V(1030));
SymStack.push(Concrete(I32V(1030), 32));
{
Stack.pop();
SymVal x8062 = SymStack.pop();
SymVal x8063 = x8062.makeI32Symbol();
Stack.push(SymEnv.read(x8063));
SymStack.push(x8063);
}
{
Num x8064 = Stack.pop();
SymVal x8065 = SymStack.pop();
Num x8066 = Stack.pop();
SymVal x8067 = SymStack.pop();
Num x8068 = x8066.i32_ne(x8064);
Stack.push(x8068);
bool x8069 = allConcrete(x8067, x8065);
SymVal x8070 = x8069 ? Concrete(x8068, 32) : x8067.neq(x8065).bool2bv();
SymStack.push(x8070);
}
Stack.push(I32V(1036));
SymStack.push(Concrete(I32V(1036), 32));
{
Stack.pop();
SymVal x8071 = SymStack.pop();
SymVal x8072 = x8071.makeI32Symbol();
Stack.push(SymEnv.read(x8072));
SymStack.push(x8072);
}
Stack.push(I32V(1032));
SymStack.push(Concrete(I32V(1032), 32));
{
Stack.pop();
SymVal x8073 = SymStack.pop();
SymVal x8074 = x8073.makeI32Symbol();
Stack.push(SymEnv.read(x8074));
SymStack.push(x8074);
}
{
Num x8075 = Stack.pop();
SymVal x8076 = SymStack.pop();
Num x8077 = Stack.pop();
SymVal x8078 = SymStack.pop();
Num x8079 = x8077.i32_ne(x8075);
Stack.push(x8079);
bool x8080 = allConcrete(x8078, x8076);
SymVal x8081 = x8080 ? Concrete(x8079, 32) : x8078.neq(x8076).bool2bv();
SymStack.push(x8081);
}
Stack.push(I32V(1036));
SymStack.push(Concrete(I32V(1036), 32));
{
Stack.pop();
SymVal x8082 = SymStack.pop();
SymVal x8083 = x8082.makeI32Symbol();
Stack.push(SymEnv.read(x8083));
SymStack.push(x8083);
}
Stack.push(I32V(1034));
SymStack.push(Concrete(I32V(1034), 32));
{
Stack.pop();
SymVal x8084 = SymStack.pop();
SymVal x8085 = x8084.makeI32Symbol();
Stack.push(SymEnv.read(x8085));
SymStack.push(x8085);
}
{
Num x8086 = Stack.pop();
SymVal x8087 = SymStack.pop();
Num x8088 = Stack.pop();
SymVal x8089 = SymStack.pop();
Num x8090 = x8088.i32_ne(x8086);
Stack.push(x8090);
bool x8091 = allConcrete(x8089, x8087);
SymVal x8092 = x8091 ? Concrete(x8090, 32) : x8089.neq(x8087).bool2bv();
SymStack.push(x8092);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
{
Stack.pop();
SymVal x8093 = SymStack.pop();
SymVal x8094 = x8093.makeI32Symbol();
Stack.push(SymEnv.read(x8094));
SymStack.push(x8094);
}
Stack.push(I32V(1026));
SymStack.push(Concrete(I32V(1026), 32));
{
Stack.pop();
SymVal x8095 = SymStack.pop();
SymVal x8096 = x8095.makeI32Symbol();
Stack.push(SymEnv.read(x8096));
SymStack.push(x8096);
}
{
Num x8097 = Stack.pop();
SymVal x8098 = SymStack.pop();
Num x8099 = Stack.pop();
SymVal x8100 = SymStack.pop();
Num x8101 = x8099.i32_gt_s(x8097);
Stack.push(x8101);
bool x8102 = allConcrete(x8100, x8098);
SymVal x8103 = x8102 ? Concrete(x8101, 32) : x8100.gt(x8098).bool2bv();
SymStack.push(x8103);
}
Stack.push(I32V(1026));
SymStack.push(Concrete(I32V(1026), 32));
{
Stack.pop();
SymVal x8104 = SymStack.pop();
SymVal x8105 = x8104.makeI32Symbol();
Stack.push(SymEnv.read(x8105));
SymStack.push(x8105);
}
Stack.push(I32V(1028));
SymStack.push(Concrete(I32V(1028), 32));
{
Stack.pop();
SymVal x8106 = SymStack.pop();
SymVal x8107 = x8106.makeI32Symbol();
Stack.push(SymEnv.read(x8107));
SymStack.push(x8107);
}
{
Num x8108 = Stack.pop();
SymVal x8109 = SymStack.pop();
Num x8110 = Stack.pop();
SymVal x8111 = SymStack.pop();
Num x8112 = x8110.i32_gt_s(x8108);
Stack.push(x8112);
bool x8113 = allConcrete(x8111, x8109);
SymVal x8114 = x8113 ? Concrete(x8112, 32) : x8111.gt(x8109).bool2bv();
SymStack.push(x8114);
}
Stack.push(I32V(1028));
SymStack.push(Concrete(I32V(1028), 32));
{
Stack.pop();
SymVal x8115 = SymStack.pop();
SymVal x8116 = x8115.makeI32Symbol();
Stack.push(SymEnv.read(x8116));
SymStack.push(x8116);
}
Stack.push(I32V(1030));
SymStack.push(Concrete(I32V(1030), 32));
{
Stack.pop();
SymVal x8117 = SymStack.pop();
SymVal x8118 = x8117.makeI32Symbol();
Stack.push(SymEnv.read(x8118));
SymStack.push(x8118);
}
{
Num x8119 = Stack.pop();
SymVal x8120 = SymStack.pop();
Num x8121 = Stack.pop();
SymVal x8122 = SymStack.pop();
Num x8123 = x8121.i32_gt_s(x8119);
Stack.push(x8123);
bool x8124 = allConcrete(x8122, x8120);
SymVal x8125 = x8124 ? Concrete(x8123, 32) : x8122.gt(x8120).bool2bv();
SymStack.push(x8125);
}
Stack.push(I32V(1030));
SymStack.push(Concrete(I32V(1030), 32));
{
Stack.pop();
SymVal x8126 = SymStack.pop();
SymVal x8127 = x8126.makeI32Symbol();
Stack.push(SymEnv.read(x8127));
SymStack.push(x8127);
}
Stack.push(I32V(1032));
SymStack.push(Concrete(I32V(1032), 32));
{
Stack.pop();
SymVal x8128 = SymStack.pop();
SymVal x8129 = x8128.makeI32Symbol();
Stack.push(SymEnv.read(x8129));
SymStack.push(x8129);
}
{
Num x8130 = Stack.pop();
SymVal x8131 = SymStack.pop();
Num x8132 = Stack.pop();
SymVal x8133 = SymStack.pop();
Num x8134 = x8132.i32_gt_s(x8130);
Stack.push(x8134);
bool x8135 = allConcrete(x8133, x8131);
SymVal x8136 = x8135 ? Concrete(x8134, 32) : x8133.gt(x8131).bool2bv();
SymStack.push(x8136);
}
Stack.push(I32V(1032));
SymStack.push(Concrete(I32V(1032), 32));
{
Stack.pop();
SymVal x8137 = SymStack.pop();
SymVal x8138 = x8137.makeI32Symbol();
Stack.push(SymEnv.read(x8138));
SymStack.push(x8138);
}
Stack.push(I32V(1034));
SymStack.push(Concrete(I32V(1034), 32));
{
Stack.pop();
SymVal x8139 = SymStack.pop();
SymVal x8140 = x8139.makeI32Symbol();
Stack.push(SymEnv.read(x8140));
SymStack.push(x8140);
}
{
Num x8141 = Stack.pop();
SymVal x8142 = SymStack.pop();
Num x8143 = Stack.pop();
SymVal x8144 = SymStack.pop();
Num x8145 = x8143.i32_gt_s(x8141);
Stack.push(x8145);
bool x8146 = allConcrete(x8144, x8142);
SymVal x8147 = x8146 ? Concrete(x8145, 32) : x8144.gt(x8142).bool2bv();
SymStack.push(x8147);
}
Stack.push(I32V(1034));
SymStack.push(Concrete(I32V(1034), 32));
{
Stack.pop();
SymVal x8148 = SymStack.pop();
SymVal x8149 = x8148.makeI32Symbol();
Stack.push(SymEnv.read(x8149));
SymStack.push(x8149);
}
Stack.push(I32V(1036));
SymStack.push(Concrete(I32V(1036), 32));
{
Stack.pop();
SymVal x8150 = SymStack.pop();
SymVal x8151 = x8150.makeI32Symbol();
Stack.push(SymEnv.read(x8151));
SymStack.push(x8151);
}
{
Num x8152 = Stack.pop();
SymVal x8153 = SymStack.pop();
Num x8154 = Stack.pop();
SymVal x8155 = SymStack.pop();
Num x8156 = x8154.i32_gt_s(x8152);
Stack.push(x8156);
bool x8157 = allConcrete(x8155, x8153);
SymVal x8158 = x8157 ? Concrete(x8156, 32) : x8155.gt(x8153).bool2bv();
SymStack.push(x8158);
}
{
Num x8159 = Stack.pop();
SymVal x8160 = SymStack.pop();
Num x8161 = Stack.pop();
SymVal x8162 = SymStack.pop();
Num x8163 = x8161.i32_and(x8159);
Stack.push(x8163);
bool x8164 = allConcrete(x8162, x8160);
SymVal x8165 = x8164 ? Concrete(x8163, 32) : x8162.bitwise_and(x8160);
SymStack.push(x8165);
}
{
Num x8166 = Stack.pop();
SymVal x8167 = SymStack.pop();
Num x8168 = Stack.pop();
SymVal x8169 = SymStack.pop();
Num x8170 = x8168.i32_and(x8166);
Stack.push(x8170);
bool x8171 = allConcrete(x8169, x8167);
SymVal x8172 = x8171 ? Concrete(x8170, 32) : x8169.bitwise_and(x8167);
SymStack.push(x8172);
}
{
Num x8173 = Stack.pop();
SymVal x8174 = SymStack.pop();
Num x8175 = Stack.pop();
SymVal x8176 = SymStack.pop();
Num x8177 = x8175.i32_and(x8173);
Stack.push(x8177);
bool x8178 = allConcrete(x8176, x8174);
SymVal x8179 = x8178 ? Concrete(x8177, 32) : x8176.bitwise_and(x8174);
SymStack.push(x8179);
}
{
Num x8180 = Stack.pop();
SymVal x8181 = SymStack.pop();
Num x8182 = Stack.pop();
SymVal x8183 = SymStack.pop();
Num x8184 = x8182.i32_and(x8180);
Stack.push(x8184);
bool x8185 = allConcrete(x8183, x8181);
SymVal x8186 = x8185 ? Concrete(x8184, 32) : x8183.bitwise_and(x8181);
SymStack.push(x8186);
}
{
Num x8187 = Stack.pop();
SymVal x8188 = SymStack.pop();
Num x8189 = Stack.pop();
SymVal x8190 = SymStack.pop();
Num x8191 = x8189.i32_and(x8187);
Stack.push(x8191);
bool x8192 = allConcrete(x8190, x8188);
SymVal x8193 = x8192 ? Concrete(x8191, 32) : x8190.bitwise_and(x8188);
SymStack.push(x8193);
}
{
Num x8194 = Stack.pop();
SymVal x8195 = SymStack.pop();
Num x8196 = Stack.pop();
SymVal x8197 = SymStack.pop();
Num x8198 = x8196.i32_and(x8194);
Stack.push(x8198);
bool x8199 = allConcrete(x8197, x8195);
SymVal x8200 = x8199 ? Concrete(x8198, 32) : x8197.bitwise_and(x8195);
SymStack.push(x8200);
}
{
Num x8201 = Stack.pop();
SymVal x8202 = SymStack.pop();
Num x8203 = Stack.pop();
SymVal x8204 = SymStack.pop();
Num x8205 = x8203.i32_and(x8201);
Stack.push(x8205);
bool x8206 = allConcrete(x8204, x8202);
SymVal x8207 = x8206 ? Concrete(x8205, 32) : x8204.bitwise_and(x8202);
SymStack.push(x8207);
}
{
Num x8208 = Stack.pop();
SymVal x8209 = SymStack.pop();
Num x8210 = Stack.pop();
SymVal x8211 = SymStack.pop();
Num x8212 = x8210.i32_and(x8208);
Stack.push(x8212);
bool x8213 = allConcrete(x8211, x8209);
SymVal x8214 = x8213 ? Concrete(x8212, 32) : x8211.bitwise_and(x8209);
SymStack.push(x8214);
}
{
Num x8215 = Stack.pop();
SymVal x8216 = SymStack.pop();
Num x8217 = Stack.pop();
SymVal x8218 = SymStack.pop();
Num x8219 = x8217.i32_and(x8215);
Stack.push(x8219);
bool x8220 = allConcrete(x8218, x8216);
SymVal x8221 = x8220 ? Concrete(x8219, 32) : x8218.bitwise_and(x8216);
SymStack.push(x8221);
}
{
Num x8222 = Stack.pop();
SymVal x8223 = SymStack.pop();
Num x8224 = Stack.pop();
SymVal x8225 = SymStack.pop();
Num x8226 = x8224.i32_and(x8222);
Stack.push(x8226);
bool x8227 = allConcrete(x8225, x8223);
SymVal x8228 = x8227 ? Concrete(x8226, 32) : x8225.bitwise_and(x8223);
SymStack.push(x8228);
}
{
Num x8229 = Stack.pop();
SymVal x8230 = SymStack.pop();
Num x8231 = Stack.pop();
SymVal x8232 = SymStack.pop();
Num x8233 = x8231.i32_and(x8229);
Stack.push(x8233);
bool x8234 = allConcrete(x8232, x8230);
SymVal x8235 = x8234 ? Concrete(x8233, 32) : x8232.bitwise_and(x8230);
SymStack.push(x8235);
}
{
Num x8236 = Stack.pop();
SymVal x8237 = SymStack.pop();
Num x8238 = Stack.pop();
SymVal x8239 = SymStack.pop();
Num x8240 = x8238.i32_and(x8236);
Stack.push(x8240);
bool x8241 = allConcrete(x8239, x8237);
SymVal x8242 = x8241 ? Concrete(x8240, 32) : x8239.bitwise_and(x8237);
SymStack.push(x8242);
}
{
Num x8243 = Stack.pop();
SymVal x8244 = SymStack.pop();
Num x8245 = Stack.pop();
SymVal x8246 = SymStack.pop();
Num x8247 = x8245.i32_and(x8243);
Stack.push(x8247);
bool x8248 = allConcrete(x8246, x8244);
SymVal x8249 = x8248 ? Concrete(x8247, 32) : x8246.bitwise_and(x8244);
SymStack.push(x8249);
}
{
Num x8250 = Stack.pop();
SymVal x8251 = SymStack.pop();
Num x8252 = Stack.pop();
SymVal x8253 = SymStack.pop();
Num x8254 = x8252.i32_and(x8250);
Stack.push(x8254);
bool x8255 = allConcrete(x8253, x8251);
SymVal x8256 = x8255 ? Concrete(x8254, 32) : x8253.bitwise_and(x8251);
SymStack.push(x8256);
}
{
Num x8257 = Stack.pop();
SymVal x8258 = SymStack.pop();
Num x8259 = Stack.pop();
SymVal x8260 = SymStack.pop();
Num x8261 = x8259.i32_and(x8257);
Stack.push(x8261);
bool x8262 = allConcrete(x8260, x8258);
SymVal x8263 = x8262 ? Concrete(x8261, 32) : x8260.bitwise_and(x8258);
SymStack.push(x8263);
}
{
Num x8264 = Stack.pop();
SymVal x8265 = SymStack.pop();
Num x8266 = Stack.pop();
SymVal x8267 = SymStack.pop();
Num x8268 = x8266.i32_and(x8264);
Stack.push(x8268);
bool x8269 = allConcrete(x8267, x8265);
SymVal x8270 = x8269 ? Concrete(x8268, 32) : x8267.bitwise_and(x8265);
SymStack.push(x8270);
}
{
Num x8271 = Stack.pop();
SymVal x8272 = SymStack.pop();
Num x8273 = Stack.pop();
SymVal x8274 = SymStack.pop();
Num x8275 = x8273.i32_and(x8271);
Stack.push(x8275);
bool x8276 = allConcrete(x8274, x8272);
SymVal x8277 = x8276 ? Concrete(x8275, 32) : x8274.bitwise_and(x8272);
SymStack.push(x8277);
}
{
Num x8278 = Stack.pop();
SymVal x8279 = SymStack.pop();
Num x8280 = Stack.pop();
SymVal x8281 = SymStack.pop();
Num x8282 = x8280.i32_and(x8278);
Stack.push(x8282);
bool x8283 = allConcrete(x8281, x8279);
SymVal x8284 = x8283 ? Concrete(x8282, 32) : x8281.bitwise_and(x8279);
SymStack.push(x8284);
}
{
Num x8285 = Stack.pop();
SymVal x8286 = SymStack.pop();
Num x8287 = Stack.pop();
SymVal x8288 = SymStack.pop();
Num x8289 = x8287.i32_and(x8285);
Stack.push(x8289);
bool x8290 = allConcrete(x8288, x8286);
SymVal x8291 = x8290 ? Concrete(x8289, 32) : x8288.bitwise_and(x8286);
SymStack.push(x8291);
}
{
Num x8292 = Stack.pop();
SymVal x8293 = SymStack.pop();
Num x8294 = Stack.pop();
SymVal x8295 = SymStack.pop();
Num x8296 = x8294.i32_and(x8292);
Stack.push(x8296);
bool x8297 = allConcrete(x8295, x8293);
SymVal x8298 = x8297 ? Concrete(x8296, 32) : x8295.bitwise_and(x8293);
SymStack.push(x8298);
}
{
Num x8299 = Stack.pop();
SymVal x8300 = SymStack.pop();
Num x8301 = Stack.pop();
SymVal x8302 = SymStack.pop();
Num x8303 = x8301.i32_and(x8299);
Stack.push(x8303);
bool x8304 = allConcrete(x8302, x8300);
SymVal x8305 = x8304 ? Concrete(x8303, 32) : x8302.bitwise_and(x8300);
SymStack.push(x8305);
}
{
Num x8306 = Stack.pop();
SymVal x8307 = SymStack.pop();
Num x8308 = Stack.pop();
SymVal x8309 = SymStack.pop();
Num x8310 = x8308.i32_and(x8306);
Stack.push(x8310);
bool x8311 = allConcrete(x8309, x8307);
SymVal x8312 = x8311 ? Concrete(x8310, 32) : x8309.bitwise_and(x8307);
SymStack.push(x8312);
}
{
Num x8313 = Stack.pop();
SymVal x8314 = SymStack.pop();
Num x8315 = Stack.pop();
SymVal x8316 = SymStack.pop();
Num x8317 = x8315.i32_and(x8313);
Stack.push(x8317);
bool x8318 = allConcrete(x8316, x8314);
SymVal x8319 = x8318 ? Concrete(x8317, 32) : x8316.bitwise_and(x8314);
SymStack.push(x8319);
}
{
Num x8320 = Stack.pop();
SymVal x8321 = SymStack.pop();
Num x8322 = Stack.pop();
SymVal x8323 = SymStack.pop();
Num x8324 = x8322.i32_and(x8320);
Stack.push(x8324);
bool x8325 = allConcrete(x8323, x8321);
SymVal x8326 = x8325 ? Concrete(x8324, 32) : x8323.bitwise_and(x8321);
SymStack.push(x8326);
}
{
Num x8327 = Stack.pop();
SymVal x8328 = SymStack.pop();
Num x8329 = Stack.pop();
SymVal x8330 = SymStack.pop();
Num x8331 = x8329.i32_and(x8327);
Stack.push(x8331);
bool x8332 = allConcrete(x8330, x8328);
SymVal x8333 = x8332 ? Concrete(x8331, 32) : x8330.bitwise_and(x8328);
SymStack.push(x8333);
}
{
Num x8334 = Stack.pop();
SymVal x8335 = SymStack.pop();
Num x8336 = Stack.pop();
SymVal x8337 = SymStack.pop();
Num x8338 = x8336.i32_and(x8334);
Stack.push(x8338);
bool x8339 = allConcrete(x8337, x8335);
SymVal x8340 = x8339 ? Concrete(x8338, 32) : x8337.bitwise_and(x8335);
SymStack.push(x8340);
}
Stack.push(I32V(1038));
SymStack.push(Concrete(I32V(1038), 32));
{
Stack.pop();
SymVal x8341 = SymStack.pop();
SymVal x8342 = x8341.makeI32Symbol();
Stack.push(SymEnv.read(x8342));
SymStack.push(x8342);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
{
Stack.pop();
SymVal x8343 = SymStack.pop();
SymVal x8344 = x8343.makeI32Symbol();
Stack.push(SymEnv.read(x8344));
SymStack.push(x8344);
}
{
Num x8345 = Stack.pop();
SymVal x8346 = SymStack.pop();
Num x8347 = Stack.pop();
SymVal x8348 = SymStack.pop();
Num x8349 = x8347.i32_ne(x8345);
Stack.push(x8349);
bool x8350 = allConcrete(x8348, x8346);
SymVal x8351 = x8350 ? Concrete(x8349, 32) : x8348.neq(x8346).bool2bv();
SymStack.push(x8351);
}
Stack.push(I32V(1038));
SymStack.push(Concrete(I32V(1038), 32));
{
Stack.pop();
SymVal x8352 = SymStack.pop();
SymVal x8353 = x8352.makeI32Symbol();
Stack.push(SymEnv.read(x8353));
SymStack.push(x8353);
}
Stack.push(I32V(1026));
SymStack.push(Concrete(I32V(1026), 32));
{
Stack.pop();
SymVal x8354 = SymStack.pop();
SymVal x8355 = x8354.makeI32Symbol();
Stack.push(SymEnv.read(x8355));
SymStack.push(x8355);
}
{
Num x8356 = Stack.pop();
SymVal x8357 = SymStack.pop();
Num x8358 = Stack.pop();
SymVal x8359 = SymStack.pop();
Num x8360 = x8358.i32_ne(x8356);
Stack.push(x8360);
bool x8361 = allConcrete(x8359, x8357);
SymVal x8362 = x8361 ? Concrete(x8360, 32) : x8359.neq(x8357).bool2bv();
SymStack.push(x8362);
}
Stack.push(I32V(1038));
SymStack.push(Concrete(I32V(1038), 32));
{
Stack.pop();
SymVal x8363 = SymStack.pop();
SymVal x8364 = x8363.makeI32Symbol();
Stack.push(SymEnv.read(x8364));
SymStack.push(x8364);
}
Stack.push(I32V(1028));
SymStack.push(Concrete(I32V(1028), 32));
{
Stack.pop();
SymVal x8365 = SymStack.pop();
SymVal x8366 = x8365.makeI32Symbol();
Stack.push(SymEnv.read(x8366));
SymStack.push(x8366);
}
{
Num x8367 = Stack.pop();
SymVal x8368 = SymStack.pop();
Num x8369 = Stack.pop();
SymVal x8370 = SymStack.pop();
Num x8371 = x8369.i32_ne(x8367);
Stack.push(x8371);
bool x8372 = allConcrete(x8370, x8368);
SymVal x8373 = x8372 ? Concrete(x8371, 32) : x8370.neq(x8368).bool2bv();
SymStack.push(x8373);
}
Stack.push(I32V(1038));
SymStack.push(Concrete(I32V(1038), 32));
{
Stack.pop();
SymVal x8374 = SymStack.pop();
SymVal x8375 = x8374.makeI32Symbol();
Stack.push(SymEnv.read(x8375));
SymStack.push(x8375);
}
Stack.push(I32V(1030));
SymStack.push(Concrete(I32V(1030), 32));
{
Stack.pop();
SymVal x8376 = SymStack.pop();
SymVal x8377 = x8376.makeI32Symbol();
Stack.push(SymEnv.read(x8377));
SymStack.push(x8377);
}
{
Num x8378 = Stack.pop();
SymVal x8379 = SymStack.pop();
Num x8380 = Stack.pop();
SymVal x8381 = SymStack.pop();
Num x8382 = x8380.i32_ne(x8378);
Stack.push(x8382);
bool x8383 = allConcrete(x8381, x8379);
SymVal x8384 = x8383 ? Concrete(x8382, 32) : x8381.neq(x8379).bool2bv();
SymStack.push(x8384);
}
Stack.push(I32V(1038));
SymStack.push(Concrete(I32V(1038), 32));
{
Stack.pop();
SymVal x8385 = SymStack.pop();
SymVal x8386 = x8385.makeI32Symbol();
Stack.push(SymEnv.read(x8386));
SymStack.push(x8386);
}
Stack.push(I32V(1032));
SymStack.push(Concrete(I32V(1032), 32));
{
Stack.pop();
SymVal x8387 = SymStack.pop();
SymVal x8388 = x8387.makeI32Symbol();
Stack.push(SymEnv.read(x8388));
SymStack.push(x8388);
}
{
Num x8389 = Stack.pop();
SymVal x8390 = SymStack.pop();
Num x8391 = Stack.pop();
SymVal x8392 = SymStack.pop();
Num x8393 = x8391.i32_ne(x8389);
Stack.push(x8393);
bool x8394 = allConcrete(x8392, x8390);
SymVal x8395 = x8394 ? Concrete(x8393, 32) : x8392.neq(x8390).bool2bv();
SymStack.push(x8395);
}
Stack.push(I32V(1038));
SymStack.push(Concrete(I32V(1038), 32));
{
Stack.pop();
SymVal x8396 = SymStack.pop();
SymVal x8397 = x8396.makeI32Symbol();
Stack.push(SymEnv.read(x8397));
SymStack.push(x8397);
}
Stack.push(I32V(1034));
SymStack.push(Concrete(I32V(1034), 32));
{
Stack.pop();
SymVal x8398 = SymStack.pop();
SymVal x8399 = x8398.makeI32Symbol();
Stack.push(SymEnv.read(x8399));
SymStack.push(x8399);
}
{
Num x8400 = Stack.pop();
SymVal x8401 = SymStack.pop();
Num x8402 = Stack.pop();
SymVal x8403 = SymStack.pop();
Num x8404 = x8402.i32_ne(x8400);
Stack.push(x8404);
bool x8405 = allConcrete(x8403, x8401);
SymVal x8406 = x8405 ? Concrete(x8404, 32) : x8403.neq(x8401).bool2bv();
SymStack.push(x8406);
}
Stack.push(I32V(1038));
SymStack.push(Concrete(I32V(1038), 32));
{
Stack.pop();
SymVal x8407 = SymStack.pop();
SymVal x8408 = x8407.makeI32Symbol();
Stack.push(SymEnv.read(x8408));
SymStack.push(x8408);
}
Stack.push(I32V(1036));
SymStack.push(Concrete(I32V(1036), 32));
{
Stack.pop();
SymVal x8409 = SymStack.pop();
SymVal x8410 = x8409.makeI32Symbol();
Stack.push(SymEnv.read(x8410));
SymStack.push(x8410);
}
{
Num x8411 = Stack.pop();
SymVal x8412 = SymStack.pop();
Num x8413 = Stack.pop();
SymVal x8414 = SymStack.pop();
Num x8415 = x8413.i32_ne(x8411);
Stack.push(x8415);
bool x8416 = allConcrete(x8414, x8412);
SymVal x8417 = x8416 ? Concrete(x8415, 32) : x8414.neq(x8412).bool2bv();
SymStack.push(x8417);
}
Stack.push(I32V(1040));
SymStack.push(Concrete(I32V(1040), 32));
{
Stack.pop();
SymVal x8418 = SymStack.pop();
SymVal x8419 = x8418.makeI32Symbol();
Stack.push(SymEnv.read(x8419));
SymStack.push(x8419);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
{
Stack.pop();
SymVal x8420 = SymStack.pop();
SymVal x8421 = x8420.makeI32Symbol();
Stack.push(SymEnv.read(x8421));
SymStack.push(x8421);
}
{
Num x8422 = Stack.pop();
SymVal x8423 = SymStack.pop();
Num x8424 = Stack.pop();
SymVal x8425 = SymStack.pop();
Num x8426 = x8424.i32_ne(x8422);
Stack.push(x8426);
bool x8427 = allConcrete(x8425, x8423);
SymVal x8428 = x8427 ? Concrete(x8426, 32) : x8425.neq(x8423).bool2bv();
SymStack.push(x8428);
}
Stack.push(I32V(1040));
SymStack.push(Concrete(I32V(1040), 32));
{
Stack.pop();
SymVal x8429 = SymStack.pop();
SymVal x8430 = x8429.makeI32Symbol();
Stack.push(SymEnv.read(x8430));
SymStack.push(x8430);
}
Stack.push(I32V(1026));
SymStack.push(Concrete(I32V(1026), 32));
{
Stack.pop();
SymVal x8431 = SymStack.pop();
SymVal x8432 = x8431.makeI32Symbol();
Stack.push(SymEnv.read(x8432));
SymStack.push(x8432);
}
{
Num x8433 = Stack.pop();
SymVal x8434 = SymStack.pop();
Num x8435 = Stack.pop();
SymVal x8436 = SymStack.pop();
Num x8437 = x8435.i32_ne(x8433);
Stack.push(x8437);
bool x8438 = allConcrete(x8436, x8434);
SymVal x8439 = x8438 ? Concrete(x8437, 32) : x8436.neq(x8434).bool2bv();
SymStack.push(x8439);
}
Stack.push(I32V(1040));
SymStack.push(Concrete(I32V(1040), 32));
{
Stack.pop();
SymVal x8440 = SymStack.pop();
SymVal x8441 = x8440.makeI32Symbol();
Stack.push(SymEnv.read(x8441));
SymStack.push(x8441);
}
Stack.push(I32V(1028));
SymStack.push(Concrete(I32V(1028), 32));
{
Stack.pop();
SymVal x8442 = SymStack.pop();
SymVal x8443 = x8442.makeI32Symbol();
Stack.push(SymEnv.read(x8443));
SymStack.push(x8443);
}
{
Num x8444 = Stack.pop();
SymVal x8445 = SymStack.pop();
Num x8446 = Stack.pop();
SymVal x8447 = SymStack.pop();
Num x8448 = x8446.i32_ne(x8444);
Stack.push(x8448);
bool x8449 = allConcrete(x8447, x8445);
SymVal x8450 = x8449 ? Concrete(x8448, 32) : x8447.neq(x8445).bool2bv();
SymStack.push(x8450);
}
Stack.push(I32V(1040));
SymStack.push(Concrete(I32V(1040), 32));
{
Stack.pop();
SymVal x8451 = SymStack.pop();
SymVal x8452 = x8451.makeI32Symbol();
Stack.push(SymEnv.read(x8452));
SymStack.push(x8452);
}
Stack.push(I32V(1030));
SymStack.push(Concrete(I32V(1030), 32));
{
Stack.pop();
SymVal x8453 = SymStack.pop();
SymVal x8454 = x8453.makeI32Symbol();
Stack.push(SymEnv.read(x8454));
SymStack.push(x8454);
}
{
Num x8455 = Stack.pop();
SymVal x8456 = SymStack.pop();
Num x8457 = Stack.pop();
SymVal x8458 = SymStack.pop();
Num x8459 = x8457.i32_ne(x8455);
Stack.push(x8459);
bool x8460 = allConcrete(x8458, x8456);
SymVal x8461 = x8460 ? Concrete(x8459, 32) : x8458.neq(x8456).bool2bv();
SymStack.push(x8461);
}
Stack.push(I32V(1040));
SymStack.push(Concrete(I32V(1040), 32));
{
Stack.pop();
SymVal x8462 = SymStack.pop();
SymVal x8463 = x8462.makeI32Symbol();
Stack.push(SymEnv.read(x8463));
SymStack.push(x8463);
}
Stack.push(I32V(1032));
SymStack.push(Concrete(I32V(1032), 32));
{
Stack.pop();
SymVal x8464 = SymStack.pop();
SymVal x8465 = x8464.makeI32Symbol();
Stack.push(SymEnv.read(x8465));
SymStack.push(x8465);
}
{
Num x8466 = Stack.pop();
SymVal x8467 = SymStack.pop();
Num x8468 = Stack.pop();
SymVal x8469 = SymStack.pop();
Num x8470 = x8468.i32_ne(x8466);
Stack.push(x8470);
bool x8471 = allConcrete(x8469, x8467);
SymVal x8472 = x8471 ? Concrete(x8470, 32) : x8469.neq(x8467).bool2bv();
SymStack.push(x8472);
}
Stack.push(I32V(1040));
SymStack.push(Concrete(I32V(1040), 32));
{
Stack.pop();
SymVal x8473 = SymStack.pop();
SymVal x8474 = x8473.makeI32Symbol();
Stack.push(SymEnv.read(x8474));
SymStack.push(x8474);
}
Stack.push(I32V(1034));
SymStack.push(Concrete(I32V(1034), 32));
{
Stack.pop();
SymVal x8475 = SymStack.pop();
SymVal x8476 = x8475.makeI32Symbol();
Stack.push(SymEnv.read(x8476));
SymStack.push(x8476);
}
{
Num x8477 = Stack.pop();
SymVal x8478 = SymStack.pop();
Num x8479 = Stack.pop();
SymVal x8480 = SymStack.pop();
Num x8481 = x8479.i32_ne(x8477);
Stack.push(x8481);
bool x8482 = allConcrete(x8480, x8478);
SymVal x8483 = x8482 ? Concrete(x8481, 32) : x8480.neq(x8478).bool2bv();
SymStack.push(x8483);
}
Stack.push(I32V(1040));
SymStack.push(Concrete(I32V(1040), 32));
{
Stack.pop();
SymVal x8484 = SymStack.pop();
SymVal x8485 = x8484.makeI32Symbol();
Stack.push(SymEnv.read(x8485));
SymStack.push(x8485);
}
Stack.push(I32V(1036));
SymStack.push(Concrete(I32V(1036), 32));
{
Stack.pop();
SymVal x8486 = SymStack.pop();
SymVal x8487 = x8486.makeI32Symbol();
Stack.push(SymEnv.read(x8487));
SymStack.push(x8487);
}
{
Num x8488 = Stack.pop();
SymVal x8489 = SymStack.pop();
Num x8490 = Stack.pop();
SymVal x8491 = SymStack.pop();
Num x8492 = x8490.i32_ne(x8488);
Stack.push(x8492);
bool x8493 = allConcrete(x8491, x8489);
SymVal x8494 = x8493 ? Concrete(x8492, 32) : x8491.neq(x8489).bool2bv();
SymStack.push(x8494);
}
Stack.push(I32V(1040));
SymStack.push(Concrete(I32V(1040), 32));
{
Stack.pop();
SymVal x8495 = SymStack.pop();
SymVal x8496 = x8495.makeI32Symbol();
Stack.push(SymEnv.read(x8496));
SymStack.push(x8496);
}
Stack.push(I32V(1038));
SymStack.push(Concrete(I32V(1038), 32));
{
Stack.pop();
SymVal x8497 = SymStack.pop();
SymVal x8498 = x8497.makeI32Symbol();
Stack.push(SymEnv.read(x8498));
SymStack.push(x8498);
}
{
Num x8499 = Stack.pop();
SymVal x8500 = SymStack.pop();
Num x8501 = Stack.pop();
SymVal x8502 = SymStack.pop();
Num x8503 = x8501.i32_ne(x8499);
Stack.push(x8503);
bool x8504 = allConcrete(x8502, x8500);
SymVal x8505 = x8504 ? Concrete(x8503, 32) : x8502.neq(x8500).bool2bv();
SymStack.push(x8505);
}
Stack.push(I32V(1042));
SymStack.push(Concrete(I32V(1042), 32));
{
Stack.pop();
SymVal x8506 = SymStack.pop();
SymVal x8507 = x8506.makeI32Symbol();
Stack.push(SymEnv.read(x8507));
SymStack.push(x8507);
}
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
{
Stack.pop();
SymVal x8508 = SymStack.pop();
SymVal x8509 = x8508.makeI32Symbol();
Stack.push(SymEnv.read(x8509));
SymStack.push(x8509);
}
{
Num x8510 = Stack.pop();
SymVal x8511 = SymStack.pop();
Num x8512 = Stack.pop();
SymVal x8513 = SymStack.pop();
Num x8514 = x8512.i32_ne(x8510);
Stack.push(x8514);
bool x8515 = allConcrete(x8513, x8511);
SymVal x8516 = x8515 ? Concrete(x8514, 32) : x8513.neq(x8511).bool2bv();
SymStack.push(x8516);
}
Stack.push(I32V(1042));
SymStack.push(Concrete(I32V(1042), 32));
{
Stack.pop();
SymVal x8517 = SymStack.pop();
SymVal x8518 = x8517.makeI32Symbol();
Stack.push(SymEnv.read(x8518));
SymStack.push(x8518);
}
Stack.push(I32V(1026));
SymStack.push(Concrete(I32V(1026), 32));
{
Stack.pop();
SymVal x8519 = SymStack.pop();
SymVal x8520 = x8519.makeI32Symbol();
Stack.push(SymEnv.read(x8520));
SymStack.push(x8520);
}
{
Num x8521 = Stack.pop();
SymVal x8522 = SymStack.pop();
Num x8523 = Stack.pop();
SymVal x8524 = SymStack.pop();
Num x8525 = x8523.i32_ne(x8521);
Stack.push(x8525);
bool x8526 = allConcrete(x8524, x8522);
SymVal x8527 = x8526 ? Concrete(x8525, 32) : x8524.neq(x8522).bool2bv();
SymStack.push(x8527);
}
Stack.push(I32V(1042));
SymStack.push(Concrete(I32V(1042), 32));
{
Stack.pop();
SymVal x8528 = SymStack.pop();
SymVal x8529 = x8528.makeI32Symbol();
Stack.push(SymEnv.read(x8529));
SymStack.push(x8529);
}
Stack.push(I32V(1028));
SymStack.push(Concrete(I32V(1028), 32));
{
Stack.pop();
SymVal x8530 = SymStack.pop();
SymVal x8531 = x8530.makeI32Symbol();
Stack.push(SymEnv.read(x8531));
SymStack.push(x8531);
}
{
Num x8532 = Stack.pop();
SymVal x8533 = SymStack.pop();
Num x8534 = Stack.pop();
SymVal x8535 = SymStack.pop();
Num x8536 = x8534.i32_ne(x8532);
Stack.push(x8536);
bool x8537 = allConcrete(x8535, x8533);
SymVal x8538 = x8537 ? Concrete(x8536, 32) : x8535.neq(x8533).bool2bv();
SymStack.push(x8538);
}
Stack.push(I32V(1042));
SymStack.push(Concrete(I32V(1042), 32));
{
Stack.pop();
SymVal x8539 = SymStack.pop();
SymVal x8540 = x8539.makeI32Symbol();
Stack.push(SymEnv.read(x8540));
SymStack.push(x8540);
}
Stack.push(I32V(1030));
SymStack.push(Concrete(I32V(1030), 32));
{
Stack.pop();
SymVal x8541 = SymStack.pop();
SymVal x8542 = x8541.makeI32Symbol();
Stack.push(SymEnv.read(x8542));
SymStack.push(x8542);
}
{
Num x8543 = Stack.pop();
SymVal x8544 = SymStack.pop();
Num x8545 = Stack.pop();
SymVal x8546 = SymStack.pop();
Num x8547 = x8545.i32_ne(x8543);
Stack.push(x8547);
bool x8548 = allConcrete(x8546, x8544);
SymVal x8549 = x8548 ? Concrete(x8547, 32) : x8546.neq(x8544).bool2bv();
SymStack.push(x8549);
}
Stack.push(I32V(1042));
SymStack.push(Concrete(I32V(1042), 32));
{
Stack.pop();
SymVal x8550 = SymStack.pop();
SymVal x8551 = x8550.makeI32Symbol();
Stack.push(SymEnv.read(x8551));
SymStack.push(x8551);
}
Stack.push(I32V(1032));
SymStack.push(Concrete(I32V(1032), 32));
{
Stack.pop();
SymVal x8552 = SymStack.pop();
SymVal x8553 = x8552.makeI32Symbol();
Stack.push(SymEnv.read(x8553));
SymStack.push(x8553);
}
{
Num x8554 = Stack.pop();
SymVal x8555 = SymStack.pop();
Num x8556 = Stack.pop();
SymVal x8557 = SymStack.pop();
Num x8558 = x8556.i32_ne(x8554);
Stack.push(x8558);
bool x8559 = allConcrete(x8557, x8555);
SymVal x8560 = x8559 ? Concrete(x8558, 32) : x8557.neq(x8555).bool2bv();
SymStack.push(x8560);
}
Stack.push(I32V(1042));
SymStack.push(Concrete(I32V(1042), 32));
{
Stack.pop();
SymVal x8561 = SymStack.pop();
SymVal x8562 = x8561.makeI32Symbol();
Stack.push(SymEnv.read(x8562));
SymStack.push(x8562);
}
Stack.push(I32V(1034));
SymStack.push(Concrete(I32V(1034), 32));
{
Stack.pop();
SymVal x8563 = SymStack.pop();
SymVal x8564 = x8563.makeI32Symbol();
Stack.push(SymEnv.read(x8564));
SymStack.push(x8564);
}
{
Num x8565 = Stack.pop();
SymVal x8566 = SymStack.pop();
Num x8567 = Stack.pop();
SymVal x8568 = SymStack.pop();
Num x8569 = x8567.i32_ne(x8565);
Stack.push(x8569);
bool x8570 = allConcrete(x8568, x8566);
SymVal x8571 = x8570 ? Concrete(x8569, 32) : x8568.neq(x8566).bool2bv();
SymStack.push(x8571);
}
Stack.push(I32V(1042));
SymStack.push(Concrete(I32V(1042), 32));
{
Stack.pop();
SymVal x8572 = SymStack.pop();
SymVal x8573 = x8572.makeI32Symbol();
Stack.push(SymEnv.read(x8573));
SymStack.push(x8573);
}
Stack.push(I32V(1036));
SymStack.push(Concrete(I32V(1036), 32));
{
Stack.pop();
SymVal x8574 = SymStack.pop();
SymVal x8575 = x8574.makeI32Symbol();
Stack.push(SymEnv.read(x8575));
SymStack.push(x8575);
}
{
Num x8576 = Stack.pop();
SymVal x8577 = SymStack.pop();
Num x8578 = Stack.pop();
SymVal x8579 = SymStack.pop();
Num x8580 = x8578.i32_ne(x8576);
Stack.push(x8580);
bool x8581 = allConcrete(x8579, x8577);
SymVal x8582 = x8581 ? Concrete(x8580, 32) : x8579.neq(x8577).bool2bv();
SymStack.push(x8582);
}
Stack.push(I32V(1042));
SymStack.push(Concrete(I32V(1042), 32));
{
Stack.pop();
SymVal x8583 = SymStack.pop();
SymVal x8584 = x8583.makeI32Symbol();
Stack.push(SymEnv.read(x8584));
SymStack.push(x8584);
}
Stack.push(I32V(1038));
SymStack.push(Concrete(I32V(1038), 32));
{
Stack.pop();
SymVal x8585 = SymStack.pop();
SymVal x8586 = x8585.makeI32Symbol();
Stack.push(SymEnv.read(x8586));
SymStack.push(x8586);
}
{
Num x8587 = Stack.pop();
SymVal x8588 = SymStack.pop();
Num x8589 = Stack.pop();
SymVal x8590 = SymStack.pop();
Num x8591 = x8589.i32_ne(x8587);
Stack.push(x8591);
bool x8592 = allConcrete(x8590, x8588);
SymVal x8593 = x8592 ? Concrete(x8591, 32) : x8590.neq(x8588).bool2bv();
SymStack.push(x8593);
}
Stack.push(I32V(1042));
SymStack.push(Concrete(I32V(1042), 32));
{
Stack.pop();
SymVal x8594 = SymStack.pop();
SymVal x8595 = x8594.makeI32Symbol();
Stack.push(SymEnv.read(x8595));
SymStack.push(x8595);
}
Stack.push(I32V(1040));
SymStack.push(Concrete(I32V(1040), 32));
{
Stack.pop();
SymVal x8596 = SymStack.pop();
SymVal x8597 = x8596.makeI32Symbol();
Stack.push(SymEnv.read(x8597));
SymStack.push(x8597);
}
{
Num x8598 = Stack.pop();
SymVal x8599 = SymStack.pop();
Num x8600 = Stack.pop();
SymVal x8601 = SymStack.pop();
Num x8602 = x8600.i32_ne(x8598);
Stack.push(x8602);
bool x8603 = allConcrete(x8601, x8599);
SymVal x8604 = x8603 ? Concrete(x8602, 32) : x8601.neq(x8599).bool2bv();
SymStack.push(x8604);
}
{
Num x8605 = Stack.pop();
SymVal x8606 = SymStack.pop();
Num x8607 = Stack.pop();
SymVal x8608 = SymStack.pop();
Num x8609 = x8607.i32_and(x8605);
Stack.push(x8609);
bool x8610 = allConcrete(x8608, x8606);
SymVal x8611 = x8610 ? Concrete(x8609, 32) : x8608.bitwise_and(x8606);
SymStack.push(x8611);
}
{
Num x8612 = Stack.pop();
SymVal x8613 = SymStack.pop();
Num x8614 = Stack.pop();
SymVal x8615 = SymStack.pop();
Num x8616 = x8614.i32_and(x8612);
Stack.push(x8616);
bool x8617 = allConcrete(x8615, x8613);
SymVal x8618 = x8617 ? Concrete(x8616, 32) : x8615.bitwise_and(x8613);
SymStack.push(x8618);
}
{
Num x8619 = Stack.pop();
SymVal x8620 = SymStack.pop();
Num x8621 = Stack.pop();
SymVal x8622 = SymStack.pop();
Num x8623 = x8621.i32_and(x8619);
Stack.push(x8623);
bool x8624 = allConcrete(x8622, x8620);
SymVal x8625 = x8624 ? Concrete(x8623, 32) : x8622.bitwise_and(x8620);
SymStack.push(x8625);
}
{
Num x8626 = Stack.pop();
SymVal x8627 = SymStack.pop();
Num x8628 = Stack.pop();
SymVal x8629 = SymStack.pop();
Num x8630 = x8628.i32_and(x8626);
Stack.push(x8630);
bool x8631 = allConcrete(x8629, x8627);
SymVal x8632 = x8631 ? Concrete(x8630, 32) : x8629.bitwise_and(x8627);
SymStack.push(x8632);
}
{
Num x8633 = Stack.pop();
SymVal x8634 = SymStack.pop();
Num x8635 = Stack.pop();
SymVal x8636 = SymStack.pop();
Num x8637 = x8635.i32_and(x8633);
Stack.push(x8637);
bool x8638 = allConcrete(x8636, x8634);
SymVal x8639 = x8638 ? Concrete(x8637, 32) : x8636.bitwise_and(x8634);
SymStack.push(x8639);
}
{
Num x8640 = Stack.pop();
SymVal x8641 = SymStack.pop();
Num x8642 = Stack.pop();
SymVal x8643 = SymStack.pop();
Num x8644 = x8642.i32_and(x8640);
Stack.push(x8644);
bool x8645 = allConcrete(x8643, x8641);
SymVal x8646 = x8645 ? Concrete(x8644, 32) : x8643.bitwise_and(x8641);
SymStack.push(x8646);
}
{
Num x8647 = Stack.pop();
SymVal x8648 = SymStack.pop();
Num x8649 = Stack.pop();
SymVal x8650 = SymStack.pop();
Num x8651 = x8649.i32_and(x8647);
Stack.push(x8651);
bool x8652 = allConcrete(x8650, x8648);
SymVal x8653 = x8652 ? Concrete(x8651, 32) : x8650.bitwise_and(x8648);
SymStack.push(x8653);
}
{
Num x8654 = Stack.pop();
SymVal x8655 = SymStack.pop();
Num x8656 = Stack.pop();
SymVal x8657 = SymStack.pop();
Num x8658 = x8656.i32_and(x8654);
Stack.push(x8658);
bool x8659 = allConcrete(x8657, x8655);
SymVal x8660 = x8659 ? Concrete(x8658, 32) : x8657.bitwise_and(x8655);
SymStack.push(x8660);
}
{
Num x8661 = Stack.pop();
SymVal x8662 = SymStack.pop();
Num x8663 = Stack.pop();
SymVal x8664 = SymStack.pop();
Num x8665 = x8663.i32_and(x8661);
Stack.push(x8665);
bool x8666 = allConcrete(x8664, x8662);
SymVal x8667 = x8666 ? Concrete(x8665, 32) : x8664.bitwise_and(x8662);
SymStack.push(x8667);
}
{
Num x8668 = Stack.pop();
SymVal x8669 = SymStack.pop();
Num x8670 = Stack.pop();
SymVal x8671 = SymStack.pop();
Num x8672 = x8670.i32_and(x8668);
Stack.push(x8672);
bool x8673 = allConcrete(x8671, x8669);
SymVal x8674 = x8673 ? Concrete(x8672, 32) : x8671.bitwise_and(x8669);
SymStack.push(x8674);
}
{
Num x8675 = Stack.pop();
SymVal x8676 = SymStack.pop();
Num x8677 = Stack.pop();
SymVal x8678 = SymStack.pop();
Num x8679 = x8677.i32_and(x8675);
Stack.push(x8679);
bool x8680 = allConcrete(x8678, x8676);
SymVal x8681 = x8680 ? Concrete(x8679, 32) : x8678.bitwise_and(x8676);
SymStack.push(x8681);
}
{
Num x8682 = Stack.pop();
SymVal x8683 = SymStack.pop();
Num x8684 = Stack.pop();
SymVal x8685 = SymStack.pop();
Num x8686 = x8684.i32_and(x8682);
Stack.push(x8686);
bool x8687 = allConcrete(x8685, x8683);
SymVal x8688 = x8687 ? Concrete(x8686, 32) : x8685.bitwise_and(x8683);
SymStack.push(x8688);
}
{
Num x8689 = Stack.pop();
SymVal x8690 = SymStack.pop();
Num x8691 = Stack.pop();
SymVal x8692 = SymStack.pop();
Num x8693 = x8691.i32_and(x8689);
Stack.push(x8693);
bool x8694 = allConcrete(x8692, x8690);
SymVal x8695 = x8694 ? Concrete(x8693, 32) : x8692.bitwise_and(x8690);
SymStack.push(x8695);
}
{
Num x8696 = Stack.pop();
SymVal x8697 = SymStack.pop();
Num x8698 = Stack.pop();
SymVal x8699 = SymStack.pop();
Num x8700 = x8698.i32_and(x8696);
Stack.push(x8700);
bool x8701 = allConcrete(x8699, x8697);
SymVal x8702 = x8701 ? Concrete(x8700, 32) : x8699.bitwise_and(x8697);
SymStack.push(x8702);
}
{
Num x8703 = Stack.pop();
SymVal x8704 = SymStack.pop();
Num x8705 = Stack.pop();
SymVal x8706 = SymStack.pop();
Num x8707 = x8705.i32_and(x8703);
Stack.push(x8707);
bool x8708 = allConcrete(x8706, x8704);
SymVal x8709 = x8708 ? Concrete(x8707, 32) : x8706.bitwise_and(x8704);
SymStack.push(x8709);
}
{
Num x8710 = Stack.pop();
SymVal x8711 = SymStack.pop();
Num x8712 = Stack.pop();
SymVal x8713 = SymStack.pop();
Num x8714 = x8712.i32_and(x8710);
Stack.push(x8714);
bool x8715 = allConcrete(x8713, x8711);
SymVal x8716 = x8715 ? Concrete(x8714, 32) : x8713.bitwise_and(x8711);
SymStack.push(x8716);
}
{
Num x8717 = Stack.pop();
SymVal x8718 = SymStack.pop();
Num x8719 = Stack.pop();
SymVal x8720 = SymStack.pop();
Num x8721 = x8719.i32_and(x8717);
Stack.push(x8721);
bool x8722 = allConcrete(x8720, x8718);
SymVal x8723 = x8722 ? Concrete(x8721, 32) : x8720.bitwise_and(x8718);
SymStack.push(x8723);
}
{
Num x8724 = Stack.pop();
SymVal x8725 = SymStack.pop();
Num x8726 = Stack.pop();
SymVal x8727 = SymStack.pop();
Num x8728 = x8726.i32_and(x8724);
Stack.push(x8728);
bool x8729 = allConcrete(x8727, x8725);
SymVal x8730 = x8729 ? Concrete(x8728, 32) : x8727.bitwise_and(x8725);
SymStack.push(x8730);
}
{
Num x8731 = Stack.pop();
SymVal x8732 = SymStack.pop();
Num x8733 = Stack.pop();
SymVal x8734 = SymStack.pop();
Num x8735 = x8733.i32_and(x8731);
Stack.push(x8735);
bool x8736 = allConcrete(x8734, x8732);
SymVal x8737 = x8736 ? Concrete(x8735, 32) : x8734.bitwise_and(x8732);
SymStack.push(x8737);
}
{
Num x8738 = Stack.pop();
SymVal x8739 = SymStack.pop();
Num x8740 = Stack.pop();
SymVal x8741 = SymStack.pop();
Num x8742 = x8740.i32_and(x8738);
Stack.push(x8742);
bool x8743 = allConcrete(x8741, x8739);
SymVal x8744 = x8743 ? Concrete(x8742, 32) : x8741.bitwise_and(x8739);
SymStack.push(x8744);
}
{
Num x8745 = Stack.pop();
SymVal x8746 = SymStack.pop();
Num x8747 = Stack.pop();
SymVal x8748 = SymStack.pop();
Num x8749 = x8747.i32_and(x8745);
Stack.push(x8749);
bool x8750 = allConcrete(x8748, x8746);
SymVal x8751 = x8750 ? Concrete(x8749, 32) : x8748.bitwise_and(x8746);
SymStack.push(x8751);
}
{
Num x8752 = Stack.pop();
SymVal x8753 = SymStack.pop();
Num x8754 = Stack.pop();
SymVal x8755 = SymStack.pop();
Num x8756 = x8754.i32_and(x8752);
Stack.push(x8756);
bool x8757 = allConcrete(x8755, x8753);
SymVal x8758 = x8757 ? Concrete(x8756, 32) : x8755.bitwise_and(x8753);
SymStack.push(x8758);
}
{
Num x8759 = Stack.pop();
SymVal x8760 = SymStack.pop();
Num x8761 = Stack.pop();
SymVal x8762 = SymStack.pop();
Num x8763 = x8761.i32_and(x8759);
Stack.push(x8763);
bool x8764 = allConcrete(x8762, x8760);
SymVal x8765 = x8764 ? Concrete(x8763, 32) : x8762.bitwise_and(x8760);
SymStack.push(x8765);
}
{
Num x8766 = Stack.pop();
SymVal x8767 = SymStack.pop();
Num x8768 = Stack.pop();
SymVal x8769 = SymStack.pop();
Num x8770 = x8768.i32_and(x8766);
Stack.push(x8770);
bool x8771 = allConcrete(x8769, x8767);
SymVal x8772 = x8771 ? Concrete(x8770, 32) : x8769.bitwise_and(x8767);
SymStack.push(x8772);
}
Num x8773 = Stack.pop();
{
SymVal x8774 = SymStack.pop();
ExploreTree.fillIfElseNode(x8774, 1);
}
int x8775 = x8773.toInt();
if (x8775 != 0) {
ExploreTree.moveCursorNoControl(true);
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
{
Stack.pop();
SymVal x8776 = SymStack.pop();
SymVal x8777 = x8776.makeI32Symbol();
Stack.push(SymEnv.read(x8777));
SymStack.push(x8777);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 11);
Num x8778 = Stack.pop();
SymVal x8779 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x8778);
SymFrames.set(0, x8779);
updateCurrentMCont(prependCont(x7844, CURRENT_MCONT));
}
__attribute__((musttail)) return x1593(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x7852, CURRENT_MCONT));
ExploreTree.fillNotToExploredNode();
}
return std::monostate{};
}
std::monostate x7852(std::monostate x7853) {
info("Successfully assumed condition at 1");
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
{
Stack.pop();
SymVal x7854 = SymStack.pop();
SymVal x7855 = x7854.makeI32Symbol();
Stack.push(SymEnv.read(x7855));
SymStack.push(x7855);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 11);
Num x7856 = Stack.pop();
SymVal x7857 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x7856);
SymFrames.set(0, x7857);
updateCurrentMCont(prependCont(x7844, CURRENT_MCONT));
}
__attribute__((musttail)) return x1593(std::monostate{});
return std::monostate{};
}
std::monostate x7844(std::monostate x7845) {
infoWhen("CALL", "Returning from the function at 11, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
{
Num x7846 = Stack.pop();
SymVal x7847 = SymStack.pop();
Frames.set(0, x7846);
SymFrames.set(0, x7847);
}
Stack.push(I32V(1026));
SymStack.push(Concrete(I32V(1026), 32));
{
Stack.pop();
SymVal x7848 = SymStack.pop();
SymVal x7849 = x7848.makeI32Symbol();
Stack.push(SymEnv.read(x7849));
SymStack.push(x7849);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 11);
Num x7850 = Stack.pop();
SymVal x7851 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x7850);
SymFrames.set(0, x7851);
updateCurrentMCont(prependCont(x7836, CURRENT_MCONT));
}
__attribute__((musttail)) return x1593(std::monostate{});
return std::monostate{};
}
std::monostate x7836(std::monostate x7837) {
infoWhen("CALL", "Returning from the function at 11, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
{
Num x7838 = Stack.pop();
SymVal x7839 = SymStack.pop();
Frames.set(0, x7838);
SymFrames.set(0, x7839);
}
Stack.push(I32V(1028));
SymStack.push(Concrete(I32V(1028), 32));
{
Stack.pop();
SymVal x7840 = SymStack.pop();
SymVal x7841 = x7840.makeI32Symbol();
Stack.push(SymEnv.read(x7841));
SymStack.push(x7841);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 11);
Num x7842 = Stack.pop();
SymVal x7843 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x7842);
SymFrames.set(0, x7843);
updateCurrentMCont(prependCont(x7828, CURRENT_MCONT));
}
__attribute__((musttail)) return x1593(std::monostate{});
return std::monostate{};
}
std::monostate x7828(std::monostate x7829) {
infoWhen("CALL", "Returning from the function at 11, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
{
Num x7830 = Stack.pop();
SymVal x7831 = SymStack.pop();
Frames.set(0, x7830);
SymFrames.set(0, x7831);
}
Stack.push(I32V(1030));
SymStack.push(Concrete(I32V(1030), 32));
{
Stack.pop();
SymVal x7832 = SymStack.pop();
SymVal x7833 = x7832.makeI32Symbol();
Stack.push(SymEnv.read(x7833));
SymStack.push(x7833);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 11);
Num x7834 = Stack.pop();
SymVal x7835 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x7834);
SymFrames.set(0, x7835);
updateCurrentMCont(prependCont(x7820, CURRENT_MCONT));
}
__attribute__((musttail)) return x1593(std::monostate{});
return std::monostate{};
}
std::monostate x7820(std::monostate x7821) {
infoWhen("CALL", "Returning from the function at 11, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
{
Num x7822 = Stack.pop();
SymVal x7823 = SymStack.pop();
Frames.set(0, x7822);
SymFrames.set(0, x7823);
}
Stack.push(I32V(1032));
SymStack.push(Concrete(I32V(1032), 32));
{
Stack.pop();
SymVal x7824 = SymStack.pop();
SymVal x7825 = x7824.makeI32Symbol();
Stack.push(SymEnv.read(x7825));
SymStack.push(x7825);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 11);
Num x7826 = Stack.pop();
SymVal x7827 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x7826);
SymFrames.set(0, x7827);
updateCurrentMCont(prependCont(x7812, CURRENT_MCONT));
}
__attribute__((musttail)) return x1593(std::monostate{});
return std::monostate{};
}
std::monostate x7812(std::monostate x7813) {
infoWhen("CALL", "Returning from the function at 11, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
{
Num x7814 = Stack.pop();
SymVal x7815 = SymStack.pop();
Frames.set(0, x7814);
SymFrames.set(0, x7815);
}
Stack.push(I32V(1034));
SymStack.push(Concrete(I32V(1034), 32));
{
Stack.pop();
SymVal x7816 = SymStack.pop();
SymVal x7817 = x7816.makeI32Symbol();
Stack.push(SymEnv.read(x7817));
SymStack.push(x7817);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 11);
Num x7818 = Stack.pop();
SymVal x7819 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x7818);
SymFrames.set(0, x7819);
updateCurrentMCont(prependCont(x7804, CURRENT_MCONT));
}
__attribute__((musttail)) return x1593(std::monostate{});
return std::monostate{};
}
std::monostate x7804(std::monostate x7805) {
infoWhen("CALL", "Returning from the function at 11, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
{
Num x7806 = Stack.pop();
SymVal x7807 = SymStack.pop();
Frames.set(0, x7806);
SymFrames.set(0, x7807);
}
Stack.push(I32V(1036));
SymStack.push(Concrete(I32V(1036), 32));
{
Stack.pop();
SymVal x7808 = SymStack.pop();
SymVal x7809 = x7808.makeI32Symbol();
Stack.push(SymEnv.read(x7809));
SymStack.push(x7809);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 11);
Num x7810 = Stack.pop();
SymVal x7811 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x7810);
SymFrames.set(0, x7811);
updateCurrentMCont(prependCont(x7796, CURRENT_MCONT));
}
__attribute__((musttail)) return x1593(std::monostate{});
return std::monostate{};
}
std::monostate x7796(std::monostate x7797) {
infoWhen("CALL", "Returning from the function at 11, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
{
Num x7798 = Stack.pop();
SymVal x7799 = SymStack.pop();
Frames.set(0, x7798);
SymFrames.set(0, x7799);
}
Stack.push(I32V(1038));
SymStack.push(Concrete(I32V(1038), 32));
{
Stack.pop();
SymVal x7800 = SymStack.pop();
SymVal x7801 = x7800.makeI32Symbol();
Stack.push(SymEnv.read(x7801));
SymStack.push(x7801);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 11);
Num x7802 = Stack.pop();
SymVal x7803 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x7802);
SymFrames.set(0, x7803);
updateCurrentMCont(prependCont(x7788, CURRENT_MCONT));
}
__attribute__((musttail)) return x1593(std::monostate{});
return std::monostate{};
}
std::monostate x7788(std::monostate x7789) {
infoWhen("CALL", "Returning from the function at 11, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
{
Num x7790 = Stack.pop();
SymVal x7791 = SymStack.pop();
Frames.set(0, x7790);
SymFrames.set(0, x7791);
}
Stack.push(I32V(1040));
SymStack.push(Concrete(I32V(1040), 32));
{
Stack.pop();
SymVal x7792 = SymStack.pop();
SymVal x7793 = x7792.makeI32Symbol();
Stack.push(SymEnv.read(x7793));
SymStack.push(x7793);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 11);
Num x7794 = Stack.pop();
SymVal x7795 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x7794);
SymFrames.set(0, x7795);
updateCurrentMCont(prependCont(x7780, CURRENT_MCONT));
}
__attribute__((musttail)) return x1593(std::monostate{});
return std::monostate{};
}
std::monostate x7780(std::monostate x7781) {
infoWhen("CALL", "Returning from the function at 11, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
{
Num x7782 = Stack.pop();
SymVal x7783 = SymStack.pop();
Frames.set(0, x7782);
SymFrames.set(0, x7783);
}
Stack.push(I32V(1042));
SymStack.push(Concrete(I32V(1042), 32));
{
Stack.pop();
SymVal x7784 = SymStack.pop();
SymVal x7785 = x7784.makeI32Symbol();
Stack.push(SymEnv.read(x7785));
SymStack.push(x7785);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 11);
Num x7786 = Stack.pop();
SymVal x7787 = SymStack.pop();
Frames.pushFrameCaller(1);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
Frames.set(0, x7786);
SymFrames.set(0, x7787);
updateCurrentMCont(prependCont(x7770, CURRENT_MCONT));
}
__attribute__((musttail)) return x1593(std::monostate{});
return std::monostate{};
}
std::monostate x7770(std::monostate x7771) {
infoWhen("CALL", "Returning from the function at 11, stackSize =", Stack.size());
Frames.popFrameCaller(1);
SymFrames.popFrameCaller(1);
{
Num x7772 = Stack.pop();
SymVal x7773 = SymStack.pop();
Frames.set(0, x7772);
SymFrames.set(0, x7773);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
{
Stack.pop();
SymVal x7774 = SymStack.pop();
SymVal x7775 = x7774.makeI32Symbol();
Stack.push(SymEnv.read(x7775));
SymStack.push(x7775);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7776 = Stack.pop();
Num x7777 = Stack.pop();
SymVal x7778 = SymStack.pop();
SymVal x7779 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7777);
Frames.set(1, x7776);
SymFrames.set(0, x7779);
SymFrames.set(1, x7778);
updateCurrentMCont(prependCont(x7755, CURRENT_MCONT));
}
__attribute__((musttail)) return x1678(std::monostate{});
return std::monostate{};
}
std::monostate x7755(std::monostate x7756) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7757 = Stack.pop();
SymVal x7758 = SymStack.pop();
Num x7759 = Stack.pop();
SymVal x7760 = SymStack.pop();
Num x7761 = x7759.i32_ne(x7757);
Stack.push(x7761);
bool x7762 = allConcrete(x7760, x7758);
SymVal x7763 = x7762 ? Concrete(x7761, 32) : x7760.neq(x7758).bool2bv();
SymStack.push(x7763);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1026));
SymStack.push(Concrete(I32V(1026), 32));
{
Stack.pop();
SymVal x7764 = SymStack.pop();
SymVal x7765 = x7764.makeI32Symbol();
Stack.push(SymEnv.read(x7765));
SymStack.push(x7765);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7766 = Stack.pop();
Num x7767 = Stack.pop();
SymVal x7768 = SymStack.pop();
SymVal x7769 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7767);
Frames.set(1, x7766);
SymFrames.set(0, x7769);
SymFrames.set(1, x7768);
updateCurrentMCont(prependCont(x7740, CURRENT_MCONT));
}
__attribute__((musttail)) return x1678(std::monostate{});
return std::monostate{};
}
std::monostate x7740(std::monostate x7741) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7742 = Stack.pop();
SymVal x7743 = SymStack.pop();
Num x7744 = Stack.pop();
SymVal x7745 = SymStack.pop();
Num x7746 = x7744.i32_ne(x7742);
Stack.push(x7746);
bool x7747 = allConcrete(x7745, x7743);
SymVal x7748 = x7747 ? Concrete(x7746, 32) : x7745.neq(x7743).bool2bv();
SymStack.push(x7748);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1028));
SymStack.push(Concrete(I32V(1028), 32));
{
Stack.pop();
SymVal x7749 = SymStack.pop();
SymVal x7750 = x7749.makeI32Symbol();
Stack.push(SymEnv.read(x7750));
SymStack.push(x7750);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7751 = Stack.pop();
Num x7752 = Stack.pop();
SymVal x7753 = SymStack.pop();
SymVal x7754 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7752);
Frames.set(1, x7751);
SymFrames.set(0, x7754);
SymFrames.set(1, x7753);
updateCurrentMCont(prependCont(x7725, CURRENT_MCONT));
}
__attribute__((musttail)) return x1678(std::monostate{});
return std::monostate{};
}
std::monostate x7725(std::monostate x7726) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7727 = Stack.pop();
SymVal x7728 = SymStack.pop();
Num x7729 = Stack.pop();
SymVal x7730 = SymStack.pop();
Num x7731 = x7729.i32_ne(x7727);
Stack.push(x7731);
bool x7732 = allConcrete(x7730, x7728);
SymVal x7733 = x7732 ? Concrete(x7731, 32) : x7730.neq(x7728).bool2bv();
SymStack.push(x7733);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1030));
SymStack.push(Concrete(I32V(1030), 32));
{
Stack.pop();
SymVal x7734 = SymStack.pop();
SymVal x7735 = x7734.makeI32Symbol();
Stack.push(SymEnv.read(x7735));
SymStack.push(x7735);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7736 = Stack.pop();
Num x7737 = Stack.pop();
SymVal x7738 = SymStack.pop();
SymVal x7739 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7737);
Frames.set(1, x7736);
SymFrames.set(0, x7739);
SymFrames.set(1, x7738);
updateCurrentMCont(prependCont(x7710, CURRENT_MCONT));
}
__attribute__((musttail)) return x1678(std::monostate{});
return std::monostate{};
}
std::monostate x7710(std::monostate x7711) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7712 = Stack.pop();
SymVal x7713 = SymStack.pop();
Num x7714 = Stack.pop();
SymVal x7715 = SymStack.pop();
Num x7716 = x7714.i32_ne(x7712);
Stack.push(x7716);
bool x7717 = allConcrete(x7715, x7713);
SymVal x7718 = x7717 ? Concrete(x7716, 32) : x7715.neq(x7713).bool2bv();
SymStack.push(x7718);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1032));
SymStack.push(Concrete(I32V(1032), 32));
{
Stack.pop();
SymVal x7719 = SymStack.pop();
SymVal x7720 = x7719.makeI32Symbol();
Stack.push(SymEnv.read(x7720));
SymStack.push(x7720);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7721 = Stack.pop();
Num x7722 = Stack.pop();
SymVal x7723 = SymStack.pop();
SymVal x7724 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7722);
Frames.set(1, x7721);
SymFrames.set(0, x7724);
SymFrames.set(1, x7723);
updateCurrentMCont(prependCont(x7695, CURRENT_MCONT));
}
__attribute__((musttail)) return x1678(std::monostate{});
return std::monostate{};
}
std::monostate x7695(std::monostate x7696) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7697 = Stack.pop();
SymVal x7698 = SymStack.pop();
Num x7699 = Stack.pop();
SymVal x7700 = SymStack.pop();
Num x7701 = x7699.i32_ne(x7697);
Stack.push(x7701);
bool x7702 = allConcrete(x7700, x7698);
SymVal x7703 = x7702 ? Concrete(x7701, 32) : x7700.neq(x7698).bool2bv();
SymStack.push(x7703);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1034));
SymStack.push(Concrete(I32V(1034), 32));
{
Stack.pop();
SymVal x7704 = SymStack.pop();
SymVal x7705 = x7704.makeI32Symbol();
Stack.push(SymEnv.read(x7705));
SymStack.push(x7705);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7706 = Stack.pop();
Num x7707 = Stack.pop();
SymVal x7708 = SymStack.pop();
SymVal x7709 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7707);
Frames.set(1, x7706);
SymFrames.set(0, x7709);
SymFrames.set(1, x7708);
updateCurrentMCont(prependCont(x7680, CURRENT_MCONT));
}
__attribute__((musttail)) return x1678(std::monostate{});
return std::monostate{};
}
std::monostate x7680(std::monostate x7681) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7682 = Stack.pop();
SymVal x7683 = SymStack.pop();
Num x7684 = Stack.pop();
SymVal x7685 = SymStack.pop();
Num x7686 = x7684.i32_ne(x7682);
Stack.push(x7686);
bool x7687 = allConcrete(x7685, x7683);
SymVal x7688 = x7687 ? Concrete(x7686, 32) : x7685.neq(x7683).bool2bv();
SymStack.push(x7688);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1036));
SymStack.push(Concrete(I32V(1036), 32));
{
Stack.pop();
SymVal x7689 = SymStack.pop();
SymVal x7690 = x7689.makeI32Symbol();
Stack.push(SymEnv.read(x7690));
SymStack.push(x7690);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7691 = Stack.pop();
Num x7692 = Stack.pop();
SymVal x7693 = SymStack.pop();
SymVal x7694 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7692);
Frames.set(1, x7691);
SymFrames.set(0, x7694);
SymFrames.set(1, x7693);
updateCurrentMCont(prependCont(x7665, CURRENT_MCONT));
}
__attribute__((musttail)) return x1678(std::monostate{});
return std::monostate{};
}
std::monostate x7665(std::monostate x7666) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7667 = Stack.pop();
SymVal x7668 = SymStack.pop();
Num x7669 = Stack.pop();
SymVal x7670 = SymStack.pop();
Num x7671 = x7669.i32_ne(x7667);
Stack.push(x7671);
bool x7672 = allConcrete(x7670, x7668);
SymVal x7673 = x7672 ? Concrete(x7671, 32) : x7670.neq(x7668).bool2bv();
SymStack.push(x7673);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1038));
SymStack.push(Concrete(I32V(1038), 32));
{
Stack.pop();
SymVal x7674 = SymStack.pop();
SymVal x7675 = x7674.makeI32Symbol();
Stack.push(SymEnv.read(x7675));
SymStack.push(x7675);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7676 = Stack.pop();
Num x7677 = Stack.pop();
SymVal x7678 = SymStack.pop();
SymVal x7679 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7677);
Frames.set(1, x7676);
SymFrames.set(0, x7679);
SymFrames.set(1, x7678);
updateCurrentMCont(prependCont(x7650, CURRENT_MCONT));
}
__attribute__((musttail)) return x1678(std::monostate{});
return std::monostate{};
}
std::monostate x7650(std::monostate x7651) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7652 = Stack.pop();
SymVal x7653 = SymStack.pop();
Num x7654 = Stack.pop();
SymVal x7655 = SymStack.pop();
Num x7656 = x7654.i32_ne(x7652);
Stack.push(x7656);
bool x7657 = allConcrete(x7655, x7653);
SymVal x7658 = x7657 ? Concrete(x7656, 32) : x7655.neq(x7653).bool2bv();
SymStack.push(x7658);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1040));
SymStack.push(Concrete(I32V(1040), 32));
{
Stack.pop();
SymVal x7659 = SymStack.pop();
SymVal x7660 = x7659.makeI32Symbol();
Stack.push(SymEnv.read(x7660));
SymStack.push(x7660);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7661 = Stack.pop();
Num x7662 = Stack.pop();
SymVal x7663 = SymStack.pop();
SymVal x7664 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7662);
Frames.set(1, x7661);
SymFrames.set(0, x7664);
SymFrames.set(1, x7663);
updateCurrentMCont(prependCont(x7635, CURRENT_MCONT));
}
__attribute__((musttail)) return x1678(std::monostate{});
return std::monostate{};
}
std::monostate x7635(std::monostate x7636) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7637 = Stack.pop();
SymVal x7638 = SymStack.pop();
Num x7639 = Stack.pop();
SymVal x7640 = SymStack.pop();
Num x7641 = x7639.i32_ne(x7637);
Stack.push(x7641);
bool x7642 = allConcrete(x7640, x7638);
SymVal x7643 = x7642 ? Concrete(x7641, 32) : x7640.neq(x7638).bool2bv();
SymStack.push(x7643);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1042));
SymStack.push(Concrete(I32V(1042), 32));
{
Stack.pop();
SymVal x7644 = SymStack.pop();
SymVal x7645 = x7644.makeI32Symbol();
Stack.push(SymEnv.read(x7645));
SymStack.push(x7645);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7646 = Stack.pop();
Num x7647 = Stack.pop();
SymVal x7648 = SymStack.pop();
SymVal x7649 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7647);
Frames.set(1, x7646);
SymFrames.set(0, x7649);
SymFrames.set(1, x7648);
updateCurrentMCont(prependCont(x7555, CURRENT_MCONT));
}
__attribute__((musttail)) return x1678(std::monostate{});
return std::monostate{};
}
std::monostate x7555(std::monostate x7556) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7557 = Stack.pop();
SymVal x7558 = SymStack.pop();
Num x7559 = Stack.pop();
SymVal x7560 = SymStack.pop();
Num x7561 = x7559.i32_ne(x7557);
Stack.push(x7561);
bool x7562 = allConcrete(x7560, x7558);
SymVal x7563 = x7562 ? Concrete(x7561, 32) : x7560.neq(x7558).bool2bv();
SymStack.push(x7563);
}
{
Num x7564 = Stack.pop();
SymVal x7565 = SymStack.pop();
Num x7566 = Stack.pop();
SymVal x7567 = SymStack.pop();
Num x7568 = x7566.i32_and(x7564);
Stack.push(x7568);
bool x7569 = allConcrete(x7567, x7565);
SymVal x7570 = x7569 ? Concrete(x7568, 32) : x7567.bitwise_and(x7565);
SymStack.push(x7570);
}
{
Num x7571 = Stack.pop();
SymVal x7572 = SymStack.pop();
Num x7573 = Stack.pop();
SymVal x7574 = SymStack.pop();
Num x7575 = x7573.i32_and(x7571);
Stack.push(x7575);
bool x7576 = allConcrete(x7574, x7572);
SymVal x7577 = x7576 ? Concrete(x7575, 32) : x7574.bitwise_and(x7572);
SymStack.push(x7577);
}
{
Num x7578 = Stack.pop();
SymVal x7579 = SymStack.pop();
Num x7580 = Stack.pop();
SymVal x7581 = SymStack.pop();
Num x7582 = x7580.i32_and(x7578);
Stack.push(x7582);
bool x7583 = allConcrete(x7581, x7579);
SymVal x7584 = x7583 ? Concrete(x7582, 32) : x7581.bitwise_and(x7579);
SymStack.push(x7584);
}
{
Num x7585 = Stack.pop();
SymVal x7586 = SymStack.pop();
Num x7587 = Stack.pop();
SymVal x7588 = SymStack.pop();
Num x7589 = x7587.i32_and(x7585);
Stack.push(x7589);
bool x7590 = allConcrete(x7588, x7586);
SymVal x7591 = x7590 ? Concrete(x7589, 32) : x7588.bitwise_and(x7586);
SymStack.push(x7591);
}
{
Num x7592 = Stack.pop();
SymVal x7593 = SymStack.pop();
Num x7594 = Stack.pop();
SymVal x7595 = SymStack.pop();
Num x7596 = x7594.i32_and(x7592);
Stack.push(x7596);
bool x7597 = allConcrete(x7595, x7593);
SymVal x7598 = x7597 ? Concrete(x7596, 32) : x7595.bitwise_and(x7593);
SymStack.push(x7598);
}
{
Num x7599 = Stack.pop();
SymVal x7600 = SymStack.pop();
Num x7601 = Stack.pop();
SymVal x7602 = SymStack.pop();
Num x7603 = x7601.i32_and(x7599);
Stack.push(x7603);
bool x7604 = allConcrete(x7602, x7600);
SymVal x7605 = x7604 ? Concrete(x7603, 32) : x7602.bitwise_and(x7600);
SymStack.push(x7605);
}
{
Num x7606 = Stack.pop();
SymVal x7607 = SymStack.pop();
Num x7608 = Stack.pop();
SymVal x7609 = SymStack.pop();
Num x7610 = x7608.i32_and(x7606);
Stack.push(x7610);
bool x7611 = allConcrete(x7609, x7607);
SymVal x7612 = x7611 ? Concrete(x7610, 32) : x7609.bitwise_and(x7607);
SymStack.push(x7612);
}
{
Num x7613 = Stack.pop();
SymVal x7614 = SymStack.pop();
Num x7615 = Stack.pop();
SymVal x7616 = SymStack.pop();
Num x7617 = x7615.i32_and(x7613);
Stack.push(x7617);
bool x7618 = allConcrete(x7616, x7614);
SymVal x7619 = x7618 ? Concrete(x7617, 32) : x7616.bitwise_and(x7614);
SymStack.push(x7619);
}
{
Num x7620 = Stack.pop();
SymVal x7621 = SymStack.pop();
Num x7622 = Stack.pop();
SymVal x7623 = SymStack.pop();
Num x7624 = x7622.i32_and(x7620);
Stack.push(x7624);
bool x7625 = allConcrete(x7623, x7621);
SymVal x7626 = x7625 ? Concrete(x7624, 32) : x7623.bitwise_and(x7621);
SymStack.push(x7626);
}
{
Num x7627 = Stack.pop();
SymVal x7628 = SymStack.pop();
GENSYM_SYM_ASSERT(x7628);
GENSYM_ASSERT(x7627.toInt() != 0);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
{
Stack.pop();
SymVal x7629 = SymStack.pop();
SymVal x7630 = x7629.makeI32Symbol();
Stack.push(SymEnv.read(x7630));
SymStack.push(x7630);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 12);
Num x7631 = Stack.pop();
Num x7632 = Stack.pop();
SymVal x7633 = SymStack.pop();
SymVal x7634 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7632);
Frames.set(1, x7631);
SymFrames.set(0, x7634);
SymFrames.set(1, x7633);
updateCurrentMCont(prependCont(x7545, CURRENT_MCONT));
}
__attribute__((musttail)) return x2010(std::monostate{});
return std::monostate{};
}
std::monostate x7545(std::monostate x7546) {
infoWhen("CALL", "Returning from the function at 12, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
{
Num x7547 = Stack.pop();
SymVal x7548 = SymStack.pop();
Frames.set(0, x7547);
SymFrames.set(0, x7548);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1024));
SymStack.push(Concrete(I32V(1024), 32));
{
Stack.pop();
SymVal x7549 = SymStack.pop();
SymVal x7550 = x7549.makeI32Symbol();
Stack.push(SymEnv.read(x7550));
SymStack.push(x7550);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7551 = Stack.pop();
Num x7552 = Stack.pop();
SymVal x7553 = SymStack.pop();
SymVal x7554 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7552);
Frames.set(1, x7551);
SymFrames.set(0, x7554);
SymFrames.set(1, x7553);
updateCurrentMCont(prependCont(x7530, CURRENT_MCONT));
}
__attribute__((musttail)) return x1678(std::monostate{});
return std::monostate{};
}
std::monostate x7530(std::monostate x7531) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7532 = Stack.pop();
SymVal x7533 = SymStack.pop();
Num x7534 = Stack.pop();
SymVal x7535 = SymStack.pop();
Num x7536 = x7534.i32_eq(x7532);
Stack.push(x7536);
bool x7537 = allConcrete(x7535, x7533);
SymVal x7538 = x7537 ? Concrete(x7536, 32) : x7535.eq(x7533).bool2bv();
SymStack.push(x7538);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1026));
SymStack.push(Concrete(I32V(1026), 32));
{
Stack.pop();
SymVal x7539 = SymStack.pop();
SymVal x7540 = x7539.makeI32Symbol();
Stack.push(SymEnv.read(x7540));
SymStack.push(x7540);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 12);
Num x7541 = Stack.pop();
Num x7542 = Stack.pop();
SymVal x7543 = SymStack.pop();
SymVal x7544 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7542);
Frames.set(1, x7541);
SymFrames.set(0, x7544);
SymFrames.set(1, x7543);
updateCurrentMCont(prependCont(x7520, CURRENT_MCONT));
}
__attribute__((musttail)) return x2010(std::monostate{});
return std::monostate{};
}
std::monostate x7520(std::monostate x7521) {
infoWhen("CALL", "Returning from the function at 12, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
{
Num x7522 = Stack.pop();
SymVal x7523 = SymStack.pop();
Frames.set(0, x7522);
SymFrames.set(0, x7523);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1026));
SymStack.push(Concrete(I32V(1026), 32));
{
Stack.pop();
SymVal x7524 = SymStack.pop();
SymVal x7525 = x7524.makeI32Symbol();
Stack.push(SymEnv.read(x7525));
SymStack.push(x7525);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7526 = Stack.pop();
Num x7527 = Stack.pop();
SymVal x7528 = SymStack.pop();
SymVal x7529 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7527);
Frames.set(1, x7526);
SymFrames.set(0, x7529);
SymFrames.set(1, x7528);
updateCurrentMCont(prependCont(x7505, CURRENT_MCONT));
}
__attribute__((musttail)) return x1678(std::monostate{});
return std::monostate{};
}
std::monostate x7505(std::monostate x7506) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7507 = Stack.pop();
SymVal x7508 = SymStack.pop();
Num x7509 = Stack.pop();
SymVal x7510 = SymStack.pop();
Num x7511 = x7509.i32_eq(x7507);
Stack.push(x7511);
bool x7512 = allConcrete(x7510, x7508);
SymVal x7513 = x7512 ? Concrete(x7511, 32) : x7510.eq(x7508).bool2bv();
SymStack.push(x7513);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1028));
SymStack.push(Concrete(I32V(1028), 32));
{
Stack.pop();
SymVal x7514 = SymStack.pop();
SymVal x7515 = x7514.makeI32Symbol();
Stack.push(SymEnv.read(x7515));
SymStack.push(x7515);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 12);
Num x7516 = Stack.pop();
Num x7517 = Stack.pop();
SymVal x7518 = SymStack.pop();
SymVal x7519 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7517);
Frames.set(1, x7516);
SymFrames.set(0, x7519);
SymFrames.set(1, x7518);
updateCurrentMCont(prependCont(x7495, CURRENT_MCONT));
}
__attribute__((musttail)) return x2010(std::monostate{});
return std::monostate{};
}
std::monostate x7495(std::monostate x7496) {
infoWhen("CALL", "Returning from the function at 12, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
{
Num x7497 = Stack.pop();
SymVal x7498 = SymStack.pop();
Frames.set(0, x7497);
SymFrames.set(0, x7498);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1028));
SymStack.push(Concrete(I32V(1028), 32));
{
Stack.pop();
SymVal x7499 = SymStack.pop();
SymVal x7500 = x7499.makeI32Symbol();
Stack.push(SymEnv.read(x7500));
SymStack.push(x7500);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7501 = Stack.pop();
Num x7502 = Stack.pop();
SymVal x7503 = SymStack.pop();
SymVal x7504 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7502);
Frames.set(1, x7501);
SymFrames.set(0, x7504);
SymFrames.set(1, x7503);
updateCurrentMCont(prependCont(x7480, CURRENT_MCONT));
}
__attribute__((musttail)) return x1678(std::monostate{});
return std::monostate{};
}
std::monostate x7480(std::monostate x7481) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7482 = Stack.pop();
SymVal x7483 = SymStack.pop();
Num x7484 = Stack.pop();
SymVal x7485 = SymStack.pop();
Num x7486 = x7484.i32_eq(x7482);
Stack.push(x7486);
bool x7487 = allConcrete(x7485, x7483);
SymVal x7488 = x7487 ? Concrete(x7486, 32) : x7485.eq(x7483).bool2bv();
SymStack.push(x7488);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1030));
SymStack.push(Concrete(I32V(1030), 32));
{
Stack.pop();
SymVal x7489 = SymStack.pop();
SymVal x7490 = x7489.makeI32Symbol();
Stack.push(SymEnv.read(x7490));
SymStack.push(x7490);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 12);
Num x7491 = Stack.pop();
Num x7492 = Stack.pop();
SymVal x7493 = SymStack.pop();
SymVal x7494 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7492);
Frames.set(1, x7491);
SymFrames.set(0, x7494);
SymFrames.set(1, x7493);
updateCurrentMCont(prependCont(x7470, CURRENT_MCONT));
}
__attribute__((musttail)) return x2010(std::monostate{});
return std::monostate{};
}
std::monostate x7470(std::monostate x7471) {
infoWhen("CALL", "Returning from the function at 12, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
{
Num x7472 = Stack.pop();
SymVal x7473 = SymStack.pop();
Frames.set(0, x7472);
SymFrames.set(0, x7473);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1030));
SymStack.push(Concrete(I32V(1030), 32));
{
Stack.pop();
SymVal x7474 = SymStack.pop();
SymVal x7475 = x7474.makeI32Symbol();
Stack.push(SymEnv.read(x7475));
SymStack.push(x7475);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7476 = Stack.pop();
Num x7477 = Stack.pop();
SymVal x7478 = SymStack.pop();
SymVal x7479 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7477);
Frames.set(1, x7476);
SymFrames.set(0, x7479);
SymFrames.set(1, x7478);
updateCurrentMCont(prependCont(x7455, CURRENT_MCONT));
}
__attribute__((musttail)) return x1678(std::monostate{});
return std::monostate{};
}
std::monostate x7455(std::monostate x7456) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7457 = Stack.pop();
SymVal x7458 = SymStack.pop();
Num x7459 = Stack.pop();
SymVal x7460 = SymStack.pop();
Num x7461 = x7459.i32_eq(x7457);
Stack.push(x7461);
bool x7462 = allConcrete(x7460, x7458);
SymVal x7463 = x7462 ? Concrete(x7461, 32) : x7460.eq(x7458).bool2bv();
SymStack.push(x7463);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1032));
SymStack.push(Concrete(I32V(1032), 32));
{
Stack.pop();
SymVal x7464 = SymStack.pop();
SymVal x7465 = x7464.makeI32Symbol();
Stack.push(SymEnv.read(x7465));
SymStack.push(x7465);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 12);
Num x7466 = Stack.pop();
Num x7467 = Stack.pop();
SymVal x7468 = SymStack.pop();
SymVal x7469 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7467);
Frames.set(1, x7466);
SymFrames.set(0, x7469);
SymFrames.set(1, x7468);
updateCurrentMCont(prependCont(x7445, CURRENT_MCONT));
}
__attribute__((musttail)) return x2010(std::monostate{});
return std::monostate{};
}
std::monostate x7445(std::monostate x7446) {
infoWhen("CALL", "Returning from the function at 12, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
{
Num x7447 = Stack.pop();
SymVal x7448 = SymStack.pop();
Frames.set(0, x7447);
SymFrames.set(0, x7448);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1032));
SymStack.push(Concrete(I32V(1032), 32));
{
Stack.pop();
SymVal x7449 = SymStack.pop();
SymVal x7450 = x7449.makeI32Symbol();
Stack.push(SymEnv.read(x7450));
SymStack.push(x7450);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7451 = Stack.pop();
Num x7452 = Stack.pop();
SymVal x7453 = SymStack.pop();
SymVal x7454 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7452);
Frames.set(1, x7451);
SymFrames.set(0, x7454);
SymFrames.set(1, x7453);
updateCurrentMCont(prependCont(x7430, CURRENT_MCONT));
}
__attribute__((musttail)) return x1678(std::monostate{});
return std::monostate{};
}
std::monostate x7430(std::monostate x7431) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7432 = Stack.pop();
SymVal x7433 = SymStack.pop();
Num x7434 = Stack.pop();
SymVal x7435 = SymStack.pop();
Num x7436 = x7434.i32_eq(x7432);
Stack.push(x7436);
bool x7437 = allConcrete(x7435, x7433);
SymVal x7438 = x7437 ? Concrete(x7436, 32) : x7435.eq(x7433).bool2bv();
SymStack.push(x7438);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1034));
SymStack.push(Concrete(I32V(1034), 32));
{
Stack.pop();
SymVal x7439 = SymStack.pop();
SymVal x7440 = x7439.makeI32Symbol();
Stack.push(SymEnv.read(x7440));
SymStack.push(x7440);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 12);
Num x7441 = Stack.pop();
Num x7442 = Stack.pop();
SymVal x7443 = SymStack.pop();
SymVal x7444 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7442);
Frames.set(1, x7441);
SymFrames.set(0, x7444);
SymFrames.set(1, x7443);
updateCurrentMCont(prependCont(x7420, CURRENT_MCONT));
}
__attribute__((musttail)) return x2010(std::monostate{});
return std::monostate{};
}
std::monostate x7420(std::monostate x7421) {
infoWhen("CALL", "Returning from the function at 12, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
{
Num x7422 = Stack.pop();
SymVal x7423 = SymStack.pop();
Frames.set(0, x7422);
SymFrames.set(0, x7423);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1034));
SymStack.push(Concrete(I32V(1034), 32));
{
Stack.pop();
SymVal x7424 = SymStack.pop();
SymVal x7425 = x7424.makeI32Symbol();
Stack.push(SymEnv.read(x7425));
SymStack.push(x7425);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7426 = Stack.pop();
Num x7427 = Stack.pop();
SymVal x7428 = SymStack.pop();
SymVal x7429 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7427);
Frames.set(1, x7426);
SymFrames.set(0, x7429);
SymFrames.set(1, x7428);
updateCurrentMCont(prependCont(x7405, CURRENT_MCONT));
}
__attribute__((musttail)) return x1678(std::monostate{});
return std::monostate{};
}
std::monostate x7405(std::monostate x7406) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7407 = Stack.pop();
SymVal x7408 = SymStack.pop();
Num x7409 = Stack.pop();
SymVal x7410 = SymStack.pop();
Num x7411 = x7409.i32_eq(x7407);
Stack.push(x7411);
bool x7412 = allConcrete(x7410, x7408);
SymVal x7413 = x7412 ? Concrete(x7411, 32) : x7410.eq(x7408).bool2bv();
SymStack.push(x7413);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1036));
SymStack.push(Concrete(I32V(1036), 32));
{
Stack.pop();
SymVal x7414 = SymStack.pop();
SymVal x7415 = x7414.makeI32Symbol();
Stack.push(SymEnv.read(x7415));
SymStack.push(x7415);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 12);
Num x7416 = Stack.pop();
Num x7417 = Stack.pop();
SymVal x7418 = SymStack.pop();
SymVal x7419 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7417);
Frames.set(1, x7416);
SymFrames.set(0, x7419);
SymFrames.set(1, x7418);
updateCurrentMCont(prependCont(x7395, CURRENT_MCONT));
}
__attribute__((musttail)) return x2010(std::monostate{});
return std::monostate{};
}
std::monostate x7395(std::monostate x7396) {
infoWhen("CALL", "Returning from the function at 12, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
{
Num x7397 = Stack.pop();
SymVal x7398 = SymStack.pop();
Frames.set(0, x7397);
SymFrames.set(0, x7398);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1036));
SymStack.push(Concrete(I32V(1036), 32));
{
Stack.pop();
SymVal x7399 = SymStack.pop();
SymVal x7400 = x7399.makeI32Symbol();
Stack.push(SymEnv.read(x7400));
SymStack.push(x7400);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7401 = Stack.pop();
Num x7402 = Stack.pop();
SymVal x7403 = SymStack.pop();
SymVal x7404 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7402);
Frames.set(1, x7401);
SymFrames.set(0, x7404);
SymFrames.set(1, x7403);
updateCurrentMCont(prependCont(x7380, CURRENT_MCONT));
}
__attribute__((musttail)) return x1678(std::monostate{});
return std::monostate{};
}
std::monostate x7380(std::monostate x7381) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7382 = Stack.pop();
SymVal x7383 = SymStack.pop();
Num x7384 = Stack.pop();
SymVal x7385 = SymStack.pop();
Num x7386 = x7384.i32_eq(x7382);
Stack.push(x7386);
bool x7387 = allConcrete(x7385, x7383);
SymVal x7388 = x7387 ? Concrete(x7386, 32) : x7385.eq(x7383).bool2bv();
SymStack.push(x7388);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1038));
SymStack.push(Concrete(I32V(1038), 32));
{
Stack.pop();
SymVal x7389 = SymStack.pop();
SymVal x7390 = x7389.makeI32Symbol();
Stack.push(SymEnv.read(x7390));
SymStack.push(x7390);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 12);
Num x7391 = Stack.pop();
Num x7392 = Stack.pop();
SymVal x7393 = SymStack.pop();
SymVal x7394 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7392);
Frames.set(1, x7391);
SymFrames.set(0, x7394);
SymFrames.set(1, x7393);
updateCurrentMCont(prependCont(x7370, CURRENT_MCONT));
}
__attribute__((musttail)) return x2010(std::monostate{});
return std::monostate{};
}
std::monostate x7370(std::monostate x7371) {
infoWhen("CALL", "Returning from the function at 12, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
{
Num x7372 = Stack.pop();
SymVal x7373 = SymStack.pop();
Frames.set(0, x7372);
SymFrames.set(0, x7373);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1038));
SymStack.push(Concrete(I32V(1038), 32));
{
Stack.pop();
SymVal x7374 = SymStack.pop();
SymVal x7375 = x7374.makeI32Symbol();
Stack.push(SymEnv.read(x7375));
SymStack.push(x7375);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7376 = Stack.pop();
Num x7377 = Stack.pop();
SymVal x7378 = SymStack.pop();
SymVal x7379 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7377);
Frames.set(1, x7376);
SymFrames.set(0, x7379);
SymFrames.set(1, x7378);
updateCurrentMCont(prependCont(x7355, CURRENT_MCONT));
}
__attribute__((musttail)) return x1678(std::monostate{});
return std::monostate{};
}
std::monostate x7355(std::monostate x7356) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7357 = Stack.pop();
SymVal x7358 = SymStack.pop();
Num x7359 = Stack.pop();
SymVal x7360 = SymStack.pop();
Num x7361 = x7359.i32_eq(x7357);
Stack.push(x7361);
bool x7362 = allConcrete(x7360, x7358);
SymVal x7363 = x7362 ? Concrete(x7361, 32) : x7360.eq(x7358).bool2bv();
SymStack.push(x7363);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1040));
SymStack.push(Concrete(I32V(1040), 32));
{
Stack.pop();
SymVal x7364 = SymStack.pop();
SymVal x7365 = x7364.makeI32Symbol();
Stack.push(SymEnv.read(x7365));
SymStack.push(x7365);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 12);
Num x7366 = Stack.pop();
Num x7367 = Stack.pop();
SymVal x7368 = SymStack.pop();
SymVal x7369 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7367);
Frames.set(1, x7366);
SymFrames.set(0, x7369);
SymFrames.set(1, x7368);
updateCurrentMCont(prependCont(x7345, CURRENT_MCONT));
}
__attribute__((musttail)) return x2010(std::monostate{});
return std::monostate{};
}
std::monostate x7345(std::monostate x7346) {
infoWhen("CALL", "Returning from the function at 12, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
{
Num x7347 = Stack.pop();
SymVal x7348 = SymStack.pop();
Frames.set(0, x7347);
SymFrames.set(0, x7348);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(1040));
SymStack.push(Concrete(I32V(1040), 32));
{
Stack.pop();
SymVal x7349 = SymStack.pop();
SymVal x7350 = x7349.makeI32Symbol();
Stack.push(SymEnv.read(x7350));
SymStack.push(x7350);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x7351 = Stack.pop();
Num x7352 = Stack.pop();
SymVal x7353 = SymStack.pop();
SymVal x7354 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x7352);
Frames.set(1, x7351);
SymFrames.set(0, x7354);
SymFrames.set(1, x7353);
updateCurrentMCont(prependCont(x7278, CURRENT_MCONT));
}
__attribute__((musttail)) return x1678(std::monostate{});
return std::monostate{};
}
std::monostate x7278(std::monostate x7279) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x7280 = Stack.pop();
SymVal x7281 = SymStack.pop();
Num x7282 = Stack.pop();
SymVal x7283 = SymStack.pop();
Num x7284 = x7282.i32_eq(x7280);
Stack.push(x7284);
bool x7285 = allConcrete(x7283, x7281);
SymVal x7286 = x7285 ? Concrete(x7284, 32) : x7283.eq(x7281).bool2bv();
SymStack.push(x7286);
}
{
Num x7287 = Stack.pop();
SymVal x7288 = SymStack.pop();
Num x7289 = Stack.pop();
SymVal x7290 = SymStack.pop();
Num x7291 = x7289.i32_and(x7287);
Stack.push(x7291);
bool x7292 = allConcrete(x7290, x7288);
SymVal x7293 = x7292 ? Concrete(x7291, 32) : x7290.bitwise_and(x7288);
SymStack.push(x7293);
}
{
Num x7294 = Stack.pop();
SymVal x7295 = SymStack.pop();
Num x7296 = Stack.pop();
SymVal x7297 = SymStack.pop();
Num x7298 = x7296.i32_and(x7294);
Stack.push(x7298);
bool x7299 = allConcrete(x7297, x7295);
SymVal x7300 = x7299 ? Concrete(x7298, 32) : x7297.bitwise_and(x7295);
SymStack.push(x7300);
}
{
Num x7301 = Stack.pop();
SymVal x7302 = SymStack.pop();
Num x7303 = Stack.pop();
SymVal x7304 = SymStack.pop();
Num x7305 = x7303.i32_and(x7301);
Stack.push(x7305);
bool x7306 = allConcrete(x7304, x7302);
SymVal x7307 = x7306 ? Concrete(x7305, 32) : x7304.bitwise_and(x7302);
SymStack.push(x7307);
}
{
Num x7308 = Stack.pop();
SymVal x7309 = SymStack.pop();
Num x7310 = Stack.pop();
SymVal x7311 = SymStack.pop();
Num x7312 = x7310.i32_and(x7308);
Stack.push(x7312);
bool x7313 = allConcrete(x7311, x7309);
SymVal x7314 = x7313 ? Concrete(x7312, 32) : x7311.bitwise_and(x7309);
SymStack.push(x7314);
}
{
Num x7315 = Stack.pop();
SymVal x7316 = SymStack.pop();
Num x7317 = Stack.pop();
SymVal x7318 = SymStack.pop();
Num x7319 = x7317.i32_and(x7315);
Stack.push(x7319);
bool x7320 = allConcrete(x7318, x7316);
SymVal x7321 = x7320 ? Concrete(x7319, 32) : x7318.bitwise_and(x7316);
SymStack.push(x7321);
}
{
Num x7322 = Stack.pop();
SymVal x7323 = SymStack.pop();
Num x7324 = Stack.pop();
SymVal x7325 = SymStack.pop();
Num x7326 = x7324.i32_and(x7322);
Stack.push(x7326);
bool x7327 = allConcrete(x7325, x7323);
SymVal x7328 = x7327 ? Concrete(x7326, 32) : x7325.bitwise_and(x7323);
SymStack.push(x7328);
}
{
Num x7329 = Stack.pop();
SymVal x7330 = SymStack.pop();
Num x7331 = Stack.pop();
SymVal x7332 = SymStack.pop();
Num x7333 = x7331.i32_and(x7329);
Stack.push(x7333);
bool x7334 = allConcrete(x7332, x7330);
SymVal x7335 = x7334 ? Concrete(x7333, 32) : x7332.bitwise_and(x7330);
SymStack.push(x7335);
}
{
Num x7336 = Stack.pop();
SymVal x7337 = SymStack.pop();
Num x7338 = Stack.pop();
SymVal x7339 = SymStack.pop();
Num x7340 = x7338.i32_and(x7336);
Stack.push(x7340);
bool x7341 = allConcrete(x7339, x7337);
SymVal x7342 = x7341 ? Concrete(x7340, 32) : x7339.bitwise_and(x7337);
SymStack.push(x7342);
}
{
Num x7343 = Stack.pop();
SymVal x7344 = SymStack.pop();
GENSYM_SYM_ASSERT(x7344);
GENSYM_ASSERT(x7343.toInt() != 0);
}
__attribute__((musttail)) return x11(std::monostate{});
return std::monostate{};
}
std::monostate x2010(std::monostate x7264) {
infoWhen("CALL", "Entered the function at 12, stackSize =", Stack.size());
Frames.pushFrameCallee(4);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x7265 = Stack.pop();
SymStack.pop();
Num x7266 = I32V(Memory.loadInt(x7265.toInt(), 0));
SymVal x7267 = SymMemory.loadSym(x7265.toInt(), 0);
Stack.push(x7266);
SymStack.push(x7267);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x7268 = Stack.pop();
SymVal x7269 = SymStack.pop();
Num x7270 = Stack.pop();
SymVal x7271 = SymStack.pop();
Num x7272 = x7270.i32_eq(x7268);
Stack.push(x7272);
bool x7273 = allConcrete(x7271, x7269);
SymVal x7274 = x7273 ? Concrete(x7272, 32) : x7271.eq(x7269).bool2bv();
SymStack.push(x7274);
}
Num x7275 = Stack.pop();
{
SymVal x7276 = SymStack.pop();
ExploreTree.fillIfElseNode(x7276, 23);
}
int x7277 = x7275.toInt();
if (x7277 != 0) {
ExploreTree.moveCursor(true, makeControl(x7092, CURRENT_MCONT));
__attribute__((musttail)) return x7260(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x7260, CURRENT_MCONT));
__attribute__((musttail)) return x7092(std::monostate{});
}
return std::monostate{};
}
std::monostate x7260(std::monostate x7261) {
info("Entering the true branch 23 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x7262 = Stack.pop();
SymVal x7263 = SymStack.pop();
Frames.set(2, x7262);
SymFrames.set(2, x7263);
}
__attribute__((musttail)) return x7258(std::monostate{});
return std::monostate{};
}
std::monostate x7258(std::monostate x7259) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x7107(std::monostate{});
return std::monostate{};
}
std::monostate x7107(std::monostate x7244) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x7245 = Stack.pop();
SymStack.pop();
Num x7246 = I32V(Memory.loadInt(x7245.toInt(), 4));
SymVal x7247 = SymMemory.loadSym(x7245.toInt(), 4);
Stack.push(x7246);
SymStack.push(x7247);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x7248 = Stack.pop();
SymVal x7249 = SymStack.pop();
Num x7250 = Stack.pop();
SymVal x7251 = SymStack.pop();
Num x7252 = x7250.i32_eq(x7248);
Stack.push(x7252);
bool x7253 = allConcrete(x7251, x7249);
SymVal x7254 = x7253 ? Concrete(x7252, 32) : x7251.eq(x7249).bool2bv();
SymStack.push(x7254);
}
Num x7255 = Stack.pop();
{
SymVal x7256 = SymStack.pop();
ExploreTree.fillIfElseNode(x7256, 56);
}
int x7257 = x7255.toInt();
if (x7257 != 0) {
ExploreTree.moveCursor(true, makeControl(x7213, CURRENT_MCONT));
__attribute__((musttail)) return x7242(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x7242, CURRENT_MCONT));
__attribute__((musttail)) return x7213(std::monostate{});
}
return std::monostate{};
}
std::monostate x7242(std::monostate x7243) {
info("Entering the true branch 56 of the if");
info("Jump to 2");
__attribute__((musttail)) return x7166(std::monostate{});
return std::monostate{};
}
std::monostate x7213(std::monostate x7214) {
info("Entering the false branch 56 of the if");
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x7215 = Stack.pop();
SymVal x7216 = SymStack.pop();
Num x7217 = Stack.pop();
SymVal x7218 = SymStack.pop();
Num x7219 = x7217.i32_mul(x7215);
Stack.push(x7219);
bool x7220 = allConcrete(x7218, x7216);
SymVal x7221 = x7220 ? Concrete(x7219, 32) : x7218.mul(x7216);
SymStack.push(x7221);
}
{
Num x7222 = Stack.pop();
SymVal x7223 = SymStack.pop();
Num x7224 = Stack.pop();
SymVal x7225 = SymStack.pop();
Num x7226 = x7224.i32_add(x7222);
Stack.push(x7226);
bool x7227 = allConcrete(x7225, x7223);
SymVal x7228 = x7227 ? Concrete(x7226, 32) : x7225.add(x7223);
SymStack.push(x7228);
}
{
Num x7229 = Stack.pop();
SymStack.pop();
Num x7230 = I32V(Memory.loadInt(x7229.toInt(), 8));
SymVal x7231 = SymMemory.loadSym(x7229.toInt(), 8);
Stack.push(x7230);
SymStack.push(x7231);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x7232 = Stack.pop();
SymVal x7233 = SymStack.pop();
Num x7234 = Stack.pop();
SymVal x7235 = SymStack.pop();
Num x7236 = x7234.i32_eq(x7232);
Stack.push(x7236);
bool x7237 = allConcrete(x7235, x7233);
SymVal x7238 = x7237 ? Concrete(x7236, 32) : x7235.eq(x7233).bool2bv();
SymStack.push(x7238);
}
Num x7239 = Stack.pop();
{
SymVal x7240 = SymStack.pop();
ExploreTree.fillIfElseNode(x7240, 57);
}
int x7241 = x7239.toInt();
if (x7241 != 0) {
ExploreTree.moveCursor(true, makeControl(x7110, CURRENT_MCONT));
__attribute__((musttail)) return x7209(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x7209, CURRENT_MCONT));
__attribute__((musttail)) return x7110(std::monostate{});
}
return std::monostate{};
}
std::monostate x7209(std::monostate x7210) {
info("Entering the true branch 57 of the if");
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x7211 = Stack.pop();
SymVal x7212 = SymStack.pop();
Frames.set(3, x7211);
SymFrames.set(3, x7212);
}
__attribute__((musttail)) return x7207(std::monostate{});
return std::monostate{};
}
std::monostate x7207(std::monostate x7208) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x7165(std::monostate{});
return std::monostate{};
}
std::monostate x7165(std::monostate x7186) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x7187 = Stack.pop();
SymStack.pop();
Num x7188 = I32V(Memory.loadInt(x7187.toInt(), 4));
SymVal x7189 = SymMemory.loadSym(x7187.toInt(), 4);
Stack.push(x7188);
SymStack.push(x7189);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x7190 = Stack.pop();
SymVal x7191 = SymStack.pop();
Num x7192 = Stack.pop();
SymVal x7193 = SymStack.pop();
Num x7194 = x7192.i32_sub(x7190);
Stack.push(x7194);
bool x7195 = allConcrete(x7193, x7191);
SymVal x7196 = x7195 ? Concrete(x7194, 32) : x7193.minus(x7191);
SymStack.push(x7196);
}
{
Num x7197 = Stack.pop();
SymVal x7198 = SymStack.pop();
Num x7199 = Stack.pop();
SymVal x7200 = SymStack.pop();
Num x7201 = x7199.i32_eq(x7197);
Stack.push(x7201);
bool x7202 = allConcrete(x7200, x7198);
SymVal x7203 = x7202 ? Concrete(x7201, 32) : x7200.eq(x7198).bool2bv();
SymStack.push(x7203);
}
Num x7204 = Stack.pop();
{
SymVal x7205 = SymStack.pop();
ExploreTree.fillIfElseNode(x7205, 39);
}
int x7206 = x7204.toInt();
if (x7206 != 0) {
ExploreTree.moveCursor(true, makeControl(x7112, CURRENT_MCONT));
__attribute__((musttail)) return x7184(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x7184, CURRENT_MCONT));
__attribute__((musttail)) return x7112(std::monostate{});
}
return std::monostate{};
}
std::monostate x7184(std::monostate x7185) {
info("Entering the true branch 39 of the if");
info("Jump to 2");
__attribute__((musttail)) return x7168(std::monostate{});
return std::monostate{};
}
std::monostate x7168(std::monostate x7169) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x7170 = Stack.pop();
SymStack.pop();
Num x7171 = I32V(Memory.loadInt(x7170.toInt(), 4));
SymVal x7172 = SymMemory.loadSym(x7170.toInt(), 4);
Stack.push(x7171);
SymStack.push(x7172);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x7173 = Stack.pop();
SymVal x7174 = SymStack.pop();
Num x7175 = Stack.pop();
SymVal x7176 = SymStack.pop();
Num x7177 = x7175.i32_sub(x7173);
Stack.push(x7177);
bool x7178 = allConcrete(x7176, x7174);
SymVal x7179 = x7178 ? Concrete(x7177, 32) : x7176.minus(x7174);
SymStack.push(x7179);
}
{
Num x7180 = Stack.pop();
SymVal x7181 = SymStack.pop();
Num x7182 = Stack.pop();
SymStack.pop();
int x7183 = x7182.toInt();
Memory.storeInt(x7183, 4, x7180.toInt());
SymMemory.storeSym(x7183, 4, x7181);
}
info("Jump to 3");
__attribute__((musttail)) return x7166(std::monostate{});
return std::monostate{};
}
std::monostate x7166(std::monostate x7167) {
info("Exiting the block, stackSize =", Stack.size());
__attribute__((musttail)) return x1980(std::monostate{});
return std::monostate{};
}
std::monostate x7112(std::monostate x7113) {
info("Entering the false branch 39 of the if");
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x7114 = Stack.pop();
SymVal x7115 = SymStack.pop();
Num x7116 = Stack.pop();
SymVal x7117 = SymStack.pop();
Num x7118 = x7116.i32_mul(x7114);
Stack.push(x7118);
bool x7119 = allConcrete(x7117, x7115);
SymVal x7120 = x7119 ? Concrete(x7118, 32) : x7117.mul(x7115);
SymStack.push(x7120);
}
{
Num x7121 = Stack.pop();
SymVal x7122 = SymStack.pop();
Num x7123 = Stack.pop();
SymVal x7124 = SymStack.pop();
Num x7125 = x7123.i32_add(x7121);
Stack.push(x7125);
bool x7126 = allConcrete(x7124, x7122);
SymVal x7127 = x7126 ? Concrete(x7125, 32) : x7124.add(x7122);
SymStack.push(x7127);
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
Num x7128 = Stack.pop();
SymVal x7129 = SymStack.pop();
Num x7130 = Stack.pop();
SymVal x7131 = SymStack.pop();
Num x7132 = x7130.i32_add(x7128);
Stack.push(x7132);
bool x7133 = allConcrete(x7131, x7129);
SymVal x7134 = x7133 ? Concrete(x7132, 32) : x7131.add(x7129);
SymStack.push(x7134);
}
{
Num x7135 = Stack.pop();
SymVal x7136 = SymStack.pop();
Num x7137 = Stack.pop();
SymVal x7138 = SymStack.pop();
Num x7139 = x7137.i32_mul(x7135);
Stack.push(x7139);
bool x7140 = allConcrete(x7138, x7136);
SymVal x7141 = x7140 ? Concrete(x7139, 32) : x7138.mul(x7136);
SymStack.push(x7141);
}
{
Num x7142 = Stack.pop();
SymVal x7143 = SymStack.pop();
Num x7144 = Stack.pop();
SymVal x7145 = SymStack.pop();
Num x7146 = x7144.i32_add(x7142);
Stack.push(x7146);
bool x7147 = allConcrete(x7145, x7143);
SymVal x7148 = x7147 ? Concrete(x7146, 32) : x7145.add(x7143);
SymStack.push(x7148);
}
{
Num x7149 = Stack.pop();
SymStack.pop();
Num x7150 = I32V(Memory.loadInt(x7149.toInt(), 8));
SymVal x7151 = SymMemory.loadSym(x7149.toInt(), 8);
Stack.push(x7150);
SymStack.push(x7151);
}
{
Num x7152 = Stack.pop();
SymVal x7153 = SymStack.pop();
Num x7154 = Stack.pop();
SymStack.pop();
int x7155 = x7154.toInt();
Memory.storeInt(x7155, 8, x7152.toInt());
SymMemory.storeSym(x7155, 8, x7153);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x7156 = Stack.pop();
SymVal x7157 = SymStack.pop();
Num x7158 = Stack.pop();
SymVal x7159 = SymStack.pop();
Num x7160 = x7158.i32_add(x7156);
Stack.push(x7160);
bool x7161 = allConcrete(x7159, x7157);
SymVal x7162 = x7161 ? Concrete(x7160, 32) : x7159.add(x7157);
SymStack.push(x7162);
}
{
Num x7163 = Stack.pop();
SymVal x7164 = SymStack.pop();
Frames.set(3, x7163);
SymFrames.set(3, x7164);
}
info("Jump to 1");
__attribute__((musttail)) return x7165(std::monostate{});
return std::monostate{};
}
std::monostate x7110(std::monostate x7111) {
info("Entering the false branch 57 of the if");
__attribute__((musttail)) return x7108(std::monostate{});
return std::monostate{};
}
std::monostate x7108(std::monostate x7109) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x7096(std::monostate{});
return std::monostate{};
}
std::monostate x7096(std::monostate x7097) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x7098 = Stack.pop();
SymVal x7099 = SymStack.pop();
Num x7100 = Stack.pop();
SymVal x7101 = SymStack.pop();
Num x7102 = x7100.i32_add(x7098);
Stack.push(x7102);
bool x7103 = allConcrete(x7101, x7099);
SymVal x7104 = x7103 ? Concrete(x7102, 32) : x7101.add(x7099);
SymStack.push(x7104);
}
{
Num x7105 = Stack.pop();
SymVal x7106 = SymStack.pop();
Frames.set(2, x7105);
SymFrames.set(2, x7106);
}
info("Jump to 0");
__attribute__((musttail)) return x7107(std::monostate{});
return std::monostate{};
}
std::monostate x7092(std::monostate x7093) {
info("Entering the false branch 23 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x7094 = Stack.pop();
SymVal x7095 = SymStack.pop();
Frames.set(2, x7094);
SymFrames.set(2, x7095);
}
__attribute__((musttail)) return x7090(std::monostate{});
return std::monostate{};
}
std::monostate x7090(std::monostate x7091) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x7011(std::monostate{});
return std::monostate{};
}
std::monostate x7011(std::monostate x7069) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x7070 = Stack.pop();
SymStack.pop();
Num x7071 = I32V(Memory.loadInt(x7070.toInt(), 4));
SymVal x7072 = SymMemory.loadSym(x7070.toInt(), 4);
Stack.push(x7071);
SymStack.push(x7072);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x7073 = Stack.pop();
SymVal x7074 = SymStack.pop();
Num x7075 = Stack.pop();
SymVal x7076 = SymStack.pop();
Num x7077 = x7075.i32_sub(x7073);
Stack.push(x7077);
bool x7078 = allConcrete(x7076, x7074);
SymVal x7079 = x7078 ? Concrete(x7077, 32) : x7076.minus(x7074);
SymStack.push(x7079);
}
{
Num x7080 = Stack.pop();
SymVal x7081 = SymStack.pop();
Num x7082 = Stack.pop();
SymVal x7083 = SymStack.pop();
Num x7084 = x7082.i32_le_s(x7080);
Stack.push(x7084);
bool x7085 = allConcrete(x7083, x7081);
SymVal x7086 = x7085 ? Concrete(x7084, 32) : x7083.le(x7081).bool2bv();
SymStack.push(x7086);
}
Num x7087 = Stack.pop();
{
SymVal x7088 = SymStack.pop();
ExploreTree.fillIfElseNode(x7088, 24);
}
int x7089 = x7087.toInt();
if (x7089 != 0) {
ExploreTree.moveCursor(true, makeControl(x7041, CURRENT_MCONT));
__attribute__((musttail)) return x7043(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x7043, CURRENT_MCONT));
__attribute__((musttail)) return x7041(std::monostate{});
}
return std::monostate{};
}
std::monostate x7043(std::monostate x7044) {
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
Num x7045 = Stack.pop();
SymVal x7046 = SymStack.pop();
Num x7047 = Stack.pop();
SymVal x7048 = SymStack.pop();
Num x7049 = x7047.i32_mul(x7045);
Stack.push(x7049);
bool x7050 = allConcrete(x7048, x7046);
SymVal x7051 = x7050 ? Concrete(x7049, 32) : x7048.mul(x7046);
SymStack.push(x7051);
}
{
Num x7052 = Stack.pop();
SymVal x7053 = SymStack.pop();
Num x7054 = Stack.pop();
SymVal x7055 = SymStack.pop();
Num x7056 = x7054.i32_add(x7052);
Stack.push(x7056);
bool x7057 = allConcrete(x7055, x7053);
SymVal x7058 = x7057 ? Concrete(x7056, 32) : x7055.add(x7053);
SymStack.push(x7058);
}
{
Num x7059 = Stack.pop();
SymStack.pop();
Num x7060 = I32V(Memory.loadInt(x7059.toInt(), 8));
SymVal x7061 = SymMemory.loadSym(x7059.toInt(), 8);
Stack.push(x7060);
SymStack.push(x7061);
}
{
Num x7062 = Stack.pop();
SymVal x7063 = SymStack.pop();
Num x7064 = Stack.pop();
SymVal x7065 = SymStack.pop();
Num x7066 = x7064.i32_gt_s(x7062);
Stack.push(x7066);
bool x7067 = allConcrete(x7065, x7063);
SymVal x7068 = x7067 ? Concrete(x7066, 32) : x7065.gt(x7063).bool2bv();
SymStack.push(x7068);
}
__attribute__((musttail)) return x7012(std::monostate{});
return std::monostate{};
}
std::monostate x7041(std::monostate x7042) {
info("Entering the false branch 24 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
__attribute__((musttail)) return x7012(std::monostate{});
return std::monostate{};
}
std::monostate x7012(std::monostate x7013) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x7014 = Stack.pop();
SymStack.pop();
Num x7015 = I32V(Memory.loadInt(x7014.toInt(), 4));
SymVal x7016 = SymMemory.loadSym(x7014.toInt(), 4);
Stack.push(x7015);
SymStack.push(x7016);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x7017 = Stack.pop();
SymVal x7018 = SymStack.pop();
Num x7019 = Stack.pop();
SymVal x7020 = SymStack.pop();
Num x7021 = x7019.i32_sub(x7017);
Stack.push(x7021);
bool x7022 = allConcrete(x7020, x7018);
SymVal x7023 = x7022 ? Concrete(x7021, 32) : x7020.minus(x7018);
SymStack.push(x7023);
}
{
Num x7024 = Stack.pop();
SymVal x7025 = SymStack.pop();
Num x7026 = Stack.pop();
SymVal x7027 = SymStack.pop();
Num x7028 = x7026.i32_le_s(x7024);
Stack.push(x7028);
bool x7029 = allConcrete(x7027, x7025);
SymVal x7030 = x7029 ? Concrete(x7028, 32) : x7027.le(x7025).bool2bv();
SymStack.push(x7030);
}
{
Num x7031 = Stack.pop();
SymVal x7032 = SymStack.pop();
Num x7033 = Stack.pop();
SymVal x7034 = SymStack.pop();
Num x7035 = x7033.i32_and(x7031);
Stack.push(x7035);
bool x7036 = allConcrete(x7034, x7032);
SymVal x7037 = x7036 ? Concrete(x7035, 32) : x7034.bitwise_and(x7032);
SymStack.push(x7037);
}
Num x7038 = Stack.pop();
{
SymVal x7039 = SymStack.pop();
ExploreTree.fillIfElseNode(x7039, 25);
}
int x7040 = x7038.toInt();
if (x7040 != 0) {
ExploreTree.moveCursor(true, makeControl(x6998, CURRENT_MCONT));
__attribute__((musttail)) return x7000(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x7000, CURRENT_MCONT));
__attribute__((musttail)) return x6998(std::monostate{});
}
return std::monostate{};
}
std::monostate x7000(std::monostate x7001) {
info("Entering the true branch 25 of the if");
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x7002 = Stack.pop();
SymVal x7003 = SymStack.pop();
Num x7004 = Stack.pop();
SymVal x7005 = SymStack.pop();
Num x7006 = x7004.i32_add(x7002);
Stack.push(x7006);
bool x7007 = allConcrete(x7005, x7003);
SymVal x7008 = x7007 ? Concrete(x7006, 32) : x7005.add(x7003);
SymStack.push(x7008);
}
{
Num x7009 = Stack.pop();
SymVal x7010 = SymStack.pop();
Frames.set(2, x7009);
SymFrames.set(2, x7010);
}
info("Jump to 1");
__attribute__((musttail)) return x7011(std::monostate{});
return std::monostate{};
}
std::monostate x6998(std::monostate x6999) {
info("Entering the false branch 25 of the if");
info("Jump to 2");
__attribute__((musttail)) return x6983(std::monostate{});
return std::monostate{};
}
std::monostate x6983(std::monostate x6984) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x6985 = Stack.pop();
SymStack.pop();
Num x6986 = I32V(Memory.loadInt(x6985.toInt(), 4));
SymVal x6987 = SymMemory.loadSym(x6985.toInt(), 4);
Stack.push(x6986);
SymStack.push(x6987);
}
{
Num x6988 = Stack.pop();
SymVal x6989 = SymStack.pop();
Num x6990 = Stack.pop();
SymVal x6991 = SymStack.pop();
Num x6992 = x6990.i32_lt_s(x6988);
Stack.push(x6992);
bool x6993 = allConcrete(x6991, x6989);
SymVal x6994 = x6993 ? Concrete(x6992, 32) : x6991.lt(x6989).bool2bv();
SymStack.push(x6994);
}
Num x6995 = Stack.pop();
{
SymVal x6996 = SymStack.pop();
ExploreTree.fillIfElseNode(x6996, 26);
}
int x6997 = x6995.toInt();
if (x6997 != 0) {
ExploreTree.moveCursor(true, makeControl(x6955, CURRENT_MCONT));
__attribute__((musttail)) return x6957(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x6957, CURRENT_MCONT));
__attribute__((musttail)) return x6955(std::monostate{});
}
return std::monostate{};
}
std::monostate x6957(std::monostate x6958) {
info("Entering the true branch 26 of the if");
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x6959 = Stack.pop();
SymVal x6960 = SymStack.pop();
Num x6961 = Stack.pop();
SymVal x6962 = SymStack.pop();
Num x6963 = x6961.i32_mul(x6959);
Stack.push(x6963);
bool x6964 = allConcrete(x6962, x6960);
SymVal x6965 = x6964 ? Concrete(x6963, 32) : x6962.mul(x6960);
SymStack.push(x6965);
}
{
Num x6966 = Stack.pop();
SymVal x6967 = SymStack.pop();
Num x6968 = Stack.pop();
SymVal x6969 = SymStack.pop();
Num x6970 = x6968.i32_add(x6966);
Stack.push(x6970);
bool x6971 = allConcrete(x6969, x6967);
SymVal x6972 = x6971 ? Concrete(x6970, 32) : x6969.add(x6967);
SymStack.push(x6972);
}
{
Num x6973 = Stack.pop();
SymStack.pop();
Num x6974 = I32V(Memory.loadInt(x6973.toInt(), 8));
SymVal x6975 = SymMemory.loadSym(x6973.toInt(), 8);
Stack.push(x6974);
SymStack.push(x6975);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x6976 = Stack.pop();
SymVal x6977 = SymStack.pop();
Num x6978 = Stack.pop();
SymVal x6979 = SymStack.pop();
Num x6980 = x6978.i32_eq(x6976);
Stack.push(x6980);
bool x6981 = allConcrete(x6979, x6977);
SymVal x6982 = x6981 ? Concrete(x6980, 32) : x6979.eq(x6977).bool2bv();
SymStack.push(x6982);
}
__attribute__((musttail)) return x6950(std::monostate{});
return std::monostate{};
}
std::monostate x6955(std::monostate x6956) {
info("Entering the false branch 26 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
__attribute__((musttail)) return x6950(std::monostate{});
return std::monostate{};
}
std::monostate x6950(std::monostate x6951) {
info("Exiting the if, stackSize =", Stack.size());
Num x6952 = Stack.pop();
{
SymVal x6953 = SymStack.pop();
ExploreTree.fillIfElseNode(x6953, 27);
}
int x6954 = x6952.toInt();
if (x6954 != 0) {
ExploreTree.moveCursor(true, makeControl(x5778, CURRENT_MCONT));
__attribute__((musttail)) return x6882(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x6882, CURRENT_MCONT));
__attribute__((musttail)) return x5778(std::monostate{});
}
return std::monostate{};
}
std::monostate x6882(std::monostate x6883) {
info("Entering the true branch 27 of the if");
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
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6887 = Stack.pop();
SymVal x6888 = SymStack.pop();
Num x6889 = Stack.pop();
SymVal x6890 = SymStack.pop();
Num x6891 = x6889.i32_sub(x6887);
Stack.push(x6891);
bool x6892 = allConcrete(x6890, x6888);
SymVal x6893 = x6892 ? Concrete(x6891, 32) : x6890.minus(x6888);
SymStack.push(x6893);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6894 = Stack.pop();
SymVal x6895 = SymStack.pop();
Num x6896 = Stack.pop();
SymVal x6897 = SymStack.pop();
Num x6898 = x6896.i32_mul(x6894);
Stack.push(x6898);
bool x6899 = allConcrete(x6897, x6895);
SymVal x6900 = x6899 ? Concrete(x6898, 32) : x6897.mul(x6895);
SymStack.push(x6900);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6901 = Stack.pop();
SymVal x6902 = SymStack.pop();
Num x6903 = Stack.pop();
SymVal x6904 = SymStack.pop();
Num x6905 = x6903.i32_mul(x6901);
Stack.push(x6905);
bool x6906 = allConcrete(x6904, x6902);
SymVal x6907 = x6906 ? Concrete(x6905, 32) : x6904.mul(x6902);
SymStack.push(x6907);
}
{
Num x6908 = Stack.pop();
SymVal x6909 = SymStack.pop();
Num x6910 = Stack.pop();
SymVal x6911 = SymStack.pop();
Num x6912 = x6910.i32_add(x6908);
Stack.push(x6912);
bool x6913 = allConcrete(x6911, x6909);
SymVal x6914 = x6913 ? Concrete(x6912, 32) : x6911.add(x6909);
SymStack.push(x6914);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x6915 = Stack.pop();
SymVal x6916 = SymStack.pop();
Num x6917 = Stack.pop();
SymVal x6918 = SymStack.pop();
Num x6919 = x6917.i32_add(x6915);
Stack.push(x6919);
bool x6920 = allConcrete(x6918, x6916);
SymVal x6921 = x6920 ? Concrete(x6919, 32) : x6918.add(x6916);
SymStack.push(x6921);
}
{
Num x6922 = Stack.pop();
SymStack.pop();
Num x6923 = I32V(Memory.loadInt(x6922.toInt(), 8));
SymVal x6924 = SymMemory.loadSym(x6922.toInt(), 8);
Stack.push(x6923);
SymStack.push(x6924);
}
{
Num x6925 = Stack.peek();
SymVal x6926 = SymStack.peek();
Frames.set(5, x6925);
SymFrames.set(5, x6926);
}
{
Num x6927 = Stack.pop();
SymStack.pop();
Num x6928 = I32V(Memory.loadInt(x6927.toInt(), 4));
SymVal x6929 = SymMemory.loadSym(x6927.toInt(), 4);
Stack.push(x6928);
SymStack.push(x6929);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x6930 = Stack.pop();
SymStack.pop();
Num x6931 = I32V(Memory.loadInt(x6930.toInt(), 0));
SymVal x6932 = SymMemory.loadSym(x6930.toInt(), 0);
Stack.push(x6931);
SymStack.push(x6932);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x6933 = Stack.pop();
SymVal x6934 = SymStack.pop();
Num x6935 = Stack.pop();
SymVal x6936 = SymStack.pop();
Num x6937 = x6935.i32_div_s(x6933);
Stack.push(x6937);
bool x6938 = allConcrete(x6936, x6934);
SymVal x6939 = x6938 ? Concrete(x6937, 32) : x6936.div(x6934);
SymStack.push(x6939);
}
{
Num x6940 = Stack.pop();
SymVal x6941 = SymStack.pop();
Num x6942 = Stack.pop();
SymVal x6943 = SymStack.pop();
Num x6944 = x6942.i32_ge_s(x6940);
Stack.push(x6944);
bool x6945 = allConcrete(x6943, x6941);
SymVal x6946 = x6945 ? Concrete(x6944, 32) : x6943.ge(x6941).bool2bv();
SymStack.push(x6946);
}
Num x6947 = Stack.pop();
{
SymVal x6948 = SymStack.pop();
ExploreTree.fillIfElseNode(x6948, 54);
}
int x6949 = x6947.toInt();
if (x6949 != 0) {
ExploreTree.moveCursor(true, makeControl(x6727, CURRENT_MCONT));
__attribute__((musttail)) return x6808(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x6808, CURRENT_MCONT));
__attribute__((musttail)) return x6727(std::monostate{});
}
return std::monostate{};
}
std::monostate x6808(std::monostate x6809) {
info("Entering the true branch 54 of the if");
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x6810 = Stack.pop();
SymVal x6811 = SymStack.pop();
Num x6812 = Stack.pop();
SymVal x6813 = SymStack.pop();
Num x6814 = x6812.i32_mul(x6810);
Stack.push(x6814);
bool x6815 = allConcrete(x6813, x6811);
SymVal x6816 = x6815 ? Concrete(x6814, 32) : x6813.mul(x6811);
SymStack.push(x6816);
}
{
Num x6817 = Stack.pop();
SymVal x6818 = SymStack.pop();
Num x6819 = Stack.pop();
SymVal x6820 = SymStack.pop();
Num x6821 = x6819.i32_add(x6817);
Stack.push(x6821);
bool x6822 = allConcrete(x6820, x6818);
SymVal x6823 = x6822 ? Concrete(x6821, 32) : x6820.add(x6818);
SymStack.push(x6823);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x6824 = Stack.pop();
SymStack.pop();
Num x6825 = I32V(Memory.loadInt(x6824.toInt(), 4));
SymVal x6826 = SymMemory.loadSym(x6824.toInt(), 4);
Stack.push(x6825);
SymStack.push(x6826);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6827 = Stack.pop();
SymVal x6828 = SymStack.pop();
Num x6829 = Stack.pop();
SymVal x6830 = SymStack.pop();
Num x6831 = x6829.i32_sub(x6827);
Stack.push(x6831);
bool x6832 = allConcrete(x6830, x6828);
SymVal x6833 = x6832 ? Concrete(x6831, 32) : x6830.minus(x6828);
SymStack.push(x6833);
}
{
Num x6834 = Stack.pop();
SymVal x6835 = SymStack.pop();
Num x6836 = Stack.pop();
SymVal x6837 = SymStack.pop();
Num x6838 = x6836.i32_mul(x6834);
Stack.push(x6838);
bool x6839 = allConcrete(x6837, x6835);
SymVal x6840 = x6839 ? Concrete(x6838, 32) : x6837.mul(x6835);
SymStack.push(x6840);
}
{
Num x6841 = Stack.pop();
SymVal x6842 = SymStack.pop();
Num x6843 = Stack.pop();
SymVal x6844 = SymStack.pop();
Num x6845 = x6843.i32_add(x6841);
Stack.push(x6845);
bool x6846 = allConcrete(x6844, x6842);
SymVal x6847 = x6846 ? Concrete(x6845, 32) : x6844.add(x6842);
SymStack.push(x6847);
}
{
Num x6848 = Stack.pop();
SymStack.pop();
Num x6849 = I32V(Memory.loadInt(x6848.toInt(), 8));
SymVal x6850 = SymMemory.loadSym(x6848.toInt(), 8);
Stack.push(x6849);
SymStack.push(x6850);
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
Num x6851 = Stack.pop();
SymStack.pop();
Num x6852 = I32V(Memory.loadInt(x6851.toInt(), 4));
SymVal x6853 = SymMemory.loadSym(x6851.toInt(), 4);
Stack.push(x6852);
SymStack.push(x6853);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6854 = Stack.pop();
SymVal x6855 = SymStack.pop();
Num x6856 = Stack.pop();
SymVal x6857 = SymStack.pop();
Num x6858 = x6856.i32_sub(x6854);
Stack.push(x6858);
bool x6859 = allConcrete(x6857, x6855);
SymVal x6860 = x6859 ? Concrete(x6858, 32) : x6857.minus(x6855);
SymStack.push(x6860);
}
{
Num x6861 = Stack.pop();
SymVal x6862 = SymStack.pop();
Num x6863 = Stack.pop();
SymVal x6864 = SymStack.pop();
Num x6865 = x6863.i32_mul(x6861);
Stack.push(x6865);
bool x6866 = allConcrete(x6864, x6862);
SymVal x6867 = x6866 ? Concrete(x6865, 32) : x6864.mul(x6862);
SymStack.push(x6867);
}
{
Num x6868 = Stack.pop();
SymVal x6869 = SymStack.pop();
Num x6870 = Stack.pop();
SymVal x6871 = SymStack.pop();
Num x6872 = x6870.i32_add(x6868);
Stack.push(x6872);
bool x6873 = allConcrete(x6871, x6869);
SymVal x6874 = x6873 ? Concrete(x6872, 32) : x6871.add(x6869);
SymStack.push(x6874);
}
{
Num x6875 = Stack.pop();
SymStack.pop();
Num x6876 = I32V(Memory.loadInt(x6875.toInt(), 8));
SymVal x6877 = SymMemory.loadSym(x6875.toInt(), 8);
Stack.push(x6876);
SymStack.push(x6877);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 12);
Num x6878 = Stack.pop();
Num x6879 = Stack.pop();
SymVal x6880 = SymStack.pop();
SymVal x6881 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x6879);
Frames.set(1, x6878);
SymFrames.set(0, x6881);
SymFrames.set(1, x6880);
updateCurrentMCont(prependCont(x6802, CURRENT_MCONT));
}
__attribute__((musttail)) return x2010(std::monostate{});
return std::monostate{};
}
std::monostate x6802(std::monostate x6803) {
infoWhen("CALL", "Returning from the function at 12, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.pop();
SymStack.pop();
{
Num x6804 = Stack.pop();
SymVal x6805 = SymStack.pop();
Num x6806 = Stack.pop();
SymStack.pop();
int x6807 = x6806.toInt();
Memory.storeInt(x6807, 8, x6804.toInt());
SymMemory.storeSym(x6807, 8, x6805);
}
__attribute__((musttail)) return x6121(std::monostate{});
return std::monostate{};
}
std::monostate x6727(std::monostate x6728) {
info("Entering the false branch 54 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x6729 = Stack.pop();
SymStack.pop();
Num x6730 = I32V(Memory.loadInt(x6729.toInt(), 0));
SymVal x6731 = SymMemory.loadSym(x6729.toInt(), 0);
Stack.push(x6730);
SymStack.push(x6731);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6732 = Stack.pop();
SymVal x6733 = SymStack.pop();
Num x6734 = Stack.pop();
SymVal x6735 = SymStack.pop();
Num x6736 = x6734.i32_sub(x6732);
Stack.push(x6736);
bool x6737 = allConcrete(x6735, x6733);
SymVal x6738 = x6737 ? Concrete(x6736, 32) : x6735.minus(x6733);
SymStack.push(x6738);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6739 = Stack.pop();
SymVal x6740 = SymStack.pop();
Num x6741 = Stack.pop();
SymVal x6742 = SymStack.pop();
Num x6743 = x6741.i32_mul(x6739);
Stack.push(x6743);
bool x6744 = allConcrete(x6742, x6740);
SymVal x6745 = x6744 ? Concrete(x6743, 32) : x6742.mul(x6740);
SymStack.push(x6745);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6746 = Stack.pop();
SymVal x6747 = SymStack.pop();
Num x6748 = Stack.pop();
SymVal x6749 = SymStack.pop();
Num x6750 = x6748.i32_add(x6746);
Stack.push(x6750);
bool x6751 = allConcrete(x6749, x6747);
SymVal x6752 = x6751 ? Concrete(x6750, 32) : x6749.add(x6747);
SymStack.push(x6752);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6753 = Stack.pop();
SymVal x6754 = SymStack.pop();
Num x6755 = Stack.pop();
SymVal x6756 = SymStack.pop();
Num x6757 = x6755.i32_mul(x6753);
Stack.push(x6757);
bool x6758 = allConcrete(x6756, x6754);
SymVal x6759 = x6758 ? Concrete(x6757, 32) : x6756.mul(x6754);
SymStack.push(x6759);
}
{
Num x6760 = Stack.pop();
SymVal x6761 = SymStack.pop();
Num x6762 = Stack.pop();
SymVal x6763 = SymStack.pop();
Num x6764 = x6762.i32_add(x6760);
Stack.push(x6764);
bool x6765 = allConcrete(x6763, x6761);
SymVal x6766 = x6765 ? Concrete(x6764, 32) : x6763.add(x6761);
SymStack.push(x6766);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x6767 = Stack.pop();
SymVal x6768 = SymStack.pop();
Num x6769 = Stack.pop();
SymVal x6770 = SymStack.pop();
Num x6771 = x6769.i32_add(x6767);
Stack.push(x6771);
bool x6772 = allConcrete(x6770, x6768);
SymVal x6773 = x6772 ? Concrete(x6771, 32) : x6770.add(x6768);
SymStack.push(x6773);
}
{
Num x6774 = Stack.pop();
SymStack.pop();
Num x6775 = I32V(Memory.loadInt(x6774.toInt(), 8));
SymVal x6776 = SymMemory.loadSym(x6774.toInt(), 8);
Stack.push(x6775);
SymStack.push(x6776);
}
{
Num x6777 = Stack.peek();
SymVal x6778 = SymStack.peek();
Frames.set(5, x6777);
SymFrames.set(5, x6778);
}
{
Num x6779 = Stack.pop();
SymStack.pop();
Num x6780 = I32V(Memory.loadInt(x6779.toInt(), 4));
SymVal x6781 = SymMemory.loadSym(x6779.toInt(), 4);
Stack.push(x6780);
SymStack.push(x6781);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x6782 = Stack.pop();
SymStack.pop();
Num x6783 = I32V(Memory.loadInt(x6782.toInt(), 0));
SymVal x6784 = SymMemory.loadSym(x6782.toInt(), 0);
Stack.push(x6783);
SymStack.push(x6784);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x6785 = Stack.pop();
SymVal x6786 = SymStack.pop();
Num x6787 = Stack.pop();
SymVal x6788 = SymStack.pop();
Num x6789 = x6787.i32_div_s(x6785);
Stack.push(x6789);
bool x6790 = allConcrete(x6788, x6786);
SymVal x6791 = x6790 ? Concrete(x6789, 32) : x6788.div(x6786);
SymStack.push(x6791);
}
{
Num x6792 = Stack.pop();
SymVal x6793 = SymStack.pop();
Num x6794 = Stack.pop();
SymVal x6795 = SymStack.pop();
Num x6796 = x6794.i32_ge_s(x6792);
Stack.push(x6796);
bool x6797 = allConcrete(x6795, x6793);
SymVal x6798 = x6797 ? Concrete(x6796, 32) : x6795.ge(x6793).bool2bv();
SymStack.push(x6798);
}
Num x6799 = Stack.pop();
{
SymVal x6800 = SymStack.pop();
ExploreTree.fillIfElseNode(x6800, 55);
}
int x6801 = x6799.toInt();
if (x6801 != 0) {
ExploreTree.moveCursor(true, makeControl(x6599, CURRENT_MCONT));
__attribute__((musttail)) return x6673(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x6673, CURRENT_MCONT));
__attribute__((musttail)) return x6599(std::monostate{});
}
return std::monostate{};
}
std::monostate x6673(std::monostate x6674) {
info("Entering the true branch 55 of the if");
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x6675 = Stack.pop();
SymVal x6676 = SymStack.pop();
Num x6677 = Stack.pop();
SymVal x6678 = SymStack.pop();
Num x6679 = x6677.i32_mul(x6675);
Stack.push(x6679);
bool x6680 = allConcrete(x6678, x6676);
SymVal x6681 = x6680 ? Concrete(x6679, 32) : x6678.mul(x6676);
SymStack.push(x6681);
}
{
Num x6682 = Stack.pop();
SymVal x6683 = SymStack.pop();
Num x6684 = Stack.pop();
SymVal x6685 = SymStack.pop();
Num x6686 = x6684.i32_add(x6682);
Stack.push(x6686);
bool x6687 = allConcrete(x6685, x6683);
SymVal x6688 = x6687 ? Concrete(x6686, 32) : x6685.add(x6683);
SymStack.push(x6688);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x6689 = Stack.pop();
SymVal x6690 = SymStack.pop();
Num x6691 = Stack.pop();
SymVal x6692 = SymStack.pop();
Num x6693 = x6691.i32_mul(x6689);
Stack.push(x6693);
bool x6694 = allConcrete(x6692, x6690);
SymVal x6695 = x6694 ? Concrete(x6693, 32) : x6692.mul(x6690);
SymStack.push(x6695);
}
{
Num x6696 = Stack.pop();
SymVal x6697 = SymStack.pop();
Num x6698 = Stack.pop();
SymVal x6699 = SymStack.pop();
Num x6700 = x6698.i32_add(x6696);
Stack.push(x6700);
bool x6701 = allConcrete(x6699, x6697);
SymVal x6702 = x6701 ? Concrete(x6700, 32) : x6699.add(x6697);
SymStack.push(x6702);
}
{
Num x6703 = Stack.pop();
SymStack.pop();
Num x6704 = I32V(Memory.loadInt(x6703.toInt(), 8));
SymVal x6705 = SymMemory.loadSym(x6703.toInt(), 8);
Stack.push(x6704);
SymStack.push(x6705);
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
Num x6706 = Stack.pop();
SymVal x6707 = SymStack.pop();
Num x6708 = Stack.pop();
SymVal x6709 = SymStack.pop();
Num x6710 = x6708.i32_mul(x6706);
Stack.push(x6710);
bool x6711 = allConcrete(x6709, x6707);
SymVal x6712 = x6711 ? Concrete(x6710, 32) : x6709.mul(x6707);
SymStack.push(x6712);
}
{
Num x6713 = Stack.pop();
SymVal x6714 = SymStack.pop();
Num x6715 = Stack.pop();
SymVal x6716 = SymStack.pop();
Num x6717 = x6715.i32_add(x6713);
Stack.push(x6717);
bool x6718 = allConcrete(x6716, x6714);
SymVal x6719 = x6718 ? Concrete(x6717, 32) : x6716.add(x6714);
SymStack.push(x6719);
}
{
Num x6720 = Stack.pop();
SymStack.pop();
Num x6721 = I32V(Memory.loadInt(x6720.toInt(), 8));
SymVal x6722 = SymMemory.loadSym(x6720.toInt(), 8);
Stack.push(x6721);
SymStack.push(x6722);
}
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 12);
Num x6723 = Stack.pop();
Num x6724 = Stack.pop();
SymVal x6725 = SymStack.pop();
SymVal x6726 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x6724);
Frames.set(1, x6723);
SymFrames.set(0, x6726);
SymFrames.set(1, x6725);
updateCurrentMCont(prependCont(x6667, CURRENT_MCONT));
}
__attribute__((musttail)) return x2010(std::monostate{});
return std::monostate{};
}
std::monostate x6667(std::monostate x6668) {
infoWhen("CALL", "Returning from the function at 12, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.pop();
SymStack.pop();
{
Num x6669 = Stack.pop();
SymVal x6670 = SymStack.pop();
Num x6671 = Stack.pop();
SymStack.pop();
int x6672 = x6671.toInt();
Memory.storeInt(x6672, 8, x6669.toInt());
SymMemory.storeSym(x6672, 8, x6670);
}
__attribute__((musttail)) return x6123(std::monostate{});
return std::monostate{};
}
std::monostate x6599(std::monostate x6600) {
info("Entering the false branch 55 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x6601 = Stack.pop();
SymStack.pop();
Num x6602 = I32V(Memory.loadInt(x6601.toInt(), 0));
SymVal x6603 = SymMemory.loadSym(x6601.toInt(), 0);
Stack.push(x6602);
SymStack.push(x6603);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6604 = Stack.pop();
SymVal x6605 = SymStack.pop();
Num x6606 = Stack.pop();
SymVal x6607 = SymStack.pop();
Num x6608 = x6606.i32_sub(x6604);
Stack.push(x6608);
bool x6609 = allConcrete(x6607, x6605);
SymVal x6610 = x6609 ? Concrete(x6608, 32) : x6607.minus(x6605);
SymStack.push(x6610);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6611 = Stack.pop();
SymVal x6612 = SymStack.pop();
Num x6613 = Stack.pop();
SymVal x6614 = SymStack.pop();
Num x6615 = x6613.i32_mul(x6611);
Stack.push(x6615);
bool x6616 = allConcrete(x6614, x6612);
SymVal x6617 = x6616 ? Concrete(x6615, 32) : x6614.mul(x6612);
SymStack.push(x6617);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6618 = Stack.pop();
SymVal x6619 = SymStack.pop();
Num x6620 = Stack.pop();
SymVal x6621 = SymStack.pop();
Num x6622 = x6620.i32_mul(x6618);
Stack.push(x6622);
bool x6623 = allConcrete(x6621, x6619);
SymVal x6624 = x6623 ? Concrete(x6622, 32) : x6621.mul(x6619);
SymStack.push(x6624);
}
{
Num x6625 = Stack.pop();
SymVal x6626 = SymStack.pop();
Num x6627 = Stack.pop();
SymVal x6628 = SymStack.pop();
Num x6629 = x6627.i32_add(x6625);
Stack.push(x6629);
bool x6630 = allConcrete(x6628, x6626);
SymVal x6631 = x6630 ? Concrete(x6629, 32) : x6628.add(x6626);
SymStack.push(x6631);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x6632 = Stack.pop();
SymVal x6633 = SymStack.pop();
Num x6634 = Stack.pop();
SymVal x6635 = SymStack.pop();
Num x6636 = x6634.i32_add(x6632);
Stack.push(x6636);
bool x6637 = allConcrete(x6635, x6633);
SymVal x6638 = x6637 ? Concrete(x6636, 32) : x6635.add(x6633);
SymStack.push(x6638);
}
{
Num x6639 = Stack.pop();
SymStack.pop();
Num x6640 = I32V(Memory.loadInt(x6639.toInt(), 8));
SymVal x6641 = SymMemory.loadSym(x6639.toInt(), 8);
Stack.push(x6640);
SymStack.push(x6641);
}
{
Num x6642 = Stack.pop();
SymVal x6643 = SymStack.pop();
Frames.set(5, x6642);
SymFrames.set(5, x6643);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x6644 = Stack.pop();
SymStack.pop();
Num x6645 = I32V(Memory.loadInt(x6644.toInt(), 4));
SymVal x6646 = SymMemory.loadSym(x6644.toInt(), 4);
Stack.push(x6645);
SymStack.push(x6646);
}
{
Num x6647 = Stack.pop();
SymVal x6648 = SymStack.pop();
Num x6649 = Stack.pop();
SymVal x6650 = SymStack.pop();
Num x6651 = x6649.i32_mul(x6647);
Stack.push(x6651);
bool x6652 = allConcrete(x6650, x6648);
SymVal x6653 = x6652 ? Concrete(x6651, 32) : x6650.mul(x6648);
SymStack.push(x6653);
}
{
Num x6654 = Stack.pop();
SymVal x6655 = SymStack.pop();
Num x6656 = Stack.pop();
SymVal x6657 = SymStack.pop();
Num x6658 = x6656.i32_add(x6654);
Stack.push(x6658);
bool x6659 = allConcrete(x6657, x6655);
SymVal x6660 = x6659 ? Concrete(x6658, 32) : x6657.add(x6655);
SymStack.push(x6660);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x6661 = Stack.pop();
SymVal x6662 = SymStack.pop();
Num x6663 = Stack.pop();
SymStack.pop();
int x6664 = x6663.toInt();
Memory.storeInt(x6664, 8, x6661.toInt());
SymMemory.storeSym(x6664, 8, x6662);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x6665 = Stack.pop();
SymVal x6666 = SymStack.pop();
Frames.set(3, x6665);
SymFrames.set(3, x6666);
}
__attribute__((musttail)) return x6597(std::monostate{});
return std::monostate{};
}
std::monostate x6597(std::monostate x6598) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x5964(std::monostate{});
return std::monostate{};
}
std::monostate x5964(std::monostate x6535) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x6536 = Stack.pop();
SymStack.pop();
Num x6537 = I32V(Memory.loadInt(x6536.toInt(), 0));
SymVal x6538 = SymMemory.loadSym(x6536.toInt(), 0);
Stack.push(x6537);
SymStack.push(x6538);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6539 = Stack.pop();
SymVal x6540 = SymStack.pop();
Num x6541 = Stack.pop();
SymVal x6542 = SymStack.pop();
Num x6543 = x6541.i32_sub(x6539);
Stack.push(x6543);
bool x6544 = allConcrete(x6542, x6540);
SymVal x6545 = x6544 ? Concrete(x6543, 32) : x6542.minus(x6540);
SymStack.push(x6545);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6546 = Stack.pop();
SymVal x6547 = SymStack.pop();
Num x6548 = Stack.pop();
SymVal x6549 = SymStack.pop();
Num x6550 = x6548.i32_mul(x6546);
Stack.push(x6550);
bool x6551 = allConcrete(x6549, x6547);
SymVal x6552 = x6551 ? Concrete(x6550, 32) : x6549.mul(x6547);
SymStack.push(x6552);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6553 = Stack.pop();
SymVal x6554 = SymStack.pop();
Num x6555 = Stack.pop();
SymVal x6556 = SymStack.pop();
Num x6557 = x6555.i32_add(x6553);
Stack.push(x6557);
bool x6558 = allConcrete(x6556, x6554);
SymVal x6559 = x6558 ? Concrete(x6557, 32) : x6556.add(x6554);
SymStack.push(x6559);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6560 = Stack.pop();
SymVal x6561 = SymStack.pop();
Num x6562 = Stack.pop();
SymVal x6563 = SymStack.pop();
Num x6564 = x6562.i32_mul(x6560);
Stack.push(x6564);
bool x6565 = allConcrete(x6563, x6561);
SymVal x6566 = x6565 ? Concrete(x6564, 32) : x6563.mul(x6561);
SymStack.push(x6566);
}
{
Num x6567 = Stack.pop();
SymVal x6568 = SymStack.pop();
Num x6569 = Stack.pop();
SymVal x6570 = SymStack.pop();
Num x6571 = x6569.i32_add(x6567);
Stack.push(x6571);
bool x6572 = allConcrete(x6570, x6568);
SymVal x6573 = x6572 ? Concrete(x6571, 32) : x6570.add(x6568);
SymStack.push(x6573);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x6574 = Stack.pop();
SymVal x6575 = SymStack.pop();
Num x6576 = Stack.pop();
SymVal x6577 = SymStack.pop();
Num x6578 = x6576.i32_add(x6574);
Stack.push(x6578);
bool x6579 = allConcrete(x6577, x6575);
SymVal x6580 = x6579 ? Concrete(x6578, 32) : x6577.add(x6575);
SymStack.push(x6580);
}
{
Num x6581 = Stack.pop();
SymStack.pop();
Num x6582 = I32V(Memory.loadInt(x6581.toInt(), 8));
SymVal x6583 = SymMemory.loadSym(x6581.toInt(), 8);
Stack.push(x6582);
SymStack.push(x6583);
}
{
Num x6584 = Stack.pop();
SymStack.pop();
Num x6585 = I32V(Memory.loadInt(x6584.toInt(), 4));
SymVal x6586 = SymMemory.loadSym(x6584.toInt(), 4);
Stack.push(x6585);
SymStack.push(x6586);
}
{
Num x6587 = Stack.pop();
SymVal x6588 = SymStack.pop();
Num x6589 = Stack.pop();
SymVal x6590 = SymStack.pop();
Num x6591 = x6589.i32_eq(x6587);
Stack.push(x6591);
bool x6592 = allConcrete(x6590, x6588);
SymVal x6593 = x6592 ? Concrete(x6591, 32) : x6590.eq(x6588).bool2bv();
SymStack.push(x6593);
}
Num x6594 = Stack.pop();
{
SymVal x6595 = SymStack.pop();
ExploreTree.fillIfElseNode(x6595, 41);
}
int x6596 = x6594.toInt();
if (x6596 != 0) {
ExploreTree.moveCursor(true, makeControl(x5853, CURRENT_MCONT));
__attribute__((musttail)) return x6533(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x6533, CURRENT_MCONT));
__attribute__((musttail)) return x5853(std::monostate{});
}
return std::monostate{};
}
std::monostate x6533(std::monostate x6534) {
info("Entering the true branch 41 of the if");
info("Jump to 2");
__attribute__((musttail)) return x6446(std::monostate{});
return std::monostate{};
}
std::monostate x6446(std::monostate x6447) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x6448 = Stack.pop();
SymStack.pop();
Num x6449 = I32V(Memory.loadInt(x6448.toInt(), 4));
SymVal x6450 = SymMemory.loadSym(x6448.toInt(), 4);
Stack.push(x6449);
SymStack.push(x6450);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6451 = Stack.pop();
SymVal x6452 = SymStack.pop();
Num x6453 = Stack.pop();
SymVal x6454 = SymStack.pop();
Num x6455 = x6453.i32_add(x6451);
Stack.push(x6455);
bool x6456 = allConcrete(x6454, x6452);
SymVal x6457 = x6456 ? Concrete(x6455, 32) : x6454.add(x6452);
SymStack.push(x6457);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x6458 = Stack.pop();
SymStack.pop();
Num x6459 = I32V(Memory.loadInt(x6458.toInt(), 0));
SymVal x6460 = SymMemory.loadSym(x6458.toInt(), 0);
Stack.push(x6459);
SymStack.push(x6460);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6461 = Stack.pop();
SymVal x6462 = SymStack.pop();
Num x6463 = Stack.pop();
SymVal x6464 = SymStack.pop();
Num x6465 = x6463.i32_sub(x6461);
Stack.push(x6465);
bool x6466 = allConcrete(x6464, x6462);
SymVal x6467 = x6466 ? Concrete(x6465, 32) : x6464.minus(x6462);
SymStack.push(x6467);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6468 = Stack.pop();
SymVal x6469 = SymStack.pop();
Num x6470 = Stack.pop();
SymVal x6471 = SymStack.pop();
Num x6472 = x6470.i32_mul(x6468);
Stack.push(x6472);
bool x6473 = allConcrete(x6471, x6469);
SymVal x6474 = x6473 ? Concrete(x6472, 32) : x6471.mul(x6469);
SymStack.push(x6474);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6475 = Stack.pop();
SymVal x6476 = SymStack.pop();
Num x6477 = Stack.pop();
SymVal x6478 = SymStack.pop();
Num x6479 = x6477.i32_add(x6475);
Stack.push(x6479);
bool x6480 = allConcrete(x6478, x6476);
SymVal x6481 = x6480 ? Concrete(x6479, 32) : x6478.add(x6476);
SymStack.push(x6481);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6482 = Stack.pop();
SymVal x6483 = SymStack.pop();
Num x6484 = Stack.pop();
SymVal x6485 = SymStack.pop();
Num x6486 = x6484.i32_mul(x6482);
Stack.push(x6486);
bool x6487 = allConcrete(x6485, x6483);
SymVal x6488 = x6487 ? Concrete(x6486, 32) : x6485.mul(x6483);
SymStack.push(x6488);
}
{
Num x6489 = Stack.pop();
SymVal x6490 = SymStack.pop();
Num x6491 = Stack.pop();
SymVal x6492 = SymStack.pop();
Num x6493 = x6491.i32_add(x6489);
Stack.push(x6493);
bool x6494 = allConcrete(x6492, x6490);
SymVal x6495 = x6494 ? Concrete(x6493, 32) : x6492.add(x6490);
SymStack.push(x6495);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x6496 = Stack.pop();
SymVal x6497 = SymStack.pop();
Num x6498 = Stack.pop();
SymVal x6499 = SymStack.pop();
Num x6500 = x6498.i32_add(x6496);
Stack.push(x6500);
bool x6501 = allConcrete(x6499, x6497);
SymVal x6502 = x6501 ? Concrete(x6500, 32) : x6499.add(x6497);
SymStack.push(x6502);
}
{
Num x6503 = Stack.pop();
SymStack.pop();
Num x6504 = I32V(Memory.loadInt(x6503.toInt(), 8));
SymVal x6505 = SymMemory.loadSym(x6503.toInt(), 8);
Stack.push(x6504);
SymStack.push(x6505);
}
{
Num x6506 = Stack.pop();
SymStack.pop();
Num x6507 = I32V(Memory.loadInt(x6506.toInt(), 4));
SymVal x6508 = SymMemory.loadSym(x6506.toInt(), 4);
Stack.push(x6507);
SymStack.push(x6508);
}
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
{
Num x6516 = Stack.pop();
SymVal x6517 = SymStack.pop();
Num x6518 = Stack.pop();
SymStack.pop();
int x6519 = x6518.toInt();
Memory.storeInt(x6519, 4, x6516.toInt());
SymMemory.storeSym(x6519, 4, x6517);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x6520 = Stack.pop();
SymStack.pop();
Num x6521 = I32V(Memory.loadInt(x6520.toInt(), 0));
SymVal x6522 = SymMemory.loadSym(x6520.toInt(), 0);
Stack.push(x6521);
SymStack.push(x6522);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6523 = Stack.pop();
SymVal x6524 = SymStack.pop();
Num x6525 = Stack.pop();
SymVal x6526 = SymStack.pop();
Num x6527 = x6525.i32_ne(x6523);
Stack.push(x6527);
bool x6528 = allConcrete(x6526, x6524);
SymVal x6529 = x6528 ? Concrete(x6527, 32) : x6526.neq(x6524).bool2bv();
SymStack.push(x6529);
}
Num x6530 = Stack.pop();
{
SymVal x6531 = SymStack.pop();
ExploreTree.fillIfElseNode(x6531, 42);
}
int x6532 = x6530.toInt();
if (x6532 != 0) {
ExploreTree.moveCursor(true, makeControl(x6205, CURRENT_MCONT));
__attribute__((musttail)) return x6442(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x6442, CURRENT_MCONT));
__attribute__((musttail)) return x6205(std::monostate{});
}
return std::monostate{};
}
std::monostate x6442(std::monostate x6443) {
info("Entering the true branch 42 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x6444 = Stack.pop();
SymVal x6445 = SymStack.pop();
Frames.set(3, x6444);
SymFrames.set(3, x6445);
}
__attribute__((musttail)) return x6440(std::monostate{});
return std::monostate{};
}
std::monostate x6440(std::monostate x6441) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x6366(std::monostate{});
return std::monostate{};
}
std::monostate x6366(std::monostate x6371) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x6372 = Stack.pop();
SymStack.pop();
Num x6373 = I32V(Memory.loadInt(x6372.toInt(), 0));
SymVal x6374 = SymMemory.loadSym(x6372.toInt(), 0);
Stack.push(x6373);
SymStack.push(x6374);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6375 = Stack.pop();
SymVal x6376 = SymStack.pop();
Num x6377 = Stack.pop();
SymVal x6378 = SymStack.pop();
Num x6379 = x6377.i32_sub(x6375);
Stack.push(x6379);
bool x6380 = allConcrete(x6378, x6376);
SymVal x6381 = x6380 ? Concrete(x6379, 32) : x6378.minus(x6376);
SymStack.push(x6381);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6382 = Stack.pop();
SymVal x6383 = SymStack.pop();
Num x6384 = Stack.pop();
SymVal x6385 = SymStack.pop();
Num x6386 = x6384.i32_mul(x6382);
Stack.push(x6386);
bool x6387 = allConcrete(x6385, x6383);
SymVal x6388 = x6387 ? Concrete(x6386, 32) : x6385.mul(x6383);
SymStack.push(x6388);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6389 = Stack.pop();
SymVal x6390 = SymStack.pop();
Num x6391 = Stack.pop();
SymVal x6392 = SymStack.pop();
Num x6393 = x6391.i32_add(x6389);
Stack.push(x6393);
bool x6394 = allConcrete(x6392, x6390);
SymVal x6395 = x6394 ? Concrete(x6393, 32) : x6392.add(x6390);
SymStack.push(x6395);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6396 = Stack.pop();
SymVal x6397 = SymStack.pop();
Num x6398 = Stack.pop();
SymVal x6399 = SymStack.pop();
Num x6400 = x6398.i32_mul(x6396);
Stack.push(x6400);
bool x6401 = allConcrete(x6399, x6397);
SymVal x6402 = x6401 ? Concrete(x6400, 32) : x6399.mul(x6397);
SymStack.push(x6402);
}
{
Num x6403 = Stack.pop();
SymVal x6404 = SymStack.pop();
Num x6405 = Stack.pop();
SymVal x6406 = SymStack.pop();
Num x6407 = x6405.i32_add(x6403);
Stack.push(x6407);
bool x6408 = allConcrete(x6406, x6404);
SymVal x6409 = x6408 ? Concrete(x6407, 32) : x6406.add(x6404);
SymStack.push(x6409);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x6410 = Stack.pop();
SymVal x6411 = SymStack.pop();
Num x6412 = Stack.pop();
SymVal x6413 = SymStack.pop();
Num x6414 = x6412.i32_add(x6410);
Stack.push(x6414);
bool x6415 = allConcrete(x6413, x6411);
SymVal x6416 = x6415 ? Concrete(x6414, 32) : x6413.add(x6411);
SymStack.push(x6416);
}
{
Num x6417 = Stack.pop();
SymStack.pop();
Num x6418 = I32V(Memory.loadInt(x6417.toInt(), 8));
SymVal x6419 = SymMemory.loadSym(x6417.toInt(), 8);
Stack.push(x6418);
SymStack.push(x6419);
}
{
Num x6420 = Stack.pop();
SymStack.pop();
Num x6421 = I32V(Memory.loadInt(x6420.toInt(), 4));
SymVal x6422 = SymMemory.loadSym(x6420.toInt(), 4);
Stack.push(x6421);
SymStack.push(x6422);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6423 = Stack.pop();
SymVal x6424 = SymStack.pop();
Num x6425 = Stack.pop();
SymVal x6426 = SymStack.pop();
Num x6427 = x6425.i32_add(x6423);
Stack.push(x6427);
bool x6428 = allConcrete(x6426, x6424);
SymVal x6429 = x6428 ? Concrete(x6427, 32) : x6426.add(x6424);
SymStack.push(x6429);
}
{
Num x6430 = Stack.pop();
SymVal x6431 = SymStack.pop();
Num x6432 = Stack.pop();
SymVal x6433 = SymStack.pop();
Num x6434 = x6432.i32_eq(x6430);
Stack.push(x6434);
bool x6435 = allConcrete(x6433, x6431);
SymVal x6436 = x6435 ? Concrete(x6434, 32) : x6433.eq(x6431).bool2bv();
SymStack.push(x6436);
}
Num x6437 = Stack.pop();
{
SymVal x6438 = SymStack.pop();
ExploreTree.fillIfElseNode(x6438, 43);
}
int x6439 = x6437.toInt();
if (x6439 != 0) {
ExploreTree.moveCursor(true, makeControl(x6207, CURRENT_MCONT));
__attribute__((musttail)) return x6369(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x6369, CURRENT_MCONT));
__attribute__((musttail)) return x6207(std::monostate{});
}
return std::monostate{};
}
std::monostate x6369(std::monostate x6370) {
info("Entering the true branch 43 of the if");
info("Jump to 2");
__attribute__((musttail)) return x6367(std::monostate{});
return std::monostate{};
}
std::monostate x6367(std::monostate x6368) {
info("Exiting the block, stackSize =", Stack.size());
__attribute__((musttail)) return x6201(std::monostate{});
return std::monostate{};
}
std::monostate x6207(std::monostate x6208) {
info("Entering the false branch 43 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x6209 = Stack.pop();
SymStack.pop();
Num x6210 = I32V(Memory.loadInt(x6209.toInt(), 0));
SymVal x6211 = SymMemory.loadSym(x6209.toInt(), 0);
Stack.push(x6210);
SymStack.push(x6211);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6212 = Stack.pop();
SymVal x6213 = SymStack.pop();
Num x6214 = Stack.pop();
SymVal x6215 = SymStack.pop();
Num x6216 = x6214.i32_sub(x6212);
Stack.push(x6216);
bool x6217 = allConcrete(x6215, x6213);
SymVal x6218 = x6217 ? Concrete(x6216, 32) : x6215.minus(x6213);
SymStack.push(x6218);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6219 = Stack.pop();
SymVal x6220 = SymStack.pop();
Num x6221 = Stack.pop();
SymVal x6222 = SymStack.pop();
Num x6223 = x6221.i32_mul(x6219);
Stack.push(x6223);
bool x6224 = allConcrete(x6222, x6220);
SymVal x6225 = x6224 ? Concrete(x6223, 32) : x6222.mul(x6220);
SymStack.push(x6225);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x6226 = Stack.pop();
SymStack.pop();
Num x6227 = I32V(Memory.loadInt(x6226.toInt(), 4));
SymVal x6228 = SymMemory.loadSym(x6226.toInt(), 4);
Stack.push(x6227);
SymStack.push(x6228);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x6229 = Stack.pop();
SymVal x6230 = SymStack.pop();
Num x6231 = Stack.pop();
SymVal x6232 = SymStack.pop();
Num x6233 = x6231.i32_add(x6229);
Stack.push(x6233);
bool x6234 = allConcrete(x6232, x6230);
SymVal x6235 = x6234 ? Concrete(x6233, 32) : x6232.add(x6230);
SymStack.push(x6235);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6236 = Stack.pop();
SymVal x6237 = SymStack.pop();
Num x6238 = Stack.pop();
SymVal x6239 = SymStack.pop();
Num x6240 = x6238.i32_add(x6236);
Stack.push(x6240);
bool x6241 = allConcrete(x6239, x6237);
SymVal x6242 = x6241 ? Concrete(x6240, 32) : x6239.add(x6237);
SymStack.push(x6242);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6243 = Stack.pop();
SymVal x6244 = SymStack.pop();
Num x6245 = Stack.pop();
SymVal x6246 = SymStack.pop();
Num x6247 = x6245.i32_mul(x6243);
Stack.push(x6247);
bool x6248 = allConcrete(x6246, x6244);
SymVal x6249 = x6248 ? Concrete(x6247, 32) : x6246.mul(x6244);
SymStack.push(x6249);
}
{
Num x6250 = Stack.pop();
SymVal x6251 = SymStack.pop();
Num x6252 = Stack.pop();
SymVal x6253 = SymStack.pop();
Num x6254 = x6252.i32_add(x6250);
Stack.push(x6254);
bool x6255 = allConcrete(x6253, x6251);
SymVal x6256 = x6255 ? Concrete(x6254, 32) : x6253.add(x6251);
SymStack.push(x6256);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x6257 = Stack.pop();
SymVal x6258 = SymStack.pop();
Num x6259 = Stack.pop();
SymVal x6260 = SymStack.pop();
Num x6261 = x6259.i32_add(x6257);
Stack.push(x6261);
bool x6262 = allConcrete(x6260, x6258);
SymVal x6263 = x6262 ? Concrete(x6261, 32) : x6260.add(x6258);
SymStack.push(x6263);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x6264 = Stack.pop();
SymStack.pop();
Num x6265 = I32V(Memory.loadInt(x6264.toInt(), 0));
SymVal x6266 = SymMemory.loadSym(x6264.toInt(), 0);
Stack.push(x6265);
SymStack.push(x6266);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6267 = Stack.pop();
SymVal x6268 = SymStack.pop();
Num x6269 = Stack.pop();
SymVal x6270 = SymStack.pop();
Num x6271 = x6269.i32_sub(x6267);
Stack.push(x6271);
bool x6272 = allConcrete(x6270, x6268);
SymVal x6273 = x6272 ? Concrete(x6271, 32) : x6270.minus(x6268);
SymStack.push(x6273);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6274 = Stack.pop();
SymVal x6275 = SymStack.pop();
Num x6276 = Stack.pop();
SymVal x6277 = SymStack.pop();
Num x6278 = x6276.i32_mul(x6274);
Stack.push(x6278);
bool x6279 = allConcrete(x6277, x6275);
SymVal x6280 = x6279 ? Concrete(x6278, 32) : x6277.mul(x6275);
SymStack.push(x6280);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6281 = Stack.pop();
SymVal x6282 = SymStack.pop();
Num x6283 = Stack.pop();
SymVal x6284 = SymStack.pop();
Num x6285 = x6283.i32_mul(x6281);
Stack.push(x6285);
bool x6286 = allConcrete(x6284, x6282);
SymVal x6287 = x6286 ? Concrete(x6285, 32) : x6284.mul(x6282);
SymStack.push(x6287);
}
{
Num x6288 = Stack.pop();
SymVal x6289 = SymStack.pop();
Num x6290 = Stack.pop();
SymVal x6291 = SymStack.pop();
Num x6292 = x6290.i32_add(x6288);
Stack.push(x6292);
bool x6293 = allConcrete(x6291, x6289);
SymVal x6294 = x6293 ? Concrete(x6292, 32) : x6291.add(x6289);
SymStack.push(x6294);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x6295 = Stack.pop();
SymStack.pop();
Num x6296 = I32V(Memory.loadInt(x6295.toInt(), 0));
SymVal x6297 = SymMemory.loadSym(x6295.toInt(), 0);
Stack.push(x6296);
SymStack.push(x6297);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6298 = Stack.pop();
SymVal x6299 = SymStack.pop();
Num x6300 = Stack.pop();
SymVal x6301 = SymStack.pop();
Num x6302 = x6300.i32_sub(x6298);
Stack.push(x6302);
bool x6303 = allConcrete(x6301, x6299);
SymVal x6304 = x6303 ? Concrete(x6302, 32) : x6301.minus(x6299);
SymStack.push(x6304);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6305 = Stack.pop();
SymVal x6306 = SymStack.pop();
Num x6307 = Stack.pop();
SymVal x6308 = SymStack.pop();
Num x6309 = x6307.i32_mul(x6305);
Stack.push(x6309);
bool x6310 = allConcrete(x6308, x6306);
SymVal x6311 = x6310 ? Concrete(x6309, 32) : x6308.mul(x6306);
SymStack.push(x6311);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6312 = Stack.pop();
SymVal x6313 = SymStack.pop();
Num x6314 = Stack.pop();
SymVal x6315 = SymStack.pop();
Num x6316 = x6314.i32_add(x6312);
Stack.push(x6316);
bool x6317 = allConcrete(x6315, x6313);
SymVal x6318 = x6317 ? Concrete(x6316, 32) : x6315.add(x6313);
SymStack.push(x6318);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6319 = Stack.pop();
SymVal x6320 = SymStack.pop();
Num x6321 = Stack.pop();
SymVal x6322 = SymStack.pop();
Num x6323 = x6321.i32_mul(x6319);
Stack.push(x6323);
bool x6324 = allConcrete(x6322, x6320);
SymVal x6325 = x6324 ? Concrete(x6323, 32) : x6322.mul(x6320);
SymStack.push(x6325);
}
{
Num x6326 = Stack.pop();
SymVal x6327 = SymStack.pop();
Num x6328 = Stack.pop();
SymVal x6329 = SymStack.pop();
Num x6330 = x6328.i32_add(x6326);
Stack.push(x6330);
bool x6331 = allConcrete(x6329, x6327);
SymVal x6332 = x6331 ? Concrete(x6330, 32) : x6329.add(x6327);
SymStack.push(x6332);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x6333 = Stack.pop();
SymVal x6334 = SymStack.pop();
Num x6335 = Stack.pop();
SymVal x6336 = SymStack.pop();
Num x6337 = x6335.i32_add(x6333);
Stack.push(x6337);
bool x6338 = allConcrete(x6336, x6334);
SymVal x6339 = x6338 ? Concrete(x6337, 32) : x6336.add(x6334);
SymStack.push(x6339);
}
{
Num x6340 = Stack.pop();
SymStack.pop();
Num x6341 = I32V(Memory.loadInt(x6340.toInt(), 8));
SymVal x6342 = SymMemory.loadSym(x6340.toInt(), 8);
Stack.push(x6341);
SymStack.push(x6342);
}
{
Num x6343 = Stack.pop();
SymVal x6344 = SymStack.pop();
Num x6345 = Stack.pop();
SymVal x6346 = SymStack.pop();
Num x6347 = x6345.i32_add(x6343);
Stack.push(x6347);
bool x6348 = allConcrete(x6346, x6344);
SymVal x6349 = x6348 ? Concrete(x6347, 32) : x6346.add(x6344);
SymStack.push(x6349);
}
{
Num x6350 = Stack.pop();
SymStack.pop();
Num x6351 = I32V(Memory.loadInt(x6350.toInt(), 8));
SymVal x6352 = SymMemory.loadSym(x6350.toInt(), 8);
Stack.push(x6351);
SymStack.push(x6352);
}
{
Num x6353 = Stack.pop();
SymVal x6354 = SymStack.pop();
Num x6355 = Stack.pop();
SymStack.pop();
int x6356 = x6355.toInt();
Memory.storeInt(x6356, 8, x6353.toInt());
SymMemory.storeSym(x6356, 8, x6354);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6357 = Stack.pop();
SymVal x6358 = SymStack.pop();
Num x6359 = Stack.pop();
SymVal x6360 = SymStack.pop();
Num x6361 = x6359.i32_add(x6357);
Stack.push(x6361);
bool x6362 = allConcrete(x6360, x6358);
SymVal x6363 = x6362 ? Concrete(x6361, 32) : x6360.add(x6358);
SymStack.push(x6363);
}
{
Num x6364 = Stack.pop();
SymVal x6365 = SymStack.pop();
Frames.set(3, x6364);
SymFrames.set(3, x6365);
}
info("Jump to 1");
__attribute__((musttail)) return x6366(std::monostate{});
return std::monostate{};
}
std::monostate x6205(std::monostate x6206) {
info("Entering the false branch 42 of the if");
__attribute__((musttail)) return x6201(std::monostate{});
return std::monostate{};
}
std::monostate x6201(std::monostate x6202) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x6203 = Stack.pop();
SymVal x6204 = SymStack.pop();
Frames.set(3, x6203);
SymFrames.set(3, x6204);
}
__attribute__((musttail)) return x6199(std::monostate{});
return std::monostate{};
}
std::monostate x6199(std::monostate x6200) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x6018(std::monostate{});
return std::monostate{};
}
std::monostate x6018(std::monostate x6178) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x6179 = Stack.pop();
SymStack.pop();
Num x6180 = I32V(Memory.loadInt(x6179.toInt(), 4));
SymVal x6181 = SymMemory.loadSym(x6179.toInt(), 4);
Stack.push(x6180);
SymStack.push(x6181);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6182 = Stack.pop();
SymVal x6183 = SymStack.pop();
Num x6184 = Stack.pop();
SymVal x6185 = SymStack.pop();
Num x6186 = x6184.i32_sub(x6182);
Stack.push(x6186);
bool x6187 = allConcrete(x6185, x6183);
SymVal x6188 = x6187 ? Concrete(x6186, 32) : x6185.minus(x6183);
SymStack.push(x6188);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x6189 = Stack.pop();
SymVal x6190 = SymStack.pop();
Num x6191 = Stack.pop();
SymVal x6192 = SymStack.pop();
Num x6193 = x6191.i32_eq(x6189);
Stack.push(x6193);
bool x6194 = allConcrete(x6192, x6190);
SymVal x6195 = x6194 ? Concrete(x6193, 32) : x6192.eq(x6190).bool2bv();
SymStack.push(x6195);
}
Num x6196 = Stack.pop();
{
SymVal x6197 = SymStack.pop();
ExploreTree.fillIfElseNode(x6197, 39);
}
int x6198 = x6196.toInt();
if (x6198 != 0) {
ExploreTree.moveCursor(true, makeControl(x5965, CURRENT_MCONT));
__attribute__((musttail)) return x6176(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x6176, CURRENT_MCONT));
__attribute__((musttail)) return x5965(std::monostate{});
}
return std::monostate{};
}
std::monostate x6176(std::monostate x6177) {
info("Entering the true branch 39 of the if");
info("Jump to 2");
__attribute__((musttail)) return x6165(std::monostate{});
return std::monostate{};
}
std::monostate x6165(std::monostate x6166) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6167 = Stack.pop();
SymVal x6168 = SymStack.pop();
Num x6169 = Stack.pop();
SymVal x6170 = SymStack.pop();
Num x6171 = x6169.i32_add(x6167);
Stack.push(x6171);
bool x6172 = allConcrete(x6170, x6168);
SymVal x6173 = x6172 ? Concrete(x6171, 32) : x6170.add(x6168);
SymStack.push(x6173);
}
{
Num x6174 = Stack.pop();
SymVal x6175 = SymStack.pop();
Frames.set(3, x6174);
SymFrames.set(3, x6175);
}
__attribute__((musttail)) return x6163(std::monostate{});
return std::monostate{};
}
std::monostate x6163(std::monostate x6164) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x6120(std::monostate{});
return std::monostate{};
}
std::monostate x6120(std::monostate x6149) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x6150 = Stack.pop();
SymStack.pop();
Num x6151 = I32V(Memory.loadInt(x6150.toInt(), 4));
SymVal x6152 = SymMemory.loadSym(x6150.toInt(), 4);
Stack.push(x6151);
SymStack.push(x6152);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x6153 = Stack.pop();
SymVal x6154 = SymStack.pop();
Num x6155 = Stack.pop();
SymVal x6156 = SymStack.pop();
Num x6157 = x6155.i32_eq(x6153);
Stack.push(x6157);
bool x6158 = allConcrete(x6156, x6154);
SymVal x6159 = x6158 ? Concrete(x6157, 32) : x6156.eq(x6154).bool2bv();
SymStack.push(x6159);
}
Num x6160 = Stack.pop();
{
SymVal x6161 = SymStack.pop();
ExploreTree.fillIfElseNode(x6161, 38);
}
int x6162 = x6160.toInt();
if (x6162 != 0) {
ExploreTree.moveCursor(true, makeControl(x6019, CURRENT_MCONT));
__attribute__((musttail)) return x6147(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x6147, CURRENT_MCONT));
__attribute__((musttail)) return x6019(std::monostate{});
}
return std::monostate{};
}
std::monostate x6147(std::monostate x6148) {
info("Entering the true branch 38 of the if");
info("Jump to 2");
__attribute__((musttail)) return x6127(std::monostate{});
return std::monostate{};
}
std::monostate x6127(std::monostate x6128) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x6129 = Stack.pop();
SymStack.pop();
Num x6130 = I32V(Memory.loadInt(x6129.toInt(), 4));
SymVal x6131 = SymMemory.loadSym(x6129.toInt(), 4);
Stack.push(x6130);
SymStack.push(x6131);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6132 = Stack.pop();
SymVal x6133 = SymStack.pop();
Num x6134 = Stack.pop();
SymVal x6135 = SymStack.pop();
Num x6136 = x6134.i32_sub(x6132);
Stack.push(x6136);
bool x6137 = allConcrete(x6135, x6133);
SymVal x6138 = x6137 ? Concrete(x6136, 32) : x6135.minus(x6133);
SymStack.push(x6138);
}
{
Num x6139 = Stack.pop();
SymVal x6140 = SymStack.pop();
Num x6141 = Stack.pop();
SymStack.pop();
int x6142 = x6141.toInt();
Memory.storeInt(x6142, 4, x6139.toInt());
SymMemory.storeSym(x6142, 4, x6140);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 12);
Num x6143 = Stack.pop();
Num x6144 = Stack.pop();
SymVal x6145 = SymStack.pop();
SymVal x6146 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x6144);
Frames.set(1, x6143);
SymFrames.set(0, x6146);
SymFrames.set(1, x6145);
updateCurrentMCont(prependCont(x6125, CURRENT_MCONT));
}
__attribute__((musttail)) return x2010(std::monostate{});
return std::monostate{};
}
std::monostate x6125(std::monostate x6126) {
infoWhen("CALL", "Returning from the function at 12, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.pop();
SymStack.pop();
__attribute__((musttail)) return x6123(std::monostate{});
return std::monostate{};
}
std::monostate x6123(std::monostate x6124) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x6121(std::monostate{});
return std::monostate{};
}
std::monostate x6121(std::monostate x6122) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x1998(std::monostate{});
return std::monostate{};
}
std::monostate x6019(std::monostate x6020) {
info("Entering the false branch 38 of the if");
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
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6038 = Stack.pop();
SymVal x6039 = SymStack.pop();
Num x6040 = Stack.pop();
SymVal x6041 = SymStack.pop();
Num x6042 = x6040.i32_mul(x6038);
Stack.push(x6042);
bool x6043 = allConcrete(x6041, x6039);
SymVal x6044 = x6043 ? Concrete(x6042, 32) : x6041.mul(x6039);
SymStack.push(x6044);
}
{
Num x6045 = Stack.pop();
SymVal x6046 = SymStack.pop();
Num x6047 = Stack.pop();
SymVal x6048 = SymStack.pop();
Num x6049 = x6047.i32_add(x6045);
Stack.push(x6049);
bool x6050 = allConcrete(x6048, x6046);
SymVal x6051 = x6050 ? Concrete(x6049, 32) : x6048.add(x6046);
SymStack.push(x6051);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
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
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x6059 = Stack.pop();
SymStack.pop();
Num x6060 = I32V(Memory.loadInt(x6059.toInt(), 0));
SymVal x6061 = SymMemory.loadSym(x6059.toInt(), 0);
Stack.push(x6060);
SymStack.push(x6061);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6062 = Stack.pop();
SymVal x6063 = SymStack.pop();
Num x6064 = Stack.pop();
SymVal x6065 = SymStack.pop();
Num x6066 = x6064.i32_sub(x6062);
Stack.push(x6066);
bool x6067 = allConcrete(x6065, x6063);
SymVal x6068 = x6067 ? Concrete(x6066, 32) : x6065.minus(x6063);
SymStack.push(x6068);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6069 = Stack.pop();
SymVal x6070 = SymStack.pop();
Num x6071 = Stack.pop();
SymVal x6072 = SymStack.pop();
Num x6073 = x6071.i32_mul(x6069);
Stack.push(x6073);
bool x6074 = allConcrete(x6072, x6070);
SymVal x6075 = x6074 ? Concrete(x6073, 32) : x6072.mul(x6070);
SymStack.push(x6075);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6076 = Stack.pop();
SymVal x6077 = SymStack.pop();
Num x6078 = Stack.pop();
SymVal x6079 = SymStack.pop();
Num x6080 = x6078.i32_add(x6076);
Stack.push(x6080);
bool x6081 = allConcrete(x6079, x6077);
SymVal x6082 = x6081 ? Concrete(x6080, 32) : x6079.add(x6077);
SymStack.push(x6082);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x6083 = Stack.pop();
SymVal x6084 = SymStack.pop();
Num x6085 = Stack.pop();
SymVal x6086 = SymStack.pop();
Num x6087 = x6085.i32_mul(x6083);
Stack.push(x6087);
bool x6088 = allConcrete(x6086, x6084);
SymVal x6089 = x6088 ? Concrete(x6087, 32) : x6086.mul(x6084);
SymStack.push(x6089);
}
{
Num x6090 = Stack.pop();
SymVal x6091 = SymStack.pop();
Num x6092 = Stack.pop();
SymVal x6093 = SymStack.pop();
Num x6094 = x6092.i32_add(x6090);
Stack.push(x6094);
bool x6095 = allConcrete(x6093, x6091);
SymVal x6096 = x6095 ? Concrete(x6094, 32) : x6093.add(x6091);
SymStack.push(x6096);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x6097 = Stack.pop();
SymVal x6098 = SymStack.pop();
Num x6099 = Stack.pop();
SymVal x6100 = SymStack.pop();
Num x6101 = x6099.i32_add(x6097);
Stack.push(x6101);
bool x6102 = allConcrete(x6100, x6098);
SymVal x6103 = x6102 ? Concrete(x6101, 32) : x6100.add(x6098);
SymStack.push(x6103);
}
{
Num x6104 = Stack.pop();
SymStack.pop();
Num x6105 = I32V(Memory.loadInt(x6104.toInt(), 8));
SymVal x6106 = SymMemory.loadSym(x6104.toInt(), 8);
Stack.push(x6105);
SymStack.push(x6106);
}
{
Num x6107 = Stack.pop();
SymVal x6108 = SymStack.pop();
Num x6109 = Stack.pop();
SymStack.pop();
int x6110 = x6109.toInt();
Memory.storeInt(x6110, 8, x6107.toInt());
SymMemory.storeSym(x6110, 8, x6108);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6111 = Stack.pop();
SymVal x6112 = SymStack.pop();
Num x6113 = Stack.pop();
SymVal x6114 = SymStack.pop();
Num x6115 = x6113.i32_add(x6111);
Stack.push(x6115);
bool x6116 = allConcrete(x6114, x6112);
SymVal x6117 = x6116 ? Concrete(x6115, 32) : x6114.add(x6112);
SymStack.push(x6117);
}
{
Num x6118 = Stack.pop();
SymVal x6119 = SymStack.pop();
Frames.set(3, x6118);
SymFrames.set(3, x6119);
}
info("Jump to 1");
__attribute__((musttail)) return x6120(std::monostate{});
return std::monostate{};
}
std::monostate x5965(std::monostate x5966) {
info("Entering the false branch 39 of the if");
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x5967 = Stack.pop();
SymVal x5968 = SymStack.pop();
Num x5969 = Stack.pop();
SymVal x5970 = SymStack.pop();
Num x5971 = x5969.i32_mul(x5967);
Stack.push(x5971);
bool x5972 = allConcrete(x5970, x5968);
SymVal x5973 = x5972 ? Concrete(x5971, 32) : x5970.mul(x5968);
SymStack.push(x5973);
}
{
Num x5974 = Stack.pop();
SymVal x5975 = SymStack.pop();
Num x5976 = Stack.pop();
SymVal x5977 = SymStack.pop();
Num x5978 = x5976.i32_add(x5974);
Stack.push(x5978);
bool x5979 = allConcrete(x5977, x5975);
SymVal x5980 = x5979 ? Concrete(x5978, 32) : x5977.add(x5975);
SymStack.push(x5980);
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
Num x5981 = Stack.pop();
SymVal x5982 = SymStack.pop();
Num x5983 = Stack.pop();
SymVal x5984 = SymStack.pop();
Num x5985 = x5983.i32_add(x5981);
Stack.push(x5985);
bool x5986 = allConcrete(x5984, x5982);
SymVal x5987 = x5986 ? Concrete(x5985, 32) : x5984.add(x5982);
SymStack.push(x5987);
}
{
Num x5988 = Stack.pop();
SymVal x5989 = SymStack.pop();
Num x5990 = Stack.pop();
SymVal x5991 = SymStack.pop();
Num x5992 = x5990.i32_mul(x5988);
Stack.push(x5992);
bool x5993 = allConcrete(x5991, x5989);
SymVal x5994 = x5993 ? Concrete(x5992, 32) : x5991.mul(x5989);
SymStack.push(x5994);
}
{
Num x5995 = Stack.pop();
SymVal x5996 = SymStack.pop();
Num x5997 = Stack.pop();
SymVal x5998 = SymStack.pop();
Num x5999 = x5997.i32_add(x5995);
Stack.push(x5999);
bool x6000 = allConcrete(x5998, x5996);
SymVal x6001 = x6000 ? Concrete(x5999, 32) : x5998.add(x5996);
SymStack.push(x6001);
}
{
Num x6002 = Stack.pop();
SymStack.pop();
Num x6003 = I32V(Memory.loadInt(x6002.toInt(), 8));
SymVal x6004 = SymMemory.loadSym(x6002.toInt(), 8);
Stack.push(x6003);
SymStack.push(x6004);
}
{
Num x6005 = Stack.pop();
SymVal x6006 = SymStack.pop();
Num x6007 = Stack.pop();
SymStack.pop();
int x6008 = x6007.toInt();
Memory.storeInt(x6008, 8, x6005.toInt());
SymMemory.storeSym(x6008, 8, x6006);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x6009 = Stack.pop();
SymVal x6010 = SymStack.pop();
Num x6011 = Stack.pop();
SymVal x6012 = SymStack.pop();
Num x6013 = x6011.i32_add(x6009);
Stack.push(x6013);
bool x6014 = allConcrete(x6012, x6010);
SymVal x6015 = x6014 ? Concrete(x6013, 32) : x6012.add(x6010);
SymStack.push(x6015);
}
{
Num x6016 = Stack.pop();
SymVal x6017 = SymStack.pop();
Frames.set(3, x6016);
SymFrames.set(3, x6017);
}
info("Jump to 1");
__attribute__((musttail)) return x6018(std::monostate{});
return std::monostate{};
}
std::monostate x5853(std::monostate x5854) {
info("Entering the false branch 41 of the if");
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x5855 = Stack.pop();
SymStack.pop();
Num x5856 = I32V(Memory.loadInt(x5855.toInt(), 4));
SymVal x5857 = SymMemory.loadSym(x5855.toInt(), 4);
Stack.push(x5856);
SymStack.push(x5857);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x5858 = Stack.pop();
SymVal x5859 = SymStack.pop();
Num x5860 = Stack.pop();
SymVal x5861 = SymStack.pop();
Num x5862 = x5860.i32_add(x5858);
Stack.push(x5862);
bool x5863 = allConcrete(x5861, x5859);
SymVal x5864 = x5863 ? Concrete(x5862, 32) : x5861.add(x5859);
SymStack.push(x5864);
}
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
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5886 = Stack.pop();
SymStack.pop();
Num x5887 = I32V(Memory.loadInt(x5886.toInt(), 0));
SymVal x5888 = SymMemory.loadSym(x5886.toInt(), 0);
Stack.push(x5887);
SymStack.push(x5888);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5889 = Stack.pop();
SymVal x5890 = SymStack.pop();
Num x5891 = Stack.pop();
SymVal x5892 = SymStack.pop();
Num x5893 = x5891.i32_sub(x5889);
Stack.push(x5893);
bool x5894 = allConcrete(x5892, x5890);
SymVal x5895 = x5894 ? Concrete(x5893, 32) : x5892.minus(x5890);
SymStack.push(x5895);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
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
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
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
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5910 = Stack.pop();
SymVal x5911 = SymStack.pop();
Num x5912 = Stack.pop();
SymVal x5913 = SymStack.pop();
Num x5914 = x5912.i32_mul(x5910);
Stack.push(x5914);
bool x5915 = allConcrete(x5913, x5911);
SymVal x5916 = x5915 ? Concrete(x5914, 32) : x5913.mul(x5911);
SymStack.push(x5916);
}
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
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x5924 = Stack.pop();
SymVal x5925 = SymStack.pop();
Num x5926 = Stack.pop();
SymVal x5927 = SymStack.pop();
Num x5928 = x5926.i32_add(x5924);
Stack.push(x5928);
bool x5929 = allConcrete(x5927, x5925);
SymVal x5930 = x5929 ? Concrete(x5928, 32) : x5927.add(x5925);
SymStack.push(x5930);
}
{
Num x5931 = Stack.pop();
SymStack.pop();
Num x5932 = I32V(Memory.loadInt(x5931.toInt(), 8));
SymVal x5933 = SymMemory.loadSym(x5931.toInt(), 8);
Stack.push(x5932);
SymStack.push(x5933);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x5934 = Stack.pop();
SymVal x5935 = SymStack.pop();
Num x5936 = Stack.pop();
SymVal x5937 = SymStack.pop();
Num x5938 = x5936.i32_mul(x5934);
Stack.push(x5938);
bool x5939 = allConcrete(x5937, x5935);
SymVal x5940 = x5939 ? Concrete(x5938, 32) : x5937.mul(x5935);
SymStack.push(x5940);
}
{
Num x5941 = Stack.pop();
SymVal x5942 = SymStack.pop();
Num x5943 = Stack.pop();
SymVal x5944 = SymStack.pop();
Num x5945 = x5943.i32_add(x5941);
Stack.push(x5945);
bool x5946 = allConcrete(x5944, x5942);
SymVal x5947 = x5946 ? Concrete(x5945, 32) : x5944.add(x5942);
SymStack.push(x5947);
}
{
Num x5948 = Stack.pop();
SymStack.pop();
Num x5949 = I32V(Memory.loadInt(x5948.toInt(), 8));
SymVal x5950 = SymMemory.loadSym(x5948.toInt(), 8);
Stack.push(x5949);
SymStack.push(x5950);
}
{
Num x5951 = Stack.pop();
SymVal x5952 = SymStack.pop();
Num x5953 = Stack.pop();
SymStack.pop();
int x5954 = x5953.toInt();
Memory.storeInt(x5954, 8, x5951.toInt());
SymMemory.storeSym(x5954, 8, x5952);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5955 = Stack.pop();
SymVal x5956 = SymStack.pop();
Num x5957 = Stack.pop();
SymVal x5958 = SymStack.pop();
Num x5959 = x5957.i32_add(x5955);
Stack.push(x5959);
bool x5960 = allConcrete(x5958, x5956);
SymVal x5961 = x5960 ? Concrete(x5959, 32) : x5958.add(x5956);
SymStack.push(x5961);
}
{
Num x5962 = Stack.pop();
SymVal x5963 = SymStack.pop();
Frames.set(3, x5962);
SymFrames.set(3, x5963);
}
info("Jump to 1");
__attribute__((musttail)) return x5964(std::monostate{});
return std::monostate{};
}
std::monostate x5778(std::monostate x5779) {
info("Entering the false branch 27 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5780 = Stack.pop();
SymStack.pop();
Num x5781 = I32V(Memory.loadInt(x5780.toInt(), 0));
SymVal x5782 = SymMemory.loadSym(x5780.toInt(), 0);
Stack.push(x5781);
SymStack.push(x5782);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5783 = Stack.pop();
SymVal x5784 = SymStack.pop();
Num x5785 = Stack.pop();
SymVal x5786 = SymStack.pop();
Num x5787 = x5785.i32_sub(x5783);
Stack.push(x5787);
bool x5788 = allConcrete(x5786, x5784);
SymVal x5789 = x5788 ? Concrete(x5787, 32) : x5786.minus(x5784);
SymStack.push(x5789);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5790 = Stack.pop();
SymVal x5791 = SymStack.pop();
Num x5792 = Stack.pop();
SymVal x5793 = SymStack.pop();
Num x5794 = x5792.i32_mul(x5790);
Stack.push(x5794);
bool x5795 = allConcrete(x5793, x5791);
SymVal x5796 = x5795 ? Concrete(x5794, 32) : x5793.mul(x5791);
SymStack.push(x5796);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5797 = Stack.pop();
SymVal x5798 = SymStack.pop();
Num x5799 = Stack.pop();
SymVal x5800 = SymStack.pop();
Num x5801 = x5799.i32_mul(x5797);
Stack.push(x5801);
bool x5802 = allConcrete(x5800, x5798);
SymVal x5803 = x5802 ? Concrete(x5801, 32) : x5800.mul(x5798);
SymStack.push(x5803);
}
{
Num x5804 = Stack.pop();
SymVal x5805 = SymStack.pop();
Num x5806 = Stack.pop();
SymVal x5807 = SymStack.pop();
Num x5808 = x5806.i32_add(x5804);
Stack.push(x5808);
bool x5809 = allConcrete(x5807, x5805);
SymVal x5810 = x5809 ? Concrete(x5808, 32) : x5807.add(x5805);
SymStack.push(x5810);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x5811 = Stack.pop();
SymVal x5812 = SymStack.pop();
Num x5813 = Stack.pop();
SymVal x5814 = SymStack.pop();
Num x5815 = x5813.i32_add(x5811);
Stack.push(x5815);
bool x5816 = allConcrete(x5814, x5812);
SymVal x5817 = x5816 ? Concrete(x5815, 32) : x5814.add(x5812);
SymStack.push(x5817);
}
{
Num x5818 = Stack.pop();
SymStack.pop();
Num x5819 = I32V(Memory.loadInt(x5818.toInt(), 8));
SymVal x5820 = SymMemory.loadSym(x5818.toInt(), 8);
Stack.push(x5819);
SymStack.push(x5820);
}
{
Num x5821 = Stack.pop();
SymVal x5822 = SymStack.pop();
Frames.set(5, x5821);
SymFrames.set(5, x5822);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x5823 = Stack.pop();
SymStack.pop();
Num x5824 = I32V(Memory.loadInt(x5823.toInt(), 4));
SymVal x5825 = SymMemory.loadSym(x5823.toInt(), 4);
Stack.push(x5824);
SymStack.push(x5825);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5826 = Stack.pop();
SymStack.pop();
Num x5827 = I32V(Memory.loadInt(x5826.toInt(), 0));
SymVal x5828 = SymMemory.loadSym(x5826.toInt(), 0);
Stack.push(x5827);
SymStack.push(x5828);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x5829 = Stack.pop();
SymVal x5830 = SymStack.pop();
Num x5831 = Stack.pop();
SymVal x5832 = SymStack.pop();
Num x5833 = x5831.i32_div_s(x5829);
Stack.push(x5833);
bool x5834 = allConcrete(x5832, x5830);
SymVal x5835 = x5834 ? Concrete(x5833, 32) : x5832.div(x5830);
SymStack.push(x5835);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5836 = Stack.pop();
SymVal x5837 = SymStack.pop();
Num x5838 = Stack.pop();
SymVal x5839 = SymStack.pop();
Num x5840 = x5838.i32_sub(x5836);
Stack.push(x5840);
bool x5841 = allConcrete(x5839, x5837);
SymVal x5842 = x5841 ? Concrete(x5840, 32) : x5839.minus(x5837);
SymStack.push(x5842);
}
{
Num x5843 = Stack.pop();
SymVal x5844 = SymStack.pop();
Num x5845 = Stack.pop();
SymVal x5846 = SymStack.pop();
Num x5847 = x5845.i32_eq(x5843);
Stack.push(x5847);
bool x5848 = allConcrete(x5846, x5844);
SymVal x5849 = x5848 ? Concrete(x5847, 32) : x5846.eq(x5844).bool2bv();
SymStack.push(x5849);
}
Num x5850 = Stack.pop();
{
SymVal x5851 = SymStack.pop();
ExploreTree.fillIfElseNode(x5851, 28);
}
int x5852 = x5850.toInt();
if (x5852 != 0) {
ExploreTree.moveCursor(true, makeControl(x2079, CURRENT_MCONT));
__attribute__((musttail)) return x5754(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x5754, CURRENT_MCONT));
__attribute__((musttail)) return x2079(std::monostate{});
}
return std::monostate{};
}
std::monostate x5754(std::monostate x5755) {
info("Entering the true branch 28 of the if");
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x5756 = Stack.pop();
SymVal x5757 = SymStack.pop();
Frames.set(4, x5756);
SymFrames.set(4, x5757);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5758 = Stack.pop();
SymVal x5759 = SymStack.pop();
Num x5760 = Stack.pop();
SymVal x5761 = SymStack.pop();
Num x5762 = x5760.i32_add(x5758);
Stack.push(x5762);
bool x5763 = allConcrete(x5761, x5759);
SymVal x5764 = x5763 ? Concrete(x5762, 32) : x5761.add(x5759);
SymStack.push(x5764);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x5765 = Stack.pop();
SymStack.pop();
Num x5766 = I32V(Memory.loadInt(x5765.toInt(), 4));
SymVal x5767 = SymMemory.loadSym(x5765.toInt(), 4);
Stack.push(x5766);
SymStack.push(x5767);
}
{
Num x5768 = Stack.pop();
SymVal x5769 = SymStack.pop();
Num x5770 = Stack.pop();
SymVal x5771 = SymStack.pop();
Num x5772 = x5770.i32_le_s(x5768);
Stack.push(x5772);
bool x5773 = allConcrete(x5771, x5769);
SymVal x5774 = x5773 ? Concrete(x5772, 32) : x5771.le(x5769).bool2bv();
SymStack.push(x5774);
}
Num x5775 = Stack.pop();
{
SymVal x5776 = SymStack.pop();
ExploreTree.fillIfElseNode(x5776, 31);
}
int x5777 = x5775.toInt();
if (x5777 != 0) {
ExploreTree.moveCursor(true, makeControl(x4724, CURRENT_MCONT));
__attribute__((musttail)) return x5681(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x5681, CURRENT_MCONT));
__attribute__((musttail)) return x4724(std::monostate{});
}
return std::monostate{};
}
std::monostate x5681(std::monostate x5682) {
info("Entering the true branch 31 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5683 = Stack.pop();
SymStack.pop();
Num x5684 = I32V(Memory.loadInt(x5683.toInt(), 0));
SymVal x5685 = SymMemory.loadSym(x5683.toInt(), 0);
Stack.push(x5684);
SymStack.push(x5685);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5686 = Stack.pop();
SymVal x5687 = SymStack.pop();
Num x5688 = Stack.pop();
SymVal x5689 = SymStack.pop();
Num x5690 = x5688.i32_sub(x5686);
Stack.push(x5690);
bool x5691 = allConcrete(x5689, x5687);
SymVal x5692 = x5691 ? Concrete(x5690, 32) : x5689.minus(x5687);
SymStack.push(x5692);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5693 = Stack.pop();
SymVal x5694 = SymStack.pop();
Num x5695 = Stack.pop();
SymVal x5696 = SymStack.pop();
Num x5697 = x5695.i32_mul(x5693);
Stack.push(x5697);
bool x5698 = allConcrete(x5696, x5694);
SymVal x5699 = x5698 ? Concrete(x5697, 32) : x5696.mul(x5694);
SymStack.push(x5699);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5700 = Stack.pop();
SymVal x5701 = SymStack.pop();
Num x5702 = Stack.pop();
SymVal x5703 = SymStack.pop();
Num x5704 = x5702.i32_add(x5700);
Stack.push(x5704);
bool x5705 = allConcrete(x5703, x5701);
SymVal x5706 = x5705 ? Concrete(x5704, 32) : x5703.add(x5701);
SymStack.push(x5706);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5707 = Stack.pop();
SymVal x5708 = SymStack.pop();
Num x5709 = Stack.pop();
SymVal x5710 = SymStack.pop();
Num x5711 = x5709.i32_mul(x5707);
Stack.push(x5711);
bool x5712 = allConcrete(x5710, x5708);
SymVal x5713 = x5712 ? Concrete(x5711, 32) : x5710.mul(x5708);
SymStack.push(x5713);
}
{
Num x5714 = Stack.pop();
SymVal x5715 = SymStack.pop();
Num x5716 = Stack.pop();
SymVal x5717 = SymStack.pop();
Num x5718 = x5716.i32_add(x5714);
Stack.push(x5718);
bool x5719 = allConcrete(x5717, x5715);
SymVal x5720 = x5719 ? Concrete(x5718, 32) : x5717.add(x5715);
SymStack.push(x5720);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x5721 = Stack.pop();
SymVal x5722 = SymStack.pop();
Num x5723 = Stack.pop();
SymVal x5724 = SymStack.pop();
Num x5725 = x5723.i32_add(x5721);
Stack.push(x5725);
bool x5726 = allConcrete(x5724, x5722);
SymVal x5727 = x5726 ? Concrete(x5725, 32) : x5724.add(x5722);
SymStack.push(x5727);
}
{
Num x5728 = Stack.pop();
SymStack.pop();
Num x5729 = I32V(Memory.loadInt(x5728.toInt(), 8));
SymVal x5730 = SymMemory.loadSym(x5728.toInt(), 8);
Stack.push(x5729);
SymStack.push(x5730);
}
{
Num x5731 = Stack.pop();
SymStack.pop();
Num x5732 = I32V(Memory.loadInt(x5731.toInt(), 4));
SymVal x5733 = SymMemory.loadSym(x5731.toInt(), 4);
Stack.push(x5732);
SymStack.push(x5733);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5734 = Stack.pop();
SymStack.pop();
Num x5735 = I32V(Memory.loadInt(x5734.toInt(), 0));
SymVal x5736 = SymMemory.loadSym(x5734.toInt(), 0);
Stack.push(x5735);
SymStack.push(x5736);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x5737 = Stack.pop();
SymVal x5738 = SymStack.pop();
Num x5739 = Stack.pop();
SymVal x5740 = SymStack.pop();
Num x5741 = x5739.i32_div_s(x5737);
Stack.push(x5741);
bool x5742 = allConcrete(x5740, x5738);
SymVal x5743 = x5742 ? Concrete(x5741, 32) : x5740.div(x5738);
SymStack.push(x5743);
}
{
Num x5744 = Stack.pop();
SymVal x5745 = SymStack.pop();
Num x5746 = Stack.pop();
SymVal x5747 = SymStack.pop();
Num x5748 = x5746.i32_ge_s(x5744);
Stack.push(x5748);
bool x5749 = allConcrete(x5747, x5745);
SymVal x5750 = x5749 ? Concrete(x5748, 32) : x5747.ge(x5745).bool2bv();
SymStack.push(x5750);
}
Num x5751 = Stack.pop();
{
SymVal x5752 = SymStack.pop();
ExploreTree.fillIfElseNode(x5752, 49);
}
int x5753 = x5751.toInt();
if (x5753 != 0) {
ExploreTree.moveCursor(true, makeControl(x4728, CURRENT_MCONT));
__attribute__((musttail)) return x5517(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x5517, CURRENT_MCONT));
__attribute__((musttail)) return x4728(std::monostate{});
}
return std::monostate{};
}
std::monostate x5517(std::monostate x5518) {
info("Entering the true branch 49 of the if");
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5519 = Stack.pop();
SymStack.pop();
Num x5520 = I32V(Memory.loadInt(x5519.toInt(), 0));
SymVal x5521 = SymMemory.loadSym(x5519.toInt(), 0);
Stack.push(x5520);
SymStack.push(x5521);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x5522 = Stack.pop();
SymVal x5523 = SymStack.pop();
Num x5524 = Stack.pop();
SymVal x5525 = SymStack.pop();
Num x5526 = x5524.i32_div_s(x5522);
Stack.push(x5526);
bool x5527 = allConcrete(x5525, x5523);
SymVal x5528 = x5527 ? Concrete(x5526, 32) : x5525.div(x5523);
SymStack.push(x5528);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5529 = Stack.pop();
SymVal x5530 = SymStack.pop();
Num x5531 = Stack.pop();
SymVal x5532 = SymStack.pop();
Num x5533 = x5531.i32_sub(x5529);
Stack.push(x5533);
bool x5534 = allConcrete(x5532, x5530);
SymVal x5535 = x5534 ? Concrete(x5533, 32) : x5532.minus(x5530);
SymStack.push(x5535);
}
{
Num x5536 = Stack.pop();
SymVal x5537 = SymStack.pop();
Num x5538 = Stack.pop();
SymVal x5539 = SymStack.pop();
Num x5540 = x5538.i32_mul(x5536);
Stack.push(x5540);
bool x5541 = allConcrete(x5539, x5537);
SymVal x5542 = x5541 ? Concrete(x5540, 32) : x5539.mul(x5537);
SymStack.push(x5542);
}
{
Num x5543 = Stack.pop();
SymVal x5544 = SymStack.pop();
Num x5545 = Stack.pop();
SymVal x5546 = SymStack.pop();
Num x5547 = x5545.i32_add(x5543);
Stack.push(x5547);
bool x5548 = allConcrete(x5546, x5544);
SymVal x5549 = x5548 ? Concrete(x5547, 32) : x5546.add(x5544);
SymStack.push(x5549);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x5550 = Stack.pop();
SymVal x5551 = SymStack.pop();
Num x5552 = Stack.pop();
SymVal x5553 = SymStack.pop();
Num x5554 = x5552.i32_mul(x5550);
Stack.push(x5554);
bool x5555 = allConcrete(x5553, x5551);
SymVal x5556 = x5555 ? Concrete(x5554, 32) : x5553.mul(x5551);
SymStack.push(x5556);
}
{
Num x5557 = Stack.pop();
SymVal x5558 = SymStack.pop();
Num x5559 = Stack.pop();
SymVal x5560 = SymStack.pop();
Num x5561 = x5559.i32_add(x5557);
Stack.push(x5561);
bool x5562 = allConcrete(x5560, x5558);
SymVal x5563 = x5562 ? Concrete(x5561, 32) : x5560.add(x5558);
SymStack.push(x5563);
}
{
Num x5564 = Stack.pop();
SymStack.pop();
Num x5565 = I32V(Memory.loadInt(x5564.toInt(), 8));
SymVal x5566 = SymMemory.loadSym(x5564.toInt(), 8);
Stack.push(x5565);
SymStack.push(x5566);
}
{
Num x5567 = Stack.pop();
SymVal x5568 = SymStack.pop();
Num x5569 = Stack.pop();
SymStack.pop();
int x5570 = x5569.toInt();
Memory.storeInt(x5570, 8, x5567.toInt());
SymMemory.storeSym(x5570, 8, x5568);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x5571 = Stack.pop();
SymStack.pop();
Num x5572 = I32V(Memory.loadInt(x5571.toInt(), 4));
SymVal x5573 = SymMemory.loadSym(x5571.toInt(), 4);
Stack.push(x5572);
SymStack.push(x5573);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5574 = Stack.pop();
SymVal x5575 = SymStack.pop();
Num x5576 = Stack.pop();
SymVal x5577 = SymStack.pop();
Num x5578 = x5576.i32_add(x5574);
Stack.push(x5578);
bool x5579 = allConcrete(x5577, x5575);
SymVal x5580 = x5579 ? Concrete(x5578, 32) : x5577.add(x5575);
SymStack.push(x5580);
}
{
Num x5581 = Stack.pop();
SymVal x5582 = SymStack.pop();
Num x5583 = Stack.pop();
SymStack.pop();
int x5584 = x5583.toInt();
Memory.storeInt(x5584, 4, x5581.toInt());
SymMemory.storeSym(x5584, 4, x5582);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
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
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5599 = Stack.pop();
SymStack.pop();
Num x5600 = I32V(Memory.loadInt(x5599.toInt(), 0));
SymVal x5601 = SymMemory.loadSym(x5599.toInt(), 0);
Stack.push(x5600);
SymStack.push(x5601);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5602 = Stack.pop();
SymVal x5603 = SymStack.pop();
Num x5604 = Stack.pop();
SymVal x5605 = SymStack.pop();
Num x5606 = x5604.i32_sub(x5602);
Stack.push(x5606);
bool x5607 = allConcrete(x5605, x5603);
SymVal x5608 = x5607 ? Concrete(x5606, 32) : x5605.minus(x5603);
SymStack.push(x5608);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
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
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
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
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5623 = Stack.pop();
SymVal x5624 = SymStack.pop();
Num x5625 = Stack.pop();
SymVal x5626 = SymStack.pop();
Num x5627 = x5625.i32_mul(x5623);
Stack.push(x5627);
bool x5628 = allConcrete(x5626, x5624);
SymVal x5629 = x5628 ? Concrete(x5627, 32) : x5626.mul(x5624);
SymStack.push(x5629);
}
{
Num x5630 = Stack.pop();
SymVal x5631 = SymStack.pop();
Num x5632 = Stack.pop();
SymVal x5633 = SymStack.pop();
Num x5634 = x5632.i32_add(x5630);
Stack.push(x5634);
bool x5635 = allConcrete(x5633, x5631);
SymVal x5636 = x5635 ? Concrete(x5634, 32) : x5633.add(x5631);
SymStack.push(x5636);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x5637 = Stack.pop();
SymVal x5638 = SymStack.pop();
Num x5639 = Stack.pop();
SymVal x5640 = SymStack.pop();
Num x5641 = x5639.i32_add(x5637);
Stack.push(x5641);
bool x5642 = allConcrete(x5640, x5638);
SymVal x5643 = x5642 ? Concrete(x5641, 32) : x5640.add(x5638);
SymStack.push(x5643);
}
{
Num x5644 = Stack.pop();
SymStack.pop();
Num x5645 = I32V(Memory.loadInt(x5644.toInt(), 8));
SymVal x5646 = SymMemory.loadSym(x5644.toInt(), 8);
Stack.push(x5645);
SymStack.push(x5646);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5647 = Stack.pop();
SymVal x5648 = SymStack.pop();
Num x5649 = Stack.pop();
SymVal x5650 = SymStack.pop();
Num x5651 = x5649.i32_mul(x5647);
Stack.push(x5651);
bool x5652 = allConcrete(x5650, x5648);
SymVal x5653 = x5652 ? Concrete(x5651, 32) : x5650.mul(x5648);
SymStack.push(x5653);
}
{
Num x5654 = Stack.pop();
SymVal x5655 = SymStack.pop();
Num x5656 = Stack.pop();
SymVal x5657 = SymStack.pop();
Num x5658 = x5656.i32_add(x5654);
Stack.push(x5658);
bool x5659 = allConcrete(x5657, x5655);
SymVal x5660 = x5659 ? Concrete(x5658, 32) : x5657.add(x5655);
SymStack.push(x5660);
}
{
Num x5661 = Stack.pop();
SymStack.pop();
Num x5662 = I32V(Memory.loadInt(x5661.toInt(), 8));
SymVal x5663 = SymMemory.loadSym(x5661.toInt(), 8);
Stack.push(x5662);
SymStack.push(x5663);
}
{
Num x5664 = Stack.pop();
SymVal x5665 = SymStack.pop();
Num x5666 = Stack.pop();
SymStack.pop();
int x5667 = x5666.toInt();
Memory.storeInt(x5667, 8, x5664.toInt());
SymMemory.storeSym(x5667, 8, x5665);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x5668 = Stack.pop();
SymStack.pop();
Num x5669 = I32V(Memory.loadInt(x5668.toInt(), 0));
SymVal x5670 = SymMemory.loadSym(x5668.toInt(), 0);
Stack.push(x5669);
SymStack.push(x5670);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5671 = Stack.pop();
SymVal x5672 = SymStack.pop();
Num x5673 = Stack.pop();
SymVal x5674 = SymStack.pop();
Num x5675 = x5673.i32_ne(x5671);
Stack.push(x5675);
bool x5676 = allConcrete(x5674, x5672);
SymVal x5677 = x5676 ? Concrete(x5675, 32) : x5674.neq(x5672).bool2bv();
SymStack.push(x5677);
}
Num x5678 = Stack.pop();
{
SymVal x5679 = SymStack.pop();
ExploreTree.fillIfElseNode(x5679, 50);
}
int x5680 = x5678.toInt();
if (x5680 != 0) {
ExploreTree.moveCursor(true, makeControl(x5372, CURRENT_MCONT));
__attribute__((musttail)) return x5374(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x5374, CURRENT_MCONT));
__attribute__((musttail)) return x5372(std::monostate{});
}
return std::monostate{};
}
std::monostate x5374(std::monostate x5375) {
info("Entering the true branch 50 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5376 = Stack.pop();
SymStack.pop();
Num x5377 = I32V(Memory.loadInt(x5376.toInt(), 0));
SymVal x5378 = SymMemory.loadSym(x5376.toInt(), 0);
Stack.push(x5377);
SymStack.push(x5378);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5379 = Stack.pop();
SymVal x5380 = SymStack.pop();
Num x5381 = Stack.pop();
SymVal x5382 = SymStack.pop();
Num x5383 = x5381.i32_sub(x5379);
Stack.push(x5383);
bool x5384 = allConcrete(x5382, x5380);
SymVal x5385 = x5384 ? Concrete(x5383, 32) : x5382.minus(x5380);
SymStack.push(x5385);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5386 = Stack.pop();
SymVal x5387 = SymStack.pop();
Num x5388 = Stack.pop();
SymVal x5389 = SymStack.pop();
Num x5390 = x5388.i32_mul(x5386);
Stack.push(x5390);
bool x5391 = allConcrete(x5389, x5387);
SymVal x5392 = x5391 ? Concrete(x5390, 32) : x5389.mul(x5387);
SymStack.push(x5392);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5393 = Stack.pop();
SymStack.pop();
Num x5394 = I32V(Memory.loadInt(x5393.toInt(), 0));
SymVal x5395 = SymMemory.loadSym(x5393.toInt(), 0);
Stack.push(x5394);
SymStack.push(x5395);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x5396 = Stack.pop();
SymVal x5397 = SymStack.pop();
Num x5398 = Stack.pop();
SymVal x5399 = SymStack.pop();
Num x5400 = x5398.i32_div_s(x5396);
Stack.push(x5400);
bool x5401 = allConcrete(x5399, x5397);
SymVal x5402 = x5401 ? Concrete(x5400, 32) : x5399.div(x5397);
SymStack.push(x5402);
}
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
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x5417 = Stack.pop();
SymVal x5418 = SymStack.pop();
Num x5419 = Stack.pop();
SymVal x5420 = SymStack.pop();
Num x5421 = x5419.i32_add(x5417);
Stack.push(x5421);
bool x5422 = allConcrete(x5420, x5418);
SymVal x5423 = x5422 ? Concrete(x5421, 32) : x5420.add(x5418);
SymStack.push(x5423);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5424 = Stack.pop();
SymStack.pop();
Num x5425 = I32V(Memory.loadInt(x5424.toInt(), 0));
SymVal x5426 = SymMemory.loadSym(x5424.toInt(), 0);
Stack.push(x5425);
SymStack.push(x5426);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5427 = Stack.pop();
SymVal x5428 = SymStack.pop();
Num x5429 = Stack.pop();
SymVal x5430 = SymStack.pop();
Num x5431 = x5429.i32_sub(x5427);
Stack.push(x5431);
bool x5432 = allConcrete(x5430, x5428);
SymVal x5433 = x5432 ? Concrete(x5431, 32) : x5430.minus(x5428);
SymStack.push(x5433);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5434 = Stack.pop();
SymVal x5435 = SymStack.pop();
Num x5436 = Stack.pop();
SymVal x5437 = SymStack.pop();
Num x5438 = x5436.i32_mul(x5434);
Stack.push(x5438);
bool x5439 = allConcrete(x5437, x5435);
SymVal x5440 = x5439 ? Concrete(x5438, 32) : x5437.mul(x5435);
SymStack.push(x5440);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
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
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5455 = Stack.pop();
SymStack.pop();
Num x5456 = I32V(Memory.loadInt(x5455.toInt(), 0));
SymVal x5457 = SymMemory.loadSym(x5455.toInt(), 0);
Stack.push(x5456);
SymStack.push(x5457);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5458 = Stack.pop();
SymVal x5459 = SymStack.pop();
Num x5460 = Stack.pop();
SymVal x5461 = SymStack.pop();
Num x5462 = x5460.i32_sub(x5458);
Stack.push(x5462);
bool x5463 = allConcrete(x5461, x5459);
SymVal x5464 = x5463 ? Concrete(x5462, 32) : x5461.minus(x5459);
SymStack.push(x5464);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5465 = Stack.pop();
SymVal x5466 = SymStack.pop();
Num x5467 = Stack.pop();
SymVal x5468 = SymStack.pop();
Num x5469 = x5467.i32_mul(x5465);
Stack.push(x5469);
bool x5470 = allConcrete(x5468, x5466);
SymVal x5471 = x5470 ? Concrete(x5469, 32) : x5468.mul(x5466);
SymStack.push(x5471);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5472 = Stack.pop();
SymVal x5473 = SymStack.pop();
Num x5474 = Stack.pop();
SymVal x5475 = SymStack.pop();
Num x5476 = x5474.i32_add(x5472);
Stack.push(x5476);
bool x5477 = allConcrete(x5475, x5473);
SymVal x5478 = x5477 ? Concrete(x5476, 32) : x5475.add(x5473);
SymStack.push(x5478);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5479 = Stack.pop();
SymVal x5480 = SymStack.pop();
Num x5481 = Stack.pop();
SymVal x5482 = SymStack.pop();
Num x5483 = x5481.i32_mul(x5479);
Stack.push(x5483);
bool x5484 = allConcrete(x5482, x5480);
SymVal x5485 = x5484 ? Concrete(x5483, 32) : x5482.mul(x5480);
SymStack.push(x5485);
}
{
Num x5486 = Stack.pop();
SymVal x5487 = SymStack.pop();
Num x5488 = Stack.pop();
SymVal x5489 = SymStack.pop();
Num x5490 = x5488.i32_add(x5486);
Stack.push(x5490);
bool x5491 = allConcrete(x5489, x5487);
SymVal x5492 = x5491 ? Concrete(x5490, 32) : x5489.add(x5487);
SymStack.push(x5492);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x5493 = Stack.pop();
SymVal x5494 = SymStack.pop();
Num x5495 = Stack.pop();
SymVal x5496 = SymStack.pop();
Num x5497 = x5495.i32_add(x5493);
Stack.push(x5497);
bool x5498 = allConcrete(x5496, x5494);
SymVal x5499 = x5498 ? Concrete(x5497, 32) : x5496.add(x5494);
SymStack.push(x5499);
}
{
Num x5500 = Stack.pop();
SymStack.pop();
Num x5501 = I32V(Memory.loadInt(x5500.toInt(), 8));
SymVal x5502 = SymMemory.loadSym(x5500.toInt(), 8);
Stack.push(x5501);
SymStack.push(x5502);
}
{
Num x5503 = Stack.pop();
SymVal x5504 = SymStack.pop();
Num x5505 = Stack.pop();
SymVal x5506 = SymStack.pop();
Num x5507 = x5505.i32_add(x5503);
Stack.push(x5507);
bool x5508 = allConcrete(x5506, x5504);
SymVal x5509 = x5508 ? Concrete(x5507, 32) : x5506.add(x5504);
SymStack.push(x5509);
}
{
Num x5510 = Stack.pop();
SymStack.pop();
Num x5511 = I32V(Memory.loadInt(x5510.toInt(), 8));
SymVal x5512 = SymMemory.loadSym(x5510.toInt(), 8);
Stack.push(x5511);
SymStack.push(x5512);
}
{
Num x5513 = Stack.pop();
SymVal x5514 = SymStack.pop();
Num x5515 = Stack.pop();
SymStack.pop();
int x5516 = x5515.toInt();
Memory.storeInt(x5516, 8, x5513.toInt());
SymMemory.storeSym(x5516, 8, x5514);
}
__attribute__((musttail)) return x5354(std::monostate{});
return std::monostate{};
}
std::monostate x5372(std::monostate x5373) {
info("Entering the false branch 50 of the if");
__attribute__((musttail)) return x5354(std::monostate{});
return std::monostate{};
}
std::monostate x5354(std::monostate x5355) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5356 = Stack.pop();
SymStack.pop();
Num x5357 = I32V(Memory.loadInt(x5356.toInt(), 0));
SymVal x5358 = SymMemory.loadSym(x5356.toInt(), 0);
Stack.push(x5357);
SymStack.push(x5358);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x5359 = Stack.pop();
SymVal x5360 = SymStack.pop();
Num x5361 = Stack.pop();
SymVal x5362 = SymStack.pop();
Num x5363 = x5361.i32_div_s(x5359);
Stack.push(x5363);
bool x5364 = allConcrete(x5362, x5360);
SymVal x5365 = x5364 ? Concrete(x5363, 32) : x5362.div(x5360);
SymStack.push(x5365);
}
{
Num x5366 = Stack.pop();
SymVal x5367 = SymStack.pop();
Num x5368 = Stack.pop();
SymStack.pop();
int x5369 = x5368.toInt();
Memory.storeInt(x5369, 4, x5366.toInt());
SymMemory.storeSym(x5369, 4, x5367);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5370 = Stack.pop();
SymVal x5371 = SymStack.pop();
Frames.set(3, x5370);
SymFrames.set(3, x5371);
}
__attribute__((musttail)) return x5352(std::monostate{});
return std::monostate{};
}
std::monostate x5352(std::monostate x5353) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x4879(std::monostate{});
return std::monostate{};
}
std::monostate x4879(std::monostate x5283) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5284 = Stack.pop();
SymStack.pop();
Num x5285 = I32V(Memory.loadInt(x5284.toInt(), 0));
SymVal x5286 = SymMemory.loadSym(x5284.toInt(), 0);
Stack.push(x5285);
SymStack.push(x5286);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5287 = Stack.pop();
SymVal x5288 = SymStack.pop();
Num x5289 = Stack.pop();
SymVal x5290 = SymStack.pop();
Num x5291 = x5289.i32_sub(x5287);
Stack.push(x5291);
bool x5292 = allConcrete(x5290, x5288);
SymVal x5293 = x5292 ? Concrete(x5291, 32) : x5290.minus(x5288);
SymStack.push(x5293);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5294 = Stack.pop();
SymVal x5295 = SymStack.pop();
Num x5296 = Stack.pop();
SymVal x5297 = SymStack.pop();
Num x5298 = x5296.i32_mul(x5294);
Stack.push(x5298);
bool x5299 = allConcrete(x5297, x5295);
SymVal x5300 = x5299 ? Concrete(x5298, 32) : x5297.mul(x5295);
SymStack.push(x5300);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5301 = Stack.pop();
SymVal x5302 = SymStack.pop();
Num x5303 = Stack.pop();
SymVal x5304 = SymStack.pop();
Num x5305 = x5303.i32_add(x5301);
Stack.push(x5305);
bool x5306 = allConcrete(x5304, x5302);
SymVal x5307 = x5306 ? Concrete(x5305, 32) : x5304.add(x5302);
SymStack.push(x5307);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5308 = Stack.pop();
SymVal x5309 = SymStack.pop();
Num x5310 = Stack.pop();
SymVal x5311 = SymStack.pop();
Num x5312 = x5310.i32_mul(x5308);
Stack.push(x5312);
bool x5313 = allConcrete(x5311, x5309);
SymVal x5314 = x5313 ? Concrete(x5312, 32) : x5311.mul(x5309);
SymStack.push(x5314);
}
{
Num x5315 = Stack.pop();
SymVal x5316 = SymStack.pop();
Num x5317 = Stack.pop();
SymVal x5318 = SymStack.pop();
Num x5319 = x5317.i32_add(x5315);
Stack.push(x5319);
bool x5320 = allConcrete(x5318, x5316);
SymVal x5321 = x5320 ? Concrete(x5319, 32) : x5318.add(x5316);
SymStack.push(x5321);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x5322 = Stack.pop();
SymVal x5323 = SymStack.pop();
Num x5324 = Stack.pop();
SymVal x5325 = SymStack.pop();
Num x5326 = x5324.i32_add(x5322);
Stack.push(x5326);
bool x5327 = allConcrete(x5325, x5323);
SymVal x5328 = x5327 ? Concrete(x5326, 32) : x5325.add(x5323);
SymStack.push(x5328);
}
{
Num x5329 = Stack.pop();
SymStack.pop();
Num x5330 = I32V(Memory.loadInt(x5329.toInt(), 8));
SymVal x5331 = SymMemory.loadSym(x5329.toInt(), 8);
Stack.push(x5330);
SymStack.push(x5331);
}
{
Num x5332 = Stack.pop();
SymStack.pop();
Num x5333 = I32V(Memory.loadInt(x5332.toInt(), 4));
SymVal x5334 = SymMemory.loadSym(x5332.toInt(), 4);
Stack.push(x5333);
SymStack.push(x5334);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5335 = Stack.pop();
SymVal x5336 = SymStack.pop();
Num x5337 = Stack.pop();
SymVal x5338 = SymStack.pop();
Num x5339 = x5337.i32_sub(x5335);
Stack.push(x5339);
bool x5340 = allConcrete(x5338, x5336);
SymVal x5341 = x5340 ? Concrete(x5339, 32) : x5338.minus(x5336);
SymStack.push(x5341);
}
{
Num x5342 = Stack.pop();
SymVal x5343 = SymStack.pop();
Num x5344 = Stack.pop();
SymVal x5345 = SymStack.pop();
Num x5346 = x5344.i32_eq(x5342);
Stack.push(x5346);
bool x5347 = allConcrete(x5345, x5343);
SymVal x5348 = x5347 ? Concrete(x5346, 32) : x5345.eq(x5343).bool2bv();
SymStack.push(x5348);
}
Num x5349 = Stack.pop();
{
SymVal x5350 = SymStack.pop();
ExploreTree.fillIfElseNode(x5350, 51);
}
int x5351 = x5349.toInt();
if (x5351 != 0) {
ExploreTree.moveCursor(true, makeControl(x4730, CURRENT_MCONT));
__attribute__((musttail)) return x5281(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x5281, CURRENT_MCONT));
__attribute__((musttail)) return x4730(std::monostate{});
}
return std::monostate{};
}
std::monostate x5281(std::monostate x5282) {
info("Entering the true branch 51 of the if");
info("Jump to 2");
__attribute__((musttail)) return x5266(std::monostate{});
return std::monostate{};
}
std::monostate x5266(std::monostate x5267) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x5268 = Stack.pop();
SymStack.pop();
Num x5269 = I32V(Memory.loadInt(x5268.toInt(), 0));
SymVal x5270 = SymMemory.loadSym(x5268.toInt(), 0);
Stack.push(x5269);
SymStack.push(x5270);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5271 = Stack.pop();
SymVal x5272 = SymStack.pop();
Num x5273 = Stack.pop();
SymVal x5274 = SymStack.pop();
Num x5275 = x5273.i32_ne(x5271);
Stack.push(x5275);
bool x5276 = allConcrete(x5274, x5272);
SymVal x5277 = x5276 ? Concrete(x5275, 32) : x5274.neq(x5272).bool2bv();
SymStack.push(x5277);
}
Num x5278 = Stack.pop();
{
SymVal x5279 = SymStack.pop();
ExploreTree.fillIfElseNode(x5279, 52);
}
int x5280 = x5278.toInt();
if (x5280 != 0) {
ExploreTree.moveCursor(true, makeControl(x4994, CURRENT_MCONT));
__attribute__((musttail)) return x5262(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x5262, CURRENT_MCONT));
__attribute__((musttail)) return x4994(std::monostate{});
}
return std::monostate{};
}
std::monostate x5262(std::monostate x5263) {
info("Entering the true branch 52 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5264 = Stack.pop();
SymVal x5265 = SymStack.pop();
Frames.set(3, x5264);
SymFrames.set(3, x5265);
}
__attribute__((musttail)) return x5260(std::monostate{});
return std::monostate{};
}
std::monostate x5260(std::monostate x5261) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x5193(std::monostate{});
return std::monostate{};
}
std::monostate x5193(std::monostate x5198) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5199 = Stack.pop();
SymStack.pop();
Num x5200 = I32V(Memory.loadInt(x5199.toInt(), 0));
SymVal x5201 = SymMemory.loadSym(x5199.toInt(), 0);
Stack.push(x5200);
SymStack.push(x5201);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5202 = Stack.pop();
SymVal x5203 = SymStack.pop();
Num x5204 = Stack.pop();
SymVal x5205 = SymStack.pop();
Num x5206 = x5204.i32_sub(x5202);
Stack.push(x5206);
bool x5207 = allConcrete(x5205, x5203);
SymVal x5208 = x5207 ? Concrete(x5206, 32) : x5205.minus(x5203);
SymStack.push(x5208);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5209 = Stack.pop();
SymVal x5210 = SymStack.pop();
Num x5211 = Stack.pop();
SymVal x5212 = SymStack.pop();
Num x5213 = x5211.i32_mul(x5209);
Stack.push(x5213);
bool x5214 = allConcrete(x5212, x5210);
SymVal x5215 = x5214 ? Concrete(x5213, 32) : x5212.mul(x5210);
SymStack.push(x5215);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5216 = Stack.pop();
SymVal x5217 = SymStack.pop();
Num x5218 = Stack.pop();
SymVal x5219 = SymStack.pop();
Num x5220 = x5218.i32_add(x5216);
Stack.push(x5220);
bool x5221 = allConcrete(x5219, x5217);
SymVal x5222 = x5221 ? Concrete(x5220, 32) : x5219.add(x5217);
SymStack.push(x5222);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5223 = Stack.pop();
SymVal x5224 = SymStack.pop();
Num x5225 = Stack.pop();
SymVal x5226 = SymStack.pop();
Num x5227 = x5225.i32_mul(x5223);
Stack.push(x5227);
bool x5228 = allConcrete(x5226, x5224);
SymVal x5229 = x5228 ? Concrete(x5227, 32) : x5226.mul(x5224);
SymStack.push(x5229);
}
{
Num x5230 = Stack.pop();
SymVal x5231 = SymStack.pop();
Num x5232 = Stack.pop();
SymVal x5233 = SymStack.pop();
Num x5234 = x5232.i32_add(x5230);
Stack.push(x5234);
bool x5235 = allConcrete(x5233, x5231);
SymVal x5236 = x5235 ? Concrete(x5234, 32) : x5233.add(x5231);
SymStack.push(x5236);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x5237 = Stack.pop();
SymVal x5238 = SymStack.pop();
Num x5239 = Stack.pop();
SymVal x5240 = SymStack.pop();
Num x5241 = x5239.i32_add(x5237);
Stack.push(x5241);
bool x5242 = allConcrete(x5240, x5238);
SymVal x5243 = x5242 ? Concrete(x5241, 32) : x5240.add(x5238);
SymStack.push(x5243);
}
{
Num x5244 = Stack.pop();
SymStack.pop();
Num x5245 = I32V(Memory.loadInt(x5244.toInt(), 8));
SymVal x5246 = SymMemory.loadSym(x5244.toInt(), 8);
Stack.push(x5245);
SymStack.push(x5246);
}
{
Num x5247 = Stack.pop();
SymStack.pop();
Num x5248 = I32V(Memory.loadInt(x5247.toInt(), 4));
SymVal x5249 = SymMemory.loadSym(x5247.toInt(), 4);
Stack.push(x5248);
SymStack.push(x5249);
}
{
Num x5250 = Stack.pop();
SymVal x5251 = SymStack.pop();
Num x5252 = Stack.pop();
SymVal x5253 = SymStack.pop();
Num x5254 = x5252.i32_eq(x5250);
Stack.push(x5254);
bool x5255 = allConcrete(x5253, x5251);
SymVal x5256 = x5255 ? Concrete(x5254, 32) : x5253.eq(x5251).bool2bv();
SymStack.push(x5256);
}
Num x5257 = Stack.pop();
{
SymVal x5258 = SymStack.pop();
ExploreTree.fillIfElseNode(x5258, 53);
}
int x5259 = x5257.toInt();
if (x5259 != 0) {
ExploreTree.moveCursor(true, makeControl(x4996, CURRENT_MCONT));
__attribute__((musttail)) return x5196(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x5196, CURRENT_MCONT));
__attribute__((musttail)) return x4996(std::monostate{});
}
return std::monostate{};
}
std::monostate x5196(std::monostate x5197) {
info("Entering the true branch 53 of the if");
info("Jump to 2");
__attribute__((musttail)) return x5194(std::monostate{});
return std::monostate{};
}
std::monostate x5194(std::monostate x5195) {
info("Exiting the block, stackSize =", Stack.size());
__attribute__((musttail)) return x4880(std::monostate{});
return std::monostate{};
}
std::monostate x4996(std::monostate x4997) {
info("Entering the false branch 53 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4998 = Stack.pop();
SymStack.pop();
Num x4999 = I32V(Memory.loadInt(x4998.toInt(), 0));
SymVal x5000 = SymMemory.loadSym(x4998.toInt(), 0);
Stack.push(x4999);
SymStack.push(x5000);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5001 = Stack.pop();
SymVal x5002 = SymStack.pop();
Num x5003 = Stack.pop();
SymVal x5004 = SymStack.pop();
Num x5005 = x5003.i32_sub(x5001);
Stack.push(x5005);
bool x5006 = allConcrete(x5004, x5002);
SymVal x5007 = x5006 ? Concrete(x5005, 32) : x5004.minus(x5002);
SymStack.push(x5007);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5008 = Stack.pop();
SymVal x5009 = SymStack.pop();
Num x5010 = Stack.pop();
SymVal x5011 = SymStack.pop();
Num x5012 = x5010.i32_mul(x5008);
Stack.push(x5012);
bool x5013 = allConcrete(x5011, x5009);
SymVal x5014 = x5013 ? Concrete(x5012, 32) : x5011.mul(x5009);
SymStack.push(x5014);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5015 = Stack.pop();
SymVal x5016 = SymStack.pop();
Num x5017 = Stack.pop();
SymVal x5018 = SymStack.pop();
Num x5019 = x5017.i32_mul(x5015);
Stack.push(x5019);
bool x5020 = allConcrete(x5018, x5016);
SymVal x5021 = x5020 ? Concrete(x5019, 32) : x5018.mul(x5016);
SymStack.push(x5021);
}
{
Num x5022 = Stack.pop();
SymVal x5023 = SymStack.pop();
Num x5024 = Stack.pop();
SymVal x5025 = SymStack.pop();
Num x5026 = x5024.i32_add(x5022);
Stack.push(x5026);
bool x5027 = allConcrete(x5025, x5023);
SymVal x5028 = x5027 ? Concrete(x5026, 32) : x5025.add(x5023);
SymStack.push(x5028);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5029 = Stack.pop();
SymStack.pop();
Num x5030 = I32V(Memory.loadInt(x5029.toInt(), 0));
SymVal x5031 = SymMemory.loadSym(x5029.toInt(), 0);
Stack.push(x5030);
SymStack.push(x5031);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5032 = Stack.pop();
SymVal x5033 = SymStack.pop();
Num x5034 = Stack.pop();
SymVal x5035 = SymStack.pop();
Num x5036 = x5034.i32_sub(x5032);
Stack.push(x5036);
bool x5037 = allConcrete(x5035, x5033);
SymVal x5038 = x5037 ? Concrete(x5036, 32) : x5035.minus(x5033);
SymStack.push(x5038);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5039 = Stack.pop();
SymVal x5040 = SymStack.pop();
Num x5041 = Stack.pop();
SymVal x5042 = SymStack.pop();
Num x5043 = x5041.i32_mul(x5039);
Stack.push(x5043);
bool x5044 = allConcrete(x5042, x5040);
SymVal x5045 = x5044 ? Concrete(x5043, 32) : x5042.mul(x5040);
SymStack.push(x5045);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5046 = Stack.pop();
SymVal x5047 = SymStack.pop();
Num x5048 = Stack.pop();
SymVal x5049 = SymStack.pop();
Num x5050 = x5048.i32_add(x5046);
Stack.push(x5050);
bool x5051 = allConcrete(x5049, x5047);
SymVal x5052 = x5051 ? Concrete(x5050, 32) : x5049.add(x5047);
SymStack.push(x5052);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5053 = Stack.pop();
SymVal x5054 = SymStack.pop();
Num x5055 = Stack.pop();
SymVal x5056 = SymStack.pop();
Num x5057 = x5055.i32_mul(x5053);
Stack.push(x5057);
bool x5058 = allConcrete(x5056, x5054);
SymVal x5059 = x5058 ? Concrete(x5057, 32) : x5056.mul(x5054);
SymStack.push(x5059);
}
{
Num x5060 = Stack.pop();
SymVal x5061 = SymStack.pop();
Num x5062 = Stack.pop();
SymVal x5063 = SymStack.pop();
Num x5064 = x5062.i32_add(x5060);
Stack.push(x5064);
bool x5065 = allConcrete(x5063, x5061);
SymVal x5066 = x5065 ? Concrete(x5064, 32) : x5063.add(x5061);
SymStack.push(x5066);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
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
{
Num x5074 = Stack.pop();
SymStack.pop();
Num x5075 = I32V(Memory.loadInt(x5074.toInt(), 8));
SymVal x5076 = SymMemory.loadSym(x5074.toInt(), 8);
Stack.push(x5075);
SymStack.push(x5076);
}
{
Num x5077 = Stack.pop();
SymVal x5078 = SymStack.pop();
Num x5079 = Stack.pop();
SymVal x5080 = SymStack.pop();
Num x5081 = x5079.i32_add(x5077);
Stack.push(x5081);
bool x5082 = allConcrete(x5080, x5078);
SymVal x5083 = x5082 ? Concrete(x5081, 32) : x5080.add(x5078);
SymStack.push(x5083);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5084 = Stack.pop();
SymStack.pop();
Num x5085 = I32V(Memory.loadInt(x5084.toInt(), 0));
SymVal x5086 = SymMemory.loadSym(x5084.toInt(), 0);
Stack.push(x5085);
SymStack.push(x5086);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5087 = Stack.pop();
SymVal x5088 = SymStack.pop();
Num x5089 = Stack.pop();
SymVal x5090 = SymStack.pop();
Num x5091 = x5089.i32_sub(x5087);
Stack.push(x5091);
bool x5092 = allConcrete(x5090, x5088);
SymVal x5093 = x5092 ? Concrete(x5091, 32) : x5090.minus(x5088);
SymStack.push(x5093);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5094 = Stack.pop();
SymVal x5095 = SymStack.pop();
Num x5096 = Stack.pop();
SymVal x5097 = SymStack.pop();
Num x5098 = x5096.i32_mul(x5094);
Stack.push(x5098);
bool x5099 = allConcrete(x5097, x5095);
SymVal x5100 = x5099 ? Concrete(x5098, 32) : x5097.mul(x5095);
SymStack.push(x5100);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5101 = Stack.pop();
SymVal x5102 = SymStack.pop();
Num x5103 = Stack.pop();
SymVal x5104 = SymStack.pop();
Num x5105 = x5103.i32_add(x5101);
Stack.push(x5105);
bool x5106 = allConcrete(x5104, x5102);
SymVal x5107 = x5106 ? Concrete(x5105, 32) : x5104.add(x5102);
SymStack.push(x5107);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5108 = Stack.pop();
SymVal x5109 = SymStack.pop();
Num x5110 = Stack.pop();
SymVal x5111 = SymStack.pop();
Num x5112 = x5110.i32_mul(x5108);
Stack.push(x5112);
bool x5113 = allConcrete(x5111, x5109);
SymVal x5114 = x5113 ? Concrete(x5112, 32) : x5111.mul(x5109);
SymStack.push(x5114);
}
{
Num x5115 = Stack.pop();
SymVal x5116 = SymStack.pop();
Num x5117 = Stack.pop();
SymVal x5118 = SymStack.pop();
Num x5119 = x5117.i32_add(x5115);
Stack.push(x5119);
bool x5120 = allConcrete(x5118, x5116);
SymVal x5121 = x5120 ? Concrete(x5119, 32) : x5118.add(x5116);
SymStack.push(x5121);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x5122 = Stack.pop();
SymStack.pop();
Num x5123 = I32V(Memory.loadInt(x5122.toInt(), 0));
SymVal x5124 = SymMemory.loadSym(x5122.toInt(), 0);
Stack.push(x5123);
SymStack.push(x5124);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5125 = Stack.pop();
SymVal x5126 = SymStack.pop();
Num x5127 = Stack.pop();
SymVal x5128 = SymStack.pop();
Num x5129 = x5127.i32_sub(x5125);
Stack.push(x5129);
bool x5130 = allConcrete(x5128, x5126);
SymVal x5131 = x5130 ? Concrete(x5129, 32) : x5128.minus(x5126);
SymStack.push(x5131);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5132 = Stack.pop();
SymVal x5133 = SymStack.pop();
Num x5134 = Stack.pop();
SymVal x5135 = SymStack.pop();
Num x5136 = x5134.i32_mul(x5132);
Stack.push(x5136);
bool x5137 = allConcrete(x5135, x5133);
SymVal x5138 = x5137 ? Concrete(x5136, 32) : x5135.mul(x5133);
SymStack.push(x5138);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5139 = Stack.pop();
SymVal x5140 = SymStack.pop();
Num x5141 = Stack.pop();
SymVal x5142 = SymStack.pop();
Num x5143 = x5141.i32_add(x5139);
Stack.push(x5143);
bool x5144 = allConcrete(x5142, x5140);
SymVal x5145 = x5144 ? Concrete(x5143, 32) : x5142.add(x5140);
SymStack.push(x5145);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x5146 = Stack.pop();
SymVal x5147 = SymStack.pop();
Num x5148 = Stack.pop();
SymVal x5149 = SymStack.pop();
Num x5150 = x5148.i32_mul(x5146);
Stack.push(x5150);
bool x5151 = allConcrete(x5149, x5147);
SymVal x5152 = x5151 ? Concrete(x5150, 32) : x5149.mul(x5147);
SymStack.push(x5152);
}
{
Num x5153 = Stack.pop();
SymVal x5154 = SymStack.pop();
Num x5155 = Stack.pop();
SymVal x5156 = SymStack.pop();
Num x5157 = x5155.i32_add(x5153);
Stack.push(x5157);
bool x5158 = allConcrete(x5156, x5154);
SymVal x5159 = x5158 ? Concrete(x5157, 32) : x5156.add(x5154);
SymStack.push(x5159);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x5160 = Stack.pop();
SymVal x5161 = SymStack.pop();
Num x5162 = Stack.pop();
SymVal x5163 = SymStack.pop();
Num x5164 = x5162.i32_add(x5160);
Stack.push(x5164);
bool x5165 = allConcrete(x5163, x5161);
SymVal x5166 = x5165 ? Concrete(x5164, 32) : x5163.add(x5161);
SymStack.push(x5166);
}
{
Num x5167 = Stack.pop();
SymStack.pop();
Num x5168 = I32V(Memory.loadInt(x5167.toInt(), 8));
SymVal x5169 = SymMemory.loadSym(x5167.toInt(), 8);
Stack.push(x5168);
SymStack.push(x5169);
}
{
Num x5170 = Stack.pop();
SymVal x5171 = SymStack.pop();
Num x5172 = Stack.pop();
SymVal x5173 = SymStack.pop();
Num x5174 = x5172.i32_add(x5170);
Stack.push(x5174);
bool x5175 = allConcrete(x5173, x5171);
SymVal x5176 = x5175 ? Concrete(x5174, 32) : x5173.add(x5171);
SymStack.push(x5176);
}
{
Num x5177 = Stack.pop();
SymStack.pop();
Num x5178 = I32V(Memory.loadInt(x5177.toInt(), 8));
SymVal x5179 = SymMemory.loadSym(x5177.toInt(), 8);
Stack.push(x5178);
SymStack.push(x5179);
}
{
Num x5180 = Stack.pop();
SymVal x5181 = SymStack.pop();
Num x5182 = Stack.pop();
SymStack.pop();
int x5183 = x5182.toInt();
Memory.storeInt(x5183, 8, x5180.toInt());
SymMemory.storeSym(x5183, 8, x5181);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x5184 = Stack.pop();
SymVal x5185 = SymStack.pop();
Num x5186 = Stack.pop();
SymVal x5187 = SymStack.pop();
Num x5188 = x5186.i32_add(x5184);
Stack.push(x5188);
bool x5189 = allConcrete(x5187, x5185);
SymVal x5190 = x5189 ? Concrete(x5188, 32) : x5187.add(x5185);
SymStack.push(x5190);
}
{
Num x5191 = Stack.pop();
SymVal x5192 = SymStack.pop();
Frames.set(3, x5191);
SymFrames.set(3, x5192);
}
info("Jump to 1");
__attribute__((musttail)) return x5193(std::monostate{});
return std::monostate{};
}
std::monostate x4994(std::monostate x4995) {
info("Entering the false branch 52 of the if");
__attribute__((musttail)) return x4880(std::monostate{});
return std::monostate{};
}
std::monostate x4880(std::monostate x4881) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4882 = Stack.pop();
SymStack.pop();
Num x4883 = I32V(Memory.loadInt(x4882.toInt(), 0));
SymVal x4884 = SymMemory.loadSym(x4882.toInt(), 0);
Stack.push(x4883);
SymStack.push(x4884);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4885 = Stack.pop();
SymVal x4886 = SymStack.pop();
Num x4887 = Stack.pop();
SymVal x4888 = SymStack.pop();
Num x4889 = x4887.i32_sub(x4885);
Stack.push(x4889);
bool x4890 = allConcrete(x4888, x4886);
SymVal x4891 = x4890 ? Concrete(x4889, 32) : x4888.minus(x4886);
SymStack.push(x4891);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4892 = Stack.pop();
SymVal x4893 = SymStack.pop();
Num x4894 = Stack.pop();
SymVal x4895 = SymStack.pop();
Num x4896 = x4894.i32_mul(x4892);
Stack.push(x4896);
bool x4897 = allConcrete(x4895, x4893);
SymVal x4898 = x4897 ? Concrete(x4896, 32) : x4895.mul(x4893);
SymStack.push(x4898);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4899 = Stack.pop();
SymVal x4900 = SymStack.pop();
Num x4901 = Stack.pop();
SymVal x4902 = SymStack.pop();
Num x4903 = x4901.i32_add(x4899);
Stack.push(x4903);
bool x4904 = allConcrete(x4902, x4900);
SymVal x4905 = x4904 ? Concrete(x4903, 32) : x4902.add(x4900);
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
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x4920 = Stack.pop();
SymVal x4921 = SymStack.pop();
Num x4922 = Stack.pop();
SymVal x4923 = SymStack.pop();
Num x4924 = x4922.i32_add(x4920);
Stack.push(x4924);
bool x4925 = allConcrete(x4923, x4921);
SymVal x4926 = x4925 ? Concrete(x4924, 32) : x4923.add(x4921);
SymStack.push(x4926);
}
{
Num x4927 = Stack.pop();
SymStack.pop();
Num x4928 = I32V(Memory.loadInt(x4927.toInt(), 8));
SymVal x4929 = SymMemory.loadSym(x4927.toInt(), 8);
Stack.push(x4928);
SymStack.push(x4929);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4930 = Stack.pop();
SymStack.pop();
Num x4931 = I32V(Memory.loadInt(x4930.toInt(), 0));
SymVal x4932 = SymMemory.loadSym(x4930.toInt(), 0);
Stack.push(x4931);
SymStack.push(x4932);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4933 = Stack.pop();
SymVal x4934 = SymStack.pop();
Num x4935 = Stack.pop();
SymVal x4936 = SymStack.pop();
Num x4937 = x4935.i32_sub(x4933);
Stack.push(x4937);
bool x4938 = allConcrete(x4936, x4934);
SymVal x4939 = x4938 ? Concrete(x4937, 32) : x4936.minus(x4934);
SymStack.push(x4939);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4940 = Stack.pop();
SymVal x4941 = SymStack.pop();
Num x4942 = Stack.pop();
SymVal x4943 = SymStack.pop();
Num x4944 = x4942.i32_mul(x4940);
Stack.push(x4944);
bool x4945 = allConcrete(x4943, x4941);
SymVal x4946 = x4945 ? Concrete(x4944, 32) : x4943.mul(x4941);
SymStack.push(x4946);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4947 = Stack.pop();
SymVal x4948 = SymStack.pop();
Num x4949 = Stack.pop();
SymVal x4950 = SymStack.pop();
Num x4951 = x4949.i32_add(x4947);
Stack.push(x4951);
bool x4952 = allConcrete(x4950, x4948);
SymVal x4953 = x4952 ? Concrete(x4951, 32) : x4950.add(x4948);
SymStack.push(x4953);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4954 = Stack.pop();
SymVal x4955 = SymStack.pop();
Num x4956 = Stack.pop();
SymVal x4957 = SymStack.pop();
Num x4958 = x4956.i32_mul(x4954);
Stack.push(x4958);
bool x4959 = allConcrete(x4957, x4955);
SymVal x4960 = x4959 ? Concrete(x4958, 32) : x4957.mul(x4955);
SymStack.push(x4960);
}
{
Num x4961 = Stack.pop();
SymVal x4962 = SymStack.pop();
Num x4963 = Stack.pop();
SymVal x4964 = SymStack.pop();
Num x4965 = x4963.i32_add(x4961);
Stack.push(x4965);
bool x4966 = allConcrete(x4964, x4962);
SymVal x4967 = x4966 ? Concrete(x4965, 32) : x4964.add(x4962);
SymStack.push(x4967);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x4968 = Stack.pop();
SymVal x4969 = SymStack.pop();
Num x4970 = Stack.pop();
SymVal x4971 = SymStack.pop();
Num x4972 = x4970.i32_add(x4968);
Stack.push(x4972);
bool x4973 = allConcrete(x4971, x4969);
SymVal x4974 = x4973 ? Concrete(x4972, 32) : x4971.add(x4969);
SymStack.push(x4974);
}
{
Num x4975 = Stack.pop();
SymStack.pop();
Num x4976 = I32V(Memory.loadInt(x4975.toInt(), 8));
SymVal x4977 = SymMemory.loadSym(x4975.toInt(), 8);
Stack.push(x4976);
SymStack.push(x4977);
}
{
Num x4978 = Stack.pop();
SymStack.pop();
Num x4979 = I32V(Memory.loadInt(x4978.toInt(), 4));
SymVal x4980 = SymMemory.loadSym(x4978.toInt(), 4);
Stack.push(x4979);
SymStack.push(x4980);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4981 = Stack.pop();
SymVal x4982 = SymStack.pop();
Num x4983 = Stack.pop();
SymVal x4984 = SymStack.pop();
Num x4985 = x4983.i32_sub(x4981);
Stack.push(x4985);
bool x4986 = allConcrete(x4984, x4982);
SymVal x4987 = x4986 ? Concrete(x4985, 32) : x4984.minus(x4982);
SymStack.push(x4987);
}
{
Num x4988 = Stack.pop();
SymVal x4989 = SymStack.pop();
Num x4990 = Stack.pop();
SymStack.pop();
int x4991 = x4990.toInt();
Memory.storeInt(x4991, 4, x4988.toInt());
SymMemory.storeSym(x4991, 4, x4989);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4992 = Stack.pop();
SymVal x4993 = SymStack.pop();
Frames.set(4, x4992);
SymFrames.set(4, x4993);
}
__attribute__((musttail)) return x4726(std::monostate{});
return std::monostate{};
}
std::monostate x4730(std::monostate x4731) {
info("Entering the false branch 51 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4732 = Stack.pop();
SymStack.pop();
Num x4733 = I32V(Memory.loadInt(x4732.toInt(), 0));
SymVal x4734 = SymMemory.loadSym(x4732.toInt(), 0);
Stack.push(x4733);
SymStack.push(x4734);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4735 = Stack.pop();
SymVal x4736 = SymStack.pop();
Num x4737 = Stack.pop();
SymVal x4738 = SymStack.pop();
Num x4739 = x4737.i32_sub(x4735);
Stack.push(x4739);
bool x4740 = allConcrete(x4738, x4736);
SymVal x4741 = x4740 ? Concrete(x4739, 32) : x4738.minus(x4736);
SymStack.push(x4741);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4742 = Stack.pop();
SymVal x4743 = SymStack.pop();
Num x4744 = Stack.pop();
SymVal x4745 = SymStack.pop();
Num x4746 = x4744.i32_mul(x4742);
Stack.push(x4746);
bool x4747 = allConcrete(x4745, x4743);
SymVal x4748 = x4747 ? Concrete(x4746, 32) : x4745.mul(x4743);
SymStack.push(x4748);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4749 = Stack.pop();
SymVal x4750 = SymStack.pop();
Num x4751 = Stack.pop();
SymVal x4752 = SymStack.pop();
Num x4753 = x4751.i32_add(x4749);
Stack.push(x4753);
bool x4754 = allConcrete(x4752, x4750);
SymVal x4755 = x4754 ? Concrete(x4753, 32) : x4752.add(x4750);
SymStack.push(x4755);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4756 = Stack.pop();
SymVal x4757 = SymStack.pop();
Num x4758 = Stack.pop();
SymVal x4759 = SymStack.pop();
Num x4760 = x4758.i32_mul(x4756);
Stack.push(x4760);
bool x4761 = allConcrete(x4759, x4757);
SymVal x4762 = x4761 ? Concrete(x4760, 32) : x4759.mul(x4757);
SymStack.push(x4762);
}
{
Num x4763 = Stack.pop();
SymVal x4764 = SymStack.pop();
Num x4765 = Stack.pop();
SymVal x4766 = SymStack.pop();
Num x4767 = x4765.i32_add(x4763);
Stack.push(x4767);
bool x4768 = allConcrete(x4766, x4764);
SymVal x4769 = x4768 ? Concrete(x4767, 32) : x4766.add(x4764);
SymStack.push(x4769);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x4770 = Stack.pop();
SymVal x4771 = SymStack.pop();
Num x4772 = Stack.pop();
SymVal x4773 = SymStack.pop();
Num x4774 = x4772.i32_add(x4770);
Stack.push(x4774);
bool x4775 = allConcrete(x4773, x4771);
SymVal x4776 = x4775 ? Concrete(x4774, 32) : x4773.add(x4771);
SymStack.push(x4776);
}
{
Num x4777 = Stack.pop();
SymStack.pop();
Num x4778 = I32V(Memory.loadInt(x4777.toInt(), 8));
SymVal x4779 = SymMemory.loadSym(x4777.toInt(), 8);
Stack.push(x4778);
SymStack.push(x4779);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x4780 = Stack.pop();
SymVal x4781 = SymStack.pop();
Num x4782 = Stack.pop();
SymVal x4783 = SymStack.pop();
Num x4784 = x4782.i32_mul(x4780);
Stack.push(x4784);
bool x4785 = allConcrete(x4783, x4781);
SymVal x4786 = x4785 ? Concrete(x4784, 32) : x4783.mul(x4781);
SymStack.push(x4786);
}
{
Num x4787 = Stack.pop();
SymVal x4788 = SymStack.pop();
Num x4789 = Stack.pop();
SymVal x4790 = SymStack.pop();
Num x4791 = x4789.i32_add(x4787);
Stack.push(x4791);
bool x4792 = allConcrete(x4790, x4788);
SymVal x4793 = x4792 ? Concrete(x4791, 32) : x4790.add(x4788);
SymStack.push(x4793);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4794 = Stack.pop();
SymStack.pop();
Num x4795 = I32V(Memory.loadInt(x4794.toInt(), 0));
SymVal x4796 = SymMemory.loadSym(x4794.toInt(), 0);
Stack.push(x4795);
SymStack.push(x4796);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4797 = Stack.pop();
SymVal x4798 = SymStack.pop();
Num x4799 = Stack.pop();
SymVal x4800 = SymStack.pop();
Num x4801 = x4799.i32_sub(x4797);
Stack.push(x4801);
bool x4802 = allConcrete(x4800, x4798);
SymVal x4803 = x4802 ? Concrete(x4801, 32) : x4800.minus(x4798);
SymStack.push(x4803);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4804 = Stack.pop();
SymVal x4805 = SymStack.pop();
Num x4806 = Stack.pop();
SymVal x4807 = SymStack.pop();
Num x4808 = x4806.i32_mul(x4804);
Stack.push(x4808);
bool x4809 = allConcrete(x4807, x4805);
SymVal x4810 = x4809 ? Concrete(x4808, 32) : x4807.mul(x4805);
SymStack.push(x4810);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4811 = Stack.pop();
SymVal x4812 = SymStack.pop();
Num x4813 = Stack.pop();
SymVal x4814 = SymStack.pop();
Num x4815 = x4813.i32_add(x4811);
Stack.push(x4815);
bool x4816 = allConcrete(x4814, x4812);
SymVal x4817 = x4816 ? Concrete(x4815, 32) : x4814.add(x4812);
SymStack.push(x4817);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4818 = Stack.pop();
SymVal x4819 = SymStack.pop();
Num x4820 = Stack.pop();
SymVal x4821 = SymStack.pop();
Num x4822 = x4820.i32_mul(x4818);
Stack.push(x4822);
bool x4823 = allConcrete(x4821, x4819);
SymVal x4824 = x4823 ? Concrete(x4822, 32) : x4821.mul(x4819);
SymStack.push(x4824);
}
{
Num x4825 = Stack.pop();
SymVal x4826 = SymStack.pop();
Num x4827 = Stack.pop();
SymVal x4828 = SymStack.pop();
Num x4829 = x4827.i32_add(x4825);
Stack.push(x4829);
bool x4830 = allConcrete(x4828, x4826);
SymVal x4831 = x4830 ? Concrete(x4829, 32) : x4828.add(x4826);
SymStack.push(x4831);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x4832 = Stack.pop();
SymVal x4833 = SymStack.pop();
Num x4834 = Stack.pop();
SymVal x4835 = SymStack.pop();
Num x4836 = x4834.i32_add(x4832);
Stack.push(x4836);
bool x4837 = allConcrete(x4835, x4833);
SymVal x4838 = x4837 ? Concrete(x4836, 32) : x4835.add(x4833);
SymStack.push(x4838);
}
{
Num x4839 = Stack.pop();
SymStack.pop();
Num x4840 = I32V(Memory.loadInt(x4839.toInt(), 8));
SymVal x4841 = SymMemory.loadSym(x4839.toInt(), 8);
Stack.push(x4840);
SymStack.push(x4841);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4842 = Stack.pop();
SymVal x4843 = SymStack.pop();
Num x4844 = Stack.pop();
SymVal x4845 = SymStack.pop();
Num x4846 = x4844.i32_add(x4842);
Stack.push(x4846);
bool x4847 = allConcrete(x4845, x4843);
SymVal x4848 = x4847 ? Concrete(x4846, 32) : x4845.add(x4843);
SymStack.push(x4848);
}
{
Num x4849 = Stack.pop();
SymVal x4850 = SymStack.pop();
Num x4851 = Stack.pop();
SymVal x4852 = SymStack.pop();
Num x4853 = x4851.i32_mul(x4849);
Stack.push(x4853);
bool x4854 = allConcrete(x4852, x4850);
SymVal x4855 = x4854 ? Concrete(x4853, 32) : x4852.mul(x4850);
SymStack.push(x4855);
}
{
Num x4856 = Stack.pop();
SymVal x4857 = SymStack.pop();
Num x4858 = Stack.pop();
SymVal x4859 = SymStack.pop();
Num x4860 = x4858.i32_add(x4856);
Stack.push(x4860);
bool x4861 = allConcrete(x4859, x4857);
SymVal x4862 = x4861 ? Concrete(x4860, 32) : x4859.add(x4857);
SymStack.push(x4862);
}
{
Num x4863 = Stack.pop();
SymStack.pop();
Num x4864 = I32V(Memory.loadInt(x4863.toInt(), 8));
SymVal x4865 = SymMemory.loadSym(x4863.toInt(), 8);
Stack.push(x4864);
SymStack.push(x4865);
}
{
Num x4866 = Stack.pop();
SymVal x4867 = SymStack.pop();
Num x4868 = Stack.pop();
SymStack.pop();
int x4869 = x4868.toInt();
Memory.storeInt(x4869, 8, x4866.toInt());
SymMemory.storeSym(x4869, 8, x4867);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4870 = Stack.pop();
SymVal x4871 = SymStack.pop();
Num x4872 = Stack.pop();
SymVal x4873 = SymStack.pop();
Num x4874 = x4872.i32_add(x4870);
Stack.push(x4874);
bool x4875 = allConcrete(x4873, x4871);
SymVal x4876 = x4875 ? Concrete(x4874, 32) : x4873.add(x4871);
SymStack.push(x4876);
}
{
Num x4877 = Stack.pop();
SymVal x4878 = SymStack.pop();
Frames.set(3, x4877);
SymFrames.set(3, x4878);
}
info("Jump to 1");
__attribute__((musttail)) return x4879(std::monostate{});
return std::monostate{};
}
std::monostate x4728(std::monostate x4729) {
info("Entering the false branch 49 of the if");
__attribute__((musttail)) return x4726(std::monostate{});
return std::monostate{};
}
std::monostate x4726(std::monostate x4727) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x4691(std::monostate{});
return std::monostate{};
}
std::monostate x4724(std::monostate x4725) {
info("Entering the false branch 31 of the if");
__attribute__((musttail)) return x4691(std::monostate{});
return std::monostate{};
}
std::monostate x4691(std::monostate x4692) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(Frames.get(4));
SymStack.push(SymFrames.get(4));
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x4693 = Stack.pop();
SymVal x4694 = SymStack.pop();
Num x4695 = Stack.pop();
SymVal x4696 = SymStack.pop();
Num x4697 = x4695.i32_eq(x4693);
Stack.push(x4697);
bool x4698 = allConcrete(x4696, x4694);
SymVal x4699 = x4698 ? Concrete(x4697, 32) : x4696.eq(x4694).bool2bv();
SymStack.push(x4699);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4700 = Stack.pop();
SymVal x4701 = SymStack.pop();
Num x4702 = Stack.pop();
SymVal x4703 = SymStack.pop();
Num x4704 = x4702.i32_sub(x4700);
Stack.push(x4704);
bool x4705 = allConcrete(x4703, x4701);
SymVal x4706 = x4705 ? Concrete(x4704, 32) : x4703.minus(x4701);
SymStack.push(x4706);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4707 = Stack.pop();
SymVal x4708 = SymStack.pop();
Num x4709 = Stack.pop();
SymVal x4710 = SymStack.pop();
Num x4711 = x4709.i32_ge_s(x4707);
Stack.push(x4711);
bool x4712 = allConcrete(x4710, x4708);
SymVal x4713 = x4712 ? Concrete(x4711, 32) : x4710.ge(x4708).bool2bv();
SymStack.push(x4713);
}
{
Num x4714 = Stack.pop();
SymVal x4715 = SymStack.pop();
Num x4716 = Stack.pop();
SymVal x4717 = SymStack.pop();
Num x4718 = x4716.i32_and(x4714);
Stack.push(x4718);
bool x4719 = allConcrete(x4717, x4715);
SymVal x4720 = x4719 ? Concrete(x4718, 32) : x4717.bitwise_and(x4715);
SymStack.push(x4720);
}
Num x4721 = Stack.pop();
{
SymVal x4722 = SymStack.pop();
ExploreTree.fillIfElseNode(x4722, 32);
}
int x4723 = x4721.toInt();
if (x4723 != 0) {
ExploreTree.moveCursor(true, makeControl(x3867, CURRENT_MCONT));
__attribute__((musttail)) return x4618(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x4618, CURRENT_MCONT));
__attribute__((musttail)) return x3867(std::monostate{});
}
return std::monostate{};
}
std::monostate x4618(std::monostate x4619) {
info("Entering the true branch 32 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4620 = Stack.pop();
SymStack.pop();
Num x4621 = I32V(Memory.loadInt(x4620.toInt(), 0));
SymVal x4622 = SymMemory.loadSym(x4620.toInt(), 0);
Stack.push(x4621);
SymStack.push(x4622);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4623 = Stack.pop();
SymVal x4624 = SymStack.pop();
Num x4625 = Stack.pop();
SymVal x4626 = SymStack.pop();
Num x4627 = x4625.i32_sub(x4623);
Stack.push(x4627);
bool x4628 = allConcrete(x4626, x4624);
SymVal x4629 = x4628 ? Concrete(x4627, 32) : x4626.minus(x4624);
SymStack.push(x4629);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4630 = Stack.pop();
SymVal x4631 = SymStack.pop();
Num x4632 = Stack.pop();
SymVal x4633 = SymStack.pop();
Num x4634 = x4632.i32_mul(x4630);
Stack.push(x4634);
bool x4635 = allConcrete(x4633, x4631);
SymVal x4636 = x4635 ? Concrete(x4634, 32) : x4633.mul(x4631);
SymStack.push(x4636);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4637 = Stack.pop();
SymVal x4638 = SymStack.pop();
Num x4639 = Stack.pop();
SymVal x4640 = SymStack.pop();
Num x4641 = x4639.i32_sub(x4637);
Stack.push(x4641);
bool x4642 = allConcrete(x4640, x4638);
SymVal x4643 = x4642 ? Concrete(x4641, 32) : x4640.minus(x4638);
SymStack.push(x4643);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4644 = Stack.pop();
SymVal x4645 = SymStack.pop();
Num x4646 = Stack.pop();
SymVal x4647 = SymStack.pop();
Num x4648 = x4646.i32_mul(x4644);
Stack.push(x4648);
bool x4649 = allConcrete(x4647, x4645);
SymVal x4650 = x4649 ? Concrete(x4648, 32) : x4647.mul(x4645);
SymStack.push(x4650);
}
{
Num x4651 = Stack.pop();
SymVal x4652 = SymStack.pop();
Num x4653 = Stack.pop();
SymVal x4654 = SymStack.pop();
Num x4655 = x4653.i32_add(x4651);
Stack.push(x4655);
bool x4656 = allConcrete(x4654, x4652);
SymVal x4657 = x4656 ? Concrete(x4655, 32) : x4654.add(x4652);
SymStack.push(x4657);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x4658 = Stack.pop();
SymVal x4659 = SymStack.pop();
Num x4660 = Stack.pop();
SymVal x4661 = SymStack.pop();
Num x4662 = x4660.i32_add(x4658);
Stack.push(x4662);
bool x4663 = allConcrete(x4661, x4659);
SymVal x4664 = x4663 ? Concrete(x4662, 32) : x4661.add(x4659);
SymStack.push(x4664);
}
{
Num x4665 = Stack.pop();
SymStack.pop();
Num x4666 = I32V(Memory.loadInt(x4665.toInt(), 8));
SymVal x4667 = SymMemory.loadSym(x4665.toInt(), 8);
Stack.push(x4666);
SymStack.push(x4667);
}
{
Num x4668 = Stack.pop();
SymStack.pop();
Num x4669 = I32V(Memory.loadInt(x4668.toInt(), 4));
SymVal x4670 = SymMemory.loadSym(x4668.toInt(), 4);
Stack.push(x4669);
SymStack.push(x4670);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4671 = Stack.pop();
SymStack.pop();
Num x4672 = I32V(Memory.loadInt(x4671.toInt(), 0));
SymVal x4673 = SymMemory.loadSym(x4671.toInt(), 0);
Stack.push(x4672);
SymStack.push(x4673);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x4674 = Stack.pop();
SymVal x4675 = SymStack.pop();
Num x4676 = Stack.pop();
SymVal x4677 = SymStack.pop();
Num x4678 = x4676.i32_div_s(x4674);
Stack.push(x4678);
bool x4679 = allConcrete(x4677, x4675);
SymVal x4680 = x4679 ? Concrete(x4678, 32) : x4677.div(x4675);
SymStack.push(x4680);
}
{
Num x4681 = Stack.pop();
SymVal x4682 = SymStack.pop();
Num x4683 = Stack.pop();
SymVal x4684 = SymStack.pop();
Num x4685 = x4683.i32_ge_s(x4681);
Stack.push(x4685);
bool x4686 = allConcrete(x4684, x4682);
SymVal x4687 = x4686 ? Concrete(x4685, 32) : x4684.ge(x4682).bool2bv();
SymStack.push(x4687);
}
Num x4688 = Stack.pop();
{
SymVal x4689 = SymStack.pop();
ExploreTree.fillIfElseNode(x4689, 44);
}
int x4690 = x4688.toInt();
if (x4690 != 0) {
ExploreTree.moveCursor(true, makeControl(x3871, CURRENT_MCONT));
__attribute__((musttail)) return x4611(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x4611, CURRENT_MCONT));
__attribute__((musttail)) return x3871(std::monostate{});
}
return std::monostate{};
}
std::monostate x4611(std::monostate x4612) {
info("Entering the true branch 44 of the if");
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x4613 = Stack.pop();
SymStack.pop();
Num x4614 = I32V(Memory.loadInt(x4613.toInt(), 4));
SymVal x4615 = SymMemory.loadSym(x4613.toInt(), 4);
Stack.push(x4614);
SymStack.push(x4615);
}
{
Num x4616 = Stack.pop();
SymVal x4617 = SymStack.pop();
Frames.set(3, x4616);
SymFrames.set(3, x4617);
}
__attribute__((musttail)) return x4609(std::monostate{});
return std::monostate{};
}
std::monostate x4609(std::monostate x4610) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x3926(std::monostate{});
return std::monostate{};
}
std::monostate x3926(std::monostate x4598) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4599 = Stack.pop();
SymVal x4600 = SymStack.pop();
Num x4601 = Stack.pop();
SymVal x4602 = SymStack.pop();
Num x4603 = x4601.i32_eq(x4599);
Stack.push(x4603);
bool x4604 = allConcrete(x4602, x4600);
SymVal x4605 = x4604 ? Concrete(x4603, 32) : x4602.eq(x4600).bool2bv();
SymStack.push(x4605);
}
Num x4606 = Stack.pop();
{
SymVal x4607 = SymStack.pop();
ExploreTree.fillIfElseNode(x4607, 45);
}
int x4608 = x4606.toInt();
if (x4608 != 0) {
ExploreTree.moveCursor(true, makeControl(x3873, CURRENT_MCONT));
__attribute__((musttail)) return x4596(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x4596, CURRENT_MCONT));
__attribute__((musttail)) return x3873(std::monostate{});
}
return std::monostate{};
}
std::monostate x4596(std::monostate x4597) {
info("Entering the true branch 45 of the if");
info("Jump to 2");
__attribute__((musttail)) return x4581(std::monostate{});
return std::monostate{};
}
std::monostate x4581(std::monostate x4582) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x4583 = Stack.pop();
SymStack.pop();
Num x4584 = I32V(Memory.loadInt(x4583.toInt(), 0));
SymVal x4585 = SymMemory.loadSym(x4583.toInt(), 0);
Stack.push(x4584);
SymStack.push(x4585);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4586 = Stack.pop();
SymVal x4587 = SymStack.pop();
Num x4588 = Stack.pop();
SymVal x4589 = SymStack.pop();
Num x4590 = x4588.i32_ne(x4586);
Stack.push(x4590);
bool x4591 = allConcrete(x4589, x4587);
SymVal x4592 = x4591 ? Concrete(x4590, 32) : x4589.neq(x4587).bool2bv();
SymStack.push(x4592);
}
Num x4593 = Stack.pop();
{
SymVal x4594 = SymStack.pop();
ExploreTree.fillIfElseNode(x4594, 46);
}
int x4595 = x4593.toInt();
if (x4595 != 0) {
ExploreTree.moveCursor(true, makeControl(x4446, CURRENT_MCONT));
__attribute__((musttail)) return x4567(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x4567, CURRENT_MCONT));
__attribute__((musttail)) return x4446(std::monostate{});
}
return std::monostate{};
}
std::monostate x4567(std::monostate x4568) {
info("Entering the true branch 46 of the if");
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x4569 = Stack.pop();
SymStack.pop();
Num x4570 = I32V(Memory.loadInt(x4569.toInt(), 4));
SymVal x4571 = SymMemory.loadSym(x4569.toInt(), 4);
Stack.push(x4570);
SymStack.push(x4571);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4572 = Stack.pop();
SymVal x4573 = SymStack.pop();
Num x4574 = Stack.pop();
SymVal x4575 = SymStack.pop();
Num x4576 = x4574.i32_add(x4572);
Stack.push(x4576);
bool x4577 = allConcrete(x4575, x4573);
SymVal x4578 = x4577 ? Concrete(x4576, 32) : x4575.add(x4573);
SymStack.push(x4578);
}
{
Num x4579 = Stack.pop();
SymVal x4580 = SymStack.pop();
Frames.set(3, x4579);
SymFrames.set(3, x4580);
}
__attribute__((musttail)) return x4565(std::monostate{});
return std::monostate{};
}
std::monostate x4565(std::monostate x4566) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x4549(std::monostate{});
return std::monostate{};
}
std::monostate x4549(std::monostate x4554) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4555 = Stack.pop();
SymVal x4556 = SymStack.pop();
Num x4557 = Stack.pop();
SymVal x4558 = SymStack.pop();
Num x4559 = x4557.i32_eq(x4555);
Stack.push(x4559);
bool x4560 = allConcrete(x4558, x4556);
SymVal x4561 = x4560 ? Concrete(x4559, 32) : x4558.eq(x4556).bool2bv();
SymStack.push(x4561);
}
Num x4562 = Stack.pop();
{
SymVal x4563 = SymStack.pop();
ExploreTree.fillIfElseNode(x4563, 48);
}
int x4564 = x4562.toInt();
if (x4564 != 0) {
ExploreTree.moveCursor(true, makeControl(x4448, CURRENT_MCONT));
__attribute__((musttail)) return x4552(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x4552, CURRENT_MCONT));
__attribute__((musttail)) return x4448(std::monostate{});
}
return std::monostate{};
}
std::monostate x4552(std::monostate x4553) {
info("Entering the true branch 48 of the if");
info("Jump to 2");
__attribute__((musttail)) return x4550(std::monostate{});
return std::monostate{};
}
std::monostate x4550(std::monostate x4551) {
info("Exiting the block, stackSize =", Stack.size());
__attribute__((musttail)) return x4124(std::monostate{});
return std::monostate{};
}
std::monostate x4448(std::monostate x4449) {
info("Entering the false branch 48 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4450 = Stack.pop();
SymStack.pop();
Num x4451 = I32V(Memory.loadInt(x4450.toInt(), 0));
SymVal x4452 = SymMemory.loadSym(x4450.toInt(), 0);
Stack.push(x4451);
SymStack.push(x4452);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4453 = Stack.pop();
SymVal x4454 = SymStack.pop();
Num x4455 = Stack.pop();
SymVal x4456 = SymStack.pop();
Num x4457 = x4455.i32_sub(x4453);
Stack.push(x4457);
bool x4458 = allConcrete(x4456, x4454);
SymVal x4459 = x4458 ? Concrete(x4457, 32) : x4456.minus(x4454);
SymStack.push(x4459);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4460 = Stack.pop();
SymVal x4461 = SymStack.pop();
Num x4462 = Stack.pop();
SymVal x4463 = SymStack.pop();
Num x4464 = x4462.i32_mul(x4460);
Stack.push(x4464);
bool x4465 = allConcrete(x4463, x4461);
SymVal x4466 = x4465 ? Concrete(x4464, 32) : x4463.mul(x4461);
SymStack.push(x4466);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4467 = Stack.pop();
SymVal x4468 = SymStack.pop();
Num x4469 = Stack.pop();
SymVal x4470 = SymStack.pop();
Num x4471 = x4469.i32_mul(x4467);
Stack.push(x4471);
bool x4472 = allConcrete(x4470, x4468);
SymVal x4473 = x4472 ? Concrete(x4471, 32) : x4470.mul(x4468);
SymStack.push(x4473);
}
{
Num x4474 = Stack.pop();
SymVal x4475 = SymStack.pop();
Num x4476 = Stack.pop();
SymVal x4477 = SymStack.pop();
Num x4478 = x4476.i32_add(x4474);
Stack.push(x4478);
bool x4479 = allConcrete(x4477, x4475);
SymVal x4480 = x4479 ? Concrete(x4478, 32) : x4477.add(x4475);
SymStack.push(x4480);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x4481 = Stack.pop();
SymVal x4482 = SymStack.pop();
Num x4483 = Stack.pop();
SymVal x4484 = SymStack.pop();
Num x4485 = x4483.i32_add(x4481);
Stack.push(x4485);
bool x4486 = allConcrete(x4484, x4482);
SymVal x4487 = x4486 ? Concrete(x4485, 32) : x4484.add(x4482);
SymStack.push(x4487);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4488 = Stack.pop();
SymStack.pop();
Num x4489 = I32V(Memory.loadInt(x4488.toInt(), 0));
SymVal x4490 = SymMemory.loadSym(x4488.toInt(), 0);
Stack.push(x4489);
SymStack.push(x4490);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4491 = Stack.pop();
SymVal x4492 = SymStack.pop();
Num x4493 = Stack.pop();
SymVal x4494 = SymStack.pop();
Num x4495 = x4493.i32_sub(x4491);
Stack.push(x4495);
bool x4496 = allConcrete(x4494, x4492);
SymVal x4497 = x4496 ? Concrete(x4495, 32) : x4494.minus(x4492);
SymStack.push(x4497);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4498 = Stack.pop();
SymVal x4499 = SymStack.pop();
Num x4500 = Stack.pop();
SymVal x4501 = SymStack.pop();
Num x4502 = x4500.i32_mul(x4498);
Stack.push(x4502);
bool x4503 = allConcrete(x4501, x4499);
SymVal x4504 = x4503 ? Concrete(x4502, 32) : x4501.mul(x4499);
SymStack.push(x4504);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4505 = Stack.pop();
SymVal x4506 = SymStack.pop();
Num x4507 = Stack.pop();
SymVal x4508 = SymStack.pop();
Num x4509 = x4507.i32_sub(x4505);
Stack.push(x4509);
bool x4510 = allConcrete(x4508, x4506);
SymVal x4511 = x4510 ? Concrete(x4509, 32) : x4508.minus(x4506);
SymStack.push(x4511);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4512 = Stack.pop();
SymVal x4513 = SymStack.pop();
Num x4514 = Stack.pop();
SymVal x4515 = SymStack.pop();
Num x4516 = x4514.i32_mul(x4512);
Stack.push(x4516);
bool x4517 = allConcrete(x4515, x4513);
SymVal x4518 = x4517 ? Concrete(x4516, 32) : x4515.mul(x4513);
SymStack.push(x4518);
}
{
Num x4519 = Stack.pop();
SymVal x4520 = SymStack.pop();
Num x4521 = Stack.pop();
SymVal x4522 = SymStack.pop();
Num x4523 = x4521.i32_add(x4519);
Stack.push(x4523);
bool x4524 = allConcrete(x4522, x4520);
SymVal x4525 = x4524 ? Concrete(x4523, 32) : x4522.add(x4520);
SymStack.push(x4525);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x4526 = Stack.pop();
SymVal x4527 = SymStack.pop();
Num x4528 = Stack.pop();
SymVal x4529 = SymStack.pop();
Num x4530 = x4528.i32_add(x4526);
Stack.push(x4530);
bool x4531 = allConcrete(x4529, x4527);
SymVal x4532 = x4531 ? Concrete(x4530, 32) : x4529.add(x4527);
SymStack.push(x4532);
}
{
Num x4533 = Stack.pop();
SymStack.pop();
Num x4534 = I32V(Memory.loadInt(x4533.toInt(), 8));
SymVal x4535 = SymMemory.loadSym(x4533.toInt(), 8);
Stack.push(x4534);
SymStack.push(x4535);
}
{
Num x4536 = Stack.pop();
SymVal x4537 = SymStack.pop();
Num x4538 = Stack.pop();
SymStack.pop();
int x4539 = x4538.toInt();
Memory.storeInt(x4539, 8, x4536.toInt());
SymMemory.storeSym(x4539, 8, x4537);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4540 = Stack.pop();
SymVal x4541 = SymStack.pop();
Num x4542 = Stack.pop();
SymVal x4543 = SymStack.pop();
Num x4544 = x4542.i32_sub(x4540);
Stack.push(x4544);
bool x4545 = allConcrete(x4543, x4541);
SymVal x4546 = x4545 ? Concrete(x4544, 32) : x4543.minus(x4541);
SymStack.push(x4546);
}
{
Num x4547 = Stack.pop();
SymVal x4548 = SymStack.pop();
Frames.set(3, x4547);
SymFrames.set(3, x4548);
}
info("Jump to 1");
__attribute__((musttail)) return x4549(std::monostate{});
return std::monostate{};
}
std::monostate x4446(std::monostate x4447) {
info("Entering the false branch 46 of the if");
__attribute__((musttail)) return x4124(std::monostate{});
return std::monostate{};
}
std::monostate x4124(std::monostate x4125) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x4126 = Stack.pop();
SymStack.pop();
Num x4127 = I32V(Memory.loadInt(x4126.toInt(), 4));
SymVal x4128 = SymMemory.loadSym(x4126.toInt(), 4);
Stack.push(x4127);
SymStack.push(x4128);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4129 = Stack.pop();
SymVal x4130 = SymStack.pop();
Num x4131 = Stack.pop();
SymVal x4132 = SymStack.pop();
Num x4133 = x4131.i32_add(x4129);
Stack.push(x4133);
bool x4134 = allConcrete(x4132, x4130);
SymVal x4135 = x4134 ? Concrete(x4133, 32) : x4132.add(x4130);
SymStack.push(x4135);
}
{
Num x4136 = Stack.pop();
SymVal x4137 = SymStack.pop();
Num x4138 = Stack.pop();
SymStack.pop();
int x4139 = x4138.toInt();
Memory.storeInt(x4139, 4, x4136.toInt());
SymMemory.storeSym(x4139, 4, x4137);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4140 = Stack.pop();
SymVal x4141 = SymStack.pop();
Num x4142 = Stack.pop();
SymVal x4143 = SymStack.pop();
Num x4144 = x4142.i32_mul(x4140);
Stack.push(x4144);
bool x4145 = allConcrete(x4143, x4141);
SymVal x4146 = x4145 ? Concrete(x4144, 32) : x4143.mul(x4141);
SymStack.push(x4146);
}
{
Num x4147 = Stack.pop();
SymVal x4148 = SymStack.pop();
Num x4149 = Stack.pop();
SymVal x4150 = SymStack.pop();
Num x4151 = x4149.i32_add(x4147);
Stack.push(x4151);
bool x4152 = allConcrete(x4150, x4148);
SymVal x4153 = x4152 ? Concrete(x4151, 32) : x4150.add(x4148);
SymStack.push(x4153);
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
Num x4154 = Stack.pop();
SymVal x4155 = SymStack.pop();
Num x4156 = Stack.pop();
SymVal x4157 = SymStack.pop();
Num x4158 = x4156.i32_sub(x4154);
Stack.push(x4158);
bool x4159 = allConcrete(x4157, x4155);
SymVal x4160 = x4159 ? Concrete(x4158, 32) : x4157.minus(x4155);
SymStack.push(x4160);
}
{
Num x4161 = Stack.pop();
SymVal x4162 = SymStack.pop();
Num x4163 = Stack.pop();
SymVal x4164 = SymStack.pop();
Num x4165 = x4163.i32_mul(x4161);
Stack.push(x4165);
bool x4166 = allConcrete(x4164, x4162);
SymVal x4167 = x4166 ? Concrete(x4165, 32) : x4164.mul(x4162);
SymStack.push(x4167);
}
{
Num x4168 = Stack.pop();
SymVal x4169 = SymStack.pop();
Num x4170 = Stack.pop();
SymVal x4171 = SymStack.pop();
Num x4172 = x4170.i32_add(x4168);
Stack.push(x4172);
bool x4173 = allConcrete(x4171, x4169);
SymVal x4174 = x4173 ? Concrete(x4172, 32) : x4171.add(x4169);
SymStack.push(x4174);
}
{
Num x4175 = Stack.pop();
SymStack.pop();
Num x4176 = I32V(Memory.loadInt(x4175.toInt(), 8));
SymVal x4177 = SymMemory.loadSym(x4175.toInt(), 8);
Stack.push(x4176);
SymStack.push(x4177);
}
{
Num x4178 = Stack.pop();
SymVal x4179 = SymStack.pop();
Num x4180 = Stack.pop();
SymStack.pop();
int x4181 = x4180.toInt();
Memory.storeInt(x4181, 8, x4178.toInt());
SymMemory.storeSym(x4181, 8, x4179);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4182 = Stack.pop();
SymStack.pop();
Num x4183 = I32V(Memory.loadInt(x4182.toInt(), 0));
SymVal x4184 = SymMemory.loadSym(x4182.toInt(), 0);
Stack.push(x4183);
SymStack.push(x4184);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4185 = Stack.pop();
SymVal x4186 = SymStack.pop();
Num x4187 = Stack.pop();
SymVal x4188 = SymStack.pop();
Num x4189 = x4187.i32_sub(x4185);
Stack.push(x4189);
bool x4190 = allConcrete(x4188, x4186);
SymVal x4191 = x4190 ? Concrete(x4189, 32) : x4188.minus(x4186);
SymStack.push(x4191);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4192 = Stack.pop();
SymVal x4193 = SymStack.pop();
Num x4194 = Stack.pop();
SymVal x4195 = SymStack.pop();
Num x4196 = x4194.i32_mul(x4192);
Stack.push(x4196);
bool x4197 = allConcrete(x4195, x4193);
SymVal x4198 = x4197 ? Concrete(x4196, 32) : x4195.mul(x4193);
SymStack.push(x4198);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4199 = Stack.pop();
SymVal x4200 = SymStack.pop();
Num x4201 = Stack.pop();
SymVal x4202 = SymStack.pop();
Num x4203 = x4201.i32_sub(x4199);
Stack.push(x4203);
bool x4204 = allConcrete(x4202, x4200);
SymVal x4205 = x4204 ? Concrete(x4203, 32) : x4202.minus(x4200);
SymStack.push(x4205);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4206 = Stack.pop();
SymVal x4207 = SymStack.pop();
Num x4208 = Stack.pop();
SymVal x4209 = SymStack.pop();
Num x4210 = x4208.i32_mul(x4206);
Stack.push(x4210);
bool x4211 = allConcrete(x4209, x4207);
SymVal x4212 = x4211 ? Concrete(x4210, 32) : x4209.mul(x4207);
SymStack.push(x4212);
}
{
Num x4213 = Stack.pop();
SymVal x4214 = SymStack.pop();
Num x4215 = Stack.pop();
SymVal x4216 = SymStack.pop();
Num x4217 = x4215.i32_add(x4213);
Stack.push(x4217);
bool x4218 = allConcrete(x4216, x4214);
SymVal x4219 = x4218 ? Concrete(x4217, 32) : x4216.add(x4214);
SymStack.push(x4219);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x4220 = Stack.pop();
SymVal x4221 = SymStack.pop();
Num x4222 = Stack.pop();
SymVal x4223 = SymStack.pop();
Num x4224 = x4222.i32_add(x4220);
Stack.push(x4224);
bool x4225 = allConcrete(x4223, x4221);
SymVal x4226 = x4225 ? Concrete(x4224, 32) : x4223.add(x4221);
SymStack.push(x4226);
}
{
Num x4227 = Stack.pop();
SymStack.pop();
Num x4228 = I32V(Memory.loadInt(x4227.toInt(), 8));
SymVal x4229 = SymMemory.loadSym(x4227.toInt(), 8);
Stack.push(x4228);
SymStack.push(x4229);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4230 = Stack.pop();
SymStack.pop();
Num x4231 = I32V(Memory.loadInt(x4230.toInt(), 0));
SymVal x4232 = SymMemory.loadSym(x4230.toInt(), 0);
Stack.push(x4231);
SymStack.push(x4232);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4233 = Stack.pop();
SymVal x4234 = SymStack.pop();
Num x4235 = Stack.pop();
SymVal x4236 = SymStack.pop();
Num x4237 = x4235.i32_sub(x4233);
Stack.push(x4237);
bool x4238 = allConcrete(x4236, x4234);
SymVal x4239 = x4238 ? Concrete(x4237, 32) : x4236.minus(x4234);
SymStack.push(x4239);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4240 = Stack.pop();
SymVal x4241 = SymStack.pop();
Num x4242 = Stack.pop();
SymVal x4243 = SymStack.pop();
Num x4244 = x4242.i32_mul(x4240);
Stack.push(x4244);
bool x4245 = allConcrete(x4243, x4241);
SymVal x4246 = x4245 ? Concrete(x4244, 32) : x4243.mul(x4241);
SymStack.push(x4246);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4247 = Stack.pop();
SymVal x4248 = SymStack.pop();
Num x4249 = Stack.pop();
SymVal x4250 = SymStack.pop();
Num x4251 = x4249.i32_sub(x4247);
Stack.push(x4251);
bool x4252 = allConcrete(x4250, x4248);
SymVal x4253 = x4252 ? Concrete(x4251, 32) : x4250.minus(x4248);
SymStack.push(x4253);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4254 = Stack.pop();
SymVal x4255 = SymStack.pop();
Num x4256 = Stack.pop();
SymVal x4257 = SymStack.pop();
Num x4258 = x4256.i32_mul(x4254);
Stack.push(x4258);
bool x4259 = allConcrete(x4257, x4255);
SymVal x4260 = x4259 ? Concrete(x4258, 32) : x4257.mul(x4255);
SymStack.push(x4260);
}
{
Num x4261 = Stack.pop();
SymVal x4262 = SymStack.pop();
Num x4263 = Stack.pop();
SymVal x4264 = SymStack.pop();
Num x4265 = x4263.i32_add(x4261);
Stack.push(x4265);
bool x4266 = allConcrete(x4264, x4262);
SymVal x4267 = x4266 ? Concrete(x4265, 32) : x4264.add(x4262);
SymStack.push(x4267);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x4268 = Stack.pop();
SymVal x4269 = SymStack.pop();
Num x4270 = Stack.pop();
SymVal x4271 = SymStack.pop();
Num x4272 = x4270.i32_add(x4268);
Stack.push(x4272);
bool x4273 = allConcrete(x4271, x4269);
SymVal x4274 = x4273 ? Concrete(x4272, 32) : x4271.add(x4269);
SymStack.push(x4274);
}
{
Num x4275 = Stack.pop();
SymStack.pop();
Num x4276 = I32V(Memory.loadInt(x4275.toInt(), 8));
SymVal x4277 = SymMemory.loadSym(x4275.toInt(), 8);
Stack.push(x4276);
SymStack.push(x4277);
}
{
Num x4278 = Stack.pop();
SymStack.pop();
Num x4279 = I32V(Memory.loadInt(x4278.toInt(), 4));
SymVal x4280 = SymMemory.loadSym(x4278.toInt(), 4);
Stack.push(x4279);
SymStack.push(x4280);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4281 = Stack.pop();
SymVal x4282 = SymStack.pop();
Num x4283 = Stack.pop();
SymVal x4284 = SymStack.pop();
Num x4285 = x4283.i32_sub(x4281);
Stack.push(x4285);
bool x4286 = allConcrete(x4284, x4282);
SymVal x4287 = x4286 ? Concrete(x4285, 32) : x4284.minus(x4282);
SymStack.push(x4287);
}
{
Num x4288 = Stack.pop();
SymVal x4289 = SymStack.pop();
Num x4290 = Stack.pop();
SymStack.pop();
int x4291 = x4290.toInt();
Memory.storeInt(x4291, 4, x4288.toInt());
SymMemory.storeSym(x4291, 4, x4289);
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
Num x4292 = Stack.pop();
SymVal x4293 = SymStack.pop();
Num x4294 = Stack.pop();
SymVal x4295 = SymStack.pop();
Num x4296 = x4294.i32_sub(x4292);
Stack.push(x4296);
bool x4297 = allConcrete(x4295, x4293);
SymVal x4298 = x4297 ? Concrete(x4296, 32) : x4295.minus(x4293);
SymStack.push(x4298);
}
{
Num x4299 = Stack.pop();
SymVal x4300 = SymStack.pop();
Num x4301 = Stack.pop();
SymVal x4302 = SymStack.pop();
Num x4303 = x4301.i32_mul(x4299);
Stack.push(x4303);
bool x4304 = allConcrete(x4302, x4300);
SymVal x4305 = x4304 ? Concrete(x4303, 32) : x4302.mul(x4300);
SymStack.push(x4305);
}
{
Num x4306 = Stack.pop();
SymVal x4307 = SymStack.pop();
Num x4308 = Stack.pop();
SymVal x4309 = SymStack.pop();
Num x4310 = x4308.i32_add(x4306);
Stack.push(x4310);
bool x4311 = allConcrete(x4309, x4307);
SymVal x4312 = x4311 ? Concrete(x4310, 32) : x4309.add(x4307);
SymStack.push(x4312);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4313 = Stack.pop();
SymStack.pop();
Num x4314 = I32V(Memory.loadInt(x4313.toInt(), 0));
SymVal x4315 = SymMemory.loadSym(x4313.toInt(), 0);
Stack.push(x4314);
SymStack.push(x4315);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4316 = Stack.pop();
SymVal x4317 = SymStack.pop();
Num x4318 = Stack.pop();
SymVal x4319 = SymStack.pop();
Num x4320 = x4318.i32_sub(x4316);
Stack.push(x4320);
bool x4321 = allConcrete(x4319, x4317);
SymVal x4322 = x4321 ? Concrete(x4320, 32) : x4319.minus(x4317);
SymStack.push(x4322);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4323 = Stack.pop();
SymVal x4324 = SymStack.pop();
Num x4325 = Stack.pop();
SymVal x4326 = SymStack.pop();
Num x4327 = x4325.i32_mul(x4323);
Stack.push(x4327);
bool x4328 = allConcrete(x4326, x4324);
SymVal x4329 = x4328 ? Concrete(x4327, 32) : x4326.mul(x4324);
SymStack.push(x4329);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4330 = Stack.pop();
SymVal x4331 = SymStack.pop();
Num x4332 = Stack.pop();
SymVal x4333 = SymStack.pop();
Num x4334 = x4332.i32_sub(x4330);
Stack.push(x4334);
bool x4335 = allConcrete(x4333, x4331);
SymVal x4336 = x4335 ? Concrete(x4334, 32) : x4333.minus(x4331);
SymStack.push(x4336);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4337 = Stack.pop();
SymVal x4338 = SymStack.pop();
Num x4339 = Stack.pop();
SymVal x4340 = SymStack.pop();
Num x4341 = x4339.i32_mul(x4337);
Stack.push(x4341);
bool x4342 = allConcrete(x4340, x4338);
SymVal x4343 = x4342 ? Concrete(x4341, 32) : x4340.mul(x4338);
SymStack.push(x4343);
}
{
Num x4344 = Stack.pop();
SymVal x4345 = SymStack.pop();
Num x4346 = Stack.pop();
SymVal x4347 = SymStack.pop();
Num x4348 = x4346.i32_add(x4344);
Stack.push(x4348);
bool x4349 = allConcrete(x4347, x4345);
SymVal x4350 = x4349 ? Concrete(x4348, 32) : x4347.add(x4345);
SymStack.push(x4350);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x4351 = Stack.pop();
SymVal x4352 = SymStack.pop();
Num x4353 = Stack.pop();
SymVal x4354 = SymStack.pop();
Num x4355 = x4353.i32_add(x4351);
Stack.push(x4355);
bool x4356 = allConcrete(x4354, x4352);
SymVal x4357 = x4356 ? Concrete(x4355, 32) : x4354.add(x4352);
SymStack.push(x4357);
}
{
Num x4358 = Stack.pop();
SymStack.pop();
Num x4359 = I32V(Memory.loadInt(x4358.toInt(), 8));
SymVal x4360 = SymMemory.loadSym(x4358.toInt(), 8);
Stack.push(x4359);
SymStack.push(x4360);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4361 = Stack.pop();
SymStack.pop();
Num x4362 = I32V(Memory.loadInt(x4361.toInt(), 0));
SymVal x4363 = SymMemory.loadSym(x4361.toInt(), 0);
Stack.push(x4362);
SymStack.push(x4363);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4364 = Stack.pop();
SymVal x4365 = SymStack.pop();
Num x4366 = Stack.pop();
SymVal x4367 = SymStack.pop();
Num x4368 = x4366.i32_sub(x4364);
Stack.push(x4368);
bool x4369 = allConcrete(x4367, x4365);
SymVal x4370 = x4369 ? Concrete(x4368, 32) : x4367.minus(x4365);
SymStack.push(x4370);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4371 = Stack.pop();
SymVal x4372 = SymStack.pop();
Num x4373 = Stack.pop();
SymVal x4374 = SymStack.pop();
Num x4375 = x4373.i32_mul(x4371);
Stack.push(x4375);
bool x4376 = allConcrete(x4374, x4372);
SymVal x4377 = x4376 ? Concrete(x4375, 32) : x4374.mul(x4372);
SymStack.push(x4377);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4378 = Stack.pop();
SymVal x4379 = SymStack.pop();
Num x4380 = Stack.pop();
SymVal x4381 = SymStack.pop();
Num x4382 = x4380.i32_sub(x4378);
Stack.push(x4382);
bool x4383 = allConcrete(x4381, x4379);
SymVal x4384 = x4383 ? Concrete(x4382, 32) : x4381.minus(x4379);
SymStack.push(x4384);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4385 = Stack.pop();
SymVal x4386 = SymStack.pop();
Num x4387 = Stack.pop();
SymVal x4388 = SymStack.pop();
Num x4389 = x4387.i32_mul(x4385);
Stack.push(x4389);
bool x4390 = allConcrete(x4388, x4386);
SymVal x4391 = x4390 ? Concrete(x4389, 32) : x4388.mul(x4386);
SymStack.push(x4391);
}
{
Num x4392 = Stack.pop();
SymVal x4393 = SymStack.pop();
Num x4394 = Stack.pop();
SymVal x4395 = SymStack.pop();
Num x4396 = x4394.i32_add(x4392);
Stack.push(x4396);
bool x4397 = allConcrete(x4395, x4393);
SymVal x4398 = x4397 ? Concrete(x4396, 32) : x4395.add(x4393);
SymStack.push(x4398);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x4399 = Stack.pop();
SymVal x4400 = SymStack.pop();
Num x4401 = Stack.pop();
SymVal x4402 = SymStack.pop();
Num x4403 = x4401.i32_add(x4399);
Stack.push(x4403);
bool x4404 = allConcrete(x4402, x4400);
SymVal x4405 = x4404 ? Concrete(x4403, 32) : x4402.add(x4400);
SymStack.push(x4405);
}
{
Num x4406 = Stack.pop();
SymStack.pop();
Num x4407 = I32V(Memory.loadInt(x4406.toInt(), 8));
SymVal x4408 = SymMemory.loadSym(x4406.toInt(), 8);
Stack.push(x4407);
SymStack.push(x4408);
}
{
Num x4409 = Stack.pop();
SymStack.pop();
Num x4410 = I32V(Memory.loadInt(x4409.toInt(), 4));
SymVal x4411 = SymMemory.loadSym(x4409.toInt(), 4);
Stack.push(x4410);
SymStack.push(x4411);
}
{
Num x4412 = Stack.pop();
SymVal x4413 = SymStack.pop();
Num x4414 = Stack.pop();
SymVal x4415 = SymStack.pop();
Num x4416 = x4414.i32_mul(x4412);
Stack.push(x4416);
bool x4417 = allConcrete(x4415, x4413);
SymVal x4418 = x4417 ? Concrete(x4416, 32) : x4415.mul(x4413);
SymStack.push(x4418);
}
{
Num x4419 = Stack.pop();
SymVal x4420 = SymStack.pop();
Num x4421 = Stack.pop();
SymVal x4422 = SymStack.pop();
Num x4423 = x4421.i32_add(x4419);
Stack.push(x4423);
bool x4424 = allConcrete(x4422, x4420);
SymVal x4425 = x4424 ? Concrete(x4423, 32) : x4422.add(x4420);
SymStack.push(x4425);
}
{
Num x4426 = Stack.pop();
SymStack.pop();
Num x4427 = I32V(Memory.loadInt(x4426.toInt(), 8));
SymVal x4428 = SymMemory.loadSym(x4426.toInt(), 8);
Stack.push(x4427);
SymStack.push(x4428);
}
{
Num x4429 = Stack.pop();
SymVal x4430 = SymStack.pop();
Num x4431 = Stack.pop();
SymStack.pop();
int x4432 = x4431.toInt();
Memory.storeInt(x4432, 8, x4429.toInt());
SymMemory.storeSym(x4432, 8, x4430);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x4433 = Stack.pop();
SymStack.pop();
Num x4434 = I32V(Memory.loadInt(x4433.toInt(), 0));
SymVal x4435 = SymMemory.loadSym(x4433.toInt(), 0);
Stack.push(x4434);
SymStack.push(x4435);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4436 = Stack.pop();
SymVal x4437 = SymStack.pop();
Num x4438 = Stack.pop();
SymVal x4439 = SymStack.pop();
Num x4440 = x4438.i32_ne(x4436);
Stack.push(x4440);
bool x4441 = allConcrete(x4439, x4437);
SymVal x4442 = x4441 ? Concrete(x4440, 32) : x4439.neq(x4437).bool2bv();
SymStack.push(x4442);
}
Num x4443 = Stack.pop();
{
SymVal x4444 = SymStack.pop();
ExploreTree.fillIfElseNode(x4444, 47);
}
int x4445 = x4443.toInt();
if (x4445 != 0) {
ExploreTree.moveCursor(true, makeControl(x3931, CURRENT_MCONT));
__attribute__((musttail)) return x3933(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x3933, CURRENT_MCONT));
__attribute__((musttail)) return x3931(std::monostate{});
}
return std::monostate{};
}
std::monostate x3933(std::monostate x3934) {
info("Entering the true branch 47 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3935 = Stack.pop();
SymStack.pop();
Num x3936 = I32V(Memory.loadInt(x3935.toInt(), 0));
SymVal x3937 = SymMemory.loadSym(x3935.toInt(), 0);
Stack.push(x3936);
SymStack.push(x3937);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3938 = Stack.pop();
SymVal x3939 = SymStack.pop();
Num x3940 = Stack.pop();
SymVal x3941 = SymStack.pop();
Num x3942 = x3940.i32_sub(x3938);
Stack.push(x3942);
bool x3943 = allConcrete(x3941, x3939);
SymVal x3944 = x3943 ? Concrete(x3942, 32) : x3941.minus(x3939);
SymStack.push(x3944);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3945 = Stack.pop();
SymVal x3946 = SymStack.pop();
Num x3947 = Stack.pop();
SymVal x3948 = SymStack.pop();
Num x3949 = x3947.i32_mul(x3945);
Stack.push(x3949);
bool x3950 = allConcrete(x3948, x3946);
SymVal x3951 = x3950 ? Concrete(x3949, 32) : x3948.mul(x3946);
SymStack.push(x3951);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3952 = Stack.pop();
SymVal x3953 = SymStack.pop();
Num x3954 = Stack.pop();
SymVal x3955 = SymStack.pop();
Num x3956 = x3954.i32_mul(x3952);
Stack.push(x3956);
bool x3957 = allConcrete(x3955, x3953);
SymVal x3958 = x3957 ? Concrete(x3956, 32) : x3955.mul(x3953);
SymStack.push(x3958);
}
{
Num x3959 = Stack.pop();
SymVal x3960 = SymStack.pop();
Num x3961 = Stack.pop();
SymVal x3962 = SymStack.pop();
Num x3963 = x3961.i32_add(x3959);
Stack.push(x3963);
bool x3964 = allConcrete(x3962, x3960);
SymVal x3965 = x3964 ? Concrete(x3963, 32) : x3962.add(x3960);
SymStack.push(x3965);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x3966 = Stack.pop();
SymVal x3967 = SymStack.pop();
Num x3968 = Stack.pop();
SymVal x3969 = SymStack.pop();
Num x3970 = x3968.i32_add(x3966);
Stack.push(x3970);
bool x3971 = allConcrete(x3969, x3967);
SymVal x3972 = x3971 ? Concrete(x3970, 32) : x3969.add(x3967);
SymStack.push(x3972);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3973 = Stack.pop();
SymStack.pop();
Num x3974 = I32V(Memory.loadInt(x3973.toInt(), 0));
SymVal x3975 = SymMemory.loadSym(x3973.toInt(), 0);
Stack.push(x3974);
SymStack.push(x3975);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3976 = Stack.pop();
SymVal x3977 = SymStack.pop();
Num x3978 = Stack.pop();
SymVal x3979 = SymStack.pop();
Num x3980 = x3978.i32_sub(x3976);
Stack.push(x3980);
bool x3981 = allConcrete(x3979, x3977);
SymVal x3982 = x3981 ? Concrete(x3980, 32) : x3979.minus(x3977);
SymStack.push(x3982);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3983 = Stack.pop();
SymVal x3984 = SymStack.pop();
Num x3985 = Stack.pop();
SymVal x3986 = SymStack.pop();
Num x3987 = x3985.i32_mul(x3983);
Stack.push(x3987);
bool x3988 = allConcrete(x3986, x3984);
SymVal x3989 = x3988 ? Concrete(x3987, 32) : x3986.mul(x3984);
SymStack.push(x3989);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3990 = Stack.pop();
SymStack.pop();
Num x3991 = I32V(Memory.loadInt(x3990.toInt(), 0));
SymVal x3992 = SymMemory.loadSym(x3990.toInt(), 0);
Stack.push(x3991);
SymStack.push(x3992);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3993 = Stack.pop();
SymVal x3994 = SymStack.pop();
Num x3995 = Stack.pop();
SymVal x3996 = SymStack.pop();
Num x3997 = x3995.i32_sub(x3993);
Stack.push(x3997);
bool x3998 = allConcrete(x3996, x3994);
SymVal x3999 = x3998 ? Concrete(x3997, 32) : x3996.minus(x3994);
SymStack.push(x3999);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4000 = Stack.pop();
SymVal x4001 = SymStack.pop();
Num x4002 = Stack.pop();
SymVal x4003 = SymStack.pop();
Num x4004 = x4002.i32_mul(x4000);
Stack.push(x4004);
bool x4005 = allConcrete(x4003, x4001);
SymVal x4006 = x4005 ? Concrete(x4004, 32) : x4003.mul(x4001);
SymStack.push(x4006);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4007 = Stack.pop();
SymVal x4008 = SymStack.pop();
Num x4009 = Stack.pop();
SymVal x4010 = SymStack.pop();
Num x4011 = x4009.i32_sub(x4007);
Stack.push(x4011);
bool x4012 = allConcrete(x4010, x4008);
SymVal x4013 = x4012 ? Concrete(x4011, 32) : x4010.minus(x4008);
SymStack.push(x4013);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4014 = Stack.pop();
SymVal x4015 = SymStack.pop();
Num x4016 = Stack.pop();
SymVal x4017 = SymStack.pop();
Num x4018 = x4016.i32_mul(x4014);
Stack.push(x4018);
bool x4019 = allConcrete(x4017, x4015);
SymVal x4020 = x4019 ? Concrete(x4018, 32) : x4017.mul(x4015);
SymStack.push(x4020);
}
{
Num x4021 = Stack.pop();
SymVal x4022 = SymStack.pop();
Num x4023 = Stack.pop();
SymVal x4024 = SymStack.pop();
Num x4025 = x4023.i32_add(x4021);
Stack.push(x4025);
bool x4026 = allConcrete(x4024, x4022);
SymVal x4027 = x4026 ? Concrete(x4025, 32) : x4024.add(x4022);
SymStack.push(x4027);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x4028 = Stack.pop();
SymVal x4029 = SymStack.pop();
Num x4030 = Stack.pop();
SymVal x4031 = SymStack.pop();
Num x4032 = x4030.i32_add(x4028);
Stack.push(x4032);
bool x4033 = allConcrete(x4031, x4029);
SymVal x4034 = x4033 ? Concrete(x4032, 32) : x4031.add(x4029);
SymStack.push(x4034);
}
{
Num x4035 = Stack.pop();
SymStack.pop();
Num x4036 = I32V(Memory.loadInt(x4035.toInt(), 8));
SymVal x4037 = SymMemory.loadSym(x4035.toInt(), 8);
Stack.push(x4036);
SymStack.push(x4037);
}
{
Num x4038 = Stack.pop();
SymStack.pop();
Num x4039 = I32V(Memory.loadInt(x4038.toInt(), 4));
SymVal x4040 = SymMemory.loadSym(x4038.toInt(), 4);
Stack.push(x4039);
SymStack.push(x4040);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4041 = Stack.pop();
SymVal x4042 = SymStack.pop();
Num x4043 = Stack.pop();
SymVal x4044 = SymStack.pop();
Num x4045 = x4043.i32_add(x4041);
Stack.push(x4045);
bool x4046 = allConcrete(x4044, x4042);
SymVal x4047 = x4046 ? Concrete(x4045, 32) : x4044.add(x4042);
SymStack.push(x4047);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4048 = Stack.pop();
SymVal x4049 = SymStack.pop();
Num x4050 = Stack.pop();
SymVal x4051 = SymStack.pop();
Num x4052 = x4050.i32_mul(x4048);
Stack.push(x4052);
bool x4053 = allConcrete(x4051, x4049);
SymVal x4054 = x4053 ? Concrete(x4052, 32) : x4051.mul(x4049);
SymStack.push(x4054);
}
{
Num x4055 = Stack.pop();
SymVal x4056 = SymStack.pop();
Num x4057 = Stack.pop();
SymVal x4058 = SymStack.pop();
Num x4059 = x4057.i32_add(x4055);
Stack.push(x4059);
bool x4060 = allConcrete(x4058, x4056);
SymVal x4061 = x4060 ? Concrete(x4059, 32) : x4058.add(x4056);
SymStack.push(x4061);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x4062 = Stack.pop();
SymStack.pop();
Num x4063 = I32V(Memory.loadInt(x4062.toInt(), 0));
SymVal x4064 = SymMemory.loadSym(x4062.toInt(), 0);
Stack.push(x4063);
SymStack.push(x4064);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4065 = Stack.pop();
SymVal x4066 = SymStack.pop();
Num x4067 = Stack.pop();
SymVal x4068 = SymStack.pop();
Num x4069 = x4067.i32_sub(x4065);
Stack.push(x4069);
bool x4070 = allConcrete(x4068, x4066);
SymVal x4071 = x4070 ? Concrete(x4069, 32) : x4068.minus(x4066);
SymStack.push(x4071);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4072 = Stack.pop();
SymVal x4073 = SymStack.pop();
Num x4074 = Stack.pop();
SymVal x4075 = SymStack.pop();
Num x4076 = x4074.i32_mul(x4072);
Stack.push(x4076);
bool x4077 = allConcrete(x4075, x4073);
SymVal x4078 = x4077 ? Concrete(x4076, 32) : x4075.mul(x4073);
SymStack.push(x4078);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x4079 = Stack.pop();
SymVal x4080 = SymStack.pop();
Num x4081 = Stack.pop();
SymVal x4082 = SymStack.pop();
Num x4083 = x4081.i32_sub(x4079);
Stack.push(x4083);
bool x4084 = allConcrete(x4082, x4080);
SymVal x4085 = x4084 ? Concrete(x4083, 32) : x4082.minus(x4080);
SymStack.push(x4085);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x4086 = Stack.pop();
SymVal x4087 = SymStack.pop();
Num x4088 = Stack.pop();
SymVal x4089 = SymStack.pop();
Num x4090 = x4088.i32_mul(x4086);
Stack.push(x4090);
bool x4091 = allConcrete(x4089, x4087);
SymVal x4092 = x4091 ? Concrete(x4090, 32) : x4089.mul(x4087);
SymStack.push(x4092);
}
{
Num x4093 = Stack.pop();
SymVal x4094 = SymStack.pop();
Num x4095 = Stack.pop();
SymVal x4096 = SymStack.pop();
Num x4097 = x4095.i32_add(x4093);
Stack.push(x4097);
bool x4098 = allConcrete(x4096, x4094);
SymVal x4099 = x4098 ? Concrete(x4097, 32) : x4096.add(x4094);
SymStack.push(x4099);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x4100 = Stack.pop();
SymVal x4101 = SymStack.pop();
Num x4102 = Stack.pop();
SymVal x4103 = SymStack.pop();
Num x4104 = x4102.i32_add(x4100);
Stack.push(x4104);
bool x4105 = allConcrete(x4103, x4101);
SymVal x4106 = x4105 ? Concrete(x4104, 32) : x4103.add(x4101);
SymStack.push(x4106);
}
{
Num x4107 = Stack.pop();
SymStack.pop();
Num x4108 = I32V(Memory.loadInt(x4107.toInt(), 8));
SymVal x4109 = SymMemory.loadSym(x4107.toInt(), 8);
Stack.push(x4108);
SymStack.push(x4109);
}
{
Num x4110 = Stack.pop();
SymVal x4111 = SymStack.pop();
Num x4112 = Stack.pop();
SymVal x4113 = SymStack.pop();
Num x4114 = x4112.i32_add(x4110);
Stack.push(x4114);
bool x4115 = allConcrete(x4113, x4111);
SymVal x4116 = x4115 ? Concrete(x4114, 32) : x4113.add(x4111);
SymStack.push(x4116);
}
{
Num x4117 = Stack.pop();
SymStack.pop();
Num x4118 = I32V(Memory.loadInt(x4117.toInt(), 8));
SymVal x4119 = SymMemory.loadSym(x4117.toInt(), 8);
Stack.push(x4118);
SymStack.push(x4119);
}
{
Num x4120 = Stack.pop();
SymVal x4121 = SymStack.pop();
Num x4122 = Stack.pop();
SymStack.pop();
int x4123 = x4122.toInt();
Memory.storeInt(x4123, 8, x4120.toInt());
SymMemory.storeSym(x4123, 8, x4121);
}
__attribute__((musttail)) return x3927(std::monostate{});
return std::monostate{};
}
std::monostate x3931(std::monostate x3932) {
info("Entering the false branch 47 of the if");
__attribute__((musttail)) return x3927(std::monostate{});
return std::monostate{};
}
std::monostate x3927(std::monostate x3928) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3929 = Stack.pop();
SymVal x3930 = SymStack.pop();
Frames.set(4, x3929);
SymFrames.set(4, x3930);
}
__attribute__((musttail)) return x3869(std::monostate{});
return std::monostate{};
}
std::monostate x3873(std::monostate x3874) {
info("Entering the false branch 45 of the if");
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x3875 = Stack.pop();
SymVal x3876 = SymStack.pop();
Num x3877 = Stack.pop();
SymVal x3878 = SymStack.pop();
Num x3879 = x3877.i32_mul(x3875);
Stack.push(x3879);
bool x3880 = allConcrete(x3878, x3876);
SymVal x3881 = x3880 ? Concrete(x3879, 32) : x3878.mul(x3876);
SymStack.push(x3881);
}
{
Num x3882 = Stack.pop();
SymVal x3883 = SymStack.pop();
Num x3884 = Stack.pop();
SymVal x3885 = SymStack.pop();
Num x3886 = x3884.i32_add(x3882);
Stack.push(x3886);
bool x3887 = allConcrete(x3885, x3883);
SymVal x3888 = x3887 ? Concrete(x3886, 32) : x3885.add(x3883);
SymStack.push(x3888);
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
Num x3889 = Stack.pop();
SymVal x3890 = SymStack.pop();
Num x3891 = Stack.pop();
SymVal x3892 = SymStack.pop();
Num x3893 = x3891.i32_sub(x3889);
Stack.push(x3893);
bool x3894 = allConcrete(x3892, x3890);
SymVal x3895 = x3894 ? Concrete(x3893, 32) : x3892.minus(x3890);
SymStack.push(x3895);
}
{
Num x3896 = Stack.pop();
SymVal x3897 = SymStack.pop();
Num x3898 = Stack.pop();
SymVal x3899 = SymStack.pop();
Num x3900 = x3898.i32_mul(x3896);
Stack.push(x3900);
bool x3901 = allConcrete(x3899, x3897);
SymVal x3902 = x3901 ? Concrete(x3900, 32) : x3899.mul(x3897);
SymStack.push(x3902);
}
{
Num x3903 = Stack.pop();
SymVal x3904 = SymStack.pop();
Num x3905 = Stack.pop();
SymVal x3906 = SymStack.pop();
Num x3907 = x3905.i32_add(x3903);
Stack.push(x3907);
bool x3908 = allConcrete(x3906, x3904);
SymVal x3909 = x3908 ? Concrete(x3907, 32) : x3906.add(x3904);
SymStack.push(x3909);
}
{
Num x3910 = Stack.pop();
SymStack.pop();
Num x3911 = I32V(Memory.loadInt(x3910.toInt(), 8));
SymVal x3912 = SymMemory.loadSym(x3910.toInt(), 8);
Stack.push(x3911);
SymStack.push(x3912);
}
{
Num x3913 = Stack.pop();
SymVal x3914 = SymStack.pop();
Num x3915 = Stack.pop();
SymStack.pop();
int x3916 = x3915.toInt();
Memory.storeInt(x3916, 8, x3913.toInt());
SymMemory.storeSym(x3916, 8, x3914);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3917 = Stack.pop();
SymVal x3918 = SymStack.pop();
Num x3919 = Stack.pop();
SymVal x3920 = SymStack.pop();
Num x3921 = x3919.i32_sub(x3917);
Stack.push(x3921);
bool x3922 = allConcrete(x3920, x3918);
SymVal x3923 = x3922 ? Concrete(x3921, 32) : x3920.minus(x3918);
SymStack.push(x3923);
}
{
Num x3924 = Stack.pop();
SymVal x3925 = SymStack.pop();
Frames.set(3, x3924);
SymFrames.set(3, x3925);
}
info("Jump to 1");
__attribute__((musttail)) return x3926(std::monostate{});
return std::monostate{};
}
std::monostate x3871(std::monostate x3872) {
info("Entering the false branch 44 of the if");
__attribute__((musttail)) return x3869(std::monostate{});
return std::monostate{};
}
std::monostate x3869(std::monostate x3870) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x3855(std::monostate{});
return std::monostate{};
}
std::monostate x3867(std::monostate x3868) {
info("Entering the false branch 32 of the if");
__attribute__((musttail)) return x3855(std::monostate{});
return std::monostate{};
}
std::monostate x3855(std::monostate x3856) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(Frames.get(4));
SymStack.push(SymFrames.get(4));
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x3857 = Stack.pop();
SymVal x3858 = SymStack.pop();
Num x3859 = Stack.pop();
SymVal x3860 = SymStack.pop();
Num x3861 = x3859.i32_eq(x3857);
Stack.push(x3861);
bool x3862 = allConcrete(x3860, x3858);
SymVal x3863 = x3862 ? Concrete(x3861, 32) : x3860.eq(x3858).bool2bv();
SymStack.push(x3863);
}
Num x3864 = Stack.pop();
{
SymVal x3865 = SymStack.pop();
ExploreTree.fillIfElseNode(x3865, 33);
}
int x3866 = x3864.toInt();
if (x3866 != 0) {
ExploreTree.moveCursor(true, makeControl(x2083, CURRENT_MCONT));
__attribute__((musttail)) return x3833(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x3833, CURRENT_MCONT));
__attribute__((musttail)) return x2083(std::monostate{});
}
return std::monostate{};
}
std::monostate x3833(std::monostate x3834) {
info("Entering the true branch 33 of the if");
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3835 = Stack.pop();
SymVal x3836 = SymStack.pop();
Num x3837 = Stack.pop();
SymVal x3838 = SymStack.pop();
Num x3839 = x3837.i32_add(x3835);
Stack.push(x3839);
bool x3840 = allConcrete(x3838, x3836);
SymVal x3841 = x3840 ? Concrete(x3839, 32) : x3838.add(x3836);
SymStack.push(x3841);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x3842 = Stack.pop();
SymStack.pop();
Num x3843 = I32V(Memory.loadInt(x3842.toInt(), 4));
SymVal x3844 = SymMemory.loadSym(x3842.toInt(), 4);
Stack.push(x3843);
SymStack.push(x3844);
}
{
Num x3845 = Stack.pop();
SymVal x3846 = SymStack.pop();
Num x3847 = Stack.pop();
SymVal x3848 = SymStack.pop();
Num x3849 = x3847.i32_le_s(x3845);
Stack.push(x3849);
bool x3850 = allConcrete(x3848, x3846);
SymVal x3851 = x3850 ? Concrete(x3849, 32) : x3848.le(x3846).bool2bv();
SymStack.push(x3851);
}
Num x3852 = Stack.pop();
{
SymVal x3853 = SymStack.pop();
ExploreTree.fillIfElseNode(x3853, 34);
}
int x3854 = x3852.toInt();
if (x3854 != 0) {
ExploreTree.moveCursor(true, makeControl(x3051, CURRENT_MCONT));
__attribute__((musttail)) return x3791(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x3791, CURRENT_MCONT));
__attribute__((musttail)) return x3051(std::monostate{});
}
return std::monostate{};
}
std::monostate x3791(std::monostate x3792) {
info("Entering the true branch 34 of the if");
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x3793 = Stack.pop();
SymStack.pop();
Num x3794 = I32V(Memory.loadInt(x3793.toInt(), 4));
SymVal x3795 = SymMemory.loadSym(x3793.toInt(), 4);
Stack.push(x3794);
SymStack.push(x3795);
}
{
Num x3796 = Stack.pop();
SymVal x3797 = SymStack.pop();
Num x3798 = Stack.pop();
SymVal x3799 = SymStack.pop();
Num x3800 = x3798.i32_mul(x3796);
Stack.push(x3800);
bool x3801 = allConcrete(x3799, x3797);
SymVal x3802 = x3801 ? Concrete(x3800, 32) : x3799.mul(x3797);
SymStack.push(x3802);
}
{
Num x3803 = Stack.pop();
SymVal x3804 = SymStack.pop();
Num x3805 = Stack.pop();
SymVal x3806 = SymStack.pop();
Num x3807 = x3805.i32_add(x3803);
Stack.push(x3807);
bool x3808 = allConcrete(x3806, x3804);
SymVal x3809 = x3808 ? Concrete(x3807, 32) : x3806.add(x3804);
SymStack.push(x3809);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x3810 = Stack.pop();
SymVal x3811 = SymStack.pop();
Num x3812 = Stack.pop();
SymVal x3813 = SymStack.pop();
Num x3814 = x3812.i32_mul(x3810);
Stack.push(x3814);
bool x3815 = allConcrete(x3813, x3811);
SymVal x3816 = x3815 ? Concrete(x3814, 32) : x3813.mul(x3811);
SymStack.push(x3816);
}
{
Num x3817 = Stack.pop();
SymVal x3818 = SymStack.pop();
Num x3819 = Stack.pop();
SymVal x3820 = SymStack.pop();
Num x3821 = x3819.i32_add(x3817);
Stack.push(x3821);
bool x3822 = allConcrete(x3820, x3818);
SymVal x3823 = x3822 ? Concrete(x3821, 32) : x3820.add(x3818);
SymStack.push(x3823);
}
{
Num x3824 = Stack.pop();
SymStack.pop();
Num x3825 = I32V(Memory.loadInt(x3824.toInt(), 8));
SymVal x3826 = SymMemory.loadSym(x3824.toInt(), 8);
Stack.push(x3825);
SymStack.push(x3826);
}
{
Num x3827 = Stack.pop();
SymVal x3828 = SymStack.pop();
Num x3829 = Stack.pop();
SymStack.pop();
int x3830 = x3829.toInt();
Memory.storeInt(x3830, 8, x3827.toInt());
SymMemory.storeSym(x3830, 8, x3828);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3831 = Stack.pop();
SymVal x3832 = SymStack.pop();
Frames.set(3, x3831);
SymFrames.set(3, x3832);
}
__attribute__((musttail)) return x3789(std::monostate{});
return std::monostate{};
}
std::monostate x3789(std::monostate x3790) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x3164(std::monostate{});
return std::monostate{};
}
std::monostate x3164(std::monostate x3727) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3728 = Stack.pop();
SymStack.pop();
Num x3729 = I32V(Memory.loadInt(x3728.toInt(), 0));
SymVal x3730 = SymMemory.loadSym(x3728.toInt(), 0);
Stack.push(x3729);
SymStack.push(x3730);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3731 = Stack.pop();
SymVal x3732 = SymStack.pop();
Num x3733 = Stack.pop();
SymVal x3734 = SymStack.pop();
Num x3735 = x3733.i32_sub(x3731);
Stack.push(x3735);
bool x3736 = allConcrete(x3734, x3732);
SymVal x3737 = x3736 ? Concrete(x3735, 32) : x3734.minus(x3732);
SymStack.push(x3737);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3738 = Stack.pop();
SymVal x3739 = SymStack.pop();
Num x3740 = Stack.pop();
SymVal x3741 = SymStack.pop();
Num x3742 = x3740.i32_mul(x3738);
Stack.push(x3742);
bool x3743 = allConcrete(x3741, x3739);
SymVal x3744 = x3743 ? Concrete(x3742, 32) : x3741.mul(x3739);
SymStack.push(x3744);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3745 = Stack.pop();
SymVal x3746 = SymStack.pop();
Num x3747 = Stack.pop();
SymVal x3748 = SymStack.pop();
Num x3749 = x3747.i32_add(x3745);
Stack.push(x3749);
bool x3750 = allConcrete(x3748, x3746);
SymVal x3751 = x3750 ? Concrete(x3749, 32) : x3748.add(x3746);
SymStack.push(x3751);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3752 = Stack.pop();
SymVal x3753 = SymStack.pop();
Num x3754 = Stack.pop();
SymVal x3755 = SymStack.pop();
Num x3756 = x3754.i32_mul(x3752);
Stack.push(x3756);
bool x3757 = allConcrete(x3755, x3753);
SymVal x3758 = x3757 ? Concrete(x3756, 32) : x3755.mul(x3753);
SymStack.push(x3758);
}
{
Num x3759 = Stack.pop();
SymVal x3760 = SymStack.pop();
Num x3761 = Stack.pop();
SymVal x3762 = SymStack.pop();
Num x3763 = x3761.i32_add(x3759);
Stack.push(x3763);
bool x3764 = allConcrete(x3762, x3760);
SymVal x3765 = x3764 ? Concrete(x3763, 32) : x3762.add(x3760);
SymStack.push(x3765);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x3766 = Stack.pop();
SymVal x3767 = SymStack.pop();
Num x3768 = Stack.pop();
SymVal x3769 = SymStack.pop();
Num x3770 = x3768.i32_add(x3766);
Stack.push(x3770);
bool x3771 = allConcrete(x3769, x3767);
SymVal x3772 = x3771 ? Concrete(x3770, 32) : x3769.add(x3767);
SymStack.push(x3772);
}
{
Num x3773 = Stack.pop();
SymStack.pop();
Num x3774 = I32V(Memory.loadInt(x3773.toInt(), 8));
SymVal x3775 = SymMemory.loadSym(x3773.toInt(), 8);
Stack.push(x3774);
SymStack.push(x3775);
}
{
Num x3776 = Stack.pop();
SymStack.pop();
Num x3777 = I32V(Memory.loadInt(x3776.toInt(), 4));
SymVal x3778 = SymMemory.loadSym(x3776.toInt(), 4);
Stack.push(x3777);
SymStack.push(x3778);
}
{
Num x3779 = Stack.pop();
SymVal x3780 = SymStack.pop();
Num x3781 = Stack.pop();
SymVal x3782 = SymStack.pop();
Num x3783 = x3781.i32_eq(x3779);
Stack.push(x3783);
bool x3784 = allConcrete(x3782, x3780);
SymVal x3785 = x3784 ? Concrete(x3783, 32) : x3782.eq(x3780).bool2bv();
SymStack.push(x3785);
}
Num x3786 = Stack.pop();
{
SymVal x3787 = SymStack.pop();
ExploreTree.fillIfElseNode(x3787, 41);
}
int x3788 = x3786.toInt();
if (x3788 != 0) {
ExploreTree.moveCursor(true, makeControl(x3053, CURRENT_MCONT));
__attribute__((musttail)) return x3725(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x3725, CURRENT_MCONT));
__attribute__((musttail)) return x3053(std::monostate{});
}
return std::monostate{};
}
std::monostate x3725(std::monostate x3726) {
info("Entering the true branch 41 of the if");
info("Jump to 2");
__attribute__((musttail)) return x3710(std::monostate{});
return std::monostate{};
}
std::monostate x3710(std::monostate x3711) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x3712 = Stack.pop();
SymStack.pop();
Num x3713 = I32V(Memory.loadInt(x3712.toInt(), 0));
SymVal x3714 = SymMemory.loadSym(x3712.toInt(), 0);
Stack.push(x3713);
SymStack.push(x3714);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3715 = Stack.pop();
SymVal x3716 = SymStack.pop();
Num x3717 = Stack.pop();
SymVal x3718 = SymStack.pop();
Num x3719 = x3717.i32_ne(x3715);
Stack.push(x3719);
bool x3720 = allConcrete(x3718, x3716);
SymVal x3721 = x3720 ? Concrete(x3719, 32) : x3718.neq(x3716).bool2bv();
SymStack.push(x3721);
}
Num x3722 = Stack.pop();
{
SymVal x3723 = SymStack.pop();
ExploreTree.fillIfElseNode(x3723, 42);
}
int x3724 = x3722.toInt();
if (x3724 != 0) {
ExploreTree.moveCursor(true, makeControl(x3469, CURRENT_MCONT));
__attribute__((musttail)) return x3706(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x3706, CURRENT_MCONT));
__attribute__((musttail)) return x3469(std::monostate{});
}
return std::monostate{};
}
std::monostate x3706(std::monostate x3707) {
info("Entering the true branch 42 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3708 = Stack.pop();
SymVal x3709 = SymStack.pop();
Frames.set(3, x3708);
SymFrames.set(3, x3709);
}
__attribute__((musttail)) return x3704(std::monostate{});
return std::monostate{};
}
std::monostate x3704(std::monostate x3705) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x3630(std::monostate{});
return std::monostate{};
}
std::monostate x3630(std::monostate x3635) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3636 = Stack.pop();
SymStack.pop();
Num x3637 = I32V(Memory.loadInt(x3636.toInt(), 0));
SymVal x3638 = SymMemory.loadSym(x3636.toInt(), 0);
Stack.push(x3637);
SymStack.push(x3638);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3639 = Stack.pop();
SymVal x3640 = SymStack.pop();
Num x3641 = Stack.pop();
SymVal x3642 = SymStack.pop();
Num x3643 = x3641.i32_sub(x3639);
Stack.push(x3643);
bool x3644 = allConcrete(x3642, x3640);
SymVal x3645 = x3644 ? Concrete(x3643, 32) : x3642.minus(x3640);
SymStack.push(x3645);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3646 = Stack.pop();
SymVal x3647 = SymStack.pop();
Num x3648 = Stack.pop();
SymVal x3649 = SymStack.pop();
Num x3650 = x3648.i32_mul(x3646);
Stack.push(x3650);
bool x3651 = allConcrete(x3649, x3647);
SymVal x3652 = x3651 ? Concrete(x3650, 32) : x3649.mul(x3647);
SymStack.push(x3652);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3653 = Stack.pop();
SymVal x3654 = SymStack.pop();
Num x3655 = Stack.pop();
SymVal x3656 = SymStack.pop();
Num x3657 = x3655.i32_add(x3653);
Stack.push(x3657);
bool x3658 = allConcrete(x3656, x3654);
SymVal x3659 = x3658 ? Concrete(x3657, 32) : x3656.add(x3654);
SymStack.push(x3659);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3660 = Stack.pop();
SymVal x3661 = SymStack.pop();
Num x3662 = Stack.pop();
SymVal x3663 = SymStack.pop();
Num x3664 = x3662.i32_mul(x3660);
Stack.push(x3664);
bool x3665 = allConcrete(x3663, x3661);
SymVal x3666 = x3665 ? Concrete(x3664, 32) : x3663.mul(x3661);
SymStack.push(x3666);
}
{
Num x3667 = Stack.pop();
SymVal x3668 = SymStack.pop();
Num x3669 = Stack.pop();
SymVal x3670 = SymStack.pop();
Num x3671 = x3669.i32_add(x3667);
Stack.push(x3671);
bool x3672 = allConcrete(x3670, x3668);
SymVal x3673 = x3672 ? Concrete(x3671, 32) : x3670.add(x3668);
SymStack.push(x3673);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x3674 = Stack.pop();
SymVal x3675 = SymStack.pop();
Num x3676 = Stack.pop();
SymVal x3677 = SymStack.pop();
Num x3678 = x3676.i32_add(x3674);
Stack.push(x3678);
bool x3679 = allConcrete(x3677, x3675);
SymVal x3680 = x3679 ? Concrete(x3678, 32) : x3677.add(x3675);
SymStack.push(x3680);
}
{
Num x3681 = Stack.pop();
SymStack.pop();
Num x3682 = I32V(Memory.loadInt(x3681.toInt(), 8));
SymVal x3683 = SymMemory.loadSym(x3681.toInt(), 8);
Stack.push(x3682);
SymStack.push(x3683);
}
{
Num x3684 = Stack.pop();
SymStack.pop();
Num x3685 = I32V(Memory.loadInt(x3684.toInt(), 4));
SymVal x3686 = SymMemory.loadSym(x3684.toInt(), 4);
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
Num x3691 = x3689.i32_add(x3687);
Stack.push(x3691);
bool x3692 = allConcrete(x3690, x3688);
SymVal x3693 = x3692 ? Concrete(x3691, 32) : x3690.add(x3688);
SymStack.push(x3693);
}
{
Num x3694 = Stack.pop();
SymVal x3695 = SymStack.pop();
Num x3696 = Stack.pop();
SymVal x3697 = SymStack.pop();
Num x3698 = x3696.i32_eq(x3694);
Stack.push(x3698);
bool x3699 = allConcrete(x3697, x3695);
SymVal x3700 = x3699 ? Concrete(x3698, 32) : x3697.eq(x3695).bool2bv();
SymStack.push(x3700);
}
Num x3701 = Stack.pop();
{
SymVal x3702 = SymStack.pop();
ExploreTree.fillIfElseNode(x3702, 43);
}
int x3703 = x3701.toInt();
if (x3703 != 0) {
ExploreTree.moveCursor(true, makeControl(x3471, CURRENT_MCONT));
__attribute__((musttail)) return x3633(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x3633, CURRENT_MCONT));
__attribute__((musttail)) return x3471(std::monostate{});
}
return std::monostate{};
}
std::monostate x3633(std::monostate x3634) {
info("Entering the true branch 43 of the if");
info("Jump to 2");
__attribute__((musttail)) return x3631(std::monostate{});
return std::monostate{};
}
std::monostate x3631(std::monostate x3632) {
info("Exiting the block, stackSize =", Stack.size());
__attribute__((musttail)) return x3386(std::monostate{});
return std::monostate{};
}
std::monostate x3471(std::monostate x3472) {
info("Entering the false branch 43 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3473 = Stack.pop();
SymStack.pop();
Num x3474 = I32V(Memory.loadInt(x3473.toInt(), 0));
SymVal x3475 = SymMemory.loadSym(x3473.toInt(), 0);
Stack.push(x3474);
SymStack.push(x3475);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3476 = Stack.pop();
SymVal x3477 = SymStack.pop();
Num x3478 = Stack.pop();
SymVal x3479 = SymStack.pop();
Num x3480 = x3478.i32_sub(x3476);
Stack.push(x3480);
bool x3481 = allConcrete(x3479, x3477);
SymVal x3482 = x3481 ? Concrete(x3480, 32) : x3479.minus(x3477);
SymStack.push(x3482);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3483 = Stack.pop();
SymVal x3484 = SymStack.pop();
Num x3485 = Stack.pop();
SymVal x3486 = SymStack.pop();
Num x3487 = x3485.i32_mul(x3483);
Stack.push(x3487);
bool x3488 = allConcrete(x3486, x3484);
SymVal x3489 = x3488 ? Concrete(x3487, 32) : x3486.mul(x3484);
SymStack.push(x3489);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x3490 = Stack.pop();
SymStack.pop();
Num x3491 = I32V(Memory.loadInt(x3490.toInt(), 4));
SymVal x3492 = SymMemory.loadSym(x3490.toInt(), 4);
Stack.push(x3491);
SymStack.push(x3492);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x3493 = Stack.pop();
SymVal x3494 = SymStack.pop();
Num x3495 = Stack.pop();
SymVal x3496 = SymStack.pop();
Num x3497 = x3495.i32_add(x3493);
Stack.push(x3497);
bool x3498 = allConcrete(x3496, x3494);
SymVal x3499 = x3498 ? Concrete(x3497, 32) : x3496.add(x3494);
SymStack.push(x3499);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3500 = Stack.pop();
SymVal x3501 = SymStack.pop();
Num x3502 = Stack.pop();
SymVal x3503 = SymStack.pop();
Num x3504 = x3502.i32_add(x3500);
Stack.push(x3504);
bool x3505 = allConcrete(x3503, x3501);
SymVal x3506 = x3505 ? Concrete(x3504, 32) : x3503.add(x3501);
SymStack.push(x3506);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3507 = Stack.pop();
SymVal x3508 = SymStack.pop();
Num x3509 = Stack.pop();
SymVal x3510 = SymStack.pop();
Num x3511 = x3509.i32_mul(x3507);
Stack.push(x3511);
bool x3512 = allConcrete(x3510, x3508);
SymVal x3513 = x3512 ? Concrete(x3511, 32) : x3510.mul(x3508);
SymStack.push(x3513);
}
{
Num x3514 = Stack.pop();
SymVal x3515 = SymStack.pop();
Num x3516 = Stack.pop();
SymVal x3517 = SymStack.pop();
Num x3518 = x3516.i32_add(x3514);
Stack.push(x3518);
bool x3519 = allConcrete(x3517, x3515);
SymVal x3520 = x3519 ? Concrete(x3518, 32) : x3517.add(x3515);
SymStack.push(x3520);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x3521 = Stack.pop();
SymVal x3522 = SymStack.pop();
Num x3523 = Stack.pop();
SymVal x3524 = SymStack.pop();
Num x3525 = x3523.i32_add(x3521);
Stack.push(x3525);
bool x3526 = allConcrete(x3524, x3522);
SymVal x3527 = x3526 ? Concrete(x3525, 32) : x3524.add(x3522);
SymStack.push(x3527);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3528 = Stack.pop();
SymStack.pop();
Num x3529 = I32V(Memory.loadInt(x3528.toInt(), 0));
SymVal x3530 = SymMemory.loadSym(x3528.toInt(), 0);
Stack.push(x3529);
SymStack.push(x3530);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3531 = Stack.pop();
SymVal x3532 = SymStack.pop();
Num x3533 = Stack.pop();
SymVal x3534 = SymStack.pop();
Num x3535 = x3533.i32_sub(x3531);
Stack.push(x3535);
bool x3536 = allConcrete(x3534, x3532);
SymVal x3537 = x3536 ? Concrete(x3535, 32) : x3534.minus(x3532);
SymStack.push(x3537);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3538 = Stack.pop();
SymVal x3539 = SymStack.pop();
Num x3540 = Stack.pop();
SymVal x3541 = SymStack.pop();
Num x3542 = x3540.i32_mul(x3538);
Stack.push(x3542);
bool x3543 = allConcrete(x3541, x3539);
SymVal x3544 = x3543 ? Concrete(x3542, 32) : x3541.mul(x3539);
SymStack.push(x3544);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3545 = Stack.pop();
SymVal x3546 = SymStack.pop();
Num x3547 = Stack.pop();
SymVal x3548 = SymStack.pop();
Num x3549 = x3547.i32_mul(x3545);
Stack.push(x3549);
bool x3550 = allConcrete(x3548, x3546);
SymVal x3551 = x3550 ? Concrete(x3549, 32) : x3548.mul(x3546);
SymStack.push(x3551);
}
{
Num x3552 = Stack.pop();
SymVal x3553 = SymStack.pop();
Num x3554 = Stack.pop();
SymVal x3555 = SymStack.pop();
Num x3556 = x3554.i32_add(x3552);
Stack.push(x3556);
bool x3557 = allConcrete(x3555, x3553);
SymVal x3558 = x3557 ? Concrete(x3556, 32) : x3555.add(x3553);
SymStack.push(x3558);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3559 = Stack.pop();
SymStack.pop();
Num x3560 = I32V(Memory.loadInt(x3559.toInt(), 0));
SymVal x3561 = SymMemory.loadSym(x3559.toInt(), 0);
Stack.push(x3560);
SymStack.push(x3561);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3562 = Stack.pop();
SymVal x3563 = SymStack.pop();
Num x3564 = Stack.pop();
SymVal x3565 = SymStack.pop();
Num x3566 = x3564.i32_sub(x3562);
Stack.push(x3566);
bool x3567 = allConcrete(x3565, x3563);
SymVal x3568 = x3567 ? Concrete(x3566, 32) : x3565.minus(x3563);
SymStack.push(x3568);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3569 = Stack.pop();
SymVal x3570 = SymStack.pop();
Num x3571 = Stack.pop();
SymVal x3572 = SymStack.pop();
Num x3573 = x3571.i32_mul(x3569);
Stack.push(x3573);
bool x3574 = allConcrete(x3572, x3570);
SymVal x3575 = x3574 ? Concrete(x3573, 32) : x3572.mul(x3570);
SymStack.push(x3575);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3576 = Stack.pop();
SymVal x3577 = SymStack.pop();
Num x3578 = Stack.pop();
SymVal x3579 = SymStack.pop();
Num x3580 = x3578.i32_add(x3576);
Stack.push(x3580);
bool x3581 = allConcrete(x3579, x3577);
SymVal x3582 = x3581 ? Concrete(x3580, 32) : x3579.add(x3577);
SymStack.push(x3582);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3583 = Stack.pop();
SymVal x3584 = SymStack.pop();
Num x3585 = Stack.pop();
SymVal x3586 = SymStack.pop();
Num x3587 = x3585.i32_mul(x3583);
Stack.push(x3587);
bool x3588 = allConcrete(x3586, x3584);
SymVal x3589 = x3588 ? Concrete(x3587, 32) : x3586.mul(x3584);
SymStack.push(x3589);
}
{
Num x3590 = Stack.pop();
SymVal x3591 = SymStack.pop();
Num x3592 = Stack.pop();
SymVal x3593 = SymStack.pop();
Num x3594 = x3592.i32_add(x3590);
Stack.push(x3594);
bool x3595 = allConcrete(x3593, x3591);
SymVal x3596 = x3595 ? Concrete(x3594, 32) : x3593.add(x3591);
SymStack.push(x3596);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x3597 = Stack.pop();
SymVal x3598 = SymStack.pop();
Num x3599 = Stack.pop();
SymVal x3600 = SymStack.pop();
Num x3601 = x3599.i32_add(x3597);
Stack.push(x3601);
bool x3602 = allConcrete(x3600, x3598);
SymVal x3603 = x3602 ? Concrete(x3601, 32) : x3600.add(x3598);
SymStack.push(x3603);
}
{
Num x3604 = Stack.pop();
SymStack.pop();
Num x3605 = I32V(Memory.loadInt(x3604.toInt(), 8));
SymVal x3606 = SymMemory.loadSym(x3604.toInt(), 8);
Stack.push(x3605);
SymStack.push(x3606);
}
{
Num x3607 = Stack.pop();
SymVal x3608 = SymStack.pop();
Num x3609 = Stack.pop();
SymVal x3610 = SymStack.pop();
Num x3611 = x3609.i32_add(x3607);
Stack.push(x3611);
bool x3612 = allConcrete(x3610, x3608);
SymVal x3613 = x3612 ? Concrete(x3611, 32) : x3610.add(x3608);
SymStack.push(x3613);
}
{
Num x3614 = Stack.pop();
SymStack.pop();
Num x3615 = I32V(Memory.loadInt(x3614.toInt(), 8));
SymVal x3616 = SymMemory.loadSym(x3614.toInt(), 8);
Stack.push(x3615);
SymStack.push(x3616);
}
{
Num x3617 = Stack.pop();
SymVal x3618 = SymStack.pop();
Num x3619 = Stack.pop();
SymStack.pop();
int x3620 = x3619.toInt();
Memory.storeInt(x3620, 8, x3617.toInt());
SymMemory.storeSym(x3620, 8, x3618);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3621 = Stack.pop();
SymVal x3622 = SymStack.pop();
Num x3623 = Stack.pop();
SymVal x3624 = SymStack.pop();
Num x3625 = x3623.i32_add(x3621);
Stack.push(x3625);
bool x3626 = allConcrete(x3624, x3622);
SymVal x3627 = x3626 ? Concrete(x3625, 32) : x3624.add(x3622);
SymStack.push(x3627);
}
{
Num x3628 = Stack.pop();
SymVal x3629 = SymStack.pop();
Frames.set(3, x3628);
SymFrames.set(3, x3629);
}
info("Jump to 1");
__attribute__((musttail)) return x3630(std::monostate{});
return std::monostate{};
}
std::monostate x3469(std::monostate x3470) {
info("Entering the false branch 42 of the if");
__attribute__((musttail)) return x3386(std::monostate{});
return std::monostate{};
}
std::monostate x3386(std::monostate x3387) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x3388 = Stack.pop();
SymStack.pop();
Num x3389 = I32V(Memory.loadInt(x3388.toInt(), 4));
SymVal x3390 = SymMemory.loadSym(x3388.toInt(), 4);
Stack.push(x3389);
SymStack.push(x3390);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3391 = Stack.pop();
SymStack.pop();
Num x3392 = I32V(Memory.loadInt(x3391.toInt(), 0));
SymVal x3393 = SymMemory.loadSym(x3391.toInt(), 0);
Stack.push(x3392);
SymStack.push(x3393);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3394 = Stack.pop();
SymVal x3395 = SymStack.pop();
Num x3396 = Stack.pop();
SymVal x3397 = SymStack.pop();
Num x3398 = x3396.i32_sub(x3394);
Stack.push(x3398);
bool x3399 = allConcrete(x3397, x3395);
SymVal x3400 = x3399 ? Concrete(x3398, 32) : x3397.minus(x3395);
SymStack.push(x3400);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3401 = Stack.pop();
SymVal x3402 = SymStack.pop();
Num x3403 = Stack.pop();
SymVal x3404 = SymStack.pop();
Num x3405 = x3403.i32_mul(x3401);
Stack.push(x3405);
bool x3406 = allConcrete(x3404, x3402);
SymVal x3407 = x3406 ? Concrete(x3405, 32) : x3404.mul(x3402);
SymStack.push(x3407);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3408 = Stack.pop();
SymVal x3409 = SymStack.pop();
Num x3410 = Stack.pop();
SymVal x3411 = SymStack.pop();
Num x3412 = x3410.i32_add(x3408);
Stack.push(x3412);
bool x3413 = allConcrete(x3411, x3409);
SymVal x3414 = x3413 ? Concrete(x3412, 32) : x3411.add(x3409);
SymStack.push(x3414);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3415 = Stack.pop();
SymVal x3416 = SymStack.pop();
Num x3417 = Stack.pop();
SymVal x3418 = SymStack.pop();
Num x3419 = x3417.i32_mul(x3415);
Stack.push(x3419);
bool x3420 = allConcrete(x3418, x3416);
SymVal x3421 = x3420 ? Concrete(x3419, 32) : x3418.mul(x3416);
SymStack.push(x3421);
}
{
Num x3422 = Stack.pop();
SymVal x3423 = SymStack.pop();
Num x3424 = Stack.pop();
SymVal x3425 = SymStack.pop();
Num x3426 = x3424.i32_add(x3422);
Stack.push(x3426);
bool x3427 = allConcrete(x3425, x3423);
SymVal x3428 = x3427 ? Concrete(x3426, 32) : x3425.add(x3423);
SymStack.push(x3428);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x3429 = Stack.pop();
SymVal x3430 = SymStack.pop();
Num x3431 = Stack.pop();
SymVal x3432 = SymStack.pop();
Num x3433 = x3431.i32_add(x3429);
Stack.push(x3433);
bool x3434 = allConcrete(x3432, x3430);
SymVal x3435 = x3434 ? Concrete(x3433, 32) : x3432.add(x3430);
SymStack.push(x3435);
}
{
Num x3436 = Stack.pop();
SymStack.pop();
Num x3437 = I32V(Memory.loadInt(x3436.toInt(), 8));
SymVal x3438 = SymMemory.loadSym(x3436.toInt(), 8);
Stack.push(x3437);
SymStack.push(x3438);
}
{
Num x3439 = Stack.pop();
SymStack.pop();
Num x3440 = I32V(Memory.loadInt(x3439.toInt(), 4));
SymVal x3441 = SymMemory.loadSym(x3439.toInt(), 4);
Stack.push(x3440);
SymStack.push(x3441);
}
{
Num x3442 = Stack.pop();
SymVal x3443 = SymStack.pop();
Num x3444 = Stack.pop();
SymVal x3445 = SymStack.pop();
Num x3446 = x3444.i32_add(x3442);
Stack.push(x3446);
bool x3447 = allConcrete(x3445, x3443);
SymVal x3448 = x3447 ? Concrete(x3446, 32) : x3445.add(x3443);
SymStack.push(x3448);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3449 = Stack.pop();
SymVal x3450 = SymStack.pop();
Num x3451 = Stack.pop();
SymVal x3452 = SymStack.pop();
Num x3453 = x3451.i32_add(x3449);
Stack.push(x3453);
bool x3454 = allConcrete(x3452, x3450);
SymVal x3455 = x3454 ? Concrete(x3453, 32) : x3452.add(x3450);
SymStack.push(x3455);
}
{
Num x3456 = Stack.pop();
SymVal x3457 = SymStack.pop();
Num x3458 = Stack.pop();
SymStack.pop();
int x3459 = x3458.toInt();
Memory.storeInt(x3459, 4, x3456.toInt());
SymMemory.storeSym(x3459, 4, x3457);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3460 = Stack.pop();
SymVal x3461 = SymStack.pop();
Num x3462 = Stack.pop();
SymVal x3463 = SymStack.pop();
Num x3464 = x3462.i32_add(x3460);
Stack.push(x3464);
bool x3465 = allConcrete(x3463, x3461);
SymVal x3466 = x3465 ? Concrete(x3464, 32) : x3463.add(x3461);
SymStack.push(x3466);
}
{
Num x3467 = Stack.pop();
SymVal x3468 = SymStack.pop();
Frames.set(3, x3467);
SymFrames.set(3, x3468);
}
__attribute__((musttail)) return x3384(std::monostate{});
return std::monostate{};
}
std::monostate x3384(std::monostate x3385) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x3266(std::monostate{});
return std::monostate{};
}
std::monostate x3266(std::monostate x3370) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x3371 = Stack.pop();
SymStack.pop();
Num x3372 = I32V(Memory.loadInt(x3371.toInt(), 4));
SymVal x3373 = SymMemory.loadSym(x3371.toInt(), 4);
Stack.push(x3372);
SymStack.push(x3373);
}
{
Num x3374 = Stack.pop();
SymVal x3375 = SymStack.pop();
Num x3376 = Stack.pop();
SymVal x3377 = SymStack.pop();
Num x3378 = x3376.i32_eq(x3374);
Stack.push(x3378);
bool x3379 = allConcrete(x3377, x3375);
SymVal x3380 = x3379 ? Concrete(x3378, 32) : x3377.eq(x3375).bool2bv();
SymStack.push(x3380);
}
Num x3381 = Stack.pop();
{
SymVal x3382 = SymStack.pop();
ExploreTree.fillIfElseNode(x3382, 38);
}
int x3383 = x3381.toInt();
if (x3383 != 0) {
ExploreTree.moveCursor(true, makeControl(x3165, CURRENT_MCONT));
__attribute__((musttail)) return x3368(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x3368, CURRENT_MCONT));
__attribute__((musttail)) return x3165(std::monostate{});
}
return std::monostate{};
}
std::monostate x3368(std::monostate x3369) {
info("Entering the true branch 38 of the if");
info("Jump to 2");
__attribute__((musttail)) return x3364(std::monostate{});
return std::monostate{};
}
std::monostate x3364(std::monostate x3365) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x3366 = Stack.pop();
SymVal x3367 = SymStack.pop();
Frames.set(3, x3366);
SymFrames.set(3, x3367);
}
__attribute__((musttail)) return x3362(std::monostate{});
return std::monostate{};
}
std::monostate x3362(std::monostate x3363) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x3320(std::monostate{});
return std::monostate{};
}
std::monostate x3320(std::monostate x3341) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x3342 = Stack.pop();
SymStack.pop();
Num x3343 = I32V(Memory.loadInt(x3342.toInt(), 4));
SymVal x3344 = SymMemory.loadSym(x3342.toInt(), 4);
Stack.push(x3343);
SymStack.push(x3344);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3345 = Stack.pop();
SymVal x3346 = SymStack.pop();
Num x3347 = Stack.pop();
SymVal x3348 = SymStack.pop();
Num x3349 = x3347.i32_sub(x3345);
Stack.push(x3349);
bool x3350 = allConcrete(x3348, x3346);
SymVal x3351 = x3350 ? Concrete(x3349, 32) : x3348.minus(x3346);
SymStack.push(x3351);
}
{
Num x3352 = Stack.pop();
SymVal x3353 = SymStack.pop();
Num x3354 = Stack.pop();
SymVal x3355 = SymStack.pop();
Num x3356 = x3354.i32_eq(x3352);
Stack.push(x3356);
bool x3357 = allConcrete(x3355, x3353);
SymVal x3358 = x3357 ? Concrete(x3356, 32) : x3355.eq(x3353).bool2bv();
SymStack.push(x3358);
}
Num x3359 = Stack.pop();
{
SymVal x3360 = SymStack.pop();
ExploreTree.fillIfElseNode(x3360, 39);
}
int x3361 = x3359.toInt();
if (x3361 != 0) {
ExploreTree.moveCursor(true, makeControl(x3267, CURRENT_MCONT));
__attribute__((musttail)) return x3339(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x3339, CURRENT_MCONT));
__attribute__((musttail)) return x3267(std::monostate{});
}
return std::monostate{};
}
std::monostate x3339(std::monostate x3340) {
info("Entering the true branch 39 of the if");
info("Jump to 2");
__attribute__((musttail)) return x3321(std::monostate{});
return std::monostate{};
}
std::monostate x3321(std::monostate x3322) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x3323 = Stack.pop();
SymStack.pop();
Num x3324 = I32V(Memory.loadInt(x3323.toInt(), 4));
SymVal x3325 = SymMemory.loadSym(x3323.toInt(), 4);
Stack.push(x3324);
SymStack.push(x3325);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3326 = Stack.pop();
SymVal x3327 = SymStack.pop();
Num x3328 = Stack.pop();
SymVal x3329 = SymStack.pop();
Num x3330 = x3328.i32_sub(x3326);
Stack.push(x3330);
bool x3331 = allConcrete(x3329, x3327);
SymVal x3332 = x3331 ? Concrete(x3330, 32) : x3329.minus(x3327);
SymStack.push(x3332);
}
{
Num x3333 = Stack.pop();
SymVal x3334 = SymStack.pop();
Num x3335 = Stack.pop();
SymStack.pop();
int x3336 = x3335.toInt();
Memory.storeInt(x3336, 4, x3333.toInt());
SymMemory.storeSym(x3336, 4, x3334);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3337 = Stack.pop();
SymVal x3338 = SymStack.pop();
Frames.set(4, x3337);
SymFrames.set(4, x3338);
}
__attribute__((musttail)) return x3018(std::monostate{});
return std::monostate{};
}
std::monostate x3267(std::monostate x3268) {
info("Entering the false branch 39 of the if");
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x3269 = Stack.pop();
SymVal x3270 = SymStack.pop();
Num x3271 = Stack.pop();
SymVal x3272 = SymStack.pop();
Num x3273 = x3271.i32_mul(x3269);
Stack.push(x3273);
bool x3274 = allConcrete(x3272, x3270);
SymVal x3275 = x3274 ? Concrete(x3273, 32) : x3272.mul(x3270);
SymStack.push(x3275);
}
{
Num x3276 = Stack.pop();
SymVal x3277 = SymStack.pop();
Num x3278 = Stack.pop();
SymVal x3279 = SymStack.pop();
Num x3280 = x3278.i32_add(x3276);
Stack.push(x3280);
bool x3281 = allConcrete(x3279, x3277);
SymVal x3282 = x3281 ? Concrete(x3280, 32) : x3279.add(x3277);
SymStack.push(x3282);
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
Num x3283 = Stack.pop();
SymVal x3284 = SymStack.pop();
Num x3285 = Stack.pop();
SymVal x3286 = SymStack.pop();
Num x3287 = x3285.i32_add(x3283);
Stack.push(x3287);
bool x3288 = allConcrete(x3286, x3284);
SymVal x3289 = x3288 ? Concrete(x3287, 32) : x3286.add(x3284);
SymStack.push(x3289);
}
{
Num x3290 = Stack.pop();
SymVal x3291 = SymStack.pop();
Num x3292 = Stack.pop();
SymVal x3293 = SymStack.pop();
Num x3294 = x3292.i32_mul(x3290);
Stack.push(x3294);
bool x3295 = allConcrete(x3293, x3291);
SymVal x3296 = x3295 ? Concrete(x3294, 32) : x3293.mul(x3291);
SymStack.push(x3296);
}
{
Num x3297 = Stack.pop();
SymVal x3298 = SymStack.pop();
Num x3299 = Stack.pop();
SymVal x3300 = SymStack.pop();
Num x3301 = x3299.i32_add(x3297);
Stack.push(x3301);
bool x3302 = allConcrete(x3300, x3298);
SymVal x3303 = x3302 ? Concrete(x3301, 32) : x3300.add(x3298);
SymStack.push(x3303);
}
{
Num x3304 = Stack.pop();
SymStack.pop();
Num x3305 = I32V(Memory.loadInt(x3304.toInt(), 8));
SymVal x3306 = SymMemory.loadSym(x3304.toInt(), 8);
Stack.push(x3305);
SymStack.push(x3306);
}
{
Num x3307 = Stack.pop();
SymVal x3308 = SymStack.pop();
Num x3309 = Stack.pop();
SymStack.pop();
int x3310 = x3309.toInt();
Memory.storeInt(x3310, 8, x3307.toInt());
SymMemory.storeSym(x3310, 8, x3308);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3311 = Stack.pop();
SymVal x3312 = SymStack.pop();
Num x3313 = Stack.pop();
SymVal x3314 = SymStack.pop();
Num x3315 = x3313.i32_add(x3311);
Stack.push(x3315);
bool x3316 = allConcrete(x3314, x3312);
SymVal x3317 = x3316 ? Concrete(x3315, 32) : x3314.add(x3312);
SymStack.push(x3317);
}
{
Num x3318 = Stack.pop();
SymVal x3319 = SymStack.pop();
Frames.set(3, x3318);
SymFrames.set(3, x3319);
}
info("Jump to 1");
__attribute__((musttail)) return x3320(std::monostate{});
return std::monostate{};
}
std::monostate x3165(std::monostate x3166) {
info("Entering the false branch 38 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3167 = Stack.pop();
SymStack.pop();
Num x3168 = I32V(Memory.loadInt(x3167.toInt(), 0));
SymVal x3169 = SymMemory.loadSym(x3167.toInt(), 0);
Stack.push(x3168);
SymStack.push(x3169);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3170 = Stack.pop();
SymVal x3171 = SymStack.pop();
Num x3172 = Stack.pop();
SymVal x3173 = SymStack.pop();
Num x3174 = x3172.i32_sub(x3170);
Stack.push(x3174);
bool x3175 = allConcrete(x3173, x3171);
SymVal x3176 = x3175 ? Concrete(x3174, 32) : x3173.minus(x3171);
SymStack.push(x3176);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3177 = Stack.pop();
SymVal x3178 = SymStack.pop();
Num x3179 = Stack.pop();
SymVal x3180 = SymStack.pop();
Num x3181 = x3179.i32_mul(x3177);
Stack.push(x3181);
bool x3182 = allConcrete(x3180, x3178);
SymVal x3183 = x3182 ? Concrete(x3181, 32) : x3180.mul(x3178);
SymStack.push(x3183);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3184 = Stack.pop();
SymVal x3185 = SymStack.pop();
Num x3186 = Stack.pop();
SymVal x3187 = SymStack.pop();
Num x3188 = x3186.i32_mul(x3184);
Stack.push(x3188);
bool x3189 = allConcrete(x3187, x3185);
SymVal x3190 = x3189 ? Concrete(x3188, 32) : x3187.mul(x3185);
SymStack.push(x3190);
}
{
Num x3191 = Stack.pop();
SymVal x3192 = SymStack.pop();
Num x3193 = Stack.pop();
SymVal x3194 = SymStack.pop();
Num x3195 = x3193.i32_add(x3191);
Stack.push(x3195);
bool x3196 = allConcrete(x3194, x3192);
SymVal x3197 = x3196 ? Concrete(x3195, 32) : x3194.add(x3192);
SymStack.push(x3197);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x3198 = Stack.pop();
SymVal x3199 = SymStack.pop();
Num x3200 = Stack.pop();
SymVal x3201 = SymStack.pop();
Num x3202 = x3200.i32_add(x3198);
Stack.push(x3202);
bool x3203 = allConcrete(x3201, x3199);
SymVal x3204 = x3203 ? Concrete(x3202, 32) : x3201.add(x3199);
SymStack.push(x3204);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3205 = Stack.pop();
SymStack.pop();
Num x3206 = I32V(Memory.loadInt(x3205.toInt(), 0));
SymVal x3207 = SymMemory.loadSym(x3205.toInt(), 0);
Stack.push(x3206);
SymStack.push(x3207);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3208 = Stack.pop();
SymVal x3209 = SymStack.pop();
Num x3210 = Stack.pop();
SymVal x3211 = SymStack.pop();
Num x3212 = x3210.i32_sub(x3208);
Stack.push(x3212);
bool x3213 = allConcrete(x3211, x3209);
SymVal x3214 = x3213 ? Concrete(x3212, 32) : x3211.minus(x3209);
SymStack.push(x3214);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3215 = Stack.pop();
SymVal x3216 = SymStack.pop();
Num x3217 = Stack.pop();
SymVal x3218 = SymStack.pop();
Num x3219 = x3217.i32_mul(x3215);
Stack.push(x3219);
bool x3220 = allConcrete(x3218, x3216);
SymVal x3221 = x3220 ? Concrete(x3219, 32) : x3218.mul(x3216);
SymStack.push(x3221);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3222 = Stack.pop();
SymVal x3223 = SymStack.pop();
Num x3224 = Stack.pop();
SymVal x3225 = SymStack.pop();
Num x3226 = x3224.i32_add(x3222);
Stack.push(x3226);
bool x3227 = allConcrete(x3225, x3223);
SymVal x3228 = x3227 ? Concrete(x3226, 32) : x3225.add(x3223);
SymStack.push(x3228);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3229 = Stack.pop();
SymVal x3230 = SymStack.pop();
Num x3231 = Stack.pop();
SymVal x3232 = SymStack.pop();
Num x3233 = x3231.i32_mul(x3229);
Stack.push(x3233);
bool x3234 = allConcrete(x3232, x3230);
SymVal x3235 = x3234 ? Concrete(x3233, 32) : x3232.mul(x3230);
SymStack.push(x3235);
}
{
Num x3236 = Stack.pop();
SymVal x3237 = SymStack.pop();
Num x3238 = Stack.pop();
SymVal x3239 = SymStack.pop();
Num x3240 = x3238.i32_add(x3236);
Stack.push(x3240);
bool x3241 = allConcrete(x3239, x3237);
SymVal x3242 = x3241 ? Concrete(x3240, 32) : x3239.add(x3237);
SymStack.push(x3242);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x3243 = Stack.pop();
SymVal x3244 = SymStack.pop();
Num x3245 = Stack.pop();
SymVal x3246 = SymStack.pop();
Num x3247 = x3245.i32_add(x3243);
Stack.push(x3247);
bool x3248 = allConcrete(x3246, x3244);
SymVal x3249 = x3248 ? Concrete(x3247, 32) : x3246.add(x3244);
SymStack.push(x3249);
}
{
Num x3250 = Stack.pop();
SymStack.pop();
Num x3251 = I32V(Memory.loadInt(x3250.toInt(), 8));
SymVal x3252 = SymMemory.loadSym(x3250.toInt(), 8);
Stack.push(x3251);
SymStack.push(x3252);
}
{
Num x3253 = Stack.pop();
SymVal x3254 = SymStack.pop();
Num x3255 = Stack.pop();
SymStack.pop();
int x3256 = x3255.toInt();
Memory.storeInt(x3256, 8, x3253.toInt());
SymMemory.storeSym(x3256, 8, x3254);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3257 = Stack.pop();
SymVal x3258 = SymStack.pop();
Num x3259 = Stack.pop();
SymVal x3260 = SymStack.pop();
Num x3261 = x3259.i32_add(x3257);
Stack.push(x3261);
bool x3262 = allConcrete(x3260, x3258);
SymVal x3263 = x3262 ? Concrete(x3261, 32) : x3260.add(x3258);
SymStack.push(x3263);
}
{
Num x3264 = Stack.pop();
SymVal x3265 = SymStack.pop();
Frames.set(3, x3264);
SymFrames.set(3, x3265);
}
info("Jump to 1");
__attribute__((musttail)) return x3266(std::monostate{});
return std::monostate{};
}
std::monostate x3053(std::monostate x3054) {
info("Entering the false branch 41 of the if");
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x3055 = Stack.pop();
SymStack.pop();
Num x3056 = I32V(Memory.loadInt(x3055.toInt(), 4));
SymVal x3057 = SymMemory.loadSym(x3055.toInt(), 4);
Stack.push(x3056);
SymStack.push(x3057);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x3058 = Stack.pop();
SymVal x3059 = SymStack.pop();
Num x3060 = Stack.pop();
SymVal x3061 = SymStack.pop();
Num x3062 = x3060.i32_add(x3058);
Stack.push(x3062);
bool x3063 = allConcrete(x3061, x3059);
SymVal x3064 = x3063 ? Concrete(x3062, 32) : x3061.add(x3059);
SymStack.push(x3064);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3065 = Stack.pop();
SymVal x3066 = SymStack.pop();
Num x3067 = Stack.pop();
SymVal x3068 = SymStack.pop();
Num x3069 = x3067.i32_add(x3065);
Stack.push(x3069);
bool x3070 = allConcrete(x3068, x3066);
SymVal x3071 = x3070 ? Concrete(x3069, 32) : x3068.add(x3066);
SymStack.push(x3071);
}
{
Num x3072 = Stack.pop();
SymVal x3073 = SymStack.pop();
Num x3074 = Stack.pop();
SymVal x3075 = SymStack.pop();
Num x3076 = x3074.i32_mul(x3072);
Stack.push(x3076);
bool x3077 = allConcrete(x3075, x3073);
SymVal x3078 = x3077 ? Concrete(x3076, 32) : x3075.mul(x3073);
SymStack.push(x3078);
}
{
Num x3079 = Stack.pop();
SymVal x3080 = SymStack.pop();
Num x3081 = Stack.pop();
SymVal x3082 = SymStack.pop();
Num x3083 = x3081.i32_add(x3079);
Stack.push(x3083);
bool x3084 = allConcrete(x3082, x3080);
SymVal x3085 = x3084 ? Concrete(x3083, 32) : x3082.add(x3080);
SymStack.push(x3085);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3086 = Stack.pop();
SymStack.pop();
Num x3087 = I32V(Memory.loadInt(x3086.toInt(), 0));
SymVal x3088 = SymMemory.loadSym(x3086.toInt(), 0);
Stack.push(x3087);
SymStack.push(x3088);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3089 = Stack.pop();
SymVal x3090 = SymStack.pop();
Num x3091 = Stack.pop();
SymVal x3092 = SymStack.pop();
Num x3093 = x3091.i32_sub(x3089);
Stack.push(x3093);
bool x3094 = allConcrete(x3092, x3090);
SymVal x3095 = x3094 ? Concrete(x3093, 32) : x3092.minus(x3090);
SymStack.push(x3095);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3096 = Stack.pop();
SymVal x3097 = SymStack.pop();
Num x3098 = Stack.pop();
SymVal x3099 = SymStack.pop();
Num x3100 = x3098.i32_mul(x3096);
Stack.push(x3100);
bool x3101 = allConcrete(x3099, x3097);
SymVal x3102 = x3101 ? Concrete(x3100, 32) : x3099.mul(x3097);
SymStack.push(x3102);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3103 = Stack.pop();
SymVal x3104 = SymStack.pop();
Num x3105 = Stack.pop();
SymVal x3106 = SymStack.pop();
Num x3107 = x3105.i32_add(x3103);
Stack.push(x3107);
bool x3108 = allConcrete(x3106, x3104);
SymVal x3109 = x3108 ? Concrete(x3107, 32) : x3106.add(x3104);
SymStack.push(x3109);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x3110 = Stack.pop();
SymVal x3111 = SymStack.pop();
Num x3112 = Stack.pop();
SymVal x3113 = SymStack.pop();
Num x3114 = x3112.i32_mul(x3110);
Stack.push(x3114);
bool x3115 = allConcrete(x3113, x3111);
SymVal x3116 = x3115 ? Concrete(x3114, 32) : x3113.mul(x3111);
SymStack.push(x3116);
}
{
Num x3117 = Stack.pop();
SymVal x3118 = SymStack.pop();
Num x3119 = Stack.pop();
SymVal x3120 = SymStack.pop();
Num x3121 = x3119.i32_add(x3117);
Stack.push(x3121);
bool x3122 = allConcrete(x3120, x3118);
SymVal x3123 = x3122 ? Concrete(x3121, 32) : x3120.add(x3118);
SymStack.push(x3123);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x3124 = Stack.pop();
SymVal x3125 = SymStack.pop();
Num x3126 = Stack.pop();
SymVal x3127 = SymStack.pop();
Num x3128 = x3126.i32_add(x3124);
Stack.push(x3128);
bool x3129 = allConcrete(x3127, x3125);
SymVal x3130 = x3129 ? Concrete(x3128, 32) : x3127.add(x3125);
SymStack.push(x3130);
}
{
Num x3131 = Stack.pop();
SymStack.pop();
Num x3132 = I32V(Memory.loadInt(x3131.toInt(), 8));
SymVal x3133 = SymMemory.loadSym(x3131.toInt(), 8);
Stack.push(x3132);
SymStack.push(x3133);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x3134 = Stack.pop();
SymVal x3135 = SymStack.pop();
Num x3136 = Stack.pop();
SymVal x3137 = SymStack.pop();
Num x3138 = x3136.i32_mul(x3134);
Stack.push(x3138);
bool x3139 = allConcrete(x3137, x3135);
SymVal x3140 = x3139 ? Concrete(x3138, 32) : x3137.mul(x3135);
SymStack.push(x3140);
}
{
Num x3141 = Stack.pop();
SymVal x3142 = SymStack.pop();
Num x3143 = Stack.pop();
SymVal x3144 = SymStack.pop();
Num x3145 = x3143.i32_add(x3141);
Stack.push(x3145);
bool x3146 = allConcrete(x3144, x3142);
SymVal x3147 = x3146 ? Concrete(x3145, 32) : x3144.add(x3142);
SymStack.push(x3147);
}
{
Num x3148 = Stack.pop();
SymStack.pop();
Num x3149 = I32V(Memory.loadInt(x3148.toInt(), 8));
SymVal x3150 = SymMemory.loadSym(x3148.toInt(), 8);
Stack.push(x3149);
SymStack.push(x3150);
}
{
Num x3151 = Stack.pop();
SymVal x3152 = SymStack.pop();
Num x3153 = Stack.pop();
SymStack.pop();
int x3154 = x3153.toInt();
Memory.storeInt(x3154, 8, x3151.toInt());
SymMemory.storeSym(x3154, 8, x3152);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3155 = Stack.pop();
SymVal x3156 = SymStack.pop();
Num x3157 = Stack.pop();
SymVal x3158 = SymStack.pop();
Num x3159 = x3157.i32_add(x3155);
Stack.push(x3159);
bool x3160 = allConcrete(x3158, x3156);
SymVal x3161 = x3160 ? Concrete(x3159, 32) : x3158.add(x3156);
SymStack.push(x3161);
}
{
Num x3162 = Stack.pop();
SymVal x3163 = SymStack.pop();
Frames.set(3, x3162);
SymFrames.set(3, x3163);
}
info("Jump to 1");
__attribute__((musttail)) return x3164(std::monostate{});
return std::monostate{};
}
std::monostate x3051(std::monostate x3052) {
info("Entering the false branch 34 of the if");
__attribute__((musttail)) return x3018(std::monostate{});
return std::monostate{};
}
std::monostate x3018(std::monostate x3019) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(Frames.get(4));
SymStack.push(SymFrames.get(4));
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x3020 = Stack.pop();
SymVal x3021 = SymStack.pop();
Num x3022 = Stack.pop();
SymVal x3023 = SymStack.pop();
Num x3024 = x3022.i32_eq(x3020);
Stack.push(x3024);
bool x3025 = allConcrete(x3023, x3021);
SymVal x3026 = x3025 ? Concrete(x3024, 32) : x3023.eq(x3021).bool2bv();
SymStack.push(x3026);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x3027 = Stack.pop();
SymVal x3028 = SymStack.pop();
Num x3029 = Stack.pop();
SymVal x3030 = SymStack.pop();
Num x3031 = x3029.i32_sub(x3027);
Stack.push(x3031);
bool x3032 = allConcrete(x3030, x3028);
SymVal x3033 = x3032 ? Concrete(x3031, 32) : x3030.minus(x3028);
SymStack.push(x3033);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3034 = Stack.pop();
SymVal x3035 = SymStack.pop();
Num x3036 = Stack.pop();
SymVal x3037 = SymStack.pop();
Num x3038 = x3036.i32_ge_s(x3034);
Stack.push(x3038);
bool x3039 = allConcrete(x3037, x3035);
SymVal x3040 = x3039 ? Concrete(x3038, 32) : x3037.ge(x3035).bool2bv();
SymStack.push(x3040);
}
{
Num x3041 = Stack.pop();
SymVal x3042 = SymStack.pop();
Num x3043 = Stack.pop();
SymVal x3044 = SymStack.pop();
Num x3045 = x3043.i32_and(x3041);
Stack.push(x3045);
bool x3046 = allConcrete(x3044, x3042);
SymVal x3047 = x3046 ? Concrete(x3045, 32) : x3044.bitwise_and(x3042);
SymStack.push(x3047);
}
Num x3048 = Stack.pop();
{
SymVal x3049 = SymStack.pop();
ExploreTree.fillIfElseNode(x3049, 35);
}
int x3050 = x3048.toInt();
if (x3050 != 0) {
ExploreTree.moveCursor(true, makeControl(x2087, CURRENT_MCONT));
__attribute__((musttail)) return x2873(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x2873, CURRENT_MCONT));
__attribute__((musttail)) return x2087(std::monostate{});
}
return std::monostate{};
}
std::monostate x2873(std::monostate x2874) {
info("Entering the true branch 35 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x2875 = Stack.pop();
SymStack.pop();
Num x2876 = I32V(Memory.loadInt(x2875.toInt(), 0));
SymVal x2877 = SymMemory.loadSym(x2875.toInt(), 0);
Stack.push(x2876);
SymStack.push(x2877);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2878 = Stack.pop();
SymVal x2879 = SymStack.pop();
Num x2880 = Stack.pop();
SymVal x2881 = SymStack.pop();
Num x2882 = x2880.i32_sub(x2878);
Stack.push(x2882);
bool x2883 = allConcrete(x2881, x2879);
SymVal x2884 = x2883 ? Concrete(x2882, 32) : x2881.minus(x2879);
SymStack.push(x2884);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2885 = Stack.pop();
SymVal x2886 = SymStack.pop();
Num x2887 = Stack.pop();
SymVal x2888 = SymStack.pop();
Num x2889 = x2887.i32_mul(x2885);
Stack.push(x2889);
bool x2890 = allConcrete(x2888, x2886);
SymVal x2891 = x2890 ? Concrete(x2889, 32) : x2888.mul(x2886);
SymStack.push(x2891);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2892 = Stack.pop();
SymVal x2893 = SymStack.pop();
Num x2894 = Stack.pop();
SymVal x2895 = SymStack.pop();
Num x2896 = x2894.i32_sub(x2892);
Stack.push(x2896);
bool x2897 = allConcrete(x2895, x2893);
SymVal x2898 = x2897 ? Concrete(x2896, 32) : x2895.minus(x2893);
SymStack.push(x2898);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2899 = Stack.pop();
SymVal x2900 = SymStack.pop();
Num x2901 = Stack.pop();
SymVal x2902 = SymStack.pop();
Num x2903 = x2901.i32_mul(x2899);
Stack.push(x2903);
bool x2904 = allConcrete(x2902, x2900);
SymVal x2905 = x2904 ? Concrete(x2903, 32) : x2902.mul(x2900);
SymStack.push(x2905);
}
{
Num x2906 = Stack.pop();
SymVal x2907 = SymStack.pop();
Num x2908 = Stack.pop();
SymVal x2909 = SymStack.pop();
Num x2910 = x2908.i32_add(x2906);
Stack.push(x2910);
bool x2911 = allConcrete(x2909, x2907);
SymVal x2912 = x2911 ? Concrete(x2910, 32) : x2909.add(x2907);
SymStack.push(x2912);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x2913 = Stack.pop();
SymVal x2914 = SymStack.pop();
Num x2915 = Stack.pop();
SymVal x2916 = SymStack.pop();
Num x2917 = x2915.i32_add(x2913);
Stack.push(x2917);
bool x2918 = allConcrete(x2916, x2914);
SymVal x2919 = x2918 ? Concrete(x2917, 32) : x2916.add(x2914);
SymStack.push(x2919);
}
{
Num x2920 = Stack.pop();
SymStack.pop();
Num x2921 = I32V(Memory.loadInt(x2920.toInt(), 8));
SymVal x2922 = SymMemory.loadSym(x2920.toInt(), 8);
Stack.push(x2921);
SymStack.push(x2922);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x2923 = Stack.pop();
SymStack.pop();
Num x2924 = I32V(Memory.loadInt(x2923.toInt(), 0));
SymVal x2925 = SymMemory.loadSym(x2923.toInt(), 0);
Stack.push(x2924);
SymStack.push(x2925);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2926 = Stack.pop();
SymVal x2927 = SymStack.pop();
Num x2928 = Stack.pop();
SymVal x2929 = SymStack.pop();
Num x2930 = x2928.i32_sub(x2926);
Stack.push(x2930);
bool x2931 = allConcrete(x2929, x2927);
SymVal x2932 = x2931 ? Concrete(x2930, 32) : x2929.minus(x2927);
SymStack.push(x2932);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2933 = Stack.pop();
SymVal x2934 = SymStack.pop();
Num x2935 = Stack.pop();
SymVal x2936 = SymStack.pop();
Num x2937 = x2935.i32_mul(x2933);
Stack.push(x2937);
bool x2938 = allConcrete(x2936, x2934);
SymVal x2939 = x2938 ? Concrete(x2937, 32) : x2936.mul(x2934);
SymStack.push(x2939);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2940 = Stack.pop();
SymVal x2941 = SymStack.pop();
Num x2942 = Stack.pop();
SymVal x2943 = SymStack.pop();
Num x2944 = x2942.i32_sub(x2940);
Stack.push(x2944);
bool x2945 = allConcrete(x2943, x2941);
SymVal x2946 = x2945 ? Concrete(x2944, 32) : x2943.minus(x2941);
SymStack.push(x2946);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2947 = Stack.pop();
SymVal x2948 = SymStack.pop();
Num x2949 = Stack.pop();
SymVal x2950 = SymStack.pop();
Num x2951 = x2949.i32_mul(x2947);
Stack.push(x2951);
bool x2952 = allConcrete(x2950, x2948);
SymVal x2953 = x2952 ? Concrete(x2951, 32) : x2950.mul(x2948);
SymStack.push(x2953);
}
{
Num x2954 = Stack.pop();
SymVal x2955 = SymStack.pop();
Num x2956 = Stack.pop();
SymVal x2957 = SymStack.pop();
Num x2958 = x2956.i32_add(x2954);
Stack.push(x2958);
bool x2959 = allConcrete(x2957, x2955);
SymVal x2960 = x2959 ? Concrete(x2958, 32) : x2957.add(x2955);
SymStack.push(x2960);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x2961 = Stack.pop();
SymVal x2962 = SymStack.pop();
Num x2963 = Stack.pop();
SymVal x2964 = SymStack.pop();
Num x2965 = x2963.i32_add(x2961);
Stack.push(x2965);
bool x2966 = allConcrete(x2964, x2962);
SymVal x2967 = x2966 ? Concrete(x2965, 32) : x2964.add(x2962);
SymStack.push(x2967);
}
{
Num x2968 = Stack.pop();
SymStack.pop();
Num x2969 = I32V(Memory.loadInt(x2968.toInt(), 8));
SymVal x2970 = SymMemory.loadSym(x2968.toInt(), 8);
Stack.push(x2969);
SymStack.push(x2970);
}
{
Num x2971 = Stack.pop();
SymStack.pop();
Num x2972 = I32V(Memory.loadInt(x2971.toInt(), 4));
SymVal x2973 = SymMemory.loadSym(x2971.toInt(), 4);
Stack.push(x2972);
SymStack.push(x2973);
}
{
Num x2974 = Stack.pop();
SymVal x2975 = SymStack.pop();
Num x2976 = Stack.pop();
SymVal x2977 = SymStack.pop();
Num x2978 = x2976.i32_mul(x2974);
Stack.push(x2978);
bool x2979 = allConcrete(x2977, x2975);
SymVal x2980 = x2979 ? Concrete(x2978, 32) : x2977.mul(x2975);
SymStack.push(x2980);
}
{
Num x2981 = Stack.pop();
SymVal x2982 = SymStack.pop();
Num x2983 = Stack.pop();
SymVal x2984 = SymStack.pop();
Num x2985 = x2983.i32_add(x2981);
Stack.push(x2985);
bool x2986 = allConcrete(x2984, x2982);
SymVal x2987 = x2986 ? Concrete(x2985, 32) : x2984.add(x2982);
SymStack.push(x2987);
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
Num x2988 = Stack.pop();
SymVal x2989 = SymStack.pop();
Num x2990 = Stack.pop();
SymVal x2991 = SymStack.pop();
Num x2992 = x2990.i32_sub(x2988);
Stack.push(x2992);
bool x2993 = allConcrete(x2991, x2989);
SymVal x2994 = x2993 ? Concrete(x2992, 32) : x2991.minus(x2989);
SymStack.push(x2994);
}
{
Num x2995 = Stack.pop();
SymVal x2996 = SymStack.pop();
Num x2997 = Stack.pop();
SymVal x2998 = SymStack.pop();
Num x2999 = x2997.i32_mul(x2995);
Stack.push(x2999);
bool x3000 = allConcrete(x2998, x2996);
SymVal x3001 = x3000 ? Concrete(x2999, 32) : x2998.mul(x2996);
SymStack.push(x3001);
}
{
Num x3002 = Stack.pop();
SymVal x3003 = SymStack.pop();
Num x3004 = Stack.pop();
SymVal x3005 = SymStack.pop();
Num x3006 = x3004.i32_add(x3002);
Stack.push(x3006);
bool x3007 = allConcrete(x3005, x3003);
SymVal x3008 = x3007 ? Concrete(x3006, 32) : x3005.add(x3003);
SymStack.push(x3008);
}
{
Num x3009 = Stack.pop();
SymStack.pop();
Num x3010 = I32V(Memory.loadInt(x3009.toInt(), 8));
SymVal x3011 = SymMemory.loadSym(x3009.toInt(), 8);
Stack.push(x3010);
SymStack.push(x3011);
}
{
Num x3012 = Stack.pop();
SymVal x3013 = SymStack.pop();
Num x3014 = Stack.pop();
SymStack.pop();
int x3015 = x3014.toInt();
Memory.storeInt(x3015, 8, x3012.toInt());
SymMemory.storeSym(x3015, 8, x3013);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x3016 = Stack.pop();
SymVal x3017 = SymStack.pop();
Frames.set(3, x3016);
SymFrames.set(3, x3017);
}
__attribute__((musttail)) return x2871(std::monostate{});
return std::monostate{};
}
std::monostate x2871(std::monostate x2872) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x2248(std::monostate{});
return std::monostate{};
}
std::monostate x2248(std::monostate x2857) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x2858 = Stack.pop();
SymStack.pop();
Num x2859 = I32V(Memory.loadInt(x2858.toInt(), 4));
SymVal x2860 = SymMemory.loadSym(x2858.toInt(), 4);
Stack.push(x2859);
SymStack.push(x2860);
}
{
Num x2861 = Stack.pop();
SymVal x2862 = SymStack.pop();
Num x2863 = Stack.pop();
SymVal x2864 = SymStack.pop();
Num x2865 = x2863.i32_eq(x2861);
Stack.push(x2865);
bool x2866 = allConcrete(x2864, x2862);
SymVal x2867 = x2866 ? Concrete(x2865, 32) : x2864.eq(x2862).bool2bv();
SymStack.push(x2867);
}
Num x2868 = Stack.pop();
{
SymVal x2869 = SymStack.pop();
ExploreTree.fillIfElseNode(x2869, 36);
}
int x2870 = x2868.toInt();
if (x2870 != 0) {
ExploreTree.moveCursor(true, makeControl(x2089, CURRENT_MCONT));
__attribute__((musttail)) return x2855(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x2855, CURRENT_MCONT));
__attribute__((musttail)) return x2089(std::monostate{});
}
return std::monostate{};
}
std::monostate x2855(std::monostate x2856) {
info("Entering the true branch 36 of the if");
info("Jump to 2");
__attribute__((musttail)) return x2840(std::monostate{});
return std::monostate{};
}
std::monostate x2840(std::monostate x2841) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x2842 = Stack.pop();
SymStack.pop();
Num x2843 = I32V(Memory.loadInt(x2842.toInt(), 0));
SymVal x2844 = SymMemory.loadSym(x2842.toInt(), 0);
Stack.push(x2843);
SymStack.push(x2844);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2845 = Stack.pop();
SymVal x2846 = SymStack.pop();
Num x2847 = Stack.pop();
SymVal x2848 = SymStack.pop();
Num x2849 = x2847.i32_ne(x2845);
Stack.push(x2849);
bool x2850 = allConcrete(x2848, x2846);
SymVal x2851 = x2850 ? Concrete(x2849, 32) : x2848.neq(x2846).bool2bv();
SymStack.push(x2851);
}
Num x2852 = Stack.pop();
{
SymVal x2853 = SymStack.pop();
ExploreTree.fillIfElseNode(x2853, 37);
}
int x2854 = x2852.toInt();
if (x2854 != 0) {
ExploreTree.moveCursor(true, makeControl(x2599, CURRENT_MCONT));
__attribute__((musttail)) return x2836(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x2836, CURRENT_MCONT));
__attribute__((musttail)) return x2599(std::monostate{});
}
return std::monostate{};
}
std::monostate x2836(std::monostate x2837) {
info("Entering the true branch 37 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x2838 = Stack.pop();
SymVal x2839 = SymStack.pop();
Frames.set(3, x2838);
SymFrames.set(3, x2839);
}
__attribute__((musttail)) return x2834(std::monostate{});
return std::monostate{};
}
std::monostate x2834(std::monostate x2835) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x2808(std::monostate{});
return std::monostate{};
}
std::monostate x2808(std::monostate x2813) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x2814 = Stack.pop();
SymStack.pop();
Num x2815 = I32V(Memory.loadInt(x2814.toInt(), 4));
SymVal x2816 = SymMemory.loadSym(x2814.toInt(), 4);
Stack.push(x2815);
SymStack.push(x2816);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2817 = Stack.pop();
SymVal x2818 = SymStack.pop();
Num x2819 = Stack.pop();
SymVal x2820 = SymStack.pop();
Num x2821 = x2819.i32_add(x2817);
Stack.push(x2821);
bool x2822 = allConcrete(x2820, x2818);
SymVal x2823 = x2822 ? Concrete(x2821, 32) : x2820.add(x2818);
SymStack.push(x2823);
}
{
Num x2824 = Stack.pop();
SymVal x2825 = SymStack.pop();
Num x2826 = Stack.pop();
SymVal x2827 = SymStack.pop();
Num x2828 = x2826.i32_eq(x2824);
Stack.push(x2828);
bool x2829 = allConcrete(x2827, x2825);
SymVal x2830 = x2829 ? Concrete(x2828, 32) : x2827.eq(x2825).bool2bv();
SymStack.push(x2830);
}
Num x2831 = Stack.pop();
{
SymVal x2832 = SymStack.pop();
ExploreTree.fillIfElseNode(x2832, 40);
}
int x2833 = x2831.toInt();
if (x2833 != 0) {
ExploreTree.moveCursor(true, makeControl(x2601, CURRENT_MCONT));
__attribute__((musttail)) return x2811(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x2811, CURRENT_MCONT));
__attribute__((musttail)) return x2601(std::monostate{});
}
return std::monostate{};
}
std::monostate x2811(std::monostate x2812) {
info("Entering the true branch 40 of the if");
info("Jump to 2");
__attribute__((musttail)) return x2809(std::monostate{});
return std::monostate{};
}
std::monostate x2809(std::monostate x2810) {
info("Exiting the block, stackSize =", Stack.size());
__attribute__((musttail)) return x2475(std::monostate{});
return std::monostate{};
}
std::monostate x2601(std::monostate x2602) {
info("Entering the false branch 40 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x2603 = Stack.pop();
SymStack.pop();
Num x2604 = I32V(Memory.loadInt(x2603.toInt(), 0));
SymVal x2605 = SymMemory.loadSym(x2603.toInt(), 0);
Stack.push(x2604);
SymStack.push(x2605);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2606 = Stack.pop();
SymVal x2607 = SymStack.pop();
Num x2608 = Stack.pop();
SymVal x2609 = SymStack.pop();
Num x2610 = x2608.i32_sub(x2606);
Stack.push(x2610);
bool x2611 = allConcrete(x2609, x2607);
SymVal x2612 = x2611 ? Concrete(x2610, 32) : x2609.minus(x2607);
SymStack.push(x2612);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2613 = Stack.pop();
SymVal x2614 = SymStack.pop();
Num x2615 = Stack.pop();
SymVal x2616 = SymStack.pop();
Num x2617 = x2615.i32_mul(x2613);
Stack.push(x2617);
bool x2618 = allConcrete(x2616, x2614);
SymVal x2619 = x2618 ? Concrete(x2617, 32) : x2616.mul(x2614);
SymStack.push(x2619);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x2620 = Stack.pop();
SymStack.pop();
Num x2621 = I32V(Memory.loadInt(x2620.toInt(), 0));
SymVal x2622 = SymMemory.loadSym(x2620.toInt(), 0);
Stack.push(x2621);
SymStack.push(x2622);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2623 = Stack.pop();
SymVal x2624 = SymStack.pop();
Num x2625 = Stack.pop();
SymVal x2626 = SymStack.pop();
Num x2627 = x2625.i32_sub(x2623);
Stack.push(x2627);
bool x2628 = allConcrete(x2626, x2624);
SymVal x2629 = x2628 ? Concrete(x2627, 32) : x2626.minus(x2624);
SymStack.push(x2629);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2630 = Stack.pop();
SymVal x2631 = SymStack.pop();
Num x2632 = Stack.pop();
SymVal x2633 = SymStack.pop();
Num x2634 = x2632.i32_mul(x2630);
Stack.push(x2634);
bool x2635 = allConcrete(x2633, x2631);
SymVal x2636 = x2635 ? Concrete(x2634, 32) : x2633.mul(x2631);
SymStack.push(x2636);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2637 = Stack.pop();
SymVal x2638 = SymStack.pop();
Num x2639 = Stack.pop();
SymVal x2640 = SymStack.pop();
Num x2641 = x2639.i32_sub(x2637);
Stack.push(x2641);
bool x2642 = allConcrete(x2640, x2638);
SymVal x2643 = x2642 ? Concrete(x2641, 32) : x2640.minus(x2638);
SymStack.push(x2643);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2644 = Stack.pop();
SymVal x2645 = SymStack.pop();
Num x2646 = Stack.pop();
SymVal x2647 = SymStack.pop();
Num x2648 = x2646.i32_mul(x2644);
Stack.push(x2648);
bool x2649 = allConcrete(x2647, x2645);
SymVal x2650 = x2649 ? Concrete(x2648, 32) : x2647.mul(x2645);
SymStack.push(x2650);
}
{
Num x2651 = Stack.pop();
SymVal x2652 = SymStack.pop();
Num x2653 = Stack.pop();
SymVal x2654 = SymStack.pop();
Num x2655 = x2653.i32_add(x2651);
Stack.push(x2655);
bool x2656 = allConcrete(x2654, x2652);
SymVal x2657 = x2656 ? Concrete(x2655, 32) : x2654.add(x2652);
SymStack.push(x2657);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x2658 = Stack.pop();
SymVal x2659 = SymStack.pop();
Num x2660 = Stack.pop();
SymVal x2661 = SymStack.pop();
Num x2662 = x2660.i32_add(x2658);
Stack.push(x2662);
bool x2663 = allConcrete(x2661, x2659);
SymVal x2664 = x2663 ? Concrete(x2662, 32) : x2661.add(x2659);
SymStack.push(x2664);
}
{
Num x2665 = Stack.pop();
SymStack.pop();
Num x2666 = I32V(Memory.loadInt(x2665.toInt(), 8));
SymVal x2667 = SymMemory.loadSym(x2665.toInt(), 8);
Stack.push(x2666);
SymStack.push(x2667);
}
{
Num x2668 = Stack.pop();
SymStack.pop();
Num x2669 = I32V(Memory.loadInt(x2668.toInt(), 4));
SymVal x2670 = SymMemory.loadSym(x2668.toInt(), 4);
Stack.push(x2669);
SymStack.push(x2670);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x2671 = Stack.pop();
SymVal x2672 = SymStack.pop();
Num x2673 = Stack.pop();
SymVal x2674 = SymStack.pop();
Num x2675 = x2673.i32_add(x2671);
Stack.push(x2675);
bool x2676 = allConcrete(x2674, x2672);
SymVal x2677 = x2676 ? Concrete(x2675, 32) : x2674.add(x2672);
SymStack.push(x2677);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2678 = Stack.pop();
SymVal x2679 = SymStack.pop();
Num x2680 = Stack.pop();
SymVal x2681 = SymStack.pop();
Num x2682 = x2680.i32_add(x2678);
Stack.push(x2682);
bool x2683 = allConcrete(x2681, x2679);
SymVal x2684 = x2683 ? Concrete(x2682, 32) : x2681.add(x2679);
SymStack.push(x2684);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2685 = Stack.pop();
SymVal x2686 = SymStack.pop();
Num x2687 = Stack.pop();
SymVal x2688 = SymStack.pop();
Num x2689 = x2687.i32_mul(x2685);
Stack.push(x2689);
bool x2690 = allConcrete(x2688, x2686);
SymVal x2691 = x2690 ? Concrete(x2689, 32) : x2688.mul(x2686);
SymStack.push(x2691);
}
{
Num x2692 = Stack.pop();
SymVal x2693 = SymStack.pop();
Num x2694 = Stack.pop();
SymVal x2695 = SymStack.pop();
Num x2696 = x2694.i32_add(x2692);
Stack.push(x2696);
bool x2697 = allConcrete(x2695, x2693);
SymVal x2698 = x2697 ? Concrete(x2696, 32) : x2695.add(x2693);
SymStack.push(x2698);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x2699 = Stack.pop();
SymStack.pop();
Num x2700 = I32V(Memory.loadInt(x2699.toInt(), 0));
SymVal x2701 = SymMemory.loadSym(x2699.toInt(), 0);
Stack.push(x2700);
SymStack.push(x2701);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2702 = Stack.pop();
SymVal x2703 = SymStack.pop();
Num x2704 = Stack.pop();
SymVal x2705 = SymStack.pop();
Num x2706 = x2704.i32_sub(x2702);
Stack.push(x2706);
bool x2707 = allConcrete(x2705, x2703);
SymVal x2708 = x2707 ? Concrete(x2706, 32) : x2705.minus(x2703);
SymStack.push(x2708);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2709 = Stack.pop();
SymVal x2710 = SymStack.pop();
Num x2711 = Stack.pop();
SymVal x2712 = SymStack.pop();
Num x2713 = x2711.i32_mul(x2709);
Stack.push(x2713);
bool x2714 = allConcrete(x2712, x2710);
SymVal x2715 = x2714 ? Concrete(x2713, 32) : x2712.mul(x2710);
SymStack.push(x2715);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2716 = Stack.pop();
SymVal x2717 = SymStack.pop();
Num x2718 = Stack.pop();
SymVal x2719 = SymStack.pop();
Num x2720 = x2718.i32_sub(x2716);
Stack.push(x2720);
bool x2721 = allConcrete(x2719, x2717);
SymVal x2722 = x2721 ? Concrete(x2720, 32) : x2719.minus(x2717);
SymStack.push(x2722);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2723 = Stack.pop();
SymVal x2724 = SymStack.pop();
Num x2725 = Stack.pop();
SymVal x2726 = SymStack.pop();
Num x2727 = x2725.i32_mul(x2723);
Stack.push(x2727);
bool x2728 = allConcrete(x2726, x2724);
SymVal x2729 = x2728 ? Concrete(x2727, 32) : x2726.mul(x2724);
SymStack.push(x2729);
}
{
Num x2730 = Stack.pop();
SymVal x2731 = SymStack.pop();
Num x2732 = Stack.pop();
SymVal x2733 = SymStack.pop();
Num x2734 = x2732.i32_add(x2730);
Stack.push(x2734);
bool x2735 = allConcrete(x2733, x2731);
SymVal x2736 = x2735 ? Concrete(x2734, 32) : x2733.add(x2731);
SymStack.push(x2736);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x2737 = Stack.pop();
SymVal x2738 = SymStack.pop();
Num x2739 = Stack.pop();
SymVal x2740 = SymStack.pop();
Num x2741 = x2739.i32_add(x2737);
Stack.push(x2741);
bool x2742 = allConcrete(x2740, x2738);
SymVal x2743 = x2742 ? Concrete(x2741, 32) : x2740.add(x2738);
SymStack.push(x2743);
}
{
Num x2744 = Stack.pop();
SymStack.pop();
Num x2745 = I32V(Memory.loadInt(x2744.toInt(), 8));
SymVal x2746 = SymMemory.loadSym(x2744.toInt(), 8);
Stack.push(x2745);
SymStack.push(x2746);
}
{
Num x2747 = Stack.pop();
SymVal x2748 = SymStack.pop();
Num x2749 = Stack.pop();
SymVal x2750 = SymStack.pop();
Num x2751 = x2749.i32_add(x2747);
Stack.push(x2751);
bool x2752 = allConcrete(x2750, x2748);
SymVal x2753 = x2752 ? Concrete(x2751, 32) : x2750.add(x2748);
SymStack.push(x2753);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x2754 = Stack.pop();
SymStack.pop();
Num x2755 = I32V(Memory.loadInt(x2754.toInt(), 0));
SymVal x2756 = SymMemory.loadSym(x2754.toInt(), 0);
Stack.push(x2755);
SymStack.push(x2756);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2757 = Stack.pop();
SymVal x2758 = SymStack.pop();
Num x2759 = Stack.pop();
SymVal x2760 = SymStack.pop();
Num x2761 = x2759.i32_sub(x2757);
Stack.push(x2761);
bool x2762 = allConcrete(x2760, x2758);
SymVal x2763 = x2762 ? Concrete(x2761, 32) : x2760.minus(x2758);
SymStack.push(x2763);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2764 = Stack.pop();
SymVal x2765 = SymStack.pop();
Num x2766 = Stack.pop();
SymVal x2767 = SymStack.pop();
Num x2768 = x2766.i32_mul(x2764);
Stack.push(x2768);
bool x2769 = allConcrete(x2767, x2765);
SymVal x2770 = x2769 ? Concrete(x2768, 32) : x2767.mul(x2765);
SymStack.push(x2770);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2771 = Stack.pop();
SymVal x2772 = SymStack.pop();
Num x2773 = Stack.pop();
SymVal x2774 = SymStack.pop();
Num x2775 = x2773.i32_mul(x2771);
Stack.push(x2775);
bool x2776 = allConcrete(x2774, x2772);
SymVal x2777 = x2776 ? Concrete(x2775, 32) : x2774.mul(x2772);
SymStack.push(x2777);
}
{
Num x2778 = Stack.pop();
SymVal x2779 = SymStack.pop();
Num x2780 = Stack.pop();
SymVal x2781 = SymStack.pop();
Num x2782 = x2780.i32_add(x2778);
Stack.push(x2782);
bool x2783 = allConcrete(x2781, x2779);
SymVal x2784 = x2783 ? Concrete(x2782, 32) : x2781.add(x2779);
SymStack.push(x2784);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x2785 = Stack.pop();
SymVal x2786 = SymStack.pop();
Num x2787 = Stack.pop();
SymVal x2788 = SymStack.pop();
Num x2789 = x2787.i32_add(x2785);
Stack.push(x2789);
bool x2790 = allConcrete(x2788, x2786);
SymVal x2791 = x2790 ? Concrete(x2789, 32) : x2788.add(x2786);
SymStack.push(x2791);
}
{
Num x2792 = Stack.pop();
SymStack.pop();
Num x2793 = I32V(Memory.loadInt(x2792.toInt(), 8));
SymVal x2794 = SymMemory.loadSym(x2792.toInt(), 8);
Stack.push(x2793);
SymStack.push(x2794);
}
{
Num x2795 = Stack.pop();
SymVal x2796 = SymStack.pop();
Num x2797 = Stack.pop();
SymStack.pop();
int x2798 = x2797.toInt();
Memory.storeInt(x2798, 8, x2795.toInt());
SymMemory.storeSym(x2798, 8, x2796);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
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
Frames.set(3, x2806);
SymFrames.set(3, x2807);
}
info("Jump to 1");
__attribute__((musttail)) return x2808(std::monostate{});
return std::monostate{};
}
std::monostate x2599(std::monostate x2600) {
info("Entering the false branch 37 of the if");
__attribute__((musttail)) return x2475(std::monostate{});
return std::monostate{};
}
std::monostate x2475(std::monostate x2476) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x2477 = Stack.pop();
SymStack.pop();
Num x2478 = I32V(Memory.loadInt(x2477.toInt(), 0));
SymVal x2479 = SymMemory.loadSym(x2477.toInt(), 0);
Stack.push(x2478);
SymStack.push(x2479);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2480 = Stack.pop();
SymVal x2481 = SymStack.pop();
Num x2482 = Stack.pop();
SymVal x2483 = SymStack.pop();
Num x2484 = x2482.i32_sub(x2480);
Stack.push(x2484);
bool x2485 = allConcrete(x2483, x2481);
SymVal x2486 = x2485 ? Concrete(x2484, 32) : x2483.minus(x2481);
SymStack.push(x2486);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2487 = Stack.pop();
SymVal x2488 = SymStack.pop();
Num x2489 = Stack.pop();
SymVal x2490 = SymStack.pop();
Num x2491 = x2489.i32_mul(x2487);
Stack.push(x2491);
bool x2492 = allConcrete(x2490, x2488);
SymVal x2493 = x2492 ? Concrete(x2491, 32) : x2490.mul(x2488);
SymStack.push(x2493);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2494 = Stack.pop();
SymVal x2495 = SymStack.pop();
Num x2496 = Stack.pop();
SymVal x2497 = SymStack.pop();
Num x2498 = x2496.i32_sub(x2494);
Stack.push(x2498);
bool x2499 = allConcrete(x2497, x2495);
SymVal x2500 = x2499 ? Concrete(x2498, 32) : x2497.minus(x2495);
SymStack.push(x2500);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2501 = Stack.pop();
SymVal x2502 = SymStack.pop();
Num x2503 = Stack.pop();
SymVal x2504 = SymStack.pop();
Num x2505 = x2503.i32_mul(x2501);
Stack.push(x2505);
bool x2506 = allConcrete(x2504, x2502);
SymVal x2507 = x2506 ? Concrete(x2505, 32) : x2504.mul(x2502);
SymStack.push(x2507);
}
{
Num x2508 = Stack.pop();
SymVal x2509 = SymStack.pop();
Num x2510 = Stack.pop();
SymVal x2511 = SymStack.pop();
Num x2512 = x2510.i32_add(x2508);
Stack.push(x2512);
bool x2513 = allConcrete(x2511, x2509);
SymVal x2514 = x2513 ? Concrete(x2512, 32) : x2511.add(x2509);
SymStack.push(x2514);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x2515 = Stack.pop();
SymVal x2516 = SymStack.pop();
Num x2517 = Stack.pop();
SymVal x2518 = SymStack.pop();
Num x2519 = x2517.i32_add(x2515);
Stack.push(x2519);
bool x2520 = allConcrete(x2518, x2516);
SymVal x2521 = x2520 ? Concrete(x2519, 32) : x2518.add(x2516);
SymStack.push(x2521);
}
{
Num x2522 = Stack.pop();
SymStack.pop();
Num x2523 = I32V(Memory.loadInt(x2522.toInt(), 8));
SymVal x2524 = SymMemory.loadSym(x2522.toInt(), 8);
Stack.push(x2523);
SymStack.push(x2524);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x2525 = Stack.pop();
SymStack.pop();
Num x2526 = I32V(Memory.loadInt(x2525.toInt(), 0));
SymVal x2527 = SymMemory.loadSym(x2525.toInt(), 0);
Stack.push(x2526);
SymStack.push(x2527);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2528 = Stack.pop();
SymVal x2529 = SymStack.pop();
Num x2530 = Stack.pop();
SymVal x2531 = SymStack.pop();
Num x2532 = x2530.i32_sub(x2528);
Stack.push(x2532);
bool x2533 = allConcrete(x2531, x2529);
SymVal x2534 = x2533 ? Concrete(x2532, 32) : x2531.minus(x2529);
SymStack.push(x2534);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2535 = Stack.pop();
SymVal x2536 = SymStack.pop();
Num x2537 = Stack.pop();
SymVal x2538 = SymStack.pop();
Num x2539 = x2537.i32_mul(x2535);
Stack.push(x2539);
bool x2540 = allConcrete(x2538, x2536);
SymVal x2541 = x2540 ? Concrete(x2539, 32) : x2538.mul(x2536);
SymStack.push(x2541);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2542 = Stack.pop();
SymVal x2543 = SymStack.pop();
Num x2544 = Stack.pop();
SymVal x2545 = SymStack.pop();
Num x2546 = x2544.i32_sub(x2542);
Stack.push(x2546);
bool x2547 = allConcrete(x2545, x2543);
SymVal x2548 = x2547 ? Concrete(x2546, 32) : x2545.minus(x2543);
SymStack.push(x2548);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2549 = Stack.pop();
SymVal x2550 = SymStack.pop();
Num x2551 = Stack.pop();
SymVal x2552 = SymStack.pop();
Num x2553 = x2551.i32_mul(x2549);
Stack.push(x2553);
bool x2554 = allConcrete(x2552, x2550);
SymVal x2555 = x2554 ? Concrete(x2553, 32) : x2552.mul(x2550);
SymStack.push(x2555);
}
{
Num x2556 = Stack.pop();
SymVal x2557 = SymStack.pop();
Num x2558 = Stack.pop();
SymVal x2559 = SymStack.pop();
Num x2560 = x2558.i32_add(x2556);
Stack.push(x2560);
bool x2561 = allConcrete(x2559, x2557);
SymVal x2562 = x2561 ? Concrete(x2560, 32) : x2559.add(x2557);
SymStack.push(x2562);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x2563 = Stack.pop();
SymVal x2564 = SymStack.pop();
Num x2565 = Stack.pop();
SymVal x2566 = SymStack.pop();
Num x2567 = x2565.i32_add(x2563);
Stack.push(x2567);
bool x2568 = allConcrete(x2566, x2564);
SymVal x2569 = x2568 ? Concrete(x2567, 32) : x2566.add(x2564);
SymStack.push(x2569);
}
{
Num x2570 = Stack.pop();
SymStack.pop();
Num x2571 = I32V(Memory.loadInt(x2570.toInt(), 8));
SymVal x2572 = SymMemory.loadSym(x2570.toInt(), 8);
Stack.push(x2571);
SymStack.push(x2572);
}
{
Num x2573 = Stack.pop();
SymStack.pop();
Num x2574 = I32V(Memory.loadInt(x2573.toInt(), 4));
SymVal x2575 = SymMemory.loadSym(x2573.toInt(), 4);
Stack.push(x2574);
SymStack.push(x2575);
}
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
{
Num x2576 = Stack.pop();
SymStack.pop();
Num x2577 = I32V(Memory.loadInt(x2576.toInt(), 4));
SymVal x2578 = SymMemory.loadSym(x2576.toInt(), 4);
Stack.push(x2577);
SymStack.push(x2578);
}
{
Num x2579 = Stack.pop();
SymVal x2580 = SymStack.pop();
Num x2581 = Stack.pop();
SymVal x2582 = SymStack.pop();
Num x2583 = x2581.i32_add(x2579);
Stack.push(x2583);
bool x2584 = allConcrete(x2582, x2580);
SymVal x2585 = x2584 ? Concrete(x2583, 32) : x2582.add(x2580);
SymStack.push(x2585);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2586 = Stack.pop();
SymVal x2587 = SymStack.pop();
Num x2588 = Stack.pop();
SymVal x2589 = SymStack.pop();
Num x2590 = x2588.i32_add(x2586);
Stack.push(x2590);
bool x2591 = allConcrete(x2589, x2587);
SymVal x2592 = x2591 ? Concrete(x2590, 32) : x2589.add(x2587);
SymStack.push(x2592);
}
{
Num x2593 = Stack.pop();
SymVal x2594 = SymStack.pop();
Num x2595 = Stack.pop();
SymStack.pop();
int x2596 = x2595.toInt();
Memory.storeInt(x2596, 4, x2593.toInt());
SymMemory.storeSym(x2596, 4, x2594);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x2597 = Stack.pop();
SymVal x2598 = SymStack.pop();
Frames.set(3, x2597);
SymFrames.set(3, x2598);
}
__attribute__((musttail)) return x2473(std::monostate{});
return std::monostate{};
}
std::monostate x2473(std::monostate x2474) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x2350(std::monostate{});
return std::monostate{};
}
std::monostate x2350(std::monostate x2459) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x2460 = Stack.pop();
SymStack.pop();
Num x2461 = I32V(Memory.loadInt(x2460.toInt(), 4));
SymVal x2462 = SymMemory.loadSym(x2460.toInt(), 4);
Stack.push(x2461);
SymStack.push(x2462);
}
{
Num x2463 = Stack.pop();
SymVal x2464 = SymStack.pop();
Num x2465 = Stack.pop();
SymVal x2466 = SymStack.pop();
Num x2467 = x2465.i32_eq(x2463);
Stack.push(x2467);
bool x2468 = allConcrete(x2466, x2464);
SymVal x2469 = x2468 ? Concrete(x2467, 32) : x2466.eq(x2464).bool2bv();
SymStack.push(x2469);
}
Num x2470 = Stack.pop();
{
SymVal x2471 = SymStack.pop();
ExploreTree.fillIfElseNode(x2471, 38);
}
int x2472 = x2470.toInt();
if (x2472 != 0) {
ExploreTree.moveCursor(true, makeControl(x2249, CURRENT_MCONT));
__attribute__((musttail)) return x2457(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x2457, CURRENT_MCONT));
__attribute__((musttail)) return x2249(std::monostate{});
}
return std::monostate{};
}
std::monostate x2457(std::monostate x2458) {
info("Entering the true branch 38 of the if");
info("Jump to 2");
__attribute__((musttail)) return x2446(std::monostate{});
return std::monostate{};
}
std::monostate x2446(std::monostate x2447) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2448 = Stack.pop();
SymVal x2449 = SymStack.pop();
Num x2450 = Stack.pop();
SymVal x2451 = SymStack.pop();
Num x2452 = x2450.i32_sub(x2448);
Stack.push(x2452);
bool x2453 = allConcrete(x2451, x2449);
SymVal x2454 = x2453 ? Concrete(x2452, 32) : x2451.minus(x2449);
SymStack.push(x2454);
}
{
Num x2455 = Stack.pop();
SymVal x2456 = SymStack.pop();
Frames.set(3, x2455);
SymFrames.set(3, x2456);
}
__attribute__((musttail)) return x2444(std::monostate{});
return std::monostate{};
}
std::monostate x2444(std::monostate x2445) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x2404(std::monostate{});
return std::monostate{};
}
std::monostate x2404(std::monostate x2423) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x2424 = Stack.pop();
SymStack.pop();
Num x2425 = I32V(Memory.loadInt(x2424.toInt(), 4));
SymVal x2426 = SymMemory.loadSym(x2424.toInt(), 4);
Stack.push(x2425);
SymStack.push(x2426);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2427 = Stack.pop();
SymVal x2428 = SymStack.pop();
Num x2429 = Stack.pop();
SymVal x2430 = SymStack.pop();
Num x2431 = x2429.i32_sub(x2427);
Stack.push(x2431);
bool x2432 = allConcrete(x2430, x2428);
SymVal x2433 = x2432 ? Concrete(x2431, 32) : x2430.minus(x2428);
SymStack.push(x2433);
}
{
Num x2434 = Stack.pop();
SymVal x2435 = SymStack.pop();
Num x2436 = Stack.pop();
SymVal x2437 = SymStack.pop();
Num x2438 = x2436.i32_eq(x2434);
Stack.push(x2438);
bool x2439 = allConcrete(x2437, x2435);
SymVal x2440 = x2439 ? Concrete(x2438, 32) : x2437.eq(x2435).bool2bv();
SymStack.push(x2440);
}
Num x2441 = Stack.pop();
{
SymVal x2442 = SymStack.pop();
ExploreTree.fillIfElseNode(x2442, 39);
}
int x2443 = x2441.toInt();
if (x2443 != 0) {
ExploreTree.moveCursor(true, makeControl(x2351, CURRENT_MCONT));
__attribute__((musttail)) return x2421(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x2421, CURRENT_MCONT));
__attribute__((musttail)) return x2351(std::monostate{});
}
return std::monostate{};
}
std::monostate x2421(std::monostate x2422) {
info("Entering the true branch 39 of the if");
info("Jump to 2");
__attribute__((musttail)) return x2405(std::monostate{});
return std::monostate{};
}
std::monostate x2405(std::monostate x2406) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x2407 = Stack.pop();
SymStack.pop();
Num x2408 = I32V(Memory.loadInt(x2407.toInt(), 4));
SymVal x2409 = SymMemory.loadSym(x2407.toInt(), 4);
Stack.push(x2408);
SymStack.push(x2409);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2410 = Stack.pop();
SymVal x2411 = SymStack.pop();
Num x2412 = Stack.pop();
SymVal x2413 = SymStack.pop();
Num x2414 = x2412.i32_sub(x2410);
Stack.push(x2414);
bool x2415 = allConcrete(x2413, x2411);
SymVal x2416 = x2415 ? Concrete(x2414, 32) : x2413.minus(x2411);
SymStack.push(x2416);
}
{
Num x2417 = Stack.pop();
SymVal x2418 = SymStack.pop();
Num x2419 = Stack.pop();
SymStack.pop();
int x2420 = x2419.toInt();
Memory.storeInt(x2420, 4, x2417.toInt());
SymMemory.storeSym(x2420, 4, x2418);
}
__attribute__((musttail)) return x2085(std::monostate{});
return std::monostate{};
}
std::monostate x2351(std::monostate x2352) {
info("Entering the false branch 39 of the if");
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x2353 = Stack.pop();
SymVal x2354 = SymStack.pop();
Num x2355 = Stack.pop();
SymVal x2356 = SymStack.pop();
Num x2357 = x2355.i32_mul(x2353);
Stack.push(x2357);
bool x2358 = allConcrete(x2356, x2354);
SymVal x2359 = x2358 ? Concrete(x2357, 32) : x2356.mul(x2354);
SymStack.push(x2359);
}
{
Num x2360 = Stack.pop();
SymVal x2361 = SymStack.pop();
Num x2362 = Stack.pop();
SymVal x2363 = SymStack.pop();
Num x2364 = x2362.i32_add(x2360);
Stack.push(x2364);
bool x2365 = allConcrete(x2363, x2361);
SymVal x2366 = x2365 ? Concrete(x2364, 32) : x2363.add(x2361);
SymStack.push(x2366);
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
SymVal x2375 = SymStack.pop();
Num x2376 = Stack.pop();
SymVal x2377 = SymStack.pop();
Num x2378 = x2376.i32_mul(x2374);
Stack.push(x2378);
bool x2379 = allConcrete(x2377, x2375);
SymVal x2380 = x2379 ? Concrete(x2378, 32) : x2377.mul(x2375);
SymStack.push(x2380);
}
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
SymStack.pop();
Num x2389 = I32V(Memory.loadInt(x2388.toInt(), 8));
SymVal x2390 = SymMemory.loadSym(x2388.toInt(), 8);
Stack.push(x2389);
SymStack.push(x2390);
}
{
Num x2391 = Stack.pop();
SymVal x2392 = SymStack.pop();
Num x2393 = Stack.pop();
SymStack.pop();
int x2394 = x2393.toInt();
Memory.storeInt(x2394, 8, x2391.toInt());
SymMemory.storeSym(x2394, 8, x2392);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2395 = Stack.pop();
SymVal x2396 = SymStack.pop();
Num x2397 = Stack.pop();
SymVal x2398 = SymStack.pop();
Num x2399 = x2397.i32_add(x2395);
Stack.push(x2399);
bool x2400 = allConcrete(x2398, x2396);
SymVal x2401 = x2400 ? Concrete(x2399, 32) : x2398.add(x2396);
SymStack.push(x2401);
}
{
Num x2402 = Stack.pop();
SymVal x2403 = SymStack.pop();
Frames.set(3, x2402);
SymFrames.set(3, x2403);
}
info("Jump to 1");
__attribute__((musttail)) return x2404(std::monostate{});
return std::monostate{};
}
std::monostate x2249(std::monostate x2250) {
info("Entering the false branch 38 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x2251 = Stack.pop();
SymStack.pop();
Num x2252 = I32V(Memory.loadInt(x2251.toInt(), 0));
SymVal x2253 = SymMemory.loadSym(x2251.toInt(), 0);
Stack.push(x2252);
SymStack.push(x2253);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2254 = Stack.pop();
SymVal x2255 = SymStack.pop();
Num x2256 = Stack.pop();
SymVal x2257 = SymStack.pop();
Num x2258 = x2256.i32_sub(x2254);
Stack.push(x2258);
bool x2259 = allConcrete(x2257, x2255);
SymVal x2260 = x2259 ? Concrete(x2258, 32) : x2257.minus(x2255);
SymStack.push(x2260);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2261 = Stack.pop();
SymVal x2262 = SymStack.pop();
Num x2263 = Stack.pop();
SymVal x2264 = SymStack.pop();
Num x2265 = x2263.i32_mul(x2261);
Stack.push(x2265);
bool x2266 = allConcrete(x2264, x2262);
SymVal x2267 = x2266 ? Concrete(x2265, 32) : x2264.mul(x2262);
SymStack.push(x2267);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2268 = Stack.pop();
SymVal x2269 = SymStack.pop();
Num x2270 = Stack.pop();
SymVal x2271 = SymStack.pop();
Num x2272 = x2270.i32_mul(x2268);
Stack.push(x2272);
bool x2273 = allConcrete(x2271, x2269);
SymVal x2274 = x2273 ? Concrete(x2272, 32) : x2271.mul(x2269);
SymStack.push(x2274);
}
{
Num x2275 = Stack.pop();
SymVal x2276 = SymStack.pop();
Num x2277 = Stack.pop();
SymVal x2278 = SymStack.pop();
Num x2279 = x2277.i32_add(x2275);
Stack.push(x2279);
bool x2280 = allConcrete(x2278, x2276);
SymVal x2281 = x2280 ? Concrete(x2279, 32) : x2278.add(x2276);
SymStack.push(x2281);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x2282 = Stack.pop();
SymVal x2283 = SymStack.pop();
Num x2284 = Stack.pop();
SymVal x2285 = SymStack.pop();
Num x2286 = x2284.i32_add(x2282);
Stack.push(x2286);
bool x2287 = allConcrete(x2285, x2283);
SymVal x2288 = x2287 ? Concrete(x2286, 32) : x2285.add(x2283);
SymStack.push(x2288);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x2289 = Stack.pop();
SymStack.pop();
Num x2290 = I32V(Memory.loadInt(x2289.toInt(), 0));
SymVal x2291 = SymMemory.loadSym(x2289.toInt(), 0);
Stack.push(x2290);
SymStack.push(x2291);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2292 = Stack.pop();
SymVal x2293 = SymStack.pop();
Num x2294 = Stack.pop();
SymVal x2295 = SymStack.pop();
Num x2296 = x2294.i32_sub(x2292);
Stack.push(x2296);
bool x2297 = allConcrete(x2295, x2293);
SymVal x2298 = x2297 ? Concrete(x2296, 32) : x2295.minus(x2293);
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
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
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
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2313 = Stack.pop();
SymVal x2314 = SymStack.pop();
Num x2315 = Stack.pop();
SymVal x2316 = SymStack.pop();
Num x2317 = x2315.i32_mul(x2313);
Stack.push(x2317);
bool x2318 = allConcrete(x2316, x2314);
SymVal x2319 = x2318 ? Concrete(x2317, 32) : x2316.mul(x2314);
SymStack.push(x2319);
}
{
Num x2320 = Stack.pop();
SymVal x2321 = SymStack.pop();
Num x2322 = Stack.pop();
SymVal x2323 = SymStack.pop();
Num x2324 = x2322.i32_add(x2320);
Stack.push(x2324);
bool x2325 = allConcrete(x2323, x2321);
SymVal x2326 = x2325 ? Concrete(x2324, 32) : x2323.add(x2321);
SymStack.push(x2326);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
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
SymStack.pop();
Num x2335 = I32V(Memory.loadInt(x2334.toInt(), 8));
SymVal x2336 = SymMemory.loadSym(x2334.toInt(), 8);
Stack.push(x2335);
SymStack.push(x2336);
}
{
Num x2337 = Stack.pop();
SymVal x2338 = SymStack.pop();
Num x2339 = Stack.pop();
SymStack.pop();
int x2340 = x2339.toInt();
Memory.storeInt(x2340, 8, x2337.toInt());
SymMemory.storeSym(x2340, 8, x2338);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2341 = Stack.pop();
SymVal x2342 = SymStack.pop();
Num x2343 = Stack.pop();
SymVal x2344 = SymStack.pop();
Num x2345 = x2343.i32_add(x2341);
Stack.push(x2345);
bool x2346 = allConcrete(x2344, x2342);
SymVal x2347 = x2346 ? Concrete(x2345, 32) : x2344.add(x2342);
SymStack.push(x2347);
}
{
Num x2348 = Stack.pop();
SymVal x2349 = SymStack.pop();
Frames.set(3, x2348);
SymFrames.set(3, x2349);
}
info("Jump to 1");
__attribute__((musttail)) return x2350(std::monostate{});
return std::monostate{};
}
std::monostate x2089(std::monostate x2090) {
info("Entering the false branch 36 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x2091 = Stack.pop();
SymStack.pop();
Num x2092 = I32V(Memory.loadInt(x2091.toInt(), 0));
SymVal x2093 = SymMemory.loadSym(x2091.toInt(), 0);
Stack.push(x2092);
SymStack.push(x2093);
}
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
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2108 = Stack.pop();
SymVal x2109 = SymStack.pop();
Num x2110 = Stack.pop();
SymVal x2111 = SymStack.pop();
Num x2112 = x2110.i32_sub(x2108);
Stack.push(x2112);
bool x2113 = allConcrete(x2111, x2109);
SymVal x2114 = x2113 ? Concrete(x2112, 32) : x2111.minus(x2109);
SymStack.push(x2114);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2115 = Stack.pop();
SymVal x2116 = SymStack.pop();
Num x2117 = Stack.pop();
SymVal x2118 = SymStack.pop();
Num x2119 = x2117.i32_mul(x2115);
Stack.push(x2119);
bool x2120 = allConcrete(x2118, x2116);
SymVal x2121 = x2120 ? Concrete(x2119, 32) : x2118.mul(x2116);
SymStack.push(x2121);
}
{
Num x2122 = Stack.pop();
SymVal x2123 = SymStack.pop();
Num x2124 = Stack.pop();
SymVal x2125 = SymStack.pop();
Num x2126 = x2124.i32_add(x2122);
Stack.push(x2126);
bool x2127 = allConcrete(x2125, x2123);
SymVal x2128 = x2127 ? Concrete(x2126, 32) : x2125.add(x2123);
SymStack.push(x2128);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x2129 = Stack.pop();
SymVal x2130 = SymStack.pop();
Num x2131 = Stack.pop();
SymVal x2132 = SymStack.pop();
Num x2133 = x2131.i32_add(x2129);
Stack.push(x2133);
bool x2134 = allConcrete(x2132, x2130);
SymVal x2135 = x2134 ? Concrete(x2133, 32) : x2132.add(x2130);
SymStack.push(x2135);
}
{
Num x2136 = Stack.pop();
SymStack.pop();
Num x2137 = I32V(Memory.loadInt(x2136.toInt(), 8));
SymVal x2138 = SymMemory.loadSym(x2136.toInt(), 8);
Stack.push(x2137);
SymStack.push(x2138);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x2139 = Stack.pop();
SymStack.pop();
Num x2140 = I32V(Memory.loadInt(x2139.toInt(), 0));
SymVal x2141 = SymMemory.loadSym(x2139.toInt(), 0);
Stack.push(x2140);
SymStack.push(x2141);
}
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
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2156 = Stack.pop();
SymVal x2157 = SymStack.pop();
Num x2158 = Stack.pop();
SymVal x2159 = SymStack.pop();
Num x2160 = x2158.i32_sub(x2156);
Stack.push(x2160);
bool x2161 = allConcrete(x2159, x2157);
SymVal x2162 = x2161 ? Concrete(x2160, 32) : x2159.minus(x2157);
SymStack.push(x2162);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2163 = Stack.pop();
SymVal x2164 = SymStack.pop();
Num x2165 = Stack.pop();
SymVal x2166 = SymStack.pop();
Num x2167 = x2165.i32_mul(x2163);
Stack.push(x2167);
bool x2168 = allConcrete(x2166, x2164);
SymVal x2169 = x2168 ? Concrete(x2167, 32) : x2166.mul(x2164);
SymStack.push(x2169);
}
{
Num x2170 = Stack.pop();
SymVal x2171 = SymStack.pop();
Num x2172 = Stack.pop();
SymVal x2173 = SymStack.pop();
Num x2174 = x2172.i32_add(x2170);
Stack.push(x2174);
bool x2175 = allConcrete(x2173, x2171);
SymVal x2176 = x2175 ? Concrete(x2174, 32) : x2173.add(x2171);
SymStack.push(x2176);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x2177 = Stack.pop();
SymVal x2178 = SymStack.pop();
Num x2179 = Stack.pop();
SymVal x2180 = SymStack.pop();
Num x2181 = x2179.i32_add(x2177);
Stack.push(x2181);
bool x2182 = allConcrete(x2180, x2178);
SymVal x2183 = x2182 ? Concrete(x2181, 32) : x2180.add(x2178);
SymStack.push(x2183);
}
{
Num x2184 = Stack.pop();
SymStack.pop();
Num x2185 = I32V(Memory.loadInt(x2184.toInt(), 8));
SymVal x2186 = SymMemory.loadSym(x2184.toInt(), 8);
Stack.push(x2185);
SymStack.push(x2186);
}
{
Num x2187 = Stack.pop();
SymStack.pop();
Num x2188 = I32V(Memory.loadInt(x2187.toInt(), 4));
SymVal x2189 = SymMemory.loadSym(x2187.toInt(), 4);
Stack.push(x2188);
SymStack.push(x2189);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x2190 = Stack.pop();
SymVal x2191 = SymStack.pop();
Num x2192 = Stack.pop();
SymVal x2193 = SymStack.pop();
Num x2194 = x2192.i32_add(x2190);
Stack.push(x2194);
bool x2195 = allConcrete(x2193, x2191);
SymVal x2196 = x2195 ? Concrete(x2194, 32) : x2193.add(x2191);
SymStack.push(x2196);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
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
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x2218 = Stack.pop();
SymVal x2219 = SymStack.pop();
Num x2220 = Stack.pop();
SymVal x2221 = SymStack.pop();
Num x2222 = x2220.i32_mul(x2218);
Stack.push(x2222);
bool x2223 = allConcrete(x2221, x2219);
SymVal x2224 = x2223 ? Concrete(x2222, 32) : x2221.mul(x2219);
SymStack.push(x2224);
}
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
SymStack.pop();
Num x2233 = I32V(Memory.loadInt(x2232.toInt(), 8));
SymVal x2234 = SymMemory.loadSym(x2232.toInt(), 8);
Stack.push(x2233);
SymStack.push(x2234);
}
{
Num x2235 = Stack.pop();
SymVal x2236 = SymStack.pop();
Num x2237 = Stack.pop();
SymStack.pop();
int x2238 = x2237.toInt();
Memory.storeInt(x2238, 8, x2235.toInt());
SymMemory.storeSym(x2238, 8, x2236);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2239 = Stack.pop();
SymVal x2240 = SymStack.pop();
Num x2241 = Stack.pop();
SymVal x2242 = SymStack.pop();
Num x2243 = x2241.i32_add(x2239);
Stack.push(x2243);
bool x2244 = allConcrete(x2242, x2240);
SymVal x2245 = x2244 ? Concrete(x2243, 32) : x2242.add(x2240);
SymStack.push(x2245);
}
{
Num x2246 = Stack.pop();
SymVal x2247 = SymStack.pop();
Frames.set(3, x2246);
SymFrames.set(3, x2247);
}
info("Jump to 1");
__attribute__((musttail)) return x2248(std::monostate{});
return std::monostate{};
}
std::monostate x2087(std::monostate x2088) {
info("Entering the false branch 35 of the if");
__attribute__((musttail)) return x2085(std::monostate{});
return std::monostate{};
}
std::monostate x2085(std::monostate x2086) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x2081(std::monostate{});
return std::monostate{};
}
std::monostate x2083(std::monostate x2084) {
info("Entering the false branch 33 of the if");
__attribute__((musttail)) return x2081(std::monostate{});
return std::monostate{};
}
std::monostate x2081(std::monostate x2082) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x2067(std::monostate{});
return std::monostate{};
}
std::monostate x2079(std::monostate x2080) {
info("Entering the false branch 28 of the if");
__attribute__((musttail)) return x2067(std::monostate{});
return std::monostate{};
}
std::monostate x2067(std::monostate x2068) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(Frames.get(4));
SymStack.push(SymFrames.get(4));
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x2069 = Stack.pop();
SymVal x2070 = SymStack.pop();
Num x2071 = Stack.pop();
SymVal x2072 = SymStack.pop();
Num x2073 = x2071.i32_eq(x2069);
Stack.push(x2073);
bool x2074 = allConcrete(x2072, x2070);
SymVal x2075 = x2074 ? Concrete(x2073, 32) : x2072.eq(x2070).bool2bv();
SymStack.push(x2075);
}
Num x2076 = Stack.pop();
{
SymVal x2077 = SymStack.pop();
ExploreTree.fillIfElseNode(x2077, 29);
}
int x2078 = x2076.toInt();
if (x2078 != 0) {
ExploreTree.moveCursor(true, makeControl(x2004, CURRENT_MCONT));
__attribute__((musttail)) return x2013(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x2013, CURRENT_MCONT));
__attribute__((musttail)) return x2004(std::monostate{});
}
return std::monostate{};
}
std::monostate x2013(std::monostate x2014) {
info("Entering the true branch 29 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x2015 = Stack.pop();
SymStack.pop();
Num x2016 = I32V(Memory.loadInt(x2015.toInt(), 0));
SymVal x2017 = SymMemory.loadSym(x2015.toInt(), 0);
Stack.push(x2016);
SymStack.push(x2017);
}
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
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x2032 = Stack.pop();
SymVal x2033 = SymStack.pop();
Num x2034 = Stack.pop();
SymVal x2035 = SymStack.pop();
Num x2036 = x2034.i32_sub(x2032);
Stack.push(x2036);
bool x2037 = allConcrete(x2035, x2033);
SymVal x2038 = x2037 ? Concrete(x2036, 32) : x2035.minus(x2033);
SymStack.push(x2038);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x2039 = Stack.pop();
SymVal x2040 = SymStack.pop();
Num x2041 = Stack.pop();
SymVal x2042 = SymStack.pop();
Num x2043 = x2041.i32_mul(x2039);
Stack.push(x2043);
bool x2044 = allConcrete(x2042, x2040);
SymVal x2045 = x2044 ? Concrete(x2043, 32) : x2042.mul(x2040);
SymStack.push(x2045);
}
{
Num x2046 = Stack.pop();
SymVal x2047 = SymStack.pop();
Num x2048 = Stack.pop();
SymVal x2049 = SymStack.pop();
Num x2050 = x2048.i32_add(x2046);
Stack.push(x2050);
bool x2051 = allConcrete(x2049, x2047);
SymVal x2052 = x2051 ? Concrete(x2050, 32) : x2049.add(x2047);
SymStack.push(x2052);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x2053 = Stack.pop();
SymVal x2054 = SymStack.pop();
Num x2055 = Stack.pop();
SymVal x2056 = SymStack.pop();
Num x2057 = x2055.i32_add(x2053);
Stack.push(x2057);
bool x2058 = allConcrete(x2056, x2054);
SymVal x2059 = x2058 ? Concrete(x2057, 32) : x2056.add(x2054);
SymStack.push(x2059);
}
{
Num x2060 = Stack.pop();
SymStack.pop();
Num x2061 = I32V(Memory.loadInt(x2060.toInt(), 8));
SymVal x2062 = SymMemory.loadSym(x2060.toInt(), 8);
Stack.push(x2061);
SymStack.push(x2062);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 12);
Num x2063 = Stack.pop();
Num x2064 = Stack.pop();
SymVal x2065 = SymStack.pop();
SymVal x2066 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x2064);
Frames.set(1, x2063);
SymFrames.set(0, x2066);
SymFrames.set(1, x2065);
updateCurrentMCont(prependCont(x2011, CURRENT_MCONT));
}
__attribute__((musttail)) return x2010(std::monostate{});
return std::monostate{};
}
std::monostate x2011(std::monostate x2012) {
infoWhen("CALL", "Returning from the function at 12, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.pop();
SymStack.pop();
__attribute__((musttail)) return x2000(std::monostate{});
return std::monostate{};
}
std::monostate x2004(std::monostate x2005) {
info("Entering the false branch 29 of the if");
Stack.push(Frames.get(5));
SymStack.push(SymFrames.get(5));
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 12);
Num x2006 = Stack.pop();
Num x2007 = Stack.pop();
SymVal x2008 = SymStack.pop();
SymVal x2009 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x2007);
Frames.set(1, x2006);
SymFrames.set(0, x2009);
SymFrames.set(1, x2008);
updateCurrentMCont(prependCont(x2002, CURRENT_MCONT));
}
__attribute__((musttail)) return x2010(std::monostate{});
return std::monostate{};
}
std::monostate x2002(std::monostate x2003) {
infoWhen("CALL", "Returning from the function at 12, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.pop();
SymStack.pop();
__attribute__((musttail)) return x2000(std::monostate{});
return std::monostate{};
}
std::monostate x2000(std::monostate x2001) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x1998(std::monostate{});
return std::monostate{};
}
std::monostate x1998(std::monostate x1999) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x1980(std::monostate{});
return std::monostate{};
}
std::monostate x1980(std::monostate x1981) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1982 = Stack.pop();
SymStack.pop();
Num x1983 = I32V(Memory.loadInt(x1982.toInt(), 8));
SymVal x1984 = SymMemory.loadSym(x1982.toInt(), 8);
Stack.push(x1983);
SymStack.push(x1984);
}
{
Num x1985 = Stack.pop();
SymStack.pop();
Num x1986 = I32V(Memory.loadInt(x1985.toInt(), 4));
SymVal x1987 = SymMemory.loadSym(x1985.toInt(), 4);
Stack.push(x1986);
SymStack.push(x1987);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1988 = Stack.pop();
SymVal x1989 = SymStack.pop();
Num x1990 = Stack.pop();
SymVal x1991 = SymStack.pop();
Num x1992 = x1990.i32_eq(x1988);
Stack.push(x1992);
bool x1993 = allConcrete(x1991, x1989);
SymVal x1994 = x1993 ? Concrete(x1992, 32) : x1991.eq(x1989).bool2bv();
SymStack.push(x1994);
}
Num x1995 = Stack.pop();
{
SymVal x1996 = SymStack.pop();
ExploreTree.fillIfElseNode(x1996, 30);
}
int x1997 = x1995.toInt();
if (x1997 != 0) {
ExploreTree.moveCursor(true, makeControl(x1928, CURRENT_MCONT));
__attribute__((musttail)) return x1930(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1930, CURRENT_MCONT));
__attribute__((musttail)) return x1928(std::monostate{});
}
return std::monostate{};
}
std::monostate x1930(std::monostate x1931) {
info("Entering the true branch 30 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1932 = Stack.pop();
SymStack.pop();
Num x1933 = I32V(Memory.loadInt(x1932.toInt(), 0));
SymVal x1934 = SymMemory.loadSym(x1932.toInt(), 0);
Stack.push(x1933);
SymStack.push(x1934);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1935 = Stack.pop();
SymVal x1936 = SymStack.pop();
Num x1937 = Stack.pop();
SymVal x1938 = SymStack.pop();
Num x1939 = x1937.i32_sub(x1935);
Stack.push(x1939);
bool x1940 = allConcrete(x1938, x1936);
SymVal x1941 = x1940 ? Concrete(x1939, 32) : x1938.minus(x1936);
SymStack.push(x1941);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x1942 = Stack.pop();
SymVal x1943 = SymStack.pop();
Num x1944 = Stack.pop();
SymVal x1945 = SymStack.pop();
Num x1946 = x1944.i32_mul(x1942);
Stack.push(x1946);
bool x1947 = allConcrete(x1945, x1943);
SymVal x1948 = x1947 ? Concrete(x1946, 32) : x1945.mul(x1943);
SymStack.push(x1948);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x1949 = Stack.pop();
SymVal x1950 = SymStack.pop();
Num x1951 = Stack.pop();
SymVal x1952 = SymStack.pop();
Num x1953 = x1951.i32_mul(x1949);
Stack.push(x1953);
bool x1954 = allConcrete(x1952, x1950);
SymVal x1955 = x1954 ? Concrete(x1953, 32) : x1952.mul(x1950);
SymStack.push(x1955);
}
{
Num x1956 = Stack.pop();
SymVal x1957 = SymStack.pop();
Num x1958 = Stack.pop();
SymVal x1959 = SymStack.pop();
Num x1960 = x1958.i32_add(x1956);
Stack.push(x1960);
bool x1961 = allConcrete(x1959, x1957);
SymVal x1962 = x1961 ? Concrete(x1960, 32) : x1959.add(x1957);
SymStack.push(x1962);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1963 = Stack.pop();
SymStack.pop();
Num x1964 = I32V(Memory.loadInt(x1963.toInt(), 8));
SymVal x1965 = SymMemory.loadSym(x1963.toInt(), 8);
Stack.push(x1964);
SymStack.push(x1965);
}
{
Num x1966 = Stack.pop();
SymVal x1967 = SymStack.pop();
Num x1968 = Stack.pop();
SymVal x1969 = SymStack.pop();
Num x1970 = x1968.i32_add(x1966);
Stack.push(x1970);
bool x1971 = allConcrete(x1969, x1967);
SymVal x1972 = x1971 ? Concrete(x1970, 32) : x1969.add(x1967);
SymStack.push(x1972);
}
{
Num x1973 = Stack.pop();
SymStack.pop();
Num x1974 = I32V(Memory.loadInt(x1973.toInt(), 8));
SymVal x1975 = SymMemory.loadSym(x1973.toInt(), 8);
Stack.push(x1974);
SymStack.push(x1975);
}
{
Num x1976 = Stack.pop();
SymVal x1977 = SymStack.pop();
Num x1978 = Stack.pop();
SymStack.pop();
int x1979 = x1978.toInt();
Memory.storeInt(x1979, 8, x1976.toInt());
SymMemory.storeSym(x1979, 8, x1977);
}
__attribute__((musttail)) return x1923(std::monostate{});
return std::monostate{};
}
std::monostate x1928(std::monostate x1929) {
info("Entering the false branch 30 of the if");
__attribute__((musttail)) return x1923(std::monostate{});
return std::monostate{};
}
std::monostate x1923(std::monostate x1924) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1925 = Stack.pop();
SymStack.pop();
Num x1926 = I32V(Memory.loadInt(x1925.toInt(), 8));
SymVal x1927 = SymMemory.loadSym(x1925.toInt(), 8);
Stack.push(x1926);
SymStack.push(x1927);
}
__attribute__((musttail)) return x1921(std::monostate{});
return std::monostate{};
}
std::monostate x1921(std::monostate x1922) {
infoWhen("CALL", "Exiting the function at 12, stackSize =", Stack.size());
Frames.popFrameCallee(4);
SymFrames.popFrameCallee(4);
return enterCC(std::monostate());
}
std::monostate x1678(std::monostate x1918) {
infoWhen("CALL", "Entered the function at 8, stackSize =", Stack.size());
Frames.pushFrameCallee(1);
SymFrames.pushFrameSlot(32);
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1919 = Stack.pop();
SymVal x1920 = SymStack.pop();
Frames.set(2, x1919);
SymFrames.set(2, x1920);
}
__attribute__((musttail)) return x1916(std::monostate{});
return std::monostate{};
}
std::monostate x1916(std::monostate x1917) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x1865(std::monostate{});
return std::monostate{};
}
std::monostate x1865(std::monostate x1895) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x1896 = Stack.pop();
SymStack.pop();
Num x1897 = I32V(Memory.loadInt(x1896.toInt(), 4));
SymVal x1898 = SymMemory.loadSym(x1896.toInt(), 4);
Stack.push(x1897);
SymStack.push(x1898);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1899 = Stack.pop();
SymVal x1900 = SymStack.pop();
Num x1901 = Stack.pop();
SymVal x1902 = SymStack.pop();
Num x1903 = x1901.i32_sub(x1899);
Stack.push(x1903);
bool x1904 = allConcrete(x1902, x1900);
SymVal x1905 = x1904 ? Concrete(x1903, 32) : x1902.minus(x1900);
SymStack.push(x1905);
}
{
Num x1906 = Stack.pop();
SymVal x1907 = SymStack.pop();
Num x1908 = Stack.pop();
SymVal x1909 = SymStack.pop();
Num x1910 = x1908.i32_le_s(x1906);
Stack.push(x1910);
bool x1911 = allConcrete(x1909, x1907);
SymVal x1912 = x1911 ? Concrete(x1910, 32) : x1909.le(x1907).bool2bv();
SymStack.push(x1912);
}
Num x1913 = Stack.pop();
{
SymVal x1914 = SymStack.pop();
ExploreTree.fillIfElseNode(x1914, 18);
}
int x1915 = x1913.toInt();
if (x1915 != 0) {
ExploreTree.moveCursor(true, makeControl(x1850, CURRENT_MCONT));
__attribute__((musttail)) return x1866(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1866, CURRENT_MCONT));
__attribute__((musttail)) return x1850(std::monostate{});
}
return std::monostate{};
}
std::monostate x1866(std::monostate x1867) {
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
Num x1868 = Stack.pop();
SymVal x1869 = SymStack.pop();
Num x1870 = Stack.pop();
SymVal x1871 = SymStack.pop();
Num x1872 = x1870.i32_mul(x1868);
Stack.push(x1872);
bool x1873 = allConcrete(x1871, x1869);
SymVal x1874 = x1873 ? Concrete(x1872, 32) : x1871.mul(x1869);
SymStack.push(x1874);
}
{
Num x1875 = Stack.pop();
SymVal x1876 = SymStack.pop();
Num x1877 = Stack.pop();
SymVal x1878 = SymStack.pop();
Num x1879 = x1877.i32_add(x1875);
Stack.push(x1879);
bool x1880 = allConcrete(x1878, x1876);
SymVal x1881 = x1880 ? Concrete(x1879, 32) : x1878.add(x1876);
SymStack.push(x1881);
}
{
Num x1882 = Stack.pop();
SymStack.pop();
Num x1883 = I32V(Memory.loadInt(x1882.toInt(), 8));
SymVal x1884 = SymMemory.loadSym(x1882.toInt(), 8);
Stack.push(x1883);
SymStack.push(x1884);
}
{
Num x1885 = Stack.pop();
SymVal x1886 = SymStack.pop();
Num x1887 = Stack.pop();
SymVal x1888 = SymStack.pop();
Num x1889 = x1887.i32_gt_s(x1885);
Stack.push(x1889);
bool x1890 = allConcrete(x1888, x1886);
SymVal x1891 = x1890 ? Concrete(x1889, 32) : x1888.gt(x1886).bool2bv();
SymStack.push(x1891);
}
Num x1892 = Stack.pop();
{
SymVal x1893 = SymStack.pop();
ExploreTree.fillIfElseNode(x1893, 22);
}
int x1894 = x1892.toInt();
if (x1894 != 0) {
ExploreTree.moveCursor(true, makeControl(x1852, CURRENT_MCONT));
__attribute__((musttail)) return x1854(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1854, CURRENT_MCONT));
__attribute__((musttail)) return x1852(std::monostate{});
}
return std::monostate{};
}
std::monostate x1854(std::monostate x1855) {
info("Entering the true branch 22 of the if");
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x1856 = Stack.pop();
SymVal x1857 = SymStack.pop();
Num x1858 = Stack.pop();
SymVal x1859 = SymStack.pop();
Num x1860 = x1858.i32_add(x1856);
Stack.push(x1860);
bool x1861 = allConcrete(x1859, x1857);
SymVal x1862 = x1861 ? Concrete(x1860, 32) : x1859.add(x1857);
SymStack.push(x1862);
}
{
Num x1863 = Stack.pop();
SymVal x1864 = SymStack.pop();
Frames.set(2, x1863);
SymFrames.set(2, x1864);
}
info("Jump to 2");
__attribute__((musttail)) return x1865(std::monostate{});
return std::monostate{};
}
std::monostate x1852(std::monostate x1853) {
info("Entering the false branch 22 of the if");
info("Jump to 3");
__attribute__((musttail)) return x1828(std::monostate{});
return std::monostate{};
}
std::monostate x1850(std::monostate x1851) {
info("Entering the false branch 18 of the if");
info("Jump to 2");
__attribute__((musttail)) return x1828(std::monostate{});
return std::monostate{};
}
std::monostate x1828(std::monostate x1829) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x1830 = Stack.pop();
SymStack.pop();
Num x1831 = I32V(Memory.loadInt(x1830.toInt(), 4));
SymVal x1832 = SymMemory.loadSym(x1830.toInt(), 4);
Stack.push(x1831);
SymStack.push(x1832);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1833 = Stack.pop();
SymVal x1834 = SymStack.pop();
Num x1835 = Stack.pop();
SymVal x1836 = SymStack.pop();
Num x1837 = x1835.i32_sub(x1833);
Stack.push(x1837);
bool x1838 = allConcrete(x1836, x1834);
SymVal x1839 = x1838 ? Concrete(x1837, 32) : x1836.minus(x1834);
SymStack.push(x1839);
}
{
Num x1840 = Stack.pop();
SymVal x1841 = SymStack.pop();
Num x1842 = Stack.pop();
SymVal x1843 = SymStack.pop();
Num x1844 = x1842.i32_le_s(x1840);
Stack.push(x1844);
bool x1845 = allConcrete(x1843, x1841);
SymVal x1846 = x1845 ? Concrete(x1844, 32) : x1843.le(x1841).bool2bv();
SymStack.push(x1846);
}
Num x1847 = Stack.pop();
{
SymVal x1848 = SymStack.pop();
ExploreTree.fillIfElseNode(x1848, 19);
}
int x1849 = x1847.toInt();
if (x1849 != 0) {
ExploreTree.moveCursor(true, makeControl(x1687, CURRENT_MCONT));
__attribute__((musttail)) return x1799(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1799, CURRENT_MCONT));
__attribute__((musttail)) return x1687(std::monostate{});
}
return std::monostate{};
}
std::monostate x1799(std::monostate x1800) {
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
Num x1801 = Stack.pop();
SymVal x1802 = SymStack.pop();
Num x1803 = Stack.pop();
SymVal x1804 = SymStack.pop();
Num x1805 = x1803.i32_mul(x1801);
Stack.push(x1805);
bool x1806 = allConcrete(x1804, x1802);
SymVal x1807 = x1806 ? Concrete(x1805, 32) : x1804.mul(x1802);
SymStack.push(x1807);
}
{
Num x1808 = Stack.pop();
SymVal x1809 = SymStack.pop();
Num x1810 = Stack.pop();
SymVal x1811 = SymStack.pop();
Num x1812 = x1810.i32_add(x1808);
Stack.push(x1812);
bool x1813 = allConcrete(x1811, x1809);
SymVal x1814 = x1813 ? Concrete(x1812, 32) : x1811.add(x1809);
SymStack.push(x1814);
}
{
Num x1815 = Stack.pop();
SymStack.pop();
Num x1816 = I32V(Memory.loadInt(x1815.toInt(), 8));
SymVal x1817 = SymMemory.loadSym(x1815.toInt(), 8);
Stack.push(x1816);
SymStack.push(x1817);
}
{
Num x1818 = Stack.pop();
SymVal x1819 = SymStack.pop();
Num x1820 = Stack.pop();
SymVal x1821 = SymStack.pop();
Num x1822 = x1820.i32_eq(x1818);
Stack.push(x1822);
bool x1823 = allConcrete(x1821, x1819);
SymVal x1824 = x1823 ? Concrete(x1822, 32) : x1821.eq(x1819).bool2bv();
SymStack.push(x1824);
}
Num x1825 = Stack.pop();
{
SymVal x1826 = SymStack.pop();
ExploreTree.fillIfElseNode(x1826, 21);
}
int x1827 = x1825.toInt();
if (x1827 != 0) {
ExploreTree.moveCursor(true, makeControl(x1761, CURRENT_MCONT));
__attribute__((musttail)) return x1776(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1776, CURRENT_MCONT));
__attribute__((musttail)) return x1761(std::monostate{});
}
return std::monostate{};
}
std::monostate x1776(std::monostate x1777) {
info("Entering the true branch 21 of the if");
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(8));
SymStack.push(Concrete(I32V(8), 32));
{
Num x1778 = Stack.pop();
SymVal x1779 = SymStack.pop();
Num x1780 = Stack.pop();
SymVal x1781 = SymStack.pop();
Num x1782 = x1780.i32_add(x1778);
Stack.push(x1782);
bool x1783 = allConcrete(x1781, x1779);
SymVal x1784 = x1783 ? Concrete(x1782, 32) : x1781.add(x1779);
SymStack.push(x1784);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x1785 = Stack.pop();
SymVal x1786 = SymStack.pop();
Num x1787 = Stack.pop();
SymVal x1788 = SymStack.pop();
Num x1789 = x1787.i32_mul(x1785);
Stack.push(x1789);
bool x1790 = allConcrete(x1788, x1786);
SymVal x1791 = x1790 ? Concrete(x1789, 32) : x1788.mul(x1786);
SymStack.push(x1791);
}
{
Num x1792 = Stack.pop();
SymVal x1793 = SymStack.pop();
Num x1794 = Stack.pop();
SymVal x1795 = SymStack.pop();
Num x1796 = x1794.i32_add(x1792);
Stack.push(x1796);
bool x1797 = allConcrete(x1795, x1793);
SymVal x1798 = x1797 ? Concrete(x1796, 32) : x1795.add(x1793);
SymStack.push(x1798);
}
__attribute__((musttail)) return x1755(std::monostate{});
return std::monostate{};
}
std::monostate x1761(std::monostate x1762) {
info("Entering the false branch 21 of the if");
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x1763 = Stack.pop();
SymStack.pop();
Num x1764 = I32V(Memory.loadInt(x1763.toInt(), 0));
SymVal x1765 = SymMemory.loadSym(x1763.toInt(), 0);
Stack.push(x1764);
SymStack.push(x1765);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1766 = Stack.pop();
SymVal x1767 = SymStack.pop();
Num x1768 = Stack.pop();
SymVal x1769 = SymStack.pop();
Num x1770 = x1768.i32_eq(x1766);
Stack.push(x1770);
bool x1771 = allConcrete(x1769, x1767);
SymVal x1772 = x1771 ? Concrete(x1770, 32) : x1769.eq(x1767).bool2bv();
SymStack.push(x1772);
}
Num x1773 = Stack.pop();
{
SymVal x1774 = SymStack.pop();
ExploreTree.fillIfElseNode(x1774, 20);
}
int x1775 = x1773.toInt();
if (x1775 != 0) {
ExploreTree.moveCursor(true, makeControl(x1708, CURRENT_MCONT));
__attribute__((musttail)) return x1759(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1759, CURRENT_MCONT));
__attribute__((musttail)) return x1708(std::monostate{});
}
return std::monostate{};
}
std::monostate x1759(std::monostate x1760) {
info("Entering the true branch 20 of the if");
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
__attribute__((musttail)) return x1757(std::monostate{});
return std::monostate{};
}
std::monostate x1757(std::monostate x1758) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x1755(std::monostate{});
return std::monostate{};
}
std::monostate x1755(std::monostate x1756) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x1681(std::monostate{});
return std::monostate{};
}
std::monostate x1708(std::monostate x1709) {
info("Entering the false branch 20 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1710 = Stack.pop();
SymStack.pop();
Num x1711 = I32V(Memory.loadInt(x1710.toInt(), 0));
SymVal x1712 = SymMemory.loadSym(x1710.toInt(), 0);
Stack.push(x1711);
SymStack.push(x1712);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1713 = Stack.pop();
SymVal x1714 = SymStack.pop();
Num x1715 = Stack.pop();
SymVal x1716 = SymStack.pop();
Num x1717 = x1715.i32_sub(x1713);
Stack.push(x1717);
bool x1718 = allConcrete(x1716, x1714);
SymVal x1719 = x1718 ? Concrete(x1717, 32) : x1716.minus(x1714);
SymStack.push(x1719);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x1720 = Stack.pop();
SymVal x1721 = SymStack.pop();
Num x1722 = Stack.pop();
SymVal x1723 = SymStack.pop();
Num x1724 = x1722.i32_mul(x1720);
Stack.push(x1724);
bool x1725 = allConcrete(x1723, x1721);
SymVal x1726 = x1725 ? Concrete(x1724, 32) : x1723.mul(x1721);
SymStack.push(x1726);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x1727 = Stack.pop();
SymVal x1728 = SymStack.pop();
Num x1729 = Stack.pop();
SymVal x1730 = SymStack.pop();
Num x1731 = x1729.i32_mul(x1727);
Stack.push(x1731);
bool x1732 = allConcrete(x1730, x1728);
SymVal x1733 = x1732 ? Concrete(x1731, 32) : x1730.mul(x1728);
SymStack.push(x1733);
}
{
Num x1734 = Stack.pop();
SymVal x1735 = SymStack.pop();
Num x1736 = Stack.pop();
SymVal x1737 = SymStack.pop();
Num x1738 = x1736.i32_add(x1734);
Stack.push(x1738);
bool x1739 = allConcrete(x1737, x1735);
SymVal x1740 = x1739 ? Concrete(x1738, 32) : x1737.add(x1735);
SymStack.push(x1740);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x1741 = Stack.pop();
SymVal x1742 = SymStack.pop();
Num x1743 = Stack.pop();
SymVal x1744 = SymStack.pop();
Num x1745 = x1743.i32_add(x1741);
Stack.push(x1745);
bool x1746 = allConcrete(x1744, x1742);
SymVal x1747 = x1746 ? Concrete(x1745, 32) : x1744.add(x1742);
SymStack.push(x1747);
}
{
Num x1748 = Stack.pop();
SymStack.pop();
Num x1749 = I32V(Memory.loadInt(x1748.toInt(), 8));
SymVal x1750 = SymMemory.loadSym(x1748.toInt(), 8);
Stack.push(x1749);
SymStack.push(x1750);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x1751 = Stack.pop();
Num x1752 = Stack.pop();
SymVal x1753 = SymStack.pop();
SymVal x1754 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x1752);
Frames.set(1, x1751);
SymFrames.set(0, x1754);
SymFrames.set(1, x1753);
updateCurrentMCont(prependCont(x1706, CURRENT_MCONT));
}
__attribute__((musttail)) return x1678(std::monostate{});
return std::monostate{};
}
std::monostate x1706(std::monostate x1707) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
__attribute__((musttail)) return x1704(std::monostate{});
return std::monostate{};
}
std::monostate x1704(std::monostate x1705) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x1702(std::monostate{});
return std::monostate{};
}
std::monostate x1702(std::monostate x1703) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x1625(std::monostate{});
return std::monostate{};
}
std::monostate x1687(std::monostate x1688) {
info("Entering the false branch 19 of the if");
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x1689 = Stack.pop();
SymStack.pop();
Num x1690 = I32V(Memory.loadInt(x1689.toInt(), 0));
SymVal x1691 = SymMemory.loadSym(x1689.toInt(), 0);
Stack.push(x1690);
SymStack.push(x1691);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1692 = Stack.pop();
SymVal x1693 = SymStack.pop();
Num x1694 = Stack.pop();
SymVal x1695 = SymStack.pop();
Num x1696 = x1694.i32_eq(x1692);
Stack.push(x1696);
bool x1697 = allConcrete(x1695, x1693);
SymVal x1698 = x1697 ? Concrete(x1696, 32) : x1695.eq(x1693).bool2bv();
SymStack.push(x1698);
}
Num x1699 = Stack.pop();
{
SymVal x1700 = SymStack.pop();
ExploreTree.fillIfElseNode(x1700, 20);
}
int x1701 = x1699.toInt();
if (x1701 != 0) {
ExploreTree.moveCursor(true, makeControl(x1631, CURRENT_MCONT));
__attribute__((musttail)) return x1685(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1685, CURRENT_MCONT));
__attribute__((musttail)) return x1631(std::monostate{});
}
return std::monostate{};
}
std::monostate x1685(std::monostate x1686) {
info("Entering the true branch 20 of the if");
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
__attribute__((musttail)) return x1683(std::monostate{});
return std::monostate{};
}
std::monostate x1683(std::monostate x1684) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x1681(std::monostate{});
return std::monostate{};
}
std::monostate x1681(std::monostate x1682) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x1679(std::monostate{});
return std::monostate{};
}
std::monostate x1679(std::monostate x1680) {
infoWhen("CALL", "Exiting the function at 8, stackSize =", Stack.size());
Frames.popFrameCallee(1);
SymFrames.popFrameCallee(1);
return enterCC(std::monostate());
}
std::monostate x1631(std::monostate x1632) {
info("Entering the false branch 20 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1633 = Stack.pop();
SymStack.pop();
Num x1634 = I32V(Memory.loadInt(x1633.toInt(), 0));
SymVal x1635 = SymMemory.loadSym(x1633.toInt(), 0);
Stack.push(x1634);
SymStack.push(x1635);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1636 = Stack.pop();
SymVal x1637 = SymStack.pop();
Num x1638 = Stack.pop();
SymVal x1639 = SymStack.pop();
Num x1640 = x1638.i32_sub(x1636);
Stack.push(x1640);
bool x1641 = allConcrete(x1639, x1637);
SymVal x1642 = x1641 ? Concrete(x1640, 32) : x1639.minus(x1637);
SymStack.push(x1642);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x1643 = Stack.pop();
SymVal x1644 = SymStack.pop();
Num x1645 = Stack.pop();
SymVal x1646 = SymStack.pop();
Num x1647 = x1645.i32_mul(x1643);
Stack.push(x1647);
bool x1648 = allConcrete(x1646, x1644);
SymVal x1649 = x1648 ? Concrete(x1647, 32) : x1646.mul(x1644);
SymStack.push(x1649);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x1650 = Stack.pop();
SymVal x1651 = SymStack.pop();
Num x1652 = Stack.pop();
SymVal x1653 = SymStack.pop();
Num x1654 = x1652.i32_mul(x1650);
Stack.push(x1654);
bool x1655 = allConcrete(x1653, x1651);
SymVal x1656 = x1655 ? Concrete(x1654, 32) : x1653.mul(x1651);
SymStack.push(x1656);
}
{
Num x1657 = Stack.pop();
SymVal x1658 = SymStack.pop();
Num x1659 = Stack.pop();
SymVal x1660 = SymStack.pop();
Num x1661 = x1659.i32_add(x1657);
Stack.push(x1661);
bool x1662 = allConcrete(x1660, x1658);
SymVal x1663 = x1662 ? Concrete(x1661, 32) : x1660.add(x1658);
SymStack.push(x1663);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x1664 = Stack.pop();
SymVal x1665 = SymStack.pop();
Num x1666 = Stack.pop();
SymVal x1667 = SymStack.pop();
Num x1668 = x1666.i32_add(x1664);
Stack.push(x1668);
bool x1669 = allConcrete(x1667, x1665);
SymVal x1670 = x1669 ? Concrete(x1668, 32) : x1667.add(x1665);
SymStack.push(x1670);
}
{
Num x1671 = Stack.pop();
SymStack.pop();
Num x1672 = I32V(Memory.loadInt(x1671.toInt(), 8));
SymVal x1673 = SymMemory.loadSym(x1671.toInt(), 8);
Stack.push(x1672);
SymStack.push(x1673);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 8);
Num x1674 = Stack.pop();
Num x1675 = Stack.pop();
SymVal x1676 = SymStack.pop();
SymVal x1677 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x1675);
Frames.set(1, x1674);
SymFrames.set(0, x1677);
SymFrames.set(1, x1676);
updateCurrentMCont(prependCont(x1629, CURRENT_MCONT));
}
__attribute__((musttail)) return x1678(std::monostate{});
return std::monostate{};
}
std::monostate x1629(std::monostate x1630) {
infoWhen("CALL", "Returning from the function at 8, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
__attribute__((musttail)) return x1627(std::monostate{});
return std::monostate{};
}
std::monostate x1627(std::monostate x1628) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x1625(std::monostate{});
return std::monostate{};
}
std::monostate x1625(std::monostate x1626) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x1623(std::monostate{});
return std::monostate{};
}
std::monostate x1623(std::monostate x1624) {
infoWhen("CALL", "Exiting the function at 8, stackSize =", Stack.size());
Frames.popFrameCallee(1);
SymFrames.popFrameCallee(1);
return enterCC(std::monostate());
}
std::monostate x1593(std::monostate x1594) {
infoWhen("CALL", "Entered the function at 11, stackSize =", Stack.size());
Frames.pushFrameCallee(2);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1595 = Stack.pop();
SymStack.pop();
Num x1596 = I32V(Memory.loadInt(x1595.toInt(), 8));
SymVal x1597 = SymMemory.loadSym(x1595.toInt(), 8);
Stack.push(x1596);
SymStack.push(x1597);
}
{
Num x1598 = Stack.peek();
SymVal x1599 = SymStack.peek();
Frames.set(2, x1598);
SymFrames.set(2, x1599);
}
{
Num x1600 = Stack.pop();
SymStack.pop();
Num x1601 = I32V(Memory.loadInt(x1600.toInt(), 4));
SymVal x1602 = SymMemory.loadSym(x1600.toInt(), 4);
Stack.push(x1601);
SymStack.push(x1602);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1603 = Stack.pop();
SymStack.pop();
Num x1604 = I32V(Memory.loadInt(x1603.toInt(), 0));
SymVal x1605 = SymMemory.loadSym(x1603.toInt(), 0);
Stack.push(x1604);
SymStack.push(x1605);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1606 = Stack.pop();
SymVal x1607 = SymStack.pop();
Num x1608 = Stack.pop();
SymVal x1609 = SymStack.pop();
Num x1610 = x1608.i32_sub(x1606);
Stack.push(x1610);
bool x1611 = allConcrete(x1609, x1607);
SymVal x1612 = x1611 ? Concrete(x1610, 32) : x1609.minus(x1607);
SymStack.push(x1612);
}
{
Num x1613 = Stack.pop();
SymVal x1614 = SymStack.pop();
Num x1615 = Stack.pop();
SymVal x1616 = SymStack.pop();
Num x1617 = x1615.i32_eq(x1613);
Stack.push(x1617);
bool x1618 = allConcrete(x1616, x1614);
SymVal x1619 = x1618 ? Concrete(x1617, 32) : x1616.eq(x1614).bool2bv();
SymStack.push(x1619);
}
Num x1620 = Stack.pop();
{
SymVal x1621 = SymStack.pop();
ExploreTree.fillIfElseNode(x1621, 2);
}
int x1622 = x1620.toInt();
if (x1622 != 0) {
ExploreTree.moveCursor(true, makeControl(x1457, CURRENT_MCONT));
__attribute__((musttail)) return x1578(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1578, CURRENT_MCONT));
__attribute__((musttail)) return x1457(std::monostate{});
}
return std::monostate{};
}
std::monostate x1578(std::monostate x1579) {
info("Entering the true branch 2 of the if");
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1580 = Stack.pop();
SymStack.pop();
Num x1581 = I32V(Memory.grow(x1580.toInt()));
SymVal x1582 = Concrete(x1581, 32);
Stack.push(x1581);
SymStack.push(x1582);
}
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x1583 = Stack.pop();
SymVal x1584 = SymStack.pop();
Num x1585 = Stack.pop();
SymVal x1586 = SymStack.pop();
Num x1587 = x1585.i32_ne(x1583);
Stack.push(x1587);
bool x1588 = allConcrete(x1586, x1584);
SymVal x1589 = x1588 ? Concrete(x1587, 32) : x1586.neq(x1584).bool2bv();
SymStack.push(x1589);
}
Num x1590 = Stack.pop();
{
SymVal x1591 = SymStack.pop();
ExploreTree.fillIfElseNode(x1591, 17);
}
int x1592 = x1590.toInt();
if (x1592 != 0) {
ExploreTree.moveCursor(true, makeControl(x1469, CURRENT_MCONT));
__attribute__((musttail)) return x1485(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1485, CURRENT_MCONT));
__attribute__((musttail)) return x1469(std::monostate{});
}
return std::monostate{};
}
std::monostate x1485(std::monostate x1486) {
info("Entering the true branch 17 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1487 = Stack.pop();
SymStack.pop();
Num x1488 = I32V(Memory.loadInt(x1487.toInt(), 4));
SymVal x1489 = SymMemory.loadSym(x1487.toInt(), 4);
Stack.push(x1488);
SymStack.push(x1489);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1490 = Stack.pop();
SymVal x1491 = SymStack.pop();
Num x1492 = Stack.pop();
SymVal x1493 = SymStack.pop();
Num x1494 = x1492.i32_add(x1490);
Stack.push(x1494);
bool x1495 = allConcrete(x1493, x1491);
SymVal x1496 = x1495 ? Concrete(x1494, 32) : x1493.add(x1491);
SymStack.push(x1496);
}
Stack.push(I32V(65536));
SymStack.push(Concrete(I32V(65536), 32));
{
Num x1497 = Stack.pop();
SymVal x1498 = SymStack.pop();
Num x1499 = Stack.pop();
SymVal x1500 = SymStack.pop();
Num x1501 = x1499.i32_mul(x1497);
Stack.push(x1501);
bool x1502 = allConcrete(x1500, x1498);
SymVal x1503 = x1502 ? Concrete(x1501, 32) : x1500.mul(x1498);
SymStack.push(x1503);
}
{
Num x1504 = Stack.pop();
SymVal x1505 = SymStack.pop();
Frames.set(1, x1504);
SymFrames.set(1, x1505);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1506 = Stack.pop();
SymStack.pop();
Num x1507 = I32V(Memory.loadInt(x1506.toInt(), 4));
SymVal x1508 = SymMemory.loadSym(x1506.toInt(), 4);
Stack.push(x1507);
SymStack.push(x1508);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1509 = Stack.pop();
SymVal x1510 = SymStack.pop();
Num x1511 = Stack.pop();
SymVal x1512 = SymStack.pop();
Num x1513 = x1511.i32_add(x1509);
Stack.push(x1513);
bool x1514 = allConcrete(x1512, x1510);
SymVal x1515 = x1514 ? Concrete(x1513, 32) : x1512.add(x1510);
SymStack.push(x1515);
}
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
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x1520 = Stack.pop();
SymVal x1521 = SymStack.pop();
Num x1522 = Stack.pop();
SymStack.pop();
int x1523 = x1522.toInt();
Memory.storeInt(x1523, 8, x1520.toInt());
SymMemory.storeSym(x1523, 8, x1521);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1524 = Stack.pop();
SymVal x1525 = SymStack.pop();
Num x1526 = Stack.pop();
SymStack.pop();
int x1527 = x1526.toInt();
Memory.storeInt(x1527, 0, x1524.toInt());
SymMemory.storeSym(x1527, 0, x1525);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1528 = Stack.pop();
SymVal x1529 = SymStack.pop();
Num x1530 = Stack.pop();
SymStack.pop();
int x1531 = x1530.toInt();
Memory.storeInt(x1531, 4, x1528.toInt());
SymMemory.storeSym(x1531, 4, x1529);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1532 = Stack.pop();
SymStack.pop();
Num x1533 = I32V(Memory.loadInt(x1532.toInt(), 0));
SymVal x1534 = SymMemory.loadSym(x1532.toInt(), 0);
Stack.push(x1533);
SymStack.push(x1534);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1535 = Stack.pop();
SymVal x1536 = SymStack.pop();
Num x1537 = Stack.pop();
SymVal x1538 = SymStack.pop();
Num x1539 = x1537.i32_sub(x1535);
Stack.push(x1539);
bool x1540 = allConcrete(x1538, x1536);
SymVal x1541 = x1540 ? Concrete(x1539, 32) : x1538.minus(x1536);
SymStack.push(x1541);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x1542 = Stack.pop();
SymVal x1543 = SymStack.pop();
Num x1544 = Stack.pop();
SymVal x1545 = SymStack.pop();
Num x1546 = x1544.i32_mul(x1542);
Stack.push(x1546);
bool x1547 = allConcrete(x1545, x1543);
SymVal x1548 = x1547 ? Concrete(x1546, 32) : x1545.mul(x1543);
SymStack.push(x1548);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x1549 = Stack.pop();
SymVal x1550 = SymStack.pop();
Num x1551 = Stack.pop();
SymVal x1552 = SymStack.pop();
Num x1553 = x1551.i32_mul(x1549);
Stack.push(x1553);
bool x1554 = allConcrete(x1552, x1550);
SymVal x1555 = x1554 ? Concrete(x1553, 32) : x1552.mul(x1550);
SymStack.push(x1555);
}
{
Num x1556 = Stack.pop();
SymVal x1557 = SymStack.pop();
Num x1558 = Stack.pop();
SymVal x1559 = SymStack.pop();
Num x1560 = x1558.i32_add(x1556);
Stack.push(x1560);
bool x1561 = allConcrete(x1559, x1557);
SymVal x1562 = x1561 ? Concrete(x1560, 32) : x1559.add(x1557);
SymStack.push(x1562);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x1563 = Stack.pop();
SymVal x1564 = SymStack.pop();
Num x1565 = Stack.pop();
SymVal x1566 = SymStack.pop();
Num x1567 = x1565.i32_add(x1563);
Stack.push(x1567);
bool x1568 = allConcrete(x1566, x1564);
SymVal x1569 = x1568 ? Concrete(x1567, 32) : x1566.add(x1564);
SymStack.push(x1569);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x1570 = Stack.pop();
SymVal x1571 = SymStack.pop();
Num x1572 = Stack.pop();
SymStack.pop();
int x1573 = x1572.toInt();
Memory.storeInt(x1573, 8, x1570.toInt());
SymMemory.storeSym(x1573, 8, x1571);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 9);
Num x1574 = Stack.pop();
Num x1575 = Stack.pop();
SymVal x1576 = SymStack.pop();
SymVal x1577 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x1575);
Frames.set(1, x1574);
SymFrames.set(0, x1577);
SymFrames.set(1, x1576);
updateCurrentMCont(prependCont(x1479, CURRENT_MCONT));
}
__attribute__((musttail)) return x1066(std::monostate{});
return std::monostate{};
}
std::monostate x1479(std::monostate x1480) {
infoWhen("CALL", "Returning from the function at 9, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 10);
Num x1481 = Stack.pop();
Num x1482 = Stack.pop();
SymVal x1483 = SymStack.pop();
SymVal x1484 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x1482);
Frames.set(1, x1481);
SymFrames.set(0, x1484);
SymFrames.set(1, x1483);
updateCurrentMCont(prependCont(x1477, CURRENT_MCONT));
}
__attribute__((musttail)) return x118(std::monostate{});
return std::monostate{};
}
std::monostate x1477(std::monostate x1478) {
infoWhen("CALL", "Returning from the function at 10, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
__attribute__((musttail)) return x1475(std::monostate{});
return std::monostate{};
}
std::monostate x1475(std::monostate x1476) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x1473(std::monostate{});
return std::monostate{};
}
std::monostate x1473(std::monostate x1474) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x1471(std::monostate{});
return std::monostate{};
}
std::monostate x1471(std::monostate x1472) {
infoWhen("CALL", "Exiting the function at 11, stackSize =", Stack.size());
Frames.popFrameCallee(2);
SymFrames.popFrameCallee(2);
return enterCC(std::monostate());
}
std::monostate x1469(std::monostate x1470) {
info("Entering the false branch 17 of the if");
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
__attribute__((musttail)) return x1467(std::monostate{});
return std::monostate{};
}
std::monostate x1467(std::monostate x1468) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x1465(std::monostate{});
return std::monostate{};
}
std::monostate x1465(std::monostate x1466) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x1463(std::monostate{});
return std::monostate{};
}
std::monostate x1463(std::monostate x1464) {
infoWhen("CALL", "Exiting the function at 11, stackSize =", Stack.size());
Frames.popFrameCallee(2);
SymFrames.popFrameCallee(2);
return enterCC(std::monostate());
}
std::monostate x1457(std::monostate x1458) {
info("Entering the false branch 2 of the if");
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 10);
Num x1459 = Stack.pop();
Num x1460 = Stack.pop();
SymVal x1461 = SymStack.pop();
SymVal x1462 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x1460);
Frames.set(1, x1459);
SymFrames.set(0, x1462);
SymFrames.set(1, x1461);
updateCurrentMCont(prependCont(x1455, CURRENT_MCONT));
}
__attribute__((musttail)) return x118(std::monostate{});
return std::monostate{};
}
std::monostate x1455(std::monostate x1456) {
infoWhen("CALL", "Returning from the function at 10, stackSize =", Stack.size());
Frames.popFrameCaller(2);
SymFrames.popFrameCaller(2);
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
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
std::monostate x118(std::monostate x1425) {
infoWhen("CALL", "Entered the function at 10, stackSize =", Stack.size());
Frames.pushFrameCallee(1);
SymFrames.pushFrameSlot(32);
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x1426 = Stack.pop();
SymStack.pop();
Num x1427 = I32V(Memory.loadInt(x1426.toInt(), 4));
SymVal x1428 = SymMemory.loadSym(x1426.toInt(), 4);
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
Num x1433 = x1431.i32_sub(x1429);
Stack.push(x1433);
bool x1434 = allConcrete(x1432, x1430);
SymVal x1435 = x1434 ? Concrete(x1433, 32) : x1432.minus(x1430);
SymStack.push(x1435);
}
{
Num x1436 = Stack.pop();
SymVal x1437 = SymStack.pop();
Frames.set(2, x1436);
SymFrames.set(2, x1437);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x1438 = Stack.pop();
SymStack.pop();
Num x1439 = I32V(Memory.loadInt(x1438.toInt(), 0));
SymVal x1440 = SymMemory.loadSym(x1438.toInt(), 0);
Stack.push(x1439);
SymStack.push(x1440);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1441 = Stack.pop();
SymVal x1442 = SymStack.pop();
Num x1443 = Stack.pop();
SymVal x1444 = SymStack.pop();
Num x1445 = x1443.i32_eq(x1441);
Stack.push(x1445);
bool x1446 = allConcrete(x1444, x1442);
SymVal x1447 = x1446 ? Concrete(x1445, 32) : x1444.eq(x1442).bool2bv();
SymStack.push(x1447);
}
Num x1448 = Stack.pop();
{
SymVal x1449 = SymStack.pop();
ExploreTree.fillIfElseNode(x1449, 3);
}
int x1450 = x1448.toInt();
if (x1450 != 0) {
ExploreTree.moveCursor(true, makeControl(x1264, CURRENT_MCONT));
__attribute__((musttail)) return x1423(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1423, CURRENT_MCONT));
__attribute__((musttail)) return x1264(std::monostate{});
}
return std::monostate{};
}
std::monostate x1423(std::monostate x1424) {
info("Entering the true branch 3 of the if");
__attribute__((musttail)) return x1421(std::monostate{});
return std::monostate{};
}
std::monostate x1421(std::monostate x1422) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x1362(std::monostate{});
return std::monostate{};
}
std::monostate x1362(std::monostate x1410) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1411 = Stack.pop();
SymVal x1412 = SymStack.pop();
Num x1413 = Stack.pop();
SymVal x1414 = SymStack.pop();
Num x1415 = x1413.i32_ge_s(x1411);
Stack.push(x1415);
bool x1416 = allConcrete(x1414, x1412);
SymVal x1417 = x1416 ? Concrete(x1415, 32) : x1414.ge(x1412).bool2bv();
SymStack.push(x1417);
}
Num x1418 = Stack.pop();
{
SymVal x1419 = SymStack.pop();
ExploreTree.fillIfElseNode(x1419, 15);
}
int x1420 = x1418.toInt();
if (x1420 != 0) {
ExploreTree.moveCursor(true, makeControl(x1382, CURRENT_MCONT));
__attribute__((musttail)) return x1384(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1384, CURRENT_MCONT));
__attribute__((musttail)) return x1382(std::monostate{});
}
return std::monostate{};
}
std::monostate x1384(std::monostate x1385) {
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
Num x1386 = Stack.pop();
SymVal x1387 = SymStack.pop();
Num x1388 = Stack.pop();
SymVal x1389 = SymStack.pop();
Num x1390 = x1388.i32_mul(x1386);
Stack.push(x1390);
bool x1391 = allConcrete(x1389, x1387);
SymVal x1392 = x1391 ? Concrete(x1390, 32) : x1389.mul(x1387);
SymStack.push(x1392);
}
{
Num x1393 = Stack.pop();
SymVal x1394 = SymStack.pop();
Num x1395 = Stack.pop();
SymVal x1396 = SymStack.pop();
Num x1397 = x1395.i32_add(x1393);
Stack.push(x1397);
bool x1398 = allConcrete(x1396, x1394);
SymVal x1399 = x1398 ? Concrete(x1397, 32) : x1396.add(x1394);
SymStack.push(x1399);
}
{
Num x1400 = Stack.pop();
SymStack.pop();
Num x1401 = I32V(Memory.loadInt(x1400.toInt(), 8));
SymVal x1402 = SymMemory.loadSym(x1400.toInt(), 8);
Stack.push(x1401);
SymStack.push(x1402);
}
{
Num x1403 = Stack.pop();
SymVal x1404 = SymStack.pop();
Num x1405 = Stack.pop();
SymVal x1406 = SymStack.pop();
Num x1407 = x1405.i32_lt_s(x1403);
Stack.push(x1407);
bool x1408 = allConcrete(x1406, x1404);
SymVal x1409 = x1408 ? Concrete(x1407, 32) : x1406.lt(x1404).bool2bv();
SymStack.push(x1409);
}
__attribute__((musttail)) return x1363(std::monostate{});
return std::monostate{};
}
std::monostate x1382(std::monostate x1383) {
info("Entering the false branch 15 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
__attribute__((musttail)) return x1363(std::monostate{});
return std::monostate{};
}
std::monostate x1363(std::monostate x1364) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1365 = Stack.pop();
SymVal x1366 = SymStack.pop();
Num x1367 = Stack.pop();
SymVal x1368 = SymStack.pop();
Num x1369 = x1367.i32_ge_s(x1365);
Stack.push(x1369);
bool x1370 = allConcrete(x1368, x1366);
SymVal x1371 = x1370 ? Concrete(x1369, 32) : x1368.ge(x1366).bool2bv();
SymStack.push(x1371);
}
{
Num x1372 = Stack.pop();
SymVal x1373 = SymStack.pop();
Num x1374 = Stack.pop();
SymVal x1375 = SymStack.pop();
Num x1376 = x1374.i32_and(x1372);
Stack.push(x1376);
bool x1377 = allConcrete(x1375, x1373);
SymVal x1378 = x1377 ? Concrete(x1376, 32) : x1375.bitwise_and(x1373);
SymStack.push(x1378);
}
Num x1379 = Stack.pop();
{
SymVal x1380 = SymStack.pop();
ExploreTree.fillIfElseNode(x1380, 16);
}
int x1381 = x1379.toInt();
if (x1381 != 0) {
ExploreTree.moveCursor(true, makeControl(x1307, CURRENT_MCONT));
__attribute__((musttail)) return x1309(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1309, CURRENT_MCONT));
__attribute__((musttail)) return x1307(std::monostate{});
}
return std::monostate{};
}
std::monostate x1309(std::monostate x1310) {
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
Num x1311 = Stack.pop();
SymVal x1312 = SymStack.pop();
Num x1313 = Stack.pop();
SymVal x1314 = SymStack.pop();
Num x1315 = x1313.i32_add(x1311);
Stack.push(x1315);
bool x1316 = allConcrete(x1314, x1312);
SymVal x1317 = x1316 ? Concrete(x1315, 32) : x1314.add(x1312);
SymStack.push(x1317);
}
{
Num x1318 = Stack.pop();
SymVal x1319 = SymStack.pop();
Num x1320 = Stack.pop();
SymVal x1321 = SymStack.pop();
Num x1322 = x1320.i32_mul(x1318);
Stack.push(x1322);
bool x1323 = allConcrete(x1321, x1319);
SymVal x1324 = x1323 ? Concrete(x1322, 32) : x1321.mul(x1319);
SymStack.push(x1324);
}
{
Num x1325 = Stack.pop();
SymVal x1326 = SymStack.pop();
Num x1327 = Stack.pop();
SymVal x1328 = SymStack.pop();
Num x1329 = x1327.i32_add(x1325);
Stack.push(x1329);
bool x1330 = allConcrete(x1328, x1326);
SymVal x1331 = x1330 ? Concrete(x1329, 32) : x1328.add(x1326);
SymStack.push(x1331);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x1332 = Stack.pop();
SymVal x1333 = SymStack.pop();
Num x1334 = Stack.pop();
SymVal x1335 = SymStack.pop();
Num x1336 = x1334.i32_mul(x1332);
Stack.push(x1336);
bool x1337 = allConcrete(x1335, x1333);
SymVal x1338 = x1337 ? Concrete(x1336, 32) : x1335.mul(x1333);
SymStack.push(x1338);
}
{
Num x1339 = Stack.pop();
SymVal x1340 = SymStack.pop();
Num x1341 = Stack.pop();
SymVal x1342 = SymStack.pop();
Num x1343 = x1341.i32_add(x1339);
Stack.push(x1343);
bool x1344 = allConcrete(x1342, x1340);
SymVal x1345 = x1344 ? Concrete(x1343, 32) : x1342.add(x1340);
SymStack.push(x1345);
}
{
Num x1346 = Stack.pop();
SymStack.pop();
Num x1347 = I32V(Memory.loadInt(x1346.toInt(), 8));
SymVal x1348 = SymMemory.loadSym(x1346.toInt(), 8);
Stack.push(x1347);
SymStack.push(x1348);
}
{
Num x1349 = Stack.pop();
SymVal x1350 = SymStack.pop();
Num x1351 = Stack.pop();
SymStack.pop();
int x1352 = x1351.toInt();
Memory.storeInt(x1352, 8, x1349.toInt());
SymMemory.storeSym(x1352, 8, x1350);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1353 = Stack.pop();
SymVal x1354 = SymStack.pop();
Num x1355 = Stack.pop();
SymVal x1356 = SymStack.pop();
Num x1357 = x1355.i32_sub(x1353);
Stack.push(x1357);
bool x1358 = allConcrete(x1356, x1354);
SymVal x1359 = x1358 ? Concrete(x1357, 32) : x1356.minus(x1354);
SymStack.push(x1359);
}
{
Num x1360 = Stack.pop();
SymVal x1361 = SymStack.pop();
Frames.set(2, x1360);
SymFrames.set(2, x1361);
}
info("Jump to 1");
__attribute__((musttail)) return x1362(std::monostate{});
return std::monostate{};
}
std::monostate x1307(std::monostate x1308) {
info("Entering the false branch 16 of the if");
info("Jump to 2");
__attribute__((musttail)) return x1266(std::monostate{});
return std::monostate{};
}
std::monostate x1266(std::monostate x1267) {
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
Num x1268 = Stack.pop();
SymVal x1269 = SymStack.pop();
Num x1270 = Stack.pop();
SymVal x1271 = SymStack.pop();
Num x1272 = x1270.i32_add(x1268);
Stack.push(x1272);
bool x1273 = allConcrete(x1271, x1269);
SymVal x1274 = x1273 ? Concrete(x1272, 32) : x1271.add(x1269);
SymStack.push(x1274);
}
{
Num x1275 = Stack.pop();
SymVal x1276 = SymStack.pop();
Num x1277 = Stack.pop();
SymVal x1278 = SymStack.pop();
Num x1279 = x1277.i32_mul(x1275);
Stack.push(x1279);
bool x1280 = allConcrete(x1278, x1276);
SymVal x1281 = x1280 ? Concrete(x1279, 32) : x1278.mul(x1276);
SymStack.push(x1281);
}
{
Num x1282 = Stack.pop();
SymVal x1283 = SymStack.pop();
Num x1284 = Stack.pop();
SymVal x1285 = SymStack.pop();
Num x1286 = x1284.i32_add(x1282);
Stack.push(x1286);
bool x1287 = allConcrete(x1285, x1283);
SymVal x1288 = x1287 ? Concrete(x1286, 32) : x1285.add(x1283);
SymStack.push(x1288);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
{
Num x1289 = Stack.pop();
SymVal x1290 = SymStack.pop();
Num x1291 = Stack.pop();
SymStack.pop();
int x1292 = x1291.toInt();
Memory.storeInt(x1292, 8, x1289.toInt());
SymMemory.storeSym(x1292, 8, x1290);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x1293 = Stack.pop();
SymStack.pop();
Num x1294 = I32V(Memory.loadInt(x1293.toInt(), 4));
SymVal x1295 = SymMemory.loadSym(x1293.toInt(), 4);
Stack.push(x1294);
SymStack.push(x1295);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1296 = Stack.pop();
SymVal x1297 = SymStack.pop();
Num x1298 = Stack.pop();
SymVal x1299 = SymStack.pop();
Num x1300 = x1298.i32_add(x1296);
Stack.push(x1300);
bool x1301 = allConcrete(x1299, x1297);
SymVal x1302 = x1301 ? Concrete(x1300, 32) : x1299.add(x1297);
SymStack.push(x1302);
}
{
Num x1303 = Stack.pop();
SymVal x1304 = SymStack.pop();
Num x1305 = Stack.pop();
SymStack.pop();
int x1306 = x1305.toInt();
Memory.storeInt(x1306, 4, x1303.toInt());
SymMemory.storeSym(x1306, 4, x1304);
}
__attribute__((musttail)) return x67(std::monostate{});
return std::monostate{};
}
std::monostate x1264(std::monostate x1265) {
info("Entering the false branch 3 of the if");
__attribute__((musttail)) return x1262(std::monostate{});
return std::monostate{};
}
std::monostate x1262(std::monostate x1263) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x1221(std::monostate{});
return std::monostate{};
}
std::monostate x1221(std::monostate x1251) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1252 = Stack.pop();
SymVal x1253 = SymStack.pop();
Num x1254 = Stack.pop();
SymVal x1255 = SymStack.pop();
Num x1256 = x1254.i32_ge_s(x1252);
Stack.push(x1256);
bool x1257 = allConcrete(x1255, x1253);
SymVal x1258 = x1257 ? Concrete(x1256, 32) : x1255.ge(x1253).bool2bv();
SymStack.push(x1258);
}
Num x1259 = Stack.pop();
{
SymVal x1260 = SymStack.pop();
ExploreTree.fillIfElseNode(x1260, 4);
}
int x1261 = x1259.toInt();
if (x1261 != 0) {
ExploreTree.moveCursor(true, makeControl(x1206, CURRENT_MCONT));
__attribute__((musttail)) return x1222(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1222, CURRENT_MCONT));
__attribute__((musttail)) return x1206(std::monostate{});
}
return std::monostate{};
}
std::monostate x1222(std::monostate x1223) {
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
Num x1224 = Stack.pop();
SymVal x1225 = SymStack.pop();
Num x1226 = Stack.pop();
SymVal x1227 = SymStack.pop();
Num x1228 = x1226.i32_mul(x1224);
Stack.push(x1228);
bool x1229 = allConcrete(x1227, x1225);
SymVal x1230 = x1229 ? Concrete(x1228, 32) : x1227.mul(x1225);
SymStack.push(x1230);
}
{
Num x1231 = Stack.pop();
SymVal x1232 = SymStack.pop();
Num x1233 = Stack.pop();
SymVal x1234 = SymStack.pop();
Num x1235 = x1233.i32_add(x1231);
Stack.push(x1235);
bool x1236 = allConcrete(x1234, x1232);
SymVal x1237 = x1236 ? Concrete(x1235, 32) : x1234.add(x1232);
SymStack.push(x1237);
}
{
Num x1238 = Stack.pop();
SymStack.pop();
Num x1239 = I32V(Memory.loadInt(x1238.toInt(), 8));
SymVal x1240 = SymMemory.loadSym(x1238.toInt(), 8);
Stack.push(x1239);
SymStack.push(x1240);
}
{
Num x1241 = Stack.pop();
SymVal x1242 = SymStack.pop();
Num x1243 = Stack.pop();
SymVal x1244 = SymStack.pop();
Num x1245 = x1243.i32_lt_s(x1241);
Stack.push(x1245);
bool x1246 = allConcrete(x1244, x1242);
SymVal x1247 = x1246 ? Concrete(x1245, 32) : x1244.lt(x1242).bool2bv();
SymStack.push(x1247);
}
Num x1248 = Stack.pop();
{
SymVal x1249 = SymStack.pop();
ExploreTree.fillIfElseNode(x1249, 14);
}
int x1250 = x1248.toInt();
if (x1250 != 0) {
ExploreTree.moveCursor(true, makeControl(x1208, CURRENT_MCONT));
__attribute__((musttail)) return x1210(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1210, CURRENT_MCONT));
__attribute__((musttail)) return x1208(std::monostate{});
}
return std::monostate{};
}
std::monostate x1210(std::monostate x1211) {
info("Entering the true branch 14 of the if");
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1212 = Stack.pop();
SymVal x1213 = SymStack.pop();
Num x1214 = Stack.pop();
SymVal x1215 = SymStack.pop();
Num x1216 = x1214.i32_sub(x1212);
Stack.push(x1216);
bool x1217 = allConcrete(x1215, x1213);
SymVal x1218 = x1217 ? Concrete(x1216, 32) : x1215.minus(x1213);
SymStack.push(x1218);
}
{
Num x1219 = Stack.pop();
SymVal x1220 = SymStack.pop();
Frames.set(2, x1219);
SymFrames.set(2, x1220);
}
info("Jump to 2");
__attribute__((musttail)) return x1221(std::monostate{});
return std::monostate{};
}
std::monostate x1208(std::monostate x1209) {
info("Entering the false branch 14 of the if");
info("Jump to 3");
__attribute__((musttail)) return x1131(std::monostate{});
return std::monostate{};
}
std::monostate x1206(std::monostate x1207) {
info("Entering the false branch 4 of the if");
info("Jump to 2");
__attribute__((musttail)) return x1131(std::monostate{});
return std::monostate{};
}
std::monostate x1131(std::monostate x1132) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1133 = Stack.pop();
SymVal x1134 = SymStack.pop();
Num x1135 = Stack.pop();
SymVal x1136 = SymStack.pop();
Num x1137 = x1135.i32_add(x1133);
Stack.push(x1137);
bool x1138 = allConcrete(x1136, x1134);
SymVal x1139 = x1138 ? Concrete(x1137, 32) : x1136.add(x1134);
SymStack.push(x1139);
}
{
Num x1140 = Stack.pop();
SymVal x1141 = SymStack.pop();
Frames.set(2, x1140);
SymFrames.set(2, x1141);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1142 = Stack.pop();
SymStack.pop();
Num x1143 = I32V(Memory.loadInt(x1142.toInt(), 0));
SymVal x1144 = SymMemory.loadSym(x1142.toInt(), 0);
Stack.push(x1143);
SymStack.push(x1144);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1145 = Stack.pop();
SymVal x1146 = SymStack.pop();
Num x1147 = Stack.pop();
SymVal x1148 = SymStack.pop();
Num x1149 = x1147.i32_sub(x1145);
Stack.push(x1149);
bool x1150 = allConcrete(x1148, x1146);
SymVal x1151 = x1150 ? Concrete(x1149, 32) : x1148.minus(x1146);
SymStack.push(x1151);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x1152 = Stack.pop();
SymVal x1153 = SymStack.pop();
Num x1154 = Stack.pop();
SymVal x1155 = SymStack.pop();
Num x1156 = x1154.i32_mul(x1152);
Stack.push(x1156);
bool x1157 = allConcrete(x1155, x1153);
SymVal x1158 = x1157 ? Concrete(x1156, 32) : x1155.mul(x1153);
SymStack.push(x1158);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x1159 = Stack.pop();
SymVal x1160 = SymStack.pop();
Num x1161 = Stack.pop();
SymVal x1162 = SymStack.pop();
Num x1163 = x1161.i32_mul(x1159);
Stack.push(x1163);
bool x1164 = allConcrete(x1162, x1160);
SymVal x1165 = x1164 ? Concrete(x1163, 32) : x1162.mul(x1160);
SymStack.push(x1165);
}
{
Num x1166 = Stack.pop();
SymVal x1167 = SymStack.pop();
Num x1168 = Stack.pop();
SymVal x1169 = SymStack.pop();
Num x1170 = x1168.i32_add(x1166);
Stack.push(x1170);
bool x1171 = allConcrete(x1169, x1167);
SymVal x1172 = x1171 ? Concrete(x1170, 32) : x1169.add(x1167);
SymStack.push(x1172);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x1173 = Stack.pop();
SymVal x1174 = SymStack.pop();
Num x1175 = Stack.pop();
SymVal x1176 = SymStack.pop();
Num x1177 = x1175.i32_add(x1173);
Stack.push(x1177);
bool x1178 = allConcrete(x1176, x1174);
SymVal x1179 = x1178 ? Concrete(x1177, 32) : x1176.add(x1174);
SymStack.push(x1179);
}
{
Num x1180 = Stack.pop();
SymStack.pop();
Num x1181 = I32V(Memory.loadInt(x1180.toInt(), 8));
SymVal x1182 = SymMemory.loadSym(x1180.toInt(), 8);
Stack.push(x1181);
SymStack.push(x1182);
}
{
Num x1183 = Stack.pop();
SymStack.pop();
Num x1184 = I32V(Memory.loadInt(x1183.toInt(), 4));
SymVal x1185 = SymMemory.loadSym(x1183.toInt(), 4);
Stack.push(x1184);
SymStack.push(x1185);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1186 = Stack.pop();
SymStack.pop();
Num x1187 = I32V(Memory.loadInt(x1186.toInt(), 0));
SymVal x1188 = SymMemory.loadSym(x1186.toInt(), 0);
Stack.push(x1187);
SymStack.push(x1188);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1189 = Stack.pop();
SymVal x1190 = SymStack.pop();
Num x1191 = Stack.pop();
SymVal x1192 = SymStack.pop();
Num x1193 = x1191.i32_sub(x1189);
Stack.push(x1193);
bool x1194 = allConcrete(x1192, x1190);
SymVal x1195 = x1194 ? Concrete(x1193, 32) : x1192.minus(x1190);
SymStack.push(x1195);
}
{
Num x1196 = Stack.pop();
SymVal x1197 = SymStack.pop();
Num x1198 = Stack.pop();
SymVal x1199 = SymStack.pop();
Num x1200 = x1198.i32_eq(x1196);
Stack.push(x1200);
bool x1201 = allConcrete(x1199, x1197);
SymVal x1202 = x1201 ? Concrete(x1200, 32) : x1199.eq(x1197).bool2bv();
SymStack.push(x1202);
}
Num x1203 = Stack.pop();
{
SymVal x1204 = SymStack.pop();
ExploreTree.fillIfElseNode(x1204, 5);
}
int x1205 = x1203.toInt();
if (x1205 != 0) {
ExploreTree.moveCursor(true, makeControl(x119, CURRENT_MCONT));
__attribute__((musttail)) return x1125(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1125, CURRENT_MCONT));
__attribute__((musttail)) return x119(std::monostate{});
}
return std::monostate{};
}
std::monostate x1125(std::monostate x1126) {
info("Entering the true branch 5 of the if");
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
infoWhen("CALL", "Taking arguments from stack to call function at ", 9);
Num x1127 = Stack.pop();
Num x1128 = Stack.pop();
SymVal x1129 = SymStack.pop();
SymVal x1130 = SymStack.pop();
Frames.pushFrameCaller(2);
SymFrames.pushFramePtr();
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Frames.set(0, x1128);
Frames.set(1, x1127);
SymFrames.set(0, x1130);
SymFrames.set(1, x1129);
updateCurrentMCont(prependCont(x1096, CURRENT_MCONT));
}
__attribute__((musttail)) return x1066(std::monostate{});
return std::monostate{};
}
std::monostate x1096(std::monostate x1097) {
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
Num x1098 = Stack.pop();
SymVal x1099 = SymStack.pop();
Num x1100 = Stack.pop();
SymVal x1101 = SymStack.pop();
Num x1102 = x1100.i32_mul(x1098);
Stack.push(x1102);
bool x1103 = allConcrete(x1101, x1099);
SymVal x1104 = x1103 ? Concrete(x1102, 32) : x1101.mul(x1099);
SymStack.push(x1104);
}
{
Num x1105 = Stack.pop();
SymVal x1106 = SymStack.pop();
Num x1107 = Stack.pop();
SymVal x1108 = SymStack.pop();
Num x1109 = x1107.i32_add(x1105);
Stack.push(x1109);
bool x1110 = allConcrete(x1108, x1106);
SymVal x1111 = x1110 ? Concrete(x1109, 32) : x1108.add(x1106);
SymStack.push(x1111);
}
{
Num x1112 = Stack.pop();
SymStack.pop();
Num x1113 = I32V(Memory.loadInt(x1112.toInt(), 8));
SymVal x1114 = SymMemory.loadSym(x1112.toInt(), 8);
Stack.push(x1113);
SymStack.push(x1114);
}
{
Num x1115 = Stack.pop();
SymVal x1116 = SymStack.pop();
Num x1117 = Stack.pop();
SymVal x1118 = SymStack.pop();
Num x1119 = x1117.i32_gt_s(x1115);
Stack.push(x1119);
bool x1120 = allConcrete(x1118, x1116);
SymVal x1121 = x1120 ? Concrete(x1119, 32) : x1118.gt(x1116).bool2bv();
SymStack.push(x1121);
}
Num x1122 = Stack.pop();
{
SymVal x1123 = SymStack.pop();
ExploreTree.fillIfElseNode(x1123, 13);
}
int x1124 = x1122.toInt();
if (x1124 != 0) {
ExploreTree.moveCursor(true, makeControl(x1083, CURRENT_MCONT));
__attribute__((musttail)) return x1085(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x1085, CURRENT_MCONT));
__attribute__((musttail)) return x1083(std::monostate{});
}
return std::monostate{};
}
std::monostate x1085(std::monostate x1086) {
info("Entering the true branch 13 of the if");
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1087 = Stack.pop();
SymVal x1088 = SymStack.pop();
Num x1089 = Stack.pop();
SymVal x1090 = SymStack.pop();
Num x1091 = x1089.i32_add(x1087);
Stack.push(x1091);
bool x1092 = allConcrete(x1090, x1088);
SymVal x1093 = x1092 ? Concrete(x1091, 32) : x1090.add(x1088);
SymStack.push(x1093);
}
{
Num x1094 = Stack.pop();
SymVal x1095 = SymStack.pop();
Frames.set(2, x1094);
SymFrames.set(2, x1095);
}
__attribute__((musttail)) return x1081(std::monostate{});
return std::monostate{};
}
std::monostate x1083(std::monostate x1084) {
info("Entering the false branch 13 of the if");
__attribute__((musttail)) return x1081(std::monostate{});
return std::monostate{};
}
std::monostate x1081(std::monostate x1082) {
info("Exiting the if, stackSize =", Stack.size());
__attribute__((musttail)) return x71(std::monostate{});
return std::monostate{};
}
std::monostate x1066(std::monostate x1067) {
infoWhen("CALL", "Entered the function at 9, stackSize =", Stack.size());
Frames.pushFrameCallee(2);
SymFrames.pushFrameSlot(32);
SymFrames.pushFrameSlot(32);
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1068 = Stack.pop();
SymStack.pop();
Num x1069 = I32V(Memory.grow(x1068.toInt()));
SymVal x1070 = Concrete(x1069, 32);
Stack.push(x1069);
SymStack.push(x1070);
}
Stack.push(I32V(-1));
SymStack.push(Concrete(I32V(-1), 32));
{
Num x1071 = Stack.pop();
SymVal x1072 = SymStack.pop();
Num x1073 = Stack.pop();
SymVal x1074 = SymStack.pop();
Num x1075 = x1073.i32_ne(x1071);
Stack.push(x1075);
bool x1076 = allConcrete(x1074, x1072);
SymVal x1077 = x1076 ? Concrete(x1075, 32) : x1074.neq(x1072).bool2bv();
SymStack.push(x1077);
}
Num x1078 = Stack.pop();
{
SymVal x1079 = SymStack.pop();
ExploreTree.fillIfElseNode(x1079, 6);
}
int x1080 = x1078.toInt();
if (x1080 != 0) {
ExploreTree.moveCursor(true, makeControl(x125, CURRENT_MCONT));
__attribute__((musttail)) return x977(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x977, CURRENT_MCONT));
__attribute__((musttail)) return x125(std::monostate{});
}
return std::monostate{};
}
std::monostate x977(std::monostate x978) {
info("Entering the true branch 6 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x979 = Stack.pop();
SymStack.pop();
Num x980 = I32V(Memory.loadInt(x979.toInt(), 4));
SymVal x981 = SymMemory.loadSym(x979.toInt(), 4);
Stack.push(x980);
SymStack.push(x981);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x982 = Stack.pop();
SymVal x983 = SymStack.pop();
Num x984 = Stack.pop();
SymVal x985 = SymStack.pop();
Num x986 = x984.i32_add(x982);
Stack.push(x986);
bool x987 = allConcrete(x985, x983);
SymVal x988 = x987 ? Concrete(x986, 32) : x985.add(x983);
SymStack.push(x988);
}
Stack.push(I32V(65536));
SymStack.push(Concrete(I32V(65536), 32));
{
Num x989 = Stack.pop();
SymVal x990 = SymStack.pop();
Num x991 = Stack.pop();
SymVal x992 = SymStack.pop();
Num x993 = x991.i32_mul(x989);
Stack.push(x993);
bool x994 = allConcrete(x992, x990);
SymVal x995 = x994 ? Concrete(x993, 32) : x992.mul(x990);
SymStack.push(x995);
}
{
Num x996 = Stack.pop();
SymVal x997 = SymStack.pop();
Frames.set(2, x996);
SymFrames.set(2, x997);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x998 = Stack.pop();
SymStack.pop();
Num x999 = I32V(Memory.loadInt(x998.toInt(), 4));
SymVal x1000 = SymMemory.loadSym(x998.toInt(), 4);
Stack.push(x999);
SymStack.push(x1000);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1001 = Stack.pop();
SymVal x1002 = SymStack.pop();
Num x1003 = Stack.pop();
SymVal x1004 = SymStack.pop();
Num x1005 = x1003.i32_add(x1001);
Stack.push(x1005);
bool x1006 = allConcrete(x1004, x1002);
SymVal x1007 = x1006 ? Concrete(x1005, 32) : x1004.add(x1002);
SymStack.push(x1007);
}
{
Num x1008 = Stack.pop();
SymVal x1009 = SymStack.pop();
Num x1010 = Stack.pop();
SymStack.pop();
int x1011 = x1010.toInt();
Memory.storeInt(x1011, 4, x1008.toInt());
SymMemory.storeSym(x1011, 4, x1009);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x1012 = Stack.pop();
SymStack.pop();
Num x1013 = I32V(Memory.loadInt(x1012.toInt(), 0));
SymVal x1014 = SymMemory.loadSym(x1012.toInt(), 0);
Stack.push(x1013);
SymStack.push(x1014);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1015 = Stack.pop();
SymVal x1016 = SymStack.pop();
Num x1017 = Stack.pop();
SymVal x1018 = SymStack.pop();
Num x1019 = x1017.i32_sub(x1015);
Stack.push(x1019);
bool x1020 = allConcrete(x1018, x1016);
SymVal x1021 = x1020 ? Concrete(x1019, 32) : x1018.minus(x1016);
SymStack.push(x1021);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x1022 = Stack.pop();
SymVal x1023 = SymStack.pop();
Num x1024 = Stack.pop();
SymVal x1025 = SymStack.pop();
Num x1026 = x1024.i32_mul(x1022);
Stack.push(x1026);
bool x1027 = allConcrete(x1025, x1023);
SymVal x1028 = x1027 ? Concrete(x1026, 32) : x1025.mul(x1023);
SymStack.push(x1028);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x1029 = Stack.pop();
SymVal x1030 = SymStack.pop();
Num x1031 = Stack.pop();
SymVal x1032 = SymStack.pop();
Num x1033 = x1031.i32_mul(x1029);
Stack.push(x1033);
bool x1034 = allConcrete(x1032, x1030);
SymVal x1035 = x1034 ? Concrete(x1033, 32) : x1032.mul(x1030);
SymStack.push(x1035);
}
{
Num x1036 = Stack.pop();
SymVal x1037 = SymStack.pop();
Num x1038 = Stack.pop();
SymVal x1039 = SymStack.pop();
Num x1040 = x1038.i32_add(x1036);
Stack.push(x1040);
bool x1041 = allConcrete(x1039, x1037);
SymVal x1042 = x1041 ? Concrete(x1040, 32) : x1039.add(x1037);
SymStack.push(x1042);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x1043 = Stack.pop();
SymVal x1044 = SymStack.pop();
Num x1045 = Stack.pop();
SymVal x1046 = SymStack.pop();
Num x1047 = x1045.i32_add(x1043);
Stack.push(x1047);
bool x1048 = allConcrete(x1046, x1044);
SymVal x1049 = x1048 ? Concrete(x1047, 32) : x1046.add(x1044);
SymStack.push(x1049);
}
{
Num x1050 = Stack.pop();
SymStack.pop();
Num x1051 = I32V(Memory.loadInt(x1050.toInt(), 8));
SymVal x1052 = SymMemory.loadSym(x1050.toInt(), 8);
Stack.push(x1051);
SymStack.push(x1052);
}
{
Num x1053 = Stack.pop();
SymStack.pop();
Num x1054 = I32V(Memory.loadInt(x1053.toInt(), 0));
SymVal x1055 = SymMemory.loadSym(x1053.toInt(), 0);
Stack.push(x1054);
SymStack.push(x1055);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x1056 = Stack.pop();
SymVal x1057 = SymStack.pop();
Num x1058 = Stack.pop();
SymVal x1059 = SymStack.pop();
Num x1060 = x1058.i32_eq(x1056);
Stack.push(x1060);
bool x1061 = allConcrete(x1059, x1057);
SymVal x1062 = x1061 ? Concrete(x1060, 32) : x1059.eq(x1057).bool2bv();
SymStack.push(x1062);
}
Num x1063 = Stack.pop();
{
SymVal x1064 = SymStack.pop();
ExploreTree.fillIfElseNode(x1064, 7);
}
int x1065 = x1063.toInt();
if (x1065 != 0) {
ExploreTree.moveCursor(true, makeControl(x965, CURRENT_MCONT));
__attribute__((musttail)) return x971(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x971, CURRENT_MCONT));
__attribute__((musttail)) return x965(std::monostate{});
}
return std::monostate{};
}
std::monostate x971(std::monostate x972) {
info("Entering the true branch 7 of the if");
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x973 = Stack.pop();
SymVal x974 = SymStack.pop();
Num x975 = Stack.pop();
SymStack.pop();
int x976 = x975.toInt();
Memory.storeInt(x976, 0, x973.toInt());
SymMemory.storeSym(x976, 0, x974);
}
__attribute__((musttail)) return x940(std::monostate{});
return std::monostate{};
}
std::monostate x965(std::monostate x966) {
info("Entering the false branch 7 of the if");
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x967 = Stack.pop();
SymVal x968 = SymStack.pop();
Num x969 = Stack.pop();
SymStack.pop();
int x970 = x969.toInt();
Memory.storeInt(x970, 0, x967.toInt());
SymMemory.storeSym(x970, 0, x968);
}
__attribute__((musttail)) return x940(std::monostate{});
return std::monostate{};
}
std::monostate x940(std::monostate x941) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x942 = Stack.pop();
SymStack.pop();
Num x943 = I32V(Memory.loadInt(x942.toInt(), 0));
SymVal x944 = SymMemory.loadSym(x942.toInt(), 0);
Stack.push(x943);
SymStack.push(x944);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x945 = Stack.pop();
SymVal x946 = SymStack.pop();
Num x947 = Stack.pop();
SymVal x948 = SymStack.pop();
Num x949 = x947.i32_div_s(x945);
Stack.push(x949);
bool x950 = allConcrete(x948, x946);
SymVal x951 = x950 ? Concrete(x949, 32) : x948.div(x946);
SymStack.push(x951);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x952 = Stack.pop();
SymVal x953 = SymStack.pop();
Num x954 = Stack.pop();
SymVal x955 = SymStack.pop();
Num x956 = x954.i32_sub(x952);
Stack.push(x956);
bool x957 = allConcrete(x955, x953);
SymVal x958 = x957 ? Concrete(x956, 32) : x955.minus(x953);
SymStack.push(x958);
}
{
Num x959 = Stack.pop();
SymVal x960 = SymStack.pop();
Num x961 = Stack.pop();
SymStack.pop();
int x962 = x961.toInt();
Memory.storeInt(x962, 4, x959.toInt());
SymMemory.storeSym(x962, 4, x960);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x963 = Stack.pop();
SymVal x964 = SymStack.pop();
Frames.set(3, x963);
SymFrames.set(3, x964);
}
__attribute__((musttail)) return x938(std::monostate{});
return std::monostate{};
}
std::monostate x938(std::monostate x939) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x231(std::monostate{});
return std::monostate{};
}
std::monostate x231(std::monostate x910) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x911 = Stack.pop();
SymStack.pop();
Num x912 = I32V(Memory.loadInt(x911.toInt(), 0));
SymVal x913 = SymMemory.loadSym(x911.toInt(), 0);
Stack.push(x912);
SymStack.push(x913);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x914 = Stack.pop();
SymVal x915 = SymStack.pop();
Num x916 = Stack.pop();
SymVal x917 = SymStack.pop();
Num x918 = x916.i32_div_s(x914);
Stack.push(x918);
bool x919 = allConcrete(x917, x915);
SymVal x920 = x919 ? Concrete(x918, 32) : x917.div(x915);
SymStack.push(x920);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x921 = Stack.pop();
SymVal x922 = SymStack.pop();
Num x923 = Stack.pop();
SymVal x924 = SymStack.pop();
Num x925 = x923.i32_sub(x921);
Stack.push(x925);
bool x926 = allConcrete(x924, x922);
SymVal x927 = x926 ? Concrete(x925, 32) : x924.minus(x922);
SymStack.push(x927);
}
{
Num x928 = Stack.pop();
SymVal x929 = SymStack.pop();
Num x930 = Stack.pop();
SymVal x931 = SymStack.pop();
Num x932 = x930.i32_eq(x928);
Stack.push(x932);
bool x933 = allConcrete(x931, x929);
SymVal x934 = x933 ? Concrete(x932, 32) : x931.eq(x929).bool2bv();
SymStack.push(x934);
}
Num x935 = Stack.pop();
{
SymVal x936 = SymStack.pop();
ExploreTree.fillIfElseNode(x936, 8);
}
int x937 = x935.toInt();
if (x937 != 0) {
ExploreTree.moveCursor(true, makeControl(x127, CURRENT_MCONT));
__attribute__((musttail)) return x908(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x908, CURRENT_MCONT));
__attribute__((musttail)) return x127(std::monostate{});
}
return std::monostate{};
}
std::monostate x908(std::monostate x909) {
info("Entering the true branch 8 of the if");
info("Jump to 2");
__attribute__((musttail)) return x850(std::monostate{});
return std::monostate{};
}
std::monostate x850(std::monostate x851) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x852 = Stack.pop();
SymVal x853 = SymStack.pop();
Frames.set(3, x852);
SymFrames.set(3, x853);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x854 = Stack.pop();
SymStack.pop();
Num x855 = I32V(Memory.loadInt(x854.toInt(), 0));
SymVal x856 = SymMemory.loadSym(x854.toInt(), 0);
Stack.push(x855);
SymStack.push(x856);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x857 = Stack.pop();
SymVal x858 = SymStack.pop();
Num x859 = Stack.pop();
SymVal x860 = SymStack.pop();
Num x861 = x859.i32_sub(x857);
Stack.push(x861);
bool x862 = allConcrete(x860, x858);
SymVal x863 = x862 ? Concrete(x861, 32) : x860.minus(x858);
SymStack.push(x863);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x864 = Stack.pop();
SymVal x865 = SymStack.pop();
Num x866 = Stack.pop();
SymVal x867 = SymStack.pop();
Num x868 = x866.i32_mul(x864);
Stack.push(x868);
bool x869 = allConcrete(x867, x865);
SymVal x870 = x869 ? Concrete(x868, 32) : x867.mul(x865);
SymStack.push(x870);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x871 = Stack.pop();
SymVal x872 = SymStack.pop();
Num x873 = Stack.pop();
SymVal x874 = SymStack.pop();
Num x875 = x873.i32_mul(x871);
Stack.push(x875);
bool x876 = allConcrete(x874, x872);
SymVal x877 = x876 ? Concrete(x875, 32) : x874.mul(x872);
SymStack.push(x877);
}
{
Num x878 = Stack.pop();
SymVal x879 = SymStack.pop();
Num x880 = Stack.pop();
SymVal x881 = SymStack.pop();
Num x882 = x880.i32_add(x878);
Stack.push(x882);
bool x883 = allConcrete(x881, x879);
SymVal x884 = x883 ? Concrete(x882, 32) : x881.add(x879);
SymStack.push(x884);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x885 = Stack.pop();
SymVal x886 = SymStack.pop();
Num x887 = Stack.pop();
SymVal x888 = SymStack.pop();
Num x889 = x887.i32_add(x885);
Stack.push(x889);
bool x890 = allConcrete(x888, x886);
SymVal x891 = x890 ? Concrete(x889, 32) : x888.add(x886);
SymStack.push(x891);
}
{
Num x892 = Stack.pop();
SymStack.pop();
Num x893 = I32V(Memory.loadInt(x892.toInt(), 8));
SymVal x894 = SymMemory.loadSym(x892.toInt(), 8);
Stack.push(x893);
SymStack.push(x894);
}
{
Num x895 = Stack.pop();
SymStack.pop();
Num x896 = I32V(Memory.loadInt(x895.toInt(), 0));
SymVal x897 = SymMemory.loadSym(x895.toInt(), 0);
Stack.push(x896);
SymStack.push(x897);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x898 = Stack.pop();
SymVal x899 = SymStack.pop();
Num x900 = Stack.pop();
SymVal x901 = SymStack.pop();
Num x902 = x900.i32_ne(x898);
Stack.push(x902);
bool x903 = allConcrete(x901, x899);
SymVal x904 = x903 ? Concrete(x902, 32) : x901.neq(x899).bool2bv();
SymStack.push(x904);
}
Num x905 = Stack.pop();
{
SymVal x906 = SymStack.pop();
ExploreTree.fillIfElseNode(x906, 9);
}
int x907 = x905.toInt();
if (x907 != 0) {
ExploreTree.moveCursor(true, makeControl(x666, CURRENT_MCONT));
__attribute__((musttail)) return x848(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x848, CURRENT_MCONT));
__attribute__((musttail)) return x666(std::monostate{});
}
return std::monostate{};
}
std::monostate x848(std::monostate x849) {
info("Entering the true branch 9 of the if");
__attribute__((musttail)) return x846(std::monostate{});
return std::monostate{};
}
std::monostate x846(std::monostate x847) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x820(std::monostate{});
return std::monostate{};
}
std::monostate x820(std::monostate x825) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x826 = Stack.pop();
SymStack.pop();
Num x827 = I32V(Memory.loadInt(x826.toInt(), 0));
SymVal x828 = SymMemory.loadSym(x826.toInt(), 0);
Stack.push(x827);
SymStack.push(x828);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x829 = Stack.pop();
SymVal x830 = SymStack.pop();
Num x831 = Stack.pop();
SymVal x832 = SymStack.pop();
Num x833 = x831.i32_div_s(x829);
Stack.push(x833);
bool x834 = allConcrete(x832, x830);
SymVal x835 = x834 ? Concrete(x833, 32) : x832.div(x830);
SymStack.push(x835);
}
{
Num x836 = Stack.pop();
SymVal x837 = SymStack.pop();
Num x838 = Stack.pop();
SymVal x839 = SymStack.pop();
Num x840 = x838.i32_eq(x836);
Stack.push(x840);
bool x841 = allConcrete(x839, x837);
SymVal x842 = x841 ? Concrete(x840, 32) : x839.eq(x837).bool2bv();
SymStack.push(x842);
}
Num x843 = Stack.pop();
{
SymVal x844 = SymStack.pop();
ExploreTree.fillIfElseNode(x844, 12);
}
int x845 = x843.toInt();
if (x845 != 0) {
ExploreTree.moveCursor(true, makeControl(x668, CURRENT_MCONT));
__attribute__((musttail)) return x823(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x823, CURRENT_MCONT));
__attribute__((musttail)) return x668(std::monostate{});
}
return std::monostate{};
}
std::monostate x823(std::monostate x824) {
info("Entering the true branch 12 of the if");
info("Jump to 2");
__attribute__((musttail)) return x821(std::monostate{});
return std::monostate{};
}
std::monostate x821(std::monostate x822) {
info("Exiting the block, stackSize =", Stack.size());
__attribute__((musttail)) return x597(std::monostate{});
return std::monostate{};
}
std::monostate x668(std::monostate x669) {
info("Entering the false branch 12 of the if");
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x670 = Stack.pop();
SymStack.pop();
Num x671 = I32V(Memory.loadInt(x670.toInt(), 0));
SymVal x672 = SymMemory.loadSym(x670.toInt(), 0);
Stack.push(x671);
SymStack.push(x672);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x673 = Stack.pop();
SymVal x674 = SymStack.pop();
Num x675 = Stack.pop();
SymVal x676 = SymStack.pop();
Num x677 = x675.i32_sub(x673);
Stack.push(x677);
bool x678 = allConcrete(x676, x674);
SymVal x679 = x678 ? Concrete(x677, 32) : x676.minus(x674);
SymStack.push(x679);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x680 = Stack.pop();
SymVal x681 = SymStack.pop();
Num x682 = Stack.pop();
SymVal x683 = SymStack.pop();
Num x684 = x682.i32_mul(x680);
Stack.push(x684);
bool x685 = allConcrete(x683, x681);
SymVal x686 = x685 ? Concrete(x684, 32) : x683.mul(x681);
SymStack.push(x686);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x687 = Stack.pop();
SymVal x688 = SymStack.pop();
Num x689 = Stack.pop();
SymVal x690 = SymStack.pop();
Num x691 = x689.i32_mul(x687);
Stack.push(x691);
bool x692 = allConcrete(x690, x688);
SymVal x693 = x692 ? Concrete(x691, 32) : x690.mul(x688);
SymStack.push(x693);
}
{
Num x694 = Stack.pop();
SymVal x695 = SymStack.pop();
Num x696 = Stack.pop();
SymVal x697 = SymStack.pop();
Num x698 = x696.i32_add(x694);
Stack.push(x698);
bool x699 = allConcrete(x697, x695);
SymVal x700 = x699 ? Concrete(x698, 32) : x697.add(x695);
SymStack.push(x700);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x701 = Stack.pop();
SymVal x702 = SymStack.pop();
Num x703 = Stack.pop();
SymVal x704 = SymStack.pop();
Num x705 = x703.i32_add(x701);
Stack.push(x705);
bool x706 = allConcrete(x704, x702);
SymVal x707 = x706 ? Concrete(x705, 32) : x704.add(x702);
SymStack.push(x707);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x708 = Stack.pop();
SymStack.pop();
Num x709 = I32V(Memory.loadInt(x708.toInt(), 0));
SymVal x710 = SymMemory.loadSym(x708.toInt(), 0);
Stack.push(x709);
SymStack.push(x710);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x711 = Stack.pop();
SymVal x712 = SymStack.pop();
Num x713 = Stack.pop();
SymVal x714 = SymStack.pop();
Num x715 = x713.i32_sub(x711);
Stack.push(x715);
bool x716 = allConcrete(x714, x712);
SymVal x717 = x716 ? Concrete(x715, 32) : x714.minus(x712);
SymStack.push(x717);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x718 = Stack.pop();
SymVal x719 = SymStack.pop();
Num x720 = Stack.pop();
SymVal x721 = SymStack.pop();
Num x722 = x720.i32_mul(x718);
Stack.push(x722);
bool x723 = allConcrete(x721, x719);
SymVal x724 = x723 ? Concrete(x722, 32) : x721.mul(x719);
SymStack.push(x724);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x725 = Stack.pop();
SymVal x726 = SymStack.pop();
Num x727 = Stack.pop();
SymVal x728 = SymStack.pop();
Num x729 = x727.i32_mul(x725);
Stack.push(x729);
bool x730 = allConcrete(x728, x726);
SymVal x731 = x730 ? Concrete(x729, 32) : x728.mul(x726);
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
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
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
{
Num x746 = Stack.pop();
SymStack.pop();
Num x747 = I32V(Memory.loadInt(x746.toInt(), 8));
SymVal x748 = SymMemory.loadSym(x746.toInt(), 8);
Stack.push(x747);
SymStack.push(x748);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x749 = Stack.pop();
SymStack.pop();
Num x750 = I32V(Memory.loadInt(x749.toInt(), 0));
SymVal x751 = SymMemory.loadSym(x749.toInt(), 0);
Stack.push(x750);
SymStack.push(x751);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x752 = Stack.pop();
SymVal x753 = SymStack.pop();
Num x754 = Stack.pop();
SymVal x755 = SymStack.pop();
Num x756 = x754.i32_sub(x752);
Stack.push(x756);
bool x757 = allConcrete(x755, x753);
SymVal x758 = x757 ? Concrete(x756, 32) : x755.minus(x753);
SymStack.push(x758);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x759 = Stack.pop();
SymVal x760 = SymStack.pop();
Num x761 = Stack.pop();
SymVal x762 = SymStack.pop();
Num x763 = x761.i32_mul(x759);
Stack.push(x763);
bool x764 = allConcrete(x762, x760);
SymVal x765 = x764 ? Concrete(x763, 32) : x762.mul(x760);
SymStack.push(x765);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x766 = Stack.pop();
SymStack.pop();
Num x767 = I32V(Memory.loadInt(x766.toInt(), 0));
SymVal x768 = SymMemory.loadSym(x766.toInt(), 0);
Stack.push(x767);
SymStack.push(x768);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x769 = Stack.pop();
SymVal x770 = SymStack.pop();
Num x771 = Stack.pop();
SymVal x772 = SymStack.pop();
Num x773 = x771.i32_div_s(x769);
Stack.push(x773);
bool x774 = allConcrete(x772, x770);
SymVal x775 = x774 ? Concrete(x773, 32) : x772.div(x770);
SymStack.push(x775);
}
{
Num x776 = Stack.pop();
SymVal x777 = SymStack.pop();
Num x778 = Stack.pop();
SymVal x779 = SymStack.pop();
Num x780 = x778.i32_add(x776);
Stack.push(x780);
bool x781 = allConcrete(x779, x777);
SymVal x782 = x781 ? Concrete(x780, 32) : x779.add(x777);
SymStack.push(x782);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x783 = Stack.pop();
SymVal x784 = SymStack.pop();
Num x785 = Stack.pop();
SymVal x786 = SymStack.pop();
Num x787 = x785.i32_mul(x783);
Stack.push(x787);
bool x788 = allConcrete(x786, x784);
SymVal x789 = x788 ? Concrete(x787, 32) : x786.mul(x784);
SymStack.push(x789);
}
{
Num x790 = Stack.pop();
SymVal x791 = SymStack.pop();
Num x792 = Stack.pop();
SymVal x793 = SymStack.pop();
Num x794 = x792.i32_add(x790);
Stack.push(x794);
bool x795 = allConcrete(x793, x791);
SymVal x796 = x795 ? Concrete(x794, 32) : x793.add(x791);
SymStack.push(x796);
}
{
Num x797 = Stack.pop();
SymVal x798 = SymStack.pop();
Num x799 = Stack.pop();
SymVal x800 = SymStack.pop();
Num x801 = x799.i32_add(x797);
Stack.push(x801);
bool x802 = allConcrete(x800, x798);
SymVal x803 = x802 ? Concrete(x801, 32) : x800.add(x798);
SymStack.push(x803);
}
{
Num x804 = Stack.pop();
SymStack.pop();
Num x805 = I32V(Memory.loadInt(x804.toInt(), 8));
SymVal x806 = SymMemory.loadSym(x804.toInt(), 8);
Stack.push(x805);
SymStack.push(x806);
}
{
Num x807 = Stack.pop();
SymVal x808 = SymStack.pop();
Num x809 = Stack.pop();
SymStack.pop();
int x810 = x809.toInt();
Memory.storeInt(x810, 8, x807.toInt());
SymMemory.storeSym(x810, 8, x808);
}
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x811 = Stack.pop();
SymVal x812 = SymStack.pop();
Num x813 = Stack.pop();
SymVal x814 = SymStack.pop();
Num x815 = x813.i32_add(x811);
Stack.push(x815);
bool x816 = allConcrete(x814, x812);
SymVal x817 = x816 ? Concrete(x815, 32) : x814.add(x812);
SymStack.push(x817);
}
{
Num x818 = Stack.pop();
SymVal x819 = SymStack.pop();
Frames.set(3, x818);
SymFrames.set(3, x819);
}
info("Jump to 1");
__attribute__((musttail)) return x820(std::monostate{});
return std::monostate{};
}
std::monostate x666(std::monostate x667) {
info("Entering the false branch 9 of the if");
__attribute__((musttail)) return x597(std::monostate{});
return std::monostate{};
}
std::monostate x597(std::monostate x598) {
info("Exiting the if, stackSize =", Stack.size());
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x599 = Stack.pop();
SymStack.pop();
Num x600 = I32V(Memory.loadInt(x599.toInt(), 0));
SymVal x601 = SymMemory.loadSym(x599.toInt(), 0);
Stack.push(x600);
SymStack.push(x601);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x602 = Stack.pop();
SymVal x603 = SymStack.pop();
Num x604 = Stack.pop();
SymVal x605 = SymStack.pop();
Num x606 = x604.i32_sub(x602);
Stack.push(x606);
bool x607 = allConcrete(x605, x603);
SymVal x608 = x607 ? Concrete(x606, 32) : x605.minus(x603);
SymStack.push(x608);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x609 = Stack.pop();
SymVal x610 = SymStack.pop();
Num x611 = Stack.pop();
SymVal x612 = SymStack.pop();
Num x613 = x611.i32_mul(x609);
Stack.push(x613);
bool x614 = allConcrete(x612, x610);
SymVal x615 = x614 ? Concrete(x613, 32) : x612.mul(x610);
SymStack.push(x615);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x616 = Stack.pop();
SymVal x617 = SymStack.pop();
Num x618 = Stack.pop();
SymVal x619 = SymStack.pop();
Num x620 = x618.i32_mul(x616);
Stack.push(x620);
bool x621 = allConcrete(x619, x617);
SymVal x622 = x621 ? Concrete(x620, 32) : x619.mul(x617);
SymStack.push(x622);
}
{
Num x623 = Stack.pop();
SymVal x624 = SymStack.pop();
Num x625 = Stack.pop();
SymVal x626 = SymStack.pop();
Num x627 = x625.i32_add(x623);
Stack.push(x627);
bool x628 = allConcrete(x626, x624);
SymVal x629 = x628 ? Concrete(x627, 32) : x626.add(x624);
SymStack.push(x629);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x630 = Stack.pop();
SymVal x631 = SymStack.pop();
Num x632 = Stack.pop();
SymVal x633 = SymStack.pop();
Num x634 = x632.i32_add(x630);
Stack.push(x634);
bool x635 = allConcrete(x633, x631);
SymVal x636 = x635 ? Concrete(x634, 32) : x633.add(x631);
SymStack.push(x636);
}
{
Num x637 = Stack.pop();
SymStack.pop();
Num x638 = I32V(Memory.loadInt(x637.toInt(), 8));
SymVal x639 = SymMemory.loadSym(x637.toInt(), 8);
Stack.push(x638);
SymStack.push(x639);
}
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x640 = Stack.pop();
SymStack.pop();
Num x641 = I32V(Memory.loadInt(x640.toInt(), 0));
SymVal x642 = SymMemory.loadSym(x640.toInt(), 0);
Stack.push(x641);
SymStack.push(x642);
}
Stack.push(I32V(2));
SymStack.push(Concrete(I32V(2), 32));
{
Num x643 = Stack.pop();
SymVal x644 = SymStack.pop();
Num x645 = Stack.pop();
SymVal x646 = SymStack.pop();
Num x647 = x645.i32_div_s(x643);
Stack.push(x647);
bool x648 = allConcrete(x646, x644);
SymVal x649 = x648 ? Concrete(x647, 32) : x646.div(x644);
SymStack.push(x649);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x650 = Stack.pop();
SymVal x651 = SymStack.pop();
Num x652 = Stack.pop();
SymVal x653 = SymStack.pop();
Num x654 = x652.i32_sub(x650);
Stack.push(x654);
bool x655 = allConcrete(x653, x651);
SymVal x656 = x655 ? Concrete(x654, 32) : x653.minus(x651);
SymStack.push(x656);
}
{
Num x657 = Stack.pop();
SymVal x658 = SymStack.pop();
Num x659 = Stack.pop();
SymStack.pop();
int x660 = x659.toInt();
Memory.storeInt(x660, 4, x657.toInt());
SymMemory.storeSym(x660, 4, x658);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x661 = Stack.pop();
SymStack.pop();
Num x662 = I32V(Memory.loadInt(x661.toInt(), 4));
SymVal x663 = SymMemory.loadSym(x661.toInt(), 4);
Stack.push(x662);
SymStack.push(x663);
}
{
Num x664 = Stack.pop();
SymVal x665 = SymStack.pop();
Frames.set(3, x664);
SymFrames.set(3, x665);
}
__attribute__((musttail)) return x595(std::monostate{});
return std::monostate{};
}
std::monostate x595(std::monostate x596) {
info("Entering the block, stackSize =", Stack.size());
__attribute__((musttail)) return x333(std::monostate{});
return std::monostate{};
}
std::monostate x333(std::monostate x584) {
info("Entered the loop, stackSize =", Stack.size());
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(Frames.get(3));
SymStack.push(SymFrames.get(3));
{
Num x585 = Stack.pop();
SymVal x586 = SymStack.pop();
Num x587 = Stack.pop();
SymVal x588 = SymStack.pop();
Num x589 = x587.i32_eq(x585);
Stack.push(x589);
bool x590 = allConcrete(x588, x586);
SymVal x591 = x590 ? Concrete(x589, 32) : x588.eq(x586).bool2bv();
SymStack.push(x591);
}
Num x592 = Stack.pop();
{
SymVal x593 = SymStack.pop();
ExploreTree.fillIfElseNode(x593, 10);
}
int x594 = x592.toInt();
if (x594 != 0) {
ExploreTree.moveCursor(true, makeControl(x232, CURRENT_MCONT));
__attribute__((musttail)) return x582(std::monostate{});
} else {
ExploreTree.moveCursor(false, makeControl(x582, CURRENT_MCONT));
__attribute__((musttail)) return x232(std::monostate{});
}
return std::monostate{};
}
std::monostate x582(std::monostate x583) {
info("Entering the true branch 10 of the if");
info("Jump to 2");
__attribute__((musttail)) return x519(std::monostate{});
return std::monostate{};
}
std::monostate x519(std::monostate x520) {
info("Exiting the block, stackSize =", Stack.size());
Stack.push(I32V(0));
SymStack.push(Concrete(I32V(0), 32));
{
Num x521 = Stack.pop();
SymStack.pop();
Num x522 = I32V(Memory.loadInt(x521.toInt(), 0));
SymVal x523 = SymMemory.loadSym(x521.toInt(), 0);
Stack.push(x522);
SymStack.push(x523);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x524 = Stack.pop();
SymVal x525 = SymStack.pop();
Num x526 = Stack.pop();
SymVal x527 = SymStack.pop();
Num x528 = x526.i32_sub(x524);
Stack.push(x528);
bool x529 = allConcrete(x527, x525);
SymVal x530 = x529 ? Concrete(x528, 32) : x527.minus(x525);
SymStack.push(x530);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x531 = Stack.pop();
SymVal x532 = SymStack.pop();
Num x533 = Stack.pop();
SymVal x534 = SymStack.pop();
Num x535 = x533.i32_mul(x531);
Stack.push(x535);
bool x536 = allConcrete(x534, x532);
SymVal x537 = x536 ? Concrete(x535, 32) : x534.mul(x532);
SymStack.push(x537);
}
Stack.push(Frames.get(1));
SymStack.push(SymFrames.get(1));
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x538 = Stack.pop();
SymVal x539 = SymStack.pop();
Num x540 = Stack.pop();
SymVal x541 = SymStack.pop();
Num x542 = x540.i32_add(x538);
Stack.push(x542);
bool x543 = allConcrete(x541, x539);
SymVal x544 = x543 ? Concrete(x542, 32) : x541.add(x539);
SymStack.push(x544);
}
Stack.push(I32V(4));
SymStack.push(Concrete(I32V(4), 32));
{
Num x545 = Stack.pop();
SymVal x546 = SymStack.pop();
Num x547 = Stack.pop();
SymVal x548 = SymStack.pop();
Num x549 = x547.i32_mul(x545);
Stack.push(x549);
bool x550 = allConcrete(x548, x546);
SymVal x551 = x550 ? Concrete(x549, 32) : x548.mul(x546);
SymStack.push(x551);
}
{
Num x552 = Stack.pop();
SymVal x553 = SymStack.pop();
Num x554 = Stack.pop();
SymVal x555 = SymStack.pop();
Num x556 = x554.i32_add(x552);
Stack.push(x556);
bool x557 = allConcrete(x555, x553);
SymVal x558 = x557 ? Concrete(x556, 32) : x555.add(x553);
SymStack.push(x558);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x559 = Stack.pop();
SymVal x560 = SymStack.pop();
Num x561 = Stack.pop();
SymVal x562 = SymStack.pop();
Num x563 = x561.i32_add(x559);
Stack.push(x563);
bool x564 = allConcrete(x562, x560);
SymVal x565 = x564 ? Concrete(x563, 32) : x562.add(x560);
SymStack.push(x565);
}
Stack.push(Frames.get(2));
SymStack.push(SymFrames.get(2));
{
Num x566 = Stack.pop();
SymVal x567 = SymStack.pop();
Num x568 = Stack.pop();
SymStack.pop();
int x569 = x568.toInt();
Memory.storeInt(x569, 8, x566.toInt());
SymMemory.storeSym(x569, 8, x567);
}
Stack.push(Frames.get(0));
SymStack.push(SymFrames.get(0));
{
Num x570 = Stack.pop();
SymStack.pop();
Num x571 = I32V(Memory.loadInt(x570.toInt(), 4));
SymVal x572 = SymMemory.loadSym(x570.toInt(), 4);
Stack.push(x571);
SymStack.push(x572);
}
Stack.push(I32V(1));
SymStack.push(Concrete(I32V(1), 32));
{
Num x573 = Stack.pop();
SymVal x574 = SymStack.pop();
Num x575 = Stack.pop();
SymVal x576 = SymStack.pop();
Num x577 = x575.i32_sub(x573);
Stack.push(x577);
bool x578 = allConcrete(x576, x574);
SymVal x579 = x578 ? Concrete(x577, 32) : x576.minus(x574);
SymStack.push(x579);
}
{
Num x580 = Stack.pop();
SymVal x581 = SymStack.pop();
Frames.set(3, x580);
SymFrames.set(3, x581);
}
__attribute__((musttail)) return x517(std::monostate{});
return std::monostate{};
}
std::monostate x517(std::monostate x518) {
info("Entering the block, stackSize =", Stack.size());
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
updateCurrentMCont(prependCont(x7858, CURRENT_MCONT));
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
