(function() {
    'use strict';

    angular
        .module('asoApp')
        .controller('InscripcionEventoDetailController', InscripcionEventoDetailController);

    InscripcionEventoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'InscripcionEvento', 'Evento', 'User'];

    function InscripcionEventoDetailController($scope, $rootScope, $stateParams, entity, InscripcionEvento, Evento, User) {
        var vm = this;
        vm.inscripcionEvento = entity;
        vm.load = function (id) {
            InscripcionEvento.get({id: id}, function(result) {
                vm.inscripcionEvento = result;
            });
        };
        var unsubscribe = $rootScope.$on('asoApp:inscripcionEventoUpdate', function(event, result) {
            vm.inscripcionEvento = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
