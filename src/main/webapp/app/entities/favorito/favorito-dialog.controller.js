(function() {
    'use strict';

    angular
        .module('asoApp')
        .controller('FavoritoDialogController', FavoritoDialogController);

    FavoritoDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Favorito', 'Asociacion', 'User', 'Evento'];

    function FavoritoDialogController ($scope, $stateParams, $uibModalInstance, entity, Favorito, Asociacion, User, Evento) {
        var vm = this;
        vm.favorito = entity;
        vm.asociacions = Asociacion.query();
        vm.users = User.query();
        vm.eventos = Evento.query();
        vm.load = function(id) {
            Favorito.get({id : id}, function(result) {
                vm.favorito = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('asoApp:favoritoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.favorito.id !== null) {
                Favorito.update(vm.favorito, onSaveSuccess, onSaveError);
            } else {
                Favorito.save(vm.favorito, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
