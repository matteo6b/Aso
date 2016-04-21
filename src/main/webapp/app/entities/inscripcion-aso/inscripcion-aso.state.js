(function() {
    'use strict';

    angular
        .module('asoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('inscripcion-aso', {
            parent: 'entity',
            url: '/inscripcion-aso',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'asoApp.inscripcionAso.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/inscripcion-aso/inscripcion-asos.html',
                    controller: 'InscripcionAsoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('inscripcionAso');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('inscripcion-aso-detail', {
            parent: 'entity',
            url: '/inscripcion-aso/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'asoApp.inscripcionAso.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/inscripcion-aso/inscripcion-aso-detail.html',
                    controller: 'InscripcionAsoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('inscripcionAso');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InscripcionAso', function($stateParams, InscripcionAso) {
                    return InscripcionAso.get({id : $stateParams.id});
                }]
            }
        })
        .state('inscripcion-aso.new', {
            parent: 'inscripcion-aso',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inscripcion-aso/inscripcion-aso-dialog.html',
                    controller: 'InscripcionAsoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                validar: null,
                                fechaInscrito: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('inscripcion-aso', null, { reload: true });
                }, function() {
                    $state.go('inscripcion-aso');
                });
            }]
        })
        .state('inscripcion-aso.edit', {
            parent: 'inscripcion-aso',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inscripcion-aso/inscripcion-aso-dialog.html',
                    controller: 'InscripcionAsoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InscripcionAso', function(InscripcionAso) {
                            return InscripcionAso.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('inscripcion-aso', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('inscripcion-aso.delete', {
            parent: 'inscripcion-aso',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inscripcion-aso/inscripcion-aso-delete-dialog.html',
                    controller: 'InscripcionAsoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InscripcionAso', function(InscripcionAso) {
                            return InscripcionAso.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('inscripcion-aso', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
