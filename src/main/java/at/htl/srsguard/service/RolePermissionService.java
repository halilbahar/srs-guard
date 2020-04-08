package at.htl.srsguard.service;

import at.htl.srsguard.entity.App;
import at.htl.srsguard.entity.Permission;
import at.htl.srsguard.entity.Role;
import at.htl.srsguard.entity.Stream;
import at.htl.srsguard.model.AppStream;
import at.htl.srsguard.repository.AppRepository;
import at.htl.srsguard.repository.PermissionRepository;
import at.htl.srsguard.repository.StreamRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class RolePermissionService {

    @Inject
    PermissionRepository permissionRepository;
    @Inject
    AppRepository appRepository;
    @Inject
    StreamRepository streamRepository;

    @Transactional
    public void addPermission(Role role, List<AppStream> appStreamList) {
        for (AppStream appStream : appStreamList) {
            String appName = appStream.getApp();
            String streamName = appStream.getStream();

            // First look for existing permission
            Permission existingPermission = this.permissionRepository.find(
                    "app.name = ?1 and stream.name = ?2", appName, streamName
            ).firstResult();

            // If you find an existing permission use that
            if (existingPermission != null) {
                System.out.println("Found existing permission");
                role.getPermissions().add(existingPermission);
                break;
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
}
