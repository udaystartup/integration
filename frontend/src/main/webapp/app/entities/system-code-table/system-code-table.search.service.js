(function() {
    'use strict';

    angular
        .module('frontendApp')
        .factory('SystemCodeTableSearch', SystemCodeTableSearch);

    SystemCodeTableSearch.$inject = ['$resource'];

    function SystemCodeTableSearch($resource) {
        var resourceUrl =  'coreengine/' + 'api/_search/system-code-tables/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
