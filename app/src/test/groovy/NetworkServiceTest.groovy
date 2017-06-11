package com.wassa.candidate

import android.test.mock.MockContext
import com.smora.arch.webserver.WebServer
import com.wassa.candidate.model.NetworkService
import com.wassa.candidate.model.Place
import spock.lang.Ignore

/**
 * @author khadir
 * @since 11/06/2017
 */
@Ignore
//mock context issue
class NetworkServiceTest extends spock.lang.Specification {
    WebServer server
    MockContext context
    NetworkService service

    def setup() {
        context = new MockContext()
        server = WebServer.with(context).start()
        service = NetworkService.getInstance(context)
    }


    def "we request the places list from the webservice"() {
        given:
        def list = new ArrayList()
        def callback = new NetworkService.ApiPlacesResult() {
            @Override
            void success(List result) {
                list = result
            }

            @Override
            void error(int code, String message) {
            }
        }
        when:
        service.getPlacesList(callback)
        then: "list must be populated"
        !list.isEmpty()
    }

    def "we request the place info from the webservice"() {
        given:
        def id = "0b22f395-983a-40fa-a1a3-0e316092d747"
        def image_id = "be1f42fb-af0c-4800-940b-417526f36121"
        def place
        def image
        def infoCallback = new NetworkService.ApiPlaceInfoResult<>() {

            @Override
            void success(Place res) {
                place = res
            }

            @Override
            void error(int code, String message) {

            }
        }
        def imageCallback = new NetworkService.ApiPlaceImageResult<>() {
            @Override
            void success(File res) {
                image = res
            }

            @Override
            void error(int code, String message) {

            }
        }
        when:
        service.getPlaceInfo(id, infoCallback)
        service.getPlaceImage(image_id, imageCallback)
        then: "we get the place and the image file"
        place != null
        image != null

    }
}
