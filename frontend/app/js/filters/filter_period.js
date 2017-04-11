bodApp.filter( 'periodFormatConverter', function(ConfigurationService) {
    return function(periodToConvert)
    {
      if (typeof periodToConvert !== 'undefined')
      {
        var month = ConfigurationService.numberToMonth(periodToConvert.slice(-2));
        var year = periodToConvert.substring(0, 4);
        return month + " " + year;
      }
    }
});
