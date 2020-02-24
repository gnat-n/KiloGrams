/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lps.acs.ml.ngram3.utils;

import edu.lps.acs.ml.ngram3.ByteHasher;

/**
 * This function implements the Cyclic or "Buzhash" rolling hash function. It does not implement the 
 * @author Edward Raff
 */
public class RabinKarpHash implements ByteHasher
{
    static final int B = 31;
    int n;
    int B2N = 1;
    int curHashValue = 0;
    int curLen = 0;
    byte[] circular_buffer;
    int buffer_pos;
    /**
     * 
     * @param n the length of the rolling hash window
     */
    public RabinKarpHash(int n)
    {
        this.n = n;
        for(int i = 0; i < n; i++)
            B2N *= B;
        buffer_pos = 0;
        circular_buffer = new byte[n];
    }
    
    
        @Override
    public int hash(byte[] bytes)
    {
        RabinKarpHash h = new RabinKarpHash(n);
        int hash = 0;
        for(int i = 0; i < bytes.length; i++)
            hash = h.pushByteNoWindow(bytes[i]);
        return hash;
    }
    
    public void reset()
    {
        curHashValue = 0;
        curLen = 0;
        buffer_pos = 0;
    }
    
    public int pushByteNoWindow(byte b)
    {
        curHashValue = B*curHashValue + byteHash[Byte.toUnsignedInt(b)];
        return curHashValue;
    }

    @Override
    public int pushByteHash(byte b)
    {
        if(curLen < n)
        {
            curHashValue = B*curHashValue + byteHash[Byte.toUnsignedInt(b)];
            circular_buffer[curLen] = b;
            curLen++;
        }
        else//, normal case
        {
            ///swap out buffer values 
            int out = Byte.toUnsignedInt(circular_buffer[buffer_pos]);
            circular_buffer[buffer_pos] = b;
            buffer_pos = (buffer_pos + 1) % n;

            curHashValue = B*curHashValue + byteHash[Byte.toUnsignedInt(b)] - B2N*byteHash[out];
        }
        
        return curHashValue;
    }
    
    
    /**
     * This array maps each of the 256 possible byte values to a random integer
     * generated by a cryptografically secure PRNG. This table acts as the hash 
     * function for bytes by just indexing into the array by the byte's unsigned
     * value. 
     */
    static final int[] byteHash = new int[]
    {
        1272760677, -1605449132, -1919906120, -64512506, -883539833, -1866311420,
        -143484393, 1497250527, -508935371, 1893585698, -1092676707, -1064958821,
        -521162257, 73648583, 640430508, -497639961, 1170971304, -1165460575,
        -954564627, -1008945542, 1286167175, 105194404, -354930925, 1663672225,
        1807231375, 1398459395, 975361290, 494785069, -1737895086, 454693552, 
        1712750555, -773459478, 1923977624, 132790269, 398966254, 848844711, 
        -1600702618, 1277462827, 1088121088, -1389254563, 2035796011, -2126527029,
        -1300952775, 357762284, 1299685594, -1087275384, -113620692, 903827772, 
        765609055, -1170922495, -2015735258, 879445641, 1487210899, -736582792, 
        -775900920, 1831698765, -626335196, 1804759908, -1245182278, -1461759542,
        941351068, 861767095, 1068728251, -859914383, 151900745, 359277906, 
        -1881933549, -375681686, 1277314382, 928386920, -1369634267, -964730146,
        1909193234, 2089432911, -969600877, -192712630, 1325189560, 570282214, 
        34740082, 524832607, -1097565390, -91184115, 1781102099, 968812776, 
        105966638, 1774195594, 1438381638, -87838240, 973803914, -1143498814,
        336714781, 655830831, 999805711, 1558455358, -1958324172, 2014646285,
        -835014134, -1609585244, -1549304805, 1406483310, -2078357397, 
        -1853862818, 1326257120, 1836636965, 574142956, -80188820, 649864636, 
        681222344, 931708297, 823494035, -1874102270, -990646657, 241326852, 
        -1039625341, 490285448, -536278682, -564883465, -990386644, -1262740097,
        -84650042, 746440113, -935811651, -455225483, -891671874, 827656323, 
        -1748333436, 1095532827, -513596700, -1196902021, 812766986, 905700544,
        990617511, -960172309, 288698579, 794532622, 1020198770, 1256207318, 
        -2063745708, -216007427, -550535648, -1179623191, 1927221948, 
        -1526908953, 602984909, -688739812, 277557865, 59569584, -1632364485, 
        47373463, 1234122249, -337284567, -1357126597, 715464142, 783747872, 
        1793440287, -837191322, -838894316, -7985460, 1443558914, 1599589937, 
        -1568780808, -1672230366, 455191147, -1895307158, -605955253, 
        -1834258088, 913895723, 919409756, -710441082, 1347417809, -677049545, 
        1606203193, -1743529898, 1503297479, -709268545, -603572354, 952524406,
        1519571217, 70052427, -798604510, 1266416427, -1188303669, 458027125, 
        1931253256, 1043302794, 111387650, 1057982593, 505332897, 477502608, 
        -833551944, -1838580838, 1443820673, 2107907296, -679984073, 648588599,
        868601998, -910080808, 756309930, 436504866, 1045570721, 1465115239, 
        128593973, 514230976, 698517062, 848545208, 1680745080, -1234467452, 
        -1949534689, -1451825994, 1439996942, 1012849505, 1432903404, 123883003,
        597625450, -428409621, -1354611127, -1473944474, -1922007500, 964810625,
        -2069033192, -1931625933, 381255451, -596085362, -989660767, -22352011, 
        244843466, 969907659, 318476239, -1208235292, -1793704765, 1679281718, 
        1356983489, -257850439, -1888102333, 130467496, 296574366, 442372351, 
        1352730757, 1795266566, 1281420843, -434223370, 720974850, -1692353757, 
        -1219933881, -1396326383, -2078082610, 1520500507, -2049076405, 
        -901075300, 1120919941, -1751404060, -2068056172, -1696622473, 
        1683564346, -1239536189, -1124799282
    };


    
}
