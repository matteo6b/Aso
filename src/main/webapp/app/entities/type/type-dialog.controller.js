(function() {
    'use strict';

    angular
        .module('asoApp')
        .controller('TypeDialogController', TypeDialogController);

    TypeDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Type', 'Asociacion', 'Evento'];

    function TypeDialogController ($scope, $stateParams, $uibModalInstance, entity, Type, Asociacion, Evento) {
        var vm = this;
        vm.type = entity;
        vm.asociacions = Asociacion.query();
        vm.eventos = Evento.query();
        vm.load = function(id) {
            Type.get({id : id}, function(result) {
                vm.type = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('asoApp:typeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.type.id !== null) {
                Type.update(vm.type, onSaveSuccess, onSaveError);
            } else {
                Type.save(vm.type, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
