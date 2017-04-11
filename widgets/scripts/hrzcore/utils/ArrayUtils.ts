/**
 * @author ygorenburgov
 */

class ArrayUtils {
    public static findIndex(arr:Array<any>,callback:(o?:any, ind?:number, arr?:Array<any>) => boolean):number {
         var filtered = arr.filter(callback);
        return  filtered.length ? arr.indexOf(filtered[0]) : -1;
   }

}

export = ArrayUtils;