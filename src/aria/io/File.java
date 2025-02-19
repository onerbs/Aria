/*
 * Copyright (C) 2020 Alejandro Elí
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package aria.io;

import aria.util.Tools;

/**
 * Enhanced File.
 *
 * @author  Alejandro Elí
 * @version 24/05/20
 * @since   1.0
 */
public class File extends java.io.File implements FileInterface {

	private static final long serialVersionUID = 7005118070968941416L;

	/**
	 * Initialize a File.
	 *
	 * <p>This file will represent the current working directory</p>
	 */
	public File() {
		super("");
	}

	/**
	 * Initialize a File from a path.
	 *
	 * <p>Initialize a File from the specified path</p>
	 *
	 * @param path The path to the file
	 */
	public File(final String path) {
		super(path);
	}

	/**
	 * Initialize a File from a path.
	 *
	 * <p>Initialize a File from the specified path</p>
	 *
	 * @param path The path to the file
	 */
	public File(final java.nio.file.Path path) {
		super(path.toAbsolutePath().toString());
	}

	/**
	 * Initialize a File from a {@code java.io.File}.
	 *
	 * <p>This file will contain the same path as the specified file</p>
	 *
	 * @param origin The origin file
	 */
	public File(final java.io.File origin) {
		super(origin.getAbsolutePath());
	}

	/**
	 * Initialize a File from a composed path.
	 *
	 * <p>Initialize a File from the specified path. The path will be
	 * created by joining {@code parent} and {@code child} strings by a {@code
	 * /}.</p>
	 *
	 * @param parent The path to the parent file
	 * @param child  The name of the child file
	 */
	public File(final String parent, final String child) {
		this(String.format("%s/%s", parent, child));
	}

	/**
	 * Initialize a File from a composed path.
	 *
	 * <p>Initialize a File from the specified path. The path will be
	 * created by joining {@code parent.toAbsolutePath().toString()} and {@code
	 * child} strings by a {@code /}.</p>
	 *
	 * @param parent The parent file path
	 * @param child  The name of the file
	 */
	public File(final java.nio.file.Path parent, final String child) {
		this(parent.toAbsolutePath().toString(), child);
	}

	/**
	 * Initialize a File from a composed path.
	 *
	 * <p>Initialize a File from the specified path. The path will be
	 * created by joining {@code parent.toAbsolutePath()} and {@code child}
	 * strings by a {@code /}.</p>
	 *
	 * @param parent The parent file
	 * @param child  The name of the file
	 */
	public File(final java.io.File parent, final String child) {
		this(parent.getAbsolutePath(), child);
	}

	@Override
	public boolean delete() {
		if (super.delete()) return true;
		System.err.println(this + " was not deleted.");
		return false;
	}

	@Override
	public boolean moveTo(final java.io.File folder) throws java.io.IOException {
		if (!folder.exists()) throw new java.io.FileNotFoundException(folder + " does not exist.");
		if (!folder.isDirectory()) throw new java.io.IOException("Can't move " + this);
		java.nio.file.Files.move(toPath(), folder.toPath(), java.nio.file.StandardCopyOption.ATOMIC_MOVE);
		return true;
	}

	@Override
	public String toString() {
		return getAbsolutePath();
	}
}


/**
 * Enhanced File Interface.
 *
 * @author  Alejandro Elí
 * @version 24/05/20
 * @since   1.0
 */
interface FileInterface extends BaseFileInterface {

	/**
	 * Move this file to a directory different than the parent one.
	 *
	 * @param folder The destination folder
	 *
	 * @return {@code true} if the operation was successful
	 *
	 * @throws java.io.IOException if can't move or if the
	 *                             {@code File} is not a directory
	 */
	boolean moveTo(java.io.File folder) throws java.io.IOException;

	/**
	 * Move this file to a directory different than the parent one.
	 *
	 * @param path The path to the new parent folder
	 *
	 * @return {@code true} if the operation was successful
	 *
	 * @throws java.io.IOException if can't move or if the specified
	 *                             path does not points to a directory
	 */
	default boolean moveTo(CharSequence path) throws java.io.IOException {
		return moveTo(new java.io.File(String.valueOf(path)));
	}

	/**
	 * Move this file to a Directory.
	 *
	 * @param folder The destination folder
	 *
	 * @return {@code true} if the operation was successful
	 *
	 * @throws java.io.IOException if can't move
	 */
	default boolean moveTo(Folder folder) throws java.io.IOException {
		return moveTo(folder.getAbsoluteFile());
	}

	/**
	 * Change this file's name.
	 *
	 * <p>Many aspects of the behavior of this method are inherently
	 * platform-dependent: The rename operation might not be able to move a file
	 * from one filesystem to another, it might not be atomic, and it might not
	 * succeed if a file with the destination abstract pathname already exists.
	 * The return value should always be checked to make sure that the rename
	 * operation was successful.</p
	 *
	 * <p>Note that the {@link java.nio.file.Files} class defines the {@link
	 * java.nio.file.Files#move move} method to move or rename a file in a
	 * platform independent manner.</p>
	 *
	 * @param name The new name for this file
	 *
	 * @return {@code true} if the operation was successful
	 */
	default boolean rename(String name) {
		if (!Tools.bool(name)) return false;
		var nova = new java.io.File(getParent(), name);
		if (nova.exists()) {
			System.err.println(nova.getAbsolutePath() + " already exist.");
			return false;
		}
		return renameTo(nova);
	}
}
