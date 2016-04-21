(function() {
    'use strict';

    angular
        .module('asoApp')
        .controller('InscripcionEventoController', InscripcionEventoController);

    InscripcionEventoController.$inject = ['$scope', '$state', 'InscripcionEvento'];

    function InscripcionEventoController ($scope, $state, InscripcionEvento) {
        var vm = this;
        vm.inscripcionEventos = [];
        vm.loadAll = function() {
            InscripcionEvento.query(function(result) {
                vm.inscripcionEventos = result;
            });
        };

        vm.loadAll();
        
    }
})();
