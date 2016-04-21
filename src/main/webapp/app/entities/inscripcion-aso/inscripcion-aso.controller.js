(function() {
    'use strict';

    angular
        .module('asoApp')
        .controller('InscripcionAsoController', InscripcionAsoController);

    InscripcionAsoController.$inject = ['$scope', '$state', 'InscripcionAso'];

    function InscripcionAsoController ($scope, $state, InscripcionAso) {
        var vm = this;
        vm.inscripcionAsos = [];
        vm.loadAll = function() {
            InscripcionAso.query(function(result) {
                vm.inscripcionAsos = result;
            });
        };

        vm.loadAll();
        
    }
})();
