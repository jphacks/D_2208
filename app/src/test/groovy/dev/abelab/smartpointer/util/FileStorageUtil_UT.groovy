package dev.abelab.smartpointer.util

import dev.abelab.smartpointer.AbstractSpecification
import dev.abelab.smartpointer.domain.model.FileModel
import dev.abelab.smartpointer.helper.RandomHelper
import dev.abelab.smartpointer.property.GcpProperty
import org.springframework.beans.factory.annotation.Autowired

/**
 * FileStorageUtilの単体テスト
 */
class FileStorageUtil_UT extends AbstractSpecification {

    @Autowired
    FileStorageUtil sut

    @Autowired
    GcpProperty gcpProperty

    def "upload: GCP送信が抑制されている場合、アップロードされない"() {
        given:
        final fileModel = RandomHelper.mock(FileModel)

        when:
        this.sut.upload(fileModel)

        then:
        noExceptionThrown()
    }

}
