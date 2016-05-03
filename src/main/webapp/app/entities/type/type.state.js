(function() {
    'use strict';

    angular
        .module('asoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('type', {
            parent: 'entity',
            url: '/type?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'asoApp.type.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type/types.html',
                    controller: 'TypeController',
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
                    $translatePartialLoader.addPart('type');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('type-detail', {
            parent: 'entity',
            url: '/type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'asoApp.type.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type/type-detail.html',
                    controller: 'TypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('type');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Type', function($stateParams, Type) {
                    return Type.get({id : $stateParams.id});
                }]
            }
        })
        .state('type.new', {
            parent: 'type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type/type-dialog.html',
                    controller: 'TypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nombre: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('type', null, { reload: true });
                }, function() {
                    $state.go('type');
                });
            }]
        })
        .state('type.edit', {
            parent: 'type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type/type-dialog.html',
                    controller: 'TypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Type', function(Type) {
                            return Type.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type.delete', {
            parent: 'type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type/type-delete-dialog.html',
                    controller: 'TypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Type', function(Type) {
                            return Type.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
