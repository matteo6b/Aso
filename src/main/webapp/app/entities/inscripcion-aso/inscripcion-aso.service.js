(function() {
    'use strict';
    angular
        .module('asoApp')
        .factory('InscripcionAso', InscripcionAso);

    InscripcionAso.$inject = ['$resource', 'DateUtils'];

    function InscripcionAso ($resource, DateUtils) {
        var resourceUrl =  'api/inscripcion-asos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.fechaInscrito = DateUtils.convertDateTimeFromServer(data.fechaInscrito);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
