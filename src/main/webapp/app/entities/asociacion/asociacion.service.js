(function() {
    'use strict';
    angular
        .module('asoApp')
        .factory('Asociacion', Asociacion);

    Asociacion.$inject = ['$resource', 'DateUtils'];

    function Asociacion ($resource, DateUtils) {
        var resourceUrl =  'api/asociacions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.fecchaCreacion = DateUtils.convertDateTimeFromServer(data.fecchaCreacion);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
