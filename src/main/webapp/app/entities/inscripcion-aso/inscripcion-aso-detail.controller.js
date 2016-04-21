(function() {
    'use strict';

    angular
        .module('asoApp')
        .controller('InscripcionAsoDetailController', InscripcionAsoDetailController);

    InscripcionAsoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'InscripcionAso', 'Asociacion', 'User'];

    function InscripcionAsoDetailController($scope, $rootScope, $stateParams, entity, InscripcionAso, Asociacion, User) {
        var vm = this;
        vm.inscripcionAso = entity;
        vm.load = function (id) {
            InscripcionAso.get({id: id}, function(result) {
                vm.inscripcionAso = result;
            });
        };
        var unsubscribe = $rootScope.$on('asoApp:inscripcionAsoUpdate', function(event, result) {
            vm.inscripcionAso = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
