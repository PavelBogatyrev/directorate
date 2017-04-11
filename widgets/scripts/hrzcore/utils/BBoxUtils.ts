///<reference path="../../d3/d3.d.ts"/>
///<reference path="../../lodash/lodash.d.ts"/>

/**
 * @author ygorenburgov
 */

class BBoxUtils {

    /**
     * clculate bbox enclosed several input bboxes
     * @param bboxes Array of bboxes
     * @return bbox which enclosed all of input bboxes
     */
    public static getCompositeBBox(bboxes:Array<SVGRect>):SVGRect {
        var bb:SVGRect = {x: 0, y: 0, width: 0, height: 0} as SVGRect;
        bboxes.forEach(function (d) {
            bb.x = Math.min(bb.x, d.x);
            bb.y = Math.min(bb.y, d.y);
            bb.width = Math.max(bb.x + bb.width, d.x + d.width) - bb.x;
            bb.height = Math.max(bb.y + bb.height, d.y + d.height) - bb.y;
        });
        return bb;

    }


    /**
     * calculate max diametr (diagonal) of input bboxes
     * @param bboxes Array of bboxes
     * @return max diametr
     */
    public static getMaxBBoxDiametr(bboxes:Array<SVGRect>):number {
        var ret = 0
        bboxes.forEach(function (d) {
            ret = Math.max(BBoxUtils.getRadius(d.width, d.height, 0, 0))
        });
        return ret;

    }

    /**
     * calculate
     * @param bboxes Array of bboxes
     * @return bbox which enclosed all of input bboxes
     */
    public static getMinEnclosedBBox(bboxes:Array<SVGRect>, cx:number, cy:number):SVGRect {
        var bb:SVGRect = {x: 0, y: 0, width: 0, height: 0} as SVGRect;
        var mins = bboxes.map(function (bbox) {
            return BBoxUtils.getBBoxMinDistance(bbox, cx, cy);
        })

        mins.forEach(function (a) {
            if (a[1] < cx) {
                bb.x = Math.min(bb.x, a[1]);
            } else {
                bb.width = Math.max(bb.width, a[1] - bb.x);
            }

            if (a[2] < cy) {
                bb.y = Math.min(bb.x, a[2]);
            } else {
                bb.height = Math.max(bb.height, a[2] - bb.y);

            }
        });
        return bb;
    }


    /**
     * calculate distance between two points
     * @param x x-ccord of first point
     * @param y y-ccord of second point
     * @param cx x-ccord of second point (center)
     * @param cy y-ccord of second point (center)
     * @return distance calculated by Piphagor theorema
     */
    public static getRadius(x:number, y:number, cx:number, cy:number):number {
        return Math.sqrt((x - cx) * (x - cx) + (y - cy) * (y - cy));
    }



    /**
     * return {x,y} coordinates of all 4 angular points
     * @param bbox input bbox
     * @return array of four points
     */
    public static getBBoxPoints(bbox:SVGRect):Array<Point> {
        return [
            {x: bbox.x, y: bbox.y},
            {x: bbox.x + bbox.width, y: bbox.y},
            {x: bbox.x + bbox.width, y: bbox.y + bbox.height},
            {x: bbox.x, y: bbox.y + bbox.height}
        ]
    }


    /**
     * return maxdistance betwween bbox and specified point
     * @param bbox input bbox
     * @param cx x-coord  of spec point
     * @param cy y-coord  of spec point
     * @return array [R,x,y], where R - max distance, x,y coord of angular point with max distance
     */
    public static getBBoxMaxDistance(bbox:SVGRect, cx:number, cy:number):Array<number> {
        var points = [
            [BBoxUtils.getRadius(bbox.x, bbox.y, cx, cy), bbox.x, bbox.y],
            [BBoxUtils.getRadius(bbox.x + bbox.width, bbox.y, cx, cy), bbox.x + bbox.width, bbox.y],
            [BBoxUtils.getRadius(bbox.x + bbox.width, bbox.y + bbox.height, cx, cy), bbox.x + bbox.width, bbox.y + bbox.height],
            [BBoxUtils.getRadius(bbox.x, bbox.y + bbox.height, cx, cy), bbox.x, bbox.y + bbox.height]
        ];

        return points.reduce(function (ret,a) {
           return !ret || ret[0]<a[0] ? a : ret;
        });
    }


    /**
     * return mindistance betwween bbox and specified point
     * @param bbox input bbox
     * @param cx x-coord  of spec point
     * @param cy y-coord  of spec point
     * @return array [r,x,y], where r - max distance, x,y coord of angular point with min distance
     */
    public static getBBoxMinDistance(bbox:SVGRect, cx:number, cy:number):Array<number> {
        var points = [
            [BBoxUtils.getRadius(bbox.x, bbox.y, cx, cy), bbox.x, bbox.y],
            [BBoxUtils.getRadius(bbox.x + bbox.width, bbox.y, cx, cy), bbox.x + bbox.width, bbox.y],
            [BBoxUtils.getRadius(bbox.x + bbox.width, bbox.y + bbox.height, cx, cy), bbox.x + bbox.width, bbox.y + bbox.height],
            [BBoxUtils.getRadius(bbox.x, bbox.y + bbox.height, cx, cy), bbox.x, bbox.y + bbox.height]
        ];

        return points.reduce(function (ret,a) {
            return !ret || ret[0]>a[0] ? a : ret;
        });
    }


    /**
     * return pie chart angle for specified point
     * @param cx x-coord  of spec point
     * @param cy y-coord  of spec point
     * @return angle
     */
    public static getPointAngle(x:number, y:number):number {
        var y0 = -y;
        var atan = Math.atan(y0 / x);
        var ret;
        if (atan > 0 && x > 0) ret = Math.PI / 2 - atan;
        if (atan > 0 && x < 0) ret = 1.5 * Math.PI - atan;
        if (atan < 0 && x > 0) ret = Math.PI / 2 - atan;
        if (atan < 0 && x < 0) ret = 1.5 * Math.PI - atan;

        return ret;
    }


    /**
     * define if point is inside pie chart angle
     * @param cx x-coord  of spec point
     * @param cy y-coord  of spec point
     * @param startAngle of pie chart sector
     * @param endAngle of pie chart sector
     * @return true if point inside angle, false otherwise
     */
    public static isPointInsideAngle(x:number, y:number, startAngle:number, endAngle:number):boolean {
        var angle = BBoxUtils.getPointAngle(x, y);
        return angle >= startAngle && angle <= endAngle;
    }


    /**
     * define if BBox is inside pie chart sector
     * @param bbox spec bbox
     * @param R outer radius of pie chart
     * @param r inner radius of pie chart
     * @param startAngle of pie chart sector
     * @param endAngle of pie chart sector
     * @return true if bbox inside sector, false otherwise
     */

    public static isBBoxInsideSector(bbox:SVGRect, R:number, r:number, startAngle:number, endAngle:number):boolean {
        var minD = BBoxUtils.getBBoxMinDistance(bbox, 0, 0)[0];
        var maxD = BBoxUtils.getBBoxMaxDistance(bbox, 0, 0)[0];
        if (minD < r || maxD > R) return false;
        var pts:Array<Point> = BBoxUtils.getBBoxPoints(bbox);
        return pts.reduce(function (ret, o) {
                return ret && BBoxUtils.isPointInsideAngle(o.x, o.y, startAngle, endAngle)
            }, true) as boolean;
    }

};


interface Point {
    x:number;
    y:number;
}

export = BBoxUtils;