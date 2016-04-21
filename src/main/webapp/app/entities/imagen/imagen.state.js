(function() {
    'use strict';

    angular
        .module('asoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('imagen', {
            parent: 'entity',
            url: '/imagen?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'asoApp.imagen.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/imagen/imagens.html',
                    controller: 'ImagenController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('imagen');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('imagen-detail', {
            parent: 'entity',
            url: '/imagen/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'asoApp.imagen.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/imagen/imagen-detail.html',
                    controller: 'ImagenDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('imagen');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Imagen', function($stateParams, Imagen) {
                    return Imagen.get({id : $stateParams.id});
                }]
            }
        })
        .state('imagen.new', {
            parent: 'imagen',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/imagen/imagen-dialog.html',
                    controller: 'ImagenDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                url: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('imagen', null, { reload: true });
                }, function() {
                    $state.go('imagen');
                });
            }]
        })
        .state('imagen.edit', {
            parent: 'imagen',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/imagen/imagen-dialog.html',
                    controller: 'ImagenDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Imagen', function(Imagen) {
                            return Imagen.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('imagen', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('imagen.delete', {
            parent: 'imagen',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/imagen/imagen-delete-dialog.html',
                    controller: 'ImagenDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Imagen', function(Imagen) {
                            return Imagen.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('imagen', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
