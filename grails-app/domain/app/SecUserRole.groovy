package app

import org.apache.commons.lang.builder.HashCodeBuilder

class SecUserRole implements Serializable {

	SecUser user
	SecRole role

	boolean equals(other) {
		if (!(other instanceof SecUserRole)) {
			return false
		}

		other.user?.id == user?.id &&
			other.role?.id == role?.id
	}

	int hashCode() {
		def builder = new HashCodeBuilder()
		if (user) builder.append(user.id)
		if (role) builder.append(role.id)
		builder.toHashCode()
	}

	static SecUserRole get(long userId, long roleId) {
		find 'from SecUserRole where user.id=:userId and role.id=:roleId',
			[userId: userId, roleId: roleId]
	}

	static SecUserRole create(SecUser user, SecRole role, boolean flush = false) {
		new SecUserRole(user: user, role: role).save(flush: flush, insert: true)
	}

	static boolean remove(SecUser user, SecRole role, boolean flush = false) {
		SecUserRole instance = SecUserRole.findByUserAndRole(user, role)
		if (!instance) {
			return false
		}

		instance.delete(flush: flush)
		true
	}

	static void removeAll(SecUser user) {
		executeUpdate 'DELETE FROM SecUserRole WHERE user=:user', [user: user]
	}

	static void removeAll(SecRole role) {
		executeUpdate 'DELETE FROM SecUserRole WHERE role=:role', [role: role]
	}

	static mapping = {
		id composite: ['role', 'user']
		version false
	}
}
