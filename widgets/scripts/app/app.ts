///<reference path="../../scripts/hrzcore/widgets/IWidget.ts"/>
/**
 * @author rlapin
 */
import FigureWidget = require("../hrzcore/widgets/figureanimation/FigureAnimationWidget");
import FigureWidgetConfig = require("../hrzcore/widgets/figureanimation/FigureAnimationConfiguration");
import FType = require("../hrzcore/widgets/figureanimation/FigureType");
import BChart = require("../hrzcore/widgets/bodbarchart/BODBarChart");
import RefBIChart = require("../hrzcore/widgets/bibarchart/BIBarChart");
import BODPChart = require("../hrzcore/widgets/bodpiechart/BODPieChart");
import WFChart = require("../hrzcore/widgets/waterfallchart/WaterfallChart");
import PChart = require("../hrzcore/widgets/piechart/PieChart");
import LChart = require("../hrzcore/widgets/linechart/LineChart");
import RefBILineChart = require("../hrzcore/widgets/bilinechart/BILineChart");
import WMap = require("../hrzcore/widgets/worldmap/WorldMap");
import SUtils = require("../hrzcore/utils/StringUtils");
export var FigureAnimationWidget = FigureWidget;
export var FigureType = FType;
export var FigureAnimationConfiguration = FigureWidgetConfig;
export var BODBarChart = BChart;
export var BODPieChart = BODPChart;
export var BIChart = RefBIChart;
export var PieChart = PChart;
export var LineChart = LChart;
export var StringUtils = SUtils;
export var WorldMap = WMap;
export var WaterFallChart = WFChart;
export var BILineChart = RefBILineChart;
