import*as strings from"../../../base/common/strings.js";class PendingChanges{constructor(){this._hasPending=!1,this._inserts=[],this._changes=[],this._removes=[]}insert(x){this._hasPending=!0,this._inserts.push(x)}change(x){this._hasPending=!0,this._changes.push(x)}remove(x){this._hasPending=!0,this._removes.push(x)}mustCommit(){return this._hasPending}commit(linesLayout){if(!this._hasPending)return;const inserts=this._inserts,changes=this._changes,removes=this._removes;this._hasPending=!1,this._inserts=[],this._changes=[],this._removes=[],linesLayout._commitPendingChanges(inserts,changes,removes)}}export class EditorWhitespace{constructor(id,afterLineNumber,ordinal,height,minWidth){this.id=id,this.afterLineNumber=afterLineNumber,this.ordinal=ordinal,this.height=height,this.minWidth=minWidth,this.prefixSum=0}}export class LinesLayout{constructor(lineCount,lineHeight,paddingTop,paddingBottom){this._instanceId=strings.singleLetterHash(++LinesLayout.INSTANCE_COUNT),this._pendingChanges=new PendingChanges,this._lastWhitespaceId=0,this._arr=[],this._prefixSumValidIndex=-1,this._minWidth=-1,this._lineCount=lineCount,this._lineHeight=lineHeight,this._paddingTop=paddingTop,this._paddingBottom=paddingBottom}static findInsertionIndex(arr,afterLineNumber,ordinal){let low=0,high=arr.length;for(;low<high;){const mid=low+high>>>1;afterLineNumber===arr[mid].afterLineNumber?ordinal<arr[mid].ordinal?high=mid:low=mid+1:afterLineNumber<arr[mid].afterLineNumber?high=mid:low=mid+1}return low}setLineHeight(lineHeight){this._checkPendingChanges(),this._lineHeight=lineHeight}setPadding(paddingTop,paddingBottom){this._paddingTop=paddingTop,this._paddingBottom=paddingBottom}onFlushed(lineCount){this._checkPendingChanges(),this._lineCount=lineCount}changeWhitespace(callback){let hadAChange=!1;try{callback({insertWhitespace:(afterLineNumber,ordinal,heightInPx,minWidth)=>{hadAChange=!0,afterLineNumber|=0,ordinal|=0,heightInPx|=0,minWidth|=0;const id=this._instanceId+ ++this._lastWhitespaceId;return this._pendingChanges.insert(new EditorWhitespace(id,afterLineNumber,ordinal,heightInPx,minWidth)),id},changeOneWhitespace:(id,newAfterLineNumber,newHeight)=>{hadAChange=!0,newAfterLineNumber|=0,newHeight|=0,this._pendingChanges.change({id,newAfterLineNumber,newHeight})},removeWhitespace:id=>{hadAChange=!0,this._pendingChanges.remove({id})}})}finally{this._pendingChanges.commit(this)}return hadAChange}_commitPendingChanges(inserts,changes,removes){if((inserts.length>0||removes.length>0)&&(this._minWidth=-1),inserts.length+changes.length+removes.length<=1){for(const insert of inserts)this._insertWhitespace(insert);for(const change of changes)this._changeOneWhitespace(change.id,change.newAfterLineNumber,change.newHeight);for(const remove of removes){const index=this._findWhitespaceIndex(remove.id);-1!==index&&this._removeWhitespace(index)}return}const toRemove=new Set;for(const remove of removes)toRemove.add(remove.id);const toChange=new Map;for(const change of changes)toChange.set(change.id,change);const applyRemoveAndChange=whitespaces=>{const result=[];for(const whitespace of whitespaces)if(!toRemove.has(whitespace.id)){if(toChange.has(whitespace.id)){const change=toChange.get(whitespace.id);whitespace.afterLineNumber=change.newAfterLineNumber,whitespace.height=change.newHeight}result.push(whitespace)}return result},result=applyRemoveAndChange(this._arr).concat(applyRemoveAndChange(inserts));result.sort(((a,b)=>a.afterLineNumber===b.afterLineNumber?a.ordinal-b.ordinal:a.afterLineNumber-b.afterLineNumber)),this._arr=result,this._prefixSumValidIndex=-1}_checkPendingChanges(){this._pendingChanges.mustCommit()&&this._pendingChanges.commit(this)}_insertWhitespace(whitespace){const insertIndex=LinesLayout.findInsertionIndex(this._arr,whitespace.afterLineNumber,whitespace.ordinal);this._arr.splice(insertIndex,0,whitespace),this._prefixSumValidIndex=Math.min(this._prefixSumValidIndex,insertIndex-1)}_findWhitespaceIndex(id){const arr=this._arr;for(let i=0,len=arr.length;i<len;i++)if(arr[i].id===id)return i;return-1}_changeOneWhitespace(id,newAfterLineNumber,newHeight){const index=this._findWhitespaceIndex(id);if(-1!==index&&(this._arr[index].height!==newHeight&&(this._arr[index].height=newHeight,this._prefixSumValidIndex=Math.min(this._prefixSumValidIndex,index-1)),this._arr[index].afterLineNumber!==newAfterLineNumber)){const whitespace=this._arr[index];this._removeWhitespace(index),whitespace.afterLineNumber=newAfterLineNumber,this._insertWhitespace(whitespace)}}_removeWhitespace(removeIndex){this._arr.splice(removeIndex,1),this._prefixSumValidIndex=Math.min(this._prefixSumValidIndex,removeIndex-1)}onLinesDeleted(fromLineNumber,toLineNumber){this._checkPendingChanges(),fromLineNumber|=0,toLineNumber|=0,this._lineCount-=toLineNumber-fromLineNumber+1;for(let i=0,len=this._arr.length;i<len;i++){const afterLineNumber=this._arr[i].afterLineNumber;fromLineNumber<=afterLineNumber&&afterLineNumber<=toLineNumber?this._arr[i].afterLineNumber=fromLineNumber-1:afterLineNumber>toLineNumber&&(this._arr[i].afterLineNumber-=toLineNumber-fromLineNumber+1)}}onLinesInserted(fromLineNumber,toLineNumber){this._checkPendingChanges(),fromLineNumber|=0,toLineNumber|=0,this._lineCount+=toLineNumber-fromLineNumber+1;for(let i=0,len=this._arr.length;i<len;i++){fromLineNumber<=this._arr[i].afterLineNumber&&(this._arr[i].afterLineNumber+=toLineNumber-fromLineNumber+1)}}getWhitespacesTotalHeight(){return this._checkPendingChanges(),0===this._arr.length?0:this.getWhitespacesAccumulatedHeight(this._arr.length-1)}getWhitespacesAccumulatedHeight(index){this._checkPendingChanges(),index|=0;let startIndex=Math.max(0,this._prefixSumValidIndex+1);0===startIndex&&(this._arr[0].prefixSum=this._arr[0].height,startIndex++);for(let i=startIndex;i<=index;i++)this._arr[i].prefixSum=this._arr[i-1].prefixSum+this._arr[i].height;return this._prefixSumValidIndex=Math.max(this._prefixSumValidIndex,index),this._arr[index].prefixSum}getLinesTotalHeight(){this._checkPendingChanges();return this._lineHeight*this._lineCount+this.getWhitespacesTotalHeight()+this._paddingTop+this._paddingBottom}getWhitespaceAccumulatedHeightBeforeLineNumber(lineNumber){this._checkPendingChanges(),lineNumber|=0;const lastWhitespaceBeforeLineNumber=this._findLastWhitespaceBeforeLineNumber(lineNumber);return-1===lastWhitespaceBeforeLineNumber?0:this.getWhitespacesAccumulatedHeight(lastWhitespaceBeforeLineNumber)}_findLastWhitespaceBeforeLineNumber(lineNumber){lineNumber|=0;const arr=this._arr;let low=0,high=arr.length-1;for(;low<=high;){const mid=low+((high-low|0)/2|0)|0;if(arr[mid].afterLineNumber<lineNumber){if(mid+1>=arr.length||arr[mid+1].afterLineNumber>=lineNumber)return mid;low=mid+1|0}else high=mid-1|0}return-1}_findFirstWhitespaceAfterLineNumber(lineNumber){lineNumber|=0;const firstWhitespaceAfterLineNumber=this._findLastWhitespaceBeforeLineNumber(lineNumber)+1;return firstWhitespaceAfterLineNumber<this._arr.length?firstWhitespaceAfterLineNumber:-1}getFirstWhitespaceIndexAfterLineNumber(lineNumber){return this._checkPendingChanges(),lineNumber|=0,this._findFirstWhitespaceAfterLineNumber(lineNumber)}getVerticalOffsetForLineNumber(lineNumber){let previousLinesHeight;this._checkPendingChanges(),previousLinesHeight=(lineNumber|=0)>1?this._lineHeight*(lineNumber-1):0;return previousLinesHeight+this.getWhitespaceAccumulatedHeightBeforeLineNumber(lineNumber)+this._paddingTop}getWhitespaceMinWidth(){if(this._checkPendingChanges(),-1===this._minWidth){let minWidth=0;for(let i=0,len=this._arr.length;i<len;i++)minWidth=Math.max(minWidth,this._arr[i].minWidth);this._minWidth=minWidth}return this._minWidth}isAfterLines(verticalOffset){this._checkPendingChanges();return verticalOffset>this.getLinesTotalHeight()}isInTopPadding(verticalOffset){return 0!==this._paddingTop&&(this._checkPendingChanges(),verticalOffset<this._paddingTop)}isInBottomPadding(verticalOffset){if(0===this._paddingBottom)return!1;this._checkPendingChanges();return verticalOffset>=this.getLinesTotalHeight()-this._paddingBottom}getLineNumberAtOrAfterVerticalOffset(verticalOffset){if(this._checkPendingChanges(),(verticalOffset|=0)<0)return 1;const linesCount=0|this._lineCount,lineHeight=this._lineHeight;let minLineNumber=1,maxLineNumber=linesCount;for(;minLineNumber<maxLineNumber;){const midLineNumber=(minLineNumber+maxLineNumber)/2|0,midLineNumberVerticalOffset=0|this.getVerticalOffsetForLineNumber(midLineNumber);if(verticalOffset>=midLineNumberVerticalOffset+lineHeight)minLineNumber=midLineNumber+1;else{if(verticalOffset>=midLineNumberVerticalOffset)return midLineNumber;maxLineNumber=midLineNumber}}return minLineNumber>linesCount?linesCount:minLineNumber}getLinesViewportData(verticalOffset1,verticalOffset2){this._checkPendingChanges(),verticalOffset1|=0,verticalOffset2|=0;const lineHeight=this._lineHeight,startLineNumber=0|this.getLineNumberAtOrAfterVerticalOffset(verticalOffset1),startLineNumberVerticalOffset=0|this.getVerticalOffsetForLineNumber(startLineNumber);let endLineNumber=0|this._lineCount,whitespaceIndex=0|this.getFirstWhitespaceIndexAfterLineNumber(startLineNumber);const whitespaceCount=0|this.getWhitespacesCount();let currentWhitespaceHeight,currentWhitespaceAfterLineNumber;-1===whitespaceIndex?(whitespaceIndex=whitespaceCount,currentWhitespaceAfterLineNumber=endLineNumber+1,currentWhitespaceHeight=0):(currentWhitespaceAfterLineNumber=0|this.getAfterLineNumberForWhitespaceIndex(whitespaceIndex),currentWhitespaceHeight=0|this.getHeightForWhitespaceIndex(whitespaceIndex));let currentVerticalOffset=startLineNumberVerticalOffset,currentLineRelativeOffset=currentVerticalOffset;let bigNumbersDelta=0;startLineNumberVerticalOffset>=5e5&&(bigNumbersDelta=5e5*Math.floor(startLineNumberVerticalOffset/5e5),bigNumbersDelta=Math.floor(bigNumbersDelta/lineHeight)*lineHeight,currentLineRelativeOffset-=bigNumbersDelta);const linesOffsets=[],verticalCenter=verticalOffset1+(verticalOffset2-verticalOffset1)/2;let centeredLineNumber=-1;for(let lineNumber=startLineNumber;lineNumber<=endLineNumber;lineNumber++){if(-1===centeredLineNumber){(currentVerticalOffset<=verticalCenter&&verticalCenter<currentVerticalOffset+lineHeight||currentVerticalOffset>verticalCenter)&&(centeredLineNumber=lineNumber)}for(currentVerticalOffset+=lineHeight,linesOffsets[lineNumber-startLineNumber]=currentLineRelativeOffset,currentLineRelativeOffset+=lineHeight;currentWhitespaceAfterLineNumber===lineNumber;)currentLineRelativeOffset+=currentWhitespaceHeight,currentVerticalOffset+=currentWhitespaceHeight,whitespaceIndex++,whitespaceIndex>=whitespaceCount?currentWhitespaceAfterLineNumber=endLineNumber+1:(currentWhitespaceAfterLineNumber=0|this.getAfterLineNumberForWhitespaceIndex(whitespaceIndex),currentWhitespaceHeight=0|this.getHeightForWhitespaceIndex(whitespaceIndex));if(currentVerticalOffset>=verticalOffset2){endLineNumber=lineNumber;break}}-1===centeredLineNumber&&(centeredLineNumber=endLineNumber);const endLineNumberVerticalOffset=0|this.getVerticalOffsetForLineNumber(endLineNumber);let completelyVisibleStartLineNumber=startLineNumber,completelyVisibleEndLineNumber=endLineNumber;return completelyVisibleStartLineNumber<completelyVisibleEndLineNumber&&startLineNumberVerticalOffset<verticalOffset1&&completelyVisibleStartLineNumber++,completelyVisibleStartLineNumber<completelyVisibleEndLineNumber&&endLineNumberVerticalOffset+lineHeight>verticalOffset2&&completelyVisibleEndLineNumber--,{bigNumbersDelta,startLineNumber,endLineNumber,relativeVerticalOffset:linesOffsets,centeredLineNumber,completelyVisibleStartLineNumber,completelyVisibleEndLineNumber}}getVerticalOffsetForWhitespaceIndex(whitespaceIndex){this._checkPendingChanges(),whitespaceIndex|=0;const afterLineNumber=this.getAfterLineNumberForWhitespaceIndex(whitespaceIndex);let previousLinesHeight,previousWhitespacesHeight;return previousLinesHeight=afterLineNumber>=1?this._lineHeight*afterLineNumber:0,previousWhitespacesHeight=whitespaceIndex>0?this.getWhitespacesAccumulatedHeight(whitespaceIndex-1):0,previousLinesHeight+previousWhitespacesHeight+this._paddingTop}getWhitespaceIndexAtOrAfterVerticallOffset(verticalOffset){this._checkPendingChanges(),verticalOffset|=0;let minWhitespaceIndex=0,maxWhitespaceIndex=this.getWhitespacesCount()-1;if(maxWhitespaceIndex<0)return-1;if(verticalOffset>=this.getVerticalOffsetForWhitespaceIndex(maxWhitespaceIndex)+this.getHeightForWhitespaceIndex(maxWhitespaceIndex))return-1;for(;minWhitespaceIndex<maxWhitespaceIndex;){const midWhitespaceIndex=Math.floor((minWhitespaceIndex+maxWhitespaceIndex)/2),midWhitespaceVerticalOffset=this.getVerticalOffsetForWhitespaceIndex(midWhitespaceIndex);if(verticalOffset>=midWhitespaceVerticalOffset+this.getHeightForWhitespaceIndex(midWhitespaceIndex))minWhitespaceIndex=midWhitespaceIndex+1;else{if(verticalOffset>=midWhitespaceVerticalOffset)return midWhitespaceIndex;maxWhitespaceIndex=midWhitespaceIndex}}return minWhitespaceIndex}getWhitespaceAtVerticalOffset(verticalOffset){this._checkPendingChanges(),verticalOffset|=0;const candidateIndex=this.getWhitespaceIndexAtOrAfterVerticallOffset(verticalOffset);if(candidateIndex<0)return null;if(candidateIndex>=this.getWhitespacesCount())return null;const candidateTop=this.getVerticalOffsetForWhitespaceIndex(candidateIndex);if(candidateTop>verticalOffset)return null;const candidateHeight=this.getHeightForWhitespaceIndex(candidateIndex);return{id:this.getIdForWhitespaceIndex(candidateIndex),afterLineNumber:this.getAfterLineNumberForWhitespaceIndex(candidateIndex),verticalOffset:candidateTop,height:candidateHeight}}getWhitespaceViewportData(verticalOffset1,verticalOffset2){this._checkPendingChanges(),verticalOffset1|=0,verticalOffset2|=0;const startIndex=this.getWhitespaceIndexAtOrAfterVerticallOffset(verticalOffset1),endIndex=this.getWhitespacesCount()-1;if(startIndex<0)return[];const result=[];for(let i=startIndex;i<=endIndex;i++){const top=this.getVerticalOffsetForWhitespaceIndex(i),height=this.getHeightForWhitespaceIndex(i);if(top>=verticalOffset2)break;result.push({id:this.getIdForWhitespaceIndex(i),afterLineNumber:this.getAfterLineNumberForWhitespaceIndex(i),verticalOffset:top,height})}return result}getWhitespaces(){return this._checkPendingChanges(),this._arr.slice(0)}getWhitespacesCount(){return this._checkPendingChanges(),this._arr.length}getIdForWhitespaceIndex(index){return this._checkPendingChanges(),index|=0,this._arr[index].id}getAfterLineNumberForWhitespaceIndex(index){return this._checkPendingChanges(),index|=0,this._arr[index].afterLineNumber}getHeightForWhitespaceIndex(index){return this._checkPendingChanges(),index|=0,this._arr[index].height}}LinesLayout.INSTANCE_COUNT=0;