(function () {
    'use strict';

    angular
        .module('asoApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];
    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
