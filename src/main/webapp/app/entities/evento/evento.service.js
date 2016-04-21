(function() {
    'use strict';
    angular
        .module('asoApp')
        .factory('Evento', Evento);

    Evento.$inject = ['$resource', 'DateUtils'];

    function Evento ($resource, DateUtils) {
        var resourceUrl =  'api/eventos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.fecha = DateUtils.convertDateTimeFromServer(data.fecha);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
