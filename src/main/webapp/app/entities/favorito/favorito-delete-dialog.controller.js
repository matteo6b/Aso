(function() {
    'use strict';

    angular
        .module('asoApp')
        .controller('FavoritoDeleteController',FavoritoDeleteController);

    FavoritoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Favorito'];

    function FavoritoDeleteController($uibModalInstance, entity, Favorito) {
        var vm = this;
        vm.favorito = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Favorito.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
