package de.homebeaver.lei;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * count distinct languages
 * 
 * ar-AE, ar-EG, ar-SA, ar is mapped to ar
 * bg-BG, BG, bg is mapped to bg
 * GB, en-GB, en-US, ... is mapped to en
 * xxx is mapped to "invalid"
 * 
""=3856530
, ab=13
, af-ZA=36
, af=1
, an=1
, ar-AE=78
, ar-EG=9
, ar-SA=18
, ar=175
, az=2
, ba=41
, be=23
, bg-BG=63
, BG=2
, bg=561
, bn=10
, bs=3
, ca=125
, co=1
, cs-CZ=12
, CS=22
, cs=52399
, CY=19
, cy=37
, da-DK=619
, DA=2
, da=86033
, de-AT=2504
, de-CH=802
, de-DE=37250
, de-LI=5299
, de-LU=167
, DE=23884
, de=45220
, dv=24
, el-GR=36
, EL=1
, el=324
, en-AU=2882
, en-BZ=63
, en-CA=287
, en-CB=442
, en-GB=1750
, en-IE=1115
, en-NZ=44
, en-US=2689
, en-ZA=60
, EN=470
, en=483706
, es-AR=6
, es-CL=6
, es-CO=3
, es-CR=3
, es-ES=1545
, es-MX=6
, es-PA=705
, es-PE=6
, ES=22
, es=370306
, et-EE=306
, ET=52
, et=9714
, eu=1
, fi-FI=8
, fi=2863
, FI=3
, fo=104
, fr-BE=44714
, fr-CH=86
, fr-FR=173
, fr-LU=197
, fr-MC=6
, FR=194
, fr=377975
, ga=9
, GB=15
, gl=1
, gu-IN=6
, gu=5
, he-IL=9
, he=19}
, hi-IN=46
, hi=10
, hr=5243
, hu-HU=1010
, HU=10
, hu=3101
, hy=9
, id-ID=9
, id=13
, ii=1
, in=1
, is-IS=77
, is=295
, it-CH=15
, it-IT=384625
, IT=10
, it=20425
, ja=34221
, ka=2
, kk=16
, kn=1
, ko=1211
, ky=8
, la=1
, lb=39
, LB=5
, lt-LT=135
, lt=1365
, LT=28
, lu=169
, lv-LV=144
, LV=12
, lv=729
, me=44
, mk-MK=12
, mk=30
, mn=5
, ms=75
, mt-MT=74
, MT=6
, mt=73
, nb-NO=28
, nb=51
, nl-BE=82
, nl-NL=111
, nl=18871
, NL=30
, nn-NO=78
, nn=5244
, NN=8
, no=13903
, pl-PL=66
, pl=1297
, PL=26845
, pt-BR=27
, pt-PT=1819
, PT=23
, pt=8401
, ro-RO=35
, RO=131
, ro=1953
, rs=131
, ru=5207
, se=2
, si=12
, sk-SK=18
, sk=10360
, SK=12
, sl-SI=3
, sl=1210
, so=1
, sq=2
, sr=24
, sv-SE=383
, sv=30044
, SV=9
, ta-IN=2
, ta=2
, th=364
, tl=1
, tr-TR=6
, tr=11477
, ts=1
, uk=11
, uz=11
, ve=1
, vi-VN=3
, vi=2
, zh-hans=1
, zh-HK=38
, zh-MO=2
, zh-SG=12
, zh-TW=3
, zh=134092
, zu=1

 */
public class DistinctLangCounter extends DistinctStringCounter {
	
	private static final String regex = "^([a-z]{2})(-.*)?$";

	Pattern p;
	public DistinctLangCounter() {
		super();
		p = Pattern.compile(regex);
	}
	
	public void count(String s) {
		super.count(mapTo(s));
	}
	
	private String mapTo(String s) {
		if(s==null) return s;
		
		Matcher m = p.matcher(s.equals("GB") ? "en" : s.toLowerCase());
		if(m.matches()) {
			return m.group(1);
		}
		return "invalid";
	}
}
