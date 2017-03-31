(function() {
    'use strict';
    angular
        .module('frontendApp')
        .factory('SystemCodeTable', SystemCodeTable);

    SystemCodeTable.$inject = ['$resource'];

    function SystemCodeTable ($resource) {
        var resourceUrl =  'coreengine/' + 'api/system-code-tables/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
