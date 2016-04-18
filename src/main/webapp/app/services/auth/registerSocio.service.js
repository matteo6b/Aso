(function () {
    'use strict';

    angular
        .module('asoApp')
        .factory('RegisterSocio',RegisterSocio);

    RegisterSocio.$inject = ['$resource'];
    function RegisterSocio ($resource) {
        return $resource('api/registerSocio', {}, {});
    }
})();
