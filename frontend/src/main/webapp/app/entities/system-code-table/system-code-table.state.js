(function() {
    'use strict';

    angular
        .module('frontendApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('system-code-table', {
            parent: 'entity',
            url: '/system-code-table?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'frontendApp.systemCodeTable.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/system-code-table/system-code-tables.html',
                    controller: 'SystemCodeTableController',
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
                    $translatePartialLoader.addPart('systemCodeTable');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('system-code-table-detail', {
            parent: 'system-code-table',
            url: '/system-code-table/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'frontendApp.systemCodeTable.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/system-code-table/system-code-table-detail.html',
                    controller: 'SystemCodeTableDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('systemCodeTable');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SystemCodeTable', function($stateParams, SystemCodeTable) {
                    return SystemCodeTable.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'system-code-table',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('system-code-table-detail.edit', {
            parent: 'system-code-table-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/system-code-table/system-code-table-dialog.html',
                    controller: 'SystemCodeTableDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SystemCodeTable', function(SystemCodeTable) {
                            return SystemCodeTable.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('system-code-table.new', {
            parent: 'system-code-table',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/system-code-table/system-code-table-dialog.html',
                    controller: 'SystemCodeTableDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                codeTableName: null,
                                codeTableDesc: null,
                                codeTableEntry: null,
                                codeTableEntryDesc: null,
                                activeIndicator: false,
                                tenantID: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('system-code-table', null, { reload: 'system-code-table' });
                }, function() {
                    $state.go('system-code-table');
                });
            }]
        })
        .state('system-code-table.edit', {
            parent: 'system-code-table',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/system-code-table/system-code-table-dialog.html',
                    controller: 'SystemCodeTableDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SystemCodeTable', function(SystemCodeTable) {
                            return SystemCodeTable.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('system-code-table', null, { reload: 'system-code-table' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('system-code-table.delete', {
            parent: 'system-code-table',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/system-code-table/system-code-table-delete-dialog.html',
                    controller: 'SystemCodeTableDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SystemCodeTable', function(SystemCodeTable) {
                            return SystemCodeTable.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('system-code-table', null, { reload: 'system-code-table' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
