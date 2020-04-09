package at.htl.srsguard.service;

import at.htl.srsguard.entity.App;
import at.htl.srsguard.entity.Permission;
import at.htl.srsguard.entity.Role;
import at.htl.srsguard.entity.Stream;
import at.htl.srsguard.model.AppStream;
import at.htl.srsguard.repository.AppRepository;
import at.htl.srsguard.repository.PermissionRepository;
import at.htl.srsguard.repository.RoleRepository;
import at.htl.srsguard.repository.StreamRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;

@ApplicationScoped
public class RolePermissionService {

    @Inject
    PermissionRepository permissionRepository;
    @Inject
    AppRepository appRepository;
    @Inject
    StreamRepository streamRepository;
    @Inject
    RoleRepository roleRepository;

    @Transactional
    public void addPermission(Role role, List<AppStream> appStreamList) {
        for (AppStream appStream : appStreamList) {
            String appName = appStream.getApp();
            String streamName = appStream.getStream();

            // Check if the role already has that permission
            boolean hasPermission = role.getPermissions().stream().anyMatch(permission ->
                    permission.getApp().getName().equals(appName) && permission.getStream().getName().equals(streamName)
            );
            if (hasPermission) continue;

            // Look for existing permission
            Permission existingPermission = this.permissionRepository.find(
                    "app.name = ?1 and stream.name = ?2", appName, streamName
            ).firstResult();

            // If you find an existing permission use that
            if (existingPermission != null) {
                role.getPermissions().add(existingPermission);
                continue;
            }

            // Try to find if the app and stream already exist in the database
            App requiredApp = this.appRepository.find("name", appName).firstResult();
            Stream requiredStream = this.streamRepository.find("name", streamName).firstResult();

            // If they dont exist, create them
            if (requiredApp == null) requiredApp = new App(appName);
            if (requiredStream == null) requiredStream = new Stream(streamName);

            Permission permission = new Permission(requiredApp, requiredStream);
            this.permissionRepository.persistPermission(permission);
            role.getPermissions().add(permission);
        }
    }

    @Transactional
    public void removePermissions(Long id, List<AppStream> appStreamList) {
        Role role = this.roleRepository.findById(id);
        List<Permission> permissions = role.getPermissions();
        List<Permission> toBeDeletedPermission = new LinkedList<>();

        for (AppStream appStream : appStreamList) {
            for (Permission permission : permissions) {
                // Collect the permission if you match app and stream
                if (permission.getApp().getName().equals(appStream.getApp()) &&
                        permission.getStream().getName().equals(appStream.getStream())) {
                    toBeDeletedPermission.add(permission);
                }
            }
        }

        // First delete it from the role
        permissions.removeAll(toBeDeletedPermission);
        // Then delete the orphan permissions who dont belong to any permission
        List<Permission> emptyPermissions = this.permissionRepository.find("size(roles) = 0").list();
        emptyPermissions.forEach(this.permissionRepository::deletePermission);
    }
}
