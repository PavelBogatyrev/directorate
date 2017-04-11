bodApp.filter( 'renderHTMLCorrectly', function($sce) {
    return function(stringToParse)
    {
        return $sce.trustAsHtml(stringToParse);
    }
});