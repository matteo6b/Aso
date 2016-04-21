(function() {
    'use strict';

    angular
        .module('asoApp')
        .controller('ImagenDialogController', ImagenDialogController);

    ImagenDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Imagen', 'User', 'Asociacion', 'Evento'];

    function ImagenDialogController ($scope, $stateParams, $uibModalInstance, entity, Imagen, User, Asociacion, Evento) {
        var vm = this;
        vm.imagen = entity;
        vm.users = User.query();
        vm.asociacions = Asociacion.query();
        vm.eventos = Evento.query();
        vm.load = function(id) {
            Imagen.get({id : id}, function(result) {
                vm.imagen = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('asoApp:imagenUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.imagen.id !== null) {
                Imagen.update(vm.imagen, onSaveSuccess, onSaveError);
            } else {
                Imagen.save(vm.imagen, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
