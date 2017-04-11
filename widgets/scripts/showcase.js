var HRZN_ACTIVE_WIDGET;
var WIDGETS = [];


function initResizeForContainer(container) {

    var resizeTool = document.createElement("div");

    resizeTool.className = "resizeTool";
    resizeTool.draggable = false;
    container.appendChild(resizeTool);


    var dragWidget;
    resizeTool.addEventListener('mousedown', initDrag, false);

    var startX, startY, startWidth, startHeight;
    var animation;

    function initDrag(e) {
        startX = e.clientX;
        startY = e.clientY;
        dragWidget = WIDGETS.filter(function (v) {
            return v.container === container;
        })[0].widget;
        animation = dragWidget.getConfig().animation;
        animation.active = false;
        startWidth = parseInt(document.defaultView.getComputedStyle(container).width, 10);
        startHeight = parseInt(document.defaultView.getComputedStyle(container).height, 10);
        document.documentElement.addEventListener('mousemove', doDrag, false);
        document.documentElement.addEventListener('mouseup', stopDrag, false);
    }

    function doDrag(e) {
        container.style.width = (startWidth + e.clientX - startX) + 'px';
        container.style.height = (startHeight + e.clientY - startY) + 'px';

        dragWidget.resize();
        dragWidget.render();
        dragWidget.fadeIn();
    }

    function stopDrag(e) {
        document.documentElement.removeEventListener('mousemove', doDrag, false);
        document.documentElement.removeEventListener('mouseup', stopDrag, false);

        dragWidget.resize();
        dragWidget.render();
        dragWidget.fadeIn();
        animation.active = true;
    }

}


