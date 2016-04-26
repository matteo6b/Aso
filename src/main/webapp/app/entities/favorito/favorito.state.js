(function() {
    'use strict';

    angular
        .module('asoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('favorito', {
            parent: 'entity',
            url: '/favorito',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'asoApp.favorito.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/favorito/favoritos.html',
                    controller: 'FavoritoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('favorito');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('favorito-detail', {
            parent: 'entity',
            url: '/favorito/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'asoApp.favorito.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/favorito/favorito-detail.html',
                    controller: 'FavoritoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('favorito');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Favorito', function($stateParams, Favorito) {
                    return Favorito.get({id : $stateParams.id});
                }]
            }
        })
        .state('favorito.new', {
            parent: 'favorito',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/favorito/favorito-dialog.html',
                    controller: 'FavoritoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                like: null,
                                likes: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('favorito', null, { reload: true });
                }, function() {
                    $state.go('favorito');
                });
            }]
        })
        .state('favorito.edit', {
            parent: 'favorito',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/favorito/favorito-dialog.html',
                    controller: 'FavoritoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Favorito', function(Favorito) {
                            return Favorito.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('favorito', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('favorito.delete', {
            parent: 'favorito',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/favorito/favorito-delete-dialog.html',
                    controller: 'FavoritoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Favorito', function(Favorito) {
                            return Favorito.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('favorito', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
