(function() {
    'use strict';

    angular
        .module('asoApp')
        .controller('TypeDetailController', TypeDetailController);

    TypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Type', 'Asociacion', 'Evento'];

    function TypeDetailController($scope, $rootScope, $stateParams, entity, Type, Asociacion, Evento) {
        var vm = this;
        vm.type = entity;
        vm.load = function (id) {
            Type.get({id: id}, function(result) {
                vm.type = result;
            });
        };
        var unsubscribe = $rootScope.$on('asoApp:typeUpdate', function(event, result) {
            vm.type = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
