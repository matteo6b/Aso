(function() {
    'use strict';
    angular
        .module('asoApp')
        .factory('InscripcionEvento', InscripcionEvento);

    InscripcionEvento.$inject = ['$resource', 'DateUtils'];

    function InscripcionEvento ($resource, DateUtils) {
        var resourceUrl =  'api/inscripcion-eventos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.fechains = DateUtils.convertDateTimeFromServer(data.fechains);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
