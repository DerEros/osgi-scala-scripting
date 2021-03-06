package de.erna.scripting.scala.bundlefs

import java.io.IOException
import java.net.{URL, URLConnection, URLStreamHandler}

import org.osgi.framework.Bundle
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.reflect.internal.util.NoFile
import scala.reflect.io.AbstractFile

class BundleEntryTest extends FunSuite with MockitoSugar with BeforeAndAfter {
  var bundleMock: Bundle = _
  var baseUrl = new URL("file://123/")
  var parentMock: DirEntry = _

  class TestBundleEntry(bundle: Bundle, url: URL, parent: DirEntry, val subEntry: AbstractFile = null)
    extends BundleEntry(bundle, url, parent) {

    override def isDirectory: Boolean = false
    override def iterator: Iterator[AbstractFile] = Iterator.empty
    override def lookupName(name: String, directory: Boolean): AbstractFile = subEntry
  }

  def createBundleEntry() = new TestBundleEntry(bundleMock, baseUrl, parentMock)

  before {
    bundleMock = mock[Bundle]
    parentMock = mock[DirEntry]
  }

  test ("Returns itself for absolute path") {
    val entry = createBundleEntry()
    assert(entry.absolute == entry)
  }

  test ("Returns its own URL") {
    assert(createBundleEntry().toURL == baseUrl)
  }

  test ("Last modification time of dummy URL is zero") {
    assert(createBundleEntry().lastModified == 0)
  }

  test ("Last modification returns zero when URL does not support operation") {
    class MockUrlConnection(url: URL) extends URLConnection(url) {
      override def connect(): Unit = {}
      override def getLastModified: Long = throw new UnsupportedOperationException
    }
    class MockUrlStreamHandler extends URLStreamHandler {
      override def openConnection(u: URL): URLConnection = { new MockUrlConnection(u) }
    }

    val mockUrl = new URL("mock", "123", 45, "/foo", new MockUrlStreamHandler())
    val entry = new TestBundleEntry(bundleMock, mockUrl, parentMock)
    assert(entry.lastModified == 0)
  }

  test ("Getting output stream is not implemented") {
    assertThrows[IOException] { createBundleEntry().output }
  }

  test ("Creating file is not implemented") {
    assertThrows[UnsupportedOperationException] { createBundleEntry().create }
  }

  test ("Deleting file is not implemented") {
    assertThrows[UnsupportedOperationException] { createBundleEntry().delete }
  }

  test ("Looking up non-existing file") {
    assert(createBundleEntry().lookupNameUnchecked("foobar", false) == NoFile)
  }

  test ("Looking up existing file") {
    val subEntry = new TestBundleEntry(bundleMock, new URL("file://123/foobar"), parentMock)
    val entry = new TestBundleEntry(bundleMock, baseUrl, parentMock, subEntry)
    assert(entry.lookupNameUnchecked("foobar", false) == subEntry)
  }
}
