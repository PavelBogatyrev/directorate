bodApp.factory('LabelService', ['$http', '$rootScope', '$q', '$log', 'ConfigurationService',
    function ($http, $rootScope, $q, $log, ConfigurationService) {


        function getMonth(month){
          return (month < 0) ? "" : ["January","February","March","April","May","June","July","August","September","October","November","December"][month];
        };

        /**
         * resolve labels from template string
         * @param str - template string to resolve, for ex.  "{fin_year-1}", "{month}"
         * @return resolved string, for ex resolveLabels("{fin_year-1}")  =  "FY16"
         */
        function resolveLabels(str) {
            var ret = "";
            try {

                var formatters = {
                    fin_year: function (c) {
                        return "" + c.year.toString().substr(-2, 2);
                    },

                    month:function(c){
                        return getMonth(c.month);
                    },

                    active_year:function(c){
                        return (c < 0) ? "" : "" + c.year.toString();
                    },

                    active_month:function(c){
                        return getMonth(c.month);
                    }

                };
                var context = {
                    year: ConfigurationService.getCurrentFinYear(),
                    month: ConfigurationService.getCurrentMonth()
                };
                var re = /\{[^\{\}]*\}/g;
                var match, cursor = 0, tokens, formatToken, contextClone;
                while (match = re.exec(str)) {
                    contextClone = cloneObject(context);
                    ret += str.slice(cursor, match.index);
                    tokens = match[0].substring(1, match[0].length - 1).split(/([+-])/);
                    formatToken = tokens[0].trim();
                    if (tokens.length > 2) {
                        if (formatToken == "fin_year") {
                            contextClone.year = eval("" + contextClone.year + tokens[1] + tokens[2]);
                        }
                        if (formatToken == "month") {
                            contextClone.month = (eval("" + contextClone.month + tokens[1] + tokens[2])+12) % 12;
                        }
                    }
                    if (formatToken == "active_year") {
                        contextClone.year = $rootScope.activePeriodYear || -1;
                    }
                    if (formatToken == "active_month") {
                        contextClone.month = ($rootScope.activePeriodMonth || 0) - 1;
                    }
                    ret += formatters[formatToken.toLowerCase()](contextClone, formatters);
                    cursor = match.index + match[0].length;
                }
                ret += str.substr(cursor, str.length - cursor);
            } catch (e) {
                $log.error(e);
                ret = str;
            }
            return ret;

            function cloneObject(trg) {
                if (!trg) return null;
                var clone = {};
                for (var attr in trg) {
                    if (trg.hasOwnProperty(attr)) clone[attr] = trg[attr];
                }
                return clone;
            }
        }

        var labels;
        var cached = false;

        /**
         * Look for the override value first then return default value
         * substitute varables in labels
         *
         * @param id
         * @param currentQuarter
         * @returns {*} overried or defaule value
         */
        function override(id, currentQuarter) {
            var label = labels[id];
            if (!label) {
                return "";
            } else {
                var labelValue = labels[id].label;
                if(labels[id].overrides){
                labelValue = labels[id].overrides[currentQuarter];
                if (!labelValue) {
                    labelValue = labels[id].label;
                }
                }

                var quarter = {};
                quarter.year = parseInt(ConfigurationService.getCurrentQuarter().substring(0, 4));
                quarter.quarter = parseInt(ConfigurationService.getCurrentQuarter().substring(5, 6));
                var resolvedLabel = resolveLabels(labelValue);
                return resolvedLabel;
            }
        }

        return {

            resolveLabels:resolveLabels,

            getLabel: function (id, callback) {
                if (cached) {
                    var label = override(id, ConfigurationService.getCurrentQuarter());
                    callback(label);
                } else {
                    labels = [];
                    $http({
                        method: 'GET',
                        url: ConfigurationService.url + 'labels',
                        cache: true
                    }).then(function (response) {
                        response.data.forEach(function (entry) {
                            labels[parseInt(entry.identifier)] = entry;
                        });
                        cached = true;
                        var label = override(id, ConfigurationService.getCurrentQuarter());
                        callback(label);
                    });
                }
            }
        };

    }]);
