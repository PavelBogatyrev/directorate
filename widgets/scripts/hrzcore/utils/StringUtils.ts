/**
 * @author rlapin
 */

class StringUtils{
    /**
     * Convert input plain text into svg text
     * @param str input text
     * @param fontSize size of font that will be used to draw text
     * @param lineSpace optional ratio value between line interval and font size  . default value is 1
     */
    static formatPlainTextToSvgText(str: string , fontSize: string, lineSpace?: number ):string{
        var prev = 0;
        var index;
        var TSPAN = "</tspan>";
        var i = 0;
        var k = 0;
        var isBold = true;
        lineSpace = lineSpace == null ? 1 : lineSpace;
        var element = document.createElement("tspan");

        while(str.indexOf("<br>")!=-1){
            var i = str.indexOf("<br>");
            var nodes = StringUtils.formatText(str.substr(0, i), "<b>", "</b>", "font-weight","bold");
            nodes.forEach(function(node,i){
                node.setAttribute("dy", parseFloat(fontSize)*(1+lineSpace)*k+"em");
                if(i==0) {
                    k=1;
                    node.setAttribute("x", "0");
                }
                element.appendChild(node);

            });
            str = str.substr(i+"<br>".length);


        }

        var nodes = StringUtils.formatText(str.substr(0), "<b>", "</b>", "font-weight","bold");
        nodes.forEach(function(node,i){
            node.setAttribute("dy", parseFloat(fontSize)*(1+lineSpace)*k+"em");
            if(i==0) {
                node.setAttribute("x", "0");
            }
            element.appendChild(node);
        });
        return element.innerHTML;
    }

    /**
     * Check for <openTag> </closeTag> and if exist add replacement to tspan attribute
     * @param str
     * @param openTag
     * @param closeTag
     * @param attrName
     * @param attrValue
     */
    private static formatText(str:string, openTag:string, closeTag:string, attrName:string, attrValue:string):Array<HTMLElement>{
        var start = 0;
        var end = 0;
        var nodes = [];
        var isOpen = true;
        str = str.trim();
        if(str.indexOf(openTag) == -1){
            var node = document.createElement("tspan");
            node.innerText = str;
            return [node];
        }
        while(isOpen && str.indexOf(openTag)!=-1 || !isOpen && str.indexOf(closeTag)!=-1){
            if(isOpen){
                var end = str.indexOf(openTag);
                    if(end!=0) {
                        var node = document.createElement("tspan");
                        node.innerText = str.substr(0, end);
                        nodes.push(node);
                    }
                    str = str.substr(end+openTag.length);

            }else{
                var end = str.indexOf(closeTag);
                if(end!=0) {
                    var node = document.createElement("tspan");
                    node.setAttribute(attrName,attrValue);
                    node.innerText = str.substr(0, end);
                    nodes.push(node);
                }
                str = str.substr(end+closeTag.length);
            }
            isOpen = !isOpen;
        }
        if(str.length != 0){
            var node = document.createElement("tspan");
            node.innerText = str;
            nodes.push(node);
        }
        return nodes;
    }

    /**
     * Create formatted string from number according to format type
     * @param value
     * @param template
     * @param fpCount
     */

    static formatString(value:number,type:string,fractionalDigits:number):string{

        if (type.length > 0) {
            type = type.toLowerCase();
            if(type.indexOf("%") > -1){
                return value.toFixed(fractionalDigits)+"%";
            }
            if (type.indexOf("m") > -1){
                if(type.indexOf("wf") > -1) {
                    if(Math.abs(value)<100000) {
                            fractionalDigits = 2;
                    }
                }
                value /= 1000000;

            }
            if(type.indexOf("$") > -1){
                return "$"+value.toFixed(fractionalDigits);
            }else if(type.indexOf("d") > -1){
                var s = Math.round(value).toString();
                var str = "";
                var l = s.length;
                var counter = 0;
                for (var i = 0; i<l; i++ ) {
                    str = s.charAt(l-1-i)+str;
                    counter++;
                    if (counter==3) {
                        counter = 0;
                        str = " "+str;
                    }
                }
                return str.trim();
            }
        }
        return value.toFixed(fractionalDigits);

    }


    /**
     * Returns  number  truncated to specified decimals places without(!) rounding
     * @param value -
     * @param decimals
     * @returns
     */
    static truncateDecimals (value:number, decimals:number):number {
        var mult = Math.pow(10,decimals);
        return Math.floor(value*mult)/mult;
    }
}



export = StringUtils;