function addWidget(widgetName, x, y) {
    function createContainerForWidget(x, y) {
        var element = document.createElement("div");
        element.className = "widgetContainer";
        initResizeForContainer(element);
        if (x && y) {
            element.style.top = y;
            element.style.left = x;

        }
        var screen = document.querySelector("#screen");
        if (screen) {
            screen.appendChild(element);
        }

        return element;
    }


    var container = createContainerForWidget(x, y);
    var createBarChart = function () {
        var barChart = App.BODBarChart.createTrendLineBarChart(container);
        barChart.setData({
            "metaData": [],
            "metaData2": {},
            "data": [
                {
                    "groupName": "30.09.2015",
                    "values": [
                        82114000,
                        82114000,
                        82114000,
                    ]
                },
                {
                    "groupName": "31.12.2015",
                    "values": [
                        97576664.839
                    ]
                },
                {
                    "groupName": "31.03.2016",
                    "values": [
                        108545000
                    ]
                }
            ]
        });
        barChart.getConfig().background = "#fff";
        barChart.render();
        return barChart;
    };

    function create() {

        var waterfall = App.WaterFallChart.create(container);
        waterfall.setData({
            "data": [
                651.9, 652.8, 652.4, 652.4, 651.1, 651.1, 651, 650, 649.9, 651, 650, 650],
            "seriesNames": ["FX2017<br>>Plan", "Fx infl.", "FinX", "FinSS2", "Auto-<br>>motive", "EntS", "TelecomS", "Excelian", "Horizon", "GCoE s<br>>Outside<br>>Lob", "Other", "FY2017<br>>current"]
        });
        waterfall.getConfig().background = "#fff";
        waterfall.render();
        return waterfall;
    }

    function createPieChart() {
        var pieChart = App.PieChart.create(container);
        pieChart.setConfig();
        pieChart.setData({
            "metaData": [],
            "metaData2": {},
            "data": [
                {
                    "groupName": "30.09.2015",
                    "values": [
                        82114000
                    ]
                },
                {
                    "groupName": "31.12.2015",
                    "values": [
                        97576664.839
                    ]
                },
                {
                    "groupName": "31.03.2016",
                    "values": [
                        108545000
                    ]
                }
            ]
        });
        pieChart.render();
        return pieChart;
    }

    var widget;

    function createCombineLineBarChart() {
        var barChart = App.BIChart.createCombineLineBarChart(container);
        barChart.setData({
            "metaData": {
                "lines": [
                    {
                        "values": [
                            42.1,
                            53.2,
                            -36,41.4,23.4,21.4,23.7,42.1,18.3,21.3

                        ],name:"CM% FY2017 Pl"
                    },
                    {
                        "values": [
                            -21.1,-130.1,54.1,59.7,41.2,21.2,37,29,35,36.5
                        ],name:"CM% FY2017"
                    }

                ]

            },
            "data": [
                {
                    "groupName": "DB<br>(+4%){{down}}",
                    "values": [
                        77400000,
                        -50000000

                    ]
                },
                {
                    "groupName": "DB<br>(+4%)",
                    "values": [
                        64700000,
                        63600000
                    ]
                },
                {
                    "groupName": "DB<br>(+4%)",
                    "values": [
                        17500000,
                        17300000
                    ]
                },    {
                    "groupName": "DB<br>(+4%)",
                    "values": [
                        8400000,
                        7900000
                    ]
                },    {
                    "groupName": "DB<br>(+4%){{up}}",
                    "values": [
                        10800000,
                        10700000
                    ]
                },    {
                    "groupName": "DB<br>(+4%)",
                    "values": [
                        5100000,
                        5300000
                    ]
                },    {
                    "groupName": "DB<br>(+4%)",
                    "values": [
                        5400000,
                        5900000
                    ]
                },    {
                    "groupName": "DB<br>(+4%)",
                    "values": [
                        5200000,
                        5000000
                    ]
                }, {
                    "groupName": "DB<br>(+4%)",
                    "values": [
                        3300000,
                        3200000
                    ]
                }, {
                    "groupName": "DB<br>(+4%)",
                    "values": [
                        3400000,
                        3300000
                    ]
                }
            ],
            "seriesNames": ["CM FY2017 pl","CM FY2017"]
        });
        barChart.getConfig().barGroup.padding = 0.1;
        barChart.getConfig().bar.padding = 0.05;
        barChart.getConfig().background = "#fff";
        barChart.render();
        return barChart;
    }

    switch (widgetName) {

        case "piechart":
            widget = createPieChart();
            break;
        case "barchart":
            widget = createBarChart();
            break;
        case "waterfall":
            widget = create();
            break;
        case "combine":
            widget = createCombineLineBarChart();
            break;
        default:

    }
    setActiveWidget(widget);
    WIDGETS.push({"container": container, "widget": widget});

    function setActiveWidget(widget) {
        if (HRZN_ACTIVE_WIDGET) {
            HRZN_ACTIVE_WIDGET.element.classList.remove("selectedWidget");
        }
        HRZN_ACTIVE_WIDGET = widget;
        HRZN_ACTIVE_WIDGET.element.classList.add("selectedWidget");
    }

    container.addEventListener("click", function () {

        setActiveWidget(widget);
    }, false);
    container.addEventListener()
}


function init() {

    document.querySelector("#fadeIn").addEventListener("click", function (evt) {
        if (HRZN_ACTIVE_WIDGET) {
            HRZN_ACTIVE_WIDGET.fadeIn();
        }
    });
    document.querySelector("#fadeOut").addEventListener("click", function (evt) {
        if (HRZN_ACTIVE_WIDGET) {
            HRZN_ACTIVE_WIDGET.fadeOut();
        }
    });
    document.querySelector("#randomize").addEventListener("click", function (evt) {
        if (HRZN_ACTIVE_WIDGET) {
            var data = HRZN_ACTIVE_WIDGET.getData();
            for (var i = 0; i < data.data.length; i++) {
                data.data[i] = Math.random() * 100;
            }
            HRZN_ACTIVE_WIDGET.setData(data);
            HRZN_ACTIVE_WIDGET.dataUpdate();
        }
    });

}

