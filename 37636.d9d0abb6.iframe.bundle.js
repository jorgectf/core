"use strict";(self.webpackChunkdotcms_ui=self.webpackChunkdotcms_ui||[]).push([[37636],{"./node_modules/date-fns/locale/ta/_lib/match/index.js":(module,exports,__webpack_require__)=>{var _interopRequireDefault=__webpack_require__("./node_modules/date-fns/node_modules/@babel/runtime/helpers/interopRequireDefault.js").default;Object.defineProperty(exports,"__esModule",{value:!0}),exports.default=void 0;var _index=_interopRequireDefault(__webpack_require__("./node_modules/date-fns/locale/_lib/buildMatchFn/index.js")),_default={ordinalNumber:(0,_interopRequireDefault(__webpack_require__("./node_modules/date-fns/locale/_lib/buildMatchPatternFn/index.js")).default)({matchPattern:/^(\d+)(வது)?/i,parsePattern:/\d+/i,valueCallback:function valueCallback(value){return parseInt(value,10)}}),era:(0,_index.default)({matchPatterns:{narrow:/^(கி.மு.|கி.பி.)/i,abbreviated:/^(கி\.?\s?மு\.?|கி\.?\s?பி\.?)/,wide:/^(கிறிஸ்துவுக்கு\sமுன்|அன்னோ\sடோமினி)/i},defaultMatchWidth:"wide",parsePatterns:{any:[/கி\.?\s?மு\.?/,/கி\.?\s?பி\.?/]},defaultParseWidth:"any"}),quarter:(0,_index.default)({matchPatterns:{narrow:/^[1234]/i,abbreviated:/^காலா.[1234]/i,wide:/^(ஒன்றாம்|இரண்டாம்|மூன்றாம்|நான்காம்) காலாண்டு/i},defaultMatchWidth:"wide",parsePatterns:{narrow:[/1/i,/2/i,/3/i,/4/i],any:[/(1|காலா.1|ஒன்றாம்)/i,/(2|காலா.2|இரண்டாம்)/i,/(3|காலா.3|மூன்றாம்)/i,/(4|காலா.4|நான்காம்)/i]},defaultParseWidth:"any",valueCallback:function valueCallback(index){return index+1}}),month:(0,_index.default)({matchPatterns:{narrow:/^(ஜ|பி|மா|ஏ|மே|ஜூ|ஆ|செ|அ|ந|டி)$/i,abbreviated:/^(ஜன.|பிப்.|மார்.|ஏப்.|மே|ஜூன்|ஜூலை|ஆக.|செப்.|அக்.|நவ.|டிச.)/i,wide:/^(ஜனவரி|பிப்ரவரி|மார்ச்|ஏப்ரல்|மே|ஜூன்|ஜூலை|ஆகஸ்ட்|செப்டம்பர்|அக்டோபர்|நவம்பர்|டிசம்பர்)/i},defaultMatchWidth:"wide",parsePatterns:{narrow:[/^ஜ$/i,/^பி/i,/^மா/i,/^ஏ/i,/^மே/i,/^ஜூ/i,/^ஜூ/i,/^ஆ/i,/^செ/i,/^அ/i,/^ந/i,/^டி/i],any:[/^ஜன/i,/^பி/i,/^மா/i,/^ஏ/i,/^மே/i,/^ஜூன்/i,/^ஜூலை/i,/^ஆ/i,/^செ/i,/^அ/i,/^ந/i,/^டி/i]},defaultParseWidth:"any"}),day:(0,_index.default)({matchPatterns:{narrow:/^(ஞா|தி|செ|பு|வி|வெ|ச)/i,short:/^(ஞா|தி|செ|பு|வி|வெ|ச)/i,abbreviated:/^(ஞாயி.|திங்.|செவ்.|புத.|வியா.|வெள்.|சனி)/i,wide:/^(ஞாயிறு|திங்கள்|செவ்வாய்|புதன்|வியாழன்|வெள்ளி|சனி)/i},defaultMatchWidth:"wide",parsePatterns:{narrow:[/^ஞா/i,/^தி/i,/^செ/i,/^பு/i,/^வி/i,/^வெ/i,/^ச/i],any:[/^ஞா/i,/^தி/i,/^செ/i,/^பு/i,/^வி/i,/^வெ/i,/^ச/i]},defaultParseWidth:"any"}),dayPeriod:(0,_index.default)({matchPatterns:{narrow:/^(மு.ப|பி.ப|நள்|நண்|காலை|மதியம்|மாலை|இரவு)/i,any:/^(மு.ப|பி.ப|முற்பகல்|பிற்பகல்|நள்ளிரவு|நண்பகல்|காலை|மதியம்|மாலை|இரவு)/i},defaultMatchWidth:"any",parsePatterns:{any:{am:/^மு/i,pm:/^பி/i,midnight:/^நள்/i,noon:/^நண்/i,morning:/காலை/i,afternoon:/மதியம்/i,evening:/மாலை/i,night:/இரவு/i}},defaultParseWidth:"any"})};exports.default=_default,module.exports=exports.default}}]);
//# sourceMappingURL=37636.d9d0abb6.iframe.bundle.js.map