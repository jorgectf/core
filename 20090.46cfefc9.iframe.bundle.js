"use strict";(self.webpackChunkdotcms_ui=self.webpackChunkdotcms_ui||[]).push([[20090],{"./node_modules/date-fns/locale/az/_lib/localize/index.js":(module,exports,__webpack_require__)=>{var _interopRequireDefault=__webpack_require__("./node_modules/date-fns/node_modules/@babel/runtime/helpers/interopRequireDefault.js").default;Object.defineProperty(exports,"__esModule",{value:!0}),exports.default=void 0;var _index=_interopRequireDefault(__webpack_require__("./node_modules/date-fns/locale/_lib/buildLocalizeFn/index.js")),suffixes={1:"-inci",5:"-inci",8:"-inci",70:"-inci",80:"-inci",2:"-nci",7:"-nci",20:"-nci",50:"-nci",3:"-üncü",4:"-üncü",100:"-üncü",6:"-ncı",9:"-uncu",10:"-uncu",30:"-uncu",60:"-ıncı",90:"-ıncı"},_default={ordinalNumber:function ordinalNumber(dirtyNumber,_options){var number=Number(dirtyNumber),suffix=function getSuffix(number){if(0===number)return number+"-ıncı";var a=number%10,b=number%100-a,c=number>=100?100:null;return suffixes[a]?suffixes[a]:suffixes[b]?suffixes[b]:null!==c?suffixes[c]:""}(number);return number+suffix},era:(0,_index.default)({values:{narrow:["e.ə","b.e"],abbreviated:["e.ə","b.e"],wide:["eramızdan əvvəl","bizim era"]},defaultWidth:"wide"}),quarter:(0,_index.default)({values:{narrow:["1","2","3","4"],abbreviated:["K1","K2","K3","K4"],wide:["1ci kvartal","2ci kvartal","3cü kvartal","4cü kvartal"]},defaultWidth:"wide",argumentCallback:function argumentCallback(quarter){return quarter-1}}),month:(0,_index.default)({values:{narrow:["Y","F","M","A","M","İ","İ","A","S","O","N","D"],abbreviated:["Yan","Fev","Mar","Apr","May","İyun","İyul","Avq","Sen","Okt","Noy","Dek"],wide:["Yanvar","Fevral","Mart","Aprel","May","İyun","İyul","Avqust","Sentyabr","Oktyabr","Noyabr","Dekabr"]},defaultWidth:"wide"}),day:(0,_index.default)({values:{narrow:["B.","B.e","Ç.a","Ç.","C.a","C.","Ş."],short:["B.","B.e","Ç.a","Ç.","C.a","C.","Ş."],abbreviated:["Baz","Baz.e","Çər.a","Çər","Cüm.a","Cüm","Şə"],wide:["Bazar","Bazar ertəsi","Çərşənbə axşamı","Çərşənbə","Cümə axşamı","Cümə","Şənbə"]},defaultWidth:"wide"}),dayPeriod:(0,_index.default)({values:{narrow:{am:"am",pm:"pm",midnight:"gecəyarı",noon:"gün",morning:"səhər",afternoon:"gündüz",evening:"axşam",night:"gecə"},abbreviated:{am:"AM",pm:"PM",midnight:"gecəyarı",noon:"gün",morning:"səhər",afternoon:"gündüz",evening:"axşam",night:"gecə"},wide:{am:"a.m.",pm:"p.m.",midnight:"gecəyarı",noon:"gün",morning:"səhər",afternoon:"gündüz",evening:"axşam",night:"gecə"}},defaultWidth:"wide",formattingValues:{narrow:{am:"a",pm:"p",midnight:"gecəyarı",noon:"gün",morning:"səhər",afternoon:"gündüz",evening:"axşam",night:"gecə"},abbreviated:{am:"AM",pm:"PM",midnight:"gecəyarı",noon:"gün",morning:"səhər",afternoon:"gündüz",evening:"axşam",night:"gecə"},wide:{am:"a.m.",pm:"p.m.",midnight:"gecəyarı",noon:"gün",morning:"səhər",afternoon:"gündüz",evening:"axşam",night:"gecə"}},defaultFormattingWidth:"wide"})};exports.default=_default,module.exports=exports.default}}]);
//# sourceMappingURL=20090.46cfefc9.iframe.bundle.js.map