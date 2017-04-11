///<reference path="../../d3/d3.d.ts"/>
///<reference path="../../lodash/lodash.d.ts"/>

/**
 * @author ygorenburgov
 */

class ObjectUtils{
    /**
     * Convert object keys from camelCase to kebab-case and return new object
     * @param obj input object
     * @return new object produced by converting obj
     */
    public static convertObjectKeysToKebabCase(obj:Object):{ [key: string]: any } {
        var convertingRules = {
            fontFamily:"font-family",
            fontSize:"font-size",
            fontWeight:"font-weight",
            textAnchor:"text-anchor",
            strokeWidth:"stroke-width",
            shapeRendering:"shape-rendering"
        };
        var ret = {};
        for (var attr in obj)  {
            if(obj.hasOwnProperty(attr)) {
                ret[convertingRules[attr] || attr] = obj[attr];
            }
        };
        return ret;
    }

    public static deepExtend (...args0: any[]) {
    if (arguments.length < 1 || typeof arguments[0] !== 'object') {
        return false;
    }

    if (arguments.length < 2) {
        return arguments[0];
    }

    var target = arguments[0];

    // convert arguments to array and cut off target object
    var args = Array.prototype.slice.call(arguments, 1);

    var val, src, clone;

    args.forEach(function (obj) {
        // skip argument if it is array or isn't object
        if (typeof obj !== 'object' || Array.isArray(obj)) {
            return;
        }

        Object.keys(obj).forEach(function (key) {
            src = target[key]; // source value
            val = obj[key]; // new value

            // recursion prevention
            if (val === target) {
                return;

                /**
                 * if new value isn't object then just overwrite by new value
                 * instead of extending.
                 */
            } else if (typeof val !== 'object' || val === null) {
                target[key] = val;
                return;

                // just clone arrays (and recursive clone objects inside)
            } else if (Array.isArray(val)) {
                target[key] = ObjectUtils.deepCloneArray(val);
                return;

                // custom cloning and overwrite for specific objects
            } else if (ObjectUtils.isSpecificValue(val)) {
                target[key] = ObjectUtils.cloneSpecificValue(val);
                return;

                // overwrite by new value if source isn't object or array
            } else if (typeof src !== 'object' || src === null || Array.isArray(src)) {
                target[key] = ObjectUtils.deepExtend({}, val);
                return;

                // source value and new value is objects both, extending...
            } else {
                target[key] = ObjectUtils.deepExtend(src, val);
                return;
            }
        });
    });

    return target;
}


    public static  isSpecificValue(val):boolean {
    return (val instanceof Date
        || val instanceof RegExp
    ) ? true : false;
}

    public static cloneSpecificValue(val):any {
    if (val instanceof Date) {
        return new Date(val.getTime());
    } else if (val instanceof RegExp) {
        return new RegExp(val);
    } else {
        throw new Error('Unexpected situation');
    }
}

    /**
     * Recursive cloning array.
     */
    public static deepCloneArray(arr) {
    var clone = [];
    arr.forEach(function (item, index) {
        if (typeof item === 'object' && item !== null) {
            if (Array.isArray(item)) {
                clone[index] = ObjectUtils.deepCloneArray(item);
            } else if (ObjectUtils.isSpecificValue(item)) {
                clone[index] = ObjectUtils.cloneSpecificValue(item);
            } else {
                clone[index] = ObjectUtils.deepExtend({}, item);
            }
        } else {
            clone[index] = item;
        }
    });
    return clone;
}



}

export = ObjectUtils;