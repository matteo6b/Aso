(function() {
    'use strict';
    angular
        .module('asoApp')
        .factory('Imagen', Imagen);

    Imagen.$inject = ['$resource'];

    function Imagen ($resource) {
        var resourceUrl =  'api/imagens/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
